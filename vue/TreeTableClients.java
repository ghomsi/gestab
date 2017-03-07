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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modele.Client;

/**
 *
 * @author ghomsi
 */
public class TreeTableClients {
    
    private VBox pane,paneR;
    private final Label label;
    private final GridPane grid;
    
    private TreeItem<Client> rootR;
    private DAOequipement dao;
    
    private final ImageView depIcon = new ImageView (
            new Image(getClass().getResourceAsStream("/icon/secteuricon.png"))
    );

    TreeTableClients(DAOequipement dao,VBox paneDesc,Label lblscene,GridPane grid,VBox pane) {
        this.pane=new VBox();
        this.pane = paneDesc;
        this.label = lblscene;
        this.grid = grid;
        this.paneR = pane;
        rootR = new TreeItem<>();
        this.dao = dao;
        
        
    }

    
    void construire(){
        
        dao.treeTableViewClient(dao, pane, label, grid);
        
        
        TextField rechT = new TextField();
        rechT.setPromptText("Numero Client");
        final Tooltip tooltipCL = new Tooltip();
        tooltipCL.setText("Recherchez un client par sont numero de telephone Camtel...");
        rechT.setTooltip(tooltipCL);
        Button rechercherCl=new Button();
        rechercherCl.getStyleClass().add(dao.boutonColor);
        rechercherCl.setText("Rechercher");
        
        HBox hbr = new HBox(10);
        hbr.setAlignment(Pos.TOP_RIGHT);
        
        hbr.getChildren().addAll(rechT,rechercherCl);
        //grid.add(hbr, 2, 1);
        paneR.getChildren().add(hbr);
        
        rechercherCl.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if(!rechT.getText().isEmpty()){
                        Hashtable numeros = dao.getNumero();
                        if(numeros.containsKey(rechT.getText())){
                            int id = (int) numeros.get(rechT.getText());
                            Client clientRech = (Client) dao.getClient().get(id);
                            label.setText(clientRech.getNom()+" "+clientRech.getPrenom());
                            pane.getChildren().clear();
                            pane.getChildren().addAll(new PaneClientView(dao,clientRech,label,grid,pane).construire());

                        }else{
                            label.setText("Numero Client inconnu..");
                            pane.getChildren().clear();
                        }
                    }else{
                        label.setText("Champ de recherche vide..");
                        pane.getChildren().clear();
                    }
                }
        });
        
        
    }
    
    
    
    
}
