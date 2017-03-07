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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.util.Pair;
import jssh.ModalSsh;
import modele.Carte;
import modele.Categorie;
import modele.Client;
import modele.Debit;
import modele.Equipement;
import modele.Notification;
import modele.Port;
import modele.Secteur;
import modele.User;
import modele.Ville;
import notifier.Notifier;
import org.controlsfx.control.PopOver;
import telnet.ModalTelnet;

/**
 *
 * @author ghomsi
 */
public class FonctionVue {
    
    
    TreeItem<Client> root;
    TreeItem<Equipement> rootE;
    TreeItem<User> rootU;
    TreeTableView<Client> treeTableViewClient;
    TreeTableView<Equipement> treeTableViewE;
    TreeTableView<User> treeTableViewU;
    
    public String couleur="#CCCCCC";
    public String boutonColor="#CCCCCC";
    public String comboColor="#CCCCCC";
    
    private User touser = null;
    
    
    
    public TreeTableView<Client> getTreeTCL(){
        return treeTableViewClient;
    }
    public void setTreeTCL(TreeTableView<Client> tCL){
        treeTableViewClient= tCL;
    }
    
    public TreeTableView<User> getTreeTU(){
        return treeTableViewU;
    }
    public void setTreeTU(TreeTableView<User> tU){
        treeTableViewU= tU;
    }
    
    public TreeTableView<Equipement> getTreeTE(){
        return treeTableViewE;
    }
    public void setTreeTE(TreeTableView<Equipement> tE){
        treeTableViewE= tE;
    }
    
    /*******************Fonction de construction graphique***************************/
   public void allouerPortVue(DAOequipement dao,Port elt,GridPane griduser){
                
                Button btn = new Button("allouer");
                btn.getStyleClass().add("buttonGestEq");
                btn.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            ModalAllouerPort modalPort = new ModalAllouerPort(dao,t,elt,griduser);
                            modalPort.construire();
                        }
                });
                
                
                griduser.setAlignment(Pos.CENTER);
                griduser.setHgap(10);
                griduser.setVgap(10);
                griduser.setPadding(new Insets(25, 25, 25, 25));
                
                Label statusPort = new Label(elt.giveStatus());
                statusPort.setTextFill(Color.web(elt.giveColor()));
                statusPort.getStyleClass().add("gridfont");
                griduser.add(statusPort, 0, 0);
                if(elt.getStatut()!=2){
                    HBox hb = new HBox(5);
                    hb.getChildren().addAll(btn,checKandSynchro(dao,elt,2));
                    griduser.add(hb,1,0);
                }
                
   }
   
   public void portOccupeVue(DAOequipement dao,Port elt,Client client,GridPane griduser){
       
                Image images = new Image(getClass().getResourceAsStream("/images/user.png"),100,100,false,false);
                
                griduser.setAlignment(Pos.CENTER);
                griduser.setHgap(10);
                griduser.setVgap(10);
                griduser.setPadding(new Insets(25, 25, 25, 25));
                Label statusPort = new Label(elt.giveStatus());
                statusPort.setTextFill(Color.web(elt.giveColor()));
                statusPort.getStyleClass().add("gridfont");
                griduser.add(statusPort, 1, 3);
                
                griduser.add(new Label("Debit: "), 1, 1);
                
                Debit debit = (Debit) dao.getDebit().get(client.getDebit());
                Label debL = new Label(debit.getNom()+"("+debit.getprofil()+")");
                debL.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
                griduser.add(debL, 2, 1);
                griduser.add(new Label("Status: "), 1, 2);
                Label statusL = new Label(client.getStatut());
                statusL.setTextFill(Color.web(client.givestatusColor()));        
                griduser.add(statusL, 2, 2);
                
                ImageView pics = new ImageView(images);
                griduser.add(pics,0,0);

                griduser.add(new Label("Tel: "), 1, 0);
                Label dat = new Label(client.getNumTel());
                dat.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
                griduser.add(dat, 2, 0);

                griduser.add(new Label("Date: "), 0, 1);

                final DatePicker datePicker = new DatePicker();
                datePicker.getStyleClass().add("date");
                datePicker.promptTextProperty().set(client.getDateCreation());
                datePicker.setEditable(false);
                griduser.add(datePicker, 0, 2);
            
                
                //Button btn = new Button("Liberer");
                //btn.getStyleClass().add("buttonGestEq");
                Button btn2 = new Button("Basculer");
                btn2.getStyleClass().add("buttonGestEq");
                Button btn3 = new Button("Details");
                btn3.getStyleClass().add("buttonGestEq");

                  /*  btn.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            System.out.println("Désalouer");
                        }
                    });*/

                    btn2.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            ModalPaneChangePortClient modalPort = new ModalPaneChangePortClient(dao,t,client,griduser);
                            modalPort.construire();
                        }
                    });

                    btn3.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            ModalPaneDetailClient modalClient = new ModalPaneDetailClient(dao,t,client);
                            modalClient.construire();
                        }
                    });
                
              
                
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().addAll(btn2,btn3);
                griduser.add(hbBtn, 0, 4);
                griduser.add(checKandSynchro(dao,elt,1,client), 0, 0);
   } 
   
  
   
   public void treeTableViewClient(DAOequipement dao,VBox pane,Label label,GridPane grid){
        
        //grid.getChildren().remove(dao.getTreeTCL());
       
        ImageView depIcon = new ImageView (
             new Image(getClass().getResourceAsStream("/images/secteur.png"))
        );
        Client cltR = new Client();
        cltR.setNom("Villes("+dao.getClient().size()+")");
        TreeItem<Client> rootR =  new TreeItem<>(cltR, depIcon);
        root =  new TreeItem<>(cltR);
        root.setExpanded(true);
           
        Hashtable villes = dao.getVille();
        Enumeration e = villes.elements();
        while(e.hasMoreElements()){
            Ville elt = (Ville) e.nextElement();
            
            //Adding tree items to the root
            ArrayList clients = (ArrayList) dao.getClientVille().get(elt.getId());
            
            Client clt1 = new Client();
            clt1.setNom(elt.getNom()+"("+clients.size()+")");
            elt.setNbClients(clients.size());
            clt1.setVilleOwner(-1);
            clt1.setId(elt.getId());
            
            TreeItem<Client>root1 =  new TreeItem<>(clt1);
            root1.setExpanded(false); 
            
            
            if(clients!=null){
                
                Iterator<Integer> it = clients.iterator();
                while(it.hasNext()){
                    int id = it.next();
                    Client eq = (Client) dao.getClient().get(id);
                    root1.getChildren().add(new TreeItem<>(eq));
                }
            }
            root = root1;
            rootR.getChildren().add(root1);
        }
        
        
    
        
        //Creating a column
        TreeTableColumn<Client,String> sectColumn = new TreeTableColumn<>("Villes");
        sectColumn.setMinWidth(200);
        
        //Defining cell content
        sectColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<Client, String> p) -> 
            new ReadOnlyStringWrapper(p.getValue().getValue().getNom()+" "+p.getValue().getValue().getPrenom())
        );
        //Creating a tree table view
        treeTableViewClient = new TreeTableView<>(rootR);
        treeTableViewClient.getStyleClass().add("treetableviewcl");
        treeTableViewClient.getColumns().add(sectColumn);
        treeTableViewClient.setMinWidth(150);
        treeTableViewClient.setShowRoot(true);
        
         //Add change listener
        treeTableViewClient.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (treeTableViewClient.getSelectionModel().getSelectedItem() != null) {
                
                
              
                
                //-- test si on un objet Ville
                if(!treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getNom().isEmpty() && treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getVilleOwner()==-1){
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneClientVille(treeTableViewClient.getSelectionModel().getSelectedItem().getValue(),dao).construire());
                }
                
                
                if(!treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getNom().isEmpty() && !treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getPrenom().isEmpty()){
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneClientView(dao,treeTableViewClient.getSelectionModel().getSelectedItem().getValue(),label,grid,pane).construire());
                }
                
                label.setText(treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getNom()+" "+treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getPrenom());
                 
            }
        });
        
         grid.add(treeTableViewClient,0,1);
   
   }
   
   public void treeTableViewClient(DAOequipement dao,VBox pane,Label label,GridPane grid,Ville elt){
        
        //grid.getChildren().remove(dao.getTreeTCL());
       
        ImageView depIcon = new ImageView (
             new Image(getClass().getResourceAsStream("/images/secteur.png"))
        );
        Client cltR = new Client();
        cltR.setNom("Villes");
        TreeItem<Client> rootR =  new TreeItem<>(cltR, depIcon);
        rootR.setExpanded(true);
        root =  new TreeItem<>(cltR);
        //root.setExpanded(true);
           
        
            //Adding tree items to the root
            ArrayList clients = (ArrayList) dao.getClientVille().get(elt.getId());
            
            Client clt1 = new Client();
            clt1.setNom(elt.getNom()+"("+clients.size()+")");
            elt.setNbClients(clients.size());
            clt1.setVilleOwner(-1);
            clt1.setId(elt.getId());
            
            
            TreeItem<Client>root1 =  new TreeItem<>(clt1);
            root1.setExpanded(true); 
            
            
            if(clients!=null){
                
                Iterator<Integer> it = clients.iterator();
                while(it.hasNext()){
                    int id = it.next();
                    Client eq = (Client) dao.getClient().get(id);
                    root1.getChildren().add(new TreeItem<>(eq));
                }
            }
            root = root1;
            rootR.getChildren().add(root1);
        
        
        
    
        
        //Creating a column
        TreeTableColumn<Client,String> sectColumn = new TreeTableColumn<>("Villes");
        sectColumn.setMinWidth(200);
        
        //Defining cell content
        sectColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<Client, String> p) -> 
            new ReadOnlyStringWrapper(p.getValue().getValue().getNom()+" "+p.getValue().getValue().getPrenom())
        );
        //Creating a tree table view
        treeTableViewClient = new TreeTableView<>(rootR);
        treeTableViewClient.getStyleClass().add("treetableviewcl");
        treeTableViewClient.getColumns().add(sectColumn);
        treeTableViewClient.setMinWidth(150);
        treeTableViewClient.setShowRoot(true);
        
         //Add change listener
        treeTableViewClient.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (treeTableViewClient.getSelectionModel().getSelectedItem() != null) {
                
                
              
                
                //-- test si on un objet Ville
                if(!treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getNom().isEmpty() && treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getVilleOwner()==-1){
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneClientVille(treeTableViewClient.getSelectionModel().getSelectedItem().getValue(),dao).construire());
                }
                
                
                if(!treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getNom().isEmpty() && !treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getPrenom().isEmpty()){
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneClientView(dao,treeTableViewClient.getSelectionModel().getSelectedItem().getValue(),label,grid,pane).construire());
                }
                
                label.setText(treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getNom()+" "+treeTableViewClient.getSelectionModel().getSelectedItem().getValue().getPrenom());
                 
            }
        });
        
         grid.add(treeTableViewClient,0,1);
   
   }
   
   


