package by.intexsoft.trademarksearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "trade_mark")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeMark {

    @Id
    private String applicationNumber;
    private String markFeature;
    private String kindMark;
    private String content;
    @TextIndexed
    private String markCurrentStatusCode;
    private LocalDate markCurrentStatusDate;
    @TextIndexed
    private String goodsServicesDescription;
}
