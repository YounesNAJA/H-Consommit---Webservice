package com.hc.daoImpl;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.hc.idao.ICompte;
import com.hc.model.Compte;
import com.hc.model.Keys;
import com.hc.utilitaire.Reponse;



public class CompteImpl implements ICompte {

	public Configuration configuration;
	public SessionFactory sessionfactory;
	public Session session;

	public CompteImpl() {
		this.configuration = new Configuration().configure("com/hc/util/hibernate.cfg.xml");
		this.sessionfactory = configuration.buildSessionFactory();
		this.session = sessionfactory.openSession();
	}

	@Override
	public Reponse creeCompte(String login, String pwd, String email, String numCompteur, String cle_activation) {

		Keys key = verifierCle(cle_activation);

		if(key != null){
			try{
				Transaction tx = this.session.beginTransaction();
				String query = "select c from Compte c where login = '" + login + "' and num_compteur = '" + numCompteur + "'";
				ArrayList<Compte> compte_existe = (ArrayList<Compte>) session.createQuery(query).list();


				if(compte_existe.size() != 0){
					tx.commit();
					System.out.println("4");					
					return new Reponse(0, false, "Compte existe déjà !");
				}

				session.persist( new Compte(login, pwd, email, numCompteur, cle_activation, new Date()));																													
				session.delete(key);

				tx.commit();

				return new Reponse(1, true, "Compte crée avec succes");

			}catch (HibernateException he)
			{					
				System.out.println(he.getMessage());        	
				return new Reponse(0, true, "Compte n'est pas crée ! Exception");
			}
		}else{
			System.out.println("6");
			return new Reponse(0, false, "Clé d'activation incorrecte");
		}




	}

	@Override
	public Reponse login(String login, String pwd, String numCompteur) {

		try{						 		
			Transaction tx = this.session.beginTransaction();							
			String query = "select c from Compte c where login = '" + login + "' and  pwd = '" + pwd + "' and num_compteur = '" + numCompteur + "'";
			ArrayList<Compte> compte = (ArrayList<Compte>) session.createQuery(query).list();									
			tx.commit();				

			if(compte.size() == 0 ) 
				return new Reponse(0, false, "Aucun utilisateur avec le login : "+ login +" ou mot de passe : "
						+ pwd +" ou numero de compteur : "+ numCompteur);	

			else 				
				return  new Reponse(compte.get(0).getId(), true, "Vous êtes authentifié");	

		}
		catch (HibernateException he)
		{		    	
			System.out.println(he.getMessage());        	
			return new Reponse(0, false, "Aucun utilisateur avec le login : "+ login +" ou mot de passe : "
					+ pwd +" ou numero de compteur : "+ numCompteur);	
		}

	}

	@Override
	public Reponse ChangerCompteur(String numCompteur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reponse reinitialiserCompteur(String numCompteur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Keys verifierCle(String cle) {

		try{										

			Transaction tx = this.session.beginTransaction();
			String query = "select k from Keys k where key = '" + cle + "'";
			ArrayList<Keys> key = (ArrayList<Keys>) session.createQuery(query).list();

			if(key.size() != 0)
				return key.get(0);
			else 
				return null;
		}
		catch (HibernateException he)
		{		 
			System.out.println("10");
			System.out.println(he.getMessage());			
			return null;	
		}


	}




}
