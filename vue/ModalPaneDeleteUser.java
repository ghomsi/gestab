/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.util.ArrayList;
import javafx.collections.ObservableList;
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
import modele.User;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class ModalPaneDeleteUser {
    
    private final ActionEvent event;
    private ObservableList<String> options;
    private final User user;
    private final Label labelT;
    private final HBox hbox;
    private final GridPane gridR;
    private TreeTableView<User> treeTableView;
    private VBox paneR;
    private DAOequipement dao;
    
    public ModalPaneDeleteUser(DAOequipement dao,ActionEvent event,User user,HBox hbox,Label label,GridPane grid,VBox pane){
        this.event = event;
        this.user = user;
        this.labelT = label;
        this.hbox = hbox;
        this.gridR = grid;
        this.treeTableView = dao.getTreeTU();
        this.paneR = pane;
        this.dao = dao;
        
    }
    
    void construire(){
    
        Stage stage = new Stage();
        ///Creation des Variales
        Label lajout = new Label("Etes vous sur de vouloir supprimer cet Utilisateur:");
        lajout.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        lajout.setTextFill(Color.web(dao.couleur));
        lajout.setAlignment(Pos.BOTTOM_LEFT);
        // --- GridPane container
        GridPane grid = new GridPane();
        grid.getStyleClass().add("gridCarte");
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("Description/raison social: "), 0, 0);
        
        Label attach = new Label("ᚂ Attachement: ");
        attach.setTextFill(Color.web(dao.couleur));
        attach.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(attach, 0, 3);
        
        grid.add(new Label("Ville: "), 0, 4);
        grid.add(new Label("Quartier: "), 0, 5);
        
        grid.add(new Label("Numero Telephone: "), 3, 1);
        grid.add(new Label("Email: "), 3, 2);
        grid.add(new Label("Categorie: "), 3, 7);
        
        Ville ville = (Ville) dao.getVille().get(user.getVille());
        final Label label = new Label(ville.getNom());
        grid.add(label,1, 4);
        
        final Label label1 = new Label(user.getQuartier());
        grid.add(label1,1, 5);
        
        
        final TextArea textArea = new TextArea(user.getDesc());
        textArea.getStyleClass().add("textArea");
        textArea.setEditable(false);
        textArea.prefWidthProperty().set(150);
        textArea.prefHeightProperty().set(50);
        grid.add(textArea,1, 0);
        
        final Label label6 = new Label(user.getNumTel());
        label6.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(label6,4, 1);
        
        final Label label7 = new Label(user.getEmail());
        label7.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(label7,4, 2);
        
        
        final Label label12 = new Label(user.getCategorie());
        label12.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(label12,4, 7);
        
        
        Label lbMessage = new Label("");
        lbMessage.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        Label lbMessage2 = new Label("");
        
        Button bvalid = new Button();
        bvalid.setText("Oui");
        bvalid.getStyleClass().add(dao.colordanger());
        bvalid.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                   
                 
                   
                   hbox.getChildren().remove(0);
                   
                   Ville ville = (Ville) dao.getVille().get(user.getVille());
                   labelT.setText("Client "+user.getNom()+" "+user.getPrenom()+"("+ville.getNom()+") supprimé");
                   
                   dao.getUser().remove(user.getId());
                   ArrayList users = (ArrayList) dao.getUserVille().get(user.getVille());
                   users.remove(users.indexOf(user.getId()));
                   
                   //gridR.getChildren().remove(treeTableView);
                   dao.treeTableViewUser(dao, paneR, labelT, gridR, "treetableviewU");
                   
                  
                   
                   stage.close();
                                       
                }
        });
        
        Button bannul=new Button();
        bannul.getStyleClass().add(dao.boutonColor);
        bannul.setText("Annuler");
        
        bannul.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
        });
        
       
        
        
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(bannul,bvalid);
        grid.add(hbBtn, 1, 9);

        grid.add(lbMessage,1,9);
        
        VBox VbBtn = new VBox(10);
        VbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        
        HBox Lb = new HBox(10);
        Lb.setAlignment(Pos.BOTTOM_RIGHT);
        Lb.getChildren().addAll(lbMessage,lbMessage2);
        
        VbBtn.getChildren().addAll(lajout,grid,Lb);
        
              
                
                ScrollPane sp = new ScrollPane();
        
                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(VbBtn);
                sp.setPadding(new Insets(10, 10, 10, 10));
                
                Scene scene = new Scene(sp, (dao.width*65)/100, (dao.height*72)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Supprimer un utilisateur");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
