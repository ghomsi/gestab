/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.util.Duration;
import modele.Carte;
import modele.Categorie;
import modele.Client;
import modele.Debit;
import modele.Equipement;
import modele.Historique;
import modele.Notification;
import modele.Port;
import modele.Secteur;
import modele.User;
import modele.Ville;
import notifier.Notifier;
import telnet.Telnet;
import vue.FonctionVue;

/**
 *
 * @author ghomsi
 */
public class DAOequipement extends FonctionVue  {
    private Hashtable numeros;
    private List<String> nomClients;
    private List<String> nomusers;
    private Hashtable villes;
    private Hashtable ports;
    private Hashtable secteurs;
    private Hashtable equipements;
    private Hashtable clients;
    private Hashtable clientVilles;
    private Hashtable cartes;
    private Hashtable debits;
    private Hashtable categories;
    private Hashtable secteurVilles;
    private Hashtable eqSecteurs;
    private Hashtable carteEqs;
    private Hashtable portCartes;
    private Hashtable portClients;
    private Hashtable users;
    private Hashtable userVilles;
    private Hashtable clientHistoriques;
    private Hashtable notificationRecus;
    private Hashtable notificationEmis;
    private IntegerProperty heigth;
    public User user;
    public String login;
    public String passwd;
    public Label labelclientstatut;
    public TextArea textArea;
    public Button btn; 
    public HostServices hostservice;
    public Telnet telnet = null ;
    public Port tempPort1 = null;
    public Port tempPort2 = null;
    public Client client = null;
    
    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    public final double width =primScreenBounds.getWidth();//1000;
    public final double height =primScreenBounds.getHeight();//550;
    
    public DAOequipement(){
        this.user=null;
        this.login = null;
        this.passwd = null;
        this.labelclientstatut=null;
        this.villes = new Hashtable();
        this.ports = new Hashtable();
        this.secteurVilles = new Hashtable();
        this.eqSecteurs= new Hashtable();
        this.secteurs = new Hashtable();
        this.equipements = new Hashtable();
        this.cartes = new Hashtable();
        this.carteEqs = new Hashtable();
        this.portCartes = new Hashtable();
        this.portClients = new Hashtable();
        this.clientVilles = new Hashtable();
        this.debits = new Hashtable();
        this.categories = new Hashtable();
        this.clients = new Hashtable();
        this.users = new Hashtable();
        this.userVilles = new Hashtable();
        this.numeros = new Hashtable();
        this.clientHistoriques = new Hashtable();
        this.notificationRecus = new Hashtable();
        this.notificationEmis = new Hashtable();
        this.nomClients = new ArrayList<String>();
        this.nomusers = new ArrayList<String>();
        this.heigth = new SimpleIntegerProperty(400);
        
        
    }
    
    
    
    
    
    
    public int getHeigth(){
        return heigth.get();
    }
    public void setHeigth(int ht){
        heigth.set(ht);
    }
    
    
    
    public Hashtable getVille(){
        return villes;
    }
    public void setVille(Hashtable fville){
        villes = fville;
    }
    
    public Hashtable getPort(){
        return ports;
    }
    public void setPort(Hashtable fp){
        ports = fp;
    }
    
    public Hashtable getUser(){
        return users;
    }
    public void setUser(Hashtable fc){
        users = fc;
    }
    
    public Hashtable getNotificationRecus(){
        return notificationRecus;
    }
    public void setNotificationRecus(Hashtable fc){
        notificationRecus = fc;
    }
    
    public Hashtable getNotificationEmis(){
        return notificationEmis;
    }
    public void setNotificationEmis(Hashtable fc){
        notificationEmis = fc;
    }
    
    public Hashtable getUserVille(){
        return userVilles;
    }
    public void setUserVille(Hashtable fu){
        userVilles = fu;
    }
    
    public Hashtable getClientHistorique(){
        return clientHistoriques;
    }
    public void setClientHistorique(Hashtable fu){
        clientHistoriques = fu;
    }
    
    public Hashtable getClientVille(){
        return clientVilles;
    }
    public void setClientVille(Hashtable fc){
        clientVilles = fc;
    }
    
    public Hashtable getSecteurVille(){
        return secteurVilles;
    }
    public void setSecteurVille(Hashtable fville){
        secteurVilles = fville;
    }
    
    public Hashtable getEqSecteur(){
        return eqSecteurs;
    }
    public void setEqSecteur(Hashtable fsect){
        eqSecteurs = fsect;
    }
    
    public Hashtable getCarteEq(){
        return carteEqs;
    }
    public void setCarteEq(Hashtable feq){
        carteEqs = feq;
    }
    
    public Hashtable getPortCarte(){
        return portCartes;
    }
    public void setPortCarte(Hashtable fc){
        portCartes = fc;
    }
    
    public Hashtable getPortClient(){
        return portClients;
    }
    public void setPortClient(Hashtable fc){
        portClients = fc;
    }
    
    public Hashtable getSecteur(){
        return secteurs;
    }
    public void setSecteur(Hashtable fsect){
        secteurs = fsect;
    }
    
    public Hashtable getEquipement(){
        return equipements;
    }
    public void setEquipement(Hashtable feq){
        equipements = feq;
    }
    
    public Hashtable getCarte(){
        return cartes;
    }
    public void setCarte(Hashtable fcarte){
        cartes = fcarte;
    }
    
    public Hashtable getClient(){
        return clients;
    }
    public void setClient(Hashtable fclient){
        clients = fclient;
    }
    
    public Hashtable getDebit(){
        return debits;
    }
    public void setDebit(Hashtable fdeb){
        debits = fdeb;
    }
    
    public Hashtable getCategorieForfait(){
        return categories;
    }
    public void setCategorieForfait(Hashtable fcat){
        categories = fcat;
    }
    
    public Hashtable getNumero(){
        return numeros;
    }
    public void setNumero(Hashtable fnum){
        numeros = fnum;
    }
    
    public List<String> getNomClient(){
        return nomClients;
    }
    public void setNomClient(List<String> fnmClt){
        nomClients = fnmClt;
    }
    
    public List<String> getNomUser(){
        return nomusers;
    }
    public void setNomUser(List<String> fnmU){
        nomusers = fnmU;
    }
   
