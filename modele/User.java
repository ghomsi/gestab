/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ghomsi
 */
public class User {
    
    
    private SimpleStringProperty nom;
    private SimpleStringProperty prenom;
    private SimpleStringProperty numTel;
    private SimpleStringProperty quartier;
    private SimpleStringProperty login;
    private final SimpleStringProperty passwd;
    private SimpleIntegerProperty ville;
    private SimpleStringProperty email;
    private SimpleStringProperty img;
    private SimpleStringProperty desc;
    private SimpleStringProperty route;
    private SimpleIntegerProperty nivAcces;
    private SimpleIntegerProperty id;
        
    
       
    public User() {
            this.nom = new SimpleStringProperty("");
            this.prenom = new SimpleStringProperty("");
            this.ville = new SimpleIntegerProperty(0);
            this.quartier = new SimpleStringProperty("");
            this.passwd = new SimpleStringProperty("");
            this.email = new SimpleStringProperty("");
            this.nivAcces = new SimpleIntegerProperty(0);
            this.id = new SimpleIntegerProperty(0);
            this.numTel = new SimpleStringProperty("");
            this.login = new SimpleStringProperty("");
            this.desc = new SimpleStringProperty("");
            this.route = new SimpleStringProperty("");
        
    }
        
    
    public int getVille() {
            return ville.get();
        
    }
       
    public void setVille(int fville) {
            ville.set(fville);
       
    }
    
    public int getNivoAccess() {
            return nivAcces.get();
        
    }
       
    public void setNivoAccess(int fnA) {
            nivAcces.set(fnA);
       
    }
    
    public int getId() {
            return id.get();
        
    }
       
    public void setId(int fnA) {
            id.set(fnA);
       
    }
    
    public String getPrenom() {
            return prenom.get();
        
    }
       
    public void setPrenom(String fprenom) {
            prenom.set(fprenom);
       
    }
    
    public String getNom() {
            return nom.get();
        
    }
    
    public void setDesc(String fdesc) {
            desc.set(fdesc);
       
    }
    
    public String getDesc() {
            return desc.get();
        
    }
    
    public void setRoute(String fr) {
            route.set(fr);
       
    }
    
    public String getRoute() {
            return route.get();
        
    }
       
    public void setNom(String fNom) {
            nom.set(fNom);
       
    }
    
    public String getQuartier() {
            return quartier.get();
        
    }
       
    public void setQuartier(String fquartier) {
            quartier.set(fquartier);
       
    }
    
    public String getLogin() {
            return login.get();
        
    }
       
    public void setLogin(String flogin) {
            login.set(flogin);
       
    }
    
    public String getPasswd() {
            return passwd.get();
        
    }
       
    public void setPasswd(String fpwd) {
            passwd.set(fpwd);
       
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
    
    public String getImg() {
            return img.get();

    }
    
    public void setImg(String Uimg) {
            img.set(Uimg);

    }
    
  
    
    public String getCategorie(){
        if(nivAcces.get()==1){
            return "Super Admin";
        }
        
        if(nivAcces.get()==2){
            return "Admin";
        }
        
        if(nivAcces.get()==3){
            return "Gestionnaire";
        }
        
        if(nivAcces.get()==6){
            return "Technicien Admin";
        }
        
        if(nivAcces.get()==5){
            return "Technicien gestion";
        }
        return "Sous Gestionnaire";
    }
    
    public int getintCategorie(String a){
        if(a.equals("Super Admin")){
            return 1;
        }
        
        if(a.equals("Admin")){
            return 2;
        }
        if(a.equals("Gestionnaire")){
            return 3;
        }
        if(a.equals("Technicien Admin")){
            return 6;
        }
        if(a.equals("Technicien gestion")){
            return 5;
        }
        return 4;
    }
}