public void gridClientVue(DAOequipement dao,GridPane grid,Client client){
    
    grid.add(new Label("Description/raison social: "), 0, 0);  
        grid.add(new Label("Nom: "), 0, 1);
        grid.add(new Label("Prenom: "), 0, 2);
        Label attach = new Label("ᚄ Attachement: ");
        attach.setTextFill(Color.web(dao.couleur));
        attach.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(attach, 0, 3);
        grid.add(new Label("Ville: "), 0, 4);
        grid.add(new Label("Secteur: "), 0, 5);
        grid.add(new Label("Equipement: "), 0, 6);
        grid.add(new Label("Carte: "), 0, 7);
        grid.add(new Label("Port: "), 0, 8);
        
        grid.add(new Label("Numero Telephone: "), 3, 1);
        grid.add(new Label("Debit: "), 3, 2);
        grid.add(new Label("E-side: "), 3, 3);
        grid.add(new Label("Status: "), 3, 4);
        grid.add(new Label("Date de Creation: "), 3, 5);
        grid.add(new Label("Ville de résidance: "), 3, 6);
        grid.add(new Label("Categorie: "), 3, 7);
        grid.add(new Label("Email: "), 3, 8);
        
        final Label labelNom = new Label(client.getNom());
        grid.add(labelNom,1, 1);
        final Label labelPrenom = new Label(client.getPrenom());
        grid.add(labelPrenom,1, 2);
        
        Ville ville = (Ville) dao.getVille().get(client.getVille());
        if(ville!=null){
            final Label label = new Label(ville.getNom());
            label.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            grid.add(label,1, 4);
        }else{
            final Label label = new Label("N/A");
            label.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            label.getStyleClass().add(dao.colordanger());
            grid.add(label,1, 4);
        }
        
        
        
        Secteur sect = (Secteur) dao.getSecteur().get(client.getSecteur());
        if(sect!=null){
            final Label label1 = new Label(sect.getNom());
            label1.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            grid.add(label1,1, 5);
        }else{
            final Label label1 = new Label("N/A");
            label1.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            label1.getStyleClass().add(dao.colordanger());
            grid.add(label1,1, 5);
        }
        
        Equipement equip = (Equipement) dao.getEquipement().get(client.getEq());
        if(equip!=null){
            final Label label1 = new Label(equip.getNom());
            label1.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            grid.add(label1,1, 6);
        }else{
            final Label label1 = new Label("N/A");
            label1.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            label1.getStyleClass().add(dao.colordanger());
            grid.add(label1,1, 6);
        }
        
        
        Carte carte = (Carte) dao.getCarte().get(client.getCarte());
        if(carte!=null){
            final Label label2 = new Label(""+carte.getNumCarte());
            label2.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            grid.add(label2,1, 7);
        }else{
            final Label label2 = new Label("N/A");
            label2.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            label2.getStyleClass().add(dao.colordanger());
            grid.add(label2,1, 7);
        }
        Port port = (Port) dao.getPort().get(client.getPort());
        if(port!=null){
            final Label label3 = new Label(""+port.getNumeroPort());
            label3.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            grid.add(label3,1, 8);
        }else{
            final Label label3 = new Label("N/A");
            label3.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            label3.getStyleClass().add(dao.colordanger());
            grid.add(label3,1, 8);
        
        }
        final TextArea textArea = new TextArea(client.getDesc());
        textArea.getStyleClass().add("textArea");
        textArea.setEditable(false);
        textArea.prefWidthProperty().set(150);
        textArea.prefHeightProperty().set(50);
        grid.add(textArea,1, 0);
        
        final Label label6 = new Label(client.getNumTel());
        label6.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(label6,4, 1);
        
        Debit debit = (Debit) dao.getDebit().get(client.getDebit());
        final Label label7 = new Label(debit.getNom());
        label7.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(label7,4, 2);
        
        final Label label8 = new Label(client.getEside());
        grid.add(label8,4, 3);
        
        String statustexte=client.getStatut();
        if(client.isAsuspendre())
            statustexte +="/à suspendre";
        if(client.isValider())
            statustexte +="/suspension valider";
        
        final Label label9 = new Label(statustexte);
        dao.labelclientstatut=label9;
        label9.setTextFill(Color.web(client.givestatusColor()));
        label9.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(label9,4, 4);
        
        final Label label10 = new Label(client.getDateCreation());
       
        grid.add(label10,4, 5);
        
        Ville villeOwn = (Ville) dao.getVille().get(client.getVilleOwner());
        final Label label11 = new Label(villeOwn.getNom());
        grid.add(label11,4, 6);
        
        Categorie catF = (Categorie) dao.getCategorieForfait().get(client.getCategorie());
        final Label label12 = new Label(catF.getNom());
        label12.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(label12,4, 7);
        
        final Label emailL = new Label();
        if(client.getEmail()==null || client.getEmail().isEmpty()){
            emailL.setText("N/A");
        }else{
            emailL.setText(client.getEmail());
        }
        emailL.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(emailL,4, 8);
        
}   

