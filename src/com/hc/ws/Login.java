package com.hc.ws;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.hc.metier.CompteMetier;
import com.hc.metier.ConsMensuelleEauMetier;


@Path("/login")
public class Login { 	
	
	CompteMetier metier = new CompteMetier();
	
	@GET
	@Path("/dologin")
    @Produces(MediaType.APPLICATION_JSON)  
    public String doLogin(@QueryParam("login") String login, @QueryParam("pwd") String pwd, @QueryParam("num_compteur") String numCompteur){    	    					    
    	
		return metier.login(login, pwd, numCompteur);
		
    }
	
	@GET
	@Path("/creercompte")
    @Produces(MediaType.APPLICATION_JSON)  
    public String docreecompte(@QueryParam("login") String login, @QueryParam("pwd") String pwd, @QueryParam("num_compteur") String numCompteur
    		,@QueryParam("mail") String email, @QueryParam("cle_activation") String cle_activation){    	    					    
    	
		return metier.creeCompte(login, pwd, email, numCompteur, cle_activation);
		
    }
	
	
	@GET
	@Path("/ws")
    @Produces(MediaType.APPLICATION_JSON)  
    public String dotest(){    	    					    
    	
		return "Awesome.. Your web services are working a rass teng :D ";
		
    }
    
}