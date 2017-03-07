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

//+++ status defaults values: 1=Libre 2=occupé 3=Défectueux
public class Port {
    
    private IntegerProperty id;
    private final IntegerProperty idCarte;
    private final IntegerProperty idVille;
    private final IntegerProperty numeroPort;
    private final IntegerProperty status; 
    private final StringProperty desc;
    
    
    
    public Port(int np) {
            this.idCarte = new SimpleIntegerProperty(0);
            this.idVille = new SimpleIntegerProperty(0);
            this.id = new SimpleIntegerProperty(0);
            this.numeroPort = new SimpleIntegerProperty(np);
            this.desc = new SimpleStringProperty("N/A");
            this.status= new SimpleIntegerProperty(0);
        
    }
    
    public int getId() {
            return id.get();
        
    }
       
    public void setId(int fid) {
            id.set(fid);
       
    }
    
    public int getIdCarte() {
            return idCarte.get();
        
    }
       
    public void setIdCarte(int fid) {
            idCarte.set(fid);
       
    }
    
    public int getIdVille() {
            return idVille.get();
        
    }
       
    public void setIdVille(int fid) {
            idVille.set(fid);
       
    }
    
    
    public int getNumeroPort() {
            return numeroPort.get();
        
    }
       
    public void setNumeroPort(int fnum) {
            numeroPort.set(fnum);
       
    }
    
    public String getDesc() {
            return desc.get();
        
    }
       
    public void setDesc(String fdesc) {
            desc.set(fdesc);
       
    }
    
    public int getStatut() {
            return status.get();
        
    }
       
    public void setStatus(int fst) {
            status.set(fst);
       
    }
    
    public String giveStatus(){
        String St = "";
        if(status.get()==0){
            St="Libre";
        }
        if(status.get()==1){
            St="Occupé";
        }
        if(status.get()==2){
            St="Défectueux";
        }
        
        return St;
    }
    
    public boolean isDesactivate(){
        if(status.get()==2){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean isLibre(){
        if(status.get()==0){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean isOccupe(){
        if(status.get()==1){
            return true;
        }else{
            return false;
        }
    }
    
    public int librerer(){
        return 0;
    }
    
    public int occupe(){
        return 1;
    }
    
    public int defectueux(){
        return 2;
    }
    
    public String colorlibre(){
        return "#05B30B";
    }
    
    public String colloroccupe(){
        return "#10A5DE";
    }
    
    public String colordefectueux(){
        return "#F51111";
    }
    
    public String giveColor(){
        String St = "";
        if(status.get()==0){
            St=colorlibre();
        }
        if(status.get()==1){
            St=colloroccupe();
        }
        if(status.get()==2){
            St=colordefectueux();
        }
        
        return St;
    }
    
    
}
