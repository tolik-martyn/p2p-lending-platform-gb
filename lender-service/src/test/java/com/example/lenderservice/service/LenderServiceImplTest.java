package com.example.lenderservice.service;

import com.example.lenderservice.exception.LenderNotFoundException;
import com.example.lenderservice.model.Lender;
import com.example.lenderservice.repository.LenderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("Lender Service Tests")
class LenderServiceImplTest {
    @InjectMocks
    private LenderServiceImpl lenderService;

    @Mock
    private LenderRepository lenderRepository;

    @Test
    @DisplayName("Create Lender Test")
    void createLender() {
        Lender lender = new Lender();

        when(lenderRepository.save(lender)).thenReturn(lender);

        Lender createdLender = lenderService.createLender(lender);

        assertEquals(lender, createdLender);
        Mockito.verify(lenderRepository, Mockito.times(1)).save(lender);
    }

    @Test
    @DisplayName("Get Lender By Id Test")
    void getLenderById() {
        Lender lender = new Lender();

        when(lenderRepository.findById(1L)).thenReturn(Optional.of(lender));

        Lender foundLender = lenderService.getLenderById(1L);

        assertEquals(lender, foundLender);
        Mockito.verify(lenderRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get Lender By Id Not Found Test")
    void getLenderByIdNotFound() {
        when(lenderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LenderNotFoundException.class, () -> lenderService.getLenderById(1L));
        Mockito.verify(lenderRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get All Lenders Test")
    void getAllLenders() {
        List<Lender> lenders = new ArrayList<>();

        when(lenderRepository.findAll()).thenReturn(lenders);

        List<Lender> foundLender = lenderService.getAllLenders();

        assertEquals(lenders, foundLender);
        Mockito.verify(lenderRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Delete Lender Test")
    void deleteLender() {
        lenderService.deleteLender(1L);
        Mockito.verify(lenderRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Update Lender Test")
    void updateLender() {
        Lender lender = new Lender();

        Lender lenderDetails = new Lender();
        lenderDetails.setUserId(777L);

        when(lenderRepository.findById(1L)).thenReturn(Optional.of(lender));
        when(lenderRepository.save(Mockito.any(Lender.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Lender updatedLender = lenderService.updateLender(1L, lenderDetails);

        assertEquals(lenderDetails.getUserId(), updatedLender.getUserId());
        Mockito.verify(lenderRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(lenderRepository, Mockito.times(1)).save(lender);
    }
}
