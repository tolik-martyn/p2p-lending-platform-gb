package com.example.lenderservice.controller;

import com.example.lenderservice.model.Lender;
import com.example.lenderservice.service.LenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lenders")
public class LenderControllerApi {

    private final LenderService lenderService;

    @Autowired
    public LenderControllerApi(LenderService lenderService) {
        this.lenderService = lenderService;
    }

    @GetMapping
    public List<Lender> getAllLenders() {
        return lenderService.getAllLenders();
    }

    @GetMapping("/{lenderId}")
    public Lender getLenderById(@PathVariable("lenderId") Long id) {
        return lenderService.getLenderById(id);
    }
}
