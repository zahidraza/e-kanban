package com.example.ekanban.exception;

import com.example.ekanban.dto.ProductError;

import java.util.List;

/**
 * Created by razamd on 4/14/2017.
 */
public class ProductDetailsNotValidException extends RuntimeException {
    private List<ProductError> productErrors;

    public ProductDetailsNotValidException(String message, List<ProductError> productErrors) {
        super(message);
        this.productErrors = productErrors;
    }

    public ProductDetailsNotValidException(String message, Throwable cause, List<ProductError> productErrors) {
        super(message, cause);
        this.productErrors = productErrors;
    }

    public ProductDetailsNotValidException(Throwable cause, List<ProductError> productErrors) {
        super(cause);
        this.productErrors = productErrors;
    }

    public ProductDetailsNotValidException(String message) {
        super(message);
    }

    public ProductDetailsNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductDetailsNotValidException(Throwable cause) {
        super(cause);
    }

    public List<ProductError> getProductErrors() {
        return productErrors;
    }

    public void setProductErrors(List<ProductError> productErrors) {
        this.productErrors = productErrors;
    }
}
