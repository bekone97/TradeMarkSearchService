package by.intexsoft.trademarksearchservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeMarkDtoOut {
    
    private String applicationNumber;
    private String kindMark;
    private String markFeature;
    private String markCurrentStatusCode;
    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDate markCurrentStatusDate;
}
