package com.example.demo.controllers;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dtos.StudenteDto;
import com.example.demo.entities.Studente;
import com.example.demo.services.StudenteService;

@RestController
@RequestMapping("/api/studenti")
public class StudenteController {

	@Autowired
	private StudenteService ss;

	@GetMapping("/")
	public List<Studente> all() {
		return ss.all();
	}

	@GetMapping("/{id}")
	public Optional<Studente> get(@PathVariable int id) {
		return ss.get(id);
	}
	
	@GetMapping("/dto")
    public List<StudenteDto> allDto() {
        return ss.allDto();
    }

	@PostMapping("/")
	public Studente create(@RequestBody Studente studente) {
		return ss.add(studente);
	}

	@PutMapping("/{id}")
	public Studente updatePut(@PathVariable int id, @RequestBody Studente studente) {
		return ss.update(studente, id);
	}

	@PatchMapping("/{id}")
	public Studente updatePatch(@PathVariable int id, @RequestBody Studente studente) {
		return ss.update(studente, id);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		ss.delete(id);
	}
}
