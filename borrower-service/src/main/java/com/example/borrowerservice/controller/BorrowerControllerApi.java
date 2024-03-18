package com.example.borrowerservice.controller;

import com.example.borrowerservice.model.Borrower;
import com.example.borrowerservice.service.BorrowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
@Tag(name = "Borrower API Controller", description = "Endpoints for borrower management via API")
public class BorrowerControllerApi {

    private final BorrowerService borrowerService;

    @Autowired
    public BorrowerControllerApi(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @GetMapping
    @Operation(summary = "Get all borrowers", description = "Returns a list of all borrowers.")
    public List<Borrower> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }

    @GetMapping("/{borrowerId}")
    @Operation(summary = "Get borrower by ID", description = "Returns details of a borrower based on the provided ID.")
    public Borrower getBorrowerById(@PathVariable("borrowerId") Long id) {
        return borrowerService.getBorrowerById(id);
    }
}
