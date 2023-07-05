package by.intexsoft.trademarksearchservice.controller;

import by.intexsoft.trademarksearchservice.dto.TradeMarkDtoOut;
import by.intexsoft.trademarksearchservice.exception.ResourceNotFoundException;
import by.intexsoft.trademarksearchservice.initializer.DatabaseContainerInitializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TradeMarkControllerIntegrationTest extends DatabaseContainerInitializer {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private TradeMarkDtoOut tradeMarkDtoOut;

    @BeforeEach
    void setUp() {
        tradeMarkDtoOut = TradeMarkDtoOut.builder()
                .applicationNumber("018173410")
                .markFeature("Word")
                .kindMark("Individual")
                .markCurrentStatusCode("Registered")
                .markCurrentStatusDate(LocalDate.parse("2020-09-28"))
                .build();
    }

    @AfterEach
    void tearDown() {
        tradeMarkDtoOut = null;
    }

    @Test
    @Order(1)
    @SneakyThrows
    void findTradeMarks() {
        Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());
        List<TradeMarkDtoOut> tradeMarks = List.of(tradeMarkDtoOut);
        Page<TradeMarkDtoOut> expected = new PageImpl<>(tradeMarks, pageable, 1);

        String actual = mockMvc.perform(get("/trademarks"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
    }

    @Test
    @Order(2)
    @SneakyThrows
    void findTradeMarksWithSearchValue() {
        Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());
        List<TradeMarkDtoOut> tradeMarks = List.of(tradeMarkDtoOut);
        Page<TradeMarkDtoOut> expected = new PageImpl<>(tradeMarks, pageable, 1);

        String actual = mockMvc.perform(get("/trademarks")
                        .param("searchValue", "Essential"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
    }

    @Test
    @Order(3)
    @SneakyThrows
    void findTradeMarksWithWrongSearchValue() {
        Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());
        List<TradeMarkDtoOut> tradeMarks = Collections.emptyList();
        Page<TradeMarkDtoOut> expected = new PageImpl<>(tradeMarks, pageable, 0);

        String actual = mockMvc.perform(get("/trademarks")
                        .param("searchValue", "asdklmqwlkenqwlkenqlknklxclkzcklxzcxklzcmklcx"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
    }

    @Test
    @Order(4)
    @SneakyThrows
    void findTradeMarkById() {
        TradeMarkDtoOut expected = tradeMarkDtoOut;

        String actual = mockMvc.perform(get("/trademarks/{applicationNubmer}",
                        tradeMarkDtoOut.getApplicationNumber()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
    }

    @Test
    @Order(5)
    @SneakyThrows
    void findTradeMarkByWrongId() {
        String expectedMessage = "TradeMark wasn't found by";
        mockMvc.perform(get("/trademarks/{applicationNubmer}",
                        "12931931"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));
    }

    @Test
    @Order(6)
    @SneakyThrows
    void findTradeMarkByWrongInputId() {
        String expectedMessage = "${trademark.validation.applicationNumber.size.max}";
        mockMvc.perform(get("/trademarks/{applicationNubmer}",
                        "129319311231232132534252435435432543523234235"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));
    }
}