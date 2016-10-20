package com.hc.idao;

import java.util.ArrayList;

import com.hc.model.MensuelleEau;
import com.hc.utilitaire.Reponse;
import com.hc.utilitaire.ResultatMoisEau;


public interface IConsMensuelleEau {

	
	//public Reponse newConsultationMois(int id_user, long chiffreCompteur);
	

	public boolean issetMois(int id_user);
	
	public boolean isUserExist(int id_user);
	
	public void traiteDate();
	
	public ResultatMoisEau addConsMois(int id_user, long chiffre);
	
	public ResultatMoisEau calculerQte(int id_user);
	
	public long getQte(int id_user, String mois, String annee);
	
	public double calculConsommationDh(int id_user, String mois, String annee);
	
	public boolean issetMoisUser(int id_user,String mois, String annee); 
	
	public String comparerMois(int id_user, long quantite, String mois, String annee);
	
	public ArrayList<MensuelleEau> getListeEauA(int id_user);
	
	public ArrayList<MensuelleEau> getListeEauB(int id_user);
	
	public ArrayList<MensuelleEau> getListeEauC(int id_user);
	
	
	
		
	
}
