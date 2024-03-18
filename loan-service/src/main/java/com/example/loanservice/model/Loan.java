package com.example.loanservice.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lender_id")
    private Long lenderId;
    @Transient
    private LenderDto lenderDto;
    @Transient
    private UserDto userLenderDto;

    @Column(name = "borrower_id")
    private Long borrowerId;
    @Transient
    private BorrowerDto borrowerDto;
    @Transient
    private UserDto userBorrowerDto;

    @Column(name = "loan_amount")
    private double loanAmount;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "issuance_date")
    private LocalDate issuanceDate;

    @Column(name = "term_in_months")
    private int termInMonths;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    public LenderDto getLenderDto() {
        return lenderDto;
    }

    public void setLenderDto(LenderDto lenderDto) {
        this.lenderDto = lenderDto;
    }

    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public BorrowerDto getBorrowerDto() {
        return borrowerDto;
    }

    public void setBorrowerDto(BorrowerDto borrowerDto) {
        this.borrowerDto = borrowerDto;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(LocalDate issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public int getTermInMonths() {
        return termInMonths;
    }

    public void setTermInMonths(int termInMonths) {
        this.termInMonths = termInMonths;
    }

    public UserDto getUserLenderDto() {
        return userLenderDto;
    }

    public void setUserLenderDto(UserDto userLenderDto) {
        this.userLenderDto = userLenderDto;
    }

    public UserDto getUserBorrowerDto() {
        return userBorrowerDto;
    }

    public void setUserBorrowerDto(UserDto userBorrowerDto) {
        this.userBorrowerDto = userBorrowerDto;
    }
}
