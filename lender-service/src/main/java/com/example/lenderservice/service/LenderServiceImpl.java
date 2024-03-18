package com.example.lenderservice.service;

import com.example.lenderservice.exception.LenderNotFoundException;
import com.example.lenderservice.model.Lender;
import com.example.lenderservice.repository.LenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LenderServiceImpl implements LenderService {

    private final LenderRepository lenderRepository;

    @Autowired
    public LenderServiceImpl(LenderRepository lenderRepository) {
        this.lenderRepository = lenderRepository;
    }


    @Override
    public Lender createLender(Lender lender) {
        return lenderRepository.save(lender);
    }

    @Override
    public Lender getLenderById(Long id) {
        return lenderRepository
                .findById(id)
                .orElseThrow(() -> new LenderNotFoundException(id));
    }

    @Override
    public List<Lender> getAllLenders() {
        List<Lender> lenders = lenderRepository.findAll();
        lenders.sort(Comparator.comparingLong(Lender::getId));
        return lenders;
    }

    @Override
    public void deleteLender(Long id) {
        lenderRepository.deleteById(id);
    }

    @Override
    public Lender updateLender(Long id, Lender lenderDetails) {
        Lender lender = getLenderById(id);
        lender.setUserId(lenderDetails.getUserId());
        return lenderRepository.save(lender);
    }
}
