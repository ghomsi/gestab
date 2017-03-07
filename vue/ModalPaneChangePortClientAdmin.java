/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Carte;
import modele.Categorie;
import modele.Client;
import modele.Debit;
import modele.Equipement;
import modele.Port;
import modele.Secteur;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class ModalPaneChangePortClientAdmin {
    
    private final ActionEvent event;
    Scene scene,scene2;
    private final Client client;
    private final DAOequipement dao;
    private Ville villeR;
    private Ville villeOwn;
    private Secteur sectR;
    private Equipement eqR;
    private Carte carteR;
    private Debit debitR;
    private Categorie categorieR;
    private Port portR;
    private HBox statB;
    private Button btnD;
    private TitledPane tps;
    
    public ModalPaneChangePortClientAdmin(DAOequipement dao,ActionEvent event, Client client,HBox statB,Button btn,TitledPane tps){
        this.event = event;
        this.client = client;
        this.statB = statB;
        this.btnD = btn;
        this.tps = tps;
        dao.couleur =dao.colordeAdminEq();
        dao.boutonColor="buttonLogin";
        this.dao = dao;
    }
    
    public void construire(){
    
        Stage stage = new Stage();
        Label label = new Label();
        VBox containt = new VBox(5);
        
        GridPane grid = new GridPane ();
        grid.setAlignment(Pos.CENTER);

        grid.setPadding(new Insets (10,10,10,10));
        grid.setVgap(7);
        grid.setHgap(7);

        
       

        //BUTTON
        
        
        Button resetbt = new Button ("Annuler");
        resetbt.getStyleClass().add("buttonAdminEq");
        
        
        resetbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
            });

        
       
        

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(resetbt);
        
        containt.getChildren().addAll(label,new PaneClientView(dao,client,label,grid,containt).construire(),hbBtn);
        
       
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(containt);
        sp.setPadding(new Insets(10, 10, 10, 10));
                
        scene = new Scene(sp, (dao.width*70)/100, (dao.height*80)/100);
        scene.getStylesheets().add("css/style.css");
               
                
                
        stage.setScene(scene);
        stage.setTitle("Detail Client");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(
            ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
}
