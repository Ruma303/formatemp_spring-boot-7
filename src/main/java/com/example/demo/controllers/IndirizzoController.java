package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.Indirizzo;
import com.example.demo.services.IndirizzoService;

@RestController
@RequestMapping("/api/indirizzi")
public class IndirizzoController {

    @Autowired
    private IndirizzoService indirizzoService;

    // Recupera tutti gli indirizzi
    @GetMapping("/")
    public List<Indirizzo> all() {
        return indirizzoService.all();
    }

    // Recupera un indirizzo specifico tramite id
    @GetMapping("/{id}")
    public Optional<Indirizzo> get(@PathVariable int id) {
        return indirizzoService.get(id);
    }

    // Crea un nuovo indirizzo
    @PostMapping("/")
    public Indirizzo create(@RequestBody Indirizzo indirizzo) {
        return indirizzoService.add(indirizzo);
    }

    // Aggiorna un indirizzo tramite id
    @PutMapping("/{id}")
    public Indirizzo updatePut(@PathVariable int id, @RequestBody Indirizzo indirizzo) {
        return indirizzoService.update(indirizzo, id);
    }

    // Aggiorna parzialmente un indirizzo tramite id
    @PatchMapping("/{id}")
    public Indirizzo updatePatch(@PathVariable int id, @RequestBody Indirizzo indirizzo) {
        return indirizzoService.update(indirizzo, id);
    }

    // Elimina un indirizzo tramite id
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        indirizzoService.delete(id);
    }
}
