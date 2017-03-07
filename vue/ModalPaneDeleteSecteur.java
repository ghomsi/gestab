/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Equipement;
import modele.Secteur;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class ModalPaneDeleteSecteur {
    
    HBox pageHBlabel;
    private final ActionEvent event;
    private ObservableList<String> options;
    private final Secteur secteur;
    private final Label labelT;
    private TreeItem<Equipement> rootR;
    private final GridPane gridR;
    private TreeTableView<Equipement> treeTableView;
    private VBox paneR;
    private DAOequipement dao;
    
    public ModalPaneDeleteSecteur(DAOequipement dao,ActionEvent event,Secteur secteur,Label label,HBox HBlabel,GridPane grid,VBox pane){
        this.event = event;
        this.secteur = secteur;
        this.labelT = label;
        this.pageHBlabel = HBlabel;
        this.gridR = grid;
        this.treeTableView = dao.getTreeTE();
        this.paneR = pane;
        this.dao =dao;
        
    }
    
    void construire(){
    
        Stage stage = new Stage();
        ///Creation des Variales
        Label lajout = new Label("Etes vous sur de vouloir supprimer ce Secteur:");
        lajout.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        lajout.setTextFill(Color.BLACK);
        lajout.setAlignment(Pos.BOTTOM_LEFT);
        // --- GridPane container
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("Nom: "), 0, 0);  
        
        final Label label = new Label(secteur.getNom());
        grid.add(label,1, 0);
        
        
        
        Label lbMessage = new Label("");
        lbMessage.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        Label lbMessage2 = new Label("");
        
        Button bvalid = new Button();
        bvalid.setText("Oui");
        bvalid.getStyleClass().add(dao.colordanger());
        bvalid.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                   
                   
                   paneR.getChildren().clear();
                   
                   Ville ville = (Ville) dao.getVille().get(secteur.getIdVille());
                   labelT.setText("Secteur "+secteur.getNom()+"("+ville.getNom()+") supprimé");
                   Connecter c = null; 
                   try {
                        c = new Connecter();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalPaneDeleteSecteur.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ModalPaneDeleteSecteur.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                   Requettes con = new Requettes(c);
                    try {
                        con.fDeleteSecteur(secteur);
                        c.getConnexion().close();
                        
                        Ville vil = (Ville) dao.getVille().get(secteur.getIdVille());
                        vil.setNbEq(vil.getNbPort()-secteur.getNbPort());
                        vil.setNbPortLibre(vil.getNbPortLibre()-secteur.getNbPortLibre());
                        vil.setNbPortOccupe(vil.getNbPortOccupe()-secteur.getNbPortOccupe());
                        vil.setNbPortDefect(vil.getNbPortDefect()-secteur.getNbPortDefect());
                        vil.setNbCarte(vil.getNbCarte()-secteur.getNbCarte());
                        vil.setNbEq(vil.getNbEq()-secteur.getNbEq());
                        
                        
                        dao.getSecteur().remove(secteur.getId());
                        ArrayList secteurs = (ArrayList) dao.getSecteurVille().get(secteur.getIdVille());
                        secteurs.remove(secteurs.indexOf(secteur.getId()));
                    } catch (SQLException ex) {
                        Logger.getLogger(ModalPaneDeleteSecteur.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                   
                   
                   //gridR.getChildren().remove(treeTableView);
                   dao.treetableviewAdminEq(dao, paneR, labelT, pageHBlabel, gridR,"treetableviewAE");
                   
                   stage.close();
                                       
                }
        });
        
        Button bannul=new Button();
        bannul.setText("Annuler");
        bannul.getStyleClass().add(dao.boutonColor);
        
        bannul.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
        });
        
       
        
        
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(bannul,bvalid);
        grid.add(hbBtn, 1, 5);

        grid.add(lbMessage,1,6);
        
        VBox VbBtn = new VBox(10);
        VbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        
        HBox Lb = new HBox(10);
        Lb.setAlignment(Pos.BOTTOM_RIGHT);
        Lb.getChildren().addAll(lbMessage,lbMessage2);
        
        VbBtn.getChildren().addAll(lajout,grid,Lb);
        
              
                
                ScrollPane sp = new ScrollPane();
        
                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(VbBtn);
                sp.setPadding(new Insets(10, 10, 10, 10));
                
                Scene scene = new Scene(sp, (dao.width*50)/100, (dao.height*36)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Supprimer un secteur");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
