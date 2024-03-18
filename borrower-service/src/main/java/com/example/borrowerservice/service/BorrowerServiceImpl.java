package com.example.borrowerservice.service;

import com.example.borrowerservice.exception.BorrowerNotFoundException;
import com.example.borrowerservice.model.Borrower;
import com.example.borrowerservice.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Autowired
    public BorrowerServiceImpl(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    @Override
    public Borrower createBorrower(Borrower borrower) {
        return borrowerRepository.save(borrower);
    }

    @Override
    public Borrower getBorrowerById(Long id) {
        return borrowerRepository
                .findById(id)
                .orElseThrow(() -> new BorrowerNotFoundException(id));
    }

    @Override
    public List<Borrower> getAllBorrowers() {
        List<Borrower> borrowers = borrowerRepository.findAll();
        borrowers.sort(Comparator.comparingLong(Borrower::getId));
        return borrowers;
    }

    @Override
    public void deleteBorrower(Long id) {
        borrowerRepository.deleteById(id);
    }

    @Override
    public Borrower updateBorrower(Long id, Borrower borrowerDetails) {
        Borrower borrower = getBorrowerById(id);
        borrower.setUserId(borrowerDetails.getUserId());
        return borrowerRepository.save(borrower);
    }
}
