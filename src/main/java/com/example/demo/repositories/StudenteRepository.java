package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Studente;

@Repository
public interface StudenteRepository extends JpaRepository<Studente, Integer> {

}
