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
