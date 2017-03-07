/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Carte;
import modele.Categorie;
import modele.Client;
import modele.Debit;
import modele.Port;
import modele.Secteur;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class ModalPaneDetailClient {
    
    
    private final ActionEvent event;
    private final Client client;
    private Image images;
    private ImageView pics;
    private DAOequipement dao;
    
    public ModalPaneDetailClient(DAOequipement dao,ActionEvent event,Client client){
        this.event = event;
        this.client = client;
        this.dao = dao;
        
    }
    
    public void construire(){
        Label label = new Label();
        
        Stage stage = new Stage();
        ///Creation des Variales
        Label lajout = new Label("Details:");
        lajout.setTextFill(Color.web(dao.colordeGestionEq()));
        lajout.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 20));
        lajout.setAlignment(Pos.BOTTOM_LEFT);
        // --- GridPane container
        GridPane grid = new GridPane();
        grid.getStyleClass().add("gridCarte");
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        /*grid.add(new Label("Description/raison social: "), 0, 0);  
        grid.add(new Label("Nom: "), 0, 1);
        grid.add(new Label("Prenom: "), 0, 2);
        Label attach = new Label("ᚄ Attachement: ");
        attach.setTextFill(Color.web(dao.colordeGestionEq()));
        attach.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(attach, 0, 3);
        grid.add(new Label("Ville: "), 0, 4);
        grid.add(new Label("Secteur: "), 0, 5);
        grid.add(new Label("Carte: "), 0, 6);
        grid.add(new Label("Port: "), 0, 7);
        
        grid.add(new Label("Numero Telephone: "), 3, 1);
        grid.add(new Label("Debit: "), 3, 2);
        grid.add(new Label("E-side: "), 3, 3);
        grid.add(new Label("Status: "), 3, 4);
        grid.add(new Label("Date de Creation: "), 3, 5);
        grid.add(new Label("Ville de résidance: "), 3, 6);
        grid.add(new Label("Categorie: "), 3, 7);
        
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
            grid.add(label1,1, 5);
        }
        Carte carte = (Carte) dao.getCarte().get(client.getCarte());
        if(carte!=null){
            final Label label2 = new Label(""+carte.getNumCarte());
            label2.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            grid.add(label2,1, 6);
        }else{
            final Label label2 = new Label("N/A");
            label2.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            grid.add(label2,1, 6);
        }
        Port port = (Port) dao.getPort().get(client.getPort());
        if(port!=null){
            final Label label3 = new Label(""+port.getNumeroPort());
            label3.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            grid.add(label3,1, 7);
        }else{
            final Label label3 = new Label("N/A");
            label3.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
            grid.add(label3,1, 7);
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
        
        final Label label9 = new Label(client.getStatut());
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
        
        images = new Image(getClass().getResourceAsStream("/images/user.png"),200,200,false,false);
        pics = new ImageView(images);
        
        grid.add(pics,3,0);
        
        
        Label lbMessage = new Label("");
        lbMessage.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        Label lbMessage2 = new Label("");
        
        
        
        Button bclose=new Button();
        bclose.getStyleClass().add("buttonGestEq");
        bclose.setText("Fermer");
        
        bclose.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
        });
        
       
        
        
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(bclose);
        grid.add(hbBtn, 4, 10);

        grid.add(lbMessage,1,9);
        */
        VBox VbBtn = new VBox(10);
        VbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        
        /*HBox Lb = new HBox(10);
        Lb.setAlignment(Pos.BOTTOM_RIGHT);
        Lb.getChildren().addAll(lbMessage,lbMessage2);
        */
        VbBtn.getChildren().addAll(lajout,new PaneClientView(dao,client,label,grid,VbBtn).construire());
        
              
                
                ScrollPane sp = new ScrollPane();
        
                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(VbBtn);
                sp.setPadding(new Insets(10, 10, 10, 10));
                
                Scene scene = new Scene(sp, (dao.width*75)/100, (dao.height*70)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Client Détails");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
