package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;

@Service
public class StudenteService {

	@Autowired
	private StudenteRepository sr;

	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;

	@Autowired
	private IndirizzoRepository indirizzoRepository;

	// Recupera tutti gli studenti
	public List<Studente> all() {
		return sr.findAll();
	}

	// Recupera uno studente specifico tramite id
	public Optional<Studente> get(int id) {
		return sr.findById(id);
	}

	// Crea un nuovo studente
	public Studente add(Studente studente) {
		// Recupera gli oggetti CorsoLaurea e Indirizzo usando gli ID nel corpo della
		// richiesta
		CorsoLaurea corso = corsoLaureaRepository.findById(studente.getCorsoLaurea().getId())
				.orElseThrow(() -> new RuntimeException("Corso non trovato"));
		Indirizzo indirizzo = indirizzoRepository.findById(studente.getIndirizzo().getId())
				.orElseThrow(() -> new RuntimeException("Indirizzo non trovato"));

		// Imposta le relazioni
		studente.setCorsoLaurea(corso);
		studente.setIndirizzo(indirizzo);

		// Salva lo studente
		return sr.save(studente);
	}

	// Aggiorna un esistente studente
	public Studente update(Studente studente, int id) {
		// Verifica se lo studente esiste
		Studente existingStudente = sr.findById(id).orElseThrow(() -> new RuntimeException("Studente non trovato"));

		// Aggiorna solo i campi che sono stati modificati
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
		if (studente.getPassword() != null) {
			existingStudente.setPassword(studente.getPassword());
		}
		if (studente.getAttivo() != null) {
			existingStudente.setAttivo(studente.getAttivo());
		}
		if (studente.getCorsoLaurea() != null) {
			CorsoLaurea corso = corsoLaureaRepository.findById(studente.getCorsoLaurea().getId())
					.orElseThrow(() -> new RuntimeException("Corso non trovato"));
			existingStudente.setCorsoLaurea(corso);
		}
		if (studente.getIndirizzo() != null) {
			Indirizzo indirizzo = indirizzoRepository.findById(studente.getIndirizzo().getId())
					.orElseThrow(() -> new RuntimeException("Indirizzo non trovato"));
			existingStudente.setIndirizzo(indirizzo);
		}

		// Salva lo studente aggiornato
		return sr.save(existingStudente);
	}

	// Elimina uno studente tramite id
	public void delete(int id) {
		sr.deleteById(id);
	}
}
