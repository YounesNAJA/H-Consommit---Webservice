package com.hc.daoImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.hc.idao.IConsAleaEau;
import com.hc.model.AleatoireEau;
import com.hc.model.Compte;
import com.hc.model.MensuelleEau;
import com.hc.model.TrancheAssainissementEau;
import com.hc.model.TrancheTarifEau;
import com.hc.utilitaire.CalculConsommation;
import com.hc.utilitaire.ResultatAleaEau;
import com.hc.utilitaire.ResultatAleaElec;



public class ConsAleaEauImpl implements IConsAleaEau {

	public Configuration configuration;
	public SessionFactory sessionfactory;
	public Session session;

	public ConsAleaEauImpl() {
		this.configuration = new Configuration().configure("com/hc/util/hibernate.cfg.xml");
		this.sessionfactory = configuration.buildSessionFactory();
		this.session = sessionfactory.openSession();
	}
	
	@Override
	public ResultatAleaEau newConsultationAlea(int id_user, long chiffreCompteur) {

		if(chiffreCompteur > 1000000 )
			return new ResultatAleaEau(false, "Veuillez verifier le chiffre que vous avez saisi");
		
		Compte compte = getUser(id_user);

		if(compte != null){

			if(isFirstConsult(id_user)) {

				Transaction tx = this.session.beginTransaction();
				AleatoireEau ae = new AleatoireEau(compte, new Date(), chiffreCompteur, 0);
				session.persist(ae);
				tx.commit();

				return new ResultatAleaEau(false, "Vous avez initialisé votre compte, dès maintenant vous pouvez consuler votre consommation");
			}
			else{
				try{						 		
				
					Transaction tx = this.session.beginTransaction();		
					String query = "select a from AleatoireEau a where id_compte = " + id_user + " order by date_consultation desc";
					ArrayList<AleatoireEau> aleatoireEau = (ArrayList<AleatoireEau>) session.createQuery(query).list();
					tx.commit();

					if( aleatoireEau.size() != 0 ){																			

						AleatoireEau last = aleatoireEau.get(0);

						if( chiffreCompteur < last.getChiffreCompteur() ) 
							return new ResultatAleaEau(false, "Veuillez verifier le chiffre que vous avez saisi");
						
						
						long quantite_last = chiffreCompteur - last.getChiffreCompteur();						
						CalculConsommation cc_last = calculConsommationDh(quantite_last);
						
						//quantite_last
						double prix_last =  cc_last.getPrix();
						int  tranche_last = cc_last.getTranche();						
						Date last_consult = last.getDateConsultation();												
						String rcms_last = "";												
						
						
						tx = this.session.beginTransaction();																								
						query = "select m from MensuelleEau m where id_compte = "+ id_user + " and mois = '" + getMonth() + "' and annee = '" + getYear() + "'";
						ArrayList<MensuelleEau> mensuelleEau = (ArrayList<MensuelleEau>) session.createQuery(query).list();
						tx.commit();	
						
						if( mensuelleEau.size() != 0 ){														

							MensuelleEau me = mensuelleEau.get(0);
							
							if( chiffreCompteur < me.getChiffreCompteur() ) 
								return new ResultatAleaEau(false, "Veuillez verifier le chiffre que vous avez saisi");
							
							long quantite_mois = chiffreCompteur - me.getChiffreCompteur();						
							CalculConsommation cc_mois = calculConsommationDh(quantite_mois);
						
							//quantite_mois;
							double prix_mois =  cc_mois.getPrix();
							int tranche_mois = cc_mois.getTranche();
							String du_mois = getMonth();
							String du_annee = getYear();
							String rcmd_mois = "";
							
							switch (tranche_mois) {
							
							case 1:
								rcmd_mois = "Veuillez garder ce rythme de consommation";
								break;
							case 2:
								
								if( getDay() < 10 )
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la deuxième tranche ! essayez de contrôler votre consommation" ;
								else if( getDay() > 20 )
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la deuxième tranche ! c'est géniale, cependant essayez de garder ce rythme afin de ne pas passer à la troisième tranche " ;
								else
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la deuxième tranche ! Essayez de ne pas depasser cette tranche" ;
								break;
							case 3:
								if( getDay() < 10 )
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la troisième tranche ! vous risquez de passer à la quatrième, et du coup vous risquez d'augmenter votre consommation ! " ;
								else if( getDay() > 20 )
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la troisième tranche ! vous risquez toujours de passer à la quatrième, et du coup vous risquez d'augmenter votre consommation ! " ;
								else
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la troisième tranche ! penser à votre future consommation !" ;
								break;
							case 4:
								if( getDay() < 10 )
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la quatrième tranche ! Attention vous consommez beaucoup " ;
								else if( getDay() > 20 )
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la quatrième tranche ! Attention à votre consommation" ;
								else
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la quatrième tranche ! Faite attention " ;
								break;
							case 5:
								if( getDay() < 10 )
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la cinquième tranche ! c'est devenu intolérable" ;
								else if( getDay() > 20 )
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la cinquième tranche ! Vous aurez une facture colossale";
								else
									rcmd_mois = "C'est le " + getDay() +", et vous êtes déjà dans la cinquième tranche ! Attention vous avez dépassé la limite de consommation" ;
								break;
							}
							
							tx = this.session.beginTransaction();																								
							AleatoireEau ae = new AleatoireEau(compte, chiffreCompteur, quantite_last);
							session.persist(ae);
							tx.commit();
							
							return new ResultatAleaEau(true, du_mois, du_annee, quantite_mois, tranche_mois, prix_mois, last_consult, quantite_last, tranche_last, prix_last, rcmd_mois, rcms_last);
							
						}				

						tx = this.session.beginTransaction();																								
						AleatoireEau ae = new AleatoireEau(compte, chiffreCompteur, quantite_last);
						session.persist(ae);
						tx.commit();
						
						//return new ResultatAleaEau(false, "Pas de consultation de mois"); // Pas de consultation de mois
						return new ResultatAleaEau(true, "0", "0", 0, 0, 0, last_consult, quantite_last, tranche_last, prix_last, "Vous n'avez pas encore effectué une consultation pour ce mois", rcms_last);
					}					
					return new ResultatAleaEau(false, " Consultation d'initialisation non effectuée !");// Consultation d'initialisation non effectuée !
					
				}
				catch (HibernateException he)
				{		
					System.out.println(he.getMessage());        						
					return new ResultatAleaEau(false, " Calcul n'est pas effectué, veuillez réessayer plus tard s'il vous plaît "); // Erreur hibernate
				}
			}
		}else{			
			return new ResultatAleaEau(false, " Notre serveur ne peut pas vous identifier, veuillez vous reconnecter, puis réessayer la consultation à nouveau s'il vous plaît "); // Utilisateur n'est pas trouvé a7amaaaaldiik			
		}
		
	}


