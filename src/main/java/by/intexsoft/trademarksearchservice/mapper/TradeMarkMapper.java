package by.intexsoft.trademarksearchservice.mapper;

import by.intexsoft.trademarksearchservice.dto.*;
import by.intexsoft.trademarksearchservice.model.TradeMark;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TradeMarkMapper {

    @Mapping(source = "content", target = "content")
    TradeMark convert(TradeMarkDto tradeMarkDto);

    @Mapping(source = "applicationNumber", target = "applicationNumber")
    @Mapping(source = "markFeature", target = "markFeature")
    @Mapping(source = "kindMark", target = "kindMark")
    @Mapping(source = "markCurrentStatusCode", target = "markCurrentStatusCode")
    TradeMarkDtoOut convertToDtoOut(TradeMark tradeMark);

    @Mapping(source = "content", target = "content")
    @Mapping(source = "tradeMarkDtoIn.goodsServicesDetails", target = "goodsServicesDescription", qualifiedByName = "goodsServicesDetails")
    TradeMarkDto convert(TradeMarkDtoIn tradeMarkDtoIn, String content);

    @Named("goodsServicesDetails")
    default String goodsServicesDetails(GoodsServicesDetails goodsServicesDetails) {
        return goodsServicesDetails
                .getGoodsServices()
                .getClassDescriptionDetails()
                .getClassDescription().stream()
                .flatMap(description ->
                        description.getGoodsServicesDescription().stream()
                )
                .map(GoodsServicesDescription::getValue)
                .collect(Collectors.joining());
    }
}
