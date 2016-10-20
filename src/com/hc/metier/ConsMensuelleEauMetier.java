package com.hc.metier;

import java.util.ArrayList;

import com.hc.daoImpl.ConsMensuelleEauImpl;
import com.hc.idao.IConsMensuelleEau;
import com.hc.json.ConsMensuelleEauJson;
import com.hc.utilitaire.Reponse;
import com.hc.utilitaire.ResultatMoisEau;

public class ConsMensuelleEauMetier {
	
	
	ConsMensuelleEauImpl daocme; 
	
	public ConsMensuelleEauMetier() {
		this.daocme = new ConsMensuelleEauImpl();
	}
		
	
	public ResultatMoisEau calculConsultation(int id_user ,long chiffreCompteur){
		
		return null;
	}
	
	public ArrayList<ResultatMoisEau> getHistoriqueMoisEau(int id_user){
		
		return null;
	}
	
	
	public String addConsMois(int id_user,Long chiffre){				
				
		ResultatMoisEau reponse = daocme.addConsMois(id_user, chiffre);
		System.out.println("prix apres : "+reponse.getPrix());
		System.out.println("Quantite apres : "+reponse.getQuantite());
		return ConsMensuelleEauJson.constructJSON_ncm(reponse);
	}
	
	
	
	
	
	public String getQte(int id_user, String mois, String annee){				
		return  daocme.calculConsommationDh(id_user, mois, annee)+"";
	}	
	
	

	public String getHistoMensEau(int id_user){				
		
		daocme.getHistoMensEau(id_user);		
		return "blabla";
	}
	
	
	
}

