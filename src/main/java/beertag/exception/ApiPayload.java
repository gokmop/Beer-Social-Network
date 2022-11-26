package beertag.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiPayload {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;

    private int statusCode;
    private HttpStatus status;
    private String message;
    private String debugMessage;

    private Set<ApiSubError> subErrors;

    private ApiPayload() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiPayload(HttpStatus status) {
        this();
        this.message = "Unexpected error!";
        this.status = status;
        this.statusCode = status.value();
    }

    public ApiPayload(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
        this.statusCode = status.value();
    }

    public ApiPayload(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = "Unexpected error!";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiPayload(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiPayload(HttpStatus status, Collection<ApiSubError> subErrors) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = "Validation failed!";
        this.subErrors = new HashSet<>(subErrors);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public Set<ApiSubError> getSubErrors() {
        return subErrors;
    }
}