	public CalculConsommation calculConsommationDh(long quantite){

		double consommation = 0;
		int tranche = 0;

		try{						 		
			Transaction tx = this.session.beginTransaction();	

			ArrayList<TrancheTarifEau> liste = (ArrayList<TrancheTarifEau>) session.createCriteria("com.hc.model.TrancheTarifEau").list();


			if( liste.size() != 0 ){										

				// Tranche 1
				if( quantite > liste.get(0).getDu() && quantite <= liste.get(0).getA()){
					tranche = 1;
					consommation = ( quantite * liste.get(0).getPrixTtc())+ liste.get(0).getRedevance();					
				}
				// Tranche 2
				else if( quantite > liste.get(1).getDu() && quantite <= liste.get(1).getA()){

					tranche = 2;
					double temp1 =  liste.get(0).getA();
					double temp2 =  quantite - liste.get(0).getA();					
					consommation = (temp1 * liste.get(0).getPrixTtc()) + (temp2 * liste.get(1).getPrixTtc()) + liste.get(1).getRedevance();					

				}
				// Tranche 3 sélective
				else if( quantite > liste.get(2).getDu() && quantite <= liste.get(2).getA()){

					tranche = 3;
					consommation = ( quantite * liste.get(2).getPrixTtc())+ liste.get(2).getRedevance();					

				}
				// Tranche 4 sélective
				else if( quantite > liste.get(3).getDu() && quantite <= liste.get(3).getA()){

					tranche = 4;
					consommation = ( quantite * liste.get(3).getPrixTtc())+ liste.get(3).getRedevance();							

				}
				// Tranche 5 sélective
				else if( quantite > liste.get(4).getDu()){

					tranche = 5;
					consommation = ( quantite * liste.get(4).getPrixTtc())+ liste.get(4).getRedevance();		

				}


				ArrayList<TrancheAssainissementEau> listeAss = (ArrayList<TrancheAssainissementEau>) session.createCriteria("com.hc.model.TrancheAssainissementEau").list();
				tx.commit();				

				if( listeAss.size() != 0 ){										

					// Tranche 1
					if( quantite > listeAss.get(0).getDe() && quantite <= listeAss.get(0).getA()){

						consommation = consommation + ( quantite * listeAss.get(0).getPrixTtc())+ listeAss.get(0).getRedevance();					
					}
					// Tranche 2
					else if( quantite > listeAss.get(1).getDe() && quantite <= listeAss.get(1).getA()){

						double temp1 =  listeAss.get(0).getA();
						double temp2 =  quantite - listeAss.get(0).getA();					
						consommation = consommation + (temp1 * listeAss.get(0).getPrixTtc()) + (temp2 * listeAss.get(1).getPrixTtc()) + listeAss.get(1).getRedevance();					

					}
					// Tranche 3 sélective
					else if( quantite > listeAss.get(2).getDe() && quantite <= listeAss.get(2).getA()){

						consommation = consommation + ( quantite * listeAss.get(2).getPrixTtc())+ listeAss.get(2).getRedevance();					

					}
				}	

			}


			return new CalculConsommation(tranche, consommation);
		}
		catch (HibernateException he)
		{		
			System.out.println(he.getMessage());        	
			return new CalculConsommation(0, 0);
		}


	}


