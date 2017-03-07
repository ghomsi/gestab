/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ghomsi
 */
public class Equipement {
    
    private final IntegerProperty id;
    private final IntegerProperty idSecteur;
    private IntegerProperty idVille;
    private final SimpleStringProperty secteur;
    private final SimpleStringProperty ville;
    private final SimpleStringProperty nom;
    private final SimpleStringProperty route;
    private final SimpleStringProperty modeConnect;
    
    //--variable temporaire pour besoin de dynamisation du treeTableView pour Eq et AdmmEq 
    private IntegerProperty tmpNbEq;
    private IntegerProperty tmpNbCart;
    private IntegerProperty tmpNbPort;
    private IntegerProperty tmpNbPortLibre;
    private IntegerProperty tmpNbPortOccup;
    private IntegerProperty tmpNbPortDefect;
    
  
    
    //--- contructeur
    public Equipement(String name) {
            this.nom = new SimpleStringProperty(name);
            this.route = new SimpleStringProperty("");
            this.modeConnect = new SimpleStringProperty("");
            this.ville = new SimpleStringProperty("");
            this.secteur = new SimpleStringProperty("");
            this.id = new SimpleIntegerProperty(0);
            this.idSecteur = new SimpleIntegerProperty(0);
            this.idVille = new SimpleIntegerProperty(0);
            
            //--initialisation des variable temporaire
            this.tmpNbEq = new SimpleIntegerProperty(0);
            this.tmpNbCart = new SimpleIntegerProperty(0);
            this.tmpNbPort = new SimpleIntegerProperty(0);
            this.tmpNbPortLibre = new SimpleIntegerProperty(0);
            this.tmpNbPortOccup = new SimpleIntegerProperty(0);
            this.tmpNbPortDefect = new SimpleIntegerProperty(0);
        
    }
    
    public int getIdEQ() {
            return id.get();
        
    }
       
    public void setIdEQ(int fid) {
            id.set(fid);
       
    }
    
    public int getIdSecteur() {
            return idSecteur.get();
        
    }
       
    public void setIdSecteur(int fid) {
            idSecteur.set(fid);
       
    }
    
    public int getIdVille() {
            return idVille.get();
        
    }
       
    public void setIdVille(int fid) {
            idVille.set(fid);
       
    }
    
    public int getTmpNbEq() {
            return tmpNbEq.get();
        
    }
       
    public void setTmpNbEq(int fnb) {
            tmpNbEq.set(fnb);
       
    }
    
    public int getTmpNbCarte() {
            return tmpNbCart.get();
        
    }
       
    public void setTmpNbCarte(int fnb) {
            tmpNbCart.set(fnb);
       
    }
    
    public int getTmpNbPort() {
            return tmpNbPort.get();
        
    }
       
    public void setTmpNbPort(int fnb) {
            tmpNbPort.set(fnb);
       
    }
    
    public int getTmpNbPortLibre() {
            return tmpNbPortLibre.get();
        
    }
       
    public void setTmpNbPortLibre(int fnb) {
            tmpNbPortLibre.set(fnb);
       
    }
    
    public int getTmpNbPortOccupe() {
            return tmpNbPortOccup.get();
        
    }
       
    public void setTmpNbPortOccupe(int fnb) {
            tmpNbPortOccup.set(fnb);
       
    }
    
    public int getTmpNbPortDefect() {
            return tmpNbPortDefect.get();
        
    }
       
    public void setTmpNbPortDefect(int fnb) {
            tmpNbPortDefect.set(fnb);
       
    }
    
    public String getNom() {
            return nom.get();
        
    }
       
    public void setNom(String fnom) {
            nom.set(fnom);
       
    }
    
    public String getRoute() {
            return route.get();
        
    }
       
    public void setRoute(String froute) {
            route.set(froute);
       
    }
    
    public String getModeConnect() {
            return modeConnect.get();
        
    }
       
    public void setModeConnect(String fmode) {
            modeConnect.set(fmode);
       
    }
    
    public boolean isSSH(){
        if(modeConnect.get().equals("SSH"))return true;
        return false;        
    }
    
    public String getVille() {
            return ville.get();
        
    }
       
    public void setVille(String fville) {
            ville.set(fville);
       
    }
    
    public String getSecteur() {
            return secteur.get();
        
    }
       
    public void setSecteur(String fsecteur) {
            secteur.set(fsecteur);
       
    }
   
    
    
}
