/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import jssh.Desactiver;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import telnet.ModalTelnetActiver;
import telnet.ModalTelnetDesactiver;

/*import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;*/

/**
 *
 * @author ghomsi
 */
public class ModalStatClient {
    private DAOequipement dao;
    private ActionEvent event;
    
    private Categorie categorieR;
    private Debit debitR;
    private Ville ville,villeE;
    private List list;
    private Secteur sectR;
    private Equipement eqR;
    private Carte carteR;
    private Port portR;
    private String statutR;
    private String portStatutR;
    private Boolean aSusp;
    private Boolean valider;
    private DatePicker dateFrom;
    private DatePicker dateTo;
    
    Scene scene;
    ScrollPane sp;
    
    public ModalStatClient(ActionEvent event,DAOequipement dao){
        this.event = event;
        this.dao =dao;
        this.list = null;
        this.valider = null;
        this.aSusp = null;
    }
    
    void construire() throws ClassNotFoundException, SQLException{
    
       Stage stage = new Stage();
       
       Image imgContent = new Image(getClass().getResourceAsStream("/images/excelFile.png"),100,100,false,false);
       ImageView pics = new ImageView(imgContent);
       
       ImageView imgExcel = new ImageView (
                        new Image(getClass().getResourceAsStream("/images/excelFile.png"),30,30,false,false)
        );
        Button btnExcel = new Button();
        btnExcel.setDisable(true);
        btnExcel.getStyleClass().add(dao.colorannuler());
        btnExcel.setGraphic(imgExcel );
        btnExcel.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
               
               excellgenerator(); 
            }
        });
        
        /*ImageView imgPdf = new ImageView (
                        new Image(getClass().getResourceAsStream("/images/pdfFile.png"),30,30,false,false)
        );
        Button btnPdf = new Button();
        btnPdf.getStyleClass().add(dao.colorannuler());
        btnPdf.setGraphic(imgPdf );
        btnPdf.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
                pdfgenerator();
            }
        });*/
       
       
       HBox hb = new HBox(10);
       HBox hBtn = new HBox(5);
       Label nbrelist = new Label();
       hBtn.getChildren().addAll(btnExcel,nbrelist);
       
       VBox vb = new VBox(5);
       VBox vStat = new VBox(5);
       VBox mainVb = new VBox(20);
       
       Label headL = new Label("Filtre");
       headL.setTextFill(Color.web(dao.couleur));
       headL.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
       
       Label villeEQL = new Label();
       Label secteurL = new Label();
        Label eqL = new Label();
        Label carteL = new Label();
        Label portL = new Label();
       
      
      Label debit = new Label("Debits:");
       debit.setTextFill(Color.web(dao.couleur));
       Label debitL = new Label();
       Label cat = new Label("Categories:");
       cat.setTextFill(Color.web(dao.couleur));
       Label catL = new Label();
       Label villel =new Label("Villes:");
       villel.setTextFill(Color.web(dao.couleur));
       Label villeL = new Label();
       Label stat = new Label("Status:");
       stat.setTextFill(Color.web(dao.couleur));
       Label date1 = new Label("De:");
       date1.setTextFill(Color.web(dao.couleur));
       Label date2 = new Label("A:");
       date2.setTextFill(Color.web(dao.couleur));
       
       //---- combo Box villes
        Hashtable villes = dao.getVille();

        ObservableList<Ville> options = FXCollections.observableArrayList();
        Enumeration e3 = villes.elements();
        while(e3.hasMoreElements()){
            Ville elt = (Ville) e3.nextElement();
            options.add(elt);
        }
        ComboBox villeCB = new ComboBox<>(options);
        villeCB.getStyleClass().add(dao.colorannuler());
        if(dao.isSubUser(dao.user)){
            villeCB.getSelectionModel().selectFirst();
            ville = (Ville) villeCB.getValue();
            villeL.setText(ville.getNom());
        }else{
            villeCB.setPromptText("Toutes");
        }
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
              
              
                    ville = t1;
                    if(t1!=null)
                        villeL.setText(t1.getNom());
                
              
                
            }    
        });
       
       //---- combo Box debit
        Hashtable debits = dao.getDebit();

        ObservableList<Debit> optionsDebit = FXCollections.observableArrayList();
        Enumeration e = debits.elements();
        while(e.hasMoreElements()) {
            Debit elt = (Debit) e.nextElement();
            optionsDebit.add(elt);
        }
        ComboBox debitCB = new ComboBox<>(optionsDebit);
        debitCB.getStyleClass().add(dao.colorannuler());
        debitCB.setPromptText("Tout");
        
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
              if(t1!=null)
                debitL.setText(t1.getNom());
                
            }    
        });
       
       //---- combo Box categorie
        Hashtable cats = dao.getCategorieForfait();

        ObservableList<Categorie> optionsCat = FXCollections.observableArrayList();
        
        Enumeration e1 = cats.elements();
        while(e1.hasMoreElements()){
            Categorie elt = (Categorie) e1.nextElement();
            optionsCat.add(elt);
        }
        
        ComboBox categorieCB = new ComboBox<>(optionsCat);
        categorieCB.getStyleClass().add(dao.colorannuler());
        categorieCB.setPromptText("Toutes");
        
        
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
              if(t1!=null)
                  catL.setText(t1.getNom());
                
            }    
        }); 
        
        //--status
        ComboBox statusCB = new ComboBox<>();
        statusCB.getStyleClass().add(dao.colorannuler());
        statusCB.setPromptText("Tout");
        
        
        statusCB.getItems().addAll("ACTIF" ,"SUSPENDU","RESILIE");
        
        statusCB.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
              
              statutR = t1;
              
                
            }    
        });
        
        //--statusPCB
        ComboBox statusPCB = new ComboBox<>();
        statusPCB.getStyleClass().add(dao.colorannuler());
        statusPCB.setPromptText("Tout");
        
        
        statusPCB.getItems().addAll("ACTIF" ,"DEFECTUEUX");
        
        statusPCB.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
              
              portStatutR = t1;
              
                
            }    
        });
        
        
        //-- a suspendre
        ComboBox suspCB = new ComboBox<>();
        suspCB.getStyleClass().add(dao.colorannuler());
        suspCB.setPromptText("Tout");
        
        
        suspCB.getItems().addAll("Oui" ,"Non");
        
        suspCB.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                aSusp = null;
                if(t1!=null){  
                  if(t1.equals("Oui"))
                      aSusp = true;
                  else
                      aSusp = false;
                }  
            }    
        });
        
        //--valider suspension
        ComboBox validerCB = new ComboBox<>();
        validerCB.getStyleClass().add(dao.colorannuler());
        validerCB.setPromptText("Tout");
        
        
        validerCB.getItems().addAll("Oui" ,"Non");
        
        validerCB.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
             valider = null;
             if(t1!=null){ 
                if(t1.equals("Oui"))
                    valider = true;
                else
                    valider = false;              
             } 
                
            }    
        });
        
        
       //--Date
       dateFrom = new DatePicker();
       dateFrom.getStyleClass().add(dao.colorannuler());
       dateTo = new DatePicker();
       dateTo.getStyleClass().add(dao.colorannuler());
       //--Button
       Button btnRech = new Button("rechercher >");
       btnRech.getStyleClass().add(dao.colorannuler());
       
        
       FlowPane paneStat = new FlowPane(); 
       
        btnRech.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    
                    
                    Connecter c =null;
                    try {
                        c = new Connecter();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalStatClient.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ModalStatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Requettes con = new Requettes(c);
                    
                    try {
                        
                        String status = null;
                        String statusP = null;
                        if(statusPCB.getValue()!=null){
                            status = statusPCB.getValue().toString();
                        }
                        if(statusCB.getValue()!=null){
                            statusP = statusCB.getValue().toString();
                        }
                        
                        
                        
                        //Creating a column
                        TreeTableColumn<Client,String> sectColumnNom = new TreeTableColumn<>("Nom");
                        TreeTableColumn<Client,String> sectColumnStatut = new TreeTableColumn<>("Statut");
                        TreeTableColumn<Client,String> sectColumnTel = new TreeTableColumn<>("Telephone");
                        TreeTableColumn<Client,String> sectColumnEmail = new TreeTableColumn<>("Email");
                        TreeTableColumn<Client,String> sectColumnDateC = new TreeTableColumn<>("Date Creation");
                        TreeTableColumn<Client,String> sectColumnDateAct = new TreeTableColumn<>("Date Activation");
                        TreeTableColumn<Client,String> sectColumnDateS = new TreeTableColumn<>("Date Suspension");
                        TreeTableColumn<Client,String> sectColumnDateR = new TreeTableColumn<>("Date Résiliation");
                        TreeTableColumn<Client,String> sectColumnVilleR = new TreeTableColumn<>("Ville de résidence");
                        TreeTableColumn<Client,String> sectColumnEside = new TreeTableColumn<>("E-side");
                        TreeTableColumn<Client,String> sectColumnVEQ = new TreeTableColumn<>("Ville d'attachement");
                        TreeTableColumn<Client,String> sectColumnSEQ = new TreeTableColumn<>("Secteur d'attachement");
                        TreeTableColumn<Client,String> sectColumnEQ = new TreeTableColumn<>("Equipement d'attachement");
                        TreeTableColumn<Client,String> sectColumnCarteEQ = new TreeTableColumn<>("Carte d'attachement");
                        TreeTableColumn<Client,String> sectColumnPortEQ = new TreeTableColumn<>("Port d'attachement");
                        TreeTableColumn<Client,String> sectColumnDebit = new TreeTableColumn<>("Debit");
                        TreeTableColumn<Client,String> sectColumnCat = new TreeTableColumn<>("Categorie");
                        
                        TreeTableColumn<Client,String> sectColumnCom = new TreeTableColumn<>("Suspension");
                        
                        TreeTableColumn<Client,Boolean> sectColumnSusp = new TreeTableColumn<>("A Suspendre");
                        
                        TreeTableColumn<Client,Boolean> sectColumnSuspVal = new TreeTableColumn<>("Suspension Validé");
                        
                        TreeTableColumn<Client,Boolean> sectColumnSuspAdmin = new TreeTableColumn<>("Suspendre");
                        
                        
                        if(dao.isSuperAdmin(dao.user)){
                            sectColumnCom.getColumns().addAll(sectColumnSuspAdmin,sectColumnSuspVal,sectColumnSusp);
                        }else if(dao.isTechUser(dao.user)){
                            sectColumnSuspVal.setEditable(false);
                            sectColumnSusp.setEditable(false);
                            sectColumnCom.getColumns().addAll(sectColumnSuspAdmin,sectColumnSuspVal,sectColumnSusp);
                        }else if(dao.isUser(dao.user)){
                            sectColumnSusp.setEditable(false);
                            sectColumnCom.getColumns().addAll(sectColumnSuspVal,sectColumnSusp);
                        }else{
                            sectColumnCom.getColumns().addAll(sectColumnSusp);
                        }    

                        //Defining cell content
                        //--nom
                        sectColumnNom.setCellValueFactory(
                            (TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            new ReadOnlyStringWrapper(p.getValue().getValue().getNom()+" "+p.getValue().getValue().getPrenom())
                        );
                        //--statut
                        sectColumnStatut.setCellValueFactory(
                            (TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            new ReadOnlyStringWrapper(p.getValue().getValue().getStatut())
                        );
                        //--numero de telephone
                        sectColumnEmail.setCellValueFactory(
                            (TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            new ReadOnlyStringWrapper(p.getValue().getValue().getEmail())
                        );
                        //--email
                        sectColumnTel.setCellValueFactory(
                            (TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            new ReadOnlyStringWrapper(p.getValue().getValue().getNumTel())
                        );
                        //--date creation
                        sectColumnDateC.setCellValueFactory(
                            (TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            new ReadOnlyStringWrapper(p.getValue().getValue().getDateCreation())
                        );
                        //--date activation
                        sectColumnDateAct.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
                            @Override
                            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client, String> p) {
                                List list = (List) dao.getClientHistorique().get(p.getValue().getValue().getId());
                                String date="";
                                if(list!=null){
                                    Iterator<Historique> it = list.iterator();                               
                                    while (it.hasNext()) {
                                           Historique s = it.next();
                                           if(s.getStatus()==1)
                                                date+=s.getDate()+"/";
                                    }
                                }
                                return new ReadOnlyStringWrapper(date);
                            }
                        });
                        //--date suspension
                        sectColumnDateS.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
                            @Override
                            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client, String> p) {
                                List list = (List) dao.getClientHistorique().get(p.getValue().getValue().getId());
                                String date="";
                                if(list!=null){
                                    Iterator<Historique> it = list.iterator();                                
                                    while (it.hasNext()) {
                                           Historique s = it.next();
                                           if(s.getStatus()==2)
                                                date+=s.getDate()+"/";
                                    }
                                }
                                return new ReadOnlyStringWrapper(date);
                            }
                        });
                        //--date résiliation
                        sectColumnDateR.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
                            @Override
                            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Client, String> p) {
                                List list = (List) dao.getClientHistorique().get(p.getValue().getValue().getId());
                                String date="";
                                
                                if(list!=null){
                                    Iterator<Historique> it = list.iterator();                               
                                    while (it.hasNext()) {
                                           Historique s = it.next();
                                           if(s.getStatus()==3)
                                                date+=s.getDate()+"/";
                                    }
                                }
                                return new ReadOnlyStringWrapper(date);
                            }
                        });
                        //--ville de residence
                        sectColumnVilleR.setCellValueFactory((TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            {
                                Ville ville = (Ville) dao.getVille().get(p.getValue().getValue().getVilleOwner());    
                                if(ville!=null){
                                    return new ReadOnlyStringWrapper(ville.getNom());
                                }else{
                                    return new ReadOnlyStringWrapper("N/A");
                                }
                            }
                        );
                        //--E side
                        sectColumnEside.setCellValueFactory(
                            (TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            new ReadOnlyStringWrapper(p.getValue().getValue().getEside())
                        );
                        //--Ville equipement
                        sectColumnVEQ.setCellValueFactory((TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            {
                                Ville ville = (Ville) dao.getVille().get(p.getValue().getValue().getVille());    
                                if(ville!=null){
                                    return new ReadOnlyStringWrapper(ville.getNom());
                                }else{
                                    return new ReadOnlyStringWrapper("N/A");
                                }
                            }
                        );
                        //--Secteur equipement
                        sectColumnSEQ.setCellValueFactory((TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            {
                                Secteur sect = (Secteur) dao.getSecteur().get(p.getValue().getValue().getSecteur());    
                                if(sect!=null){
                                    return new ReadOnlyStringWrapper(sect.getNom());
                                }else{
                                    return new ReadOnlyStringWrapper("N/A");
                                }
                            }
                        );
                        //--Equipement
                        sectColumnEQ.setCellValueFactory((TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            {
                                Equipement eq = (Equipement) dao.getEquipement().get(p.getValue().getValue().getEq());    
                                if(eq!=null){
                                    return new ReadOnlyStringWrapper(eq.getNom());
                                }else{
                                    return new ReadOnlyStringWrapper("N/A");
                                }
                            }
                        );
                        //--Carte equipement
                        sectColumnCarteEQ.setCellValueFactory((TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            {
                                Carte carte = (Carte) dao.getCarte().get(p.getValue().getValue().getCarte());    
                                if(carte!=null){
                                    return new ReadOnlyStringWrapper("Carte "+carte.getNumCarte());
                                }else{
                                    return new ReadOnlyStringWrapper("N/A");
                                }
                            }
                        );
                        //--Port equipement
                        sectColumnPortEQ.setCellValueFactory((TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            {
                                Port port = (Port) dao.getPort().get(p.getValue().getValue().getPort());    
                                if(port!=null){
                                    return new ReadOnlyStringWrapper("Port "+port.getNumeroPort());
                                }else{
                                    return new ReadOnlyStringWrapper("N/A");
                                }
                            }
                        );
                        //--Debit
                        sectColumnDebit.setCellValueFactory((TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            {
                                Debit debit = (Debit) dao.getDebit().get(p.getValue().getValue().getDebit());    
                                if(debit!=null){
                                    return new ReadOnlyStringWrapper(debit.getNom());
                                }else{
                                    return new ReadOnlyStringWrapper("N/A");
                                }
                            }
                        );
                        //--Categorie
                        sectColumnCat.setCellValueFactory((TreeTableColumn.CellDataFeatures<Client, String> p) -> 
                            {
                                Categorie catF = (Categorie) dao.getCategorieForfait().get(p.getValue().getValue().getCategorie());    
                                if(catF!=null){
                                    return new ReadOnlyStringWrapper(catF.getNom());
                                }else{
                                    return new ReadOnlyStringWrapper("N/A");
                                }
                            }
                        );
                        
                        
                        
                        
                        
                        //Creating a treeitem table view
                        TreeItem<Client> rootClt =  new TreeItem<>(new Client());
                        rootClt.setExpanded(true); 
                        
                        list = dao.getAllClientSTATBD(c,debitR,categorieR,ville,status,villeE,sectR,eqR,carteR,portR,statusP,(LocalDate) dateFrom.getValue(),(LocalDate) dateTo.getValue(),aSusp,valider);
                        nbrelist.setText("résultat(s) "+list.size());
                        Iterator<Client> it = list.iterator();
                        while (it.hasNext()) {
                               Client s = it.next();
                               TreeItem<Client>root =  new TreeItem<>(s);
                               rootClt.getChildren().add(root);


                        }
                        
                        
                        final ObservableList<Client> data =FXCollections.observableArrayList(list);
                        
                        // ==== A suspendre? (CHECH BOX) ===
                        sectColumnSusp.setCellFactory(new Callback<TreeTableColumn<Client,Boolean>,TreeTableCell<Client,Boolean>>() {
                            @Override
                            public TreeTableCell<Client,Boolean> call( TreeTableColumn<Client,Boolean> p ) {
                                CheckBoxTreeTableCell<Client,Boolean> cell = new CheckBoxTreeTableCell<Client,Boolean>();
                                cell.setAlignment(Pos.CENTER);
                                return cell;
                            }
                        });
                        sectColumnSusp.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, Boolean>, //
                        ObservableValue<Boolean>>() {

                            @Override
                            public ObservableValue<Boolean> call(TreeTableColumn.CellDataFeatures<Client, Boolean> param) {
                                TreeItem<Client> treeItem = param.getValue();
                                Client emp = treeItem.getValue();
                                SimpleBooleanProperty booleanProp= new SimpleBooleanProperty(emp.isAsuspendre());

                                // CheckBoxTreeTableCell.
                                // When "A suspendre?" column change.
                                booleanProp.addListener(new ChangeListener<Boolean>() {

                                    @Override
                                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                            Boolean newValue) {
                                        
                                        ///System.out.println("check1 ok");
                                        if(emp.getPort()==0 || emp.getCarte()==0 || emp.getEq()==0){
                                            String message="";
                                            if(emp.getPort()==0){
                                                message+=" aucun port ";
                                            }
                                            if(emp.getCarte()==0){
                                                message+=" aucune carte ";
                                            }
                                            if(emp.getEq()==0){
                                                message+=" aucun equipement ";
                                            }
                                            if(newValue)
                                                new Notifier("Gestab Notification",emp.getNumTel()+" n'est rattaché à "+message+" donc ne peut être suspendu",3,dao);
                                            else
                                                new Notifier("Gestab Notification",emp.getNumTel()+" n'est rattaché à "+message+" donc ne peut être non suspendu",3,dao);
                                            
                                            
                                        }else{
                                            emp.setAsuspendre(newValue);
                                            Connecter c=null;
                                                try {
                                                    c = new Connecter();
                                                } catch (ClassNotFoundException ex) {
                                                    Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (SQLException ex) {
                                                    emp.setValider(oldValue);
                                                    new Notifier("Gestab Error","probleme de connection:"+ex,3,dao);
                                                    Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                Requettes con = new Requettes(c);
                                                try {
                                                    con.fUpdateClient(emp);
                                                    c.getConnexion().close();
                                                    dao.getClient().replace(emp.getId(), emp);
                                                    if(newValue)
                                                        new Notifier("Gestab Notification",emp.getNumTel()+" à suspendre",2,dao);
                                                    else
                                                        new Notifier("Gestab Notification",emp.getNumTel()+" à ne pas suspendre",2,dao);
                                                } catch (ClassNotFoundException ex) {
                                                    Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (SQLException ex) {
                                                    emp.setValider(oldValue);
                                                    new Notifier("Gestab Error","probleme de requette:"+ex,3,dao);
                                                    Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (ParseException ex) {
                                                    Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                        }        
                                    }                     
                                });
                                return booleanProp;
                            }
                        });
                        // ==== Valider? (CHECH BOX) ===
                        sectColumnSuspVal.setCellFactory(new Callback<TreeTableColumn<Client,Boolean>,TreeTableCell<Client,Boolean>>() {
                            @Override
                            public TreeTableCell<Client,Boolean> call( TreeTableColumn<Client,Boolean> p ) {
                                CheckBoxTreeTableCell<Client,Boolean> cell = new CheckBoxTreeTableCell<Client,Boolean>();
                                cell.setAlignment(Pos.CENTER);
                                return cell;
                            }
                        });
                        sectColumnSuspVal.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, Boolean>, //
                        ObservableValue<Boolean>>() {

                            @Override
                            public ObservableValue<Boolean> call(TreeTableColumn.CellDataFeatures<Client, Boolean> param) {
                                TreeItem<Client> treeItem = param.getValue();
                                Client emp = treeItem.getValue();
                                SimpleBooleanProperty booleanProp= new SimpleBooleanProperty(emp.isValider());

                                // Note: singleCol.setOnEditCommit(): Not work for
                                // CheckBoxTreeTableCell.
                                // When "valider?" column change.
                                booleanProp.addListener(new ChangeListener<Boolean>() {

                                    @Override
                                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                            Boolean newValue) {
                                        
                                        //System.out.println("check2 ok");
                                        if(emp.getPort()==0 || emp.getCarte()==0 || emp.getEq()==0){
                                            String message="";
                                            if(emp.getPort()==0){
                                                message+=" aucun port ";
                                            }
                                            if(emp.getCarte()==0){
                                                message+=" aucune carte ";
                                            }
                                            if(emp.getEq()==0){
                                                message+=" aucun equipement ";
                                            }
                                            if(newValue)
                                                new Notifier("Gestab Notification",emp.getNumTel()+" n'est rattaché à "+message+" donc ne peut être suspendu",3,dao);
                                            else
                                                new Notifier("Gestab Notification",emp.getNumTel()+" n'est rattaché à "+message+" donc ne peut être non suspendu",3,dao);
                                        }else{
                                            emp.setValider(newValue);
                                            Connecter c=null;
                                                try {
                                                    c = new Connecter();
                                                } catch (ClassNotFoundException ex) {
                                                    Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (SQLException ex) {
                                                    emp.setValider(oldValue);
                                                    new Notifier("Gestab Error","probleme de connection:"+ex,3,dao);
                                                    Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                Requettes con = new Requettes(c);
                                                try {
                                                    con.fUpdateClient(emp);
                                                    c.getConnexion().close();
                                                    dao.getClient().replace(emp.getId(), emp);
                                                    if(newValue)
                                                        new Notifier("Gestab Notification",emp.getNumTel()+" suspenssion validée",2,dao);
                                                    else
                                                        new Notifier("Gestab Notification",emp.getNumTel()+" suspenssion non validée",2,dao);
                                                } catch (ClassNotFoundException ex) {
                                                    Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (SQLException ex) {
                                                    emp.setValider(oldValue);
                                                    new Notifier("Gestab Error","probleme de requette:"+ex,3,dao);
                                                    Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (ParseException ex) {
                                                    Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                        }        
                                    }                     
                                });
                                return booleanProp;
                            }
                        });
                        
                        // ==== Suspendre? (CHECH BOX) ===
                        sectColumnSuspAdmin.setCellFactory(new Callback<TreeTableColumn<Client,Boolean>,TreeTableCell<Client,Boolean>>() {
                            @Override
                            public TreeTableCell<Client,Boolean> call( TreeTableColumn<Client,Boolean> p ) {
                                CheckBoxTreeTableCell<Client,Boolean> cell = new CheckBoxTreeTableCell<Client,Boolean>();
                                cell.setAlignment(Pos.CENTER);
                                return cell;
                            }
                        });
                        
                        sectColumnSuspAdmin.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Client, Boolean>, //
                        ObservableValue<Boolean>>() {

                            @Override
                            public ObservableValue<Boolean> call(TreeTableColumn.CellDataFeatures<Client, Boolean> param) {
                                TreeItem<Client> treeItem = param.getValue();
                                Client emp = treeItem.getValue();
                                SimpleBooleanProperty booleanProp= new SimpleBooleanProperty(emp.isSuspendre());

                                // CheckBoxTreeTableCell.
                                // When "Activer admin" column change.
                                booleanProp.addListener(new ChangeListener<Boolean>() {

                                    @Override
                                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                            Boolean newValue) {
                                        
                                        //System.out.println("check3 ok");
                                        if(emp.getPort()==0 || emp.getCarte()==0 || emp.getEq()==0){
                                            String message="";
                                            if(emp.getPort()==0){
                                                message+=" aucun port ";
                                            }
                                            if(emp.getCarte()==0){
                                                message+=" aucune carte ";
                                            }
                                            if(emp.getEq()==0){
                                                message+=" aucun equipement ";
                                            }
                                            if(newValue)
                                                new Notifier("Gestab Notification",emp.getNumTel()+" n'est rattaché à "+message+" donc ne peut être suspendu",3,dao);
                                            else
                                                new Notifier("Gestab Notification",emp.getNumTel()+" n'est rattaché à "+message+" donc ne peut être non suspendu",3,dao);
                                        }else{
                                                //emp.setSuspendre(newValue);

                                                Dialog<Pair<String, String>> dialog = new Dialog<>();
                                                dialog.setTitle("Login Dialog");
                                                Equipement e = (Equipement) dao.getEquipement().get(emp.getEq());
                                                Port port = (Port)dao.getPort().get(emp.getPort());

                                                dialog.setHeaderText(e.getNom()+", paramètre connection");
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
                                                if(dao.login!=null)
                                                    username.setText(dao.login);
                                                else    
                                                    username.setPromptText("Username");
                                                PasswordField password = new PasswordField();
                                                if(dao.passwd!=null)
                                                    password.setText(dao.passwd);
                                                else    
                                                    password.setPromptText("Password");

                                                grid.add(new Label("Username:"), 0, 0);
                                                grid.add(username, 1, 0);
                                                grid.add(new Label("Password:"), 0, 1);
                                                grid.add(password, 1, 1);
                                                // Enable/Disable login button depending on whether a username was entered.
                                                Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
                                                loginButton.setDisable(true);

                                                // Do some validation (using the Java 8 lambda syntax).
                                                password.textProperty().addListener((observab, oldVal, newVal) -> {
                                                    loginButton.setDisable(newVal.trim().isEmpty());
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

                                            if(newValue){
                                                result.ifPresent(usernamePassword -> {
                                                    //System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
                                                    emp.setStatus("ACTIF");
                                                    dao.login = usernamePassword.getValue();
                                                    dao.passwd = usernamePassword.getKey();
                                                    if(e.isSSH()){
                                                        try {
                                                            Desactiver modalClient = new Desactiver(t,dao,port,emp,usernamePassword.getKey(),usernamePassword.getValue());

                                                        } catch (Exception ex) {
                                                            Logger.getLogger(PaneClientView.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    }else{
                                                        ModalTelnetActiver modal = new ModalTelnetActiver(t,dao,usernamePassword.getKey(),usernamePassword.getValue(),port,emp);
                                                        modal.construire();
                                                    }
                                                });

                                            }else{
                                                result.ifPresent(usernamePassword -> {
                                                    //System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
                                                    emp.setStatus("SUSPENDU");
                                                    if(e.isSSH()){
                                                        try {
                                                            Desactiver modalClient = new Desactiver(t,dao,port,emp,usernamePassword.getKey(),usernamePassword.getValue());

                                                        } catch (Exception ex) {
                                                            Logger.getLogger(PaneClientView.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    }else{
                                                        ModalTelnetDesactiver modal = new ModalTelnetDesactiver(t,dao,usernamePassword.getKey(),usernamePassword.getValue(),port,emp);
                                                        modal.construire();
                                                    }
                                                });

                                            }
                                        }
                                    }    
                                });
                                return booleanProp;
                            }
                        });
                        
                        TreeTableView<Client> treeTableViewClient = new TreeTableView<>(rootClt);
                        if(dao.isSuperAdmin(dao.user)||dao.isUser(dao.user)||dao.isTechUser(dao.user)||dao.isSubUser(dao.user)){
                            treeTableViewClient.getColumns().addAll(sectColumnCom,sectColumnNom,sectColumnStatut,sectColumnTel,sectColumnEmail,sectColumnDateC,sectColumnDateAct,sectColumnDateS,sectColumnDateR,sectColumnVilleR,sectColumnEside,sectColumnVEQ,
                                sectColumnSEQ,sectColumnEQ,sectColumnCarteEQ,sectColumnPortEQ,sectColumnDebit,sectColumnCat);
                        }else{
                            treeTableViewClient.getColumns().addAll(sectColumnTel,sectColumnDateC,sectColumnVEQ,
                                sectColumnSEQ,sectColumnEQ,sectColumnCarteEQ,sectColumnPortEQ);
                        }
                        treeTableViewClient.setMinWidth(((dao.width*90)/100)*80/100);
                        treeTableViewClient.setEditable(true);
                        treeTableViewClient.setShowRoot(false);
                        
                        
                        
                        
                        
                        sp.setContent(treeTableViewClient);
                        sp.setMaxWidth(((dao.width*90)/100)*80/100);
                        sp.setMinWidth(((dao.width*90)/100)*80/100);
                        btnExcel.setDisable(false);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalStatClient.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ModalStatClient.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(ModalStatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                
                }
        });
       
       Button btnReset = new Button("reset ");
       btnReset.getStyleClass().add(dao.colorannuler()); 
       
       
       
       
       if(dao.isSuperAdmin(dao.user)||dao.isUser(dao.user)||dao.isTechUser(dao.user)||dao.isSubUser(dao.user)){
        vb.getChildren().addAll(headL,debit,debitCB,debitL,cat,categorieCB,catL,villel,villeCB,villeL,stat,statusCB,date1,dateFrom,date2,dateTo);
       }
       
       
       
       
       
       //Grid attachement
       
       //secteur
       ObservableList<Secteur> optionsSect = FXCollections.observableArrayList();
        ComboBox secteurCB = new ComboBox<>(optionsSect);
        secteurCB.getStyleClass().add(dao.colorannuler());
        
        //equipement
        ObservableList<Equipement> optionsEQ = FXCollections.observableArrayList();
        ComboBox eqCB = new ComboBox<>(optionsEQ);
        eqCB.getStyleClass().add(dao.colorannuler());
        
        //cartes
        ObservableList<Carte> optionsCarte = FXCollections.observableArrayList();
        ComboBox carteCB = new ComboBox<>(optionsCarte);
        carteCB.getStyleClass().add(dao.colorannuler());
        
        //ports
        ObservableList<Port> optionsPort = FXCollections.observableArrayList();
        ComboBox portCB = new ComboBox<>(optionsPort);
        portCB.getStyleClass().add(dao.colorannuler());
       
        
       
        ComboBox VilleEQCB = new ComboBox<>(options);
        VilleEQCB.getStyleClass().add(dao.colorannuler());
        VilleEQCB.setPromptText("Toutes");
        
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
              
              villeE = t1;
              if(t1!=null){
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

                 secteurCB.setPromptText("Tout");
              }    
                
            }    
        });
        
        // secteur
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

                     carteCB.setPromptText("Tout");
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
        
        btnReset.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    statusPCB.setValue(null);
                    statusPCB.setPromptText("Tout");
                    statusCB.setValue(null);
                    statusCB.setPromptText("Tout");
                    categorieCB.setValue(null);
                    catL.setText("");
                    categorieCB.setPromptText("Toutes");
                    debitCB.setValue(null);
                    debitL.setText("");
                    debitCB.setPromptText("Tout");
                    villeCB.setValue(null);
                    villel.setText("");
                    villeCB.setPromptText("Toutes");
                    VilleEQCB.setValue(null);
                    villeEQL.setText("");
                    VilleEQCB.setPromptText("Toutes");
                    secteurCB.setValue(null);
                    secteurL.setText("");
                    eqL.setText("");
                    carteL.setText("");
                    portL.setText("");
                    suspCB.setValue(null);
                    suspCB.setPromptText("Tout");
                    validerCB.setValue(null);
                    validerCB.setPromptText("Tout");
                    
                    
                    
                    /*dateTo = null;
                    dateFrom = null;
                    
                    ville = null;
                    debitR = null;
                    categorieR = null;
                    statutR = null;
                    portStatutR = null;
                    villeE = null;
                    sectR = null;
                    eqR = null;
                    carteR = null;
                    portR = null;*/
                    
                    
                    
                    
                
                }
        });
        
       
       
       
        
        
        
        
        Label headEL = new Label("Filtre Attachement");
        headEL.setTextFill(Color.web(dao.couleur));
        headEL.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        HBox attachHB = new HBox(5);
        VBox villeVB = new VBox(3);
        
        villeVB.getChildren().addAll(new Label("Villes:"),VilleEQCB,villeEQL);
        VBox secteurVB = new VBox(3);
        
        secteurVB.getChildren().addAll(new Label("Secteurs:"),secteurCB,secteurL);
        VBox eqVB = new VBox(3);
        
        eqVB.getChildren().addAll(new Label("Equipements:"),eqCB,eqL);
        VBox carteVB = new VBox(3);
        
        carteVB.getChildren().addAll(new Label("Cartes:"),carteCB,carteL);
        VBox portVB = new VBox(3);
        
        portVB.getChildren().addAll(new Label("Ports:"),portCB,portL);
        VBox statutVB = new VBox(3);
        
        statutVB.getChildren().addAll(new Label("Status du Port:"),statusPCB);
        
        attachHB.getChildren().addAll(headEL,villeVB,secteurVB,eqVB,carteVB,portVB,statutVB);
        
        
        
        //grid.add(headEL, 0,0 );
        
       
        
        sp = new ScrollPane();
        ScrollPane sph = new ScrollPane();
        sp.setPrefSize(AUTO,AUTO);
        sph.setPrefSize(AUTO,AUTO);
        
       sp.setPadding(new Insets(10, 10, 10, 10));
       sph.setPadding(new Insets(10, 10, 10, 10));
       hBtn.getStyleClass().add("gridCarte");
       vStat.getChildren().addAll(hBtn,sp);
       //vb.getStyleClass().add("gridCarte");
       hb.getChildren().addAll(vb,vStat);
       
       Label mainHead = new Label("Statistiques Abonnés");
       mainHead.setTextFill(Color.web(dao.couleur));
       mainHead.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
       
       HBox HheadB = new HBox();
       sph.setContent(attachHB);
       //sph.setMaxWidth(((dao.width*90)/100)*80/100);
       
       HheadB.getChildren().addAll(mainHead,attachHB);
       
       HBox dowhb = new HBox(5);
       Label footerL = new Label("Avec");
       footerL.setTextFill(Color.web(dao.couleur));
       footerL.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
       if(dao.isSuperAdmin(dao.user)||dao.isUser(dao.user)||dao.isTechUser(dao.user)||dao.isSubUser(dao.user)){
            dowhb.getChildren().addAll(btnRech,btnReset,footerL,new Label("A suspendre"),suspCB,new Label("Valider"),validerCB);
       }else{
           dowhb.getChildren().addAll(btnRech,btnReset);
       }
       mainVb.getChildren().addAll(HheadB,hb,dowhb);
       
       
        
        
        mainVb.setPadding(new Insets (10,10,10,10));
        scene = new Scene(mainVb, (dao.width*90)/100, (dao.height*90)/100);
        scene.getStylesheets().add("css/style.css");
                
               
                
               
        stage.setScene(scene);
        stage.setTitle("Statistiques Abonnés");
        stage.initModality(Modality.NONE);
        stage.initOwner(
        ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
    
    public void excellgenerator(){
        
        
        HSSFWorkbook wb = new HSSFWorkbook();
        
        HSSFFont font = wb.createFont();
        //font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
        font.setFontHeightInPoints((short)10);
        font.setColor(HSSFColor.WHITE.index);
        
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.BLACK.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFont(font);

        HSSFCellStyle cellStyleContent = wb.createCellStyle();
        cellStyleContent.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleContent.setFillForegroundColor(HSSFColor.PINK.index);
        cellStyleContent.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyleContent.setFont(font);


        HSSFCellStyle goodStyle = wb.createCellStyle();
        goodStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        goodStyle.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
        goodStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        goodStyle.setFont(font);
        /*goodStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        goodStyle.setBottomBorderColor(HSSFColor.SEA_GREEN.index);
        goodStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        goodStyle.setLeftBorderColor(HSSFColor.SEA_GREEN.index);
        goodStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        goodStyle.setRightBorderColor(HSSFColor.SEA_GREEN.index);
        goodStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        goodStyle.setTopBorderColor(HSSFColor.SEA_GREEN.index);*/

        HSSFCellStyle dangerStyle = wb.createCellStyle();
        dangerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        dangerStyle.setFillForegroundColor(HSSFColor.RED.index);
        dangerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        dangerStyle.setFont(font);
        /*dangerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        dangerStyle.setBottomBorderColor(HSSFColor.RED.index);
        dangerStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        dangerStyle.setLeftBorderColor(HSSFColor.RED.index);
        dangerStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        dangerStyle.setRightBorderColor(HSSFColor.RED.index);
        dangerStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        dangerStyle.setTopBorderColor(HSSFColor.RED.index);*/

        HSSFCellStyle oldStyle = wb.createCellStyle();
        oldStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        oldStyle.setBottomBorderColor(HSSFColor.ROSE.index);
        oldStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        oldStyle.setLeftBorderColor(HSSFColor.ROSE.index);
        oldStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        oldStyle.setRightBorderColor(HSSFColor.ROSE.index);
        oldStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        oldStyle.setTopBorderColor(HSSFColor.ROSE.index);
        
        //cell pour superadmin et gestionnaire
        if(dao.isSuperAdmin(dao.user)||dao.isUser(dao.user)||dao.isTechUser(dao.user)||dao.isSubUser(dao.user)){
                HSSFSheet sheet = wb.createSheet("données");
                HSSFRow rowTitre = sheet.createRow(0);
                HSSFRow rowTitre2 = sheet.createRow(1);
                HSSFCell cellTitre = rowTitre.createCell(0,HSSFCell.CELL_TYPE_STRING);
                cellTitre.setCellValue("ABONNES ADSL DE:");
                sheet.addMergedRegion(new CellRangeAddress(0,(short)1,0,(short)3));
                
                HSSFCell cellVil = rowTitre.createCell(4,HSSFCell.CELL_TYPE_STRING);
                if(villeE!=null){
                    cellVil.setCellValue("ville Equipement:"+villeE.getNom());
                }else{
                    cellVil.setCellValue("ville Equipement: Toutes");
                }
                HSSFCell cellSect = rowTitre.createCell(5,HSSFCell.CELL_TYPE_STRING);
                if(sectR!=null){
                    cellSect.setCellValue("Secteur Equipement:"+sectR.getNom());
                }else{
                    cellSect.setCellValue("Secteur Equipement: Tous");
                }
                HSSFCell cellEq = rowTitre.createCell(6,HSSFCell.CELL_TYPE_STRING);
                if(eqR!=null){
                    cellEq.setCellValue("Equipement:"+eqR.getNom());
                }else{
                    cellEq.setCellValue("Equipement: Tous");
                }
                HSSFCell cellCart = rowTitre.createCell(7,HSSFCell.CELL_TYPE_STRING);
                if(carteR!=null){
                    cellCart.setCellValue("Carte:"+carteR.getNumCarte());
                }else{
                    cellCart.setCellValue("Carte: Toutes");
                }
                HSSFCell cellPort = rowTitre.createCell(8,HSSFCell.CELL_TYPE_STRING);
                if(portR!=null){
                    cellPort.setCellValue("Port:"+portR.getNumeroPort());
                }else{
                    cellPort.setCellValue("Port: Tous");
                }
                HSSFCell cellPortStatut = rowTitre.createCell(9,HSSFCell.CELL_TYPE_STRING);
                if(portStatutR!=null){
                    cellPortStatut.setCellValue("Port Statut:"+portStatutR);
                }else{
                    cellPortStatut.setCellValue("Port Statut: Tous");
                }
                HSSFCell cellAsuspendre = rowTitre.createCell(10,HSSFCell.CELL_TYPE_STRING);
                if(aSusp!=null){
                    if(aSusp)
                        cellAsuspendre.setCellValue("à suspendre: oui");
                    else
                        cellAsuspendre.setCellValue("à suspendre: non");
                }else{
                    cellAsuspendre.setCellValue("à suspendre: Tous");
                }
                
                HSSFCell cellValider = rowTitre.createCell(11,HSSFCell.CELL_TYPE_STRING);
                if(valider!=null){
                    if(valider)
                        cellValider.setCellValue("suspension validé: oui");
                    else
                        cellValider.setCellValue("suspension validé: non");
                }else{
                    cellValider.setCellValue("suspension validé: Tous");
                }
                
                //celltitre2
                HSSFCell cellDeb = rowTitre2.createCell(4,HSSFCell.CELL_TYPE_STRING);
                if(debitR!=null){
                    cellDeb.setCellValue("Debit:"+debitR.getNom());
                }else{
                    cellDeb.setCellValue("Debit: Tous");
                }
                HSSFCell cellCat = rowTitre2.createCell(5,HSSFCell.CELL_TYPE_STRING);
                if(categorieR!=null){
                    cellCat.setCellValue("Categorie:"+categorieR.getNom());
                }else{
                    cellCat.setCellValue("Categorie: Toutes");
                }
                HSSFCell cellCltVille = rowTitre2.createCell(6,HSSFCell.CELL_TYPE_STRING);
                if(ville!=null){
                    cellCltVille.setCellValue("Ville:"+ville.getNom());
                }else{
                    cellCltVille.setCellValue("Ville: Toutes");
                }
                HSSFCell cellStatut = rowTitre2.createCell(7,HSSFCell.CELL_TYPE_STRING);
                if(statutR!=null){
                    cellStatut.setCellValue("Statut client:"+statutR);
                }else{
                    cellStatut.setCellValue("Statut client: Tous");
                }
                
                HSSFCell cellDate = rowTitre2.createCell(8,HSSFCell.CELL_TYPE_STRING);
                if(dateFrom.getValue()!=null){
                    cellDate.setCellValue("de(Date):"+dateFrom.getValue());
                }else{
                    cellDate.setCellValue("de(Date): Toutes");
                }
                
                if(dateTo.getValue()!=null){
                    HSSFCell cellDate2 = rowTitre2.createCell(9,HSSFCell.CELL_TYPE_STRING);
                    cellDate2.setCellValue("à(Date):"+dateTo.getValue());
                }
                
                HSSFRow row = sheet.createRow(2);
                
                
                
                
                HSSFCell cell = row.createCell(0,HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(new HSSFRichTextString("Nom"));
                cell.setCellStyle(cellStyle);
                
                HSSFCell cell1 = row.createCell(1,HSSFCell.CELL_TYPE_STRING);
                cell1.setCellValue(new HSSFRichTextString("Statut"));
                cell1.setCellStyle(cellStyle);
                
                HSSFCell cell2 = row.createCell(2,HSSFCell.CELL_TYPE_STRING);
                cell2.setCellValue(new HSSFRichTextString("Telephone"));
                cell2.setCellStyle(cellStyle);
                
                HSSFCell cell3 = row.createCell(3,HSSFCell.CELL_TYPE_STRING);
                cell3.setCellValue(new HSSFRichTextString("Email"));
                cell3.setCellStyle(cellStyle);
                
                HSSFCell cell4 = row.createCell(4,HSSFCell.CELL_TYPE_STRING);
                cell4.setCellValue(new HSSFRichTextString("Date de création"));
                cell4.setCellStyle(cellStyle);
                
                HSSFCell cell5 = row.createCell(5,HSSFCell.CELL_TYPE_STRING);
                cell5.setCellValue(new HSSFRichTextString("Date Activation"));
                cell5.setCellStyle(cellStyle);
                
                HSSFCell cell6 = row.createCell(6,HSSFCell.CELL_TYPE_STRING);
                cell6.setCellValue(new HSSFRichTextString("Date suspension"));
                cell6.setCellStyle(cellStyle);
                
                HSSFCell cell7 = row.createCell(7,HSSFCell.CELL_TYPE_STRING);
                cell7.setCellValue(new HSSFRichTextString("Date résiliation"));
                cell7.setCellStyle(cellStyle);
                
                HSSFCell cell8 = row.createCell(8,HSSFCell.CELL_TYPE_STRING);
                cell8.setCellValue(new HSSFRichTextString("Ville de résidence"));
                cell8.setCellStyle(cellStyle);
                
                HSSFCell cell9 = row.createCell(9,HSSFCell.CELL_TYPE_STRING);
                cell9.setCellValue(new HSSFRichTextString("E-side"));
                cell9.setCellStyle(cellStyle);
                
                HSSFCell cell10 = row.createCell(10,HSSFCell.CELL_TYPE_STRING);
                cell10.setCellValue(new HSSFRichTextString("Ville d'attachement"));
                cell10.setCellStyle(cellStyle);
                
                HSSFCell cell11 = row.createCell(11,HSSFCell.CELL_TYPE_STRING);
                cell11.setCellValue(new HSSFRichTextString("Secteur d'attachement"));
                cell11.setCellStyle(cellStyle);
                
                HSSFCell cell12 = row.createCell(12,HSSFCell.CELL_TYPE_STRING);
                cell12.setCellValue(new HSSFRichTextString("Equipement d'attachement"));
                cell12.setCellStyle(cellStyle);
                
                HSSFCell cell13 = row.createCell(13,HSSFCell.CELL_TYPE_STRING);
                cell13.setCellValue(new HSSFRichTextString("Carte d'attachement"));
                cell13.setCellStyle(cellStyle);
                
                HSSFCell cell14 = row.createCell(14,HSSFCell.CELL_TYPE_STRING);
                cell14.setCellValue(new HSSFRichTextString("Port d'attachement"));
                cell14.setCellStyle(cellStyle);
                
                HSSFCell cell15 = row.createCell(15,HSSFCell.CELL_TYPE_STRING);
                cell15.setCellValue(new HSSFRichTextString("Debit"));
                cell15.setCellStyle(cellStyle);
                
                HSSFCell cell16 = row.createCell(16,HSSFCell.CELL_TYPE_STRING);
                cell16.setCellValue(new HSSFRichTextString("Categorie"));
                cell16.setCellStyle(cellStyle);
                
                HSSFCell cell17 = row.createCell(17,HSSFCell.CELL_TYPE_STRING);
                cell17.setCellValue(new HSSFRichTextString("A Suspendre"));
                cell17.setCellStyle(cellStyle);
                
                HSSFCell cell18 = row.createCell(18,HSSFCell.CELL_TYPE_STRING);
                cell18.setCellValue(new HSSFRichTextString("Suspension validé"));
                cell18.setCellStyle(cellStyle);
                
                Iterator<Client> it = list.iterator();
                int i = 3;
                while (it.hasNext()) {
                    Client s = it.next();
                    HSSFRow rowClient = sheet.createRow(i);
                    
                    HSSFCell cellClient = rowClient.createCell(0,HSSFCell.CELL_TYPE_STRING);
                    cellClient.setCellValue(new HSSFRichTextString(s.getNom()+" "+s.getPrenom()));
                  

                    HSSFCell cellClient1 = rowClient.createCell(1,HSSFCell.CELL_TYPE_STRING);
                    cellClient1.setCellValue(new HSSFRichTextString(s.getStatut()));
                    if(s.isactif()){
                        cellClient1.setCellStyle(goodStyle);
                    }
                    if(s.isSusp()){
                        cellClient1.setCellStyle(dangerStyle);
                    }
                    if(s.isResil()){
                        cellClient1.setCellStyle(oldStyle);
                    } 

                    HSSFCell cellClient2 = rowClient.createCell(2,HSSFCell.CELL_TYPE_STRING);
                    cellClient2.setCellValue(new HSSFRichTextString(s.getNumTel()));
                    
                    HSSFCell cellClient3 = rowClient.createCell(3,HSSFCell.CELL_TYPE_STRING);
                    cellClient3.setCellValue(new HSSFRichTextString(s.getEmail()));

                    HSSFCell cellClient4 = rowClient.createCell(4,HSSFCell.CELL_TYPE_STRING);
                    cellClient4.setCellValue(new HSSFRichTextString(s.getDateCreation()));
                    
                    List list = (List) dao.getClientHistorique().get(s.getId());
                    Iterator<Historique> itdate = list.iterator();
                    String dateAct="";
                    String dateSusp="";
                    String dateRes="";
                    while (itdate.hasNext()) {
                           Historique sdate = itdate.next();
                           if(sdate.getStatus()==1)
                                dateAct+=sdate.getDate()+"/";
                           if(sdate.getStatus()==2)
                                dateSusp+=sdate.getDate()+"/";
                           if(sdate.getStatus()==3)
                                dateRes+=sdate.getDate()+"/";
                    }
                    HSSFCell cellClient5 = rowClient.createCell(5,HSSFCell.CELL_TYPE_STRING);
                    cellClient5.setCellValue(dateAct);
                    
                    HSSFCell cellClient6 = rowClient.createCell(6,HSSFCell.CELL_TYPE_STRING);
                    cellClient6.setCellValue(dateSusp);
               

                    HSSFCell cellClient7 = rowClient.createCell(7,HSSFCell.CELL_TYPE_STRING);
                    cellClient7.setCellValue(dateRes);
                    

                    HSSFCell cellClient8 = rowClient.createCell(8,HSSFCell.CELL_TYPE_STRING);
                    Ville ville = (Ville) dao.getVille().get(s.getVilleOwner());
                    cellClient8.setCellValue(new HSSFRichTextString(ville.getNom()));
                 

                    HSSFCell cellClient9 = rowClient.createCell(9,HSSFCell.CELL_TYPE_STRING);
                    cellClient9.setCellValue(new HSSFRichTextString(s.getEside()));
                    

                    HSSFCell cellClient10 = rowClient.createCell(10,HSSFCell.CELL_TYPE_STRING);
                    Ville villeEQ = (Ville) dao.getVille().get(s.getVille());
                    if(villeEQ!=null){
                        cellClient10.setCellValue(new HSSFRichTextString(villeEQ.getNom()));
                    }else{
                        cellClient10.setCellValue(new HSSFRichTextString("N/A"));
                    }
                    cellClient10.setCellStyle(cellStyleContent);
                    

                    HSSFCell cellClient11 = rowClient.createCell(11,HSSFCell.CELL_TYPE_STRING);
                    cellClient11.setCellStyle(oldStyle);
                    Secteur sect = (Secteur) dao.getSecteur().get(s.getSecteur());
                    if(sect!=null){
                        cellClient11.setCellValue(new HSSFRichTextString(sect.getNom()));
                    }else{
                        cellClient11.setCellValue(new HSSFRichTextString("N/A"));
                    }
                    

                    HSSFCell cellClient12 = rowClient.createCell(12,HSSFCell.CELL_TYPE_STRING);
                    cellClient12.setCellValue(new HSSFRichTextString("Equipement d'attachement"));
                    cellClient12.setCellStyle(oldStyle);
                    Equipement eq = (Equipement) dao.getEquipement().get(s.getEq());
                    if(eq!=null){
                        cellClient12.setCellValue(new HSSFRichTextString(eq.getNom()));
                    }else{
                        cellClient12.setCellValue(new HSSFRichTextString("N/A"));
                    }

                    HSSFCell cellClient13 = rowClient.createCell(13,HSSFCell.CELL_TYPE_STRING);
                    cellClient13.setCellStyle(oldStyle);
                    Carte carte = (Carte) dao.getCarte().get(s.getCarte());
                    if(carte!=null){
                        cellClient13.setCellValue(new HSSFRichTextString("carte "+carte.getNumCarte()));
                    }else{
                        cellClient13.setCellValue(new HSSFRichTextString("N/A"));
                    }
                   

                    HSSFCell cellClient14 = rowClient.createCell(14,HSSFCell.CELL_TYPE_STRING);
                    cellClient14.setCellStyle(oldStyle);
                    Port port = (Port) dao.getPort().get(s.getPort());
                    
                    if(port!=null){
                        cellClient14.setCellValue(new HSSFRichTextString("Port "+port.getNumeroPort()));
                    }else{
                        cellClient14.setCellValue(new HSSFRichTextString("N/A"));
                    }
                    

                    HSSFCell cellClient15 = rowClient.createCell(15,HSSFCell.CELL_TYPE_STRING);
                    Debit debit = (Debit) dao.getDebit().get(s.getDebit());
                    if(debit!=null){
                        cellClient15.setCellValue(new HSSFRichTextString(debit.getNom()));
                    }else{
                        cellClient15.setCellValue(new HSSFRichTextString("N/A"));
                    }
                   

                    HSSFCell cellClient16 = rowClient.createCell(16,HSSFCell.CELL_TYPE_STRING);
                    Categorie catF = (Categorie) dao.getCategorieForfait().get(s.getCategorie());
                    if(catF!=null){
                        cellClient16.setCellValue(new HSSFRichTextString(catF.getNom()));
                    }else{
                        cellClient16.setCellValue(new HSSFRichTextString("N/A"));
                    }
                    
                    HSSFCell cellClient17 = rowClient.createCell(17,HSSFCell.CELL_TYPE_STRING);
                    if(s.isAsuspendre()){
                        cellClient17.setCellValue("oui");
                        cellClient17.setCellStyle(goodStyle);
                    }else{
                        cellClient17.setCellValue("non");
                        cellClient17.setCellStyle(dangerStyle);
                    }    
                    
                    HSSFCell cellClient18 = rowClient.createCell(18,HSSFCell.CELL_TYPE_STRING);
                    if(s.isValider()){
                        cellClient18.setCellValue("oui");
                        cellClient18.setCellStyle(goodStyle);
                    }else{
                        cellClient18.setCellValue("non");
                        cellClient18.setCellStyle(dangerStyle);
                    }    
                    
                    
                    i++;
                }
        }else{
        
            HSSFSheet sheet = wb.createSheet("données");
                HSSFRow rowTitre = sheet.createRow(0);
                HSSFRow rowTitre2 = sheet.createRow(1);
                HSSFCell cellTitre = rowTitre.createCell(0,HSSFCell.CELL_TYPE_STRING);
                cellTitre.setCellValue("ABONNES ADSL DE:");
                sheet.addMergedRegion(new CellRangeAddress(0,(short)1,0,(short)3));
                
                HSSFCell cellVil = rowTitre.createCell(4,HSSFCell.CELL_TYPE_STRING);
                if(villeE!=null){
                    cellVil.setCellValue("ville Equipement:"+villeE.getNom());
                }else{
                    cellVil.setCellValue("ville Equipement: Toutes");
                }
                HSSFCell cellSect = rowTitre.createCell(5,HSSFCell.CELL_TYPE_STRING);
                if(sectR!=null){
                    cellSect.setCellValue("Secteur Equipement:"+sectR.getNom());
                }else{
                    cellSect.setCellValue("Secteur Equipement: Tous");
                }
                HSSFCell cellEq = rowTitre.createCell(6,HSSFCell.CELL_TYPE_STRING);
                if(eqR!=null){
                    cellEq.setCellValue("Equipement:"+eqR.getNom());
                }else{
                    cellEq.setCellValue("Equipement: Tous");
                }
                HSSFCell cellCart = rowTitre.createCell(7,HSSFCell.CELL_TYPE_STRING);
                if(carteR!=null){
                    cellCart.setCellValue("Carte:"+carteR.getNumCarte());
                }else{
                    cellCart.setCellValue("Carte: Toutes");
                }
                HSSFCell cellPort = rowTitre.createCell(8,HSSFCell.CELL_TYPE_STRING);
                if(portR!=null){
                    cellPort.setCellValue("Port:"+portR.getNumeroPort());
                }else{
                    cellPort.setCellValue("Port: Tous");
                }
                HSSFCell cellPortStatut = rowTitre.createCell(9,HSSFCell.CELL_TYPE_STRING);
                if(portStatutR!=null){
                    cellPortStatut.setCellValue("Port Statut:"+portStatutR);
                }else{
                    cellPortStatut.setCellValue("Port Statut: Tous");
                }
                
                
                
                HSSFRow row = sheet.createRow(2);
                
                HSSFCell cell = row.createCell(0,HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(new HSSFRichTextString("Telephone"));
                cell.setCellStyle(cellStyle);
                
                HSSFCell cell1 = row.createCell(1,HSSFCell.CELL_TYPE_STRING);
                cell1.setCellValue(new HSSFRichTextString("Date de création"));
                cell1.setCellStyle(cellStyle);
                
                HSSFCell cell2 = row.createCell(2,HSSFCell.CELL_TYPE_STRING);
                cell2.setCellValue(new HSSFRichTextString("Ville d'attachement"));
                cell2.setCellStyle(cellStyle);
                
                HSSFCell cell3 = row.createCell(3,HSSFCell.CELL_TYPE_STRING);
                cell3.setCellValue(new HSSFRichTextString("Secteur d'attachement"));
                cell3.setCellStyle(cellStyle);
                
                HSSFCell cell4 = row.createCell(4,HSSFCell.CELL_TYPE_STRING);
                cell4.setCellValue(new HSSFRichTextString("Equipement d'attachement"));
                cell4.setCellStyle(cellStyle);
                
                HSSFCell cell5 = row.createCell(5,HSSFCell.CELL_TYPE_STRING);
                cell5.setCellValue(new HSSFRichTextString("Carte d'attachement"));
                cell5.setCellStyle(cellStyle);
                
                HSSFCell cell6 = row.createCell(6,HSSFCell.CELL_TYPE_STRING);
                cell6.setCellValue(new HSSFRichTextString("Port d'attachement"));
                cell6.setCellStyle(cellStyle);
                
                Iterator<Client> it = list.iterator();
                int i = 3;
                while (it.hasNext()) {
                    Client s = it.next();
                    HSSFRow rowClient = sheet.createRow(i);
                    
                    

                    HSSFCell cellClient = rowClient.createCell(0,HSSFCell.CELL_TYPE_STRING);
                    cellClient.setCellValue(new HSSFRichTextString(s.getNumTel()));

                    HSSFCell cellClient1 = rowClient.createCell(1,HSSFCell.CELL_TYPE_STRING);
                    cellClient1.setCellValue(new HSSFRichTextString(s.getDateCreation()));
                    

                    HSSFCell cellClient2 = rowClient.createCell(2,HSSFCell.CELL_TYPE_STRING);
                    Ville villeEQ = (Ville) dao.getVille().get(s.getVille());
                    if(villeEQ!=null){
                        cellClient2.setCellValue(new HSSFRichTextString(villeEQ.getNom()));
                    }else{
                        cellClient2.setCellValue(new HSSFRichTextString("N/A"));
                    }
                    cellClient2.setCellStyle(cellStyleContent);
                    

                    HSSFCell cellClient3 = rowClient.createCell(3,HSSFCell.CELL_TYPE_STRING);
                    cellClient3.setCellStyle(oldStyle);
                    Secteur sect = (Secteur) dao.getSecteur().get(s.getSecteur());
                    if(sect!=null){
                        cellClient3.setCellValue(new HSSFRichTextString(sect.getNom()));
                    }else{
                        cellClient3.setCellValue(new HSSFRichTextString("N/A"));
                    }
                    

                    HSSFCell cellClient4 = rowClient.createCell(4,HSSFCell.CELL_TYPE_STRING);
                    cellClient4.setCellValue(new HSSFRichTextString("Equipement d'attachement"));
                    cellClient4.setCellStyle(oldStyle);
                    Equipement eq = (Equipement) dao.getEquipement().get(s.getEq());
                    if(eq!=null){
                        cellClient4.setCellValue(new HSSFRichTextString(eq.getNom()));
                    }else{
                        cellClient4.setCellValue(new HSSFRichTextString("N/A"));
                    }

                    HSSFCell cellClient5 = rowClient.createCell(5,HSSFCell.CELL_TYPE_STRING);
                    cellClient5.setCellStyle(oldStyle);
                    Carte carte = (Carte) dao.getCarte().get(s.getCarte());
                    if(carte!=null){
                        cellClient5.setCellValue(new HSSFRichTextString("carte "+carte.getNumCarte()));
                    }else{
                        cellClient5.setCellValue(new HSSFRichTextString("N/A"));
                    }
                   

                    HSSFCell cellClient6 = rowClient.createCell(6,HSSFCell.CELL_TYPE_STRING);
                    cellClient6.setCellStyle(oldStyle);
                    Port port = (Port) dao.getPort().get(s.getPort());
                    
                    if(port!=null){
                        cellClient6.setCellValue(new HSSFRichTextString("Port "+port.getNumeroPort()));
                    }else{
                        cellClient6.setCellValue(new HSSFRichTextString("N/A"));
                    }    
                    
                    
                    i++;
                }
        }    
                
        
        
        
                FileOutputStream fileOut;
                try {
                    fileOut = new FileOutputStream("gestab.xls");
                    wb.write(fileOut);
                    fileOut.close();
                    new Notifier("GESTAB","fichier excel<gestab.xls> générer",2,dao);
                    dao.hostservice.showDocument("gestab.xls");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
    
    }
    public void pdfgenerator(){
        /*Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document,new FileOutputStream("gestab.pdf"));
            document.addTitle("Statistiques Abonnés");
            document.addAuthor("Gestab");
            document.addSubject("Liste des abonnés.");
            document.addKeywords("iText, gestab");
            document.open();
            document.add(new Paragraph("Hello World"));
        } catch (DocumentException de) {
            de.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        document.close();*/
    }
}
