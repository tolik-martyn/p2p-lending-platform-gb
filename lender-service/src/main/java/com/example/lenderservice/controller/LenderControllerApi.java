package com.example.lenderservice.controller;

import com.example.lenderservice.model.Lender;
import com.example.lenderservice.service.LenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lenders")
@Tag(name = "Lender API Controller", description = "Endpoints for lender management via API")
public class LenderControllerApi {

    private final LenderService lenderService;

    @Autowired
    public LenderControllerApi(LenderService lenderService) {
        this.lenderService = lenderService;
    }

    @GetMapping
    @Operation(summary = "Get all lenders", description = "Returns a list of all lenders.")
    public List<Lender> getAllLenders() {
        return lenderService.getAllLenders();
    }

    @GetMapping("/{lenderId}")
    @Operation(summary = "Get lender by ID", description = "Returns details of a lender based on the provided ID.")
    public Lender getLenderById(@PathVariable("lenderId") Long id) {
        return lenderService.getLenderById(id);
    }
}
