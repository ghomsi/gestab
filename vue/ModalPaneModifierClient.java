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
import javafx.scene.control.DatePicker;
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
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class ModalPaneModifierClient {
    
    
    private final ActionEvent event;
    Scene scene,scene2;
    private final Client client;
    private DAOequipement dao;
    private Ville villeR;
    private Ville villeOwn;
    private Secteur sectR;
    private Equipement eqR;
    private Carte carteR;
    private Debit debitR;
    private Categorie categorieR;
    private Port portR,oldPort;
    private Carte oldCarte;
    private GridPane gridR;
    
    public ModalPaneModifierClient(DAOequipement dao,ActionEvent event,GridPane grid, Client client){
        this.event = event;
        this.client = client;
        this.dao = dao;
        this.gridR = grid;
        this.oldPort = null;
    }
    
    void construire(){
    
        Stage stage = new Stage();
        GridPane grid = new GridPane ();
        grid.setAlignment(Pos.CENTER);

        grid.setPadding(new Insets (10,10,10,10));
        grid.setVgap(7);
        grid.setHgap(7);

        
        
        Button validerbt = new Button ("Enregistrer");
        
        
        Label infoclt = new Label ("CLIENT");
        infoclt.setTextFill(Color.web(dao.couleur));
        infoclt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(infoclt, 1, 3);

        Label noms = new Label ("NOMS :");
        grid.add(noms, 0, 5);
        TextField nomTF = new TextField();
        nomTF.setText(client.getNom());
        nomTF.setEditable(false);
        grid.add(nomTF, 1, 5);

        Label prenoms = new Label("PRENOMS:");
        grid.add(prenoms, 0, 6);
        TextField prenomsTF = new TextField();
        prenomsTF.setText(client.getPrenom());
        prenomsTF.setEditable(false);
        grid.add(prenomsTF, 1, 6);

        Label numero = new Label("NUMERO TELEPHONE:");
        grid.add(numero, 0, 7);
        TextField numeroTF = new TextField();
        numeroTF.setText(client.getNumTel());
        numeroTF.setEditable(false);
        grid.add(numeroTF, 1, 7);
        
        Label debit = new Label("Debit:");
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
        Label debitL = new Label();
        HBox debitHB = new HBox(5);
        debitHB.getChildren().addAll(debitCB,debitL);
        debitCB.getStyleClass().add(dao.comboColor);
        
        debitCB.setCellFactory(new Callback<ListView<Debit>,ListCell<Debit>>(){
 
            @Override
            public ListCell<Debit> call(ListView<Debit> p) {
                 
                final ListCell<Debit> cell = new ListCell<Debit>(){
 
                    @Override
                    protected void updateItem(Debit t, boolean bln) {
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
        debitCB.valueProperty().addListener(new ChangeListener<Debit>() {
            @Override public void changed(ObservableValue ov, Debit t, Debit t1) {
              
              debitR = t1;
              debitL.setText(t1.getNom());
                
            }    
        });
        
        Debit debit1 = (Debit) dao.getDebit().get(client.getDebit());
        debitCB.promptTextProperty().set("choix ");
        
        
        //---Button ajouter Debit
        Button addDeb = new Button("+");
        addDeb.getStyleClass().add(dao.boutonColor);
        final Tooltip tooltipDeb = new Tooltip();
        tooltipDeb.setText("Ajouter un Debit");
        addDeb.setTooltip(tooltipDeb);
        
        addDeb.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               
                ModalAddDebit modal = new ModalAddDebit(event,dao,debitCB);
                try {
                    modal.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                    new Notifier("Gestab","erreur insertion debit "+debitCB.getValue()+":"+ex,3,dao);
                }
                
            }
        });
        
        HBox hbDeb = new HBox(5);
        if(dao.isSuperAdmin(dao.user)){
            hbDeb.getChildren().addAll(debitHB,addDeb);
        }else{
            hbDeb.getChildren().addAll(debitHB);
        }
        grid.add(hbDeb, 1, 8);
        Label debitl = new Label(debit1.getNom());
        debitl.setTextFill(Color.web(dao.couleur));
        grid.add(debitl,2,8);
        
        Label categorie = new Label("Catégorie:");
        grid.add(categorie, 0, 9);
        //---- combo Box categorie
        Hashtable cats = dao.getCategorieForfait();

        ObservableList<Categorie> optionsCat = FXCollections.observableArrayList();
        
        Enumeration e1 = cats.elements();
        while(e1.hasMoreElements()){
            Categorie elt = (Categorie) e1.nextElement();
            optionsCat.add(elt);
        }
        
        ComboBox categorieCB = new ComboBox<>(optionsCat);
        Label catL = new Label();
        HBox catHB = new HBox(5);
        catHB.getChildren().addAll(categorieCB,catL);
        categorieCB.getStyleClass().add(dao.comboColor);
        categorieCB.setCellFactory(new Callback<ListView<Categorie>,ListCell<Categorie>>(){
 
            @Override
            public ListCell<Categorie> call(ListView<Categorie> p) {
                 
                final ListCell<Categorie> cell = new ListCell<Categorie>(){
 
                    @Override
                    protected void updateItem(Categorie t, boolean bln) {
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
        
        Categorie cat = (Categorie) dao.getCategorieForfait().get(client.getCategorie());
        categorieCB.promptTextProperty().set("choix ");
        
        categorieCB.valueProperty().addListener(new ChangeListener<Categorie>() {
            @Override public void changed(ObservableValue ov, Categorie t, Categorie t1) {
              
              categorieR = t1; 
              catL.setText(t1.getNom());
                
            }    
        });
        
        //---Button ajouter Categorie
        Button addCat = new Button("+");
        addCat.getStyleClass().add(dao.boutonColor);
        final Tooltip tooltipCat = new Tooltip();
        tooltipCat.setText("Ajouter une Categorie");
        addCat.setTooltip(tooltipCat);
        
        addCat.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               
                ModalAddCategorie modal = new ModalAddCategorie(event,dao,categorieCB);
                try {
                    modal.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                    new Notifier("Gestab","erreur insertion categorie "+categorieCB.getValue()+":"+ex,3,dao);
                }
                
            }
        });
        
        HBox hbCat = new HBox(5);
        if(dao.isSuperAdmin(dao.user)){
            hbCat.getChildren().addAll(catHB,addCat);
        }else{
            hbCat.getChildren().addAll(catHB);
        }    
        grid.add(hbCat, 1, 9);
        Label catl = new Label(cat.getNom());
        catl.setTextFill(Color.web(dao.couleur));
        grid.add(catl,2,9);

        Label raisonsocial = new Label("Raison Sociale:");
        grid.add(raisonsocial, 0, 10);
        TextField raisonSTF = new TextField();
        raisonSTF.setText(client.getDesc());
        grid.add(raisonSTF, 1, 10);

        Label ville = new Label ("Ville:");
        grid.add(ville, 0, 11);
        
        //---- combo Box villes
        Hashtable villes = dao.getVille();

        ObservableList<Ville> options = FXCollections.observableArrayList();
        Enumeration e3 = villes.elements();
        while(e3.hasMoreElements()){
            Ville elt = (Ville) e3.nextElement();
            options.add(elt);
        }
        ComboBox villeCB = new ComboBox<>(options);
        Label villeL = new Label();
        HBox villeHB = new HBox(5);
        villeHB.getChildren().addAll(villeCB,villeL);
        villeCB.getStyleClass().add(dao.comboColor);
        
        villeCB.setCellFactory(new Callback<ListView<Ville>,ListCell<Ville>>(){
 
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
        
        villeCB.promptTextProperty().set("choix ");
        villeCB.valueProperty().addListener(new ChangeListener<Ville>() {
            @Override public void changed(ObservableValue ov, Ville t, Ville t1) {
              
              villeOwn = t1;
              villeL.setText(t1.getNom());
              
                
            }    
        });
        Ville villeOwn = (Ville) dao.getVille().get(client.getVilleOwner());
        grid.add(villeHB, 1, 11);
        Label villel = new Label(villeOwn.getNom());
        villel.setTextFill(Color.web(dao.couleur));
        grid.add(villel, 2, 11);

        Label quatier = new Label ("E-Side:") ;
        grid.add(quatier, 0, 12);
        TextField quatierTF = new TextField ();
        quatierTF.setText(client.getEside());
        grid.add(quatierTF, 1, 12);

        Label status = new Label ("Status:");
        grid.add(status, 0, 13);
        ComboBox statusCB = new ComboBox<>();
        statusCB.getStyleClass().add(dao.comboColor);
        statusCB.getItems().addAll("OK" ,"SUSPENDU");
        statusCB.setPromptText("choix ");
        grid.add(statusCB, 1, 13);
        Label statusl = new Label(client.getStatut());
        statusl.setTextFill(Color.web(dao.couleur));
        grid.add(statusl,2,13);

        Label date = new Label ("Date de Creation:");
        grid.add(date, 0, 14);
        final DatePicker dateTF = new DatePicker();
        grid.add(dateTF, 1, 14);
        grid.add(new Label(client.getDateCreation()),2,14);

        //For info equipement
         Label Ville = new Label ("Ville:");
        grid.add(Ville, 4, 5);
        ComboBox VilleEQCB = new ComboBox<>(options);
        Label villeEQL = new Label();
        HBox villeEQHB = new HBox(5);
        villeEQHB.getChildren().addAll(VilleEQCB,villeEQL);
        VilleEQCB.getStyleClass().add(dao.comboColor);
        grid.add(villeEQHB,5 ,5 );
        VilleEQCB.setPromptText("choix ");
        
        Ville villeE = (Ville) dao.getVille().get(client.getVille());
        if(villeE!=null){
            Label villeEQl = new Label(villeE.getNom());
            villeEQl.setTextFill(Color.web(dao.couleur));
            grid.add(villeEQl, 6, 5);
        }else{
            grid.add(new Label("N/A"), 6, 5);
        }
        ObservableList<Secteur> optionsSect = FXCollections.observableArrayList();
        Label secteur = new Label ("SECTEUR :");
        grid.add(secteur, 4, 6);
        ComboBox secteurCB = new ComboBox<>(optionsSect);
        Label secteurL = new Label();
        HBox secteurHB = new HBox(5);
        secteurHB.getChildren().addAll(secteurCB,secteurL);
        secteurCB.getStyleClass().add(dao.comboColor);
        grid.add(secteurHB, 5, 6);
        secteurCB.setPromptText("choix ");
        
        Secteur secteurE = (Secteur) dao.getSecteur().get(client.getSecteur());
        if(secteurE!=null){
            Label secteurEQl = new Label(secteurE.getNom());
            secteurEQl.setTextFill(Color.web(dao.couleur));
            grid.add(secteurEQl, 6, 6);
            
        }else{
            grid.add(new Label("N/A"), 6, 6);
            
        }
        Label equipements = new Label ("Equipement:");
        grid.add(equipements,4 ,7 );

        ObservableList<Secteur> optionsEQ = FXCollections.observableArrayList();
        ComboBox eqCB = new ComboBox<>(optionsEQ);
        Label eqL = new Label();
        HBox eqHB = new HBox(5);
        eqHB.getChildren().addAll(eqCB,eqL);
        eqCB.getStyleClass().add(dao.comboColor);
        grid.add(eqHB, 5, 7);
        eqCB.setPromptText("choix ");
        
        Equipement eq = (Equipement) dao.getEquipement().get(client.getEq());
        if(eq!=null){
            Label eql = new Label(eq.getNom());
            eql.setTextFill(Color.web(dao.couleur));
            grid.add(eql, 6, 7);
        }else{
            grid.add(new Label("N/A"), 6, 7);
          
        }
        

        Label carte = new Label ("Carte:");
        grid.add(carte, 4, 8);
        ObservableList<Secteur> optionsCarte = FXCollections.observableArrayList();
        ComboBox carteCB = new ComboBox<>(optionsCarte);
        Label carteL = new Label();
        HBox carteHB = new HBox(5);
        carteHB.getChildren().addAll(carteCB,carteL);
        carteCB.getStyleClass().add(dao.comboColor);
        grid.add(carteHB, 5, 8);
        carteCB.setPromptText("choix ");
        
        Carte carteE = (Carte) dao.getCarte().get(client.getCarte());
        if(carteE!=null){
            Label cartel = new Label(""+carteE.getNumCarte());
            cartel.setTextFill(Color.web(dao.couleur));
            grid.add(cartel,6,8);
            
        }else{
            grid.add(new Label("N/A"),6,8);
            
        }
        


        Label port = new Label("Port:");
        grid.add(port, 4, 9);
        ObservableList<Secteur> optionsPort = FXCollections.observableArrayList();
        ComboBox portCB = new ComboBox<>(optionsPort);
        Label portL = new Label();
        HBox portHB = new HBox(5);
        portHB.getChildren().addAll(portCB,portL);
        portCB.getStyleClass().add(dao.comboColor);
        grid.add(portHB, 5, 9);
        portCB.setPromptText("choix ");
        
        Port portE = (Port) dao.getPort().get(client.getPort());
        if(portE!=null){
            Label portl = new Label(""+portE.getNumeroPort());
            portl.setTextFill(Color.web(dao.couleur));
            grid.add(portl,6,9);
            
        }else{
            grid.add(new Label("N/A"),6,9);
            
        }
        Label actionTarget = new Label("");
        grid.add(actionTarget, 5, 11);
        
        
        Label email = new Label("Email :");
        grid.add(email, 4, 10);
        TextField emailTF = new TextField();
        if(client.getEmail()==null || client.getEmail().isEmpty()){
            emailTF.setPromptText("N/A");
        }else{
            emailTF.setText(client.getEmail());
        }
        grid.add(emailTF, 5, 10);
        

       
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
              validerbt.setDisable(true);
              ArrayList sects = (ArrayList) dao.getSecteurVille().get(t1.getId());
              if(sects!=null && !sects.isEmpty()){
                Iterator<Integer> e7 = sects.iterator();
                while(e7.hasNext()) {
                    int id = e7.next();
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
                      Iterator<Integer> e6 = eqs.iterator();
                      while(e6.hasNext()) {
                          int id = e6.next();
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
                      Iterator<Integer> e5 = cartes.iterator();
                      while(e5.hasNext()) {
                          int id = e5.next();
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
                      Iterator<Integer> e4 = ports.iterator();
                      while(e4.hasNext()) {
                          int id = e4.next();
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
              validerbt.setDisable(false);
                
            }    
        });
        

        

        Label infoEQ = new Label("EQUIPEMENT");
        infoEQ.setTextFill(Color.web(dao.colordeGestionCl()));
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

        
        validerbt.getStyleClass().add(dao.colornodanger());
        
        validerbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if(nomTF.getText().isEmpty()){
                        actionTarget.setText(noms.getText()+" Champ vide");
                    }else if(prenomsTF.getText().isEmpty()){
                        actionTarget.setText(prenoms.getText()+" Champ vide");
                    }else if(numeroTF.getText().isEmpty() || !dao.isNumeroCamtel(numeroTF.getText())){
                        actionTarget.setText(numero.getText()+" Champ vide");
                    }else if(raisonSTF.getText().isEmpty()){
                        actionTarget.setText(raisonsocial.getText()+" Champ vide");
                    }else if(quatierTF.getText().isEmpty()){
                        actionTarget.setText(quatier.getText()+" Champ vide");
                    }else if(client.getVille()==0){
                        actionTarget.setText(ville.getText()+" Champ Non Aloué");
                    }else if(client.getSecteur()==0){
                        actionTarget.setText(secteur.getText()+" Champ Non Aloué");
                    }else if(client.getEq()==0){
                        actionTarget.setText(equipements.getText()+" Champ Non Aloué");
                    }else if(client.getCarte()==0){
                        actionTarget.setText(carte.getText()+" Champ Non Aloué");
                    }else if(client.getPort()==0){
                        actionTarget.setText(port.getText()+" Champ Non Aloué");
                    }else{
                        /*if(dao.getNomClient().contains(nomTF.getText().toUpperCase()+" "+prenomsTF.getText().toUpperCase())){
                            actionTarget.setText(nomTF.getText().toUpperCase()+" "+prenomsTF.getText().toUpperCase()+" existe déja!");
                        }else if(dao.getNumero().contains(numeroTF.getText())){
                            actionTarget.setText(numeroTF.getText()+" existe déja!");
                        }else{*/
                        
                            if(debitCB.getValue()!=null){
                                client.setDebit(debitR.getId());
                            }
                            if(categorieCB.getValue()!=null){
                                client.setCategorie(categorieR.getId());
                            }
                            if(villeCB.getValue()!=null){
                                client.setVilleOwner(villeOwn.getId());
                            } 
                            if(statusCB.getValue()!=null){
                                client.setStatus(statusCB.getValue().toString());
                            }
                            if(VilleEQCB.getValue()!=null){
                                client.setVille(villeR.getId());
                            }
                            if(secteurCB.getValue()!=null){
                                client.setSecteur(sectR.getId());
                            }
                            if(eqCB.getValue()!=null){
                                client.setEq(eqR.getIdEQ());
                            }
                            if(carteCB.getValue()!=null){
                                oldCarte = (Carte) dao.getCarte().get(client.getCarte());
                                client.setCarte(carteR.getId());
                            }
                            if(portCB.getValue()!=null){
                                if(client.getPort()!=0){
                                    oldPort = (Port) dao.getPort().get(client.getPort());
                                    client.setPort(portR.getId());
                                }
                            }
                            if(dateTF.getValue()!=null){
                                client.setDateCreation(dateTF.getValue().toString());
                            }
                            if(!emailTF.getText().isEmpty()){
                                    if(!dao.isEmail(emailTF.getText())){
                                        actionTarget.setText(email.getText()+" format invalide");
                                    }else{
                                        client.setEmail(emailTF.getText());
                                    }
                            }
                            
                            
                            Connecter c=null;
                            try {
                                c = new Connecter();
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            Requettes con = new Requettes(c);
                               if(portR!=null){
                                    portR.setStatus(portR.occupe());
                               }
                               client.setNom(nomTF.getText().toUpperCase());
                               client.setPrenom(prenomsTF.getText().toUpperCase());
                               client.setNumTel(numeroTF.getText());
                               client.setDesc(raisonSTF.getText());
                               client.setEside(quatierTF.getText().toUpperCase());
                               
                               
                               
                               try { 
                                   con.fUpdateClient(client);
                                   dao.getClient().replace(client.getId(), client);
                                   
                                   if(portR!=null){
                                       //mise à jour du status du nouveau port
                                       con.fUpdateSatutPort(portR);
                                        if(oldPort!=null){
                                            oldPort.setStatus(oldPort.librerer());
                                            dao.getPortClient().remove(oldPort.getId());
                                            // mise à jour du status de l'ancien port si le client ne possedait un!
                                            con.fUpdateSatutPort(oldPort);
                                        }

                                        //ajout du nouveau client(objet) au port (portclient objet)
                                        dao.getPortClient().replace(portR.getId(), client.getId());
                                   }
                                   c.getConnexion().close();
                                   
                                   //clear la grid sur une certaine plage
                                   gridR.getChildren().remove(0, 29);
                                   
                                   dao.gridClientVue(dao, gridR, client);
                                   
                                   actionTarget.setText("Client mis a jour.");
                                   
                                   stage.close();
                                   
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                                   actionTarget.setText("Echec d'insertion.");
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                                   actionTarget.setText("Echec d'insertion.");
                               } catch (ParseException ex) {
                                    Logger.getLogger(ModalPaneModifierClient.class.getName()).log(Level.SEVERE, null, ex);
                                    actionTarget.setText("Echec d'insertion.Probleme de Date");
                               }
                        }
                    /*}*/
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
                
        scene = new Scene(sp, (dao.width*90)/100, (dao.height*72)/100);
        scene.getStylesheets().add("css/style.css");
                
               
                
                
        stage.setScene(scene);
        stage.setTitle("Modifier un Client");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(
            ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
}