	@Override
	public ResultatAleaEau calculConsultation(int id_user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ResultatAleaEau> getHistoriqueAlea(int id_user) {
		// TODO Auto-generated method stub
		return null;
	}



	// ********************************** Helper ********************************************


	public boolean isUserExist(int id_user) {


		try{						 		
			Transaction tx = this.session.beginTransaction();							
			Compte compte = (Compte)session.get("com.hc.model.Compte", id_user);									
			tx.commit();				

			if(compte != null) return true;	

		}
		catch (HibernateException he)
		{		    	
			System.out.println(he.getMessage());        	
			return false;
		}
		return false;
	}


	public boolean isFirstConsult(int id_user){

		try{						 		
			Transaction tx = this.session.beginTransaction();	

			String query = "select a from AleatoireEau a where id_compte = "+ id_user;
			ArrayList<AleatoireEau> aleatoireEau = (ArrayList<AleatoireEau>) session.createQuery(query).list();
			tx.commit();				

			if( aleatoireEau.size() == 0 ){										
				return true;
			}else{
				return false;	
			}						
		}
		catch (HibernateException he)
		{		
			System.out.println(he.getMessage());        	
			return false;
		}
	}

	public Compte getUser(int id_user) {

		try{						 		
			Transaction tx = this.session.beginTransaction();							
			String query = "select c from Compte c where id = " + id_user ;
			ArrayList<Compte> compte = (ArrayList<Compte>) session.createQuery(query).list();									
			tx.commit();				

			if(compte.size() == 0 ) 
				return null;
			else 				
				return compte.get(0);	

		}
		catch (HibernateException he)
		{		    	
			return null;	
		}

	}

	public String convertMois(String m){


		if ( m.equals("13")) m = "1";
		if ( m.equals("0")) m = "12";

		switch (m) {
		case "1":
			return "Janvier";		
		case "2":
			return "Fevrier";			
		case "3":
			return "Mars";
		case "4":
			return "Avril";
		case "5":
			return "Mai";
		case "6":
			return "Juin";
		case "7":
			return "Juillet";
		case "8":
			return "Août";
		case "9":
			return "Septembre";			
		case "10":
			return "Octobre";
		case "11":
			return "Novembre";
		case "12":
			return "Decembre";					
		default:
			return "Mois inexistant";
		}		
	}


	public String getYear(){		
		Date date = new Date();		
		DateFormat annee = new SimpleDateFormat("YYYY");			
		return Integer.parseInt(annee.format(date))+"";					
	}

	public String getMonth(){		
		Date date = new Date();		
		DateFormat mois = new SimpleDateFormat("MM");		
		return Integer.parseInt(mois.format(date))+"";					
	}

	public int getDay(){		
		Date date = new Date();		
		DateFormat jour = new SimpleDateFormat("dd");			
		return Integer.parseInt(jour.format(date));						
	}

}
