/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telnet;

import controleur.DAOequipement;
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
import static telnet.TelnetTerminal.log;

/**
 *
 * @author ghomsi
 */
public class ModalTelnet {
    private ActionEvent event;
    private DAOequipement dao;
    private static Telnet telnet = null;
    private String user;
    private String password;
    private Port port;
    
    private TelnetTerminal telnetActive = null;
    
    public ModalTelnet(ActionEvent event,DAOequipement fdao,String user,String password,Port port){
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
                cssEditorFld.setText("Connection en cour...");
                grid.add(cssEditorFld, 1, 0);
                dao.textArea =cssEditorFld;
                final Text actiontarget = new Text();
                grid.add(actiontarget, 1, 6);
                
                final PrintStream outputTelnet = new PrintStream(new TextAreaOutputStream(cssEditorFld));
                /*if (dao.telnet != null) {
                    dao.telnet.disconnect();
                }*/
                
                Button btn = new Button("statut");
                btn.setDisable(true);
                dao.btn = btn;
                btn.getStyleClass().add("buttonLogin");
                Button btn2 = new Button("Fermer");
                btn2.getStyleClass().add("buttonLogin");
                
                
                Carte carte = (Carte) dao.getCarte().get(port.getIdCarte());
                Equipement eq = (Equipement) dao.getEquipement().get(carte.getIdEQ());
                telnet = new Telnet( dao,stage,eq.getRoute(),outputTelnet ,1);
                dao.telnet = telnet;
                Thread myThready = new Thread(new Runnable() {
                @Override
                    public void run() {

                        try {
                            telnet.execute();
                        } catch (Exception ex) {
                            log.error(ex);
                        }
                    }
                });
                myThready.start();
                

                
                
                
                btn.setOnAction(new EventHandler<ActionEvent>(){
                    public void handle(ActionEvent t) {
                        telnet.sendCommand("");
                        try {
                                Thread.sleep(20);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //System.out.println("ligne:"+telnet.line);
                        if(telnet.line.contains("")){
                            telnet.sendCommand(user);
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            telnet.sendCommand(password);
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                          //  telnet.isconnected=true;
                        
                            telnet.sendCommand("enable");
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            telnet.sendCommand("config");
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            telnet.sendCommand("interface adsl 0/"+carte.getNumCarte());
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            telnet.sendCommand("display port state "+port.getNumeroPort());
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //telnet.sendCommand("quit");
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //telnet.sendCommand("quit");
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            /*telnet.sendCommand("quit");

                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            telnet.sendCommand("y");
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
                            }*/

                            //telnet.disconnect();
                            //cssEditorFld.setText("test");
                        }
                    } 
                });
                
                
                btn2.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        stage.close();
                    }
                });
                
                
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().addAll(btn2,btn);
                grid.add(hbBtn, 1, 4);



                ScrollPane sp = new ScrollPane();
        
                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(grid);
                sp.setPadding(new Insets(10, 10, 10, 10));
                
                Scene scene = new Scene(sp, (dao.width*35)/100, (dao.height*50)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Telnet");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
    
    
    
}
