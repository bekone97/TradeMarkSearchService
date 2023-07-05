package by.intexsoft.trademarksearchservice.service.impl;

import by.intexsoft.trademarksearchservice.dto.TradeMarkDto;
import by.intexsoft.trademarksearchservice.dto.TradeMarkDtoIn;
import by.intexsoft.trademarksearchservice.dto.TransactionDto;
import by.intexsoft.trademarksearchservice.exception.WrongInitDataException;
import by.intexsoft.trademarksearchservice.mapper.TradeMarkMapper;
import by.intexsoft.trademarksearchservice.service.DataLoaderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DataLoaderServiceServiceImpl implements DataLoaderService {
    private final TradeMarkMapper tradeMarkMapper;
    private final XmlMapper xmlMapper;

    private final String filePath;

    public DataLoaderServiceServiceImpl(TradeMarkMapper tradeMarkMapper,
                                        XmlMapper xmlMapper,
                                        @Value("${init.trademark.url}") String filePath) {
        this.tradeMarkMapper = tradeMarkMapper;
        this.xmlMapper = xmlMapper;
        this.filePath = filePath;
    }

    @Override
    public List<TradeMarkDto> loadTradeMarkInitData() {
        log.info("Loading init data");
        try {
            return Arrays.stream(ResourceUtils.getFile(filePath).listFiles())
                    .map(this::copyFileDataToString)
                    .map(this::parseTradeMarkInitData)
                    .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new WrongInitDataException(e.getMessage());
        }
    }

    private String copyFileDataToString(File file) {
        try {
            return FileCopyUtils.copyToString(new InputStreamReader(new FileInputStream(file),
                    Charset.defaultCharset()));
        } catch (IOException e) {
            throw new WrongInitDataException(e.getMessage());
        }
    }


    private TradeMarkDto parseTradeMarkInitData(String content) {
        log.info("Try to parse init data");
        try {
            TradeMarkDtoIn tradeMarkDtoIn = xmlMapper.readValue(content, TransactionDto.class).getTransactionBody()
                    .getTransactionContentDetails().getTransactionData().getTradeMarkDetails().getTradeMark();
            return tradeMarkMapper.convert(tradeMarkDtoIn,
                    content);
        } catch (JsonProcessingException e) {
            throw new WrongInitDataException(e.getMessage());
        }
    }
}
