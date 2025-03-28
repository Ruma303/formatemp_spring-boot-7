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

>**PS**: Il compromesso migliore per poter eseguire tutte le richieste HTTP senza avere loop di serializzazione è usare soltanto l'annotazione `@JsonIgnore` nelle tabelle secondarie.

## **Studente**

```java
package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    // @JsonManagedReference // Evita il loop
    private CorsoLaurea corsoLaurea;

    // Relazione Many-to-One con indirizzi
    @ManyToOne
    @JoinColumn(name = "indirizzo", nullable = false)
    // @JsonManagedReference // Evita il loop
    private Indirizzo indirizzo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public CorsoLaurea getCorsoLaurea() {
		return corsoLaurea;
	}

	public void setCorsoLaurea(CorsoLaurea corsoLaurea) {
		this.corsoLaurea = corsoLaurea;
	}

	public Indirizzo getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(Indirizzo indirizzo) {
		this.indirizzo = indirizzo;
	}
}
```

---

## **Indirizzo**

```java
package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore // Evita serializzazione ciclica
	private List<Studente> studenti = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStrada() {
		return strada;
	}

	public void setStrada(String strada) {
		this.strada = strada;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public List<Studente> getStudenti() {
		return studenti;
	}

	public void setStudenti(List<Studente> studenti) {
		this.studenti = studenti;
	}
}
```

---

## **CorsoLaurea**

```java
package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore // Evita serializzazione ciclica
	private List<Studente> studenti = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getFacolta() {
		return facolta;
	}

	public void setFacolta(String facolta) {
		this.facolta = facolta;
	}

	public List<Studente> getStudenti() {
		return studenti;
	}

	public void setStudenti(List<Studente> studenti) {
		this.studenti = studenti;
	}
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

---

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

---

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

---

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

# **Richieste HTTP**

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

---

# **Aggiunte: Criptazione password**

Aggiorniamo la tabella `studenti` con un paio di campi: `password` e `active`. Il nostro scopo sarà criptare la password per evitare di salvarla in chiaro, e rendere l'applicazione più sicura.

```sql
ALTER TABLE studenti ADD COLUMN password longtext NULL AFTER email;
ALTER TABLE studenti ADD COLUMN attivo INT(1) NULL AFTER telefono;
```

La tabella `studenti` aggiornata sarà:

```sql
mysql> describe studenti;
+-----------+--------------+------+-----+---------+----------------+
| Field     | Type         | Null | Key | Default | Extra          |
+-----------+--------------+------+-----+---------+----------------+
| id        | int          | NO   | PRI | NULL    | auto_increment |
| nome      | varchar(255) | YES  |     | NULL    |                |
| cognome   | varchar(255) | YES  |     | NULL    |                |
| email     | varchar(255) | YES  |     | NULL    |                |
| password  | longtext     | YES  |     | NULL    |                |
| telefono  | varchar(255) | YES  |     | NULL    |                |
| active    | int          | YES  |     | 1       |                |
| id_corso  | int          | NO   | MUL | NULL    |                |
| indirizzo | int          | NO   | MUL | NULL    |                |
+-----------+--------------+------+-----+---------+----------------+
```

---

## **Alternativa 1: BCrypt**

Per **hashare la password** abbiamo numerose alternative. Al momento non usiamo **Spring Security**, ma possiamo raggiungere lo stesso risultato con un paio di tecniche.

Il primo metodo è l'uso della classe `BCrypt`. Installiamo la dipendenza Maven:

```xml
<dependency>
	<groupId>org.mindrot</groupId>
	<artifactId>jbcrypt</artifactId>
	<version>0.4</version>
</dependency>
```

Nell'entity `Studente` aggiungiamo i campi. In particolare, la password verrà hashata nel setter.

```java
import org.mindrot.jbcrypt.BCrypt;

public void setPassword(String password) {
	this.password = BCrypt.hashpw(password, BCrypt.gensalt());
}
```

 Usiamo i metodi:
- `BCrypt.hashpw()` per hashare la password. Questo metodo richiede due parametri: la password da hashare e il "sale" per rendere ancora più univoca la password.
- Il sale è essenzialmente una stringa che si aggiunge alla password hashata. Possiamo scrivere una stringa letterale, ma è ovviamente consigliato usare il metodo della classe `BCrypt.gensalt()` per generare un sale hashato e più sicuro.

Non ci resta che fare qualche test con le API. Esempio di una richiesta POST:

```json
{
    "nome": "Tizio",
    "cognome": "Caio",
    "email": "tizio@caio.com",
    "password": "pippo",
    "telefono": "32431677461",
    "attivo": 1,
    "corsoLaurea": {"id": 4},
    "indirizzo": {"id": 2}
}
```

Vedremo che la password verrà hashata. Esempio:

```json
"password": "$2a$10$iMSmtpz1YP3hAvzWr.iFd.SdyTccMcRi96aWRi7F61uYpHcCTveKa",
```

---

## **Alternativa 2: PasswordEncoder**

In maniera molto simile possiamo usare un'altra dipendenza, `PasswordEncoder`. Installiamo la dipendenza Maven. Questa fa parte del pacchetto Spring Security, ma non prendiamo l'intera libreria, ma soltanto una piccola dipendenza chiamata `spring-security.crypto` che è adatta al nostro scopo.

```xml
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-crypto</artifactId>
</dependency>
```

### **Classe `SecurityConfig`**

Questa volta definiremo una nuova classe (o interfaccia volendo) chiamata `SecurityConfig` all'interno del package `...configs`. Ciò ci consentirà di riutilizzare il codice nei services o altrove, in più punti dell'applicazione.

Questa classe definisce un **Bean** per la gestione della codifica delle password:

```java
package com.example.demo.configs;

