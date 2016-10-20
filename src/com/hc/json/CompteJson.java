package com.hc.json;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.hc.utilitaire.Reponse;

public class CompteJson {

	
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


}
