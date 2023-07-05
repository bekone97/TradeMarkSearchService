package by.intexsoft.trademarksearchservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class GoodsServicesDetails {
    @JacksonXmlProperty(localName = "GoodsServices")
    private GoodsServices goodsServices;
}
