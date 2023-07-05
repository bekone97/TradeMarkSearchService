package by.intexsoft.trademarksearchservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TradeMarkDto {

    private String applicationNumber;
    private String kindMark;
    private String markFeature;
    private String goodsServicesDescription;
    private String content;
    private String markCurrentStatusCode;
    private LocalDate markCurrentStatusDate;
}
