package com.example.loanservice.exception;

public class LenderNotFoundException extends RuntimeException {
    public LenderNotFoundException(Long id) {
        super("Lender not found with id: " + id);
    }
}
