package com.example.ics;

import com.example.ics.dto.FieldError;
import com.example.ics.dto.ProductError;
import com.example.ics.dto.RestError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.ics.exception.ProductDetailsNotValidException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.beanutils.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericExceptionHandler {
    
    private final Logger logger = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @Autowired
    MessageSource messageSource;
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> methodNotSupported(HttpRequestMethodNotSupportedException e){
        logger.debug("methodNotSupported()");
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for(String s : e.getSupportedMethods()){
            builder.append(s + ",");
        }
        if(builder.length() > 1){
            builder.setLength(builder.length()-1);
        }
        builder.append(']');
        
        return response(HttpStatus.METHOD_NOT_ALLOWED, 405,"Supported methods are " + builder.toString() , e.getMessage(), "");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> processValidationError(MethodArgumentNotValidException ex) {
        logger.debug("processValidationError()");
        BindingResult result = ex.getBindingResult();
        return new ResponseEntity<>(processFieldError(result.getFieldErrors()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductDetailsNotValidException.class)
    public ResponseEntity<?> processProductError(ProductDetailsNotValidException e){
        logger.debug("processProductError: {}", e.getMessage());
        return new ResponseEntity<>(e.getProductErrors(), HttpStatus.BAD_REQUEST);
    }

    private List<FieldError> processFieldError(List<org.springframework.validation.FieldError> fieldErrors) {
        //NoSuchMessageException
        //messageSource.
        List<FieldError> errors = fieldErrors.stream()
                .map(error -> {
                    return new FieldError(error.getField(),
                            error.getRejectedValue(),
                            messageSource.getMessage(error.getCodes()[0], null,error.getDefaultMessage(), LocaleContextHolder.getLocale())
                    );
                })
                .collect(Collectors.toList());
        return errors;
    }

//    @ExceptionHandler
//    ResponseEntity<?> handleConflict(DataIntegrityViolationException e) {
//    	String cause = e.getRootCause().getMessage();
//    	if(cause.toLowerCase().contains("duplicate")){
//    		return response(HttpStatus.CONFLICT, 40901, "Duplicate Entry. Data with same name already exist in database.", e.getRootCause().getMessage(), "");
//    	}else if(cause.toLowerCase().contains("cannot delete")){
//    		return response(HttpStatus.CONFLICT, 40903, "Deletion restricted to prevent data inconsistency.", e.getRootCause().getMessage(), "");
//    	}
//        return response(HttpStatus.CONFLICT, 40900, "Operation cannot be performed. Integrity Constraint violated.", e.getRootCause().getMessage(), "");
//    }
    @ExceptionHandler
    ResponseEntity<?> handleException(Exception e) {
        logger.debug("handleException: {} \n {}",e , e.getMessage());
        if (e.getCause() instanceof CsvDataTypeMismatchException){
            logger.debug("error converting data type");
            CsvDataTypeMismatchException cause = (CsvDataTypeMismatchException) e.getCause();
            List<ProductError> errors = new ArrayList<>();
            long i = cause.getLineNumber();
            if (cause.getCause() instanceof ConversionException){
                ConversionException cause2 = (ConversionException) cause.getCause();
                String msg = cause2.getMessage();
                errors.add(new ProductError("",(int)(i+1),msg));
                return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
            }
            errors.add(new ProductError("",(int)(i+1),cause.getMessage()));
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);

        }
        e.printStackTrace();

        String msg = e.getMessage();
        String devMsg = "";
        do{
            devMsg += e.getMessage() + "\n";
            e = (Exception)e.getCause();
        }while(e != null);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, 500, msg, devMsg, "");
    }

    private ResponseEntity<RestError> response(HttpStatus status, int code, String msg) {
        return response(status, code, msg, "", "");
    }

    private ResponseEntity<RestError> response(HttpStatus status, int code, String msg, String devMsg, String moreInfo) {
        return new ResponseEntity<>(new RestError(status.value(), code, msg, devMsg, moreInfo), status);
    }

}
