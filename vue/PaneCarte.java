/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import modele.Carte;
import modele.Equipement;

/**
 *
 * @author ghomsi
 */
public class PaneCarte {
    FlowPane pane;
    Image images;
    ImageView pics;
    TitledPane tps;
    Equipement equipement;
    final Label label = new Label("N/A");
    private DAOequipement dao;
    private HBox hbox;
    private HBox pageHBlabel;
    
    

    public PaneCarte(DAOequipement dao,Equipement eq,HBox HBlabel) {
        this.tps = new TitledPane();
        this.pics = new ImageView();
        this.images = new Image(getClass().getResourceAsStream("/images/user.png"));
        this.equipement = eq;
        HBlabel.getChildren().clear();
        this.pageHBlabel = HBlabel;
        this.dao = dao;
        
            
    }
    
    public FlowPane construire(){
        pane=new FlowPane();
        pane.minHeightProperty().set(dao.getHeigth());
        
        //Adding tree items to the root
        ArrayList cartes = (ArrayList) dao.getCarteEq().get(equipement.getIdEQ());
       
        
        Label pageLabel = new Label(" Nombre cartes:");
        pageLabel.setTextFill(Color.web(dao.colordeGestionEq()));
        Label pageLabel2 = new Label(""+cartes.size());
        
        pageHBlabel.getChildren().addAll(pageLabel,pageLabel2);
        
        VBox Vbox = new VBox(10);
        Vbox.setPadding(new Insets(20, 0, 0, 20));
        
        //---- combo Box cartes
        //ObservableList<String> options =FXCollections.observableArrayList();
        ObservableList<Carte> options =FXCollections.observableArrayList();
        
        if(!cartes.isEmpty()){
            options.add(new Carte(0));
  
        }
        
        
        
        Iterator<Integer> it_carte = cartes.iterator();
        while(it_carte.hasNext()) {
            int id_carte = it_carte.next();
            Carte elt = (Carte) dao.getCarte().get(id_carte);
            
            // --- GridPane container
            //HBox pour l'objet port 
            hbox = new HBox(10);
            hbox.setPadding(new Insets(20, 0, 0, 20));
            HBox hb = new HBox(10);
            TitledPane gridTitlePane =  dao.carteTitleDPane(dao,elt,hb,hbox,Vbox,label,options);

                hbox.getChildren().addAll(gridTitlePane, dao.portScrollPane(dao,elt,hb,label));
            
            
            Vbox.getChildren().addAll(hbox);
            
            options.add(elt);
        }
        
        
        
        
        final ComboBox comboBox = dao.comboBoxCarte(options,cartes);
        
        comboBox.valueProperty().addListener(new ChangeListener<Carte>() {
            @Override public void changed(ObservableValue ov, Carte t, Carte t1) {
              
                Vbox.getChildren().clear();
                if(t1.getNumCarte()!=0){
                    HBox tmpHBox = new HBox(10);
                    tmpHBox.setPadding(new Insets(20, 0, 0, 20));
                    HBox hb = new HBox(10);
                    tmpHBox.getChildren().addAll(dao.carteTitleDPane(dao, t1, hb,hbox,Vbox,label,options),dao.portScrollPane(dao,t1,hb,label));
                    Vbox.getChildren().addAll(tmpHBox);
                }else{
                    Vbox.getChildren().addAll(hbox);
                }    
               
            }    
        });
        
        
        
        
        
        
        
         
        
        pane.getChildren().addAll(comboBox,Vbox);
        
        
        return pane;
    }
}
