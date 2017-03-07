/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
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
public class PaneEquipement {
    VBox pane,paneDesc;
    HBox HBlabel;
    Button btnscene;
    Label lblscene;
    private DAOequipement dao;
    
   
    

    PaneEquipement(DAOequipement dao) {
        dao.couleur =dao.colordeGestionEq();
        dao.boutonColor="buttonGestEq";
        dao.comboColor ="comboGestEq";
        this.dao = dao;
        
    }
      
    
    
    public VBox construire() throws ClassNotFoundException, SQLException{
        pane=new VBox();
        HBlabel = new HBox();
        pane.minHeightProperty().set(dao.getHeigth());
        paneDesc =new VBox();
        paneDesc.minHeightProperty().set(dao.getHeigth());
        pane.setPadding(new Insets(5, 5, 10, 10));
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        
        
        Image eqimages = new Image(getClass().getResourceAsStream("/images/equipement.png"));
        
        ImageView eqpics = new ImageView(eqimages);
        eqpics.setPreserveRatio(true);
        
        HBox imhb = new HBox();
        imhb.getChildren().add(eqpics);
        imhb.setPadding(new Insets(50, 50, 10, 10));
        
        
        lblscene=new Label("Gestion Equipements");
        lblscene.setTextFill(Color.web(dao.colordeGestionEq()));
        paneDesc.getChildren().addAll(imhb);
        
        Hashtable villes = dao.getVille();
        Enumeration e =villes.elements();
        
        
        TreeTableEQ treeTableView = new TreeTableEQ(dao,paneDesc,lblscene,HBlabel,grid);
        treeTableView.construire();
        
        
        
        //---- combo Box villes
        ObservableList<Ville> options =FXCollections.observableArrayList();
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
                        if(t1!=null){
                            comboBox.setEditable(false);
                            if(t1.getNom().equals("Tout")){
                                dao.treetableviewEq(dao, paneDesc, lblscene,HBlabel, grid,"treetablevieweq");
                            }else{
                                dao.treetableviewEq(dao, paneDesc, lblscene,HBlabel, grid,"treetablevieweq",t1);
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
               //System.out.println("add ville");
               
                ModalAddVille modal = new ModalAddVille(event,dao,lblscene,HBlabel,grid,paneDesc);
                try {
                    modal.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PaneEquipement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PaneEquipement.class.getName()).log(Level.SEVERE, null, ex);
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
        if(dao.isAdmin(dao.user)||dao.isSuperAdmin(dao.user)){
            headB.getChildren().addAll(comboBox,addVille);
        }else{
            headB.getChildren().addAll(comboBox);
        }
        
        
        
        
        grid.add(headB, 0, 0);
       
        lblscene.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
        HBox htb = new HBox();
        htb.getChildren().addAll(lblscene,HBlabel);
        HBox head = new HBox(5);
        head.getChildren().addAll(btnU,btnStat,htb);
        grid.add(head, 1, 0);
        
        //grid.add(treeTableView, 0, 1);
       
        grid.add(paneDesc,1,1);
        //pane.getChildren().addAll(lblscene, btnscene);
        pane.getChildren().addAll(grid);
        pane.autosize();
        return pane;
    }
    

}
