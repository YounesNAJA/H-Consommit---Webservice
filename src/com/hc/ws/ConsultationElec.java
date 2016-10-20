package com.hc.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.hc.metier.ConsAleatoireEauMetier;
import com.hc.metier.ConsAleatoireElecMetier;
import com.hc.metier.ConsMensuelleEauMetier;
import com.hc.metier.ConsMensuelleElecMetier;
import com.hc.utilitaire.ResultatMoisEau;

@Path("/elec")
public class ConsultationElec {

	
	ConsMensuelleElecMetier metier_mois = new ConsMensuelleElecMetier();
	ConsAleatoireElecMetier metier_alea = new ConsAleatoireElecMetier();
	
	@GET
	@Path("/nouvelleconsultation")
    @Produces(MediaType.APPLICATION_JSON)  	
    public String nouvelleConsultationMensuelle(@QueryParam("id_user") int id_user, @QueryParam("chiffre_compteur") long chiffre_compteur){    	    					        	

		System.out.println(id_user);
		System.out.println(chiffre_compteur);		
		return metier_mois.addConsMois(id_user,chiffre_compteur);
		
    }
	
	@GET
	@Path("/nouvellealea")
    @Produces(MediaType.APPLICATION_JSON)  	
    public String nouvelleConsultationAleatoire(@QueryParam("id_user") int id_user, @QueryParam("chiffre_compteur") long chiffre_compteur){    	    					        				
		return metier_alea.addConsAlea(id_user, chiffre_compteur);		
    }
}