public void treetableviewEq(DAOequipement dao,VBox pane,Label label,HBox HBlabel,GridPane grid,String css){
        
        ImageView depIcon = new ImageView (
             new Image(getClass().getResourceAsStream("/images/secteur.png"))
        );
        rootE =  new TreeItem<>(new Equipement("villes"), depIcon);
        rootE.setExpanded(true);
        Hashtable villes = dao.getVille();
        Enumeration e = villes.elements();
        while(e.hasMoreElements()){
            Ville elt = (Ville) e.nextElement();
            Equipement eqmt = new Equipement(elt.getNom());
            eqmt.setVille(elt.getNom());
            eqmt.setTmpNbEq(elt.getNbEq());
            eqmt.setTmpNbCarte(elt.getNbCarte());
            eqmt.setTmpNbPort(elt.getNbPort());
            eqmt.setTmpNbPortLibre(elt.getNbPortLibre());
            eqmt.setTmpNbPortOccupe(elt.getNbPortOccupe());
            eqmt.setTmpNbPortDefect(elt.getNbPortDefect());
            
            final TreeItem<Equipement>root1 =  new TreeItem<>(eqmt);
            root1.setExpanded(false); 
            ArrayList secteurs = (ArrayList) dao.getSecteurVille().get(elt.getId());
            if(secteurs!=null && !secteurs.isEmpty()){
                Iterator<Integer> it_sect = secteurs.iterator();
                while(it_sect.hasNext()){
                    int id_sect = it_sect.next();
                    Secteur sect =(Secteur) dao.getSecteur().get(id_sect);    
                    Equipement eqmt2 = new Equipement(sect.getNom());
                    eqmt2.setSecteur(sect.getNom());
                    eqmt2.setTmpNbEq(sect.getNbEq());
                    eqmt2.setTmpNbCarte(sect.getNbCarte());
                    eqmt2.setTmpNbPort(sect.getNbPort());
                    eqmt2.setTmpNbPortLibre(sect.getNbPortLibre());
                    eqmt2.setTmpNbPortOccupe(sect.getNbPortOccupe());
                    eqmt2.setTmpNbPortDefect(sect.getNbPortDefect());

                    final TreeItem<Equipement>root2 =  new TreeItem<>(eqmt2);

                    //Creating the root element
                    root2.setExpanded(false);   

                    //Adding tree items to the root
                    ArrayList equipements = (ArrayList) dao.getEqSecteur().get(sect.getId());
                    if(equipements!=null){
                        Iterator<Integer> it_eq = equipements.iterator();
                        while(it_eq.hasNext()){
                            int id_eq = it_eq.next();
                            Equipement eq = (Equipement) dao.getEquipement().get(id_eq);
                            root2.getChildren().add(new TreeItem<>(eq));
                        };
                    }
                    root1.getChildren().add(root2);
                
                }
                
                rootE.getChildren().add(root1);
            }
        
        }
        
        //Creating a column
        TreeTableColumn<Equipement,String> sectColumn = new TreeTableColumn<>("Equipements");
        sectColumn.setMinWidth(200);

        //Defining cell content
        sectColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<Equipement, String> p) -> 
            new ReadOnlyStringWrapper(p.getValue().getValue().getNom())
        );
        //Creating a tree table view
        treeTableViewE = new TreeTableView<>(rootE);
        treeTableViewE.getColumns().add(sectColumn);
        treeTableViewE.setMinWidth(150);
        treeTableViewE.setShowRoot(true);

         //Add change listener
        treeTableViewE.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (treeTableViewE.getSelectionModel().getSelectedItem() != null) {
               
                
               
                //-- test si on un objet Ville
                if(!treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    HBlabel.getChildren().clear();
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneVille(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue()).construire());
                }
                
                //--test si on un objet secteur
                if(treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && !treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    HBlabel.getChildren().clear();
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneSecteur(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue()).construire());
                }
                
                if(!treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && !treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneCarte(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue(),HBlabel).construire());
                }
                


                label.setText(treeTableViewE.getSelectionModel().getSelectedItem().getValue().getNom());


            }
        });
        treeTableViewE.getStyleClass().add(css);
        grid.add(treeTableViewE, 0, 1);
}

public void treetableviewEq(DAOequipement dao,VBox pane,Label label,HBox HBlabel,GridPane grid,String css,Ville elt){
        
        ImageView depIcon = new ImageView (
             new Image(getClass().getResourceAsStream("/images/secteur.png"))
        );
        rootE =  new TreeItem<>(new Equipement("villes"), depIcon);
        rootE.setExpanded(true);
            Equipement eqmt = new Equipement(elt.getNom());
            eqmt.setVille(elt.getNom());
            eqmt.setTmpNbEq(elt.getNbEq());
            eqmt.setTmpNbCarte(elt.getNbCarte());
            eqmt.setTmpNbPort(elt.getNbPort());
            eqmt.setTmpNbPortLibre(elt.getNbPortLibre());
            eqmt.setTmpNbPortOccupe(elt.getNbPortOccupe());
            eqmt.setTmpNbPortDefect(elt.getNbPortDefect());
            
            final TreeItem<Equipement>root1 =  new TreeItem<>(eqmt);
            root1.setExpanded(false); 
            ArrayList secteurs = (ArrayList) dao.getSecteurVille().get(elt.getId());
            if(secteurs!=null && !secteurs.isEmpty()){
                Iterator<Integer> it_sect = secteurs.iterator();
                while(it_sect.hasNext()){
                    int id_sect = it_sect.next();
                    Secteur sect =(Secteur) dao.getSecteur().get(id_sect);    
                    Equipement eqmt2 = new Equipement(sect.getNom());
                    eqmt2.setSecteur(sect.getNom());
                    eqmt2.setTmpNbEq(sect.getNbEq());
                    eqmt2.setTmpNbCarte(sect.getNbCarte());
                    eqmt2.setTmpNbPort(sect.getNbPort());
                    eqmt2.setTmpNbPortLibre(sect.getNbPortLibre());
                    eqmt2.setTmpNbPortOccupe(sect.getNbPortOccupe());
                    eqmt2.setTmpNbPortDefect(sect.getNbPortDefect());

                    final TreeItem<Equipement>root2 =  new TreeItem<>(eqmt2);

                    //Creating the root element
                    root2.setExpanded(false);   

                    //Adding tree items to the root
                    ArrayList equipements = (ArrayList) dao.getEqSecteur().get(sect.getId());
                    if(equipements!=null){
                        Iterator<Integer> it_eq = equipements.iterator();
                        while(it_eq.hasNext()){
                            int id_eq = it_eq.next();
                            Equipement eq = (Equipement) dao.getEquipement().get(id_eq);
                            root2.getChildren().add(new TreeItem<>(eq));
                        };
                    }
                    root1.getChildren().add(root2);
                
                }
                
                rootE.getChildren().add(root1);
            
        
        }
        
        //Creating a column
        TreeTableColumn<Equipement,String> sectColumn = new TreeTableColumn<>("Equipements");
        sectColumn.setMinWidth(200);

        //Defining cell content
        sectColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<Equipement, String> p) -> 
            new ReadOnlyStringWrapper(p.getValue().getValue().getNom())
        );
        //Creating a tree table view
        treeTableViewE = new TreeTableView<>(rootE);
        treeTableViewE.getColumns().add(sectColumn);
        treeTableViewE.setMinWidth(150);
        treeTableViewE.setShowRoot(true);

         //Add change listener
        treeTableViewE.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (treeTableViewE.getSelectionModel().getSelectedItem() != null) {
               
                
               
                //-- test si on un objet Ville
                if(!treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    HBlabel.getChildren().clear();
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneVille(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue()).construire());
                }
                
                //--test si on un objet secteur
                if(treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && !treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    HBlabel.getChildren().clear();
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneSecteur(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue()).construire());
                }
                
                if(!treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && !treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneCarte(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue(),HBlabel).construire());
                }
                


                label.setText(treeTableViewE.getSelectionModel().getSelectedItem().getValue().getNom());


            }
        });
        treeTableViewE.getStyleClass().add(css);
        grid.add(treeTableViewE, 0, 1);
}

