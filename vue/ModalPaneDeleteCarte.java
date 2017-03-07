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
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Carte;
import modele.Equipement;
import modele.Secteur;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class ModalPaneDeleteCarte {
    
    private final ActionEvent event;
    private final ObservableList<Carte> options;
    private final Carte carte;
    private final Label label;
    private final HBox hbox;
    private final VBox Vbox;
    private DAOequipement dao;
    
    public ModalPaneDeleteCarte(DAOequipement dao,ActionEvent event,Carte carte,HBox hbox,VBox Vbox,ObservableList<Carte> options){
        this.event = event;
        this.carte = carte;
        this.label = new Label();
        this.hbox = hbox;
        this.Vbox = Vbox;
        this.options = options;
        this.dao = dao;
    }
    
    void construire(){
    
        Stage stage = new Stage();
        ///Creation des Variales
        Label lajout = new Label("Etes vous sur de vouloir supprimer la carte:");
        lajout.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        lajout.setTextFill(Color.BLACK);
        lajout.setAlignment(Pos.CENTER);
        GridPane grid = new GridPane();
        grid.getStyleClass().add("gridCarte");
        grid.setVgap(4);
        grid.setPadding(new Insets(5, 5, 5, 5));

        grid.add(new Label("Nom: "), 0, 0);
        Label nomV = new Label("carte "+carte.getNumCarte());
        grid.add(nomV, 1, 0);

        grid.add(new Label("Description: "), 0, 1);
        Label descV = new Label(carte.getDesc());
        grid.add(descV, 1, 1);
        
        Label attach = new Label("ᚄ Attachement: ");
        attach.setTextFill(Color.web(dao.colordeAdminEq()));
        attach.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(attach, 0, 3);

        grid.add(new Label("Nombre de Port: "), 0, 4);
        Label portV = new Label(""+carte.getNbPort());


        grid.add(portV, 1, 4);

        grid.add(new Label("Ports libres: "), 0, 5);
        Label portLV = new Label(""+carte.getNbPortLibre());
        portLV.setTextFill(Color.web(dao.colorlibre()));
        grid.add(portLV, 1, 5);

        grid.add(new Label("Ports occupés: "), 0, 6);
        Label portLO = new Label(""+carte.getNbPortOccupe());
        portLO.setTextFill(Color.web(dao.colloroccupe()));
        grid.add(portLO, 1, 6);

        grid.add(new Label("Ports desactivés/défecteux: "), 0, 7);
        Label portLD = new Label(""+carte.getNbPortDesactive());
        portLD.setTextFill(Color.web(dao.colordefectueux()));
        grid.add(portLD, 1, 7);
        
        
        Label lbMessage = new Label("");
        lbMessage.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        Label lbMessage2 = new Label("");
        
        Button bvalid = new Button();
        bvalid.getStyleClass().add("buttonAdminEq");
        bvalid.setText("Oui");
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
                        con.fDeleteCarte(carte);
                        c.getConnexion().close();
                        
                        Vbox.getChildren().remove(hbox);
                        options.remove(carte);
                        dao.getCarte().remove(carte.getId());
                        dao.getPortCarte().remove(carte.getId());
                        ArrayList cartes= (ArrayList) dao.getCarteEq().get(carte.getIdEQ());
                        cartes.remove(cartes.indexOf(carte.getId()));

                        //mise à jour du nombre de port (libre) dans l'objet equipement
                        Equipement eq = (Equipement) dao.getEquipement().get(carte.getIdEQ());
                         eq.setTmpNbPort(eq.getTmpNbPort()-carte.getNbPort());
                         eq.setTmpNbPortLibre(eq.getTmpNbPortLibre()-carte.getNbPortLibre());
                         eq.setTmpNbCarte(eq.getTmpNbCarte()-1);
                         dao.getEquipement().replace(carte.getIdEQ(), eq);
                         
                         //mise à jour du nombre de port (libre) et nombre de carte dans l'objet secteur
                         Secteur set = (Secteur) dao.getSecteur().get(eq.getIdSecteur());
                         set.setNbPort(set.getNbPort()-carte.getNbPort());
                         set.setNbPortLibre(set.getNbPortLibre()-carte.getNbPortLibre());
                         set.setNbCarte(set.getNbCarte()-1);
                         dao.getSecteur().replace(set.getId(), set);
                         
                         //mise à jour du nombre de port (libre) et nombre de carte dans l'objet ville
                         Ville vil = (Ville) dao.getVille().get(set.getIdVille());
                         vil.setNbPort(vil.getNbPort()-carte.getNbPort());
                         vil.setNbPortLibre(vil.getNbPortLibre()-carte.getNbPortLibre());
                         vil.setNbCarte(vil.getNbCarte()-1);
                         dao.getVille().replace(vil.getId(), vil);


                        stage.close();
                   } catch (SQLException ex) {
                        Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                        lbMessage.setText("Echec suppression.");
                    }
                                       
                }
        });
        
        Button bannul=new Button();
        bannul.getStyleClass().add("buttonAdminEq");
        bannul.setText("Annuler");
        
        bannul.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
        });
        
       
        
        
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(bannul,bvalid);
        grid.add(hbBtn, 1, 8);

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
                
                Scene scene = new Scene(sp, (dao.width*40)/100, (dao.height*55)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Supprimer une carte");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
    
    
    
    
}
