package beertag.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = {"beertag.controllers.rest"})
public class RestHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (
                    MethodArgumentNotValidException ex,
                    HttpHeaders headers,
                    HttpStatus status,
                    WebRequest request
            ) {
        ApiPayload apiPayload = new ApiPayload(BAD_REQUEST, getSubErrors(ex));
        return buildResponseEntity(apiPayload);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable
            (
                    HttpMessageNotReadableException ex,
                    HttpHeaders headers,
                    HttpStatus status,
                    WebRequest request
            ) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiPayload(status, error, ex));
    }

    @ExceptionHandler(value = {DuplicateEntityException.class})
    public ResponseEntity<Object> handleDuplicateEntity(
            DuplicateEntityException e) {
        ApiPayload apiPayload = new ApiPayload(CONFLICT, e.getMessage());
        return buildResponseEntity(apiPayload);
    }

    @ExceptionHandler(value = {UnsupportedOperationException.class})
    public ResponseEntity<Object> handleUnsupportedOperation(
            UnsupportedOperationException e) {
        ApiPayload apiPayload = new ApiPayload(FORBIDDEN, e.getMessage());
        return buildResponseEntity(apiPayload);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException e) {
        ApiPayload apiPayload = new ApiPayload(NOT_FOUND, e.getMessage());
        return buildResponseEntity(apiPayload);
    }

    @ExceptionHandler(value = {AccountNotActivatedException.class})
    public ResponseEntity<Object> handleAccountNotActivated(
            AccountNotActivatedException e) {
        ApiPayload apiPayload = new ApiPayload(FORBIDDEN, e.getMessage());
        return buildResponseEntity(apiPayload);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiPayload apiPayload) {
        return new ResponseEntity<>(apiPayload, apiPayload.getStatus());
    }

    private Collection<ApiSubError> getSubErrors(MethodArgumentNotValidException ex) {
        Set<ApiSubError> errors = new HashSet<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String objectName = error.getObjectName();
            String fieldName = ((FieldError) error).getField();

            String rejectedValue;
            Object value = ((FieldError) error).getRejectedValue();
            if (value == null) {
                rejectedValue = null;
            } else {
                rejectedValue = Objects.requireNonNull(value).toString();
            }
            String errorMessage = error.getDefaultMessage();
            errors.add(new ApiValidationError
                    (
                            objectName,
                            fieldName,
                            rejectedValue,
                            errorMessage
                    ));
        });
        return errors;
    }
}
