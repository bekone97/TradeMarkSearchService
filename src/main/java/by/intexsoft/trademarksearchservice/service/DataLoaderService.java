package by.intexsoft.trademarksearchservice.service;

import by.intexsoft.trademarksearchservice.dto.TradeMarkDto;

import java.util.List;

public interface DataLoaderService {
    List<TradeMarkDto> loadTradeMarkInitData();
}
