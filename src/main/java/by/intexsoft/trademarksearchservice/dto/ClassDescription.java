package by.intexsoft.trademarksearchservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClassDescription {
    @JacksonXmlProperty(localName = "GoodsServicesDescription")
    public List<GoodsServicesDescription> goodsServicesDescription;
}
