package com.hc.metier;


import com.hc.daoImpl.ConsAleaElecImpl;
import com.hc.json.ConsAleaElecJson;

public class ConsAleatoireElecMetier {
	
	
	ConsAleaElecImpl daocae; 
	
	public ConsAleatoireElecMetier() {
		this.daocae = new ConsAleaElecImpl();
	}
		
	
	public String addConsAlea(int id_user, long chiffre_compteur){					
		return ConsAleaElecJson.constructJson((daocae.newConsultationAlea(id_user, chiffre_compteur)));
	}
	
	

}
