package by.intexsoft.trademarksearchservice.service.impl;

import by.intexsoft.trademarksearchservice.dto.TradeMarkDto;
import by.intexsoft.trademarksearchservice.dto.TradeMarkDtoOut;
import by.intexsoft.trademarksearchservice.exception.ResourceNotFoundException;
import by.intexsoft.trademarksearchservice.mapper.TradeMarkMapper;
import by.intexsoft.trademarksearchservice.model.TradeMark;
import by.intexsoft.trademarksearchservice.repository.TradeMarkRepository;
import by.intexsoft.trademarksearchservice.service.TradeMarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeMarkServiceImpl implements TradeMarkService {

    private final TradeMarkRepository tradeMarkRepository;
    private final TradeMarkMapper tradeMarkMapper;

    @Override
    public Page<TradeMarkDtoOut> findAll(Pageable pageable) {
        log.debug("Find all trademarks");
        Page<TradeMark> all = tradeMarkRepository.findAllByMarkFeature("Word", pageable);
        return all.map(tradeMarkMapper::convertToDtoOut);
    }

    @Override
    public Page<TradeMarkDtoOut> findBySearchValue(String searchValue, Pageable pageable) {
        log.debug("Find all trademarks by searchValue {} and markFeature : {}", searchValue, "Word");
        TextCriteria textCriteria = new TextCriteria().matchingAny((searchValue.split(" ")));
        return tradeMarkRepository.findAllByMarkFeature("Word", textCriteria, pageable)
                .map(tradeMarkMapper::convertToDtoOut);
    }

    @Override
    public TradeMarkDtoOut getByApplicationNumber(String applicationNumber) {
        log.debug("Find trademark by application number : {}", applicationNumber);
        return tradeMarkRepository.findById(applicationNumber)
                .map(tradeMarkMapper::convertToDtoOut)
                .orElseThrow(() -> new ResourceNotFoundException(TradeMark.class, "applicationNumber", applicationNumber));
    }

    @Override
    @Transactional
    public TradeMarkDtoOut save(TradeMarkDto tradeMarkDto) {
        log.debug("Save new trademark");
        TradeMark tradeMark = tradeMarkRepository.save(tradeMarkMapper.convert(tradeMarkDto));
        return tradeMarkMapper.convertToDtoOut(tradeMark);
    }
}
