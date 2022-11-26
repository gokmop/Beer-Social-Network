package beertag.exception;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = {"beertag.controllers.mvc"})
public class MvcHandler {

    private Log logger = LogFactory.getLog(getClass());

    @ExceptionHandler(value = {DuplicateEntityException.class})
    public ModelAndView handleDuplicateEntity(
            DuplicateEntityException e) {
        return buildView(e, CONFLICT);
    }

    @ExceptionHandler(value = {UnsupportedOperationException.class})
    public ModelAndView handleUnsupportedOperation(
            UnsupportedOperationException e) {
        return buildView(e, FORBIDDEN);
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ModelAndView handleEntityNotFound(
            NoHandlerFoundException e) {
        return buildView(e, NOT_FOUND);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ModelAndView handleEntityNotFound(
            EntityNotFoundException e) {
        return buildView(e, NOT_FOUND);

    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ModelAndView handleRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException e) {
        logger.warn(e.getMessage(), e);
        return buildView(e, NOT_FOUND);

    }

    @ExceptionHandler(value = {AccountNotActivatedException.class})
    public ModelAndView handleAccountNotActivated(
            AccountNotActivatedException e) {
        return buildView(e, FORBIDDEN);
    }

   @ExceptionHandler(value = {RuntimeException.class})
   public ModelAndView handleUnhandled(
           RuntimeException e) {
       logger.warn(e.getMessage(), e);
       return buildView(e, INTERNAL_SERVER_ERROR);
   }

    private ModelAndView buildView(Exception e, HttpStatus status) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", status.value());
        modelAndView.addObject("codeTitle", status.getReasonPhrase());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        modelAndView.addObject("timeStamp", dateTimeFormatter.format(now));
        return modelAndView;
    }

}
