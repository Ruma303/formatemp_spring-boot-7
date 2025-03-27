package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Indirizzo;
import com.example.demo.repositories.IndirizzoRepository;

@Service
public class IndirizzoService {

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    // Recupera tutti gli indirizzi
    public List<Indirizzo> all() {
        return indirizzoRepository.findAll();
    }

    // Recupera un indirizzo tramite id
    public Optional<Indirizzo> get(int id) {
        return indirizzoRepository.findById(id);
    }

    // Crea un nuovo indirizzo
    public Indirizzo add(Indirizzo indirizzo) {
        return indirizzoRepository.save(indirizzo);
    }

    // Aggiorna un indirizzo esistente
    public Indirizzo update(Indirizzo indirizzo, int id) {
        Indirizzo existingIndirizzo = indirizzoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Indirizzo non trovato"));

        if (indirizzo.getStrada() != null) {
            existingIndirizzo.setStrada(indirizzo.getStrada());
        }
        if (indirizzo.getCivico() != null) {
            existingIndirizzo.setCivico(indirizzo.getCivico());
        }
        if (indirizzo.getCap() != null) {
            existingIndirizzo.setCap(indirizzo.getCap());
        }
        if (indirizzo.getCitta() != null) {
            existingIndirizzo.setCitta(indirizzo.getCitta());
        }

        return indirizzoRepository.save(existingIndirizzo);
    }

    // Elimina un indirizzo tramite id
    public void delete(int id) {
        indirizzoRepository.deleteById(id);
    }
}
