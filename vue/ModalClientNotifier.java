/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import controleur.Email;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Client;
import sms.SMSClient;

/**
 *
 * @author ghomsi
 */
public class ModalClientNotifier {
    private final ActionEvent event;
    Scene scene;
    private DAOequipement dao;
    private Client client;
    private String message;
    public ModalClientNotifier(DAOequipement dao,String message,Client client,ActionEvent... event){
        if(event.length>0){
            this.event = event[0];
        }else{
            this.event = null;
        }    
        this.dao = dao;
        this.client = client;
        this.message = message;
    }
    
    public void construire(){
    
        Stage stage = new Stage();
        GridPane grid = new GridPane ();
        grid.setAlignment(Pos.CENTER);

        grid.setPadding(new Insets (10,10,10,10));
        grid.setVgap(7);
        grid.setHgap(7);
        
        Label infoclt = new Label ("Envoyer:");
        infoclt.setTextFill(Color.web(dao.couleur));
        infoclt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(infoclt, 0, 0);
        
        Label actiontarget = new Label ("");
        grid.add(actiontarget,1,0);
        
        //A checkbox without a caption
        CheckBox cbEmail = new CheckBox("Email");
        if(client.getEmail()!=null && !client.getEmail().isEmpty()){
            grid.add(cbEmail,1,1);
        }
        //A checkbox with a string caption
        CheckBox cbSms = new CheckBox("SMS");
        grid.add(cbSms,1,2);
        
        

        
        
        
        
        
        //BUTTON
        Button resetbt = new Button ("Non");
        resetbt.getStyleClass().add(dao.boutonColor);
        
        
        resetbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
            });

        Button validerbt = new Button ("Envoyer");
        validerbt.setDisable(true);
        validerbt.getStyleClass().add(dao.boutonColor);
        
        //---Button rechercher Action
        
        
        validerbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                   System.out.println(cbSms.selectedProperty().getValue());
                   System.out.println(cbEmail.selectedProperty().getValue());
                   if(cbSms.selectedProperty().getValue()){
                       new SMSClient(1,dao).sendMessage("696688025", "Hello");
                   }
                   if(cbEmail.selectedProperty().getValue()){
                       String[] frac = dao.LireFichier("infos_comptes.smtp",actiontarget);
                       new Email(frac[0],frac[1], client.getEmail(), "Camtel:notification", message,dao);
                   }
                   
                }
                    
                
            });
        cbSms.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    if(new_val){
                        validerbt.setDisable(false);
                        actiontarget.setText(actiontarget.getText()+"/SMS-");
                    }else{
                        actiontarget.setText(actiontarget.getText().replace("/SMS-", ""));
                    }
            }
        });
        
        cbEmail.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    if(new_val){
                        validerbt.setDisable(false);
                        actiontarget.setText(actiontarget.getText()+"/EMAIL-");
                    }else{
                        actiontarget.setText(actiontarget.getText().replace("/EMAIL-", ""));
                    }
            }
        });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(resetbt,validerbt);
        grid.add(hbBtn, 1, 4);
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(grid);
        sp.setPadding(new Insets(10, 10, 10, 10));
                
        scene = new Scene(sp, (dao.width*30)/100, (dao.height*20)/100);
        scene.getStylesheets().add("css/style.css");        
               
                
                
        stage.setScene(scene);
        stage.setTitle("Notifier un Client");
        stage.initModality(Modality.APPLICATION_MODAL);
        /*stage.initOwner(
            ((Node)event.getSource()).getScene().getWindow() );*/
        stage.show();
    }
}
