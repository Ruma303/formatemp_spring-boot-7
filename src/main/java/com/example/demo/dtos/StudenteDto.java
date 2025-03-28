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
