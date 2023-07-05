package by.intexsoft.trademarksearchservice.service;

import by.intexsoft.trademarksearchservice.dto.TradeMarkDto;
import by.intexsoft.trademarksearchservice.dto.TradeMarkDtoOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TradeMarkService {

    Page<TradeMarkDtoOut> findAll(Pageable pageable);

    Page<TradeMarkDtoOut> findBySearchValue(String searchValue, Pageable pageable);

    TradeMarkDtoOut getByApplicationNumber(String applicationId);

    TradeMarkDtoOut save(TradeMarkDto tradeMarkDto);
}
