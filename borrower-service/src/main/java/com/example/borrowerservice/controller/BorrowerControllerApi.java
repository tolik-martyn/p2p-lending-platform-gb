package com.example.borrowerservice.controller;

import com.example.borrowerservice.model.Borrower;
import com.example.borrowerservice.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerControllerApi {

    private final BorrowerService borrowerService;

    @Autowired
    public BorrowerControllerApi(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @GetMapping
    public List<Borrower> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }

    @GetMapping("/{borrowerId}")
    public Borrower getBorrowerById(@PathVariable("borrowerId") Long id) {
        return borrowerService.getBorrowerById(id);
    }
}
