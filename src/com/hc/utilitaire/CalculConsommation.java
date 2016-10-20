package com.hc.utilitaire;

public class CalculConsommation {

	
	int tranche;
	double prix;
	
	public CalculConsommation(int tranche, double prix) {	
		this.tranche = tranche;
		this.prix = prix;
	}

	public int getTranche() {
		return tranche;
	}

	public void setTranche(int tranche) {
		this.tranche = tranche;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}
	
	
	
	
	
	
}
