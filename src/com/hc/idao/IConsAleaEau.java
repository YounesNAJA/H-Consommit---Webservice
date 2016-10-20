package com.hc.idao;

import java.util.ArrayList;

import com.hc.utilitaire.Reponse;
import com.hc.utilitaire.ResultatAleaEau;

public interface IConsAleaEau {
	
	public ResultatAleaEau newConsultationAlea(int id_user,long chiffreCompteur);
	
	public ResultatAleaEau calculConsultation(int id_user);
	
	
	
	
	
	public ArrayList<ResultatAleaEau> getHistoriqueAlea(int id_user);
	
	
	
}
