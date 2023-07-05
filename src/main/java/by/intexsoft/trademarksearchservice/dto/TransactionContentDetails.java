package by.intexsoft.trademarksearchservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class TransactionContentDetails {
    @JacksonXmlProperty(localName = "TransactionIdentifier")
    private long transactionIdentifier;
    @JacksonXmlProperty(localName = "TransactionData")
    private TransactionData transactionData;
}
