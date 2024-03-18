package com.example.loanservice.service;

import com.example.loanservice.exception.LoanNotFoundException;
import com.example.loanservice.model.Loan;
import com.example.loanservice.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan createLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository
                .findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));
    }

    @Override
    public List<Loan> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        loans.sort(Comparator.comparingLong(Loan::getId));
        return loans;
    }

    @Override
    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    @Override
    public Loan updateLoan(Long id, Loan loanDetails) {
        Loan loan = getLoanById(id);
        loan.setLenderId(loanDetails.getLenderId());
        loan.setBorrowerId(loanDetails.getBorrowerId());
        loan.setLoanAmount(loanDetails.getLoanAmount());
        loan.setInterestRate(loanDetails.getInterestRate());
        loan.setIssuanceDate(loanDetails.getIssuanceDate());
        loan.setTermInMonths(loanDetails.getTermInMonths());
        return loanRepository.save(loan);
    }
}
