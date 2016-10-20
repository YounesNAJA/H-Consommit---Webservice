package com.hc.utilitaire;

import java.io.Serializable;

public class Reponse implements Serializable{

	int code;
	boolean etat;
	String msg;
	
	public Reponse(int code, boolean etat, String msg) {
		super();
		this.code = code;
		this.etat = etat;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isEtat() {
		return etat;
	}

	public void setEtat(boolean etat) {
		this.etat = etat;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Reponse [code=" + code + ", etat=" + etat + ", msg=" + msg + "]";
	}
	
	
	
	
}
