/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class PaneAdminEquipement {
    VBox pane,paneDesc;
    Button btnscene,ajouterEQ,ajouterSect;
    Label lblscene;
    HBox HBlabel;
    DAOequipement dao;
    
    public PaneAdminEquipement(DAOequipement dao){
        dao.couleur =dao.colordeAdminEq();
        dao.boutonColor="buttonAdminEq";
        dao.comboColor ="comboAdminEq";
        this.dao = dao;
    }
    
    
    public VBox construire() throws ClassNotFoundException, SQLException{
        pane=new VBox();
        pane.minHeightProperty().set(dao.getHeigth());
        paneDesc =new VBox();
        paneDesc.minHeightProperty().set(dao.getHeigth());
        pane.setPadding(new Insets(5, 5, 10, 10));
        
        
        //pane.setVgap(10);
        //pane.setHgap(10);
        
        GridPane grid = new GridPane();
        //grid.setHgap(10);
        //grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(5);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        
        
        
        
        
        
        
        lblscene=new Label("Administration Equipements");
        lblscene.setTextFill(Color.web(dao.colordeAdminEq()));
        HBlabel=new HBox(5);
          //---- Button Ajouter une ville 
        //Button addVille = new Button("+", new ImageView(imageOk));
        
        Image eqimages = new Image(getClass().getResourceAsStream("/images/secteurville.png"));
        
        ImageView eqpics = new ImageView(eqimages);
        eqpics.setPreserveRatio(true);
        
        HBox imhb = new HBox();
        imhb.getChildren().add(eqpics);
        imhb.setPadding(new Insets(50, 50, 10, 10));
        paneDesc.getChildren().addAll(imhb);
       
        
        
        Enumeration e =dao.getVille().elements();
        TreeTableEQAdmin treeTableView = new TreeTableEQAdmin(dao,paneDesc,lblscene,HBlabel,grid);
        treeTableView.construire();
        
        
        //---- combo Box villes
        ObservableList<Ville> options = FXCollections.observableArrayList();
        options.add(new Ville("Tout"));
        
        
        while(e.hasMoreElements()){
            Ville ville =(Ville) e.nextElement();
            options.add(ville);
        }
        
        
        final ComboBox comboBox = new ComboBox(options);
        comboBox.setPadding(new Insets(10, 10, 10, 10));
                
                
        comboBox.setPromptText("Filtre");
        comboBox.setEditable(true);        
        comboBox.setCellFactory(new Callback<ListView<Ville>,ListCell<Ville>>(){
 
                    @Override
                    public ListCell<Ville> call(ListView<Ville> p) {

                        final ListCell<Ville> cell = new ListCell<Ville>(){

                            @Override
                            protected void updateItem(Ville t, boolean bln) {
                                super.updateItem(t, bln);

                                if(t != null){
                                    setText(t.getNom());
                                }else{
                                    setText(null);
                                }
                            }

                        };

                        return cell;
                    }
                });
        
        
        
        comboBox.valueProperty().addListener(new ChangeListener<Ville>() {
                    @Override public void changed(ObservableValue ov, Ville t, Ville t1) {
                        comboBox.setEditable(false);
                       if(t1!=null){ 
                            if(t1.getNom().equals("Tout")){
                                dao.treetableviewAdminEq(dao, paneDesc, lblscene,HBlabel, grid,"treetableviewAE");
                            }else{
                                dao.treetableviewAdminEq(dao, paneDesc, lblscene,HBlabel, grid,"treetableviewAE",t1);
                            }
                       } 
                    }    
                });
        
        
        
      
        
        
        
        //---Button ajouter ville
        Button addVille = new Button("+");
        addVille.getStyleClass().add(dao.boutonColor);
        final Tooltip tooltipVille = new Tooltip();
        tooltipVille.setText("Ajouter une ville");
        addVille.setTooltip(tooltipVille);
        
        
        addVille.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               
                ModalAddVille modal = new ModalAddVille(event,dao,lblscene,HBlabel,grid,paneDesc);
                try {
                    modal.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PaneAdminEquipement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PaneAdminEquipement.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        ImageView depIcon = new ImageView (
                        new Image(getClass().getResourceAsStream("/images/userswhite.png"),30,30,false,false)
                    );
        Button btnU = new Button();
        btnU.setGraphic(depIcon );
        btnU.getStyleClass().add(dao.boutonColor);
        btnU.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ModalAddClient modal = new ModalAddClient(t,dao,lblscene,grid,paneDesc);
                modal.construire();
            }
        });
        
        ImageView depIconStat = new ImageView (
                        new Image(getClass().getResourceAsStream("/images/iconstatwhite.png"),30,30,false,false)
        );
        Button btnStat = new Button();
        btnStat.setGraphic(depIconStat );
        btnStat.getStyleClass().add(dao.boutonColor);
        btnStat.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ModalStatClient modal = new ModalStatClient(t,dao);
                try {
                    modal.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PaneEquipement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PaneEquipement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        HBox headB = new HBox();
        if(dao.isSuperAdmin(dao.user)||dao.isAdmin(dao.user)){
            headB.getChildren().addAll(comboBox,addVille);
        }else{
            headB.getChildren().addAll(comboBox);
        }        
        
        
        
        
        //----- Button Ajouter un Secteur
        
        ajouterEQ = new Button("Ajouter un Equipement");
        ajouterEQ.getStyleClass().add(dao.boutonColor);
        final Tooltip tooltipEQ = new Tooltip();
        tooltipEQ.setText("Ajouter un equipement");
        ajouterEQ.setPrefWidth((dao.width*20)/100);
        ajouterEQ.setTooltip(tooltipEQ);
        
        ajouterEQ.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                ModalAddEQ modal = new ModalAddEQ(event,dao,grid,lblscene,HBlabel,paneDesc);
                try {
                    modal.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PaneAdminEquipement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PaneAdminEquipement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        });
        
        //----- Button Ajouter un Secteur
        
        ajouterSect = new Button("Ajouter un secteur");
        ajouterSect.getStyleClass().add(dao.boutonColor);
        final Tooltip tooltipSect = new Tooltip();
        tooltipSect.setText("Ajouter un secteur");
        ajouterSect.setTooltip(tooltipSect);
        ajouterSect.setPrefWidth((dao.width*20)/100);
        
        ajouterSect.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                ModalAddSecteur modal = new ModalAddSecteur(event,dao,lblscene,HBlabel,grid,paneDesc);
                try {
                    modal.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PaneAdminEquipement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PaneAdminEquipement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        });
        
        grid.add(headB, 0, 0);
        grid.add(btnStat, 2, 0);
        
        lblscene.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
        HBox htb = new HBox();
        htb.getChildren().addAll(lblscene,HBlabel);
        
        HBox head = new HBox(5);
        //head.getChildren().addAll(btnU,btnStat,htb);
        if(dao.isSuperAdmin(dao.user)){
            head.getChildren().addAll(btnU,htb);
        }else{
            head.getChildren().addAll(htb);
        }
        grid.add(head, 1, 0);
        if(dao.isSuperAdmin(dao.user)||dao.isAdmin(dao.user)){
            grid.add(ajouterEQ,0,2);
            grid.add(ajouterSect,0,3);
        }
        grid.add(paneDesc,1,1);
        
        pane.getChildren().addAll(grid);
        pane.autosize();
        return pane;
    }
    
    
    
}
