/*
 * Copyright (C) 2016 ghomsi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package modele;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

/**
 *
 * @author ghomsi
 */
public class Notification  {
    
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty id_emetteur;
    private final SimpleIntegerProperty id_recepteur;
    private SimpleStringProperty objet;
    private SimpleStringProperty message;
    private SimpleStringProperty date;
    private SimpleBooleanProperty isVue;
    private Image photo;
    
    public Notification(){
        this.id = new SimpleIntegerProperty(0);
        this.id_emetteur = new SimpleIntegerProperty(0);
        this.id_recepteur = new SimpleIntegerProperty(0);
        this.objet = new SimpleStringProperty("");
        this.message = new SimpleStringProperty("");
        this.date = new SimpleStringProperty("");
        this.isVue = new SimpleBooleanProperty(false);
    
    } 
    
    public int getId() {
            return id.get();
        
    }
       
    public void setId(int fid) {
            id.set(fid);
       
    }
    
    public int getIdEmet() {
            return id_emetteur.get();
        
    }
       
    public void setIdEmet(int fid) {
            id_emetteur.set(fid);
       
    }
    public int getIdRecep() {
            return id_recepteur.get();
        
    }
       
    public void setIdRecep(int fid) {
            id_recepteur.set(fid);
       
    }
    public String getObjet() {
            return objet.get();
        
    }
       
    public void setObjet(String fobjet) {
            objet.set(fobjet);
       
    }
    public String getMessage() {
            return message.get();
        
    }
       
    public void setMessage(String fmsg) {
            message.set(fmsg);
       
    }
    public String getDate() {
            return date.get();
        
    }
       
    public void setDate(String fdate) {
            date.set(fdate);
       
    }
    public Boolean getvue() {
            return isVue.get();
        
    }
       
    public void setVue(Boolean fvue) {
            isVue.set(fvue);
       
    }
    public Image getImage(){
     if(!isVue.get()){
         return new Image(getClass().getResourceAsStream("/icon/commingnotif.png"),30,30,false,false);
     }else{
         return new Image(getClass().getResourceAsStream("/icon/email.png"),30,30,false,false);
     }
    
    }
}