public void treetableviewAdminEq(DAOequipement dao,VBox pane,Label label,HBox HBlabel,GridPane grid,String css){
        ImageView depIcon = new ImageView (
             new Image(getClass().getResourceAsStream("/images/secteur.png"))
        );
        rootE =  new TreeItem<>(new Equipement("villes"), depIcon);
        rootE.setExpanded(true);
        Enumeration e = dao.getVille().elements();
        while(e.hasMoreElements()){
            Ville elt = (Ville) e.nextElement();
            Equipement eqmt = new Equipement(elt.getNom());
            eqmt.setIdEQ(elt.getId());
            eqmt.setVille(elt.getNom());
            eqmt.setTmpNbEq(elt.getNbEq());
            eqmt.setTmpNbCarte(elt.getNbCarte());
            eqmt.setTmpNbPort(elt.getNbPort());
            eqmt.setTmpNbPortLibre(elt.getNbPortLibre());
            eqmt.setTmpNbPortOccupe(elt.getNbPortOccupe());
            eqmt.setTmpNbPortDefect(elt.getNbPortDefect());
            
            final TreeItem<Equipement>root1 =  new TreeItem<>(eqmt);
            root1.setExpanded(false);
            
            ArrayList secteurs = (ArrayList) dao.getSecteurVille().get(elt.getId());
            
            if(secteurs!=null && !secteurs.isEmpty()){
                Iterator<Integer> it_sect = secteurs.iterator();
                while(it_sect.hasNext()){
                    int id_sect = it_sect.next();
                    Secteur sect =(Secteur) dao.getSecteur().get(id_sect);
                    Equipement eqmt2 = new Equipement(sect.getNom());
                    eqmt2.setIdEQ(sect.getId());
                    eqmt2.setIdVille(sect.getIdVille());
                    eqmt2.setSecteur(sect.getNom());

                    final TreeItem<Equipement>root2 =  new TreeItem<>(eqmt2, depIcon);

                    //Creating the root element
                    root2.setExpanded(false);   

                    //Adding tree items to the root
                    ArrayList equipements = (ArrayList) dao.getEqSecteur().get(sect.getId());
                    
                    if(equipements!=null){
                        Iterator<Integer> it_eq = equipements.iterator();
                        while(it_eq.hasNext()){
                            int id_eq = it_eq.next();
                            Equipement eq = (Equipement) dao.getEquipement().get(id_eq);
                            root2.getChildren().add(new TreeItem<>(eq));
                        };
                    }
                    root1.getChildren().add(root2);

                }
            
                rootE.getChildren().add(root1);
            }
            
             
        }
        //Creating a column
        TreeTableColumn<Equipement,String> sectColumn = new TreeTableColumn<>("Equipements");
        sectColumn.setMinWidth(200);
        
        //Defining cell content
        sectColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<Equipement, String> p) -> 
            new ReadOnlyStringWrapper(p.getValue().getValue().getNom())
        );
        //Creating a tree table view
        treeTableViewE = new TreeTableView<>(rootE);
        treeTableViewE.getColumns().add(sectColumn);
        treeTableViewE.setMinWidth(150);
        treeTableViewE.setShowRoot(true);
        
         //Add change listener
        treeTableViewE.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (treeTableViewE.getSelectionModel().getSelectedItem() != null) {
                
                
                                
                //-- test si on un objet Ville
                if(!treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    HBlabel.getChildren().clear();
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneAdminVille(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue(),label,HBlabel,grid,pane).construire());
                }
                
                //--test si on un objet secteur
                if(treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && !treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    HBlabel.getChildren().clear();
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneAdminSecteur(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue(),label,HBlabel,grid,pane).construire());
                }
                
                if(!treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && !treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneCarteAdmin(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue(),HBlabel,grid,label,pane).construire());
                    HBlabel.getChildren().addAll(new Label("Route:"),new Label(treeTableViewE.getSelectionModel().getSelectedItem().getValue().getRoute()));
                }
                
                label.setText(treeTableViewE.getSelectionModel().getSelectedItem().getValue().getNom());
                
                
                
            }
        });
        
        treeTableViewE.getStyleClass().add(css);
        grid.add(treeTableViewE, 0, 1);

}

public void treetableviewAdminEq(DAOequipement dao,VBox pane,Label label,HBox HBlabel,GridPane grid,String css,Ville elt){
        ImageView depIcon = new ImageView (
             new Image(getClass().getResourceAsStream("/images/secteur.png"))
        );
        rootE =  new TreeItem<>(new Equipement("villes"), depIcon);
        rootE.setExpanded(true);
            Equipement eqmt = new Equipement(elt.getNom());
            eqmt.setIdEQ(elt.getId());
            eqmt.setVille(elt.getNom());
            eqmt.setTmpNbEq(elt.getNbEq());
            eqmt.setTmpNbCarte(elt.getNbCarte());
            eqmt.setTmpNbPort(elt.getNbPort());
            eqmt.setTmpNbPortLibre(elt.getNbPortLibre());
            eqmt.setTmpNbPortOccupe(elt.getNbPortOccupe());
            eqmt.setTmpNbPortDefect(elt.getNbPortDefect());
            
            final TreeItem<Equipement>root1 =  new TreeItem<>(eqmt);
            root1.setExpanded(false);
            
            ArrayList secteurs = (ArrayList) dao.getSecteurVille().get(elt.getId());
            
            if(secteurs!=null && !secteurs.isEmpty()){
                Iterator<Integer> it_sect = secteurs.iterator();
                while(it_sect.hasNext()){
                    int id_sect = it_sect.next();
                    Secteur sect =(Secteur) dao.getSecteur().get(id_sect);
                    Equipement eqmt2 = new Equipement(sect.getNom());
                    eqmt2.setIdEQ(sect.getId());
                    eqmt2.setIdVille(sect.getIdVille());
                    eqmt2.setSecteur(sect.getNom());

                    final TreeItem<Equipement>root2 =  new TreeItem<>(eqmt2, depIcon);

                    //Creating the root element
                    root2.setExpanded(false);   

                    //Adding tree items to the root
                    ArrayList equipements = (ArrayList) dao.getEqSecteur().get(sect.getId());
                    
                    if(equipements!=null){
                        Iterator<Integer> it_eq = equipements.iterator();
                        while(it_eq.hasNext()){
                            int id_eq = it_eq.next();
                            Equipement eq = (Equipement) dao.getEquipement().get(id_eq);
                            root2.getChildren().add(new TreeItem<>(eq));
                        };
                    }
                    root1.getChildren().add(root2);

                }
            
            rootE.getChildren().add(root1);
           
        }
        //Creating a column
        TreeTableColumn<Equipement,String> sectColumn = new TreeTableColumn<>("Equipements");
        sectColumn.setMinWidth(200);
        
        //Defining cell content
        sectColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<Equipement, String> p) -> 
            new ReadOnlyStringWrapper(p.getValue().getValue().getNom())
        );
        //Creating a tree table view
        treeTableViewE = new TreeTableView<>(rootE);
        treeTableViewE.getColumns().add(sectColumn);
        treeTableViewE.setMinWidth(150);
        treeTableViewE.setShowRoot(true);
        
         //Add change listener
        treeTableViewE.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (treeTableViewE.getSelectionModel().getSelectedItem() != null) {
                
                
                                
                //-- test si on un objet Ville
                if(!treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    HBlabel.getChildren().clear();
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneAdminVille(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue(),label,HBlabel,grid,pane).construire());
                }
                
                //--test si on un objet secteur
                if(treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && !treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    HBlabel.getChildren().clear();
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneAdminSecteur(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue(),label,HBlabel,grid,pane).construire());
                }
                
                if(!treeTableViewE.getSelectionModel().getSelectedItem().getValue().getVille().isEmpty() && !treeTableViewE.getSelectionModel().getSelectedItem().getValue().getSecteur().isEmpty()){
                    pane.getChildren().clear();
                    pane.getChildren().addAll(new PaneCarteAdmin(dao,treeTableViewE.getSelectionModel().getSelectedItem().getValue(),HBlabel,grid,label,pane).construire());
                    HBlabel.getChildren().addAll(new Label("Route:"),new Label(treeTableViewE.getSelectionModel().getSelectedItem().getValue().getRoute()));
                }
                
                label.setText(treeTableViewE.getSelectionModel().getSelectedItem().getValue().getNom());
                
                
            }
        });
        
        treeTableViewE.getStyleClass().add(css);
        grid.add(treeTableViewE, 0, 1);

}
   
