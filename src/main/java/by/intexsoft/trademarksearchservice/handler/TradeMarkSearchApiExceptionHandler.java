package by.intexsoft.trademarksearchservice.handler;

import by.intexsoft.trademarksearchservice.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class TradeMarkSearchApiExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public TradeMarkSearchApiErrorResponse resourceNotFoundExceptionHandler(HttpServletRequest request,
                                                                            ResourceNotFoundException exception) {
        log.error("The {}. There is no object in database : {}.Url of request : {}",
                exception.getClass().getSimpleName(), exception.getMessage(), request.getRequestURL());
        return new TradeMarkSearchApiErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ValidationErrorResponse constraintViolationExceptionHandler(HttpServletRequest request,
                                                                       ConstraintViolationException exception) {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse("method-arguments-notvalid-error",
                exception.getConstraintViolations().stream()
                        .map(violation -> new ValidationMessage(violation.getPropertyPath().toString(), violation.getMessage()))
                        .collect(Collectors.toList())
        );
        log.warn("The {}. Validation messages : {} .Url of request : {}",
                exception.getClass().getSimpleName(), validationErrorResponse.getMessage(), request.getRequestURL());

        return validationErrorResponse;
    }
}
