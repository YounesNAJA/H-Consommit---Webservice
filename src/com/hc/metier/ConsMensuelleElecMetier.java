package com.hc.metier;

import com.hc.daoImpl.ConsMensuelleElecImpl;
import com.hc.json.ConsMensuelleElecJson;
import com.hc.utilitaire.ResultatMoisElec;


public class ConsMensuelleElecMetier {
	
	
	ConsMensuelleElecImpl daocme; 
	

	public ConsMensuelleElecMetier() {
		this.daocme = new ConsMensuelleElecImpl();
	}
		
	public String addConsMois(int id_user,Long chiffre){				
				
		ResultatMoisElec reponse = daocme.addConsMois(id_user, chiffre);
		System.out.println("prix apres : "+reponse.getPrix());
		System.out.println("Quantite apres : "+reponse.getQuantite());
		return ConsMensuelleElecJson.constructJSON_ncm(reponse);
	}
		
	public String getQte(int id_user, String mois, String annee){				
		return  daocme.calculConsommationDh(id_user, mois, annee)+"";
	}	
	
	

}

