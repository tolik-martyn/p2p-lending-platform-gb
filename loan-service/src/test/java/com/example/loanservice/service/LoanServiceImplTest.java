package com.example.loanservice.service;

import com.example.loanservice.exception.LoanNotFoundException;
import com.example.loanservice.model.Loan;
import com.example.loanservice.repository.LoanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("Loan Service Tests")
class LoanServiceImplTest {
    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanRepository loanRepository;

    @Test
    @DisplayName("Create Loan Test")
    void createLoan() {
        Loan loan = new Loan();

        when(loanRepository.save(loan)).thenReturn(loan);

        Loan createdLoan = loanService.createLoan(loan);

        assertEquals(loan, createdLoan);
        Mockito.verify(loanRepository, Mockito.times(1)).save(loan);
    }

    @Test
    @DisplayName("Get Loan By Id Test")
    void getLoanById() {
        Loan loan = new Loan();

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Loan foundLoan = loanService.getLoanById(1L);

        assertEquals(loan, foundLoan);
        Mockito.verify(loanRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get Loan By Id Not Found Test")
    void getLoanByIdNotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class, () -> loanService.getLoanById(1L));
        Mockito.verify(loanRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get All Loans Test")
    void getAllLoans() {
        List<Loan> loans = new ArrayList<>();

        when(loanRepository.findAll()).thenReturn(loans);

        List<Loan> foundLoan = loanService.getAllLoans();

        assertEquals(loans, foundLoan);
        Mockito.verify(loanRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Delete Loan Test")
    void deleteLoan() {
        loanService.deleteLoan(1L);
        Mockito.verify(loanRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Update Loan Test")
    void updateLoan() {
        Loan loan = new Loan();

        Loan loanDetails = new Loan();
        loanDetails.setLenderId(10L);
        loanDetails.setBorrowerId(100L);
        loanDetails.setLoanAmount(10000.0);
        loanDetails.setInterestRate(10.0);
        loanDetails.setIssuanceDate(LocalDate.of(2024, 3, 21));
        loanDetails.setTermInMonths(10);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(Mockito.any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan updatedLoan = loanService.updateLoan(1L, loanDetails);

        assertEquals(loanDetails.getLenderId(), updatedLoan.getLenderId());
        assertEquals(loanDetails.getBorrowerId(), updatedLoan.getBorrowerId());
        assertEquals(loanDetails.getLoanAmount(), updatedLoan.getLoanAmount());
        assertEquals(loanDetails.getInterestRate(), updatedLoan.getInterestRate());
        assertEquals(loanDetails.getIssuanceDate(), updatedLoan.getIssuanceDate());
        assertEquals(loanDetails.getTermInMonths(), updatedLoan.getTermInMonths());

        Mockito.verify(loanRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(loanRepository, Mockito.times(1)).save(loan);
    }
}