package com.example.borrowerservice.exception;

public class BorrowerNotFoundException extends RuntimeException {
    public BorrowerNotFoundException(Long id) {
        super("Borrower not found with id: " + id);
    }
}
