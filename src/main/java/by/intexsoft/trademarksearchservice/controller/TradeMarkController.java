package by.intexsoft.trademarksearchservice.controller;

import by.intexsoft.trademarksearchservice.dto.TradeMarkDtoOut;
import by.intexsoft.trademarksearchservice.handler.TradeMarkSearchApiErrorResponse;
import by.intexsoft.trademarksearchservice.service.TradeMarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static by.intexsoft.trademarksearchservice.utils.ConstantUtil.SwaggerResponse.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trademarks")
@Slf4j
@Validated
public class TradeMarkController {

    private final TradeMarkService tradeMarkService;

    @Operation(summary = "Returns all trademarks or trademarks which are suitable for search value")
    @ApiResponses({
            @ApiResponse(responseCode = RESPONSE_CODE_OK, description = RESPONSE_DESCRIPTION_OK,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TradeMarkDtoOut.class))})
    })
    @GetMapping
    @ResponseStatus(OK)
    public Page<TradeMarkDtoOut> findTradeMarks(@Parameter(description = "Text value for searching", example = "12435")
                                                @RequestParam(required = false) String searchValue,
                                                Pageable pageable) {
        log.info("Find trade marks with search value : {}", searchValue);
        return searchValue == null ?
                tradeMarkService.findAll(pageable) :
                tradeMarkService.findBySearchValue(searchValue, pageable);
    }

    @Operation(summary = "Returns a trademark by its application id")
    @ApiResponses({
            @ApiResponse(responseCode = RESPONSE_CODE_OK, description = RESPONSE_DESCRIPTION_OK,
                    content = {@Content(mediaType = APPLICATION_JSON,
                            schema = @Schema(implementation = TradeMarkDtoOut.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_NOT_FOUNDED, description = RESPONSE_DESCRIPTION_NOT_FOUNDED,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TradeMarkSearchApiErrorResponse.class))})
    })
    @GetMapping(value = "/{applicationNumber}")
    @ResponseStatus(OK)
    public TradeMarkDtoOut findTradeMarkById(@Parameter(description = "Application id to be searched", required = true, example = "11")
                                             @PathVariable
                                             @NotBlank(message = "${trademark.validation.applicationNumber.notBlank}")
                                             @Size(max = 14, message = "${trademark.validation.applicationNumber.size.max}")
                                             String applicationNumber) {
        log.info("Get trade mark by id ");
        return tradeMarkService.getByApplicationNumber(applicationNumber);
    }

}