   // chart fonction
    public PieChart  chart(String titre,String name1,int val1,String name2,int val2,String name3,int val3){
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data(name3, val3),
                new PieChart.Data(name2, val2),
                new PieChart.Data(name1, val1));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle(titre);
        final Label caption = new Label("");
        //caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");
        for (final PieChart.Data data : chart.getData()) {
        data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent e) {
                    caption.setTranslateX(e.getSceneX());
                    caption.setTranslateY(e.getSceneY());
                    caption.setText(String.valueOf(data.getPieValue()) + "%");
                 }
            
        });
}
        return chart;
    }
    
    public PieChart  chart(String titre,String name1,int val1,String name2,int val2){
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data(name2, val2),
                new PieChart.Data(name1, val1));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle(titre);
        final Label caption = new Label("");
        //caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");
        for (final PieChart.Data data : chart.getData()) {
        data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent e) {
                    caption.setTranslateX(e.getSceneX());
                    caption.setTranslateY(e.getSceneY());
                    caption.setText(String.valueOf(data.getPieValue()) + "%");
                 }
            
        });
}
        return chart;
    }
    
    
     //--hashtable Client port
    public Hashtable clientPortBd(Connecter c) throws ClassNotFoundException, SQLException{
       Hashtable fclientport = new Hashtable();
       
       Enumeration e = ports.elements();
       
       while(e.hasMoreElements()){
           Port p = (Port) e.nextElement();
           //System.out.println(p.getId());
           int i = getAllClientBDByPortId(p.getId(),c);
           if(i!=0){
            fclientport.put(p.getId(), i);
           }
       }
       
       return fclientport;
   }
    
    
    //--hashtable client ville
    public Hashtable clientVilleBd(Connecter c) throws ClassNotFoundException, SQLException{
       Hashtable fclientville = new Hashtable();
       
       Enumeration e = villes.elements();
       while(e.hasMoreElements()){
           Ville v = (Ville) e.nextElement();
           fclientville.put(v.getId(), getAllClientBDByVilleId(v.getId(),c));
       }
       
       return fclientville;
   }
    
    
    //--hashtable Client Historiques
    public Hashtable clientHistoriqueBd(Connecter c) throws ClassNotFoundException, SQLException{
       Hashtable fclientHistorique = new Hashtable();
       
       Enumeration e = clients.elements();
       while(e.hasMoreElements()){
           Client v = (Client) e.nextElement();
           fclientHistorique.put(v.getId(), getAllClientHistoriqueBDbyId(v.getId(),c));
       }
       
       return fclientHistorique;
   }
    
    //--hashtable user ville
    public Hashtable userVilleBd(Connecter c) throws ClassNotFoundException, SQLException{
       Hashtable fuserville = new Hashtable();
       
       Enumeration e = villes.elements();
       while(e.hasMoreElements()){
           Ville v = (Ville) e.nextElement();
           fuserville.put(v.getId(), getAllUserVilleBDbyId(v.getId(),c));
       }
       
       return fuserville;
   }
    
    //--hashtable port carte
    public Hashtable portCarteBd(Connecter c) throws ClassNotFoundException, SQLException{
       Hashtable fportcarte = new Hashtable();
       
       Enumeration e = cartes.elements();
       while(e.hasMoreElements()){
           Carte ct = (Carte) e.nextElement();
           fportcarte.put(ct.getId(), getAllPortBDByCarteId(ct.getId(),c));
       }
       
       return fportcarte;
   } 
    //-- hashtable carte equipement
    public Hashtable carteEqBd(Connecter c) throws ClassNotFoundException, SQLException{
       Hashtable fcarteeq = new Hashtable();
       
       Enumeration e = equipements.elements();
       while(e.hasMoreElements()){
           Equipement eq = (Equipement) e.nextElement();
           fcarteeq.put(eq.getIdEQ(), getAllCarteBDByEqId(eq.getIdEQ(),c));
       }
       
       return fcarteeq;
   } 
    
    //--hashtable equipement secteur
    public Hashtable eqSecteurBd(Connecter c) throws ClassNotFoundException, SQLException{
       Hashtable feqsecteur = new Hashtable();
       
       Enumeration e = secteurs.elements();
       while(e.hasMoreElements()){
           Secteur sect = (Secteur) e.nextElement();
           feqsecteur.put(sect.getId(), getAllEQBDBySecteurId(sect.getId(),c));
       }
       
       return feqsecteur;
   } 
    // hashtable secteurville
   public Hashtable secteurVilleBd(Connecter c) throws ClassNotFoundException, SQLException{
       Hashtable fsecteurvilles = new Hashtable();
       
       Enumeration e = villes.elements();
       while(e.hasMoreElements()){
           Ville ville = (Ville) e.nextElement();
           fsecteurvilles.put(ville.getId(), getSecteurBDByVilleId(ville.getId(),c));
       }
       
       return fsecteurvilles;
   } 
    
   public User getConnection(String login,String pwd) throws ClassNotFoundException, SQLException{
    User user = null;
    
        
        Connecter c = new Connecter();
        Requettes con = new Requettes(c); 
        ResultSet rs = con.fUserVerification(login, pwd);
                    if(rs.next()){
                        user = new User();
                        user.setId(rs.getInt("id_utilisateur"));
                        user.setNom(rs.getString("nom_utilisateur"));
                        user.setPrenom(rs.getString("prenom_utilisateur"));
                        user.setVille(rs.getInt("id_ville_utilisateur"));
                        user.setQuartier(rs.getString("quartier_utilisateur"));
                        user.setEmail(rs.getString("email_utilisateur"));
                        user.setPasswd(rs.getString("password_utilisateur"));
                        user.setNivoAccess(rs.getInt("categories_utilisateurs"));
                        user.setDesc(rs.getString("raison_sociale_utilisateur"));
                        user.setNumTel(rs.getString("telephone_utilisateur"));
                        user.setLogin(rs.getString("nom_connexion_utilisateur"));
                        //System.out.println(user.getNivoAccess());
                        user.setRoute(c.getRoute());
                        refresh(c,user);
                        c.getConnexion().close();
                    }
    
    return user;
   }
   
   public void refresh(Connecter c,User user) throws ClassNotFoundException, SQLException{
        debits = getDebitBD(c);
        categories = getCategorieBD(c);
        if(isSubUser(user)||isTechAdmin(user)){
            villes =  getVilleBD(c,user.getVille());
            secteurs = getAllSecteurBDB(c,user.getVille());
            equipements = getAllEQBD(c,user.getVille());
            cartes = getAllCarteBD(c,user.getVille());
            ports = getAllPortBD(c, user.getVille());
            clients = getAllClientBD(c, user.getVille());
            
        }else{
            villes =  getVilleBD(c);
            secteurs = getAllSecteurBDB(c);
            equipements = getAllEQBD(c);
            cartes = getAllCarteBD(c);
            ports = getAllPortBD(c);
            clients = getAllClientBD(c);
        }
        secteurVilles = secteurVilleBd(c);
        eqSecteurs =eqSecteurBd(c);
        carteEqs = carteEqBd(c);
        portCartes = portCarteBd(c);
        clientVilles = clientVilleBd(c);
        clientHistoriques = clientHistoriqueBd(c);
        portClients = clientPortBd(c);
        notificationRecus = getNotificationById(user.getId(),c,2);
        notificationEmis = getNotificationById(user.getId(),c,1);
        
        
        users = getUserBD(c);
        if(isSuperAdmin(user)){            
            userVilles = userVilleBd(c);
        }
        
   }
   
   //Deconnection liberation de la mémoire
   public void deconnection() throws ClassNotFoundException, SQLException{
        debits = null;
        categories = null;
        villes =  null;
        secteurs = null;
        equipements = null;
        cartes = null;
        ports = null;
        clients = null;
        
        secteurVilles = null;
        eqSecteurs = null;
        carteEqs = null;
        portCartes = null;
        clientVilles = null;
        clientHistoriques = null;
        portClients = null;
        users = null;
        userVilles = null;
        
        
   }
    
 
 
    
    //--Ville
    public Hashtable getVilleBD(Connecter c,int... id) throws ClassNotFoundException, SQLException{
        //Connecter c = new Connecter();
        
        Requettes con = new Requettes(c);
        ResultSet rs;
        if(id.length>0){
            rs = con.fgetVilleById(id[0]);
        }else{
            rs = con.fgetAllVille();
        }
        //List<Ville> fvilles = new ArrayList<Ville>(Arrays.<Ville>asList());
        Hashtable fvilles = new Hashtable();
        while(rs.next()){
            Ville ville = new Ville(rs.getString("nom_ville"));
            ville.setId(rs.getInt("id_ville"));
            ville.setNbEq(rs.getInt("nbre_equipement_ville"));
            ville.setNbCarte(rs.getInt("nbre_carte"));
            ville.setNbPort(rs.getInt("nbre_port_ville"));
            ville.setNbPortLibre(rs.getInt("nbre_port_libre_ville"));
            ville.setNbPortOccupe(rs.getInt("nbre_port_occupe_ville"));
            ville.setNbPortDefect(rs.getInt("nbre_port_defectueux_ville"));
            ville.setNbClients(rs.getInt("nbre_client"));
            ville.setNbUser(rs.getInt("nbre_utilisateur"));
            
          
            fvilles.put(ville.getId(), ville);
            
        }
        //c.getConnexion().close();
        return fvilles;
    }
    
    public Ville getVilleBDById(int id,Connecter c) throws ClassNotFoundException, SQLException{
        
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetVilleById(id);
        Ville ville = null;
        
        if(rs.next()){
            ville = new Ville(rs.getString("nom_ville"));
            ville.setId(rs.getInt("id_ville"));
            ville.setNbEq(rs.getInt("nbre_equipement_ville"));
            ville.setNbCarte(rs.getInt("nbre_carte"));
            ville.setNbPort(rs.getInt("nbre_port_ville"));
            ville.setNbPortLibre(rs.getInt("nbre_port_libre_ville"));
            ville.setNbPortOccupe(rs.getInt("nbre_port_occupe_ville"));
            ville.setNbPortDefect(rs.getInt("nbre_port_defectueux_ville"));
            ville.setNbClients(rs.getInt("nbre_client"));
            ville.setNbUser(rs.getInt("nbre_utilisateur"));
            
            
        }
        //c.getConnexion().close();
        
        
        return ville;
    }
    /****
     * 
     * @param id
     * @param c
     * @param i
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public Hashtable getNotificationById(int id,Connecter c,int i) throws ClassNotFoundException, SQLException{
        Hashtable notifications = new Hashtable();
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        if(i==1){
            rs = con.fgetNotificatonByEmetteurId(id);
        }else{
            rs = con.fgetNotificatonByRecepeteurId(id);
        }
        
        while(rs.next()){
            Notification notif = new Notification();
            notif.setId(rs.getInt("id"));
            notif.setIdEmet(rs.getInt("id_emetteur"));
            notif.setIdRecep(rs.getInt("id_recepteur"));
            notif.setObjet(rs.getString("objet"));
            notif.setMessage(rs.getString("message"));
            notif.setDate(rs.getDate("date").toString());
            notif.setVue(rs.getBoolean("boolean_vue"));
            
            // recuperation et initialisation de l'objet List clients et utilisateurs
           
            //fsecteurs.put(secteur.getId(), secteur);
            notifications.put(notif.getId(),notif);
        }
        //c.getConnexion().close();
        return notifications;
    }
    
    //------Secteur
   /***********
    * @param id
    * @param ville
    * @return
    * @throws ClassNotFoundException
    * @throws SQLException 
    */ 
    public ArrayList getSecteurBDByVilleId(int id,Connecter c) throws ClassNotFoundException, SQLException{
        
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetAllSecteurByVilleId(id);
        //List<Secteur> fsecteurs = new ArrayList<Secteur>(Arrays.<Secteur>asList());
        //Hashtable fsecteurs = new Hashtable();
        ArrayList fsecteurs = new ArrayList();
        
        while(rs.next()){
            /*Secteur secteur = new Secteur(rs.getString("nom_secteur"));
            secteur.setId(rs.getInt("id_secteur"));
            secteur.setIdVille(rs.getInt("id_ville"));
            secteur.setNbEq(rs.getInt("nbre_equipement_secteur"));
            secteur.setNbCarte(rs.getInt("nbre_carte"));
            secteur.setNbPort(rs.getInt("nbre_port_secteur"));
            secteur.setNbPortLibre(rs.getInt("nbre_port_libre_secteur"));
            secteur.setNbPortOccupe(rs.getInt("nbre_port_occupe_secteur"));
            secteur.setNbPortDefect(rs.getInt("nbre_port_defectueux_secteur"));
            secteur.setNbClients(rs.getInt("nbre_client"));*/
            
            // recuperation et initialisation de l'objet List clients et utilisateurs
           
            //fsecteurs.put(secteur.getId(), secteur);
            fsecteurs.add(rs.getInt("id_secteur"));
        }
        //c.getConnexion().close();
        return fsecteurs;
    }
   /**
    * 
    * @param c
    * @param id
    * @return
    * @throws ClassNotFoundException
    * @throws SQLException 
    */ 
    public Hashtable getAllSecteurBDB(Connecter c, int... id ) throws ClassNotFoundException, SQLException{
        
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        if(id.length>0){
            rs = con.fgetAllSecteurByVilleId(id[0]);
        }else{
            rs = con.fgetAllSecteur();
        }    
        //List<Secteur> fsecteurs = new ArrayList<Secteur>(Arrays.<Secteur>asList());
        Hashtable fsecteurs = new Hashtable();
        while(rs.next()){
            Secteur secteur = new Secteur(rs.getString("nom_secteur"));
            secteur.setId(rs.getInt("id_secteur"));
            secteur.setIdVille(rs.getInt("id_ville"));
            secteur.setNbEq(rs.getInt("nbre_equipement_secteur"));
            secteur.setNbCarte(rs.getInt("nbre_carte"));
            secteur.setNbPort(rs.getInt("nbre_port_secteur"));
            secteur.setNbPortLibre(rs.getInt("nbre_port_libre_secteur"));
            secteur.setNbPortOccupe(rs.getInt("nbre_port_occupe_secteur"));
            secteur.setNbPortDefect(rs.getInt("nbre_port_defectueux_secteur"));
            secteur.setNbClients(rs.getInt("nbre_client"));
            
            // recuperation et initialisation de l'objet List clients et utilisateurs
            //secteur.setEquipements(getAllEQBDBySecteurId(rs.getInt("id_secteur"),c));
            //fsecteurs.add(secteur);
            fsecteurs.put(secteur.getId(), secteur);
            
        }
        //c.getConnexion().close();
        return fsecteurs;
    }
  
   public ArrayList getAllEQBDBySecteurId(int id,Connecter c) throws ClassNotFoundException, SQLException{
       
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetAllEqBySecteurId(id);
        //List<Equipement> feqs = new ArrayList<Equipement>(Arrays.<Equipement>asList());
        //Hashtable feqs = new Hashtable();
        ArrayList feqs = new ArrayList();
        while(rs.next()){
            /*Equipement eq = new Equipement(rs.getString("nom_equipement"));
            eq.setIdEQ(rs.getInt("id_equipement"));
            eq.setIdVille(rs.getInt("id_ville_equipement"));
            eq.setIdSecteur(rs.getInt("id_secteur_equipement"));
            eq.setTmpNbCarte(rs.getInt("nbre_carte_equipement"));
            eq.setTmpNbPort(rs.getInt("nbre_port_equipement"));
            eq.setTmpNbPortLibre(rs.getInt("nbre_port_libre_equipement"));
            eq.setTmpNbPortOccupe(rs.getInt("nbre_port_occupe_equipement"));
            eq.setTmpNbPortDefect(rs.getInt("nbre_port_defectueux_equipement"));
            eq.setVille("N/A");
            eq.setSecteur("N/A");*/
            
            
            // recuperation et initialisation de l'objet List clients et utilisateurs
            //eq.setCartes(getAllCarteBDByEqId(rs.getInt("id_equipement"),c));
            //feqs.add(eq);
            //feqs.put(eq.getIdEQ(), eq);
            feqs.add(rs.getInt("id_equipement"));
            
        }
      //c.getConnexion().close();
        
        return feqs;
    }
   
    public Hashtable getAllEQBD(Connecter c,int... id) throws ClassNotFoundException, SQLException{
       
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        if(id.length>0){
            rs = con.fgetAllEqByVilleId(id[0]);
        }else{
            rs = con.fgetAllEq();
        }    
        //List<Equipement> feqs = new ArrayList<Equipement>(Arrays.<Equipement>asList());
        Hashtable feqs = new Hashtable();
        while(rs.next()){
            Equipement eq = new Equipement(rs.getString("nom_equipement"));
            eq.setIdEQ(rs.getInt("id_equipement"));
            eq.setIdVille(rs.getInt("id_ville_equipement"));
            eq.setIdSecteur(rs.getInt("id_secteur_equipement"));
            eq.setTmpNbCarte(rs.getInt("nbre_carte_equipement"));
            eq.setTmpNbPort(rs.getInt("nbre_port_equipement"));
            eq.setTmpNbPortLibre(rs.getInt("nbre_port_libre_equipement"));
            eq.setTmpNbPortOccupe(rs.getInt("nbre_port_occupe_equipement"));
            eq.setTmpNbPortDefect(rs.getInt("nbre_port_defectueux_equipement"));
            eq.setRoute(rs.getString("route"));
            eq.setModeConnect(rs.getString("mode_connect"));
            eq.setVille("N/A");
            eq.setSecteur("N/A");
            
            
            feqs.put(eq.getIdEQ(), eq);
            
        }
      //c.getConnexion().close();
        
        return feqs;
    }
   
   
   //----Carte
   
    public ArrayList getAllCarteBDByEqId(int id,Connecter c) throws ClassNotFoundException, SQLException{
        
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetAllCarteByEqId(id);
        //List<Carte> fcartes = new ArrayList<Carte>(Arrays.<Carte>asList());
        //Hashtable fcartes = new Hashtable();
        ArrayList fcartes = new ArrayList();
        while(rs.next()){
            /*Carte carte = new Carte(rs.getInt("numero_carte"));
            carte.setId(rs.getInt("id_carte"));
            carte.setIdEQ(rs.getInt("id_equipement"));
            carte.setNbPort(rs.getInt("nbre_port_carte"));
            carte.setNbPortLibre(rs.getInt("nbre_port_libre_carte"));
            carte.setNbPortOccupe(rs.getInt("nbre_port_defectueux_carte"));
            carte.setDesc(rs.getString("description_carte"));*/
            
            //fcartes.put(carte.getId(),carte);
            fcartes.add(rs.getInt("id_carte"));
            
        }
        
        
        //c.getConnexion().close();
        
        return fcartes;
    }
    
    public Hashtable getAllCarteBD(Connecter c,int... id) throws ClassNotFoundException, SQLException{
        
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        if(id.length>0){
            rs = con.fgetAllCarteByVilleId(id[0]);
        }else{
            rs = con.fgetAllCarte();
        }    
        //List<Carte> fcartes = new ArrayList<Carte>(Arrays.<Carte>asList());
        Hashtable fcartes = new Hashtable();
        while(rs.next()){
            Carte carte = new Carte(rs.getInt("numero_carte"));
            carte.setId(rs.getInt("id_carte"));
            carte.setIdEQ(rs.getInt("id_equipement"));
            carte.setNbPort(rs.getInt("nbre_port_carte"));
            carte.setNbPortLibre(rs.getInt("nbre_port_libre_carte"));
            carte.setNbPortOccupe(rs.getInt("nbre_port_occupe_carte"));
            carte.setNbPortDesactive(rs.getInt("nbre_port_defectueux_carte"));
            carte.setDesc(rs.getString("description_carte"));
            carte.setIdVille(rs.getInt("ville"));
            
            fcartes.put(carte.getId(),carte);
            
        }
        return fcartes;
    }
  
   public ArrayList getAllPortBDByCarteId(int id,Connecter c) throws ClassNotFoundException, SQLException{
       
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetAllPortByCarteId(id);
        //List<Port> fports = new ArrayList<Port>(Arrays.<Port>asList());
        //Hashtable fports = new Hashtable();
        ArrayList fports = new ArrayList();
        
        while(rs.next()){
            /*Port pt = new Port(rs.getInt("numero_port"));
            pt.setId(rs.getInt("id_port"));
            pt.setIdCarte(rs.getInt("id_carte"));
            pt.setDesc(rs.getString("description_port"));
            pt.setStatus(rs.getInt("status_port"));*/
           
            //fports.put(pt.getId(),pt);
            fports.add(rs.getInt("id_port"));
            
            
        }
        //c.getConnexion().close();
        return fports;
    }
   
   public Hashtable getAllPortBD(Connecter c,int... id) throws ClassNotFoundException, SQLException{
       
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        if(id.length>0){
            rs = con.fgetAllPortByVilleId(id[0]);
        }else{
            rs = con.fgetAllPort();
        }
        //List<Port> fports = new ArrayList<Port>(Arrays.<Port>asList());
        Hashtable fports = new Hashtable();
        
        while(rs.next()){
            Port pt = new Port(rs.getInt("numero_port"));
            pt.setId(rs.getInt("id_port"));
            pt.setIdCarte(rs.getInt("id_carte"));
            pt.setDesc(rs.getString("description_port"));
            pt.setStatus(rs.getInt("status_port"));
            pt.setIdVille(rs.getInt("ville"));
            
            fports.put(pt.getId(),pt);
            
        }
        //c.getConnexion().close();
        return fports;
    }

   //--- client
   
   public ArrayList getAllClientBDByVilleId(int id,Connecter c) throws ClassNotFoundException, SQLException{
        
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetClientByVilleId(id);
        
        //Hashtable fclients = new Hashtable();
        ArrayList fclients = new ArrayList();
        while(rs.next()){
            /*Client client = new Client();
            client.setId(rs.getInt("id_client"));
            client.setPort(rs.getInt("id_port"));
            client.setNom(rs.getString("nom_client"));
            client.setPrenom(rs.getString("prenom_client"));
            client.setEside(rs.getString("eside"));
            client.setDateCreation(rs.getDate("date_creation").toString());
            client.setNumTel(rs.getString("numero_telephone"));
            client.setEq(rs.getInt("id_equipement"));
            client.setCarte(rs.getInt("id_carte"));
            client.setSecteur(rs.getInt("id_secteur"));
            client.setVille(rs.getInt("id_ville"));
            client.setVilleOwner(rs.getInt("ville_client"));
            client.setCategorie(rs.getInt("id_categorie"));
            client.setStatus(rs.getString("status_client"));
            client.setDesc(rs.getString("description"));
            
          
            client.setDebit(rs.getInt("id_debit"));*/
            //fclients.put(client.getId(),client);
            fclients.add(rs.getInt("id_client"));
            
        }
        
        //c.getConnexion().close();
        
        return fclients;
    }
   
   public int getAllClientBDByPortId(int id,Connecter c) throws ClassNotFoundException, SQLException{
        
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetClientByPortId(id);
        
        Client client = null;
        int i=0;
        if(rs.next()){
            i=rs.getInt("id_client");
            /*client.setPort(rs.getInt("id_port"));
            client.setNom(rs.getString("nom_client"));
            client.setPrenom(rs.getString("prenom_client"));
            client.setEside(rs.getString("eside"));
            client.setDateCreation(rs.getDate("date_creation").toString());
            client.setNumTel(rs.getString("numero_telephone"));
            client.setEq(rs.getInt("id_equipement"));
            client.setCarte(rs.getInt("id_carte"));
            client.setSecteur(rs.getInt("id_secteur"));
            client.setVille(rs.getInt("id_ville"));
            client.setVilleOwner(rs.getInt("ville_client"));
            client.setCategorie(rs.getInt("id_categorie"));
            client.setStatus(rs.getString("status_client"));
            client.setDesc(rs.getString("description"));
            
          
            client.setDebit(rs.getInt("id_debit"));*/
            
        }
        
        //c.getConnexion().close();
        
        return i;
    }
    
    public  Hashtable getAllClientBD(Connecter c, int... id) throws ClassNotFoundException, SQLException{
        
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        if(id.length>0){
            rs = con.fgetAllClientByVilleId(id[0]);
        }else{
            rs = con.fgetAllClient();
        }    
        //List<Client> fclients = new ArrayList<Client>(Arrays.<Client>asList());
        Hashtable fclients = new Hashtable();
        while(rs.next()){
            Client client = new Client();
            numeros.put(rs.getString("numero_telephone"),rs.getInt("id_client"));
            nomClients.add(rs.getString("nom_client")+" "+rs.getString("prenom_client"));
            client.setId(rs.getInt("id_client"));
            client.setPort(rs.getInt("id_port"));
            client.setNom(rs.getString("nom_client"));
            client.setPrenom(rs.getString("prenom_client"));
            client.setEside(rs.getString("eside"));
            client.setDateCreation(rs.getDate("date_creation").toString());
            client.setNumTel(rs.getString("numero_telephone"));
            client.setEq(rs.getInt("id_equipement"));
            client.setCarte(rs.getInt("id_carte"));
            client.setSecteur(rs.getInt("id_secteur"));
            client.setVille(rs.getInt("id_ville"));
            client.setVilleOwner(rs.getInt("ville_client"));
            client.setCategorie(rs.getInt("id_categorie"));
            client.setStatus(rs.getString("status_client"));
            client.setDesc(rs.getString("description"));
            client.setDebit(rs.getInt("id_debit"));
            client.setEmail(rs.getString("email"));
            client.setAsuspendre(rs.getBoolean("a_suspendre"));
            client.setValider(rs.getBoolean("valider_suspenssion"));
            
            
            fclients.put(client.getId(),client);
            
        }
        //c.getConnexion().close();
        
        return fclients;
    }
  
    
   public Client getClientBDByPortId(int id,Connecter c) throws ClassNotFoundException, SQLException{
       
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetClientByPortId(id);
        Client client = null;
        
        if(rs.next()){
            client = new Client();
            client.setId(rs.getInt("id_client"));
            client.setPort(rs.getInt("id_port"));
            client.setNom(rs.getString("nom_client"));
            client.setPrenom(rs.getString("prenom_client"));
            client.setEside(rs.getString("eside"));
            client.setDateCreation(rs.getDate("date_creation").toString());
            client.setNumTel(rs.getString("numero_telephone"));
            client.setEq(rs.getInt("id_equipement"));
            client.setCarte(rs.getInt("id_carte"));
            client.setSecteur(rs.getInt("id_secteur"));
            client.setVille(rs.getInt("id_ville"));
            client.setVilleOwner(rs.getInt("ville_client"));
            client.setCategorie(rs.getInt("id_categorie"));
            client.setStatus(rs.getString("status_client"));
            client.setDesc(rs.getString("description"));
            client.setDebit(rs.getInt("id_debit"));
            client.setEmail(rs.getString("email"));
            client.setAsuspendre(rs.getBoolean("a_suspendre"));
            client.setValider(rs.getBoolean("valider_suspenssion"));
            
            
        }
        //c.getConnexion().close();
        return client;
    }
   
   //stat client
   public  List getAllClientSTATBD(Connecter c,Debit debit,Categorie cat,Ville ville,String status,Ville villeE,Secteur sect,Equipement eq,Carte carte,Port port,String statusP,LocalDate dateFrom,LocalDate dateTo,Boolean aSusp,Boolean valider) throws ClassNotFoundException, SQLException, ParseException{
        
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetStatClient(debit,cat,ville,status,villeE,sect,eq,carte,port,statusP,dateFrom,dateTo,aSusp,valider);
        List<Client> fclients = new ArrayList<Client>(Arrays.<Client>asList());
        
        while(rs.next()){
            Client client = new Client();
            numeros.put(rs.getString("numero_telephone"),rs.getInt("id_client"));
            nomClients.add(rs.getString("nom_client")+" "+rs.getString("prenom_client"));
            client.setId(rs.getInt("id_client"));
            client.setPort(rs.getInt("id_port"));
            client.setNom(rs.getString("nom_client"));
            client.setPrenom(rs.getString("prenom_client"));
            client.setEside(rs.getString("eside"));
            client.setDateCreation(rs.getDate("date_creation").toString());
            client.setNumTel(rs.getString("numero_telephone"));
            client.setEq(rs.getInt("id_equipement"));
            client.setCarte(rs.getInt("id_carte"));
            client.setSecteur(rs.getInt("id_secteur"));
            client.setVille(rs.getInt("id_ville"));
            client.setVilleOwner(rs.getInt("ville_client"));
            client.setCategorie(rs.getInt("id_categorie"));
            client.setStatus(rs.getString("status_client"));
            client.setDesc(rs.getString("description"));
            client.setDebit(rs.getInt("id_debit"));
            client.setEmail(rs.getString("email"));
            client.setAsuspendre(rs.getBoolean("a_suspendre"));
            client.setValider(rs.getBoolean("valider_suspenssion"));
            
            
            fclients.add(client);
            
        }
        //c.getConnexion().close();
        
        return fclients;
    }
   
   
 
   //--Debit
   public Debit getDebitBDById(int id,Connecter c) throws ClassNotFoundException, SQLException{
       
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetDebitById(id);
        Debit deb = null;
        
        if(rs.next()){
            deb = new Debit();
            deb.setId(rs.getInt("id_debit"));
            deb.setNom(rs.getString("valeur_debit"));
            deb.setProfil(rs.getInt("profil"));
            
        }
        //c.getConnexion().close();
        return deb;
    }

    public Hashtable getDebitBD(Connecter c) throws ClassNotFoundException, SQLException{
       
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetDebit();
        
        //List<Debit> fdebits = new ArrayList<Debit>(Arrays.<Debit>asList());
        Hashtable fdebits = new Hashtable();
        
        while(rs.next()){
            Debit deb = new Debit();
            deb.setId(rs.getInt("id_debit"));
            deb.setNom(rs.getString("valeur_debit"));
            deb.setProfil(rs.getInt("profil"));
            fdebits.put(deb.getId(),deb);
            
        }
        //c.getConnexion().close();
        return fdebits;
    }
    
    //--Categorie
   public Categorie getCategorieBDById(int id,Connecter c) throws ClassNotFoundException, SQLException{
       
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetCategorieById(id);
        Categorie cat = null;
        
        if(rs.next()){
            cat = new Categorie();
            cat.setId(rs.getInt("id_categorie"));
            cat.setNom(rs.getString("valeur_categorie"));
            
        }
        //c.getConnexion();
        return cat;
    }

    public Hashtable getCategorieBD(Connecter c) throws ClassNotFoundException, SQLException{
       
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetCategorie();
        
        //List<Categorie> fcats = new ArrayList<Categorie>(Arrays.<Categorie>asList());
        Hashtable fcats = new Hashtable();
        
        while(rs.next()){
            Categorie cat = new Categorie();
            cat.setId(rs.getInt("id_categorie"));
            cat.setNom(rs.getString("valeur_categorie"));
            fcats.put(cat.getId(),cat);
            
        }
        //c.getConnexion();
        return fcats;
    }
    
    
   
   
   //---- user
    
    public Hashtable getUserBD(Connecter c) throws ClassNotFoundException, SQLException{
       
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetAllUser();
        
        //List<Categorie> fcats = new ArrayList<Categorie>(Arrays.<Categorie>asList());
        Hashtable fusers = new Hashtable();
        
        while(rs.next()){
            User user = new User();
            user.setId(rs.getInt("id_utilisateur"));
            user.setNom(rs.getString("nom_utilisateur"));
            user.setPrenom(rs.getString("prenom_utilisateur"));
            user.setVille(rs.getInt("id_ville_utilisateur"));
            user.setQuartier(rs.getString("quartier_utilisateur"));
            user.setEmail(rs.getString("email_utilisateur"));
            user.setPasswd(rs.getString("password_utilisateur"));
            user.setNivoAccess(rs.getInt("categories_utilisateurs"));
            user.setDesc(rs.getString("raison_sociale_utilisateur"));
            user.setNumTel(rs.getString("telephone_utilisateur"));
            user.setLogin(rs.getString("nom_connexion_utilisateur"));
            nomusers.add(rs.getString("nom_utilisateur")+" "+rs.getString("prenom_utilisateur"));
            fusers.put(user.getId(),user);
            
        }
        //c.getConnexion();
        return fusers;
    }
   
   
   public ArrayList getAllClientHistoriqueBDbyId( int id,Connecter c) throws ClassNotFoundException, SQLException{
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetHistoriqueClientId(id);
        
        //Hashtable fusers = new Hashtable();
        ArrayList fhistoriques = new ArrayList();
        while(rs.next()){
            Historique hist = new Historique(rs.getDate("date").toString(),rs.getString("raison"),rs.getInt("statut"));
            hist.setId(rs.getInt("id"));
            //fusers.put(user.getId(),user);
            fhistoriques.add(hist);
            
        }
        //c.getConnexion();
        return fhistoriques;
    }        
           
   public ArrayList getAllUserVilleBDbyId( int id,Connecter c) throws ClassNotFoundException, SQLException{
        ResultSet rs;
        //Connecter c = new Connecter();
        Requettes con = new Requettes(c);
        rs = con.fgetUserByVilleId(id);
        
        //Hashtable fusers = new Hashtable();
        ArrayList fusers = new ArrayList();
        while(rs.next()){
            /*User user = new User();
            user.setId(rs.getInt("id_utilisateur"));
            user.setNom(rs.getString("nom_utilisateur"));
            user.setPrenom(rs.getString("prenom_utilisateur"));
            user.setVille(rs.getInt("id_ville_utilisateur"));
            user.setQuartier(rs.getString("quartier_utilisateur"));
            user.setEmail(rs.getString("email_utilisateur"));
            user.setPasswd(rs.getString("password_utilisateur"));
            user.setNivoAccess(rs.getInt("categories_utilisateurs"));
            user.setDesc(rs.getString("raison_sociale_utilisateur"));
            user.setNumTel(rs.getString("telephone_utilisateur"));
            user.setLogin(rs.getString("nom_connexion_utilisateur"));*/
            //fusers.put(user.getId(),user);
            fusers.add(rs.getInt("id_utilisateur"));
            
        }
        //c.getConnexion();
        return fusers;
    }
   
   //est super administrateur
   public boolean isSuperAdmin(User user){
       if(user.getNivoAccess()==1){
           return true;
       }

       return false;
   }
   //est administrateur
   public boolean isAdmin(User user){
       if(user.getNivoAccess()==2){
           return true;
       }

       return false;
   }
   //est gestionnaire?
   public boolean isUser(User user){
       if(user.getNivoAccess()==3){
           return true;
       }

       return false;
   }
   // est sous gestionnaire?
   public boolean isSubUser(User user){
       if(user.getNivoAccess()==4){
           return true;
       }

       return false;
   }
   //est technicien gestion?
   public boolean isTechUser(User user){
       if(user.getNivoAccess()==5){
           return true;
       }

       return false;
   }
   //est technicien admin?
   public boolean isTechAdmin(User user){
       if(user.getNivoAccess()==6){
           return true;
       }

       return false;
   }
   
   
   public List<String> getCategorie( ){
        List<String> cats = new ArrayList<String>(Arrays.<String>asList("Super Admin","Admin","Technicien Admin","Gestionnaire","Sous Gestionnaire","Technicien gestion"));
        
        return cats;
    }
   
   
   public int getIntType(String a){
       if(a.equals(getCategorie().get(0))){
            return 1;//super admin
       }
       if(a.equals(getCategorie().get(1))){
            return 2;//admin
       }
       if(a.equals(getCategorie().get(3))){
           return 3;//gestionnaire
       }
       if(a.equals(getCategorie().get(2))){
           return 6;//technicien admin
       }
       if(a.equals(getCategorie().get(5))){
           return 5;//technicien gestion
       }
       return 4;//sous gestionnaire
   }
   
   public boolean textCompare(String a, String b){
       if(a.equals(b)){
           return true;
       }
       return false;
   }
   
   
   public boolean isEmail(String emailnumber){
    
    Boolean  isemailN= true ;
    if(!emailnumber.matches("^[a-zA-Z0-9.%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$"))
        {
            isemailN=false;
            //^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$
           
        }
         // ^[a-bA-B0-9.%+-]+@[a-bA-B0-9.-]+.[a-bA-B]{2,4}$
        
        return isemailN ;
    }
   
   public boolean isNumeroCamtel(String nutellTF ){
            
        boolean isNnumeric=true;
        //String validationString= null;
        
        if(!nutellTF.matches("^2[2-4][0-9]{1}([-. ]?[0-9]{3}){2}$"))
        {
            isNnumeric=false;
            //^2[2-4][0-9]{1}([-. ]?[0-9]{3}){2}$
          
            
            
        }
       // /^(22|23|24)[0-9]{7}$/g)
        //inputlb.setText(validationString);
        
        return isNnumeric ;
    
    }
   
   ////fichier
   public  void ecrireFichier(String hote, String port, String user,String password, Label label,String nomFich) {
                FileWriter fichier;
                BufferedWriter tampon = null;
                    try{
                        fichier=new FileWriter(nomFich,true);
                        tampon=new BufferedWriter(fichier);
                        tampon.write(hote+"~"+port+"~"+user+"~"+password+"\n");
                        label.setText("sauvegarde réussit");
                    }catch(IOException d){
                        label.setText("impossible d'ecrire dans le fichier");
                        System.out.println("impossible d'ecrire dans le fichier ");
                    }finally{
                        try{
                            tampon.close();
                        }catch(IOException d){
                            label.setText("impossible de fermer le fichier");
                            System.out.println("impossible de fermer le fichier");
                   }
               }
            
            }
   public  void ecrireFichier(String hote, String password, Label label,String nomFich) {
                FileWriter fichier;
                BufferedWriter tampon = null;
                    try{
                        fichier=new FileWriter(nomFich,true);
                        tampon=new BufferedWriter(fichier);
                        tampon.write(hote+"~"+password+"\n");
                        label.setText("sauvegarde réussit");
                    }catch(IOException d){
                        label.setText("impossible d'ecrire dans le fichier");
                        System.out.println("impossible d'ecrire dans le fichier ");
                    }finally{
                        try{
                            tampon.close();
                        }catch(IOException d){
                            label.setText("impossible de fermer le fichier");
                            System.out.println("impossible de fermer le fichier");
                   }
               }
            
            }
   
   public String[] LireFichier(String nomFich,Label label){
        FileReader fichier=null;
     
        BufferedReader tampon=null;
        String[] frac=null;
        //File f =  new File("../"+nomFich);
        try{
          
            fichier=new FileReader(nomFich);
            tampon=new BufferedReader(fichier);
            String chaine="";
            while(true){
            chaine=tampon.readLine();
            if(chaine!=null){
                frac=chaine.split("~");
                
            }else{
                break;
            }
                
            }
          
        }catch(IOException e){
            System.out.println("erreur de lecture");
            
            if(nomFich.contains(".smtp")){
                ecrireFichier("kemchepatou@gmail.com","Dieuaime",label,nomFich);
            }else{
                ecrireFichier("localhost","80","admin","admin",label,nomFich);
            }
            try {
                fichier=new FileReader(nomFich);
            } catch (FileNotFoundException ex) {
                label.setText("impossible de lire le fichier "+nomFich);
                new Notifier("Gestab exception","probleme de connection/fichier:\n"+ex,3);
                Logger.getLogger(DAOequipement.class.getName()).log(Level.SEVERE, null, ex);
            }
                tampon=new BufferedReader(fichier);
                String chaine="";
                while(true){
                try {
                    chaine=tampon.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(DAOequipement.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(chaine!=null){
                    frac=chaine.split("~");

                }else{
                    break;
                }

                }
       
        }
        return frac;
    }
   
     public  String dateToString (Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String result= sdf.format(date);
       return result;
    }
    
    public  Date stringToDate(String date) throws ParseException{
        System.out.println(date);
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       Date d = sdf.parse(date);
       return d;
    }
    
    
    // status port STring to int
    public int intStatusPort(String status){
        System.out.println("stat"+status);
        if(status.equals("ACTIF")){
            return 0;
        }
        return 1;
    }
    
    
    
    /* This method is used to generate the animation on the login window, It will generate random ints to determine
     * the size, speed, starting points and direction of each square.
     */
    public void generateAnimation(BorderPane borderPane,DAOequipement dao){
        Random rand = new Random();
        int sizeOfSqaure = rand.nextInt(50) + 1;
        int speedOfSqaure = rand.nextInt(10) + 5;
        int startXPoint = rand.nextInt(420);
        int startYPoint = rand.nextInt(350);
        int direction = rand.nextInt(5) + 1;

        KeyValue moveXAxis = null;
        KeyValue moveYAxis = null;
        Rectangle r1 = null;

        switch (direction){
            case 1 :
                // MOVE LEFT TO RIGHT
                r1 = new Rectangle(0,startYPoint,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 350 -  sizeOfSqaure);
                break;
            case 2 :
                // MOVE TOP TO BOTTOM
                r1 = new Rectangle(startXPoint,0,sizeOfSqaure,sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSqaure);
                break;
            case 3 :
                // MOVE LEFT TO RIGHT, TOP TO BOTTOM
                r1 = new Rectangle(startXPoint,0,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 350 -  sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSqaure);
                break;
            case 4 :
                // MOVE BOTTOM TO TOP
                r1 = new Rectangle(startXPoint,420-sizeOfSqaure ,sizeOfSqaure,sizeOfSqaure);
                moveYAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 5 :
                // MOVE RIGHT TO LEFT
                r1 = new Rectangle(420-sizeOfSqaure,startYPoint,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 6 :
                //MOVE RIGHT TO LEFT, BOTTOM TO TOP
                r1 = new Rectangle(startXPoint,0,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 350 -  sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSqaure);
                break;

            default:
                System.out.println("default");
        }

        r1.setFill(Color.web(dao.couleur));
        r1.setOpacity(0.1);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(speedOfSqaure * 1000), moveXAxis, moveYAxis);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        borderPane.getChildren().add(borderPane.getChildren().size()-1,r1);
    }
  
   
   
   
   
    
}
