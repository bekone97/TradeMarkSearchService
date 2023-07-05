package by.intexsoft.trademarksearchservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
public class GoodsServicesDescription {
    @JacksonXmlProperty(localName = "languageCode", isAttribute = true)
    private String languageCode;
    @JacksonXmlText
    private String value;
}