import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

#### **Spiegazione delle Annotazioni**

- `@Configuration`: indica che questa classe è una classe di configurazione, cioè una classe che dichiara Bean per il contesto di Spring.

- `@Bean`: indica che il metodo `passwordEncoder()` deve essere gestito da Spring come un Bean.

#### **Cos'è un Bean in Spring?**

Un **Bean** in Spring è un oggetto gestito dal container di Spring. Viene creato, configurato e gestito automaticamente dal framework. I Bean sono definiti nelle classi annotate con `@Configuration` o in componenti annotati con `@Component`, `@Service`, `@Repository`, ecc.

In sostanza, **un Bean è un'istanza di una classe che viene resa disponibile nel contesto applicativo di Spring per essere riutilizzata ovunque serva senza doverla istanziare manualmente ogni volta**.

#### **Cosa fa questa classe?**

- Definisce un **Bean di tipo `PasswordEncoder`**.

- Il metodo `passwordEncoder()` restituisce un'istanza di `BCryptPasswordEncoder`, che è un'implementazione di `PasswordEncoder` utilizzata per codificare e verificare le password in modo sicuro.

Quando l'applicazione viene avviata, Spring rileva questa configurazione e rende disponibile un'istanza di `BCryptPasswordEncoder` ovunque venga richiesta come dipendenza.

---

### **Usare il bean `PasswordEncoder` nel `StudentService`**

Nel service `StudenteService`, abbiamo iniettato `PasswordEncoder` (tramite `@Autowired`, ma è opzionale) per codificare le password prima di salvarle nel database:

```java
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
```

- Grazie all'**iniezione delle dipendenze**, non creiamo manualmente un'istanza di `BCryptPasswordEncoder`, ma usiamo direttamente quella gestita da Spring.

- Quando si crea un nuovo `Studente`, il metodo `pe.encode(studente.getPassword())` converte la password in una stringa cifrata prima di salvarla nel database.

---

# **Filtrare i campi con le classi DTO**

Non è una buona pratica mostrare la password all'utente finale, anche se è la sua e anche se è criptata. La comunicazione potrebbe essere comunque intercettata, e anche se le chance sono infinitesime, ma anche le password hashate potrebbero essere forzate.

Il nostro scopo è quindi filtrare i campi da inviare al client. Nel nostro caso, ci basta ritornare tutti i campi tranne la `password`.

Di seguito viene mostrata una possibile implementazione di una classe DTO (Data Transfer Object) per la classe Studente, che esclude il campo password, insieme a un esempio di mapping manuale da `Studente` a `StudenteDto`.

---

## **1. Creazione della classe StudenteDto**

La classe DTO serve a definire la struttura dei dati che vuoi inviare al frontend, escludendo campi sensibili (come la password) o quelli non necessari. Ad esempio:

```java
package com.example.demo.dtos;

public class StudenteDto {

	private Integer id;
	private String nome;
	private String cognome;
	private String email;
	private String telefono;
	private Boolean attivo;

	// Se desiderassimo includere informazioni relative alle relazioni,
	// possiamo aggiungere anche gli ID o dei DTO dedicati per CorsoLaurea e Indirizzo.
	private Integer idCorsoLaurea;
	private Integer idIndirizzo;

	// Costruttore senza argomenti
	public StudenteDto() {
	}

	// Costruttore con argomenti
	public StudenteDto(Integer id, String nome, String cognome, String email, String telefono, Boolean attivo,
			Integer idCorsoLaurea, Integer idIndirizzo) {
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.telefono = telefono;
		this.attivo = attivo;
		this.idCorsoLaurea = idCorsoLaurea;
		this.idIndirizzo = idIndirizzo;
	}

	// Getters e Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Boolean getAttivo() {
		return attivo;
	}

	public void setAttivo(Boolean attivo) {
		this.attivo = attivo;
	}

	public Integer getIdCorsoLaurea() {
		return idCorsoLaurea;
	}

	public void setIdCorsoLaurea(Integer idCorsoLaurea) {
		this.idCorsoLaurea = idCorsoLaurea;
	}

	public Integer getIdIndirizzo() {
		return idIndirizzo;
	}

	public void setIdIndirizzo(Integer idIndirizzo) {
		this.idIndirizzo = idIndirizzo;
	}
}
```

