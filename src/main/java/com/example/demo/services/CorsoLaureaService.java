package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.CorsoLaurea;
import com.example.demo.repositories.CorsoLaureaRepository;

@Service
public class CorsoLaureaService {

    @Autowired
    private CorsoLaureaRepository corsoLaureaRepository;

    // Recupera tutti i corsi di laurea
    public List<CorsoLaurea> all() {
        return corsoLaureaRepository.findAll();
    }

    // Recupera un corso di laurea tramite id
    public Optional<CorsoLaurea> get(int id) {
        return corsoLaureaRepository.findById(id);
    }

    // Crea un nuovo corso di laurea
    public CorsoLaurea add(CorsoLaurea corsoLaurea) {
        return corsoLaureaRepository.save(corsoLaurea);
    }

    // Aggiorna un corso di laurea esistente
    public CorsoLaurea update(CorsoLaurea corsoLaurea, int id) {
        CorsoLaurea existingCorsoLaurea = corsoLaureaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corso di laurea non trovato"));

        if (corsoLaurea.getAnno() != null) {
            existingCorsoLaurea.setAnno(corsoLaurea.getAnno());
        }
        if (corsoLaurea.getFacolta() != null) {
            existingCorsoLaurea.setFacolta(corsoLaurea.getFacolta());
        }

        return corsoLaureaRepository.save(existingCorsoLaurea);
    }

    // Elimina un corso di laurea tramite id
    public void delete(int id) {
        corsoLaureaRepository.deleteById(id);
    }
}
