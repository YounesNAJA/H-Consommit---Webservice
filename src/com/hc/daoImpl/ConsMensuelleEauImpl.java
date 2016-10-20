package com.hc.daoImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.hc.idao.IConsMensuelleEau;
import com.hc.model.Compte;
import com.hc.model.MensuelleEau;
import com.hc.model.TrancheAssainissementEau;
import com.hc.model.TrancheTarifEau;
import com.hc.utilitaire.Reponse;
import com.hc.utilitaire.ResultatAleaElec;
import com.hc.utilitaire.ResultatMoisEau;


public class ConsMensuelleEauImpl implements IConsMensuelleEau {

	public Configuration configuration;
	public SessionFactory sessionfactory;
	public Session session;

	private String annee, mois;
	int tranche;

	public ConsMensuelleEauImpl() {
		this.configuration = new Configuration().configure("com/hc/util/hibernate.cfg.xml");
		this.sessionfactory = configuration.buildSessionFactory();
		this.session = sessionfactory.openSession();
	}	

	// ******************* Ajouter une nouvelle consultation du mois eau  *****************************

	@Override
	public ResultatMoisEau addConsMois(int id_user, long chiffre) {

		if(chiffre > 1000000 )	
			return new ResultatMoisEau(0, 0, new Reponse(0, false,  "Veuillez verifier le chiffre que vous avez saisi"));
		
		this.traiteDate();

		if(isUserExist(id_user)){

			if(isFirstConsult(id_user)) {

				Compte compte = getUser(id_user);

				if(compte != null){
					Transaction tx = this.session.beginTransaction();
					MensuelleEau me = new MensuelleEau(compte, this.annee, this.mois, new Date(), chiffre, 0);
					session.persist(me);
					tx.commit();					
					return new ResultatMoisEau(0, 0, new Reponse(1, true, "Initialisation du compteur effectuée avec succès ! n'oubliez pas d'effectuer une nouvelle consultation en  "+ convertMois((Integer.parseInt(this.mois) + 1)+"") +"  pour savoir votre consommation pendant ce mois"));
				}
				return new ResultatMoisEau(0, 0, new Reponse(0, false, " Une erreur est survenue, veuillez réesseyer plus tard ! "));				
			}

			if(! issetMois(id_user)){
				try{						 		
					Transaction tx = this.session.beginTransaction();	
					Compte compte = (Compte)session.get("com.hc.model.Compte", id_user);
					MensuelleEau me = new MensuelleEau(compte, this.annee, this.mois , new Date(), chiffre , 0);
					session.persist(me);
					tx.commit();	
					ResultatMoisEau rep = calculerQte(id_user);
					if(! rep.getReponse().isEtat()){
						Transaction tx2 = this.session.beginTransaction();
						session.delete(me);
						tx.commit();
					}

					return rep;
				}
				catch (HibernateException he)
				{		
					System.out.println(he.getMessage());        						
					return new ResultatMoisEau(0, 0, new Reponse(0, false, "Consultation du mois "+ convertMois(this.mois)+" non ajoutée"));
				}
			}else {
				return new ResultatMoisEau(0, 0, new Reponse(0, false, "Vous avez déjà effectuer une consultation en "+ convertMois( this.mois)));				
			}
		}else{
			return new ResultatMoisEau(0, 0, new Reponse(0, false, "Utilisateur n'existe pas"));			
		}
	}