public void treeTableViewUser(DAOequipement dao,VBox pane,Label label,GridPane grid,String css){
        
        User usr = new User();
        usr.setNom("Villes");
        ImageView depIcon = new ImageView (
             new Image(getClass().getResourceAsStream("/images/usersmall.png"))
        );
        rootU =  new TreeItem<>(usr, depIcon);
        
        rootU.setExpanded(true);  
     
        //Adding tree items to the root
        Hashtable villes = dao.getVille();
        Enumeration e = villes.elements();
        while(e.hasMoreElements()){
            Ville elt = (Ville) e.nextElement();
            User usr1 = new User();
            usr1.setNom(elt.getNom());
            
            final TreeItem<User>root1 =  new TreeItem<>(usr1);
            root1.setExpanded(false); 
            //Adding tree items to the root
            ArrayList users = (ArrayList) dao.getUserVille().get(elt.getId());
            Iterator<Integer> it_user = users.iterator();
            while(it_user.hasNext()) {
                int id_user = it_user.next();
                User u = (User) dao.getUser().get(id_user);
                root1.getChildren().add(new TreeItem<>(u));
            }
            rootU.getChildren().add(root1);
        }
        
        //Creating a column
        TreeTableColumn<User,String> sectColumn = new TreeTableColumn<>("utilisateurs");
        sectColumn.setMinWidth(200);
        
        //Defining cell content
        sectColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<User, String> p) -> 
            new ReadOnlyStringWrapper(p.getValue().getValue().getNom()+" "+p.getValue().getValue().getPrenom())
        );
        //Creating a tree table view
        treeTableViewU = new TreeTableView<>(rootU);
        treeTableViewU.getColumns().add(sectColumn);
        treeTableViewU.setMinWidth(150);
        treeTableViewU.setShowRoot(true);
        treeTableViewU.getStyleClass().add(css);
        
         //Add change listener
        treeTableViewU.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (treeTableViewU.getSelectionModel().getSelectedItem() != null) {
                pane.getChildren().clear();
                
                //-- test si on un objet Ville
                if(!treeTableViewU.getSelectionModel().getSelectedItem().getValue().getNom().isEmpty() && treeTableViewU.getSelectionModel().getSelectedItem().getValue().getPrenom().isEmpty()){
                    
                    pane.getChildren().addAll(new PaneUserVille(dao,treeTableViewU.getSelectionModel().getSelectedItem().getValue()).construire());
                }
                
                
                if(!treeTableViewU.getSelectionModel().getSelectedItem().getValue().getNom().isEmpty() && !treeTableViewU.getSelectionModel().getSelectedItem().getValue().getPrenom().isEmpty()){
                    //System.out.println(villes.indexOf(treeTableView.getSelectionModel().getSelectedItem().getValue().getObjVille()));
                    pane.getChildren().addAll(new PaneUserView(dao,treeTableViewU.getSelectionModel().getSelectedItem().getValue(),label,grid,pane).construire());
                }
                
                
              
                
                label.setText(treeTableViewU.getSelectionModel().getSelectedItem().getValue().getNom()+" "+treeTableViewU.getSelectionModel().getSelectedItem().getValue().getPrenom());
                
                
            }
        });
        
        grid.add(treeTableViewU, 0, 1);
}

public void treeTableViewUser(DAOequipement dao,VBox pane,Label label,GridPane grid,String css,Ville elt){
        
        User usr = new User();
        usr.setNom("Villes");
        ImageView depIcon = new ImageView (
             new Image(getClass().getResourceAsStream("/images/usersmall.png"))
        );
        rootU =  new TreeItem<>(usr, depIcon);
        
        rootU.setExpanded(true);  
     
        //Adding tree items to the root
        Hashtable villes = dao.getVille();
        Enumeration e = villes.elements();
            User usr1 = new User();
            usr1.setNom(elt.getNom());
            
            final TreeItem<User>root1 =  new TreeItem<>(usr1);
            root1.setExpanded(false); 
            //Adding tree items to the root
            ArrayList users = (ArrayList) dao.getUserVille().get(elt.getId());
            Iterator<Integer> it_user = users.iterator();
            while(it_user.hasNext()) {
                int id_user = it_user.next();
                User u = (User) dao.getUser().get(id_user);
                root1.getChildren().add(new TreeItem<>(u));
            }
            rootU.getChildren().add(root1);
        
        
        //Creating a column
        TreeTableColumn<User,String> sectColumn = new TreeTableColumn<>("utilisateurs");
        sectColumn.setMinWidth(200);
        
        //Defining cell content
        sectColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<User, String> p) -> 
            new ReadOnlyStringWrapper(p.getValue().getValue().getNom()+" "+p.getValue().getValue().getPrenom())
        );
        //Creating a tree table view
        treeTableViewU = new TreeTableView<>(rootU);
        treeTableViewU.getColumns().add(sectColumn);
        treeTableViewU.setMinWidth(150);
        treeTableViewU.setShowRoot(true);
        treeTableViewU.getStyleClass().add(css);
        
         //Add change listener
        treeTableViewU.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (treeTableViewU.getSelectionModel().getSelectedItem() != null) {
                pane.getChildren().clear();
                
                //-- test si on un objet Ville
                if(!treeTableViewU.getSelectionModel().getSelectedItem().getValue().getNom().isEmpty() && treeTableViewU.getSelectionModel().getSelectedItem().getValue().getPrenom().isEmpty()){
                    
                    pane.getChildren().addAll(new PaneUserVille(dao,treeTableViewU.getSelectionModel().getSelectedItem().getValue()).construire());
                }
                
                
                if(!treeTableViewU.getSelectionModel().getSelectedItem().getValue().getNom().isEmpty() && !treeTableViewU.getSelectionModel().getSelectedItem().getValue().getPrenom().isEmpty()){
                    //System.out.println(villes.indexOf(treeTableView.getSelectionModel().getSelectedItem().getValue().getObjVille()));
                    pane.getChildren().addAll(new PaneUserView(dao,treeTableViewU.getSelectionModel().getSelectedItem().getValue(),label,grid,pane).construire());
                }
                
                
              
                
                label.setText(treeTableViewU.getSelectionModel().getSelectedItem().getValue().getNom()+" "+treeTableViewU.getSelectionModel().getSelectedItem().getValue().getPrenom());
                
                
            }
        });
        
        grid.add(treeTableViewU, 0, 1);
}

// fonction vue pour paneEq
public TitledPane carteTitleDPane(DAOequipement dao,Carte elt,HBox hb,HBox mainhb,VBox vbox,Label label,ObservableList<Carte> options){
    TitledPane gridTitlePane = new TitledPane();
            gridTitlePane.expandedProperty().setValue(false);
            GridPane grid = new GridPane();
            grid.getStyleClass().add("gridCarte");
            grid.setVgap(4);
            grid.setPadding(new Insets(5, 5, 5, 5));
             
            grid.add(new Label("Nom: "), 0, 0);
            Label nomV = new Label("carte "+elt.getNumCarte());
            grid.add(nomV, 1, 0);
            
            grid.add(new Label("Description: "), 0, 1);
            Label descV = new Label(elt.getDesc());
            grid.add(descV, 1, 1);
            
            grid.add(new Label("Attachment: "), 0, 3);
            
            grid.add(new Label("Nombre de Port: "), 0, 4);
            
            Label portV = new Label(""+elt.getNbPort());
            hb.setAlignment(Pos.BOTTOM_RIGHT);
            hb.getChildren().add(portV);
            
            grid.add(hb, 1, 4);
            
            grid.add(new Label("Ports libres: "), 0, 5);
            Label portLV = new Label(""+elt.getNbPortLibre());
            portLV.setTextFill(Color.web(dao.colorlibre()));
            grid.add(portLV, 1, 5);
            
            grid.add(new Label("Ports occupés: "), 0, 6);
            Label portLO = new Label(""+elt.getNbPortOccupe());
            portLO.setTextFill(Color.web(dao.colloroccupe()));
            grid.add(portLO, 1, 6);
            
            grid.add(new Label("Ports desactivés/défecteux: "), 0, 7);
            Label portLD = new Label(""+elt.getNbPortDesactive());
            portLD.setTextFill(Color.web(dao.colordefectueux()));
            grid.add(portLD, 1, 7);
            
            //final Label labelLocal = new Label("N/A");
            grid.add(label,1, 3);
            
            //-- button suprimer et modifier
            
            /*Button modifierBtn=new Button();
            modifierBtn.getStyleClass().add("buttonAdminEq");
            modifierBtn.setText("modifier");

            modifierBtn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        System.out.println("modifier");
                        ModalPaneModifierCarte modal = new ModalPaneModifierCarte(t,elt,dao);
                        modal.construire();
                    }
            });*/
            Button deleteBtn=new Button();
            deleteBtn.getStyleClass().add(dao.colordanger());
            deleteBtn.setText("Suprimer");

            deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        System.out.println("suprimer");
                        
                        ModalPaneDeleteCarte modal = new ModalPaneDeleteCarte(dao,t,elt,mainhb,vbox,options);
                        modal.construire();
                    }
            });
            
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().addAll(deleteBtn);
            
            if(dao.isSuperAdmin(dao.user)){
                grid.add(hbBtn, 1, 8);
            }
            
            
            gridTitlePane.setText("Cartes "+elt.getNumCarte());
            gridTitlePane.setTextFill(Color.web(dao.colordeGestionEq()));
            gridTitlePane.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            gridTitlePane.setContent(grid);
            
            return gridTitlePane;
} 

