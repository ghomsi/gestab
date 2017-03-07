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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Equipement;
import modele.Ville;
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class ModalAddVille {
    private ActionEvent event;
    private DAOequipement dao;
    private final Label labelT;
    private TreeItem<Equipement> rootR;
    private final GridPane gridR;
    private TreeTableView<Equipement> treeTableView;
    private VBox paneR;
    private HBox pageHBlabel;
    
    public ModalAddVille(ActionEvent event,DAOequipement fdao,Label label,HBox HBlabel,GridPane grid,VBox pane){
        this.event = event;
        this.dao = fdao;
        this.labelT = label;
        this.pageHBlabel = HBlabel;
        this.gridR = grid;
        this.treeTableView = dao.getTreeTE();
        this.paneR = pane;
    }
    
    void construire() throws ClassNotFoundException, SQLException{
    
        Stage stage = new Stage();
                
                
                
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));
                
                

               Text scenetitle = new Text("Ville");
               scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
               grid.add(scenetitle, 0, 0, 4, 1);

               Label villeName = new Label("Nom:");
               grid.add(villeName, 0, 1);

               TextField villeTextField = new TextField();
               grid.add(villeTextField, 1, 1);
               
               Label info = new Label ("");
               grid.add(info, 1, 6);


               final Text actiontarget = new Text();
                grid.add(actiontarget, 1, 6);



                Button btn = new Button("Ajouter");
                btn.getStyleClass().add(dao.colornodanger());
                Button btn2 = new Button("Annuler");
                btn2.getStyleClass().add(dao.boutonColor);
                
                btn.setOnAction(new EventHandler<ActionEvent>(){
                    boolean isCreate = false;
                    public void handle(ActionEvent t) {
                        
                        if(villeTextField.getText().isEmpty()){
                            info.setText("Champ "+villeName.getText()+" vide");
                        }else{
                           Hashtable villes = dao.getVille();
                           Enumeration e = villes.elements();
                           while(e.hasMoreElements()) {
                               Ville elt = (Ville) e.nextElement();
                                if(elt.getNom().equals(villeTextField.getText().toUpperCase())){
                                    isCreate = true;
                                }
                           } 
                            
                           if(isCreate){
                               info.setText(villeTextField.getText()+" existe déja");
                               isCreate = false;
                           }else{
                               Connecter c=null;
                               try {
                                   c = new Connecter();
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                   new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                               }
                               Requettes con = new Requettes(c);
                               
                               Ville ville = new Ville(villeTextField.getText().toUpperCase(Locale.ITALY));
                               try { 
                                   int id = con.fInsertVille(ville);
                                   c.getConnexion().close();
                                   ville.setId(id);
                                   // ajout de l'objet ville à la liste villes
                                   dao.getVille().put(ville.getId(), ville);
                                   
                                   dao.treetableviewAdminEq(dao, paneR, labelT, pageHBlabel, gridR,"treetableviewAE");
                                   

                                   stage.close();
                                   
                                   info.setText(villeTextField.getText().toUpperCase(Locale.ITALY)+" ajouté.");
                                   new Notifier("Gestab","Ville:"+ville.getNom()+" ajoutée",2,dao);
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                   info.setText("Echec d'insertion.");
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                   info.setText("Echec d'insertion.");
                               }
                               
                               
                           } 
                        }
                    } 
                });
                
                
                btn2.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        stage.close();
                    }
                });
                
                
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().addAll(btn2,btn);
                grid.add(hbBtn, 1, 4);



                ScrollPane sp = new ScrollPane();
        
                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(grid);
                sp.setPadding(new Insets(10, 10, 10, 10));
                
                Scene scene = new Scene(sp, (dao.width*35)/100, (dao.height*29)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Ajouter une ville");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
