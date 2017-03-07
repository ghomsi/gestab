/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
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

/**
 *
 * @author ghomsi
 */
public class Requettes {
    
    
    private List<String> numeros;
    private Connecter con;
    private DAOequipement dao;
    
    private java.util.Date date1;
    private java.util.Date date2;

    public Requettes(Connecter con) {
        this.con = con;
        this.dao = new DAOequipement();
        
    
    }
    
   
   
    
    public List<String> getNumero(){
        return numeros;
    }
    public void setNumero(List<String> fnumero){
        numeros = fnumero;
    }
    /**Historique**/
    public ResultSet fgetHistoriqueClientId(int id) throws ClassNotFoundException, SQLException{
        // nos Objets necessaire
        
        ResultSet rs;       // contiendra le résultat de notre requete   
        
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".historiques WHERE id_client=?;");
        stm.setInt(1, id);
        rs = stm.executeQuery();
        
        return rs;       
    }
    
    
    /*********Secteur
     * @param idClientr*********/
    
    //----fonction Insertion Secteur
    public int fInsertHistorique(int idClient,Historique hist) throws ClassNotFoundException, SQLException, ParseException{
        
        String requete = "INSERT INTO historiques(id_client,statut,date,raison) VALUES (?, ?, ?, ?);";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
        
        ps.setInt(1, idClient);
        ps.setInt(2, hist.getStatus());
        java.sql.Date sqlDate = new java.sql.Date(dao.stringToDate(hist.getDate()).getTime());
        ps.setDate(3, sqlDate);
        ps.setString(4, hist.getRaison());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
        }
        //con.getConnexion().close();
       return id;
        
       
    }
    
    
    public ResultSet fUserVerification(String userLogin,String userPasswd) throws ClassNotFoundException, SQLException{
        // nos Objets necessaire
        
        ResultSet rs;       // contiendra le résultat de notre requete   
        String requete;     // contiendra notre requete
        
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".utilisateurs WHERE nom_connexion_utilisateur=? AND password_utilisateur=md5(?);");
        stm.setString(1, userLogin);
        stm.setString(2, userPasswd);
        rs = stm.executeQuery();
        
        //con.getConnexion().close();
        
        return rs;       
    }
    
     public int fInsertUser(User user)throws ClassNotFoundException, SQLException
    {
        
        String requete = "INSERT INTO utilisateurs(categories_utilisateurs,"
                + "        nom_connexion_utilisateur, nom_utilisateur, prenom_utilisateur,"
                + "        telephone_utilisateur, password_utilisateur,"
                + "        quartier_utilisateur, email_utilisateur, date_creation_utilisateur,"
                + "        raison_sociale_utilisateur,id_ville_utilisateur)"
                + "         VALUES ( ?, ?, ?, ?,?, md5(?), ?, ?, now(), ?, ?);";
        
            //Connecter con = new Connecter();
            PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
            ps.setShort(1, (short) user.getNivoAccess());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getNom());
            ps.setString(4, user.getPrenom());
            ps.setString(5, user.getNumTel());
            ps.setString(6, user.getPasswd());
            ps.setString(7, user.getQuartier());
            ps.setString(8, user.getEmail());
            ps.setString(9, user.getDesc());
            ps.setInt(10, user.getVille());
            ps.executeUpdate();
            
            //con.getConnexion().close();
            
        ResultSet rs = ps.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
        }
        //con.getConnexion().close();
       return id;
        
        
    }
     
    public ResultSet fgetAllUser() throws ClassNotFoundException, SQLException{
        // nos Objets necessaire
        
        ResultSet rs;       // contiendra le résultat de notre requete   
        String requete;     // contiendra notre requete
        //Connecter con = new Connecter();
        
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".utilisateurs;");
        rs = stm.executeQuery();
        
        //con.getConnexion().close();
        
        return rs;       
    } 
    
    public ResultSet fgetUserByVilleId(int id) throws ClassNotFoundException, SQLException{
        // nos Objets necessaire
        
        ResultSet rs;       // contiendra le résultat de notre requete   
        
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT id_utilisateur FROM \"public\".utilisateurs WHERE id_ville_utilisateur=?;");
        stm.setInt(1, id);
        rs = stm.executeQuery();
        
        return rs;       
    }
    
    
    /*********Ville*********/
    
    //----fonction Insertion Ville
    public int fInsertVille(Ville ville) throws ClassNotFoundException, SQLException{
         
        String requete = "INSERT INTO \"public\".villes (nom_ville) VALUES (?);";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, ville.getNom());
        ps.executeUpdate();
        
        ResultSet rs = ps.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
        }
        
        return id;
        
    }
    
    //----fonction recuperation de toutes les Villes
    public ResultSet fgetAllVille() throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete 
        
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".villes ;");
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    }
    //----fonction recuperation de toutes les Villes
    public ResultSet fgetVilleById(int idVille) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".villes WHERE id_ville=? ;");
        stm.setInt(1, idVille);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        return rs;
        
    }
    
    
    /*********Secteur*********/
    
    //----fonction Insertion Secteur
    public int fInsertSecteur(Secteur secteur,int idVille) throws ClassNotFoundException, SQLException{
        
        String requete = "INSERT INTO secteurs(id_ville, nom_secteur) VALUES (?, ?);";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
        
        ps.setInt(1, idVille);
        ps.setString(2, secteur.getNom());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
        } 
       //con.getConnexion().close();
       return id;
       //con.getConnexion().close();
       
    }
    //----fonction recuperation de toutes les secteurs 
    public ResultSet fgetAllSecteur() throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".secteurs;");
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        
        return rs;
        
    }
    //----fonction recuperation de toutes les secteurs en fonction de l'id de la ville
    public ResultSet fgetAllSecteurByVilleId(int idVille) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".secteurs WHERE id_ville = ? ;");
        stm.setInt(1, idVille);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        
        return rs;
        
    }
    
    
        /*********Equipement*********/
    
    //----fonction Insertion Equipement
    public int fInsertEquipment(Equipement eq) throws ClassNotFoundException, SQLException
    {
        String requete = "INSERT INTO equipements(nom_equipement, id_secteur_equipement,"
                        + " id_ville_equipement,route,mode_connect) VALUES ( ?, ?, ?, ?, ?);";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, eq.getNom());
        ps.setInt(2, eq.getIdSecteur());
        ps.setInt(3, eq.getIdVille());
        ps.setString(4, eq.getRoute());
        ps.setString(5, eq.getModeConnect());
        ps.executeUpdate();
        
        ResultSet rs = ps.getGeneratedKeys();
        int id=0;
        if (rs.next()) {
          id = rs.getInt(1);
        }
        
        //con.getConnexion().close();
        return id;
    }
    
    public void fUpdateEquipment(Equipement eq) throws ClassNotFoundException, SQLException
    {
        String requete = " UPDATE \"public\".equipements SET \"nom_equipement\" = ?,id_secteur_equipement= ?,id_ville_equipement= ?,route= ?,mode_connect= ? WHERE id_equipement = ?;";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setString(1, eq.getNom());
        ps.setInt(2, eq.getIdSecteur());
        ps.setInt(3, eq.getIdVille());
        ps.setString(4, eq.getRoute());
        ps.setString(5, eq.getModeConnect());
        ps.setInt(6, eq.getIdEQ());
        ps.executeUpdate();
    }

    /*********Secteur*********/
    
    //----fonction recuperation de toutes les equipements en fonction de l'id du secteur
    public ResultSet fgetAllEqBySecteurId(int idSecteur) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT id_equipement FROM \"public\".equipements WHERE id_secteur_equipement = ? ;");
        stm.setInt(1, idSecteur);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    } 
    
    //----fonction recuperation de toutes les equipements en fonction de l'id du secteur
    public ResultSet fgetAllEq() throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".equipements;");
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    } 
    
    //----fonction recuperation de toutes les equipements en fonction de l'id de la ville
    public ResultSet fgetAllEqByVilleId(int id) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".equipements WHERE id_ville_equipement= ?;");
        stm.setInt(1, id);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    }
    
    // Delete equipment
    public void fDeleteEquipement(Equipement eq) throws SQLException{
        String requete = " DELETE FROM \"public\".equipements WHERE id_equipement = ?;";
        
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setInt(1, eq.getIdEQ());
        ps.executeUpdate();
    }
    
    
            /*********Carte*********/
    
    //----fonction Insertion carte
    public int fInsertCard(Carte carte) throws ClassNotFoundException, SQLException{
        
        
        String requete = "INSERT INTO cartes (id_equipement, nbre_port_carte,numero_carte,ville)"
                + " VALUES (?, ?, ?,?);";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, carte.getIdEQ());
        ps.setInt(2, carte.getNbPort());
        ps.setInt(3, carte.getNumCarte());
        ps.setInt(4, carte.getIdVille());
        
        ps.executeUpdate();
        
        ResultSet rs = ps.getGeneratedKeys();
        //con.getConnexion().close();
        
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
          
          for(int i=0;i<carte.getNbPort();i++){
            Port port = new Port(i);
            port.setIdCarte(id);
            port.setIdVille(carte.getIdVille());
            int idp = fInsertPort(port);
            
           }
        }  
       return id; 
    }

    //----fonction recuperation de toutes les equipements en fonction de l'id du secteur
    public ResultSet fgetAllCarteByEqId(int idEq) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT id_carte FROM \"public\".cartes WHERE id_equipement = ? ;");
        stm.setInt(1, idEq);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    }
    
    //----fonction recuperation de toutes les equipements en fonction de l'id du secteur
    public ResultSet fgetAllCarte() throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".cartes;");
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    }
    
    //----fonction recuperation de toutes les equipements en fonction de l'id du secteur
    public ResultSet fgetAllCarteByVilleId(int id) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".cartes WHERE ville = ? ;");
        stm.setInt(1, id);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    }
    
    
    // Delete client
    public void fDeleteCarte(Carte carte) throws SQLException{
        String requete = " DELETE FROM \"public\".cartes WHERE id_carte = ?;";
        
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setInt(1, carte.getId());
        ps.executeUpdate();
    }
    
    
              /*********Port*********/
    
    //----fonction Insertion port  
    public int fInsertPort(Port port) throws ClassNotFoundException, SQLException{
        
        String requete = " INSERT INTO ports (numero_port, id_carte,ville) VALUES (?, ?, ?);";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, port.getNumeroPort());
        ps.setInt(2, port.getIdCarte());
        ps.setInt(3, port.getIdVille());
        ps.executeUpdate();
        
        ResultSet rs = ps.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
        }
        //con.getConnexion().close();
       return id;
        
    }
    //----fonction recuperation de toutes les ports d'une carte
    public ResultSet fgetAllPortByCarteId(int idCarte) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT id_port FROM \"public\".ports WHERE id_carte = ? ;");
        stm.setInt(1, idCarte);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    } 
    
    public ResultSet fgetAllPort() throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".ports;");
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    }
    
    //----fonction recuperation de toutes les ports d'une ville
    public ResultSet fgetAllPortByVilleId(int id) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".ports WHERE ville = ? ;");
        stm.setInt(1, id);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    }
    

              /*********Client*********/
    
    //----fonction Insertion client  
    public int fInsertClient(Client client) throws ClassNotFoundException, SQLException, ParseException{
        
        String requete = " INSERT INTO clients (id_port, id_debit,nom_client,prenom_client,eside,date_creation,numero_telephone,id_ville,id_equipement,id_carte,ville_client,id_secteur,id_categorie,status_client,description,email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?);";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, client.getPort());
        ps.setInt(2, client.getDebit());
        ps.setString(3, client.getNom());
        ps.setString(4, client.getPrenom());
        ps.setString(5, client.getEside());
        java.sql.Date sqlDate = new java.sql.Date(dao.stringToDate(client.getDateCreation()).getTime());
        ps.setDate(6, sqlDate);
        ps.setString(7, client.getNumTel());
        ps.setInt(8, client.getVille());
        ps.setInt(9, client.getEq());
        ps.setInt(10, client.getCarte());
        ps.setInt(11, client.getVilleOwner());
        ps.setInt(12, client.getSecteur());
        ps.setInt(13, client.getCategorie());
        ps.setString(14, client.getStatut());
        ps.setString(15, client.getDesc());
        ps.setString(16, client.getEmail());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
        } 
        //con.getConnexion().close();
       return id;
        
    }
    //----fonction recuperation de tous les objet clients
    public ResultSet fgetAllClient() throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".clients;");
        rs = stm.executeQuery();
        return rs;
        
    }
    //----fonction recuperation de tous les objet clients d'une ville
    public ResultSet fgetAllClientByVilleId(int id) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".clients WHERE ville_client= ?;");
        stm.setInt(1, id);
        rs = stm.executeQuery();
        return rs;
        
    }
    //----fonction recuperation de tous les objet clients en fonction du filtre statistique
    public ResultSet fgetStatClient(Debit debit,Categorie cat,Ville ville,String status,Ville villeE,Secteur sect,Equipement eq,Carte carte,Port port,String statusP,LocalDate dateFrom, LocalDate dateTo,Boolean aSusp,Boolean valider) throws ClassNotFoundException, SQLException, ParseException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        System.out.println("Selected date: " + dateFrom);
        String req =" SELECT * FROM \"public\".clients ";
        int i = 1,a=1,b=1,c=1,d=1,e=1,f=1,g=1,h=1,j=1,k=1,l=1,m=1,n=1,o=1;
        if(debit!=null){
                if(i==1){
                    req+=" WHERE id_debit = ? ";
                }else if(i>1){
                    req+=" AND id_debit = ? ";
                    a=i;
                }
                i++;
        }
        if(cat!=null){
            if(i==1){
                req+=" WHERE id_categorie = ? ";
            }else if(i>1){
                req+=" AND id_categorie = ? ";
                b=i;
            }
            i++;
        }
        if(ville!=null){
            if(i==1){
                req+=" WHERE ville_client = ? ";
            }else if(i>1){
                req+=" AND ville_client = ? ";
                c=i;
            }
            i++;
        }
        if(status!=null){
            if(i==1){
                req+=" WHERE status_client = ? ";
            }else if(i>1){
                req+=" AND status_client = ? ";
                d=i;
            }
            i++;
        }
        if(villeE!=null){
            if(i==1){
                req+=" WHERE id_ville = ? ";
            }else if(i>1){
                req+=" AND id_ville = ? ";
                e=i;
            }
            i++;
        }
        if(sect!=null){
            if(i==1){
                req+=" WHERE id_secteur = ? ";
            }else if(i>1){
                req+=" AND id_secteur = ? ";
                f=i;
            }
            i++;
        }
        if(eq!=null){
            if(i==1){
                req+=" WHERE id_equipement = ? ";
            }else if(i>1){
                req+=" AND id_equipement = ? ";
                g=i;
            }
            i++;
        }
        if(carte!=null){
            if(i==1){
                req+=" WHERE id_carte = ? ";
            }else if(i>1){
                req+=" AND id_carte = ? ";
                h=i;
            }
            i++;
        }
        if(port!=null){
            if(i==1){
                req+=" WHERE id_port = ? ";
            }else if(i>1){
                req+=" AND id_port = ? ";
                j=i;
            }
            i++;
        }
        if(statusP!=null){
            if(i==1){
                req+=" WHERE id_port IN( SELECT id_port FROM \"public\".ports WHERE status_port= ?) ";
            }else if(i>1){
                req+=" AND id_port IN( SELECT id_port FROM \"public\".ports WHERE status_port= ?) ";
                k=i;
            }
            i++;
        }
        if(dateFrom!=null){
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	date1 = (java.util.Date) sdf.parse(dateFrom.toString());
            if(dateTo!=null){
                    date2 = (java.util.Date) sdf.parse(dateTo.toString());

                    System.out.println(sdf.format(date1));
                    System.out.println(sdf.format(date2));

                if(i==1){
                    if(date1.compareTo(date2)>0){
                            System.out.println("Date1 is after Date2");
                            req+=" WHERE date_creation BETWEEN ? AND ? ";
                            m=l+1;
                            i++;
                    }else if(date1.compareTo(date2)<0){
                            System.out.println("Date1 is before Date2");
                            req+=" WHERE date_creation BETWEEN ? AND ? ";
                            m=l+1;
                            i++;
                    }else if(date1.compareTo(date2)==0){
                            System.out.println("Date1 is equal to Date2");
                            req+=" WHERE date_creation = ? ";
                    }else{
                            System.out.println("How to get here?");
                    }
                }else if(i>1){

                    if(date1.compareTo(date2)>0){
                            System.out.println("Date1 is after Date2");
                            req+=" AND date_creation BETWEEN ? AND ? ";
                            l=i;
                            m=l+1;
                            i++;
                    }else if(date1.compareTo(date2)<0){
                            System.out.println("Date1 is before Date2");
                            req+=" AND date_creation BETWEEN ? AND ? ";
                            l=i;
                            m=l+1;
                            i++;
                    }else if(date1.compareTo(date2)==0){
                            System.out.println("Date1 is equal to Date2");
                            req+=" AND date_creation = ? ";
                            l=i;
                    }else{
                            System.out.println("How to get here?");
                    }
                }
                i++;
            }else{
                if(i==1){
                    req+=" WHERE date_creation = ? ";
                }else if(i>1){
                    req+=" AND date_creation = ? ";
                    l=i;
                }  
                i++;
            }    
            
        }
        
        if(aSusp!=null){
            if(i==1){
                req+=" WHERE a_suspendre = ? ";
            }else if(i>1){
                req+=" AND a_suspendre = ? ";
                n=i;
            }
            i++;
        }
        
        if(valider!=null){
            if(i==1){
                req+=" WHERE valider_suspenssion = ? ";
            }else if(i>1){
                req+=" AND valider_suspenssion = ? ";
                o=i;
            }
            i++;
        }
        
        
        
        PreparedStatement stm = con.getConnexion().prepareStatement(req);
        if(debit!=null){
            stm.setInt(a, debit.getId());
        }
        if(cat!=null){
            stm.setInt(b, cat.getId());
        }
        if(ville!=null){
            stm.setInt(c, ville.getId());
        }
        if(status!=null){
            stm.setString(d, status);
        }
        if(villeE!=null){
            stm.setInt(e, villeE.getId());
        }
        if(sect!=null){
            stm.setInt(f, sect.getId());
        }
        if(eq!=null){
            stm.setInt(g, eq.getIdEQ());
        }
        if(carte!=null){
            stm.setInt(h, carte.getId());
        }
        if(port!=null){
            stm.setInt(j, port.getId());
        }
        if(statusP!=null){
            stm.setInt(k, dao.intStatusPort(statusP));
        }
        if(dateFrom!=null){
            if(dateTo!=null){
                if(m>1){
                    stm.setDate(l, new java.sql.Date(date1.getTime()));
                    stm.setDate(m, new java.sql.Date(date2.getTime()));
                }else{
                    stm.setDate(l, new java.sql.Date(date1.getTime()));
                }
            }else{
                System.out.println("Selected date: " + new java.sql.Date(date1.getTime()));
                stm.setDate(l, new java.sql.Date(date1.getTime()));
            }
        }
        if(aSusp!=null){
            stm.setBoolean(n, aSusp);
        }
        if(valider!=null){
            stm.setBoolean(o, valider);
        }
        rs = stm.executeQuery();
        return rs;
        
    }
    //----fonction recuperation de l'objet client associé a un port
    public ResultSet fgetClientByPortId(int idPort) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT id_client FROM \"public\".clients WHERE id_port = ? ;");
        stm.setInt(1, idPort);
        rs = stm.executeQuery();
        con.getConnexion();
        
        return rs;
        
    }
    //----fonction recuperation de toutes les clients en fonction d'une ville
    public ResultSet fgetClientByVilleId(int idVille) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT id_client FROM \"public\".clients WHERE ville_client = ? ;");
        stm.setInt(1, idVille);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        return rs;
        
    }
    
    // Update client Port
    public void fUpdateClientPort(Client client) throws SQLException{
        String requete = " UPDATE \"public\".clients SET \"id_port\" = ? WHERE id_client = ?;";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setInt(1, client.getPort());
        ps.setInt(2, client.getId());
        ps.executeUpdate();
    }
    
    // Delete cleint
    public void fDeleteClient(Client client) throws SQLException{
        String requete = " DELETE FROM \"public\".clients WHERE id_client = ?;";
        
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setInt(1, client.getId());
        ps.executeUpdate();
    }
    

              /*********Debit*********/
    
    //----fonction Insertion debit  
    public int fInsertDebit(Debit debit) throws ClassNotFoundException, SQLException{
        
        String requete = " INSERT INTO debits (valeur_debit,profil) VALUES ( ?,?);";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, debit.getNom());
        ps.setInt(2, debit.getprofil());
        ps.executeUpdate();
        
        ResultSet rs = ps.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
        } 
       //con.getConnexion().close();
       return id;
        
    }
    //----fonction recuperation de la liste des debit en fonction de la BD

    public ResultSet fgetDebit() throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".debits;");
        rs = stm.executeQuery();
        //con.getConnexion().close();
        return rs;
        
    }
    //----fonction recuperation d'un debit en fonction de l'id
    public ResultSet fgetDebitById(int idDebit) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".debits WHERE id_debit = ? ;");
        stm.setInt(1, idDebit);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    }    

              /*********Categorie*********/
    
    //----fonction Insertion categorie  
    public int fInsertCategorie(Categorie cat) throws ClassNotFoundException, SQLException{
        
        String requete = " INSERT INTO categories (valeur_categorie) VALUES ( ?);";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, cat.getNom());
        ps.executeUpdate();
        
        ResultSet rs = ps.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
        } 
       //con.getConnexion().close();
       return id;
        
    }
    //----fonction recuperation de la liste des debit en fonction de la BD

    public ResultSet fgetCategorie() throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".categories;");
        rs = stm.executeQuery();
        //con.getConnexion().close();
        
        return rs;
        
    }
    //----fonction recuperation d'un debit en fonction de l'id
    public ResultSet fgetCategorieById(int idCat) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        //Connecter con = new Connecter();
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".categories WHERE id_categorie = ? ;");
        stm.setInt(1, idCat);
        rs = stm.executeQuery();
        //con.getConnexion().close();
        return rs;
        
    }



    /****UPDATE******/
    /**
     * 
     * @param port
     * @throws SQLException 
     */
    public void fUpdateSatutPort(Port port) throws SQLException{
        String requete = " UPDATE \"public\".ports SET \"status_port\" = ? WHERE id_port = ?;";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setInt(1, port.getStatut());
        ps.setInt(2, port.getId());
        ps.executeUpdate();
    }
    
    // Update ville
    public void fUpdateVille(Ville ville) throws SQLException{
        String requete = " UPDATE \"public\".villes SET \"nom_ville\" = ? WHERE id_ville = ?;";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setString(1, ville.getNom());
        ps.setInt(2, ville.getId());
        ps.executeUpdate();
    }
    
    // Update secteur
    public void fUpdateSecteur(Secteur secteur) throws SQLException{
        String requete = " UPDATE \"public\".secteurs SET \"nom_secteur\" = ? WHERE id_secteur = ?;";
        
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setString(1, secteur.getNom());
        ps.setInt(2, secteur.getId());
        ps.executeUpdate();
    }
    
    //Update client
    public void fUpdateClient(Client client) throws ClassNotFoundException, SQLException, ParseException{
    
        
            String requete = "UPDATE clients SET id_port = ? , id_debit = ?, nom_client = ?, prenom_client = ?,"
                    + "eside = ?, date_creation = ?, numero_telephone = ? , id_ville = ? , id_equipement = ?, id_carte = ? , ville_client = ?, id_secteur = ?, id_categorie = ? , status_client = ?, description = ?, email = ?, a_suspendre = ?, valider_suspenssion = ?"
                    + "WHERE (id_client = ?);";
            PreparedStatement ps = con.getConnexion().prepareStatement(requete);
            ps.setInt(1, client.getPort());
            ps.setInt(2, client.getDebit());
            ps.setString(3, client.getNom());
            ps.setString(4, client.getPrenom());
            ps.setString(5, client.getEside());
            java.sql.Date sqlDate = new java.sql.Date(dao.stringToDate(client.getDateCreation()).getDate());
            ps.setDate(6, sqlDate);
            ps.setString(7, client.getNumTel());
            ps.setInt(8, client.getVille());
            ps.setInt(9, client.getEq());
            ps.setInt(10, client.getCarte());
            ps.setInt(11, client.getVilleOwner());
            ps.setInt(12, client.getSecteur());
            ps.setInt(13, client.getCategorie());
            ps.setString(14, client.getStatut());
            ps.setString(15, client.getDesc());
            ps.setString(16, client.getEmail());
            ps.setBoolean(17, client.aSuspendreProperty().getValue());
            ps.setBoolean(18, client.validerProperty().getValue());
            ps.setInt(19, client.getId());
            ps.executeUpdate();
        
        
    }
    
    //update utilisateur
    
    public void fUpdateUser(User user) 
                                    throws ClassNotFoundException, SQLException
    {
      
            String requete = " UPDATE utilisateurs "
                        + "SET categories_utilisateurs = ?, nom_connexion_utilisateur = ? ,"
                        + " nom_utilisateur = ?, prenom_utilisateur = ?,"
                        + " telephone_utilisateur = ?, password_utilisateur = md5(?), "
                        + " quartier_utilisateur = ? , email_utilisateur = ?,"
                        + " raison_sociale_utilisateur = ?,id_ville_utilisateur = ?"
                        + " WHERE id_utilisateur = ?;";
       
            PreparedStatement ps = con.getConnexion().prepareStatement(requete);
            ps.setInt(1, user.getNivoAccess());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getNom());
            ps.setString(4, user.getPrenom());
            ps.setString(5, user.getNumTel());
            ps.setString(6, user.getPasswd());
            ps.setString(7, user.getQuartier());
            ps.setString(8, user.getEmail());
            ps.setString(9, user.getDesc());
            ps.setInt(10, user.getVille());
            ps.setInt(11, user.getId());
            
            
            ps.executeUpdate();
        
    }
    
    public void fUpdateUser2(User user) 
                                    throws ClassNotFoundException, SQLException
    {
      
            String requete = " UPDATE utilisateurs "
                        + "SET nom_utilisateur = ?, prenom_utilisateur = ?,"
                        + " telephone_utilisateur = ?, quartier_utilisateur = ? "
                        + " WHERE id_utilisateur = ?;";
       
            PreparedStatement ps = con.getConnexion().prepareStatement(requete);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getNumTel());
            ps.setString(4, user.getQuartier());
            ps.setInt(5, user.getId());
            
            
            ps.executeUpdate();
        
    }
    
    public void fUpdateUser3(User user) 
                                    throws ClassNotFoundException, SQLException
    {
      
            String requete = " UPDATE utilisateurs "
                        + "SET password_utilisateur = md5(?) WHERE id_utilisateur = ?;";
       
            PreparedStatement ps = con.getConnexion().prepareStatement(requete);
            ps.setString(1, user.getPasswd());
            ps.setInt(2, user.getId());
            
            
            ps.executeUpdate();
        
    }
    
    
    
    /********** Delete ***********/
    
    // Delete ville
    public void fDeleteVille(Ville ville) throws SQLException{
        String requete = " DELETE FROM \"public\".villes WHERE id_ville = ?;";
        
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setInt(1, ville.getId());
        ps.executeUpdate();
    }
    
    // Delete secteur
    public void fDeleteSecteur(Secteur secteur) throws SQLException{
        String requete = " DELETE FROM \"public\".secteurs WHERE id_secteur = ?;";
        
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setInt(1, secteur.getId());
        ps.executeUpdate();
    }
    
    //----fonction Insertion Notification
    public int fInsertNotification(Notification notif) throws ClassNotFoundException, SQLException, ParseException{
        
        String requete = "INSERT INTO notifications(id_emetteur,id_recepteur,objet,message,date,boolean_vue) VALUES (?, ?, ?, ?,now(),?);";
        //Connecter con = new Connecter();
        PreparedStatement ps = con.getConnexion().prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
        
        ps.setInt(1, notif.getIdEmet());
        ps.setInt(2, notif.getIdRecep());
        /*java.sql.Date sqlDate = new java.sql.Date(dao.stringToDate(notif.getDate()).getTime());
        ps.setDate(3, sqlDate);*/
        ps.setString(3, notif.getObjet());
        ps.setString(4, notif.getMessage());
        ps.setBoolean(5, notif.getvue());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
        }
       return id;
        
       
    }
    
    //---- fonction getNotification
    public ResultSet fgetNotificatonByRecepeteurId( int id) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".notifications WHERE id_recepteur = ?;");
        stm.setInt(1, id);
        rs = stm.executeQuery();
        
        return rs;
        
    }
    public ResultSet fgetNotificatonByEmetteurId( int id) throws ClassNotFoundException, SQLException{
         
        ResultSet rs;       // contiendra le résultat de notre requete   
        
        PreparedStatement stm = con.getConnexion().prepareStatement("SELECT * FROM \"public\".notifications WHERE id_emetteur = ?;");
        stm.setInt(1, id);
        rs = stm.executeQuery();
        
        return rs;
        
    }
    //---- fonction update notification
    public void fUpdateNotification(Notification notif) 
                                    throws ClassNotFoundException, SQLException
    {
      
            String requete = " UPDATE notifications "
                        + "SET id_emetteur = ?, id_recepteur = ? ,"
                        + " objet = ?, message = ?,"
                        + " boolean_vue = ? "
                        + " WHERE id = ?;";
       
            PreparedStatement ps = con.getConnexion().prepareStatement(requete);
            ps.setInt(1, notif.getIdEmet());
            ps.setInt(2, notif.getIdRecep());
            ps.setString(3, notif.getObjet());
            ps.setString(4, notif.getMessage());
            ps.setBoolean(5, notif.getvue());
            ps.setInt(6, notif.getId());
            
            
            ps.executeUpdate();
        
    }
    public void fUpdateNotificationVue(Notification notif) 
                                    throws ClassNotFoundException, SQLException
    {
      
            String requete = " UPDATE notifications "
                        + "SET  boolean_vue = ? "
                        + " WHERE id = ?;";
       
            PreparedStatement ps = con.getConnexion().prepareStatement(requete);
            ps.setBoolean(1, notif.getvue());
            ps.setInt(2, notif.getId());
            
            
            ps.executeUpdate();
        
    }
    //---- Delete notification
    public void fDeletenotification(Notification notif) throws SQLException{
        String requete = " DELETE FROM \"public\".notifications WHERE id = ?;";
        
        PreparedStatement ps = con.getConnexion().prepareStatement(requete);
        ps.setInt(1, notif.getId());
        ps.executeUpdate();
    }
    
    
    
    
 //UPDATE "public".cartes SET "nbre_port_carte" = 0 WHERE id_carte = 3;   
}