	@Override
	public ResultatMoisEau calculerQte(int id_user) {		

		try{	

			Transaction tx = this.session.beginTransaction();		
			String query = "select m from MensuelleEau m where id_compte = "+ id_user + " and mois = " + this.mois+ " and annee = " + this.annee;
			ArrayList<MensuelleEau> mensuelleEau = (ArrayList<MensuelleEau>) session.createQuery(query).list();

			if( mensuelleEau.size() == 0 ){														
				tx.commit();								
			}else{

				MensuelleEau me = mensuelleEau.get(0);				
				int year = Integer.parseInt(me.getAnnee());
				int month = Integer.parseInt(me.getMois());
				int pos = 0;										
				MensuelleEau cons = null;

				while( cons == null ){		
					month--;
					pos++;				
					if(pos == 12) 
						return new ResultatMoisEau(0, 0,  new Reponse(0, false, "Vous n'avez effectué aucune consultation depuis 12 mois, l'application ne fonctionnera pas bien ! veuillez reinitialiser votre compteur et commencer à nouveau"));														
					if( month == 0) {							
						year = year - 1 ;						
						month = 12;						
					}

					query = "select m from MensuelleEau m where id_compte = "+ id_user + " and mois = '" + month + "' and annee = '" + year + "'";
					ArrayList<MensuelleEau> listeCons = (ArrayList<MensuelleEau>) session.createQuery(query).list();

					if( listeCons.size() != 0 ){																						
						cons = listeCons.get(0);						
					}				
				}

				long qte =  me.getChiffreCompteur() - cons.getChiffreCompteur() ;

				if( qte < 0 ) return new ResultatMoisEau(0, 0,  new Reponse(0, false, "Le chiffre que vous avez saisi est incorrecte !"));
				if( pos != 1) {

					Compte compte = (Compte)session.get("com.hc.model.Compte", id_user);					
					long chiffre = cons.getChiffreCompteur();					
					long qte_mois =  qte / pos;					
					int month_cons = Integer.parseInt(cons.getMois());
					int year_cons  = Integer.parseInt(cons.getAnnee());

					cons.setQuantite(qte_mois);
					session.update(cons);

					int count = month_cons + pos;
					for( int i = month_cons  ; i < count - 1; i++){						
						month_cons ++;						
						if( month_cons == 13) {							
							year_cons = year_cons + 1;						
							month_cons = 1;							
						}					

						chiffre = chiffre + qte_mois;						
						MensuelleEau new_me = new MensuelleEau(compte, year_cons+"", month_cons +"", new Date(), chiffre , qte_mois);								
						session.persist(new_me);												
					}

				}else{
					cons.setQuantite(qte);
					session.update(cons);									
				}			
				tx.commit();		

				int m = Integer.parseInt(me.getMois());
				int a = Integer.parseInt(me.getAnnee());

				m = m -1;
				if( m == 0) {

					m = 12;
					a = a - 1;
				}

				double consommation = calculConsommationDh(id_user, m+"" ,a+"");

				long quantite = getQte(id_user, m+"" ,a+"");

				String compareMois = comparerMois(id_user, quantite, m+"", a+"");
			

				ResultatMoisEau rma =  new ResultatMoisEau(quantite, consommation, compareMois, new Reponse(this.tranche, true, "Calcul de votre consommation pour le mois "+ convertMois( (Integer.parseInt(this.mois)-1)+"") +" : ")); 
				return rma; 
			}				
		}
		catch (HibernateException he)
		{		
			System.out.println(he.getMessage());        				
			return new ResultatMoisEau(0, 0, new Reponse(0, false, "Echec calcul de la quantité"));
		}


		return null;

	}

	// *********************** Récupération d'historique Mensuel Eau  ************************************************
	
	public void getHistoMensEau(int id_user){
				
		
		if(isUserExist(id_user)){
	
			Compte compte = getUser(id_user);
	
			try{	
	
				Transaction tx = this.session.beginTransaction();	
				String query = "select m from MensuelleEau m where id_compte = "+ id_user +" order by floor(annee) DESC,floor(mois) DESC";		
				ArrayList<MensuelleEau> histoMensEau = (ArrayList<MensuelleEau>) session.createQuery(query).list();				
				if( histoMensEau.size() == 0 ){														
					tx.commit();								
				}else{
				
					for(int i = 0 ; i < histoMensEau.size() ; i++ )
					System.out.println("List " +  histoMensEau.get(i).getMois() + "/"+ histoMensEau.get(i).getAnnee());
				
				}
			
			}catch(Exception ex){
				
				System.out.println("Exception");
			}
			
		}else
			System.out.println("Pas de liste");
			//return null; // user doesn't exist
	}
	
	
	// *********************** Calcul de la consommation ************************************************

