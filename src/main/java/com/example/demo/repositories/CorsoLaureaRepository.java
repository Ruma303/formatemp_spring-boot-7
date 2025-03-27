package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.CorsoLaurea;

@Repository
public interface CorsoLaureaRepository extends JpaRepository<CorsoLaurea, Integer> {

}