---

## **2. Mapping da Studente a StudenteDto**

Una volta definito il DTO, il passo successivo è convertire un oggetto Studente in StudenteDto. Questo mapping può essere fatto in modo manuale nel service o in un'apposita classe di mapping. Ecco un esempio di metodo di conversione nel service:

```java
package com.example.demo.services;

import java.util.stream.Collectors;
import com.example.demo.dtos.StudenteDto;
import com.example.demo.entities.Studente;
// Altri import

@Service
public class StudenteService {
    // ... altri metodi e dipendenze

    // Metodo di mapping da Studente a StudenteDto
    public StudenteDto convertToDto(Studente studente) {
        StudenteDto dto = new StudenteDto();
        dto.setId(studente.getId());
        dto.setNome(studente.getNome());
        dto.setCognome(studente.getCognome());
        dto.setEmail(studente.getEmail());
        dto.setTelefono(studente.getTelefono());
        dto.setAttivo(studente.getAttivo());
        // Assumiamo che nelle relazioni Studente abbia metodi per recuperare gli ID
        dto.setIdCorsoLaurea(studente.getCorsoLaurea() != null ? studente.getCorsoLaurea().getId() : null);
        dto.setIdIndirizzo(studente.getIndirizzo() != null ? studente.getIndirizzo().getId() : null);
        return dto;
    }

    // Esempio di metodo che restituisce tutti gli studenti come DTO
    public List<StudenteDto> allDto() {
        return sr.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
```

>**NB**: in base a ciò che vorremmo inviare al frontend potremmo usare il metodo `all()` che ritorna tutti gli studenti con le password, oppure `allDto()` che non mostra le password.

### **Approfondimento `allDto()`**

```java
public List<StudenteDto> allDto() {
    return sr.findAll()                       // Recupera tutti gli studenti dal database come List<Studente>
             .stream()                        // Converte la lista in uno stream per operazioni funzionali
             .map(this::convertToDto)         // Applica il metodo convertToDto a ogni elemento dello stream
             .collect(Collectors.toList());   // Raccoglie il risultato in una lista di StudenteDto
}
```


```java
sr.findAll()
```

- Questo metodo del repository JPA (`JpaRepository`) recupera tutti gli studenti dal database come `List<Studente>`.

- Restituisce una **lista** di entità `Studente`.

```java
.stream()
```

- Converte la lista in uno **Stream** di oggetti `Studente`.

- Uno stream in Java è un flusso di dati su cui si possono applicare operazioni funzionali come `map`, `filter`, `forEach`, ecc.

- **Gli stream non modificano i dati originali**, ma generano un nuovo risultato.

```java
.map(this::convertToDto)
```

- `map()` è un'operazione intermedia che **trasforma** ogni elemento dello stream.

- In questo caso, `this::convertToDto` è un **method reference** che equivale a scrivere:

```java
.map(studente -> convertToDto(studente))
```

- **Per ogni elemento dello stream** (`Studente`), chiama `convertToDto(studente)`, trasformandolo in un `StudenteDto`.

```java
.collect(Collectors.toList())
```

- `collect()` è un'operazione **terminale** che raccoglie il risultato dello stream in una nuova struttura dati.

- `Collectors.toList()` specifica che vogliamo una **Lista** come risultato.

---

## **3. Utilizzo del DTO nel Controller**

Infine, nel controller potresti avere un endpoint che restituisce gli studenti come DTO, ad esempio:

```java
@RestController
@RequestMapping("/api/studenti")
public class StudenteController {

    @Autowired
    private StudenteService ss;

    @GetMapping("/dto")
    public List<StudenteDto> allDto() {
        return ss.allDto();
    }
}
```

---

## **4. Richieste HTTP**

Eseguiamo una richiesta per testare l'endpoint:

```http
GET http://localhost:8080/api/studenti/dto
```

Possiamo vedere che la collection JSON ritornata nel frontend non contiene più il campo `password`.

```json
[
    {
        "id": 1,
        "nome": "Mario",
        "cognome": "Rossi",
        "email": "mario.rossi@example.com",
        "telefono": "3331111111",
        "attivo": true,
        "idCorsoLaurea": 1,
        "idIndirizzo": 1
    },
    {
        "id": 2,
        "nome": "Luigi",
        "cognome": "Verdi",
        "email": "luigi.verdi@example.com",
        "telefono": "3332222222",
        "attivo": true,
        "idCorsoLaurea": 2,
        "idIndirizzo": 2
    } //...
]
```