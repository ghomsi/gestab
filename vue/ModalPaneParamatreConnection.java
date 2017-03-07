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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author ghomsi
 */
public class ModalPaneParamatreConnection {
    FlowPane pane;
    Stage stage;
    Scene scene;
    DAOequipement dao;

    ModalPaneParamatreConnection(DAOequipement dao,Stage stage,Scene scene) {
        this.stage = stage;
        this.scene = scene;
        this.dao = dao;
    }

   
    FlowPane contruire(){
        pane = new FlowPane();
        pane.getStylesheets().add("css/style.css");
        
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 25, 25, 10));
        Label actiontarget = new Label("");
        grid.add(actiontarget, 2, 6);
        
        String[] frac = dao.LireFichier("infos_comptes.tcp",actiontarget);       
             Label host = new Label("Host");
             grid.add(host, 1,0 );
             TextField hosttf = new TextField();
             if(frac!=null){
                hosttf.setText(frac[0]);
             }else{
                hosttf.setText("localhost"); 
             }
             grid.add(hosttf, 2, 0);
             
             Label portLB = new Label("Port");
             grid.add(portLB,1 , 1);
             TextField portTF = new TextField();
            if(frac!=null){
                portTF.setText(frac[1]);
            }else{
                portTF.setText("80");
            }
             grid.add(portTF, 2, 1);

             Label IdLB = new Label("Utilisateur ");
             grid.add(IdLB, 1,3);
              TextField IdTF = new TextField();
              if(frac!=null){
                    IdTF.setText(frac[2]);
              }else{
                    IdTF.setText("username");
              }
             grid.add(IdTF, 2, 3);




             Label mdplb = new Label(" Mot de pass ");
             grid.add(mdplb, 1, 4);
             PasswordField mdptf = new PasswordField();
             if(frac!=null){
                mdptf.setText(frac[3]);
             }else{
                mdptf.setText("none"); 
             }
             grid.add(mdptf,2, 4);

             
             
                
                



                Button btn = new Button("Enregistrer");
                btn.getStyleClass().add("buttonLogin");
                Button btn2 = new Button("Annuler");
                btn2.getStyleClass().add("buttonLogin");
                Button btn3 = new Button("<");
                btn3.getStyleClass().add("buttonLogin");
                
                btn2.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        stage.close();
                    }
                });
                
                btn3.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        stage.setScene(scene);
                        stage.setTitle("Connection");
                    }
                });
                
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        if(hosttf.getText().isEmpty()){
                            actiontarget.setText(host.getText()+" champ vide");
                        }else if(portTF.getText().isEmpty()){
                            actiontarget.setText(portLB.getText()+" champ vide");
                        }else if(IdTF.getText().isEmpty()){
                            actiontarget.setText(IdLB.getText()+" champ vide");
                        }else if(mdptf.getText().isEmpty()){
                            actiontarget.setText(mdplb.getText()+" champ vide");
                        }else{
                            
                           dao.ecrireFichier(hosttf.getText(),portTF.getText(),IdTF.getText(),mdptf.getText(),actiontarget,"infos_comptes.tcp"); 
                        }
                    }

            
           
                
                
                
                
                });
                
                
                
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().addAll(btn3,btn2,btn);
                grid.add(hbBtn, 2, 5);



                
                pane.getChildren().addAll(grid);
        
        
        return pane;
    }
    


}