package by.intexsoft.trademarksearchservice.service;

import by.intexsoft.trademarksearchservice.dto.TradeMarkDto;
import by.intexsoft.trademarksearchservice.dto.TradeMarkDtoOut;
import by.intexsoft.trademarksearchservice.exception.ResourceNotFoundException;
import by.intexsoft.trademarksearchservice.mapper.TradeMarkMapper;
import by.intexsoft.trademarksearchservice.model.TradeMark;
import by.intexsoft.trademarksearchservice.repository.TradeMarkRepository;
import by.intexsoft.trademarksearchservice.service.impl.TradeMarkServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TradeMarkServiceTest {

    public static final String MARK_FEATURE = "Word";
    @InjectMocks
    private TradeMarkServiceImpl tradeMarkService;

    @Mock
    private TradeMarkRepository tradeMarkRepository;

    @Mock
    private TradeMarkMapper tradeMarkMapper;

    TradeMarkDto tradeMarkDto;
    TradeMark tradeMark;
    TradeMarkDtoOut tradeMarkDtoOut;

    @BeforeEach
    void setUp() {
        tradeMarkDto = TradeMarkDto.builder()
                .applicationNumber("23141231")
                .markCurrentStatusCode("Resolved")
                .markCurrentStatusDate(LocalDate.parse("2022-05-05"))
                .markFeature("Word")
                .goodsServicesDescription("Some description")
                .content("Some content")
                .build();

        tradeMark = TradeMark.builder()
                .applicationNumber(tradeMarkDto.getApplicationNumber())
                .markCurrentStatusCode(tradeMarkDto.getMarkCurrentStatusCode())
                .markCurrentStatusDate(tradeMarkDto.getMarkCurrentStatusDate())
                .markFeature(tradeMarkDto.getMarkFeature())
                .goodsServicesDescription(tradeMarkDto.getGoodsServicesDescription())
                .content(tradeMarkDto.getContent())
                .build();

        tradeMarkDtoOut = TradeMarkDtoOut.builder()
                .markFeature(tradeMark.getMarkFeature())
                .kindMark(tradeMark.getKindMark())
                .applicationNumber(tradeMark.getApplicationNumber())
                .markCurrentStatusCode(tradeMark.getMarkCurrentStatusCode())
                .markCurrentStatusDate(tradeMark.getMarkCurrentStatusDate())
                .build();
    }

    @AfterEach
    void tearDown() {
        tradeMarkDtoOut = null;
        tradeMark = null;
        tradeMarkDto = null;
    }


    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<TradeMark> tradeMarks = new PageImpl<>(List.of(tradeMark), pageable, 1);
        PageImpl<TradeMarkDtoOut> expected = new PageImpl<>(List.of(tradeMarkDtoOut), pageable, 1);
        when(tradeMarkRepository.findAllByMarkFeature(MARK_FEATURE, pageable)).thenReturn(tradeMarks);
        when(tradeMarkMapper.convertToDtoOut(tradeMark)).thenReturn(tradeMarkDtoOut);

        var actual = tradeMarkService.findAll(pageable);

        assertEquals(expected, actual);
        verify(tradeMarkRepository).findAllByMarkFeature(MARK_FEATURE, pageable);
        verify(tradeMarkMapper).convertToDtoOut(tradeMark);
    }

    @Test
    void findBySearchValue() {
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<TradeMark> tradeMarksPage = new PageImpl<>(List.of(tradeMark), pageable, 1);
        PageImpl<TradeMarkDtoOut> expected = new PageImpl<>(List.of(tradeMarkDtoOut), pageable, 1);
        var searchValue = "searchValue";

        TextCriteria textCriteria = new TextCriteria().matchingAny((searchValue.split(" ")));
        when(tradeMarkRepository.findAllByMarkFeature(MARK_FEATURE, textCriteria, pageable))
                .thenReturn(tradeMarksPage);
        when(tradeMarkMapper.convertToDtoOut(tradeMark)).thenReturn(tradeMarkDtoOut);

        var actual = tradeMarkService.findBySearchValue(searchValue, pageable);

        assertEquals(expected, actual);
        verify(tradeMarkRepository).findAllByMarkFeature(MARK_FEATURE, textCriteria, pageable);
        verify(tradeMarkMapper).convertToDtoOut(tradeMark);
    }

    @Test
    void getByApplicationNumber() {
        var expected = tradeMarkDtoOut;
        when(tradeMarkRepository.findById(tradeMark.getApplicationNumber()))
                .thenReturn(Optional.of(tradeMark));
        when(tradeMarkMapper.convertToDtoOut(tradeMark)).thenReturn(expected);

        var actual = tradeMarkService.getByApplicationNumber(tradeMarkDto.getApplicationNumber());

        assertEquals(expected, actual);
        verify(tradeMarkRepository).findById(tradeMark.getApplicationNumber());
        verify(tradeMarkMapper).convertToDtoOut(tradeMark);
    }

    @Test
    void getByApplicationNumberFail() {
        var expected = tradeMarkDtoOut;
        when(tradeMarkRepository.findById(tradeMark.getApplicationNumber()))
                .thenReturn(Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> tradeMarkService.getByApplicationNumber(tradeMarkDto.getApplicationNumber()));

        assertTrue(exception.getMessage().contains("TradeMark wasn't found by"));
        verify(tradeMarkRepository).findById(tradeMark.getApplicationNumber());
        verify(tradeMarkMapper, never()).convertToDtoOut(tradeMark);
    }

    @Test
    void save() {
        var expected = tradeMarkDtoOut;
        when(tradeMarkRepository.save(tradeMark)).thenReturn(tradeMark);
        when(tradeMarkMapper.convertToDtoOut(tradeMark)).thenReturn(tradeMarkDtoOut);
        when(tradeMarkMapper.convert(tradeMarkDto)).thenReturn(tradeMark);

        var actual = tradeMarkService.save(tradeMarkDto);

        assertEquals(expected,actual);
        verify(tradeMarkRepository).save(tradeMark);
        verify(tradeMarkMapper).convertToDtoOut(tradeMark);
        verify(tradeMarkMapper).convert(tradeMarkDto);
    }
}