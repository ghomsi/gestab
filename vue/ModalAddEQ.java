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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
import javafx.util.Callback;
import modele.Equipement;
import modele.Secteur;
import modele.Ville;
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class ModalAddEQ {
    
    private final ActionEvent event;
    private Scene scene,scene2;
    private Ville ville;
    private Secteur secteurP;
    private final DAOequipement dao;
    private final TreeTableView<Equipement> treeTableView;
    private final GridPane gridR;
    private final Label labelT;
    private final HBox pageHBlabel;
    private TreeItem<Equipement> rootR;
    private final VBox paneDesc;
    
    public ModalAddEQ(ActionEvent event,DAOequipement dao,GridPane grid,Label lblscene,HBox HBlabel,VBox pane){
        this.event = event;
        this.dao = dao;
        this.treeTableView = dao.getTreeTE();
        this.gridR = grid;
        this.labelT = lblscene;
        this.paneDesc = pane;
        
        this.pageHBlabel = HBlabel;
    }
    
    void construire() throws ClassNotFoundException, SQLException{
    
        Stage stage = new Stage();
                
                
                
        GridPane gridpane = new GridPane();
        
        gridpane.setAlignment(Pos.CENTER);

        gridpane.setPadding(new Insets (15));
        gridpane.setVgap(10);
        gridpane.setHgap(10);

        Label scenetitle = new Label("Equipement");
        scenetitle.setTextFill(Color.web(dao.couleur));
        scenetitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        gridpane.add(scenetitle, 0, 0, 4, 1);

         Label villeEQLB = new Label ("VILLE:");
         
         gridpane.add(villeEQLB, 0, 1);
         
         //---- combo Box villes
        Hashtable villes = dao.getVille();

        ObservableList<Ville> options = FXCollections.observableArrayList();
        ObservableList<String> opt = FXCollections.observableArrayList();
        opt.addAll("SSH","Telnet");
        Enumeration e = villes.elements();
        while(e.hasMoreElements()) {
            Ville elt = (Ville) e.nextElement();
            options.add(elt);
        }
        
         final ComboBox villeEQMCB = new ComboBox(options);
         Label villeL = new Label();
         HBox villeEQHB = new HBox(5);
         villeEQHB.getChildren().addAll(villeEQMCB,villeL);
         villeEQMCB.getStyleClass().add(dao.comboColor);
         final ComboBox modeConnectCB = new ComboBox(opt);
         modeConnectCB.getStyleClass().add(dao.comboColor);
         modeConnectCB.getSelectionModel().selectFirst();
         ComboBox secteurEQMCB = new ComboBox<>();
         Label secteurL = new Label();
         HBox secteurHB = new HBox(5);
         secteurHB.getChildren().addAll(secteurEQMCB,secteurL);
         secteurEQMCB.getStyleClass().add(dao.comboColor);
         
         
        //villeEQMCB.getSelectionModel().selectFirst(); //select the first element
         
        villeEQMCB.setCellFactory(new Callback<ListView<Ville>,ListCell<Ville>>(){
 
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
         
        
        ObservableList<Secteur> optionsSect = FXCollections.observableArrayList();
        
        
        
        villeEQMCB.valueProperty().addListener(new ChangeListener<Ville>() {
            @Override public void changed(ObservableValue ov, Ville t, Ville t1) {
                
              secteurEQMCB.getItems().clear();
              secteurL.setText("");
              ville = t1;
              villeL.setText(t1.getNom());
              
              if(t1!=null){
                    
                    ArrayList sect = (ArrayList) dao.getSecteurVille().get(t1.getId());
                    Iterator<Integer> e1 = sect.iterator();
                    while(e1.hasNext()){
                        int id = e1.next();
                         Secteur elt = (Secteur) dao.getSecteur().get(id);
                           secteurEQMCB.getItems().add(elt);
                    }

                    secteurEQMCB.setCellFactory(new Callback<ListView<Secteur>,ListCell<Secteur>>(){
                      @Override
                      public ListCell<Secteur> call(ListView<Secteur> p) {

                              final ListCell<Secteur> cell = new ListCell<Secteur>(){

                                  @Override
                                  protected void updateItem(Secteur t, boolean bln) {
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

                     secteurEQMCB.setPromptText("choix");

               }
            }    
        });
        
        
         
        gridpane.add(villeEQHB,1 ,1 );
        villeEQMCB.setPromptText("Choix");

        Label secteur = new Label ("SECTEUR:");
        gridpane.add(secteur, 0, 2);
        
        secteurEQMCB.getItems().addAll(optionsSect);
        gridpane.add(secteurHB, 1, 2);
        
        TextField nomEQMTf = new TextField ();
        nomEQMTf.setDisable(true);
        gridpane.add(nomEQMTf, 1, 3);
        
        TextField routeEQMTf = new TextField ();
        routeEQMTf.setDisable(true);
        gridpane.add(routeEQMTf, 1, 4);
        
        Label modeConnect = new Label ("Mode Connection:");
        gridpane.add(modeConnect, 0, 5);
        gridpane.add(modeConnectCB, 1, 5);
        
        Button validerbtn = new Button("Ajouter") ;
        validerbtn.getStyleClass().add(dao.colornodanger());
        validerbtn.setDisable(true);
        
        secteurEQMCB.valueProperty().addListener(new ChangeListener<Secteur>() {
            @Override public void changed(ObservableValue ov, Secteur t, Secteur t1) {
                
                secteurP= t1;
                if(t1!=null){
                    secteurL.setText(t1.getNom());
                    nomEQMTf.setDisable(false);
                    routeEQMTf.setDisable(false);
                    validerbtn.setDisable(false);
                    nomEQMTf.setPromptText("nom equipement");
                    routeEQMTf.setPromptText("route equipement");
                }
            }    
        });
        
        
        secteurEQMCB.setPromptText("...");

        Label nomEQMLB = new Label ("Nom:");
        gridpane.add(nomEQMLB, 0, 3);
        
        Label routeEQMLB = new Label ("Route/ip:");
        gridpane.add(routeEQMLB, 0, 4);
        
        Label actiontarget = new Label ("");
        gridpane.add(actiontarget, 1, 8);
        

        Button annulerbtn = new Button ("Annuler");
        annulerbtn.getStyleClass().add(dao.boutonColor);
        
        annulerbtn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
        });
        
        
        
        validerbtn.setOnAction(new EventHandler<ActionEvent>() {
            boolean isCreate = false;
                public void handle(ActionEvent t) {
                    
                 if(nomEQMTf.getText().isEmpty()){
                     actiontarget.setText("Champ "+nomEQMLB.getText()+" vide");
                 }else if(routeEQMTf.getText().isEmpty()){
                     actiontarget.setText("Champ "+routeEQMLB.getText()+" vide");
                 }else{
                    ArrayList equipements = (ArrayList) dao.getEqSecteur().get(secteurP.getId());
                    if(equipements!=null){
                    Iterator<Integer> e3 = equipements.iterator();
                    while(e3.hasNext()){
                        int id = e3.next();
                        Equipement elt = (Equipement) dao.getEquipement().get(id);
                        if(elt.getNom().equals(nomEQMTf.getText().toUpperCase())){
                            isCreate = true;
                        }
                    }
                    }
                    if(isCreate){
                        actiontarget.setText(nomEQMTf.getText().toUpperCase(Locale.ITALY)+" existe déja");
                        isCreate = false;
                    }else{
                        Connecter c=null;
                        try {
                            c = new Connecter();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ModalAddEQ.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(ModalAddEQ.class.getName()).log(Level.SEVERE, null, ex);
                            new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                        }
                        Requettes con = new Requettes(c);

                        Equipement neweq = new Equipement(nomEQMTf.getText().toUpperCase(Locale.ITALY));
                        //utile pour la différentiation lors de l'affichage dans le treeTableView 
                        neweq.setVille("N/A");
                        neweq.setSecteur("N/A");
                        neweq.setIdVille(ville.getId());
                        neweq.setIdSecteur(secteurP.getId());
                        neweq.setRoute(routeEQMTf.getText());
                        neweq.setModeConnect(modeConnectCB.getValue().toString());
                        try { 
                            int id =con.fInsertEquipment(neweq);
                          
                            // ajout de l'objet secteur a la liste d'objet
                            neweq.setIdEQ(id);
                            c.getConnexion().close();
                            dao.getEquipement().put(id, neweq);
                            secteurP.setNbEq(secteurP.getNbEq()+1);
                            dao.getSecteur().replace(secteurP.getId(), secteurP);
                            
                            //mise à jour du nombre d'equipement dans l'objet ville
                            Ville vil = (Ville) dao.getVille().get(secteurP.getIdVille());
                            vil.setNbEq(vil.getNbEq()+1);
                            dao.getVille().replace(vil.getId(), vil);
                            
                            
                            //ArrayList equipements = (ArrayList) dao.getEqSecteur().get(secteurP.getId());
                            if(equipements==null){
                              equipements = new ArrayList();
                              equipements.add(id);
                              dao.getEqSecteur().put(secteurP.getId(), equipements);
                            }else{
                                equipements.add(id);
                            }
                            //gridR.getChildren().remove(treeTableView);
                            //hbox.getChildren().remove(0);
                            pageHBlabel.getChildren().clear();
                            paneDesc.getChildren().clear();
                            labelT.setText("Equipement "+nomEQMTf.getText()+"("+ville.getNom()+") ajouté");

                            

                            dao.treetableviewAdminEq(dao, paneDesc, labelT, pageHBlabel, gridR,"treetableviewAE");
                            
                            
                            new Notifier("Gestab","Equipement:"+neweq.getNom()+" ajouté",2,dao);

                            actiontarget.setText(nomEQMTf.getText().toUpperCase(Locale.ITALY)+" ajouté.");
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


        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(annulerbtn,validerbtn);
        gridpane.add(hbBtn, 1, 6);
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(gridpane);
        sp.setPadding(new Insets(10, 10, 10, 10));

        scene = new Scene(sp, (dao.width*45)/100, (dao.height*50)/100,Color.web(dao.colordeAdminEq()));
        scene.getStylesheets().add("css/style.css");
        
        


        stage.setScene(scene);
        stage.setTitle("Ajouter un Equipement");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(
            ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
}
