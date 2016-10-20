package com.hc.json;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.hc.utilitaire.Reponse;
import com.hc.utilitaire.ResultatMoisEau;

public class ConsMensuelleEauJson {

	
	// newConsultationMoi
	public static String constructJSON(Reponse reponse) {
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("Code", reponse.getCode());
			obj.put("Message", reponse.getMsg());
			obj.put("Etat", reponse.isEtat());	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
		
	}
	
public static String constructJSON_ncm(ResultatMoisEau reponse) {
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("quantite", reponse.getQuantite());
			obj.put("prix", reponse.getPrix());
			obj.put("compare", reponse.getCompareMois());
			obj.put("message", reponse.getReponse().getMsg());
			obj.put("etat", reponse.getReponse().isEtat());
			obj.put("code", reponse.getReponse().getCode());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
		
	}
	
	
}
