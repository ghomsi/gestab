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
public class Carte {
    
    private IntegerProperty id;
    private final IntegerProperty idEQ;
    private final IntegerProperty idVille;
    private final IntegerProperty  numCarte;
    private final IntegerProperty  nbPort;
    private final IntegerProperty  nbPortL;
    private final IntegerProperty  nbPortO;
    private final IntegerProperty  nbPortD;
    private final StringProperty desc;
    
    
   
    
    
    public Carte(int numeroCarte) {
            this.idEQ = new SimpleIntegerProperty(0);
            this.idVille = new SimpleIntegerProperty(0);
            this.id = new SimpleIntegerProperty(0);
            this.numCarte= new SimpleIntegerProperty(numeroCarte);
            this.nbPort= new SimpleIntegerProperty(0);
            this.nbPortL= new SimpleIntegerProperty(0);
            this.nbPortO= new SimpleIntegerProperty(0);
            this.nbPortD= new SimpleIntegerProperty(0);
            
            this.desc = new SimpleStringProperty("N/A");
        
    }
    
    
    public int getId() {
            return id.get();
        
    }
       
    public void setId(int fid) {
            id.set(fid);
       
    }
    
    public int getIdEQ() {
            return idEQ.get();
        
    }
       
    public void setIdEQ(int fidEQ) {
            idEQ.set(fidEQ);
       
    }
    
    public int getIdVille() {
            return idVille.get();
        
    }
       
    public void setIdVille(int fid) {
            idVille.set(fid);
       
    }
    public int getNumCarte() {
            return numCarte.get();
        
    }
       
    public void setNumCarte(int fnumC) {
            numCarte.set(fnumC);
       
    }
    
    public int getNbPort() {
            return nbPort.get();
        
    }
       
    public void setNbPort(int fnbP) {
            nbPort.set(fnbP);
       
    }
    
    public int getNbPortLibre() {
            return nbPortL.get();
        
    }
       
    public void setNbPortLibre(int fnbPL) {
            nbPortL.set(fnbPL);
       
    }
    
    public int getNbPortOccupe() {
            return nbPortO.get();
        
    }
       
    public void setNbPortOccupe(int fnbPO) {
            nbPortO.set(fnbPO);
       
    }
    
    public int getNbPortDesactive() {
            return nbPortD.get();
        
    }
       
    public void setNbPortDesactive(int fnbPD) {
            nbPortD.set(fnbPD);
       
    }
    
    public String getDesc() {
            return desc.get();
        
    }
       
    public void setDesc(String fdesc) {
            desc.set(fdesc);
       
    }
    
    
}
