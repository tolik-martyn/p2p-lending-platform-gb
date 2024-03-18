package com.example.lenderservice.service;

import com.example.lenderservice.model.Lender;

import java.util.List;

public interface LenderService {
    Lender createLender(Lender lender);

    Lender getLenderById(Long id);

    List<Lender> getAllLenders();

    void deleteLender(Long id);

    Lender updateLender(Long id, Lender lenderDetails);
}
