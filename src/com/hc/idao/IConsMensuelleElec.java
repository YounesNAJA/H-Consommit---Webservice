package com.hc.idao;


import com.hc.utilitaire.ResultatMoisElec;


public interface IConsMensuelleElec {

	
	public boolean issetMois(int id_user);
	
	public boolean isUserExist(int id_user);
	
	public void traiteDate();
	
	public ResultatMoisElec addConsMois(int id_user, long chiffre);
	
	public ResultatMoisElec calculerQte(int id_user);
	
	public long getQte(int id_user, String mois, String annee);
	
	public double calculConsommationDh(int id_user, String mois, String annee);
	
	public boolean issetMoisUser(int id_user,String mois, String annee); 
	
	public String comparerMois(int id_user, long quantite, String mois, String annee);
	
	
	
		
	
}
