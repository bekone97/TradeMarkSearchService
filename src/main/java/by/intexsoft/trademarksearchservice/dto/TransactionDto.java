package by.intexsoft.trademarksearchservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "Transaction", namespace = "http://euipo.europa.eu/trademark/data")
public class TransactionDto {
    @JacksonXmlProperty(localName = "TradeMarkTransactionBody")
    private TradeMarkTransactionBody transactionBody;


}
