package by.intexsoft.trademarksearchservice.changeunit;

import io.changock.migration.api.annotations.NonLockGuarded;
import io.mongock.api.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.schema.JsonSchemaProperty;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.data.mongodb.core.validation.Validator;

@ChangeUnit(id = "2023-07-04-init-trademark-collection", order = "001", author = "miachyn.a")
@RequiredArgsConstructor
@Slf4j
@NonLockGuarded
public class InitTradeMarkCollection {
    private final MongoTemplate mongoTemplate;

    @BeforeExecution
    public void beforeExecution() {
        mongoTemplate.createCollection("trade_mark", CollectionOptions.empty()
                .validator(Validator.schema(MongoJsonSchema.builder()
                        .properties(
                                JsonSchemaProperty.string("applicationNumber"),
                                JsonSchemaProperty.string("kindMark"),
                                JsonSchemaProperty.string("markFeature"),
                                JsonSchemaProperty.string("goodsServicesDescription"),
                                JsonSchemaProperty.date("markCurrentStatusDate"),
                                JsonSchemaProperty.string("markCurrentStatusCode"),
                                JsonSchemaProperty.string("content")
                        )
                        .build())));

    }

    @Execution
    public void changeSet() {
// Do nothing
    }

    @RollbackBeforeExecution
    public void rollbackBefore() {
        mongoTemplate.dropCollection("trade_mark");
    }

    @RollbackExecution
    public void rollback() {
        mongoTemplate.findAllAndRemove(new Query(), "trade_mark");
    }

}
