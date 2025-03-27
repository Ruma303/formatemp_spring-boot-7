# **Database**

```sql
CREATE DATABASE IF NOT EXISTS scuola_di_informatica;

USE scuola_di_informatica;


-- Corsi Laurea

CREATE TABLE IF NOT EXISTS corsi_laurea (
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    anno INT NULL,
    facolta VARCHAR(200) NULL
) ENGINE = InnoDB;

DESC corsi_laurea;

INSERT INTO corsi_laurea (anno, facolta)
VALUES
(1, 'Informatica'),
(2, 'Ingegneria del Software'),
(3, 'Sicurezza Informatica'),
(1, 'Matematica'),
(2, 'Statistica'),
(3, 'Fisica'),
(1, 'Economia'),
(2, 'Ingegneria Elettronica'),
(3, 'Bioinformatica'),
(1, 'Data Science');

SELECT * FROM corsi_laurea;


-- Indirizzi

CREATE TABLE IF NOT EXISTS indirizzi (
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    strada VARCHAR(200) NULL,
    civico VARCHAR(200) NULL,
    cap VARCHAR(200) NULL,
    citta VARCHAR(200) NULL
) ENGINE = InnoDB;

DESC indirizzi;

INSERT INTO indirizzi (strada, civico, cap, citta)
VALUES
('Via Roma', '10', '00100', 'Roma'),
('Corso Garibaldi', '12', '25100', 'Brescia'),
('Viale Marconi', '50', '40100', 'Bologna'),
('Via Dante', '8', '20100', 'Milano'),
('Piazza Verdi', '3', '70100', 'Bari'),
('Via dei Mille', '25', '50100', 'Firenze'),
('Via Mazzini', '45', '80100', 'Napoli'),
('Via Torino', '22', '10100', 'Torino'),
('Via San Carlo', '9', '37100', 'Verona'),
('Viale Trento', '7', '38100', 'Trento');

SELECT * FROM indirizzi;


-- Studenti

CREATE TABLE IF NOT EXISTS studenti (
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    nome VARCHAR(255) NULL,
    cognome VARCHAR(255) NULL,
    email VARCHAR(255) NULL,
    telefono VARCHAR(255) NULL,
    id_corso INT NOT NULL,
    indirizzo INT NOT NULL,
    CONSTRAINT fk_corsi_laurea
        FOREIGN KEY (id_corso)
        REFERENCES corsi_laurea(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_indirizzi
        FOREIGN KEY (indirizzo)
        REFERENCES indirizzi(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB;

DESC studenti;

INSERT INTO studenti (nome, cognome, email, telefono, id_corso, indirizzo)
VALUES
('Mario', 'Rossi', 'mario.rossi@example.com', '3331111111', 1, 1),
('Luigi', 'Verdi', 'luigi.verdi@example.com', '3332222222', 2, 2),
('Anna', 'Bianchi', 'anna.bianchi@example.com', '3333333333', 3, 3),
('Carla', 'Neri', 'carla.neri@example.com', '3334444444', 4, 4),
('Luca', 'Gialli', 'luca.gialli@example.com', '3335555555', 5, 5),
('Sara', 'Viola', 'sara.viola@example.com', '3336666666', 6, 6),
('Giuseppe', 'Marrone', 'giuseppe.marrone@example.com', '3337777777', 7, 7),
('Elena', 'Blu', 'elena.blu@example.com', '3338888888', 8, 8),
('Marco', 'Rosa', 'marco.rosa@example.com', '3339999999', 9, 9),
('Paola', 'Grigio', 'paola.grigio@example.com', '3330000000', 10, 10);

SELECT * FROM studenti;
```

---

# **Entities**

## **Studente**

```java
package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "studenti")
public class Studente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    // Relazione Many-to-One con corsi_laurea
    @ManyToOne
    @JoinColumn(name = "id_corso", nullable = false)
    private CorsoLaurea corsoLaurea;

    // Relazione Many-to-One con indirizzi
    @ManyToOne
    @JoinColumn(name = "indirizzo", nullable = false)
    private Indirizzo indirizzo;

	// Getter e setter ...
}
```

---

## **Indirizzo**

```java
package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "indirizzi")
public class Indirizzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "strada", length = 200)
    private String strada;

    @Column(name = "civico", length = 200)
    private String civico;

    @Column(name = "cap", length = 200)
    private String cap;

    @Column(name = "citta", length = 200)
    private String citta;

    // Relazione One-to-Many con Studente (lato non proprietario)
    @OneToMany(mappedBy = "indirizzo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Studente> studenti = new ArrayList<>();

	// Getter e setter ...
}
```

---

## **CorsoLaurea**

```java
package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "corsi_laurea")
public class CorsoLaurea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "anno")
    private Integer anno;

    @Column(name = "facolta", length = 200)
    private String facolta;

    // Relazione One-to-Many con Studente (lato non proprietario)
    @OneToMany(mappedBy = "corsoLaurea", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Studente> studenti = new ArrayList<>();

	// Getter e setter ...
}
```

---

# **Repositories**

## **StudenteRepository**

```java
package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Studente;

@Repository
public interface StudenteRepository extends JpaRepository<Studente, Integer> {

}
```

## **IndirizzoRepository**

```java
package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Indirizzo;

@Repository
public interface IndirizzoRepository extends JpaRepository<Indirizzo, Integer> {

}
```

## **CorsoLaureaRepository**

```java
package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.CorsoLaurea;

@Repository
public interface CorsoLaureaRepository extends JpaRepository<CorsoLaurea, Integer> {

}
```

---

# **Services**

## **StudenteService**

```java
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
```

## **IndirizzoService**

```java
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
```

## **CorsoLaureaService**

```java
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
```

---

# **Controllers**

## **StudenteController**

```java
package com.example.demo.controllers;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
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
```

---

## **CorsoLaureaController**

```java
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
```

---

## **IndirizzoController**

```java
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
```


---

# **Richieste**

le richieste HTTP sono le classiche.

## **Studente**

### Esempi richieste POST, PUT, PATCH

```http
POST http://localhost:8080/api/studenti/
```

>**NB**: quando indichiamo gli `id` nelle tabelle secondarie dobbiamo passarli come oggetti. Questo perché il campo `corsoLaurea` va ad inserire un valore nella classe CorsoLaurea, e dobbiamo quindi entrare nel campo `id` interno. Lo stesso vale per `indirizzo`.

```json
{
    "nome": "Tizio",
    "cognome": "Caio",
    "email": "tizio@caio.com",
    "telefono": "32431677461",
    "corsoLaurea": {"id": 4},
    "indirizzo": {"id": 2}
}
```
