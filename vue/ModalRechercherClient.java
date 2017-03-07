/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.util.Hashtable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import modele.Client;

/**
 *
 * @author ghomsi
 */
public class ModalRechercherClient {
    
    VBox pane,pane2;
    Stage stage;
    Scene scene;
    DAOequipement dao;

    ModalRechercherClient(DAOequipement dao,Stage stage,Scene scene) {
        this.stage = stage;
        this.scene = scene;
        this.dao = dao;
    }

   
    public VBox contruire(){
        pane = new VBox();
        pane2 = new VBox();
        pane.getStylesheets().add("css/style.css");
        pane2.getStylesheets().add("css/style.css");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 25, 25, 10));
        
        TextField rechT = new TextField();
        rechT.setPromptText("Numero Client");
        final Tooltip tooltipCL = new Tooltip();
        tooltipCL.setText("Recherchez un client par sont numero de telephone Camtel...");
        rechT.setTooltip(tooltipCL);
        Button rechercherCl=new Button();
        rechercherCl.getStyleClass().add(dao.colornodanger());
        rechercherCl.setText("Rechercher");
        
        HBox hbr = new HBox(10);
        hbr.setAlignment(Pos.TOP_RIGHT);
        
        
        
        Label actiontarget = new Label("");
        actiontarget.setTextFill(Color.web(dao.couleur));
        actiontarget.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 20));
        
        rechercherCl.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if(!rechT.getText().isEmpty()){
                        Hashtable numeros = dao.getNumero();
                        if(numeros.containsKey(rechT.getText())){
                            int id = (int) numeros.get(rechT.getText());
                            Client clientRech = (Client) dao.getClient().get(id);
                            actiontarget.setText(clientRech.getNom()+" "+clientRech.getPrenom());
                            pane2.getChildren().clear();
                            pane2.getChildren().addAll(new PaneClientView(dao,clientRech,actiontarget,grid,pane).construire());

                        }else{
                            actiontarget.setText("Numero Client inconnu..");
                            pane2.getChildren().clear();
                        }
                    }else{
                        actiontarget.setText("Champ de recherche vide..");
                        pane2.getChildren().clear();
                    }
                }
        });

             
             
                
                

        Button btn2 = new Button("Annuler");
        btn2.getStyleClass().add(dao.colorannuler());
        Button btn = new Button("< ajouter un client");
        btn.getStyleClass().add(dao.boutonColor);
                
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                stage.close();
            }
        });

        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                stage.setScene(scene);
                stage.setTitle("Ajouter un Client");
            }
        });
        
        hbr.getChildren().addAll(new Label("𐌣"),rechT,rechercherCl,btn,btn2);
                
                
                
        //conteneur vertical principale        
        VBox VerticalContent = new VBox(10);
        VerticalContent.setAlignment(Pos.BOTTOM_RIGHT);
        VerticalContent.getChildren().addAll();


        VerticalContent.getChildren().addAll(actiontarget,hbr,pane2);
                
        pane.getChildren().addAll(VerticalContent);
        
        
        return pane;
    }
}
