package com.hc.model;
// Generated 11 mai 2016 03:02:00 by Hibernate Tools 4.3.1.Final

import java.util.Date;

/**
 * MensuelleEau generated by hbm2java
 */
public class MensuelleEau implements java.io.Serializable {

	private Integer id;
	private Compte compte;
	private String annee;
	private String mois;
	private Date dateConsultation;
	private long chiffreCompteur;
	private long quantite;

	public MensuelleEau() {
	}

	public MensuelleEau(Compte compte, String annee, String mois, Date dateConsultation, long chiffreCompteur,
			long quantite) {
		this.compte = compte;
		this.annee = annee;
		this.mois = mois;
		this.dateConsultation = dateConsultation;
		this.chiffreCompteur = chiffreCompteur;
		this.quantite = quantite;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Compte getCompte() {
		return this.compte;
	}

	public void setCompte(Compte compte) {
		this.compte = compte;
	}

	public String getAnnee() {
		return this.annee;
	}

	public void setAnnee(String annee) {
		this.annee = annee;
	}

	public String getMois() {
		return this.mois;
	}

	public void setMois(String mois) {
		this.mois = mois;
	}

	public Date getDateConsultation() {
		return this.dateConsultation;
	}

	public void setDateConsultation(Date dateConsultation) {
		this.dateConsultation = dateConsultation;
	}

	public long getChiffreCompteur() {
		return this.chiffreCompteur;
	}

	public void setChiffreCompteur(long chiffreCompteur) {
		this.chiffreCompteur = chiffreCompteur;
	}

	public long getQuantite() {
		return this.quantite;
	}

	public void setQuantite(long quantite) {
		this.quantite = quantite;
	}

}