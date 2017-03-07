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
import java.time.LocalDate;
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
import modele.Carte;
import modele.Categorie;
import modele.Client;
import modele.Debit;
import modele.Equipement;
import modele.Historique;
import modele.Port;
import modele.Secteur;
import modele.Ville;
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class ModalAddClient {
    
    private final ActionEvent event;
    Scene scene,scene2;
    private DAOequipement dao;
    private Ville villeR;
    private Ville villeOwn;
    private Secteur sectR;
    private Equipement eqR;
    private Carte carteR;
    private Debit debitR;
    private Categorie categorieR;
    private Port portR;
    private final Label labelT;
    private TreeItem<Client> rootR;
    private final GridPane gridR;
    private TreeTableView<Client> treeTableView;
    private VBox paneR;
    private int hauteur,largeur;
    
    public ModalAddClient(ActionEvent event,DAOequipement dao,Label label,GridPane grid,VBox pane){
        this.event = event;
        this.dao = dao;
        this.labelT = label;
        this.gridR = grid;
        this.paneR = pane;
        this.hauteur = (int) ((dao.height*80)/100);
        this.largeur = (int) ((dao.width*90)/100);
    }
    
    void construire(){
    
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
        nomTF.setText("N/A");
        grid.add(nomTF, 1, 5);

        Label prenoms = new Label("PRENOMS :");
        grid.add(prenoms, 0, 6);
        TextField prenomsTF = new TextField();
        prenomsTF.setText("N/A");
        grid.add(prenomsTF, 1, 6);

        Label numero = new Label("NUMERO TELEPHONE :");
        grid.add(numero, 0, 7);
        TextField numeroTF = new TextField();
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
        Label labelDebit = new Label();
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
              labelDebit.setText(t1.getNom());
                
            }    
        });
        
        //---Button ajouter Debit
        Button addDeb = new Button("+");
        addDeb.getStyleClass().add(dao.boutonColor);
        final Tooltip tooltipDeb = new Tooltip();
        tooltipDeb.setText("Ajouter un Debit");
        addDeb.setTooltip(tooltipDeb);
        
        addDeb.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               System.out.println("add ville");
               
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
            hbDeb.getChildren().addAll(debitCB,addDeb,labelDebit);
        }else{
            hbDeb.getChildren().addAll(debitCB,labelDebit);
        }
        grid.add(hbDeb, 1, 8);
        
        Label categorie = new Label("Catégorie :");
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
        Label labelcat = new Label();
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
        categorieCB.valueProperty().addListener(new ChangeListener<Categorie>() {
            @Override public void changed(ObservableValue ov, Categorie t, Categorie t1) {
              
              categorieR = t1;  
              labelcat.setText(t1.getNom());
                
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
            hbCat.getChildren().addAll(categorieCB,addCat,labelcat);
        }else{
            hbCat.getChildren().addAll(categorieCB,labelcat);
        }
        grid.add(hbCat, 1, 9);
        

        Label raisonsocial = new Label("Raison Sociale:");
        grid.add(raisonsocial, 0, 10);
        TextField raisonSTF = new TextField();
        raisonSTF.setText("N/A");
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
        villeCB.valueProperty().addListener(new ChangeListener<Ville>() {
            @Override public void changed(ObservableValue ov, Ville t, Ville t1) {
              
              villeOwn = t1;
              villeL.setText(t1.getNom());
              
                
            }    
        });

        
        
        
        
        
        ObservableList<Secteur> optionsSect = FXCollections.observableArrayList();
        Label secteur = new Label ("SECTEUR :");
        grid.add(secteur, 4, 6);
        ComboBox secteurCB = new ComboBox<>(optionsSect);
        Label secteurL = new Label();
        HBox secteurHB = new HBox(5);
        secteurHB.getChildren().addAll(secteurCB,secteurL);
        secteurCB.getStyleClass().add(dao.comboColor);
        grid.add(secteurHB, 5, 6);
        secteurCB.setPromptText("...");
        
        villeCB.setPromptText("Villes");
        HBox villeHB = new HBox(5);
        villeHB.getChildren().addAll(villeCB,villeL);
        
        grid.add(villeHB, 1, 11);

        Label quatier = new Label ("E-Side:") ;
        grid.add(quatier, 0, 12);
        TextField quatierTF = new TextField ();
        quatierTF.setText("N/A");
        grid.add(quatierTF, 1, 12);

        Label status = new Label ("Status:");
        grid.add(status, 0, 13);
        ComboBox statusCB = new ComboBox<>();
        statusCB.getStyleClass().add(dao.comboColor);
        statusCB.getItems().addAll("ACTIF" ,"SUSPENDU","RESILIE");
        statusCB.getSelectionModel().selectFirst();
        grid.add(statusCB, 1, 13);

        Label date = new Label ("Date de Creation:");
        grid.add(date, 0, 14);
        //TextField dateTF = new TextField ();
        final DatePicker dateTF = new DatePicker();
        grid.add(dateTF, 1, 14);

        //For info equipement

        Label equipements = new Label ("Equipement:");
        grid.add(equipements,4 ,7 );
        
        ObservableList<Equipement> optionsEQ = FXCollections.observableArrayList();
        ComboBox eqCB = new ComboBox<>(optionsEQ);
        Label eqL = new Label();
        eqCB.getStyleClass().add(dao.comboColor);
        eqCB.setPromptText("...");
        HBox eqHB = new HBox(5);
        eqHB.getChildren().addAll(eqCB,eqL);
        grid.add(eqHB, 5, 7);

        Label carte = new Label ("Carte:");
        grid.add(carte, 4, 8);
        
        ObservableList<Carte> optionsCarte = FXCollections.observableArrayList();
        ComboBox carteCB = new ComboBox<>(optionsCarte);
        Label carteL = new Label();
        carteCB.getStyleClass().add(dao.comboColor);
        carteCB.setPromptText("...");
        HBox carteHB = new HBox(5);
        carteHB.getChildren().addAll(carteCB,carteL);
        grid.add(carteHB, 5, 8);


        Label port = new Label("Port:");
        grid.add(port, 4, 9);
        
        ObservableList<Port> optionsPort = FXCollections.observableArrayList();
        ComboBox portCB = new ComboBox<>(optionsPort);
        Label portL = new Label();
        HBox portHB = new HBox(5);
        portHB.getChildren().addAll(portCB,portL);
        portCB.getStyleClass().add(dao.comboColor);
        grid.add(portHB, 5, 9);
        portCB.setPromptText("...");
        
        Label actionTarget = new Label("");
        grid.add(actionTarget, 5, 11);
        
        //--equipement
        Label Ville = new Label ("Ville:");
        grid.add(Ville, 4, 5);
        ComboBox VilleEQCB = new ComboBox<>(options);
        Label villeEQL = new Label();
        HBox villeEQHB = new HBox(5);
        villeEQHB.getChildren().addAll(VilleEQCB,villeEQL);
        VilleEQCB.getStyleClass().add(dao.comboColor);
        grid.add(villeEQHB,5 ,5 );
        VilleEQCB.setPromptText("choix");
        
        
        Label email = new Label("Email :");
        grid.add(email, 4, 10);
        TextField emailTF = new TextField();
        emailTF.setText("N/A");
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
        
        //--secteur combobox
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
        
        //equipement combobox
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
                             if(elt.getStatut()==0){ //port libre uniquement
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
                                          setText("port "+t.getNumeroPort()+" Libre");
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
        
        Button btnRech = new Button("rechercher >");
        btnRech.getStyleClass().add(dao.boutonColor);
        
        btnRech.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        VBox modalPaneRech = new ModalRechercherClient(dao,stage,scene).contruire();
                        scene2 = new Scene(modalPaneRech, largeur, hauteur);
                        stage.setTitle("Rechercher un client");
                        stage.setScene(scene2);
                    }
                });
        
        resetbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
            });

        Button validerbt = new Button ("Ajouter");
        validerbt.getStyleClass().add(dao.colornodanger());
        
        validerbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if(nomTF.getText().isEmpty()){
                        actionTarget.setText(noms.getText()+" Champ vide");
                    }else if(prenomsTF.getText().isEmpty()){
                        actionTarget.setText(prenoms.getText()+" Champ vide");
                    }else if(numeroTF.getText().trim().isEmpty() || !dao.isNumeroCamtel(numeroTF.getText().trim())){
                        actionTarget.setText(numero.getText()+" Champ vide");
                    }else if(debitCB.getValue()==null){
                        actionTarget.setText(debit.getText()+" Champ vide");
                    }else if(categorieCB.getValue()==null){
                        actionTarget.setText(categorie.getText()+" Champ vide");
                    }else if(raisonSTF.getText().isEmpty()){
                        actionTarget.setText(raisonsocial.getText()+" Champ vide");
                    }else if(villeCB.getValue()==null){
                        actionTarget.setText(ville.getText()+" Champ vide");
                    }else if(quatierTF.getText().isEmpty()){
                        actionTarget.setText(quatier.getText()+" Champ vide");
                    }else if(statusCB.getValue()==null){
                        actionTarget.setText(status.getText()+" Champ vide");
                    }else if(dateTF.getValue().toString().isEmpty()){
                        actionTarget.setText(date.getText()+" Champ vide");
                    }else if(VilleEQCB.getValue()==null){
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
                        if(!emailTF.getText().isEmpty()){
                            if(!emailTF.getText().equals("N/A")&&!dao.isEmail(emailTF.getText())){
                                actionTarget.setText(email.getText()+" format invalide");
                            }else{
                                if(dao.getNomClient().contains(nomTF.getText().toUpperCase()+" "+prenomsTF.getText().toUpperCase())){
                                    actionTarget.setText(nomTF.getText().toUpperCase()+" "+prenomsTF.getText().toUpperCase()+" existe déja!");
                                }else if(dao.getNumero().containsKey(numeroTF.getText().trim())){
                                    actionTarget.setText(numeroTF.getText()+" existe déja!");
                                }else{
                                    Connecter c=null;
                                    try {
                                        c = new Connecter();
                                    } catch (ClassNotFoundException ex) {
                                        Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (SQLException ex) {
                                        Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                                       new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                                    }
                                    Requettes con = new Requettes(c);

                                       Client client = new Client();
                                       client.setNom(nomTF.getText().trim().toUpperCase());
                                       client.setPrenom(prenomsTF.getText().trim().toUpperCase());
                                       client.setNumTel(numeroTF.getText().trim());
                                       client.setDesc(raisonSTF.getText().trim());
                                       client.setDebit(debitR.getId());
                                       client.setCategorie(categorieR.getId());
                                       client.setEside(quatierTF.getText().trim().toUpperCase());
                                       client.setVilleOwner(villeOwn.getId());
                                       client.setStatus(statusCB.getValue().toString());
                                       LocalDate date = dateTF.getValue();
                                       client.setDateCreation(date.toString());
                                       client.setVille(villeR.getId());
                                       client.setEq(eqR.getIdEQ());
                                       client.setSecteur(sectR.getId());
                                       client.setCarte(carteR.getId());
                                       client.setPort(portR.getId());
                                       client.setEmail(emailTF.getText().trim());

                                       Historique hist = new Historique(date.toString(),"RAS",client.getIntStatut());
                                       int idh=0;

                                       try { 
                                           int id = con.fInsertClient(client);
                                           idh = con.fInsertHistorique(id,hist );
                                           portR.setStatus(1);
                                           con.fUpdateSatutPort(portR);
                                           c.getConnexion().close();
                                           client.setId(id);
                                           
                                            hist.setId(idh);
                                            Hashtable tables = dao.getClientHistorique();
                                            if(tables.get(client.getId())!=null){
                                                ArrayList fhistoriques = (ArrayList) tables.get(client.getId());
                                                fhistoriques.add(hist);
                                            }else{
                                                ArrayList fhistoriques = new ArrayList();
                                                fhistoriques.add(hist);
                                                tables.put(client.getId(), fhistoriques);
                                            }

                                           // ajout de l'objet client à la liste des clients de la ville
                                           dao.getClient().put(id, client);
                                           ArrayList clients = (ArrayList) dao.getClientVille().get(villeOwn.getId());
                                           if(clients!=null){
                                                clients.add(id);
                                           }else{
                                               //cas premier client d'une ville
                                               clients = new ArrayList();
                                               clients.add(id);
                                               dao.getClientVille().put(villeOwn.getId(),clients);
                                           }
                                           
                                           dao.getPortClient().put(client.getPort(), id);
                                           dao.getNomClient().add(client.getNom()+" "+client.getPrenom());
                                           dao.getNumero().put(client.getNumTel(),id);

                                           actionTarget.setText("Nouveau client ajouté.");

                                           //gridR.getChildren().remove(dao.getTreeTCL());
                                           dao.treeTableViewClient(dao, paneR, labelT, gridR);
                                           nomTF.setText("N/A");
                                           prenomsTF.setText("N/A");
                                           numeroTF.setText("");
                                           raisonSTF.setText("N/A");
                                           emailTF.setText("N/A");
                                           



                                       } catch (ClassNotFoundException ex) {
                                           Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                                           actionTarget.setText("Le réseau n'est pas accessible.");
                                           new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                                       } catch (SQLException ex) {
                                           Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                                           actionTarget.setText("Echec d'insertion.");
                                       } catch (ParseException ex) {
                                           Logger.getLogger(ModalAddClient.class.getName()).log(Level.SEVERE, null, ex);
                                           actionTarget.setText("Echec d'insertion.");
                                       }
                                }
                            
                            }
                        }else{
                            actionTarget.setText(email.getText()+" Champ Vide");
                        }
                        
                    }
                }
            });
        

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(resetbt,validerbt,btnRech);
        grid.add(hbBtn, 1, 15);
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(grid);
        sp.setPadding(new Insets(10, 10, 10, 10));
                
        scene = new Scene(sp, largeur, hauteur);
        scene.getStylesheets().add("css/style.css");
               
                
                
        stage.setScene(scene);
        stage.setTitle("Ajouter un Client");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(
            ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
}
