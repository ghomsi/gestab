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
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
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
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class ModalAddCarte {
 
    private final ActionEvent event;
    private final ObservableList<Carte> options;
    private final VBox Vbox;
    private final Label pageLabel2;
    private DAOequipement dao;
    
    public ModalAddCarte(DAOequipement dao,ActionEvent event,ObservableList<Carte> options,VBox Vbox,Label label){
        this.event = event;
        this.options = options;
        this.Vbox = Vbox;
        this.pageLabel2 = label;
        this.dao = dao;
    }
    
    void construire(Equipement eq){
    
        Stage stage = new Stage();
        ///Creation des Variales
        Label lajout = new Label("AJOUTEZ UNE CARTE");
        lajout.setTextFill(Color.web(dao.colordeAdminEq()));
        lajout.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 20));
        lajout.setAlignment(Pos.CENTER);
        
        Label lajoutcarte=new Label("Numero Carte :");
        Label lajoutport =new Label("Nombre de Port :");
        TextField tajoutcarte =new TextField();
        TextField tajoutport = new TextField();
        
        Label lbMessage = new Label("");
        lbMessage.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        Label lbMessage2 = new Label("");
        
        Button bvalid = new Button();
        bvalid.setText("Ajouter");
        bvalid.getStyleClass().add(dao.colornodanger());
        bvalid.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    String newCarte = "carte "+tajoutcarte.getText();
                    System.out.println(options.indexOf(newCarte));
                    if(options.indexOf(newCarte)>=0){
                        lbMessage.setText(newCarte+" ");
                        lbMessage2.setText(" existe déja!");
                    }else if(tajoutcarte.getText().isEmpty()){
                        lbMessage.setText("le numero de carte ");
                        lbMessage2.setText(" ne peut être vide!");
                    }else if(tajoutport.getText().isEmpty()){
                        lbMessage.setText("le Nombre de port ");
                        lbMessage2.setText(" ne peut être vide!");
                    }else{
                        
                        Carte carte = new Carte(new Integer(tajoutcarte.getText()));
                        carte.setIdEQ(eq.getIdEQ());
                        carte.setIdVille(eq.getIdVille());
                        carte.setNbPort(new Integer(tajoutport.getText()));
                        carte.setNbPortLibre(new Integer(tajoutport.getText()));
                        options.add(carte);
                        
                        Connecter c=null;
                        try {
                            c = new Connecter();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ModalAddCarte.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(ModalAddCarte.class.getName()).log(Level.SEVERE, null, ex);
                            new Notifier("Gestab exception","Le réseau n'est pas accessible:"+ex,3,dao);
                        }
                        Requettes con = new Requettes(c);
                               
                               
                               try {
                                    
                                   int id = con.fInsertCard(carte);
                                   ArrayList ports =  dao.getAllPortBDByCarteId(id,c);
                                   dao.setPort(dao.getAllPortBD(c));
                                   c.getConnexion().close();
                                   
                                   
                                   carte.setId(id);
                                   // ajout de l'objet ville à la liste villes
                                   dao.getCarte().put(id, carte);
                                   ArrayList cartes= (ArrayList) dao.getCarteEq().get(eq.getIdEQ());
                                   if(cartes==null){
                                       cartes = new ArrayList();
                                       cartes.add(id);
                                       dao.getCarteEq().put(eq.getIdEQ(),cartes);
                                   }else{
                                       cartes.add(id);
                                   }
                                   dao.getPortCarte().put(carte.getId(), ports);
                                   
                                   
                                   //mise à jour du nombre de port (libre) dans l'objet equipement
                                   eq.setTmpNbPort(eq.getTmpNbPort()+carte.getNbPort());
                                   eq.setTmpNbPortLibre(eq.getTmpNbPortLibre()+carte.getNbPortLibre());
                                   eq.setTmpNbCarte(eq.getTmpNbCarte()+1);
                                   dao.getEquipement().replace(eq.getIdEQ(), eq);
                                   //mise à jour du nombre de port (libre) et nombre de carte dans l'objet secteur
                                   Secteur set = (Secteur) dao.getSecteur().get(eq.getIdSecteur());
                                   set.setNbPort(set.getNbPort()+carte.getNbPort());
                                   set.setNbPortLibre(set.getNbPortLibre()+carte.getNbPortLibre());
                                   set.setNbCarte(set.getNbCarte()+1);
                                   dao.getSecteur().replace(eq.getIdSecteur(), set);
                                   
                                   //mise à jour du nombre de port (libre) et nombre de carte dans l'objet ville
                                   Ville vil = (Ville) dao.getVille().get(set.getIdVille());
                                   vil.setNbPort(vil.getNbPort()+carte.getNbPort());
                                   vil.setNbPortLibre(vil.getNbPortLibre()+carte.getNbPortLibre());
                                   vil.setNbCarte(vil.getNbCarte()+1);
                                   dao.getVille().replace(set.getIdVille(), vil);
                                   
                                   
                                   ajouterCarte(Vbox,carte,pageLabel2,eq);
                                   lbMessage.setText("carte "+carte.getNumCarte()+" ajouté.");
                                   new Notifier("Gestab","Carte:"+carte.getNumCarte()+" avec "+carte.getNbPort()+" port(s) ajouté",2,dao);
                                   stage.close();
                                   
                                   
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                   lbMessage.setText("Le réseau n'est pas accessible.");
                                   new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                   lbMessage.setText("Echec d'insertion.");
                               }
                        
                        
                        
                        
                    }
                    
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
        
        GridPane root = new GridPane();
        HBox rajout= new HBox();
       
        root.setPadding(new Insets(10,10,10,10));
        root.setGridLinesVisible(false);
        root.setHgap(10);
        root.setVgap(5);
        root.setAlignment(Pos.CENTER);
        root.add(lajout,0,0);
        
        root.add(lajoutcarte,0,3);
        root.add(tajoutcarte,1,3);
        root.add(lajoutport,0,5);
        root.add(tajoutport,1,5);
        
        
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(bannul,bvalid);
        root.add(hbBtn, 1, 7);

        root.add(lbMessage,1,8);
        //FlowPane pane = new FlowPane();
        
        VBox VbBtn = new VBox(10);
        VbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        
        HBox Lb = new HBox(10);
        Lb.setAlignment(Pos.BOTTOM_RIGHT);
        Lb.getChildren().addAll(lbMessage,lbMessage2);
        
        VbBtn.getChildren().addAll(root,Lb);
        
        //pane.getChildren().addAll(root,lbMessage);

              
                
                ScrollPane sp = new ScrollPane();
        
                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(VbBtn);
                sp.setPadding(new Insets(10, 10, 10, 10));
                
                Scene scene = new Scene(sp, (dao.width*50)/100, (dao.height*45)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Ajouter une Carte");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
    
    
    void ajouterCarte(VBox Vbox,Carte carte,Label titleLabel,Equipement eq){
        
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(20, 0, 0, 20));
        HBox hb = new HBox(10);
        final Label label = new Label("N/A");
        TitledPane gridTitlePane = dao.carteTitleDPane(dao,carte,hb,hbox,Vbox,label,options);
            
  
            hbox.getChildren().addAll(gridTitlePane,  dao.portScrollPaneAdmin(dao,carte,hb,label));
            
            Vbox.getChildren().addAll(hbox);
            int a = new Integer(titleLabel.getText())+1;
            titleLabel.setText(""+a);
    }
}
