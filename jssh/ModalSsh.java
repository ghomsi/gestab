/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jssh;

import controleur.DAOequipement;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Carte;
import modele.Equipement;
import modele.Port;
import notifier.Notifier;
import telnet.ModalTelnet;
import telnet.Telnet;
import telnet.TelnetTerminal;
import telnet.TextAreaOutputStream;

/**
 *
 * @author ghomsi
 */
public class ModalSsh {
    
    private ActionEvent event;
    private DAOequipement dao;
    private static Check sshCheck = null;
    private String user;
    private String password;
    private Port port;
    
    private TelnetTerminal telnetActive = null;
    
    public ModalSsh(ActionEvent event,DAOequipement fdao,String user,String password,Port port){
        this.event = event;
        this.dao = fdao;
        this.user = user;
        this.password = password;
        this.port = port;
        
    }
    
    public void construire(){
    
        Stage stage = new Stage();
                
                
                
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));
                
                
                TextArea cssEditorFld = new TextArea();
                cssEditorFld.setPrefRowCount(10);
                cssEditorFld.setPrefColumnCount(200);
                cssEditorFld.setWrapText(true);
                cssEditorFld.setPrefWidth(350);
                grid.add(cssEditorFld, 1, 0);
                dao.textArea =cssEditorFld;
               final Label actiontarget = new Label();
                grid.add(actiontarget, 1, 6);
                
                Carte carte = (Carte) dao.getCarte().get(port.getIdCarte());
                Equipement eq = (Equipement) dao.getEquipement().get(carte.getIdEQ());
                sshCheck = new Check( dao,actiontarget,port );
                Thread myThready = new Thread(new Runnable() {
                @Override
                    public void run() {
                        
                    try {
                        sshCheck.execute();
                    } catch (IOException ex) {
                        Logger.getLogger(ModalSsh.class.getName()).log(Level.SEVERE, null, ex);
                        new Notifier("Gestab Exception","SSH:"+ex,3);
                    } catch (Exception ex) {
                        Logger.getLogger(ModalSsh.class.getName()).log(Level.SEVERE, null, ex);
                        new Notifier("Gestab Exception","SSH:"+ex,3);
                    }
                    }
                });
                myThready.start();
                

                /*Button btn = new Button("connecter");
                btn.getStyleClass().add("buttonLogin");*/
                Button btn2 = new Button("Fermer");
                btn2.getStyleClass().add("buttonLogin");
                
                
                /*btn.setOnAction(new EventHandler<ActionEvent>(){
                    public void handle(ActionEvent t) {
                        
                    } 
                });*/
                
                
                btn2.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        stage.close();
                    }
                });
                
                
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().addAll(btn2);
                grid.add(hbBtn, 1, 4);



                ScrollPane sp = new ScrollPane();
        
                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(grid);
                sp.setPadding(new Insets(10, 10, 10, 10));
                
                Scene scene = new Scene(sp, (dao.width*35)/100, (dao.height*50)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("SSH");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
