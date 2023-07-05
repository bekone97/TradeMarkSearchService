package by.intexsoft.trademarksearchservice.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TradeMarkSearchApiErrorResponse {
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;

    public TradeMarkSearchApiErrorResponse(String message) {
        this.message = message;
    }
}
