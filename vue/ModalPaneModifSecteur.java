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
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Secteur;
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class ModalPaneModifSecteur {
    
    private ActionEvent event;
    private DAOequipement dao;
    private Secteur secteur;
    private Label label;
    
    public ModalPaneModifSecteur(DAOequipement fdao,ActionEvent event,Secteur secteur,Label label){
        this.event = event;
        this.dao = fdao;
        this.secteur = secteur;
        this.label = label;
    }
    
    void construire() throws ClassNotFoundException, SQLException{
    
        Stage stage = new Stage();
                
                
                
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));
                
                

               Label scenetitle = new Label("Secteur");
               scenetitle.setTextFill(Color.web(dao.colordeAdminEq()));
               scenetitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
               grid.add(scenetitle, 0, 0, 4, 1);

               Label villeName = new Label("Nom:");
               grid.add(villeName, 0, 1);

               TextField sectTextField = new TextField();
               sectTextField.setText(secteur.getNom());
               grid.add(sectTextField, 1, 1);
               
               Label info = new Label ("");
               grid.add(info, 1, 6);


               final Text actiontarget = new Text();
                grid.add(actiontarget, 1, 6);



                Button btn = new Button("Enregistrer");
                btn.getStyleClass().add(dao.colornodanger());
                Button btn2 = new Button("Annuler");
                btn2.getStyleClass().add(dao.boutonColor);
                
                btn.setOnAction(new EventHandler<ActionEvent>(){
                    boolean isCreate = false;
                    public void handle(ActionEvent t) {
                        
                        if(sectTextField.getText().isEmpty()){
                            info.setText("Champ vide");
                        }else{
                           
                           if(secteur.getNom().equals(sectTextField.getText().toUpperCase())) {
                               
                                
                                    isCreate = true;
                                
                           } 
                            
                           if(isCreate){
                               info.setText(sectTextField.getText()+" existe déja");
                               isCreate = false;
                           }else{
                               Connecter c=null;
                               try {
                                   c = new Connecter();
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalPaneModifSecteur.class.getName()).log(Level.SEVERE, null, ex);
                                   info.setText("Probleme connection");
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalPaneModifSecteur.class.getName()).log(Level.SEVERE, null, ex);
                                   info.setText("Probleme connection");
                               }
                               Requettes con = new Requettes(c);
                               
                               secteur.setNom(sectTextField.getText().toUpperCase());
                               
                               
                               try { 
                                   con.fUpdateSecteur(secteur);
                                   c.getConnexion().close();
                                   dao.getSecteur().replace(secteur.getId(), secteur);
                                   // ajout de l'objet ville à la liste villes=
                                   label.setText(secteur.getNom());
                                  
                                   
                                   info.setText(secteur.getNom()+" ajourné.");
                                   new Notifier("Gestab",secteur.getNom()+" ajourné.",2,dao);
                                   stage.close();
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalPaneModifSecteur.class.getName()).log(Level.SEVERE, null, ex);
                                   info.setText("Echec de mise à jour.");
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
                
                Scene scene = new Scene(sp, (dao.width*50)/100, (dao.height*36)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Modifier un secteur");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
