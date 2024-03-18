package com.example.borrowerservice.service;

import com.example.borrowerservice.model.Borrower;

import java.util.List;

public interface BorrowerService {
    Borrower createBorrower(Borrower borrower);

    Borrower getBorrowerById(Long id);

    List<Borrower> getAllBorrowers();

    void deleteBorrower(Long id);

    Borrower updateBorrower(Long id, Borrower borrowerDetails);
}
