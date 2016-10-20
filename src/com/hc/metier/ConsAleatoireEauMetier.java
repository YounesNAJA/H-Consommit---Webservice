package com.hc.metier;

import com.hc.daoImpl.ConsAleaEauImpl;
import com.hc.daoImpl.ConsMensuelleEauImpl;
import com.hc.json.ConsAleaEauJson;

public class ConsAleatoireEauMetier {
	
	
	ConsAleaEauImpl daocae; 
	
	public ConsAleatoireEauMetier() {
		this.daocae = new ConsAleaEauImpl();
	}
		
	
	public String addConsAlea(int id_user, long chiffre_compteur){					
		return ConsAleaEauJson.constructJson(daocae.newConsultationAlea(id_user, chiffre_compteur));
	}
	
	

}
