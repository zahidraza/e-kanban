package com.example.ics.dto;

/**
 * Created by razamd on 4/14/2017.
 */
public class ProductError {
    private String column;
    private int row;
    private String errorMessage;

    public ProductError() {
    }

    public ProductError(String column, int row, String errorMessage) {
        this.column = column;
        this.row = row;
        this.errorMessage = errorMessage;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