// fonction vue pour port paneEq
public ScrollPane portScrollPane(DAOequipement dao,Carte elt,HBox hb,Label label){
    Hashtable ports = (Hashtable) dao.getPortCarte();
           if(ports!=null && !ports.isEmpty()){     
                final Accordion accordion = new AccordionPorts(hb,elt,dao).construire();
                ScrollPane sp = new ScrollPane();

                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(accordion);

                accordion.expandedPaneProperty().addListener(
                    (ObservableValue<? extends TitledPane> ov, TitledPane old_val, 
                    TitledPane new_val) -> {
                        if (new_val != null) {
                            label.setText(accordion.getExpandedPane().getText() );
                        }
                });


                return sp;
            
            }
                return null;
            
}

public ScrollPane portScrollPaneAdmin(DAOequipement dao,Carte elt,HBox hb,Label label){
    ArrayList ports = (ArrayList) dao.getPortCarte().get(elt.getId());
            if(ports == null){
                ports = new ArrayList();
            }
            if(ports!=null && !ports.isEmpty()){
                
                final Accordion accordion = new AccordionPortsAdmin (hb,elt,dao).construire();
                ScrollPane sp = new ScrollPane();

                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(accordion);

                accordion.expandedPaneProperty().addListener(
                    (ObservableValue<? extends TitledPane> ov, TitledPane old_val, 
                    TitledPane new_val) -> {
                        if (new_val != null) {
                            label.setText(accordion.getExpandedPane().getText() );
                        }
                });
                return sp;
            }    
            
            return null;
}

//fonction vue port
public TitledPane portTitledPane(DAOequipement dao,Port elt){
    
               GridPane griduser = new GridPane();
                if(dao.getPortClient().get(elt.getId())!=null){
                    int id = (Integer) dao.getPortClient().get(elt.getId());
                    Client client = (Client) dao.getClient().get(id);
                    dao.portOccupeVue(dao, elt, client, griduser);
                
                }else{
                   dao.allouerPortVue(dao, elt,griduser);
                }
                griduser.getStyleClass().add("gridCarteA");
                TitledPane tps = new TitledPane("port "+elt.getNumeroPort(),griduser);
                tps.setTextFill(Color.web(elt.giveColor()));
                return tps;
}
//fonction check button
public HBox checKandSynchro(DAOequipement dao,Port elt,int i,Client... client){
    
                Button check = new Button("Check");
                check.setOpacity(0.6);
                
                check.getStyleClass().add(dao.colorsmalldanger());
                
                check.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        Carte carte = (Carte)dao.getCarte().get(elt.getIdCarte());
                        Equipement eq = (Equipement)dao.getEquipement().get(carte.getIdEQ());
                        Dialog<Pair<String, String>> dialog = new Dialog<>();
                        dialog.setTitle("Login Dialog");
                        dialog.setHeaderText(eq.getNom()+", paramètre connection");
                        // Set the icon (must be included in the project).
                        dialog.setGraphic(new ImageView(this.getClass().getResource("/icon/DSLAM-icon.png").toString()));
                        // Set the button types.
                        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
                        // Create the username and password labels and fields.
                        GridPane grid = new GridPane();
                        grid.setHgap(10);
                        grid.setVgap(10);
                        grid.setPadding(new Insets(20, 150, 10, 10));

                        TextField username = new TextField();
                        username.setPromptText("Username");
                        PasswordField password = new PasswordField();
                        password.setPromptText("Password");

                        grid.add(new Label("Username:"), 0, 0);
                        grid.add(username, 1, 0);
                        grid.add(new Label("Password:"), 0, 1);
                        grid.add(password, 1, 1);
                        // Enable/Disable login button depending on whether a username was entered.
                        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
                        loginButton.setDisable(true);

                        // Do some validation (using the Java 8 lambda syntax).
                        password.textProperty().addListener((observable, oldValue, newValue) -> {
                            loginButton.setDisable(newValue.trim().isEmpty());
                        });

                        dialog.getDialogPane().setContent(grid);

                        // Request focus on the username field by default.
                        Platform.runLater(() -> username.requestFocus());

                        // Convert the result to a username-password-pair when the login button is clicked.
                        dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == loginButtonType) {
                                return new Pair<>(username.getText(), password.getText());
                            }
                            return null;
                        });

                        Optional<Pair<String, String>> result = dialog.showAndWait();


                        result.ifPresent(usernamePassword -> {
                            //System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());

                            if(eq.isSSH()){
                                ModalSsh modal = new ModalSsh(t,dao,usernamePassword.getKey(),usernamePassword.getValue(),elt);
                                modal.construire();
                            }else{
                                ModalTelnet modal = new ModalTelnet(t,dao,usernamePassword.getKey(),usernamePassword.getValue(),elt);
                                modal.construire();
                            }
                        });
                        
                    }
                });
                Button synchroBtn = new Button("Synchroniser");
                synchroBtn.setOpacity(0.6);
                synchroBtn.getStyleClass().add(dao.colorsmalldanger());
                
                synchroBtn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        
                        
                    }
                });
                
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                if(i==1)
                    hbBtn.getChildren().addAll(check);
                else
                    hbBtn.getChildren().addAll(check);
                return hbBtn;
}

