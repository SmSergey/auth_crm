package com.exceedit.auth.web.controller.advices;

import com.exceedit.auth.web.controller.api.response.ApiResponse;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ApiExceptionHandler extends ExceptionHandlerExceptionResolver {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleBadMediaType(HttpMediaTypeNotSupportedException ex, WebRequest req) {
        return new ApiResponse()
                .setStatus(415)
                .setMessage(ex.getMessage()).build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex, WebRequest req) {
        return new ApiResponse()
                .setStatus(400)
                .setMessage(ex.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMissingParams(MethodArgumentNotValidException ex, WebRequest req) {
        val errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ApiResponse()
                .setStatus(400)
                .setMessage(errorMessage).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMissingParams(HttpMessageNotReadableException ex, WebRequest req, HttpServletRequest request) {
        logger.error(request.getRequestURI());
        logger.error(ex.getMessage());
        return new ApiResponse()
                .setStatus(400)
                .setMessage("request boyd is not readable").build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleMissingParams(Exception ex, WebRequest req) {
        logger.error(ex.getClass());
        return new ApiResponse()
                .setStatus(500)
                .setMessage("internal error").build();
    }
}
