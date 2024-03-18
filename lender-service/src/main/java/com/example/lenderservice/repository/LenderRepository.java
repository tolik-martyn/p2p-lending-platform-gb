package com.example.lenderservice.repository;

import com.example.lenderservice.model.Lender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LenderRepository extends JpaRepository<Lender, Long> {
}
