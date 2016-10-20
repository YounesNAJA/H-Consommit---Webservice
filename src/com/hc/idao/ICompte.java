package com.hc.idao;

import com.hc.model.Keys;
import com.hc.utilitaire.Reponse;

public interface ICompte {
	
	
	public Keys verifierCle(String cle);
	
	public Reponse creeCompte(String login, String pwd, String email, String numCompteur, String cle_activation);
	
	public Reponse login(String login, String pwd, String numCompteur);
	
	public Reponse ChangerCompteur(String numCompteur);
	
	public Reponse reinitialiserCompteur(String numCompteur);
	
	

}