	public double calculConsommationDh(int id_user, String mois, String annee){

		long quantite = getQte(id_user, mois, annee);

		double consommation = 0;

		try{						 		
			Transaction tx = this.session.beginTransaction();	

			ArrayList<TrancheTarifEau> liste = (ArrayList<TrancheTarifEau>) session.createCriteria("com.hc.model.TrancheTarifEau").list();


			if( liste.size() != 0 ){										

				// Tranche 1
				if( quantite > liste.get(0).getDu() && quantite <= liste.get(0).getA()){
					this.tranche = 1;
					consommation = ( quantite * liste.get(0).getPrixTtc())+ liste.get(0).getRedevance();					
				}
				// Tranche 2
				else if( quantite > liste.get(1).getDu() && quantite <= liste.get(1).getA()){

					this.tranche = 2;
					double temp1 =  liste.get(0).getA();
					double temp2 =  quantite - liste.get(0).getA();					
					consommation = (temp1 * liste.get(0).getPrixTtc()) + (temp2 * liste.get(1).getPrixTtc()) + liste.get(1).getRedevance();					

				}
				// Tranche 3 sélective
				else if( quantite > liste.get(2).getDu() && quantite <= liste.get(2).getA()){

					this.tranche = 3;
					consommation = ( quantite * liste.get(2).getPrixTtc())+ liste.get(2).getRedevance();					

				}
				// Tranche 4 sélective
				else if( quantite > liste.get(3).getDu() && quantite <= liste.get(3).getA()){

					this.tranche = 4;
					consommation = ( quantite * liste.get(3).getPrixTtc())+ liste.get(3).getRedevance();							

				}
				// Tranche 5 sélective
				else if( quantite > liste.get(4).getDu()){

					this.tranche = 5;
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


			return consommation;
		}
		catch (HibernateException he)
		{		
			System.out.println(he.getMessage());        	
			return 0;
		}


	}

	@Override
	public String comparerMois(int id_user, long quantite, String mois, String annee){

		ArrayList<MensuelleEau> me = new ArrayList<MensuelleEau>();
		String str = "";
		long moyenne = 0;
		
		int year = Integer.parseInt(annee);
		int month = Integer.parseInt(mois);
		int pos = 0;										

		while(pos < 12){	
			month--;
			pos++;																					
			if( month == 0) {							
				year = year - 1 ;						
				month = 12;						
			}

			String query = "select m from MensuelleEau m where id_compte = "+ id_user + " and mois = '" + month + "' and annee = '" + year + "'";
			ArrayList<MensuelleEau> listeCons = (ArrayList<MensuelleEau>) session.createQuery(query).list();

			if( listeCons.size() != 0 ){																						
				me.add(listeCons.get(0));
				System.out.println(listeCons.get(0).getQuantite()+"");
			}						
		}

		
		
		boolean plus_eleve = false;
		boolean plus_bas = false;

		if( me.size() != 0){			
			
			long temp = me.get(0).getQuantite() ;

			for( int i = 1; i < me.size() ; i++ ){								
				if( temp > me.get(i).getQuantite()){
					temp = me.get(i).getQuantite();
				}							
			} 

			if( quantite < temp ) plus_bas = true;
			
			for( int i = 1; i < me.size() ; i++ ){								
				if( temp < me.get(i).getQuantite()){
					temp = me.get(i).getQuantite();
				}						
			} 

			if( quantite > temp ) plus_eleve = true;
		
			for( int i = 1; i < me.size() ; i++ ){								
				moyenne = moyenne + me.get(i).getQuantite();				
			} 
			
			moyenne = moyenne / me.size();
			
		}
		
		if( quantite > moyenne ) {
			str = str + "\n Vous avez dépassé votre consommation moyenne ! \n ";			
		}
		
		if( quantite < moyenne ) {
			str = str + "\n Bravo.. Vous avez consommé une quantité inférieure à votre consommation moyenne ! \n ";			
		}
		
		if(plus_bas) {
			str = str + "\n Votre consommation pour ce mois est la plus basse sur les 12 mois précédents !";
			str = str+ "\n Bravo, Veuillez gardez ce rythme de consommation ! \n";
		}
		if(plus_eleve) {
			str = str + "\n Votre consommation pour ce mois est la plus élevée sur les 12 mois précédents !";
			str = str+ "\n Attention, Vous consommez beaucoup d'énergie ! \n";
		}		
		
		if(this.tranche == 1) {			
			str = str+ "\n Vous êtes dans la première tranche... Félicitations pour vos efforts, vous êtes un vrai Ecolowiste !";
		}
		if(this.tranche == 2) {			
			str = str+ "\n Vous êtes dans la deuxième tranche... Bon effort, mais essayez de ne pas dépasser cette tranche !";
		}
		if(this.tranche == 3) {			
			str = str+ "\n Vous êtes dans la troisième tranche... Faite attention à votre consommation !";
		}
		if(this.tranche == 4) {			
			str = str+ "\n Vous êtes dans la quatrième tranche... Vous avez un avertissement, vous consommez trop d'eau !";
		}
		if(this.tranche == 5) {			
			str = str+ "\n Vous êtes dans la cinquième tranche... Vous avez une alerte, vous devez impérativement diminuer votre consommation !";
		}
		
		return str;


	}


	@Override
	public ArrayList<MensuelleEau> getListeEauA(int id_user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MensuelleEau> getListeEauB(int id_user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MensuelleEau> getListeEauC(int id_user) {
		// TODO Auto-generated method stub
		return null;
	}




	// ********************************** Helper methodes **********************************************

	@Override
	public long getQte(int id_user, String mois, String annee) {

		if(isUserExist(id_user)){
			try{						 		
				Transaction tx = this.session.beginTransaction();	
				String query = "select m from MensuelleEau m where id_compte = " + id_user + " and mois = '" + mois + "' and annee = '"+ annee +"'";
				ArrayList<MensuelleEau> mensuelleEau = (ArrayList<MensuelleEau>) session.createQuery(query).list();				
				if( mensuelleEau.size() == 0 ){										
					tx.commit();					
					return 0;
				}else{
					tx.commit();						
					return mensuelleEau.get(0).getQuantite();	
				}	
			}
			catch (HibernateException he)
			{		
				System.out.println(he.getMessage());        	
				return 0;
			}				
		}
		else return 0;	
	}

	@Override
	public boolean issetMoisUser(int id_user,String mois, String annee) {
		try{						 		
			Transaction tx = this.session.beginTransaction();	

			String query = "select m from MensuelleEau m where id_compte = "+ id_user + " and mois = " + mois + " and annee = " + annee;
			ArrayList<MensuelleEau> mensuelleEau = (ArrayList<MensuelleEau>) session.createQuery(query).list();
			tx.commit();				

			if( mensuelleEau.size() == 0 ){										
				return false;
			}else{
				return true;	
			}						
		}
		catch (HibernateException he)
		{		
			System.out.println(he.getMessage());        	
			return false;
		}

	}

	@Override
	public boolean issetMois(int id_user) {
		try{						 		
			Transaction tx = this.session.beginTransaction();	

			String query = "select m from MensuelleEau m where id_compte = "+ id_user + " and mois = " + this.mois+ " and annee = " + this.annee;
			ArrayList<MensuelleEau> mensuelleEau = (ArrayList<MensuelleEau>) session.createQuery(query).list();
			tx.commit();				

			if( mensuelleEau.size() == 0 ){										
				return false;
			}else{
				return true;	
			}						
		}
		catch (HibernateException he)
		{		
			System.out.println(he.getMessage());        	
			return false;
		}

	}

	@Override
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


	@Override
	public void traiteDate(){	

		Date date = new Date();
		int _jour = Integer.parseInt( new SimpleDateFormat("dd").format(date));
		int _mois = Integer.parseInt(new SimpleDateFormat("MM").format(date));
		int _annee = Integer.parseInt(new SimpleDateFormat("YYYY").format(date));

		if( _jour >= 25 ){

			if( _mois == 12){				
				this.mois = "1";
				int year = _annee + 1;
				this.annee = year + "";				
			}else{
				int month = _mois + 1;
				this.mois = month + "";	
				this.annee = _annee + "";				
			}

		}else{

			if( _jour == 1 && _mois == 1 ){
				int year = _annee + 1;
				this.annee = year + "";
				this.mois = _mois + "";
			}
			else{
				this.mois = _mois + "";
				this.annee = _annee + "";
			}
		}

	}

	public boolean isFirstConsult(int id_user){

		try{						 		
			Transaction tx = this.session.beginTransaction();	

			String query = "select m from MensuelleEau m where id_compte = "+ id_user;
			ArrayList<MensuelleEau> mensuelleEau = (ArrayList<MensuelleEau>) session.createQuery(query).list();
			tx.commit();				

			if( mensuelleEau.size() == 0 ){										
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
			return "Mois innexistant";
		}		
	}



	// ************************************  Getters and Setters **************************************************


	public String getAnnee() {
		return annee;
	}

	public void setAnnee(String annee) {
		this.annee = annee;
	}

	public String getMois() {
		return mois;
	}

	public void setMois(String mois) {
		this.mois = mois;
	}	

	public int getTranche() {
		return tranche;
	}

	public void setTranche(int tranche) {
		this.tranche = tranche;
	}


}
