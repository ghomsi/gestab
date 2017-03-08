/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telnet;

import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import modele.Client;
import modele.Equipement;
import modele.Historique;
import modele.Port;
import notifier.Notifier;
import static telnet.TelnetTerminal.log;
import vue.ModalClientNotifier;

/**
 *
 * @author ghomsi
 */
public class ModalTelnetDesactiver {
    
    private ActionEvent event;
    private DAOequipement dao;
    private static Telnet telnet = null;
    private String user;
    private String password;
    private Port port;
    private Client client;
    
    private TelnetTerminal telnetActive = null;
    
    public ModalTelnetDesactiver(ActionEvent event,DAOequipement fdao,String user,String password,Port port,Client client){
        this.event = event;
        this.dao = fdao;
        this.user = user;
        this.password = password;
        this.port = port;
        this.client = client;
        
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
                cssEditorFld.setText("Connection en cours...");
                grid.add(cssEditorFld, 1, 0);
                dao.textArea =cssEditorFld;
               final Text actiontarget = new Text();
                grid.add(actiontarget, 1, 6);
                
                final PrintStream outputTelnet = new PrintStream(new TextAreaOutputStream(cssEditorFld));
                /*if (dao.telnet != null) {
                    dao.telnet.disconnect();
                }*/
                
                
                Button btn = new Button("telnet desactiver");
                btn.getStyleClass().add("buttonLogin");
                btn.setDisable(true);
                dao.btn=btn;
                Button btn2 = new Button("Fermer");
                btn2.getStyleClass().add("buttonLogin");
                
                
                Carte carte = (Carte) dao.getCarte().get(port.getIdCarte());
                Equipement eq = (Equipement) dao.getEquipement().get(carte.getIdEQ());
                dao.client = client;
                telnet = new Telnet( dao,stage,eq.getRoute(),outputTelnet,2,client);
                dao.telnet = telnet;
                Thread myThready = new Thread(new Runnable() {
                @Override
                    public void run() {

                        try {
                            telnet.execute();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if(telnet.isconnected){
                                        cssEditorFld.setText("Connection établie...\n");
                                        btn.setDisable(false);
                                        btn.getStyleClass().add(dao.colornodanger());
                                    }else{
                                        cssEditorFld.setText("Echec de connection...\n");
                                    }

                                }
                            });
                            
                        } catch (Exception ex) {
                            log.error(ex);
                        }
                    }
                });
                myThready.start();
                

                
                
                
                btn.setOnAction(new EventHandler<ActionEvent>(){
                    public void handle(ActionEvent t) {
                        try {
                        //if(!telnet.isconnected){
                            telnet.sendCommand(user);
                           
                                Thread.sleep(20);
                            
                            telnet.sendCommand(password);
                            
                                Thread.sleep(20);
                            
                           // telnet.isconnected=true;
                        ///}
                        
                        telnet.sendCommand("enable");
                        
                            Thread.sleep(20);
                        
                        telnet.sendCommand("config");
                        
                            Thread.sleep(20);
                        
                        
                        telnet.sendCommand("interface adsl 0/"+carte.getNumCarte());
                        
                            Thread.sleep(20);
                       
                        telnet.sendCommand("deactivate "+port.getNumeroPort());
                        
                            Thread.sleep(20);
                        
                        //telnet.sendCommand("quit");
                       
                            Thread.sleep(20);
                        
                        //telnet.sendCommand("quit");
                        
                            Thread.sleep(20);
                            
                            /*Connecter c=null;
                            try {
                                c = new Connecter();
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                Requettes con = new Requettes(c);
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = new Date();
                                //System.out.println("today:"+dateFormat.format(date));

                                Historique hist = new Historique(dateFormat.format(date),"RAS",client.getIntStatut());
                                int id=0;
                            try {
                                con.fUpdateClient(client);
                                id = con.fInsertHistorique(client.getId(),hist );
                                c.getConnexion().close();

                                cssEditorFld.appendText("port desactivé!\n");
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ParseException ex) {
                                Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            hist.setId(id);
                            Hashtable tables = dao.getClientHistorique();
                            if(tables.get(client.getId())!=null){
                                ArrayList fhistoriques = (ArrayList) tables.get(client.getId());
                                fhistoriques.add(hist);
                            }else{
                                ArrayList fhistoriques = new ArrayList();
                                fhistoriques.add(hist);
                                tables.put(client.getId(), fhistoriques);
                            }

                            new Notifier("Gestab Notification","port desactivé",2,dao);
                            ModalClientNotifier modalClientNotifier = new ModalClientNotifier(dao,t,"Mr/Mme:"+client.getNom()+" "+client.getPrenom()+" </br>votre ligne a été activée",client);
                            modalClientNotifier.construire();*/

                        } catch (InterruptedException ex) {
                            Logger.getLogger(ModalTelnet.class.getName()).log(Level.SEVERE, null, ex);
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
