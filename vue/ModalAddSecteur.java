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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import modele.Equipement;
import modele.Secteur;
import modele.Ville;
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class ModalAddSecteur {
    
    private ActionEvent event;
    Scene scene,scene2;
    private Ville ville;
    private DAOequipement dao;
     private final Label labelT;
    private TreeItem<Equipement> rootR;
    private final GridPane gridR;
    private TreeTableView<Equipement> treeTableView;
    private VBox paneR;
    private HBox pageHBlabel;
    
    public ModalAddSecteur(ActionEvent event,DAOequipement fdao,Label label,HBox HBlabel,GridPane grid,VBox pane){
        this.event = event;
        this.dao =fdao;
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
                
                //---- combo Box villes
                Hashtable villes = dao.getVille();

                ObservableList<Ville> options = FXCollections.observableArrayList();
                
                Enumeration e = villes.elements();
                while(e.hasMoreElements()) {
                    Ville elt = (Ville) e.nextElement();
                    options.add(elt);
                }
        
                final ComboBox comboBox = new ComboBox(options);
                Label villeL = new Label();
                HBox villeHB = new HBox(5);
                villeHB.getChildren().addAll(comboBox,villeL);
                comboBox.getStyleClass().add(dao.comboColor);
                comboBox.setPromptText("Ville");      
                
                comboBox.setCellFactory(new Callback<ListView<Ville>,ListCell<Ville>>(){
 
                    @Override
                    public ListCell<Ville> call(ListView<Ville> p) {

                        final ListCell<Ville> cell = new ListCell<Ville>(){

                            @Override
                            protected void updateItem(Ville t, boolean bln) {
                                super.updateItem(t, bln);

                                if(t != null){
                                    setText(t.getNom());
                                }else{
                                    setText(null);
                                }
                            }

                        };

                        return cell;
                    }
                });
                
                
               Label cityName = new Label("Ville:");
               grid.add(cityName, 0, 1);

               
               grid.add(villeHB, 1, 1);

               Label sectL = new Label("Nom:");
               grid.add(sectL, 0, 2);

               TextField sectName = new TextField();
               sectName.setDisable(true);
               grid.add(sectName, 1, 2);
               
               Button btn = new Button("Ajouter");
               btn.getStyleClass().add(dao.colornodanger());
               btn.setDisable(true);
               Button btn2 = new Button("Annuler");
               btn2.getStyleClass().add(dao.boutonColor);
                
                
                comboBox.valueProperty().addListener(new ChangeListener<Ville>() {
                    @Override public void changed(ObservableValue ov, Ville t, Ville t1) {
                        
                        ville = t1;
                        villeL.setText(t1.getNom());
                        sectName.setDisable(false);
                        btn.setDisable(false);
                    }    
                }); 

               Label scenetitle = new Label("Secteur");
               scenetitle.setTextFill(Color.web(dao.colordeAdminEq()));
               scenetitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
               grid.add(scenetitle, 0, 0, 4, 1);

               

               final Text actiontarget = new Text("");
                grid.add(actiontarget, 1, 6);
                
                
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    boolean isCreate = false;
                    public void handle(ActionEvent t) {
                        
                        if(sectName.getText().isEmpty()){
                            actiontarget.setText("Champ "+sectL.getText()+" vide");
                        }else{
                           
                           ArrayList fsect = (ArrayList) dao.getSecteurVille().get(ville.getId());
                           if(fsect==null){
                               fsect = new ArrayList();
                           }
                           Iterator<Integer> e1 = fsect.iterator();
                           while(e1.hasNext()) {
                               int id_secteur = e1.next();
                               Secteur elt = (Secteur) dao.getSecteur().get(id_secteur);
                                if(elt.getNom().equals(sectName.getText().toUpperCase())){
                                    isCreate = true;
                                }
                            } 
                            
                           if(isCreate){
                               actiontarget.setText(sectName.getText()+" existe déja");
                               isCreate = false;
                           }else{
                               Connecter c=null;
                               try {
                                   c = new Connecter();
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalAddSecteur.class.getName()).log(Level.SEVERE, null, ex);
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddSecteur.class.getName()).log(Level.SEVERE, null, ex);
                                   new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                               }
                               Requettes con = new Requettes(c);
                               
                               Secteur secteur = new Secteur(sectName.getText().toUpperCase(Locale.ITALY));
                               secteur.setIdVille(ville.getId());
                               try{ 
                                   int id = con.fInsertSecteur(secteur,ville.getId());
                                   c.getConnexion().close();
                                   // ajout de l'objet secteur a la liste d'objet
                                   secteur.setId(id);
                                   dao.getSecteur().put(id, secteur);
                                   
                                   
                                   ArrayList secteurs = (ArrayList) dao.getSecteurVille().get(ville.getId());
                                   
                                   if(secteurs==null){
                                       secteurs = new ArrayList();
                                       secteurs.add(id);
                                       dao.getSecteurVille().put(ville.getId(), secteurs);
                                   }else{    
                                        secteurs.add(id);
                                   }
                                   
                                   actiontarget.setText(sectName.getText().toUpperCase(Locale.ITALY)+" ajouté.");
                                   
                                   dao.treetableviewAdminEq(dao, paneR, labelT, pageHBlabel, gridR,"treetableviewAE");
                                   
                                   new Notifier("Gestab","Sevteur:"+secteur.getNom()+" ajouté",2,dao);
                                   stage.close();

                                     
                               
                                        
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                   actiontarget.setText("Echec d'insertion.");
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                   actiontarget.setText("Echec d'insertion.");
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



                FlowPane modalPane=new FlowPane();
                modalPane.getChildren().addAll(grid);
                
                scene = new Scene(modalPane, (dao.width*30)/100, (dao.height*40)/100);
                scene.getStylesheets().add("css/style.css");
               
                
                
                stage.setScene(scene);
                stage.setTitle("Ajouter secteur");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
