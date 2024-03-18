package com.example.loanservice.service;

import com.example.loanservice.model.Loan;

import java.util.List;

public interface LoanService {
    Loan createLoan(Loan loan);

    Loan getLoanById(Long id);

    List<Loan> getAllLoans();

    void deleteLoan(Long id);

    Loan updateLoan(Long id, Loan loanDetails);
}
