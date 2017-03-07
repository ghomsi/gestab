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
public class Debit {
    private SimpleStringProperty nom;
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty profil;
    public Debit(){
        this.nom = new SimpleStringProperty("");
        this.id = new SimpleIntegerProperty(0);
        this.profil = new SimpleIntegerProperty(0);
    }
    
    public int getId(){
        return id.get();
    }
    
    public void setId(int fid){
        id.set(fid);
    }
    
    public int getprofil(){
        return profil.get();
    }
    
    public void setProfil(int fp){
        profil.set(fp);
    }
    
    public String getNom(){
        return nom.get();
    }
    
    public void setNom(String fnom){
        nom.set(fnom);
    }
}
