package com.hc.json;

import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.hc.utilitaire.Reponse;
import com.hc.utilitaire.ResultatAleaEau;
import com.hc.utilitaire.ResultatAleaElec;

public class ConsAleaElecJson {

	
public static String constructJson(ResultatAleaElec reponse) {
		
		JSONObject obj = new JSONObject();
		
		if(reponse.getEtat()){
			
			try {
				obj.put("etat", reponse.getEtat());
				obj.put("du_mois", reponse.getDu_mois());
				obj.put("du_annee", reponse.getDu_annee());
				obj.put("quantite_mois", reponse.getQuantite_mois());
				obj.put("tranche_mois", reponse.getTranche_mois());
				obj.put("prix_mois", reponse.getPrix_mois());
				
				obj.put("last_consult", reponse.getLast_consult()); // date du dérniére consultation
				obj.put("quantite_last", reponse.getQuantite_last());
				obj.put("tranche_last", reponse.getTranche_last());
				obj.put("prix_last", reponse.getPrix_last());
				obj.put("rcmd_mois", reponse.getRcmd_mois());
				obj.put("rcms_last", reponse.getRcms_last());
					
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
		}else{
			
			try {
				obj.put("etat", reponse.getEtat());
				obj.put("message", reponse.getMsg_erreur());
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return obj.toString();
		
	}


}
