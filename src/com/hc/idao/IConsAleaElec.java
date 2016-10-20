package com.hc.idao;

import java.util.ArrayList;

import com.hc.utilitaire.Reponse;
import com.hc.utilitaire.ResultatAleaEau;
import com.hc.utilitaire.ResultatAleaElec;

public interface IConsAleaElec {
	
	public ResultatAleaElec newConsultationAlea(int id_user,long chiffreCompteur);
	
	public ResultatAleaElec calculConsultation(int id_user);
	
	
	public ArrayList<ResultatAleaEau> getHistoriqueAlea(int id_user);
	
	
	
}
