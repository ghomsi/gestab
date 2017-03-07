/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ghomsi
 */
public class Ville {
    private IntegerProperty id;
    private final StringProperty nom;
    
    private final IntegerProperty nbSect;
    private final IntegerProperty nbEq;
    private final IntegerProperty nbCart;
    private final IntegerProperty nbPort;
    private final IntegerProperty nbPortLibre;
    private final IntegerProperty nbPortOccup;
    private final IntegerProperty nbPortDefect;
    private final IntegerProperty nbClient;
    private final IntegerProperty nbUser;
    
    public Ville(String nom){
        this.nom = new SimpleStringProperty(nom);
        this.nbEq = new SimpleIntegerProperty(0);
        this.nbCart = new SimpleIntegerProperty(0);
        this.nbPort = new SimpleIntegerProperty(0);
        this.nbPortLibre = new SimpleIntegerProperty(0);
        this.nbPortOccup = new SimpleIntegerProperty(0);
        this.nbPortDefect = new SimpleIntegerProperty(0);
        this.nbSect = new SimpleIntegerProperty(0);
        this.nbClient = new SimpleIntegerProperty(0);
        this.nbUser = new SimpleIntegerProperty(0);
        this.id = new SimpleIntegerProperty(0);
        
    }
    
    public String getNom(){
        return nom.get();
    }
    public void setNom(String fnom){
        nom.set(fnom);
    }
    
    
    public int getId(){
        return id.get();
    }
    public void setId(int fid){
        id.set(fid);
    }
    
    public int getNbEq() {
            return nbEq.get();
        
    }
       
    public void setNbEq(int fnb) {
            nbEq.set(fnb);
       
    }
    
    public int getNbCarte() {
            return nbCart.get();
        
    }
       
    public void setNbCarte(int fnb) {
            nbCart.set(fnb);
       
    }
    
    public int getNbPort() {
            return nbPort.get();
        
    }
       
    public void setNbPort(int fnb) {
            nbPort.set(fnb);
       
    }
    
    public int getNbPortLibre() {
            return nbPortLibre.get();
        
    }
       
    public void setNbPortLibre(int fnb) {
            nbPortLibre.set(fnb);
       
    }
    
    public int getNbPortOccupe() {
            return nbPortOccup.get();
        
    }
       
    public void setNbPortOccupe(int fnb) {
            nbPortOccup.set(fnb);
       
    }
    
    public int getNbPortDefect() {
            return nbPortDefect.get();
        
    }
       
    public void setNbPortDefect(int fnb) {
            nbPortDefect.set(fnb);
       
    }
    
    public int getNbClients() {
            return nbClient.get();
        
    }
       
    public void setNbClients(int fnb) {
            nbClient.set(fnb);
       
    }
    
    public int getNbUsers() {
            return nbUser.get();
        
    }
       
    public void setNbUser(int fnb) {
            nbUser.set(fnb);
       
    }
    
    
    
    
}
