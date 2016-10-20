package com.hc.metier;

import com.hc.daoImpl.CompteImpl;
import com.hc.json.CompteJson;
import com.hc.utilitaire.Reponse;

public class CompteMetier {

	CompteImpl daocompte; 
	
	public CompteMetier() {
		this.daocompte = new CompteImpl();
	}
		
	public String login(String login, String pwd, String numCompteur) {		
		
		Reponse reponse = daocompte.login(login, pwd, numCompteur);
		return CompteJson.constructJSON(reponse);
		
	}
		
	public String creeCompte(String login, String pwd, String email, String numCompteur, String cle_activation) {								
		return  CompteJson.constructJSON(daocompte.creeCompte(login, pwd, email, numCompteur, cle_activation));		
	}
}
