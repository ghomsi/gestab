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

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ghomsi
 */
public class Historique {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty statut;
    private SimpleStringProperty date;
    private SimpleStringProperty raison;
    
    public Historique(String date,String raison,int status){
        this.date = new SimpleStringProperty(date);
        this.raison = new SimpleStringProperty(raison);
        this.statut = new SimpleIntegerProperty(status);
        this.id = new SimpleIntegerProperty(0);
    }
    
    public int getId() {
            return id.get();
        
    }
       
    public void setId(int fid) {
            id.set(fid);
       
    }
    public int getStatus() {
            return statut.get();
        
    }
       
    public void setVille(int fstatut) {
            statut.set(fstatut);
       
    }
    public String getRaison() {
            return raison.get();
        
    }
       
    public void setRaison(String fraison) {
            raison.set(fraison);
       
    }
    
    public String getDate() {
            return date.get();
        
    }
       
    public void setDate(String fdate) {
            date.set(fdate);
       
    }
}
