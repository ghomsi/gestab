/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ghomsi
 */
public class Client {
    
    private final SimpleIntegerProperty id;
    private SimpleStringProperty nom;
    private SimpleStringProperty prenom;
    private final SimpleStringProperty numTel;
    private SimpleStringProperty eSide;
    private SimpleStringProperty status;
    private SimpleStringProperty desc;
    private SimpleIntegerProperty debit;
    private final SimpleStringProperty dateCreation;
    private SimpleIntegerProperty ville;
    private SimpleIntegerProperty secteur;
    private final SimpleIntegerProperty eQ;
    private final SimpleIntegerProperty categorie;
    private final SimpleIntegerProperty carte;
    private final SimpleIntegerProperty port;
    private SimpleStringProperty email;
    private SimpleIntegerProperty villeOwner;
    
    private final BooleanProperty aSuspendre ;
    private final BooleanProperty valider ;
    private final BooleanProperty suspendre ;
    
        
    
    
       
    public Client() {
            this.id = new SimpleIntegerProperty(0);
            this.nom = new SimpleStringProperty("");
            this.prenom = new SimpleStringProperty("");
            this.numTel = new SimpleStringProperty("");
            this.email = new SimpleStringProperty("");
            this.villeOwner = new SimpleIntegerProperty(0);
            this.eSide = new SimpleStringProperty("");
            this.status = new SimpleStringProperty("");
            this.debit = new SimpleIntegerProperty(0);
            this.dateCreation = new SimpleStringProperty("");
            this.categorie = new SimpleIntegerProperty(0);
            this.aSuspendre = new SimpleBooleanProperty(false);
            this.valider = new SimpleBooleanProperty(false);
            this.suspendre = new SimpleBooleanProperty(false);
       
            
            //--- attachement port info
            this.desc = new SimpleStringProperty("N/A");
            this.ville = new SimpleIntegerProperty(0);
            this.secteur = new SimpleIntegerProperty(0);
            this.eQ = new SimpleIntegerProperty(0);
            this.carte = new SimpleIntegerProperty(0);
            this.port = new SimpleIntegerProperty(0);
            
            
            
        
    }
        
    
    public int getId() {
            return id.get();
        
    }
       
    public void setId(int fid) {
            id.set(fid);
       
    }
    
    public int getCarte() {
            return carte.get();
        
    }
       
    public void setCarte(int fcarte) {
            carte.set(fcarte);
       
    }
    
    public int getPort() {
            return port.get();
        
    }
       
    public void setPort(int fport) {
            port.set(fport);
       
    }
    
    public int getVille() {
            return ville.get();
        
    }
       
    public void setVille(int fville) {
            ville.set(fville);
       
    }
    
    public int getEq() {
            return eQ.get();
        
    }
       
    public void setEq(int feQ) {   
        eQ.set(feQ);
       
    }
    
    public int getCategorie() {
            return categorie.get();
        
    }
       
    public void setCategorie(int fcat) {
            categorie.set(fcat);
       
    }
    
    public int getVilleOwner() {
            return villeOwner.get();
        
    }
       
    public void setVilleOwner(int fville) {
            villeOwner.set(fville);
       
    }
    
    public String getDesc() {
            return desc.get();
        
    }
       
    public void setDesc(String fdesc) {
            desc.set(fdesc);
       
    }
    
    public String getDateCreation() {
            return dateCreation.get();
        
    }
       
    public void setDateCreation(String fdate) {
            dateCreation.set(fdate);
       
    }
    
    public int getDebit() {
            return debit.get();
        
    }
       
    public void setDebit(int fdeb) {
            debit.set(fdeb);
       
    }
    
    public String getEside() {
            return eSide.get();
        
    }
       
    public void setEside(String feSide) {
            eSide.set(feSide);
       
    }
    
    public int getSecteur() {
            return secteur.get();
        
    }
       
    public void setSecteur(int fsect) {
            secteur.set(fsect);
       
    }
    
    public String getStatut() {
            return status.get();
        
    }
       
    public void setStatus(String fstatus) {
            status.set(fstatus);
       
    }
    
    public String getNom() {
            return nom.get();
        
    }
       
    public void setNom(String fName) {
            nom.set(fName);
       
    }
    
    public String getPrenom() {
            return prenom.get();
        
    }
       
    public void setPrenom(String fName) {
            prenom.set(fName);
       
    }
    
    public String getNumTel() {
            return numTel.get();
        
    }
       
    public void setNumTel(String fnumTel) {
            numTel.set(fnumTel);
       
    }
    
    public String getEmail() {
            return email.get();

    }
    
    public void setEmail(String fName) {
            email.set(fName);

    }
    
    public String givestatusColor(){
        String St = "#e6e600";
        if(status.get().equals("ACTIF")){
            St="#05B30B";
        }
        if(status.get().equals("SUSPENDU")){
            St="#F51111";
        }  
        if(status.get().equals("RESILIE")){
            St="#cc6666";
        }         
        return St;
    }
    
    public boolean isactif(){
        if(status.get().equals("ACTIF")) return true;
        return false;
    }
    public boolean isSusp(){
        if(status.get().equals("SUSPENDU")) return true;
        return false;
    }
    public boolean isResil(){
        if(status.get().equals("RESILIE")) return true;
        return false;
    }
    
    public int getIntStatut(){
        if(status.get().equals("ACTIF")){
            return 1;
        }else if(status.get().equals("SUSPENDU")){
            return 2;
        }
        
        return 3;
    }
    
    
    
    public boolean isAsuspendre() {
        return aSuspendre.get();
    }

    public void setAsuspendre(boolean value) {
        aSuspendre.set(value);
    }

    public BooleanProperty aSuspendreProperty() {
        return aSuspendre;
    }
    
    public boolean isValider() {
        return valider.get();
    }

    public void setValider(boolean value) {
        valider.set(value);
    }

    public BooleanProperty validerProperty() {
        return valider;
    }
    
    public boolean isSuspendre() {
        return !isactif();
    }

    public void setSuspendre(boolean value) {
        suspendre.set(value);
    }

    public BooleanProperty suspendreProperty() {
        return suspendre;
    }
   
    
    
}
