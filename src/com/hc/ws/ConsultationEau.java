package com.hc.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.hc.metier.ConsAleatoireEauMetier;
import com.hc.metier.ConsMensuelleEauMetier;
import com.hc.utilitaire.ResultatMoisEau;

@Path("/eau")
public class ConsultationEau {

	
	ConsMensuelleEauMetier metier_mois = new ConsMensuelleEauMetier();
	ConsAleatoireEauMetier metier_alea = new ConsAleatoireEauMetier();
	
	@GET
	@Path("/nouvelleconsultation")
    @Produces(MediaType.APPLICATION_JSON)  	
    public String nouvelleConsultationMensuelle(@QueryParam("id_user") int id_user, @QueryParam("chiffre_compteur") long chiffre_compteur){    	    					        	

		System.out.println(id_user);
		System.out.println(chiffre_compteur);		
		return metier_mois.addConsMois(id_user,chiffre_compteur);
		
    }


	@GET
	@Path("/quantite")
    @Produces(MediaType.APPLICATION_JSON)  
    public String doLogin(@QueryParam("id_user") int id_user,@QueryParam("mois") String mois,@QueryParam("annee") String annee){    	    					        	
		return metier_mois.getQte(id_user, mois, annee);
    }

	
	@GET
	@Path("/nouvellealea")
    @Produces(MediaType.APPLICATION_JSON)  	
    public String nouvelleConsultationAleatoire(@QueryParam("id_user") int id_user, @QueryParam("chiffre_compteur") long chiffre_compteur){    	    					        				
		return metier_alea.addConsAlea(id_user, chiffre_compteur);		
    }
	
	@GET
	@Path("/histome")
    @Produces(MediaType.APPLICATION_JSON)  	
    public String historiqueMensEau(@QueryParam("id_user") int id_user){    	    					        							
		return metier_mois.getHistoMensEau(id_user);
    }
	
	
	
}











