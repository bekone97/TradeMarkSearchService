package by.intexsoft.trademarksearchservice.controller;

import by.intexsoft.trademarksearchservice.dto.TradeMarkDtoOut;
import by.intexsoft.trademarksearchservice.exception.ResourceNotFoundException;
import by.intexsoft.trademarksearchservice.model.TradeMark;
import by.intexsoft.trademarksearchservice.service.TradeMarkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TradeMarkController.class)
class TradeMarkControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TradeMarkService tradeMarkService;

    private TradeMarkDtoOut tradeMarkDtoOut;

    @BeforeEach
    void setUp() {
        tradeMarkDtoOut = TradeMarkDtoOut.builder()
                .applicationNumber("12345")
                .markCurrentStatusDate(LocalDate.parse("2022-05-05"))
                .markCurrentStatusCode("Resolved")
                .kindMark("someMark")
                .markFeature("Word")
                .build();
    }

    @AfterEach
    void tearDown() {
        tradeMarkDtoOut = null;
    }

    @Test
    @SneakyThrows
    void findTradeMarks() {
        List<TradeMarkDtoOut> tradeMarks = List.of(tradeMarkDtoOut);
        Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());
        Page<TradeMarkDtoOut> expected = new PageImpl<>(tradeMarks, pageable, 1);
        when(tradeMarkService.findAll(pageable)).thenReturn(expected);

        String actual = mockMvc.perform(get("/trademarks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
        verify(tradeMarkService).findAll(pageable);
    }

    @Test
    @SneakyThrows
    void findTradeMarksWithSearchValue() {
        List<TradeMarkDtoOut> tradeMarks = List.of(tradeMarkDtoOut);
        Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());
        Page<TradeMarkDtoOut> expected = new PageImpl<>(tradeMarks, pageable, 1);
        String searchValue = "hello";
        when(tradeMarkService.findBySearchValue(searchValue, pageable)).thenReturn(expected);

        String actual = mockMvc.perform(get("/trademarks")
                        .param("searchValue", searchValue)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
        verify(tradeMarkService, never()).findAll(pageable);
        verify(tradeMarkService).findBySearchValue(searchValue, pageable);
    }


    @Test
    @SneakyThrows
    void findTradeMarkById() {
        var expected = tradeMarkDtoOut;
        when(tradeMarkService.getByApplicationNumber(tradeMarkDtoOut.getApplicationNumber()))
                .thenReturn(tradeMarkDtoOut);

        String actual = mockMvc.perform(get("/trademarks/{applicationNumber}", tradeMarkDtoOut.getApplicationNumber())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
        verify(tradeMarkService).getByApplicationNumber(tradeMarkDtoOut.getApplicationNumber());
    }

    @Test
    @SneakyThrows
    void findTradeMarkByIdFail() {
        String expectedMessage = "TradeMark wasn't found by";
        when(tradeMarkService.getByApplicationNumber(tradeMarkDtoOut.getApplicationNumber()))
                .thenThrow(new ResourceNotFoundException(TradeMark.class, "applicationNumber", tradeMarkDtoOut.getApplicationNumber()));

        mockMvc.perform(get("/trademarks/{applicationNumber}", tradeMarkDtoOut.getApplicationNumber())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));

        verify(tradeMarkService).getByApplicationNumber(tradeMarkDtoOut.getApplicationNumber());
    }

    @Test
    @SneakyThrows
    void findTradeMarkByConstraintViolationFail() {
        String expectedMessage = "${trademark.validation.applicationNumber.size.max}";
        String timestampFormat = "2000-04-05 11:12:13";
        LocalDateTime timestamp = LocalDateTime.parse("2000-04-05T11:12:13");
        try (MockedStatic<LocalDateTime> time = Mockito.mockStatic(LocalDateTime.class)) {
            time.when(LocalDateTime::now).thenReturn(timestamp);
            mockMvc.perform(get("/trademarks/1238190283903583904580349583490583490583453")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                    .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)))
                    .andExpect(jsonPath("$['timestamp']", is(timestampFormat)))
                    .andExpect(jsonPath("$['message']", is("method-arguments-notvalid-error")));
        }

        verify(tradeMarkService, never()).getByApplicationNumber(tradeMarkDtoOut.getApplicationNumber());
    }
}