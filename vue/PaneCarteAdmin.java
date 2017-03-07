/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import modele.Carte;
import modele.Equipement;

/**
 *
 * @author ghomsi
 */
public class PaneCarteAdmin {
    
    FlowPane pane;
    Image images;
    ImageView pics;
    TitledPane tps;
    Equipement equipement;
    HBox pageHBlabel;
    private DAOequipement dao;
    private HBox hbox;
    final Label label = new Label("N/A");
    private GridPane gridR;
    private Label labelT;
    private VBox paneDesc;
    
    

    public PaneCarteAdmin(DAOequipement dao,Equipement eq,HBox HBlabel,GridPane grid,Label label,VBox pane) {
        
        this.tps = new TitledPane();
        this.pics = new ImageView();
        this.images = new Image(getClass().getResourceAsStream("/images/user.png"));
        this.equipement = eq;
        HBlabel.getChildren().clear();
        this.pageHBlabel = HBlabel;
        this.dao = dao;
        this.gridR = grid;
        this.labelT=label;
        this.paneDesc = pane;
            
    }
    
    public FlowPane construire(){
        pane=new FlowPane();
        pane.minHeightProperty().set(dao.getHeigth());
        
       
        ArrayList cartes = (ArrayList) dao.getCarteEq().get(equipement.getIdEQ());
        if(cartes==null){
            cartes = new ArrayList();
        }
        
        Button modifEq = new Button("modifier");
        modifEq.getStyleClass().add("buttonAdminEq");
        Button deletEq = new Button("Supprimer");
        deletEq.getStyleClass().add(dao.colordanger());
        Label nbCarteLB = new Label(" Nombre cartes:");
        nbCarteLB.setTextFill(Color.web(dao.colordeAdminEq()));
        
        Label modeCarteLB = new Label(" Mode connection:");
        modeCarteLB.setTextFill(Color.web(dao.colordeAdminEq()));
        
        
        Label valeurnbCarteLB = new Label();
        Label valeurmodeCarteLB = new Label(equipement.getModeConnect());
        valeurnbCarteLB.setTextFill(Color.web(dao.colordeAdminEq()));
        if(cartes != null){
            valeurnbCarteLB.setText(""+cartes.size());
        }
        if(dao.user.getNivoAccess()==1){
            pageHBlabel.getChildren().addAll(deletEq,modifEq,nbCarteLB,valeurnbCarteLB,modeCarteLB,valeurmodeCarteLB);
        }else if(dao.user.getNivoAccess()==2){
            pageHBlabel.getChildren().addAll(modifEq,nbCarteLB,valeurnbCarteLB,modeCarteLB,valeurmodeCarteLB);
        }else{
            pageHBlabel.getChildren().addAll(nbCarteLB,valeurnbCarteLB);
        }
        VBox Vbox = new VBox(10);
        Vbox.setPadding(new Insets(20, 0, 0, 20));
        
        //---- combo Box cartes
        ObservableList<Carte> options = FXCollections.observableArrayList();
        
        if(cartes != null && !cartes.isEmpty()){
            options.add(new Carte(0));
  
        }
        
        Iterator<Integer> it_carte = cartes.iterator();
        while(it_carte.hasNext()) {
            int id_carte = it_carte.next();
            Carte elt = (Carte) dao.getCarte().get(id_carte);
            
            hbox = new HBox(10);
            hbox.setPadding(new Insets(20, 0, 0, 20));
            HBox hb = new HBox(10);
            TitledPane gridTitlePane=null;
            ScrollPane p = null;
            if(elt!=null){
                gridTitlePane =  dao.carteTitleDPane(dao,elt,hb,hbox,Vbox,label,options);
                p =dao.portScrollPaneAdmin(dao,elt,hb,label);
            }
            
            if(p!=null){
                hbox.getChildren().addAll(gridTitlePane, p);
            }
            
            Vbox.getChildren().addAll(hbox);
            
            options.add(elt);
        }
        
        
        
        
        
        
        //---- Button Ajouter une ville 
        //Button addVille = new Button("+", new ImageView(imageOk));
        Button addCarte = new Button("+");
        addCarte.getStyleClass().add("buttonAdminEq");
        final Tooltip tooltipCarte = new Tooltip();
        tooltipCarte.setText("Ajouter une carte");
        addCarte.setTooltip(tooltipCarte);
        FlowPane villePane = new FlowPane();
        
        
        addCarte.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               System.out.println("add ville");
               
                ModalAddCarte modal = new ModalAddCarte(dao,event,options,Vbox,valeurnbCarteLB);
                modal.construire(equipement);
                
            }
        });
        deletEq.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               //System.out.println("delete equipement");
               
                ModalDeleteEq modal = new ModalDeleteEq(dao,event,equipement,pageHBlabel,paneDesc,gridR,labelT);
                modal.construire();
                
            }
        });
        modifEq.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               
                ModalModifierEq modal = new ModalModifierEq(event,dao,pageHBlabel,equipement,pane);
                try {
                    modal.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PaneCarteAdmin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PaneCarteAdmin.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        
        final ComboBox comboBox = dao.comboBoxCarte(options,cartes);
        
              
        comboBox.valueProperty().addListener(new ChangeListener<Carte>() {
            @Override public void changed(ObservableValue ov, Carte t, Carte t1) {
              
                Vbox.getChildren().clear();
                if(t1.getNumCarte()!=0){
                    HBox tmpHBox = new HBox(10);
                    tmpHBox.setPadding(new Insets(20, 0, 0, 20));
                    HBox hb = new HBox(10);
                    tmpHBox.getChildren().addAll(dao.carteTitleDPane(dao, t1, hb,hbox,Vbox,label,options),dao.portScrollPaneAdmin(dao,t1,hb,label));
                    Vbox.getChildren().addAll(tmpHBox);
                }else{
                    Vbox.getChildren().addAll(hbox);
                }
            }    
        });
        
        
        
        
        
        
         
        
        pane.getChildren().addAll(comboBox,addCarte,Vbox);
        
        
        return pane;
    }
    
    
    
}