//fonction vue port Admin
public TitledPane portTitledPaneAdmin(DAOequipement dao,Port elt){
                Button btn2 = new Button("Bon");
                btn2.getStyleClass().add(dao.colornodanger());
                Button btn = new Button("Défectueux");
                btn.getStyleClass().add(dao.colordanger());

                if (elt.isDesactivate()) {
                    btn.setDisable(true);
                    btn2.setDisable(false);
                } else {
                    btn.setDisable(false);
                    btn2.setDisable(true);
                }
                
                GridPane griduser = new GridPane();
                TitledPane tps = new TitledPane("port " + elt.getNumeroPort(), griduser);
                griduser.getStyleClass().add("gridCarteAD");
                griduser.setAlignment(Pos.CENTER);
                griduser.setHgap(10);
                griduser.setVgap(10);
                griduser.setPadding(new Insets(25, 25, 25, 25));
                griduser.add(new Label("Statut: "), 0, 3);
                Label statutL = new Label(elt.giveStatus());
                statutL.setTextFill(Color.web(elt.giveColor()));
                statutL.getStyleClass().add("gridfont");
                final Tooltip tooltipL = new Tooltip();
                tooltipL.setText("Libre/Occupé/Défectueux");
                statutL.setTooltip(tooltipL);
                
                
                Label actionTarget = new Label("");
                griduser.add(actionTarget, 1, 4);
                
                if(elt.isOccupe()){
                    Client client = (Client) dao.getClient().get(dao.getPortClient().get(elt.getId()));
                    HBox statB = new HBox();
                    btn.setDisable(true);

                    ImageView depIcon = new ImageView (
                        new Image(getClass().getResourceAsStream("/images/usersmall.png"))
                    );
                    ImageView depIcon2 = new ImageView (
                        new Image(getClass().getResourceAsStream("/images/usersmall.png"))
                    );
                    Button btnG = new Button();
                    btnG.setGraphic(depIcon );
                    
                    Button detailbtn = new Button("Details("+client.getNumTel()+")");
                    detailbtn.setCenterShape(true);
                    detailbtn.setGraphic(depIcon2 );
                    
                    //Adding GridPane

                    GridPane gridPane = new GridPane();
                    gridPane.setPadding(new Insets(20,20,20,20));
                    gridPane.setHgap(5);
                    gridPane.setVgap(5);
                    //Implementing Nodes for GridPane
                    Label lblto = new Label("to");
                    final TextField txtto = new TextField();
                    txtto.setEditable(false);
                    //---- combo Box users
                    Enumeration e =dao.getUser().elements();
                    ObservableList<User> options = FXCollections.observableArrayList();


                    while(e.hasMoreElements()){
                        User user =(User) e.nextElement();
                        options.add(user);
                    }


                    final ComboBox comboBox = new ComboBox(options);
                    comboBox.setPadding(new Insets(10, 10, 10, 10));


                    comboBox.setPromptText("choix recepteur");     
                    comboBox.setCellFactory(new Callback<ListView<User>,ListCell<User>>(){

                                @Override
                                public ListCell<User> call(ListView<User> p) {

                                    final ListCell<User> cell = new ListCell<User>(){

                                        @Override
                                        protected void updateItem(User t, boolean bln) {
                                            super.updateItem(t, bln);

                                            if(t != null){
                                                setText(t.getNom()+" "+t.getPrenom());
                                                
                                            }else{
                                                setText(null);
                                            }
                                        }

                                    };

                                    return cell;
                                }
                            });



                    comboBox.valueProperty().addListener(new ChangeListener<User>() {
                                @Override public void changed(ObservableValue ov, User t, User t1) {
                                    
                                   if(t1!=null){ 
                                     txtto.setText(t1.getNom()+" "+t1.getPrenom());
                                     touser =t1;
                                   } 
                                }    
                            });
                    /*new AutoCompletionTextFieldBinding(txtto, new Callback<AutoCompletionBinding.ISuggestionRequest, Collection>() {
                        @Override
                        public Collection call(AutoCompletionBinding.ISuggestionRequest param) {
                            return Arrays.asList("Option 1", "Option 2",elt);
                        }
                    });*/
                    Label lblobjet = new Label("objet");
                    final TextField txtobjet = new TextField();
                    txtobjet.setText("signalement d'un port défectueux auquel est attaché un client");
                    Button btnEnvoyer = new Button("envoyer");
                    TextArea textA = new TextArea();
                    textA.setPrefSize(300, 210);
                    Carte carte = (Carte) dao.getCarte().get(elt.getIdCarte());
                    Equipement eq = (Equipement) dao.getEquipement().get(carte.getIdEQ());
                    Secteur sect = (Secteur) dao.getSecteur().get(eq.getIdSecteur());
                    Ville ville = (Ville) dao.getVille().get(sect.getIdVille());
                    textA.setText("Salut Mme/Mr/Mlle juste pour vous notifer que le client("+client.getNom()+" "+client.getNumTel()+")/"+client.getNumTel()+" attaché au port("+elt.getNumeroPort()
                            +") de la carte("+carte.getNumCarte()+") de l'équipement ("+eq.getNom()+") du secteur ("+sect.getNom()
                            +") de la ville("+ville.getNom()+") doit être basculer car ce port est défectueux");
                    textA.setWrapText(true);
                    final Label lblMessage = new Label();
                    //lblMessage.setTextFill(Color.web(dao.colordanger()));
                    //Adding Nodes to GridPane layout
                    
                    HBox hb = new HBox(5);
                    hb.getChildren().addAll(lblto);
                    gridPane.add(hb, 0, 0);

                    gridPane.add(txtto, 1, 0);

                    gridPane.add(lblobjet, 0, 1);

                    gridPane.add(txtobjet, 1, 1);

                    gridPane.add(textA, 1, 3);
                    
                    gridPane.add(btnEnvoyer, 1, 4);
                    
                    ScrollPane sp = new ScrollPane();
                    sp.setMaxHeight((dao.height*36)/100);
                    sp.setContent(gridPane);

                    VBox notifHb = new VBox(5);
                    TextArea mytext =new TextArea("*pour activer les modifications sur ce port veillez notifier votre supérieur");
                    mytext.setEditable(false);
                    mytext.setPrefSize(50, 50);
                    mytext.setWrapText(true);

                    notifHb.getChildren().addAll(mytext,comboBox,sp,lblMessage,detailbtn);
                    PopOver popOver = new PopOver(notifHb);
                    popOver.setTitle("Notifications(Port "+elt.getNumeroPort()+")");
                    popOver.setAnimated(true);
                    popOver.setDetachable(true);
                    popOver.setDetached(true);
                    btnG.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            
                            popOver.show(btnG);
                            
                        }
                    });
                    btnEnvoyer.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            
                            
                            
                            if(!txtobjet.getText().isEmpty()&&!txtto.getText().isEmpty()&&!textA.getText().isEmpty()){
                                
                             
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

                                   Notification notif = new Notification();
                                   //utile pour la différentiation lors de l'affichage dans le treeTableView 
                                   notif.setIdEmet(dao.user.getId());
                                   notif.setIdRecep(touser.getId());
                                   notif.setObjet(txtobjet.getText());
                                   notif.setMessage(textA.getText());
                                   try { 
                                       int id =con.fInsertNotification(notif);

                                       // ajout de l'objet secteur a la liste d'objet
                                       notif.setId(id);

                                       new Notifier("Gestab","Notification envoyé à :"+touser.getNom()+" "+touser.getPrenom(),2,dao);

                                       popOver.hide();
                                       if (elt.isDesactivate()) {
                                            btn.setDisable(true);
                                            btn2.setDisable(false);
                                        } else {
                                            btn.setDisable(false);
                                            btn2.setDisable(true);
                                        }
                                   } catch (ClassNotFoundException ex) {
                                       Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                       lblMessage.setText("Echec d'envoie.");
                                   } catch (SQLException ex) {
                                       Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                       lblMessage.setText("Echec d'envoie.");
                                   } catch (ParseException ex) {
                                    Logger.getLogger(FonctionVue.class.getName()).log(Level.SEVERE, null, ex);
                                    lblMessage.setText("Echec d'envoie.");
                                   }
                               }else{
                                    lblMessage.setText("Echec: le(s) Champ(s) de texte vide!");
                               } 
                            
                            
                        }
                    });
                    detailbtn.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            popOver.detach();
                            
                            if(client!=null){
                                ModalPaneChangePortClientAdmin modalPort = new ModalPaneChangePortClientAdmin(dao,t,client,statB,btn,tps);
                                modalPort.construire();
                            }else{
                                actionTarget.setText("Objet Client null.");
                            }
                        }
                    });
                    
                    HBox hc = new HBox(4);
                    hc.getChildren().addAll(statutL,btnG);
                    statB.getChildren().addAll(hc);
                    griduser.add(statB, 1, 3);
                }else{
                    griduser.add(statutL, 1, 3);
                }
                

                griduser.add(new Label("Déclaré Bon le (Date): "), 0, 1);
                griduser.add(new Label("Déclaré défectueux le (Date): "), 0, 2);

                final DatePicker datePicker = new DatePicker();
                //griduser.add(datePicker, 1, 1);
                griduser.add(new Label("N/A"),1,1);
                griduser.add(new Label("N/A"),1,2);
                datePicker.setDisable(true);
                /*datePicker.setOnAction(new EventHandler() {
                    public void handle(Event t) {
                        LocalDate date = datePicker.getValue();
                        System.err.println("Selected date: " + date);
                    }
                });*/

                //desactiver port
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        btn.setDisable(true);
                        btn2.setDisable(false);
                        Connecter c = null;
                        try {
                            c = new Connecter();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(AccordionPortsAdmin.class.getName()).log(Level.SEVERE, null, ex);
                            actionTarget.setText("Probleme de connection.");
                        } catch (SQLException ex) {
                            Logger.getLogger(AccordionPortsAdmin.class.getName()).log(Level.SEVERE, null, ex);
                            actionTarget.setText("Le réseau n'est pas accessible(Parametre).");
                        }
                        Requettes con = new Requettes(c);

                        try {
                            Boolean oldStatus = elt.isLibre();
                            elt.setStatus(elt.defectueux());
                            con.fUpdateSatutPort(elt);
                            c.getConnexion().close();
                            
                            dao.getPort().replace(elt.getId(), elt);
                            
                            Carte carte = (Carte) dao.getCarte().get(elt.getIdCarte());
                            Equipement equip = (Equipement) dao.getEquipement().get(carte.getIdEQ());
                            Secteur sec = (Secteur) dao.getSecteur().get(equip.getIdSecteur());
                            Ville vil = (Ville) dao.getVille().get(sec.getIdVille());
                            if (oldStatus) {
                                carte.setNbPortLibre(carte.getNbPortLibre() - 1);
                                carte.setNbPortDesactive(carte.getNbPortDesactive() + 1);
                                equip.setTmpNbPortLibre(equip.getTmpNbPortLibre() - 1);
                                equip.setTmpNbPortDefect(equip.getTmpNbPortDefect()+1);
                                sec.setNbPortLibre(sec.getNbPortLibre()-1);
                                sec.setNbPortDefect(sec.getNbPortDefect()+1);
                                vil.setNbPortLibre(vil.getNbPortLibre()-1);
                                vil.setNbPortDefect(vil.getNbPortDefect()+1);
                            } else {
                                carte.setNbPortOccupe(carte.getNbPortOccupe() - 1);
                                carte.setNbPortDesactive(carte.getNbPortDesactive() + 1);
                                equip.setTmpNbPortOccupe(equip.getTmpNbPortOccupe() - 1);
                                equip.setTmpNbPortDefect(equip.getTmpNbPortDefect()+1);
                                sec.setNbPortOccupe(sec.getNbPortOccupe()-1);
                                sec.setNbPortDefect(sec.getNbPortDefect()+1);
                                vil.setNbPortOccupe(vil.getNbPortOccupe()-1);
                                vil.setNbPortDefect(vil.getNbPortDefect()+1);
                            }
                            dao.getCarte().replace(carte.getId(), carte);
                            dao.getEquipement().replace(equip.getIdEQ(), equip);
                            dao.getSecteur().replace(sec.getId(), sec);
                            dao.getVille().replace(vil.getId(), vil);
                            
                            
                            statutL.setText(elt.giveStatus());
                            statutL.setTextFill(Color.web(elt.giveColor()));
                            tps.setTextFill(Color.web(elt.giveColor()));
                            actionTarget.setText("Port " + elt.giveStatus());
                        } catch (SQLException ex) {
                            Logger.getLogger(AccordionPortsAdmin.class.getName()).log(Level.SEVERE, null, ex);
                            actionTarget.setText("Echec d'insertion.");
                        }

                    }
                });

                //activer port
                btn2.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        btn.setDisable(false);
                        btn2.setDisable(true);
                        Connecter c = null;
                        try {
                            c = new Connecter();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(AccordionPortsAdmin.class.getName()).log(Level.SEVERE, null, ex);
                            actionTarget.setText("Probleme de connection.");
                        } catch (SQLException ex) {
                            Logger.getLogger(AccordionPortsAdmin.class.getName()).log(Level.SEVERE, null, ex);
                            actionTarget.setText("Le réseau n'est pas accessible(Parametre).");
                        }
                        Requettes con = new Requettes(c);
                        elt.setStatus(elt.librerer());

                        try {
                            con.fUpdateSatutPort(elt);
                            c.getConnexion().close();
                            
                            Carte carte = (Carte) dao.getCarte().get(elt.getIdCarte());
                            Equipement equip = (Equipement) dao.getEquipement().get(carte.getIdEQ());
                            Secteur sec = (Secteur) dao.getSecteur().get(equip.getIdSecteur());
                            Ville vil = (Ville) dao.getVille().get(sec.getIdVille());
                            
                                carte.setNbPortLibre(carte.getNbPortLibre() + 1);
                                carte.setNbPortDesactive(carte.getNbPortDesactive() - 1);
                                equip.setTmpNbPortLibre(equip.getTmpNbPortLibre() + 1);
                                equip.setTmpNbPortDefect(equip.getTmpNbPortDefect()-1);
                                sec.setNbPortLibre(sec.getNbPortLibre()+1);
                                sec.setNbPortDefect(sec.getNbPortDefect()-1);
                                vil.setNbPortLibre(vil.getNbPortLibre()+1);
                                vil.setNbPortDefect(vil.getNbPortDefect()-1);
                            
                            dao.getCarte().replace(carte.getId(), carte);
                            dao.getEquipement().replace(equip.getIdEQ(), equip);
                            dao.getSecteur().replace(sec.getId(), sec);
                            dao.getVille().replace(vil.getId(), vil);
                            
                            statutL.setText(elt.giveStatus());
                            statutL.setTextFill(Color.web(elt.giveColor()));
                            tps.setTextFill(Color.web(elt.giveColor()));
                            actionTarget.setText("Port " + elt.giveStatus());
                        } catch (SQLException ex) {
                            Logger.getLogger(AccordionPortsAdmin.class.getName()).log(Level.SEVERE, null, ex);
                            actionTarget.setText("Echec mise a jour.");
                        }

                    }
                });
                

                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().addAll(btn, btn2);
                
                // si super administrateur
                if(dao.isSuperAdmin(dao.user)||dao.isAdmin(dao.user)||dao.isTechAdmin(dao.user)){
                    griduser.add(hbBtn, 0, 4);
                }
                
                tps.setTextFill(Color.web(elt.giveColor()));
                

                return tps;
}

