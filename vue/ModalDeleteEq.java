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
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class ModalDeleteEq {
    private final ActionEvent event;
    private final Equipement eq;
    private TreeTableView<Equipement> treeTableView;
    private DAOequipement dao;
    private HBox pageHBlabel;
    private VBox paneDesc;
    private GridPane gridR;
    private Label labelT;
    public ModalDeleteEq(DAOequipement dao,ActionEvent event,Equipement eq,HBox head,VBox desc,GridPane grid,Label label){
        this.event = event;
        this.eq = eq;
        this.treeTableView = dao.getTreeTE();
        this.dao = dao;
        this.pageHBlabel = head;
        this.paneDesc = desc;
        this.gridR = grid;
        this.labelT = label;
        
    }
    
    void construire(){
    
        Stage stage = new Stage();
        ///Creation des Variales
        Label lajout = new Label("Etes vous sur de vouloir supprimer cet Equipement:");
        lajout.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        lajout.setTextFill(Color.web(dao.couleur));
        lajout.setAlignment(Pos.BOTTOM_LEFT);
        // --- GridPane container
        GridPane grid = new GridPane();
        grid.getStyleClass().add("gridCarte");
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("Equipement: "), 0, 0);
        
        Label attach = new Label("ᚂ Attachement: ");
        attach.setTextFill(Color.web(dao.couleur));
        attach.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(attach, 0, 3);
        
        grid.add(new Label("Ville: "), 0, 4);
        grid.add(new Label("Secteur: "), 0, 5);
        
        grid.add(new Label("Nom: "), 3, 1);
        grid.add(new Label("Route: "), 3, 2);
        
        Ville ville = (Ville) dao.getVille().get(eq.getIdVille());
        final Label label = new Label(ville.getNom());
        grid.add(label,1, 4);
        
        Secteur sect = (Secteur) dao.getSecteur().get(eq.getIdSecteur());
        final Label label1 = new Label(sect.getNom());
        grid.add(label1,1, 5);
        
        
        
        final Label label6 = new Label(eq.getNom());
        label6.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(label6,4, 1);
        
        final Label label7 = new Label(eq.getRoute());
        label7.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(label7,4, 2);
        
        
        
        
        
        Label lbMessage = new Label("");
        lbMessage.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        Label lbMessage2 = new Label("");
        
        Button bvalid = new Button();
        bvalid.setText("Oui");
        bvalid.getStyleClass().add(dao.colordanger());
        bvalid.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                   
                    Connecter c=null;
                    try {
                        c = new Connecter();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalPaneDeleteCarte.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ModalPaneDeleteCarte.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Requettes con = new Requettes(c);
                    
                    try { 
                        con.fDeleteEquipement(eq);
                        c.getConnexion().close();
                        pageHBlabel.getChildren().clear();
                        paneDesc.getChildren().clear();
                        
                        
                        dao.getEquipement().remove(eq.getIdEQ());
                        ArrayList sects = (ArrayList) dao.getEqSecteur().get(eq.getIdSecteur());
                        sects.remove(sects.indexOf(eq.getIdEQ()));
                        
                        dao.getCarteEq().remove(eq.getIdEQ());
                        
                         
                         //mise à jour du nombre de port (libre) et nombre de carte dans l'objet secteur
                         Secteur set = (Secteur) dao.getSecteur().get(eq.getIdSecteur());
                         set.setNbPort(set.getNbPort()-eq.getTmpNbPort());
                         set.setNbPortLibre(set.getNbPortLibre()-eq.getTmpNbPortLibre());
                         set.setNbCarte(set.getNbCarte()-eq.getTmpNbCarte());
                         set.setNbEq(set.getNbEq()-1);
                         dao.getSecteur().replace(set.getId(), set);
                         
                         //mise à jour du nombre de port (libre) et nombre de carte dans l'objet ville
                         Ville vil = (Ville) dao.getVille().get(set.getIdVille());
                         vil.setNbPort(vil.getNbPort()-eq.getTmpNbPort());
                         vil.setNbPortLibre(vil.getNbPortLibre()-eq.getTmpNbPortLibre());
                         vil.setNbCarte(vil.getNbCarte()-eq.getTmpNbCarte());
                         vil.setNbEq(vil.getNbEq()-1);
                         dao.getVille().replace(vil.getId(), vil);
                         
                         dao.treetableviewAdminEq(dao, paneDesc, labelT, pageHBlabel, gridR,"treetableviewAE");


                        stage.close();
                        new Notifier("Gestab Notification","Equipement "+eq.getNom()+"\n supprimé",2);
                   } catch (SQLException ex) {
                        Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                        lbMessage.setText("Echec suppression.");
                    }
                   
                   /*hbox.getChildren().remove(0);
                   
                   Ville ville = (Ville) dao.getVille().get(user.getVille());
                   labelT.setText("Client "+user.getNom()+" "+user.getPrenom()+"("+ville.getNom()+") supprimé");
                   
                   dao.getUser().remove(user.getId());
                   ArrayList users = (ArrayList) dao.getUserVille().get(user.getVille());
                   users.remove(users.indexOf(user.getId()));
                   
                   //gridR.getChildren().remove(treeTableView);
                   dao.treeTableViewUser(dao, paneR, labelT, gridR, "treetableviewU");*/
                   
                  
                   
                   stage.close();
                                       
                }
        });
        
        Button bannul=new Button();
        bannul.getStyleClass().add(dao.boutonColor);
        bannul.setText("Annuler");
        
        bannul.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
        });
        
       
        
        
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(bannul,bvalid);
        grid.add(hbBtn, 1, 9);

        grid.add(lbMessage,1,9);
        
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
                
                Scene scene = new Scene(sp, (dao.width*50)/100, (dao.height*40)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Supprimer un equipement");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
