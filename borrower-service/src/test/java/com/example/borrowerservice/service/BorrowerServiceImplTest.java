package com.example.borrowerservice.service;

import com.example.borrowerservice.exception.BorrowerNotFoundException;
import com.example.borrowerservice.model.Borrower;
import com.example.borrowerservice.repository.BorrowerRepository;
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
@DisplayName("Borrower Service Tests")
class BorrowerServiceImplTest {
    @InjectMocks
    private BorrowerServiceImpl borrowerService;

    @Mock
    private BorrowerRepository borrowerRepository;

    @Test
    @DisplayName("Create Borrower Test")
    void createBorrower() {
        Borrower borrower = new Borrower();

        when(borrowerRepository.save(borrower)).thenReturn(borrower);

        Borrower createdBorrower = borrowerService.createBorrower(borrower);

        assertEquals(borrower, createdBorrower);
        Mockito.verify(borrowerRepository, Mockito.times(1)).save(borrower);
    }

    @Test
    @DisplayName("Get Borrower By Id Test")
    void getBorrowerById() {
        Borrower borrower = new Borrower();

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));

        Borrower foundBorrower = borrowerService.getBorrowerById(1L);

        assertEquals(borrower, foundBorrower);
        Mockito.verify(borrowerRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get Borrower By Id Not Found Test")
    void getBorrowerByIdNotFound() {
        when(borrowerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BorrowerNotFoundException.class, () -> borrowerService.getBorrowerById(1L));
        Mockito.verify(borrowerRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get All Borrowers Test")
    void getAllBorrowers() {
        List<Borrower> borrowers = new ArrayList<>();

        when(borrowerRepository.findAll()).thenReturn(borrowers);

        List<Borrower> foundBorrower = borrowerService.getAllBorrowers();

        assertEquals(borrowers, foundBorrower);
        Mockito.verify(borrowerRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Delete Borrower Test")
    void deleteBorrower() {
        borrowerService.deleteBorrower(1L);
        Mockito.verify(borrowerRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Update Borrower Test")
    void updateBorrower() {
        Borrower borrower = new Borrower();

        Borrower borrowerDetails = new Borrower();
        borrowerDetails.setUserId(777L);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(borrowerRepository.save(Mockito.any(Borrower.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Borrower updatedBorrower = borrowerService.updateBorrower(1L, borrowerDetails);

        assertEquals(borrowerDetails.getUserId(), updatedBorrower.getUserId());
        Mockito.verify(borrowerRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(borrowerRepository, Mockito.times(1)).save(borrower);
    }
}
