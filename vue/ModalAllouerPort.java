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
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
public class ModalAllouerPort {
    
    private final ActionEvent event;
    Scene scene;
    private DAOequipement dao;
    private Port port;
    private Client client;
    private GridPane griduser;
    private Image images;
    private ImageView pics;
    
    public ModalAllouerPort(DAOequipement dao,ActionEvent event,Port port,GridPane grid){
        this.event = event;
        this.dao = dao;
        this.port = port;
        this.griduser = grid;
        this.client = null;
    }
    
    public void construire(){
    
        Stage stage = new Stage();
        GridPane grid = new GridPane ();
        grid.setAlignment(Pos.CENTER);

        grid.setPadding(new Insets (10,10,10,10));
        grid.setVgap(7);
        grid.setHgap(7);
        
        TextField rechercheTF = new TextField();
        rechercheTF.promptTextProperty().set("Numero de telephone");
        grid.add(rechercheTF, 0, 0);
        
         //---Button rechercher client
        Button rechercherBt = new Button("ⵕ");
        rechercherBt.getStyleClass().add(dao.colornodanger());
        final Tooltip tooltipRech = new Tooltip();
        tooltipRech.setText("Rechercher un Client par son numero de telephone");
        rechercherBt.setTooltip(tooltipRech);
        grid.add(rechercherBt, 1, 0);

        Label infoclt = new Label ("CLIENT");
        infoclt.setTextFill(Color.web(dao.couleur));
        infoclt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(infoclt, 1, 3);

        Label noms = new Label ("NOMS :");
        grid.add(noms, 0, 5);
        TextField nomTF = new TextField();
        nomTF.setDisable(true);
        grid.add(nomTF, 1, 5);

        Label prenoms = new Label("PRENOMS :");
        grid.add(prenoms, 0, 6);
        TextField prenomsTF = new TextField();
        prenomsTF.setDisable(true);
        grid.add(prenomsTF, 1, 6);

        Label numero = new Label("NUMERO TELEPHONE :");
        grid.add(numero, 0, 7);
        TextField numeroTF = new TextField();
        numeroTF.setDisable(true);
        grid.add(numeroTF, 1, 7);
        
        Label debit = new Label("Debit :");
        grid.add(debit, 0, 8);
        
        //---- combo Box debit

        ObservableList<String> optionsDebit = FXCollections.observableArrayList();
        ComboBox debitCB = new ComboBox<>(optionsDebit);
        debitCB.getStyleClass().add(dao.comboColor);
        debitCB.setDisable(true);
        
        
        
        
        
        HBox hbDeb = new HBox();
        hbDeb.getChildren().addAll(debitCB);
        
        grid.add(hbDeb, 1, 8);
        
        Label categorie = new Label("Catégorie :");
        grid.add(categorie, 0, 9);
        
        //---- combo Box categorie

        ObservableList<String> optionsCat = FXCollections.observableArrayList();
       
        
        ComboBox categorieCB = new ComboBox<>(optionsCat);
        categorieCB.getStyleClass().add(dao.comboColor);
        categorieCB.setDisable(true);
        
        
        
        
        HBox hbCat = new HBox();
        hbCat.getChildren().addAll(categorieCB);
        
        grid.add(hbCat, 1, 9);
        

        Label raisonsocial = new Label("Raison Sociale:");
        grid.add(raisonsocial, 0, 10);
        TextField raisonSTF = new TextField();
        raisonSTF.setDisable(true);
        grid.add(raisonSTF, 1, 10);

        Label ville = new Label ("Ville:");
        grid.add(ville, 0, 11);
        
        
        //---- combo Box villes

        ObservableList<String> options = FXCollections.observableArrayList();
        
        ComboBox villeCB = new ComboBox<>(options) ;
        villeCB.getStyleClass().add(dao.comboColor);
        
        
        
        
        
        ObservableList<String> optionsSect = FXCollections.observableArrayList();
        Label secteur = new Label ("SECTEUR :");
        grid.add(secteur, 4, 6);
        ComboBox secteurCB = new ComboBox<>(optionsSect);
        secteurCB.getStyleClass().add(dao.comboColor);
        secteurCB.setDisable(true);
        grid.add(secteurCB, 5, 6);
       
        
        villeCB.setPromptText("Villes");
        villeCB.setDisable(true);
        grid.add(villeCB, 1, 11);

        Label quatier = new Label ("E-Side:") ;
        grid.add(quatier, 0, 12);
        TextField quatierTF = new TextField ();
        quatierTF.setDisable(true);
        grid.add(quatierTF, 1, 12);

        Label status = new Label ("Status:");
        grid.add(status, 0, 13);
        ComboBox statusCB = new ComboBox<>() ;
        statusCB.getStyleClass().add(dao.comboColor);
        statusCB.setDisable(true);
        statusCB.getItems().addAll("OK" ,"SUSPENDU");
        grid.add(statusCB, 1, 13);

        Label date = new Label ("Date de Creation:");
        grid.add(date, 0, 14);
        TextField dateTF = new TextField ();
        dateTF.setDisable(true);
        grid.add(dateTF, 1, 14);

        //For info equipement

        Label equipements = new Label ("Equipement:");
        grid.add(equipements,4 ,7 );
        ObservableList<String> optionsEQ = FXCollections.observableArrayList();
        ComboBox eqCB = new ComboBox<>(optionsEQ);
        eqCB.getStyleClass().add(dao.comboColor);
        eqCB.setDisable(true);
        
        grid.add(eqCB, 5, 7);

        Label carteL = new Label ("Carte:");
        grid.add(carteL, 4, 8);
        ObservableList<String> optionsCarte = FXCollections.observableArrayList();
        ComboBox carteCB = new ComboBox<>(optionsCarte);
        carteCB.getStyleClass().add(dao.comboColor);
        carteCB.setDisable(true);
        
        grid.add(carteCB, 5, 8);


        Label portL = new Label("Port:");
        grid.add(portL, 4, 9);
        ObservableList<String> optionsPort = FXCollections.observableArrayList();
        ComboBox portCB = new ComboBox<>(optionsPort);
        portCB.getStyleClass().add(dao.comboColor);
        portCB.setDisable(true);
        grid.add(portCB, 5, 9);
        
        
        Label actionTarget = new Label("");
        grid.add(actionTarget, 5, 11);
        
        //--equipement
        Label Ville = new Label ("Ville:");
        grid.add(Ville, 4, 5);
        ComboBox VilleEQCB = new ComboBox<>(options);
        VilleEQCB.getStyleClass().add(dao.comboColor);
        grid.add(VilleEQCB,5 ,5 );
        VilleEQCB.setDisable(true);
        
        
        
        
        
        

        Label infoEQ = new Label("EQUIPEMENT");
        infoEQ.setTextFill(Color.web(dao.colordeGestionEq()));
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

        Button validerbt = new Button ("Allouer");
        validerbt.setDisable(true);
        validerbt.getStyleClass().add(dao.colornodanger());
        
        //---Button rechercher Action
        rechercherBt.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               
               Hashtable numeros = dao.getNumero();
               if(numeros.containsKey(rechercheTF.getText())){
                   int id = (int) numeros.get(rechercheTF.getText());
                   client = (Client) dao.getClient().get(id);
                   nomTF.setText(client.getNom());
                   prenomsTF.setText(client.getPrenom());
                   numeroTF.setText(client.getNumTel());
                   
                   Debit debit = (Debit) dao.getDebit().get(client.getDebit());
                   debitCB.setPromptText(debit.getNom());
                   
                   Categorie cat = (Categorie) dao.getCategorieForfait().get(client.getCategorie());
                   categorieCB.setPromptText(cat.getNom());
                   
                   raisonSTF.setText(client.getDesc());
                   
                   Ville ville = (Ville) dao.getVille().get(client.getVilleOwner());
                   villeCB.setPromptText(ville.getNom());
                   
                   quatierTF.setText(client.getEside());
                   
                   statusCB.setPromptText(client.getStatut());
                   
                   dateTF.setText(client.getDateCreation());
                   
                   Secteur sect = (Secteur) dao.getSecteur().get(client.getSecteur());
                   if(sect!=null){
                        secteurCB.setPromptText(sect.getNom());
                   }
                   Equipement eq = (Equipement) dao.getEquipement().get(client.getEq());
                   if(eq!=null){
                        eqCB.setPromptText(eq.getNom());
                   }
                   Carte carteU = (Carte) dao.getCarte().get(client.getCarte());
                   if(carteU!=null){
                        carteCB.setPromptText(""+carteU.getNumCarte());
                   }
                   Port portU = (Port) dao.getPort().get(client.getPort());
                   if(portU!=null){
                        portCB.setPromptText(""+portU.getNumeroPort());
                   }
                   Ville villeEq = (Ville) dao.getVille().get(client.getVille());
                   if(villeEq!=null){
                        VilleEQCB.setPromptText(villeEq.getNom());
                   }
                   validerbt.setDisable(false);
               }
               
                
            }
        });
        
        validerbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                        
                    Connecter c=null;
                    try {
                        c = new Connecter();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalAllouerPort.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ModalAllouerPort.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Requettes con = new Requettes(c);
                    
                       Port oldPort = (Port) dao.getPort().get(client.getPort());
                       client.setPort(port.getId());
                       port.setStatus(port.occupe());
                       
                       if(oldPort!=null){
                            oldPort.setStatus(oldPort.librerer());
                       }
                       client.setCarte(port.getIdCarte());
                       Carte carte = (Carte) dao.getCarte().get(port.getIdCarte());
                       client.setEq(carte.getIdEQ());
                       Equipement eq = (Equipement) dao.getEquipement().get(carte.getIdEQ());
                       client.setEq(eq.getIdEQ());
                       Secteur set = (Secteur) dao.getSecteur().get(eq.getIdSecteur());
                       client.setSecteur(set.getId());
                       client.setVille(set.getIdVille());
                       
                       //changement du status port de l'ancien client proprietaire
                       /*int oldIdclient = (Integer) dao.getPortClient().get(port.getId());
                       Client oldclient = (Client) dao.getClient().get(oldIdclient);
                       if(oldclient!=null){
                            oldclient.setPort(oldPort.librerer());
                       }*/
                       

                       try { 
                           con.fUpdateClient(client);
                           con.fUpdateSatutPort(port);
                           if(oldPort!=null){
                                con.fUpdateSatutPort(oldPort);
                           }
                           c.getConnexion().close();

                           // ajournement de la table de hachage port(objet) carte(hashtable de port)
                           
                           Port newPort = (Port) dao.getPort().get(client.getPort());
                           newPort.setStatus(newPort.occupe());
                           
                           if(oldPort!=null){
                                
                                Port oldP = (Port) dao.getPort().get(oldPort.getId());
                                oldP.setStatus(oldP.librerer());
                                dao.getPortClient().remove(oldP.getId());
                           }
                           
                           
                           //ajout du nouveau client(objet) au port (portclient objet)
                           dao.getPortClient().put(port.getId(), client.getId());
                           
                           
                           griduser.getChildren().clear();
                           dao.portOccupeVue(dao, port, client, griduser);
                           actionTarget.setText("Nouveau client allouer au port "+port.getNumeroPort());
                           stage.close();
                       } catch (SQLException ex) {
                           Logger.getLogger(ModalAllouerPort.class.getName()).log(Level.SEVERE, null, ex);
                           actionTarget.setText("probleme connection.");
                       } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalAllouerPort.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(ModalAllouerPort.class.getName()).log(Level.SEVERE, null, ex);
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
                
        scene = new Scene(sp, (dao.width*90)/100, (dao.height*70)/100);
        scene.getStylesheets().add("css/style.css");        
               
                
                
        stage.setScene(scene);
        stage.setTitle("Allouer un Port");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(
            ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
}
