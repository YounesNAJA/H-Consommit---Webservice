package com.hc.utilitaire;

import java.util.Date;

import org.springframework.expression.spel.ast.BooleanLiteral;

public class ResultatAleaElec {
	
	Boolean etat;
	String du_mois;
	String du_annee;
	long quantite_mois;
	int tranche_mois;
	double prix_mois;
	Date last_consult;
	long quantite_last;
	int tranche_last;
	double prix_last;
	
	
	String rcmd_mois;
	String rcms_last;
	
	String msg_erreur;
	
	public ResultatAleaElec(Boolean etat, String du_mois, String du_annee, long quantite_mois, int tranche_mois, double prix_mois,
			Date last_consult, long quantite_last, int tranche_last, double prix_last, String rcmd_mois,
			String rcms_last) {
		
		this.etat = etat;
		this.du_mois = du_mois;
		this.du_annee = du_annee;
		this.quantite_mois = quantite_mois;
		this.tranche_mois = tranche_mois;
		this.prix_mois = prix_mois;
		this.last_consult = last_consult;
		this.quantite_last = quantite_last;
		this.tranche_last = tranche_last;
		this.prix_last = prix_last;
		this.rcmd_mois = rcmd_mois;
		this.rcms_last = rcms_last;
		
	}
	
	
	
	
	public ResultatAleaElec(Boolean etat, String msg_erreur) {
		
		this.etat = etat;
		this.msg_erreur = msg_erreur;
	}




	public String getDu_mois() {
		return du_mois;
	}
	public void setDu_mois(String du_mois) {
		this.du_mois = du_mois;
	}
	public String getDu_annee() {
		return du_annee;
	}
	public void setDu_annee(String du_annee) {
		this.du_annee = du_annee;
	}
	public long getQuantite_mois() {
		return quantite_mois;
	}
	public void setQuantite_mois(long quantite_mois) {
		this.quantite_mois = quantite_mois;
	}
	public int getTranche_mois() {
		return tranche_mois;
	}
	public void setTranche_mois(int tranche_mois) {
		this.tranche_mois = tranche_mois;
	}
	public double getPrix_mois() {
		return prix_mois;
	}
	public void setPrix_mois(double prix_mois) {
		this.prix_mois = prix_mois;
	}
	public Date getLast_consult() {
		return last_consult;
	}
	public void setLast_consult(Date last_consult) {
		this.last_consult = last_consult;
	}
	public long getQuantite_last() {
		return quantite_last;
	}
	public void setQuantite_last(long quantite_last) {
		this.quantite_last = quantite_last;
	}
	public int getTranche_last() {
		return tranche_last;
	}
	public void setTranche_last(int tranche_last) {
		this.tranche_last = tranche_last;
	}
	public double getPrix_last() {
		return prix_last;
	}
	public void setPrix_last(double prix_last) {
		this.prix_last = prix_last;
	}
	public String getRcmd_mois() {
		return rcmd_mois;
	}
	public void setRcmd_mois(String rcmd_mois) {
		this.rcmd_mois = rcmd_mois;
	}
	public String getRcms_last() {
		return rcms_last;
	}
	public void setRcms_last(String rcms_last) {
		this.rcms_last = rcms_last;
	}

	public Boolean getEtat() {
		return etat;
	}

	public void setEtat(Boolean etat) {
		this.etat = etat;
	}

	public String getMsg_erreur() {
		return msg_erreur;
	}

	public void setMsg_erreur(String msg_erreur) {
		this.msg_erreur = msg_erreur;
	}
	
	
	
	
}
