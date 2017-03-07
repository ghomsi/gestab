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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
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
import javafx.scene.control.Tooltip;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
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
public class ModalPaneChangePortClient {
    
    
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
    private GridPane griduser;
    
    public ModalPaneChangePortClient(DAOequipement dao,ActionEvent event, Client client,GridPane grid){
        this.event = event;
        this.client = client;
        this.griduser = grid;
        this.dao = dao;
    }
    
    public void construire(){
    
        Stage stage = new Stage();
        GridPane grid = new GridPane ();
        grid.setAlignment(Pos.CENTER);

        grid.setPadding(new Insets (10,10,10,10));
        grid.setVgap(7);
        grid.setHgap(7);


        Label infoclt = new Label ("CLIENT");
        infoclt.setTextFill(Color.web(dao.couleur));
        infoclt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(infoclt, 1, 3);

        Label noms = new Label ("NOMS :");
        grid.add(noms, 0, 5);
        TextField nomTF = new TextField();
        nomTF.setText(client.getNom());
        nomTF.setDisable(true);
        grid.add(nomTF, 1, 5);

        Label prenoms = new Label("PRENOMS :");
        grid.add(prenoms, 0, 6);
        TextField prenomsTF = new TextField();
        prenomsTF.setText(client.getPrenom());
        prenomsTF.setDisable(true);
        grid.add(prenomsTF, 1, 6);

        Label numero = new Label("NUMERO TELEPHONE :");
        grid.add(numero, 0, 7);
        TextField numeroTF = new TextField();
        numeroTF.setText(client.getNumTel());
        numeroTF.setDisable(true);
        grid.add(numeroTF, 1, 7);
        
        Label debit = new Label("Debit :");
        grid.add(debit, 0, 8);
        //---- combo Box debit
        Hashtable debits = dao.getDebit();

        ObservableList<Debit> optionsDebit = FXCollections.observableArrayList();
        Enumeration e = debits.elements();
        while(e.hasMoreElements()) {
            Debit elt = (Debit) e.nextElement();
            optionsDebit.add(elt);
        }
        ComboBox debitCB = new ComboBox<>(optionsDebit);
        debitCB.getStyleClass().add(dao.comboColor);
        
        debitCB.setDisable(true);
        Debit debit1 = (Debit) dao.getDebit().get(client.getDebit());
        debitCB.promptTextProperty().set(debit1.getNom());
        
        
        HBox hbDeb = new HBox();
        hbDeb.getChildren().addAll(debitCB);
        
        grid.add(hbDeb, 1, 8);
        
        Label categorie = new Label("Catégorie :");
        grid.add(categorie, 0, 9);
        //---- combo Box categorie
        Hashtable cats = dao.getCategorieForfait();

        ObservableList<Categorie> optionsCat = FXCollections.observableArrayList();
        
        
        ComboBox categorieCB = new ComboBox<>(optionsCat);
        categorieCB.getStyleClass().add(dao.comboColor);
        
        
        Categorie cat = (Categorie) dao.getCategorieForfait().get(client.getCategorie());
        categorieCB.promptTextProperty().set(cat.getNom());
        categorieCB.setDisable(true);
        
        
        
        HBox hbCat = new HBox();
        hbCat.getChildren().addAll(categorieCB);
        
        grid.add(hbCat, 1, 9);

        Label raisonsocial = new Label("Raison Sociale:");
        grid.add(raisonsocial, 0, 10);
        TextField raisonSTF = new TextField();
        raisonSTF.setText(client.getDesc());
        raisonSTF.setDisable(true);
        grid.add(raisonSTF, 1, 10);

        Label ville = new Label ("Ville:");
        grid.add(ville, 0, 11);
        
        //---- combo Box villes
        

        ObservableList<Ville> options = FXCollections.observableArrayList();
        
        ComboBox villeCB = new ComboBox<>(options) ;
        villeCB.getStyleClass().add(dao.comboColor);
        
        Ville villeOwn = (Ville) dao.getVille().get(client.getVilleOwner());
        villeCB.setPromptText(villeOwn.getNom());
        villeCB.setDisable(true);
        grid.add(villeCB, 1, 11);

        Label quatier = new Label ("E-Side:") ;
        grid.add(quatier, 0, 12);
        TextField quatierTF = new TextField ();
        quatierTF.setText(client.getEside());
        quatierTF.setDisable(true);
        grid.add(quatierTF, 1, 12);

        Label status = new Label ("Status:");
        grid.add(status, 0, 13);
        ComboBox statusCB = new ComboBox<>() ;
        statusCB.getStyleClass().add(dao.comboColor);
        statusCB.getItems().addAll("OK" ,"SUSPENDU");
        statusCB.setPromptText("confirmer "+client.getStatut());
        statusCB.setDisable(true);
        grid.add(statusCB, 1, 13);

        Label date = new Label ("Date de Creation:");
        grid.add(date, 0, 14);
        TextField dateTF = new TextField ();
        dateTF.setText(client.getDateCreation());
        dateTF.setDisable(true);
        grid.add(dateTF, 1, 14);

        //For info equipement
         Label Ville = new Label ("Ville:");
        grid.add(Ville, 4, 5);
        
        Hashtable villes = dao.getVille();
        Enumeration e3 = villes.elements();
        while(e3.hasMoreElements()){
            Ville elt = (Ville) e3.nextElement();
            options.add(elt);
        }
        ComboBox VilleEQCB = new ComboBox<>(options);
        Label villeEQL = new Label();
        HBox villeHB = new HBox(5);
        villeHB.getChildren().addAll(VilleEQCB,villeEQL);
        VilleEQCB.getStyleClass().add(dao.comboColor);
        VilleEQCB.setPromptText("choix");
        grid.add(villeHB,5 ,5 );
        
        Ville villeE = (Ville) dao.getVille().get(client.getVille());
        if(villeE!=null){
            Label villel = new Label(villeE.getNom());
            villel.setTextFill(Color.web(dao.couleur));
            grid.add(villel,6,5);
            
        }else{
            VilleEQCB.setPromptText("N/A ");
        }
        ObservableList<Secteur> optionsSect = FXCollections.observableArrayList();
        Label secteur = new Label ("SECTEUR :");
        grid.add(secteur, 4, 6);
        ComboBox secteurCB = new ComboBox<>(optionsSect);
        Label secteurL = new Label();
        HBox secteurHB = new HBox(5);
        secteurHB.getChildren().addAll(secteurCB,secteurL);
        secteurCB.getStyleClass().add(dao.comboColor);
        secteurCB.setPromptText("...");
        grid.add(secteurHB, 5, 6);
        
        Secteur secteurE = (Secteur) dao.getSecteur().get(client.getSecteur());
        if(secteurE!=null){
            Label secteurl = new Label(secteurE.getNom());
            secteurl.setTextFill(Color.web(dao.couleur));
            grid.add(secteurl,6,6);
        }else{
            secteurCB.setPromptText("N/A");
        }
        Label equipements = new Label ("Equipement:");
        grid.add(equipements,4 ,7 );

        ObservableList<Secteur> optionsEQ = FXCollections.observableArrayList();
        ComboBox eqCB = new ComboBox<>(optionsEQ);
        Label eqL = new Label();
        HBox eqHB = new HBox(5);
        eqCB.setPromptText("...");
        eqHB.getChildren().addAll(eqCB,eqL);
        eqCB.getStyleClass().add(dao.comboColor);
        Equipement eq = (Equipement) dao.getEquipement().get(client.getEq());
        if(eq!=null){
            Label eql = new Label(eq.getNom());
            eql.setTextFill(Color.web(dao.couleur));
            grid.add(eql, 6, 7);
        }else{
            eqCB.setPromptText("N/A");
        }
        grid.add(eqHB, 5, 7);

        Label carte = new Label ("Carte:");
        grid.add(carte, 4, 8);
        ObservableList<Secteur> optionsCarte = FXCollections.observableArrayList();
        ComboBox carteCB = new ComboBox<>(optionsCarte);
        Label carteL = new Label();
        HBox carteHB = new HBox(5);
        carteHB.getChildren().addAll(carteCB,carteL);
        carteCB.setPromptText("...");
        carteCB.getStyleClass().add(dao.comboColor);
        
        Carte carteE = (Carte) dao.getCarte().get(client.getCarte());
        if(carteE!=null){
            Label cartel = new Label(""+carteE.getNumCarte());
            cartel.setTextFill(Color.web(dao.couleur));
            grid.add(cartel,6,8);
            
        }else{
            carteCB.setPromptText("N/A");
        }
        grid.add(carteHB, 5, 8);


        Label port = new Label("Port:");
        grid.add(port, 4, 9);
        ObservableList<Secteur> optionsPort = FXCollections.observableArrayList();
        ComboBox portCB = new ComboBox<>(optionsPort);
        Label portL = new Label();
        HBox portHB = new HBox(5);
        portHB.getChildren().addAll(portCB,portL);
        portCB.getStyleClass().add(dao.comboColor);
        portCB.setPromptText("...");
        grid.add(portHB, 5, 9);
        
        Port portE = (Port) dao.getPort().get(client.getPort());
        if(portE!=null){
            Label portl = new Label(""+portE.getNumeroPort());
            portl.setTextFill(Color.web(dao.couleur));
            
        }else{
            portCB.setPromptText("N/A");
        }
        Label actionTarget = new Label("");
        grid.add(actionTarget, 5, 11);

       
        VilleEQCB.setCellFactory(new Callback<ListView<Ville>,ListCell<Ville>>(){
 
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
        VilleEQCB.valueProperty().addListener(new ChangeListener<Ville>() {
            @Override public void changed(ObservableValue ov, Ville t, Ville t1) {
                
              secteurCB.getItems().clear();
              secteurL.setText("");
              eqCB.getItems().clear();
              eqL.setText("");
              carteCB.getItems().clear();
              carteL.setText("");
              portCB.getItems().clear();
              portL.setText("");
              
              villeR = t1;
              villeEQL.setText(t1.getNom());
              ArrayList sects = (ArrayList) dao.getSecteurVille().get(t1.getId());
              if(sects!=null && !sects.isEmpty()){
                Iterator<Integer> e1 = sects.iterator();
                while(e1.hasNext()){
                    int id = e1.next();
                    Secteur elt = (Secteur) dao.getSecteur().get(id);
                    secteurCB.getItems().add(elt);
                }
              }
              
              secteurCB.setCellFactory(new Callback<ListView<Secteur>,ListCell<Secteur>>(){
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
                
               secteurCB.setPromptText("choix");
                
                
            }    
        });
        
        //--equipement combobox
        secteurCB.valueProperty().addListener(new ChangeListener<Secteur>() {
            @Override public void changed(ObservableValue ov, Secteur t, Secteur t1) {
                
              eqCB.getItems().clear();
              eqL.setText("");
              carteCB.getItems().clear();
              carteL.setText("");
              portCB.getItems().clear();
              portL.setText("");
              
              sectR = t1;
              if(t1!=null){
                    secteurL.setText(t1.getNom());
                    ArrayList eqs = (ArrayList) dao.getEqSecteur().get(t1.getId());
                    if(eqs!=null && !eqs.isEmpty()){
                      Iterator<Integer> e1 = eqs.iterator();
                      while(e1.hasNext()){
                          int id = e1.next();
                          Equipement elt = (Equipement) dao.getEquipement().get(id);
                          eqCB.getItems().add(elt);
                      }
                    }
                    eqCB.setCellFactory(new Callback<ListView<Equipement>,ListCell<Equipement>>(){
                      @Override
                      public ListCell<Equipement> call(ListView<Equipement> p) {

                              final ListCell<Equipement> cell = new ListCell<Equipement>(){

                                  @Override
                                  protected void updateItem(Equipement t, boolean bln) {
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

                     eqCB.setPromptText("choix");
                }
                
            }    
        });
        
        //carte combobox
        eqCB.valueProperty().addListener(new ChangeListener<Equipement>() {
            @Override public void changed(ObservableValue ov, Equipement t, Equipement t1) {
                
              carteCB.getItems().clear();
              carteL.setText("");
              portCB.getItems().clear();
              portL.setText("");
              
              eqR = t1;
              if(t1!=null){
                    eqL.setText(t1.getNom());
                    ArrayList cartes = (ArrayList) dao.getCarteEq().get(t1.getIdEQ());
                    if(cartes!=null && !cartes.isEmpty()){
                      Iterator<Integer> e1 = cartes.iterator();
                      while(e1.hasNext()){
                          int id = e1.next();
                          Carte elt = (Carte) dao.getCarte().get(id);
                          carteCB.getItems().add(elt);
                      }
                    }
                    carteCB.setCellFactory(new Callback<ListView<Carte>,ListCell<Carte>>(){
                      @Override
                      public ListCell<Carte> call(ListView<Carte> p) {

                              final ListCell<Carte> cell = new ListCell<Carte>(){

                                  @Override
                                  protected void updateItem(Carte t, boolean bln) {
                                      super.updateItem(t, bln);

                                      if(t != null){
                                          setText("carte "+t.getNumCarte());
                                      }else{
                                          setText(null);
                                      }
                                  }

                              };

                              return cell;
                          }
                      });

                     carteCB.setPromptText("choix");
              }  
                
            }    
        });
        
        // port combobox
        carteCB.valueProperty().addListener(new ChangeListener<Carte>() {
            @Override public void changed(ObservableValue ov, Carte t, Carte t1) {
                
              portCB.getItems().clear();
              portL.setText("");
              
              carteR = t1;
              if(t1!=null){
                    carteL.setText(""+t1.getNumCarte());
                    ArrayList ports = (ArrayList) dao.getPortCarte().get(t1.getId());
                    if(ports!=null && !ports.isEmpty()){
                      Iterator<Integer> e1 = ports.iterator();
                      while(e1.hasNext()){
                          int id = e1.next();
                          Port elt = (Port) dao.getPort().get(id);
                          if(elt.isLibre()){
                              portCB.getItems().add(elt);
                          }
                      }
                    }
                    portCB.setCellFactory(new Callback<ListView<Port>,ListCell<Port>>(){
                      @Override
                      public ListCell<Port> call(ListView<Port> p) {

                              final ListCell<Port> cell = new ListCell<Port>(){

                                  @Override
                                  protected void updateItem(Port t, boolean bln) {
                                      super.updateItem(t, bln);

                                      if(t != null){
                                          setText("port "+t.getNumeroPort()+" libre");
                                      }else{
                                          setText(null);
                                      }
                                  }

                              };

                              return cell;
                          }
                      });

                     portCB.setPromptText("choix");
                }
                
            }    
        });
        
        //--port
        
        portCB.valueProperty().addListener(new ChangeListener<Port>() {
            @Override public void changed(ObservableValue ov, Port t, Port t1) {
              
              portR = t1;
              if(t1!=null)portL.setText(""+t1.getNumeroPort());
                
            }    
        });
        

        

        Label infoEQ = new Label("EQUIPEMENT");
        infoEQ.setTextFill(Color.web(dao.couleur));
        infoEQ.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(infoEQ,4 ,3 );

        //BUTTON
        Button resetbt = new Button ("Annuler");
        resetbt.getStyleClass().add(dao.boutonColor);
        
        
        resetbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
            });

        Button validerbt = new Button ("Enregistrer");
        validerbt.getStyleClass().add(dao.colornodanger());
        
        validerbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if(VilleEQCB.getValue()==null){
                        actionTarget.setText(Ville.getText()+" Equipement Champ vide");
                    }else if(secteurCB.getValue()==null){
                        actionTarget.setText(secteur.getText()+" Champ vide");
                    }else if(eqCB.getValue()==null){
                        actionTarget.setText(equipements.getText()+" Champ vide");
                    }else if(carteCB.getValue()==null){
                        actionTarget.setText(carte.getText()+" Champ vide");
                    }else if(portCB.getValue()==null){
                        actionTarget.setText(port.getText()+" Champ vide");
                    }else{
                        
                            Connecter c=null;
                            try {
                                c = new Connecter();
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            Requettes con = new Requettes(c);
                               
                               Port oldPort = (Port) dao.getPort().get(client.getPort());
                               oldPort.setStatus(oldPort.librerer());
                               portR.setStatus(portR.occupe());
                               
                               Carte oldCarte = (Carte) dao.getCarte().get(client.getCarte());
                               client.setVille(villeR.getId());
                               client.setEq(eqR.getIdEQ());
                               client.setSecteur(sectR.getId());
                               client.setCarte(carteR.getId());
                               client.setPort(portR.getId());
                               
                               try { 
                                   con.fUpdateClient(client);
                                   con.fUpdateSatutPort(portR);
                                   con.fUpdateSatutPort(oldPort);
                                   c.getConnexion().close();
                                   
                                   oldPort.setStatus(oldPort.librerer());
                                   dao.getPortClient().remove(oldPort.getId());
                                   
                                   // ajournement de la table de hachage port(objet) carte(hashtable de port)
                                   Port newPort = (Port) dao.getPort().get(client.getPort());
                                   newPort.setStatus(newPort.occupe());
                                  
                                   Port oldP = (Port) dao.getPort().get(oldPort.getId());
                                   oldP.setStatus(oldP.librerer());
                                   
                                   //ajout du nouveau client(objet) au port (portclient objet)
                                   dao.getPortClient().put(portR.getId(), client.getId());
                                   dao.getPortClient().remove(oldP.getId());
                                   
                                   griduser.getChildren().clear();
                                   dao.allouerPortVue(dao, oldP,griduser);
                                   
                                   
                                   actionTarget.setText("Client mis a jour.");
                                   
                                   stage.close();
                                   
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                                   actionTarget.setText("Echec d'insertion.");
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                                   actionTarget.setText("Echec d'insertion.");
                               } catch (ParseException ex) {
                                    Logger.getLogger(ModalPaneChangePortClient.class.getName()).log(Level.SEVERE, null, ex);
                                    actionTarget.setText("Echec d'insertion.Probleme date");
                               }
                        
                    }
                }
            });
        

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(resetbt,validerbt);
        grid.add(hbBtn, 1, 15);
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(grid);
        sp.setPadding(new Insets(10, 10, 10, 10));
                
        scene = new Scene(sp,(dao.width*90)/100, (dao.height*70)/100);
        scene.getStylesheets().add("css/style.css");
               
                
                
        stage.setScene(scene);
        stage.setTitle("Changer port du Client");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(
            ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
}
