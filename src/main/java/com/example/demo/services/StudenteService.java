package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudenteService {

    private final StudenteRepository sr;
    private final CorsoLaureaRepository clr;
    private final IndirizzoRepository ir;
    private final PasswordEncoder pe;

    @Autowired
    public StudenteService(StudenteRepository sr, 
    					   CorsoLaureaRepository clr,
                           IndirizzoRepository ir, 
                           PasswordEncoder pe
    ) {
        this.sr = sr;
        this.clr = clr;
        this.ir = ir;
        this.pe = pe;
    }

    public List<Studente> all() {
        return sr.findAll();
    }

    public Optional<Studente> get(int id) {
        return sr.findById(id);
    }

    public Studente add(Studente studente) {
        CorsoLaurea corso = clr.findById(studente.getCorsoLaurea().getId())
                .orElseThrow(() -> new RuntimeException("Corso non trovato"));
        Indirizzo indirizzo = ir.findById(studente.getIndirizzo().getId())
                .orElseThrow(() -> new RuntimeException("Indirizzo non trovato"));

        studente.setCorsoLaurea(corso);
        studente.setIndirizzo(indirizzo);

        if (studente.getPassword() != null && !studente.getPassword().isEmpty()) {
            studente.setPassword(pe.encode(studente.getPassword()));
        }

        return sr.save(studente);
    }

    public Studente update(Studente studente, int id) {
        Studente existingStudente = sr.findById(id)
                .orElseThrow(() -> new RuntimeException("Studente non trovato"));

        if (studente.getNome() != null) {
            existingStudente.setNome(studente.getNome());
        }
        if (studente.getCognome() != null) {
            existingStudente.setCognome(studente.getCognome());
        }
        if (studente.getEmail() != null) {
            existingStudente.setEmail(studente.getEmail());
        }
        if (studente.getTelefono() != null) {
            existingStudente.setTelefono(studente.getTelefono());
        }
        if (studente.getPassword() != null && !studente.getPassword().isEmpty()) {
            existingStudente.setPassword(pe.encode(studente.getPassword()));
        }
        if (studente.getAttivo() != null) {
            existingStudente.setAttivo(studente.getAttivo());
        }
        if (studente.getCorsoLaurea() != null) {
            CorsoLaurea corso = clr.findById(studente.getCorsoLaurea().getId())
                    .orElseThrow(() -> new RuntimeException("Corso non trovato"));
            existingStudente.setCorsoLaurea(corso);
        }
        if (studente.getIndirizzo() != null) {
            Indirizzo indirizzo = ir.findById(studente.getIndirizzo().getId())
                    .orElseThrow(() -> new RuntimeException("Indirizzo non trovato"));
            existingStudente.setIndirizzo(indirizzo);
        }

        return sr.save(existingStudente);
    }

    public void delete(int id) {
        sr.deleteById(id);
    }
}
