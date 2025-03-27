package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.CorsoLaurea;
import com.example.demo.services.CorsoLaureaService;

@RestController
@RequestMapping("/api/corsi_laurea")
public class CorsoLaureaController {

    @Autowired
    private CorsoLaureaService corsoLaureaService;

    // Recupera tutti i corsi di laurea
    @GetMapping("/")
    public List<CorsoLaurea> all() {
        return corsoLaureaService.all();
    }

    // Recupera un corso di laurea specifico tramite id
    @GetMapping("/{id}")
    public Optional<CorsoLaurea> get(@PathVariable int id) {
        return corsoLaureaService.get(id);
    }

    // Crea un nuovo corso di laurea
    @PostMapping("/")
    public CorsoLaurea create(@RequestBody CorsoLaurea corsoLaurea) {
        return corsoLaureaService.add(corsoLaurea);
    }

    // Aggiorna un corso di laurea tramite id
    @PutMapping("/{id}")
    public CorsoLaurea updatePut(@PathVariable int id, @RequestBody CorsoLaurea corsoLaurea) {
        return corsoLaureaService.update(corsoLaurea, id);
    }

    // Aggiorna parzialmente un corso di laurea tramite id
    @PatchMapping("/{id}")
    public CorsoLaurea updatePatch(@PathVariable int id, @RequestBody CorsoLaurea corsoLaurea) {
        return corsoLaureaService.update(corsoLaurea, id);
    }

    // Elimina un corso di laurea tramite id
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        corsoLaureaService.delete(id);
    }
}
