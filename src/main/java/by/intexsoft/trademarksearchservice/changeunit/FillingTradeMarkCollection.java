package by.intexsoft.trademarksearchservice.changeunit;

import by.intexsoft.trademarksearchservice.model.TradeMark;
import by.intexsoft.trademarksearchservice.service.DataLoaderService;
import by.intexsoft.trademarksearchservice.service.TradeMarkService;
import io.changock.migration.api.annotations.NonLockGuarded;
import io.changock.migration.api.annotations.NonLockGuardedType;
import io.mongock.api.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;

@ChangeUnit(id = "2023-07-04-filling-trademark-collection", order = "002", author = "miachyn.a")
@RequiredArgsConstructor
@Slf4j

public class FillingTradeMarkCollection {
    private final MongoTemplate mongoTemplate;
    private final DataLoaderService dataLoaderService;

    @BeforeExecution
    public void beforeExecution() {
        TextIndexDefinition textIndexDefinition = new TextIndexDefinition.TextIndexDefinitionBuilder()
                .onField("goodsServicesDescription")
                .onField("markCurrentStatusCode")
                .build();
        mongoTemplate.indexOps(TradeMark.class).ensureIndex(textIndexDefinition);
    }

    @Execution
    public void changeSet(@NonLockGuarded(NonLockGuardedType.NONE) TradeMarkService tradeMarkService) {
        dataLoaderService.loadTradeMarkInitData()
                .forEach(tradeMarkService::save);

    }

    @RollbackBeforeExecution
    public void rollbackBefore() {
        mongoTemplate.dropCollection("trade_mark");
    }

    @RollbackExecution
    public void rollback() {
//        Do nothing
    }

}
