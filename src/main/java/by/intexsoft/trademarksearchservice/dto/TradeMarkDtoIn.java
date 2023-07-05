package by.intexsoft.trademarksearchservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeMarkDtoIn {
    @JacksonXmlProperty(localName = "ApplicationNumber")
    private String applicationNumber;
    @JacksonXmlProperty(localName = "kindMark")
    public String kindMark;
    @JacksonXmlProperty(localName = "MarkFeature")
    public String markFeature;
    @JacksonXmlProperty(localName = "WordMarkSpecification")
    public WordMarkSpecification wordMarkSpecification;
    @JacksonXmlProperty(localName = "GoodsServicesDetails")
    private GoodsServicesDetails goodsServicesDetails;
    @JacksonXmlProperty(localName = "MarkCurrentStatusCode")
    private String markCurrentStatusCode;
    @JacksonXmlProperty(localName = "MarkCurrentStatusDate")
    private LocalDate markCurrentStatusDate;
}
