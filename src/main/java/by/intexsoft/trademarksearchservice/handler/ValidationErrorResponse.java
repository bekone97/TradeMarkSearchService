package by.intexsoft.trademarksearchservice.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationErrorResponse extends TradeMarkSearchApiErrorResponse {

    private List<ValidationMessage> validationMessages;

    public ValidationErrorResponse(String message, List<ValidationMessage> validationMessages) {
        super(message);
        this.validationMessages = validationMessages;
    }
}
