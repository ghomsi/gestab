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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author ghomsi
 */
public class ModalSMTPParam {
     
    DAOequipement dao;

    ModalSMTPParam(DAOequipement dao) {
        this.dao = dao;
    }

   
    GridPane contruire(){
        
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 25, 25, 10));
        
        Label actiontarget = new Label("");
        grid.add(actiontarget, 2, 6); 
             
        String[] frac = dao.LireFichier("infos_comptes.smtp",actiontarget);       
             Label host = new Label("Email hôte:");
             grid.add(host, 1,0 );
             TextField hosttf = new TextField();
             hosttf.setText(frac[0]);
             grid.add(hosttf, 2, 0);
             
             Label pwdLB = new Label("Mot de passe");
             grid.add(pwdLB,1 , 1);
             TextField portTF = new TextField();
             portTF.setText(frac[1]);
             grid.add(portTF, 2, 1);


             
             
               
                



                Button btn = new Button("Enregistrer");
                btn.setDisable(true);
                btn.getStyleClass().add("buttonLogin");
                Button btn2 = new Button("Modifier");
                btn2.getStyleClass().add("buttonLogin");
                
                btn2.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        btn.setDisable(false);
                    }
                });
                
                
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        if(hosttf.getText().isEmpty()){
                            actiontarget.setText(host.getText()+" champ vide");
                        }else if(portTF.getText().isEmpty()){
                            actiontarget.setText(pwdLB.getText()+" champ vide");
                        }else{
                            
                           dao.ecrireFichier(hosttf.getText(),portTF.getText(),actiontarget,"infos_comptes.smtp"); 
                        }
                    }

            
           
                
                
                
                
                });
                
                
                
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().addAll(btn2,btn);
                grid.add(hbBtn, 2, 5);



                
        
        
        return grid;
    }
}