// ComboBox carte lister
public ComboBox comboBoxCarte(ObservableList<Carte> options,ArrayList cartes){
    final ComboBox comboBox = new ComboBox(options);
    comboBox.setPadding(new Insets(1, 1, 1, 1));
                
                
        if(!cartes.isEmpty()){
            comboBox.setPromptText("cartes");
        }else{   
            comboBox.setPromptText("Vide");
        } 
        
        comboBox.setCellFactory(new Callback<ListView<Carte>,ListCell<Carte>>(){
 
            @Override
            public ListCell<Carte> call(ListView<Carte> p) {
                 
                final ListCell<Carte> cell = new ListCell<Carte>(){
 
                    @Override
                    protected void updateItem(Carte t, boolean bln) {
                        super.updateItem(t, bln);
                         
                        if(t != null){
                            if(t.getNumCarte()!=0){
                                setText("Carte "+t.getNumCarte());
                            }else{
                                setText("Tout");
                            }
                        }else{
                            setText(null);
                        }
                    }
  
                };
                 
                return cell;
            }
        });
        return comboBox;
}

// ComboBox port lister
public ComboBox comboBoxPort(ObservableList<Port> options,ArrayList ports){
    final ComboBox comboBox = new ComboBox(options);
        
        comboBox.setPadding(new Insets(1, 1, 1, 1));
        if (ports != null && !ports.isEmpty()) {
            comboBox.setPromptText("Afficher...");
        } else {
            comboBox.setPromptText("Vide");
        }
        comboBox.getStyleClass().add("comboGestEq");
        
        comboBox.setCellFactory(new Callback<ListView<Port>,ListCell<Port>>(){
 
            @Override
            public ListCell<Port> call(ListView<Port> p) {
                 
                final ListCell<Port> cell = new ListCell<Port>(){
 
                    @Override
                    protected void updateItem(Port t, boolean bln) {
                        super.updateItem(t, bln);
                         
                        if(t != null){
                            if(t.getNumeroPort()>=0){
                                setText("Port "+t.getNumeroPort());
                            }else{
                                setText("Tout");
                            }
                        }else{
                            setText(null);
                        }
                    }
  
                };
                 
                return cell;
            }
        });
        return comboBox;
}
   
   public String colorlibre(){
        return "#05B30B";
    }
    
    public String colloroccupe(){
        return "#10A5DE";
    }
    
    public String colordefectueux(){
        return "#F51111";
    }
    
    public String colordeGestionEq(){
        return "#A30098";
    }
    
    public String colordeGestionCl(){
        return "#A35F00";
    }
    
    public String colordeAdminEq(){
        return "#0076a3";
    }
    
    public String colordeAdminU(){
        return "#75606B";
    }
    
    public String colordanger(){
        return "danger";
    }
    
    public String colorsmalldanger(){
        return "smalldanger";
    }
    public String colornodanger(){
        return "nodanger";
    }
    public String colorchange(){
        return "change";
    }
    
    public String colorannuler(){
        return "annuler";
    }
    
    
    
    
}
