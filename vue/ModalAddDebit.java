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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.util.converter.NumberStringConverter;
import modele.Debit;

/**
 *
 * @author ghomsi
 */
public class ModalAddDebit {
    private final ActionEvent event;
    private final DAOequipement dao;
    private final ComboBox debitCB;
    
    public ModalAddDebit(ActionEvent event,DAOequipement fdao,ComboBox debitCB){
        this.event = event;
        this.dao = fdao;
        this.debitCB = debitCB;
    }
    
    void construire() throws ClassNotFoundException, SQLException{
    
        Stage stage = new Stage();
                
                
                
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));
                
                

               Label scenetitle = new Label("Debit");
               scenetitle.setTextFill(Color.web(dao.couleur));
               scenetitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
               grid.add(scenetitle, 0, 0, 4, 1);

               Label debitName = new Label("Valeur:");
               grid.add(debitName, 0, 1);

               TextField debitTextField = new TextField();
               grid.add(debitTextField, 1, 1);
               
               Label profilName = new Label("profil:");
               grid.add(profilName, 0, 2);

               TextField numberField = new TextField();
               // force the field to be numeric only
                numberField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*")) {
                            numberField.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    }
                });
               grid.add(numberField, 1, 2);
               
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
                        
                        if(debitTextField.getText().isEmpty()){
                            info.setText(debitName+" Champ vide");
                        }else if(numberField.getText().isEmpty()){
                            info.setText(profilName+" Champ vide");
                        }else{
                           Hashtable debits = dao.getDebit();
                           Enumeration e = debits.elements();
                           while(e.hasMoreElements()){
                               Debit elt = (Debit) e.nextElement();
                                if(elt.getNom().equals(debitTextField.getText().toUpperCase())){
                                    isCreate = true;
                                }
                           } 
                            
                           if(isCreate){
                               info.setText(debitTextField.getText()+" existe déja");
                               isCreate = false;
                           }else{
                               Connecter c=null;
                               try {
                                   c = new Connecter();
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalAddDebit.class.getName()).log(Level.SEVERE, null, ex);
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddDebit.class.getName()).log(Level.SEVERE, null, ex);
                               }
                               Requettes con = new Requettes(c);
                               
                               Debit debit = new Debit();
                               debit.setNom(debitTextField.getText().toUpperCase(Locale.ITALY));
                               debit.setProfil(new Integer(numberField.getText()));
                               try { 
                                   int id = con.fInsertDebit(debit);
                                   debit.setId(id);
                                   c.getConnexion().close();
                                   // ajout de l'objet debit à la liste debits
                                   dao.getDebit().put(id, debit);
                                   debitCB.getItems().add(debit);
                                   
                                   info.setText(debitTextField.getText().toUpperCase(Locale.ITALY)+" ajouté.");
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalAddDebit.class.getName()).log(Level.SEVERE, null, ex);
                                   info.setText("Echec d'insertion.");
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddDebit.class.getName()).log(Level.SEVERE, null, ex);
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
                stage.setTitle("Ajouter un Debit");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
