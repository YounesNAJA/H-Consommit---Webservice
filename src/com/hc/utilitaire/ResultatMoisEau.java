package com.hc.utilitaire;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultatMoisEau implements Serializable{
	
	ArrayList<String> comparaison = new ArrayList<String>();
	ArrayList<String> remarque = new ArrayList<String>();
	ArrayList<String> conseil = new ArrayList<String>();
	long quantite;
	double prix;
	Reponse reponse;
	String compareMois;
	
	
	public ResultatMoisEau(long quantite, double prix, Reponse reponse) {
		super();
		this.quantite = quantite;
		this.prix = prix;
		this.reponse = reponse;
		this.compareMois = "";
	}

	public ResultatMoisEau(long quantite, double prix,String compareMois, Reponse reponse) {
		super();
		this.quantite = quantite;
		this.prix = prix;
		this.compareMois = compareMois;
		this.reponse = reponse;
	}


	public ArrayList<String> getComparaison() {
		return comparaison;
	}


	public void setComparaison(ArrayList<String> comparaison) {
		this.comparaison = comparaison;
	}


	public ArrayList<String> getRemarque() {
		return remarque;
	}


	public void setRemarque(ArrayList<String> remarque) {
		this.remarque = remarque;
	}


	public ArrayList<String> getConseil() {
		return conseil;
	}


	public void setConseil(ArrayList<String> conseil) {
		this.conseil = conseil;
	}


	public long getQuantite() {
		return quantite;
	}


	public void setQuantite(long quantite) {
		this.quantite = quantite;
	}


	public double getPrix() {
		return prix;
	}


	public void setPrix(double prix) {
		this.prix = prix;
	}


	public Reponse getReponse() {
		return reponse;
	}


	public void setReponse(Reponse reponse) {
		this.reponse = reponse;
	}


	public String getCompareMois() {
		return compareMois;
	}


	public void setCompareMois(String compareMois) {
		this.compareMois = compareMois;
	}
	
	
	
	
}
