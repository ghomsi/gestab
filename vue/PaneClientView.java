/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import jssh.Activer;
import jssh.Desactiver;
import modele.Client;
import modele.Equipement;
import modele.Port;
import notifier.Notifier;
import telnet.ModalTelnetActiver;
import telnet.ModalTelnetDesactiver;

/**
 *
 * @author ghomsi
 */
public class PaneClientView {
    
    private VBox pane,paneR;
    private Image images;
    private ImageView pics;
    private final TitledPane tps;
    private final Client client;
    private final Label labelT;
    private final GridPane gridR;
    private DAOequipement dao;
    private HBox hbBtn = null;
    
   
    final Label label4 = new Label("N/A");
    
    
    

    public PaneClientView(DAOequipement dao,Client client,Label label,GridPane grid,VBox pane) {
        this.tps = new TitledPane();
        this.pics = new ImageView();
        this.images = new Image(getClass().getResourceAsStream("/images/user.png"));
        this.client = client;
        this.labelT = label;
        this.gridR = grid;
        this.paneR = pane;
        this.dao = dao;
        
    }
    
    public VBox construire(){
        pane=new VBox();
        //pane.minHeightProperty().set(dao.getHeigth());
        
        
        // --- GridPane container
        GridPane grid = new GridPane();
        grid.getStyleClass().add("gridCarte");
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        dao.gridClientVue(dao, grid, client);
        
        
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(20, 0, 0, 20));
        
        Button btn = new Button("Supprimer");
        btn.getStyleClass().add(dao.colordanger());
        Button btn2 = new Button("Modifier");
        btn2.getStyleClass().add(dao.colorchange());
        
        ImageView depIconHist = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/hist.png"),30,30,false,false)
        );
        Button btn3 = new Button("Historique");
        btn3.setGraphic(depIconHist );
        btn3.getStyleClass().add(dao.boutonColor);
        
        ImageView depIconEmail = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/email.png"),30,30,false,false)
        );
        Button emailbtn = new Button("Email");
        emailbtn.setGraphic(depIconEmail );
        emailbtn.getStyleClass().add(dao.boutonColor);
        
        ImageView depIconSms = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/sms.png"),30,30,false,false)
        );
        Button smsbtn = new Button("SMS");
        smsbtn.setGraphic(depIconSms );
        smsbtn.getStyleClass().add(dao.boutonColor);
        
        ImageView depIconPhone = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/phone.png"),30,30,false,false)
        );
        Button callbtn = new Button("Appeler");
        callbtn.setGraphic(depIconPhone );
        callbtn.getStyleClass().add(dao.boutonColor);
        
        Button btnSusp = new Button("Suspendre");
        btnSusp.getStyleClass().add(dao.colorsmalldanger());
        Button btnResi = new Button("Résilier");
        btnResi.getStyleClass().add(dao.colordanger());
        Button btnActiver = new Button("Activer");
        btnActiver.getStyleClass().add(dao.colornodanger());
        ImageView IconAsusp = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/s03.png"),30,30,false,false)
        );
        Button btnAsusp = new Button("A suspendre");
        btnAsusp.setGraphic(IconAsusp );
        btnAsusp.getStyleClass().add(dao.colorsmalldanger());
        ImageView IconValider = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/s01.png"),30,30,false,false)
        );
        Button btnValider = new Button("Valider la Suspension");
        btnValider.setGraphic(IconValider );
        btnValider.getStyleClass().add(dao.colorsmalldanger());
        ImageView IconAactiver = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/s02.png"),30,30,false,false)
        );
        Button btnAactiv = new Button("A activer");
        btnAactiv.setGraphic(IconAactiver );
        btnAactiv.getStyleClass().add(dao.colornodanger());
        ImageView IconNonValider = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/s02.png"),30,30,false,false)
        );
        Button btnNonValider = new Button("annuler la Suspension");
        btnNonValider.setGraphic(IconNonValider );
        btnNonValider.getStyleClass().add(dao.colornodanger());
        
        Equipement eq = (Equipement)dao.getEquipement().get(client.getEq());
        
        /*****btn action(activation/suspe,sion/résiliation) client ***/
        Port port = (Port)dao.getPort().get(client.getPort());
        
        if(eq==null || port==null){
           btnSusp.setDisable(true);
           btnResi.setDisable(true);
           btnActiver.setDisable(true);
           btnAsusp.setDisable(true);
           btnValider.setDisable(true);
           btnAactiv.setDisable(true);
           btnNonValider.setDisable(true);
        }
        
        emailbtn.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent t) {
            ModalEmail sendMail = new ModalEmail(dao,t,client.getEmail());
                    sendMail.contruire();                   
                }
            });
        
        btnAsusp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                client.setAsuspendre(true);
                Connecter c=null;
                    try {
                        c = new Connecter();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        client.setValider(false);
                        new Notifier("Gestab Error","probleme de connection:"+ex,3,dao);
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Requettes con = new Requettes(c);
                    try {
                        con.fUpdateClient(client);
                        c.getConnexion().close();
                        dao.getClient().replace(client.getId(), client);
                        dao.labelclientstatut.setText(dao.labelclientstatut.getText()+"/à suspendre");
                        
                        hbBtn.getChildren().remove(btnAsusp);
                        hbBtn.getChildren().add(btnAactiv);
                        new Notifier("Gestab Notification",client.getNumTel()+" à suspendre",2,dao);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        client.setValider(false);
                        new Notifier("Gestab Error","probleme de requette:"+ex,3,dao);
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    }                   
            }
        });
        btnAactiv.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                client.setAsuspendre(false);
                Connecter c=null;
                    try {
                        c = new Connecter();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        client.setValider(true);
                        new Notifier("Gestab Error","probleme de connection:"+ex,3,dao);
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Requettes con = new Requettes(c);
                    try {
                        con.fUpdateClient(client);
                        c.getConnexion().close();
                        dao.getClient().replace(client.getId(), client);
                        dao.labelclientstatut.setText(dao.labelclientstatut.getText().replace("/à suspendre", ""));
                        
                        hbBtn.getChildren().remove(btnAactiv);
                        hbBtn.getChildren().add(btnAsusp);
                        new Notifier("Gestab Notification",client.getNumTel()+" à ne pas suspendre",2,dao);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        client.setValider(true);
                        new Notifier("Gestab Error","probleme de requette:"+ex,3,dao);
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    }                  
            }
        });
        btnValider.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                client.setValider(true);
                Connecter c=null;
                    try {
                        c = new Connecter();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        client.setValider(false);
                        new Notifier("Gestab Error","probleme de connection:"+ex,3,dao);
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Requettes con = new Requettes(c);
                    try {
                        con.fUpdateClient(client);
                        c.getConnexion().close();
                        dao.getClient().replace(client.getId(), client);
                        dao.labelclientstatut.setText(dao.labelclientstatut.getText()+"/suspension valider");
                        
                        hbBtn.getChildren().remove(btnValider);
                        hbBtn.getChildren().add(btnNonValider);
                        new Notifier("Gestab Notification",client.getNumTel()+" suspenssion validée",2,dao);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        client.setValider(false);
                        new Notifier("Gestab Error","probleme de requette:"+ex,3,dao);
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    }                   
            }
        });
        btnNonValider.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                client.setValider(false);
                Connecter c=null;
                    try {
                        c = new Connecter();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        client.setValider(true);
                        new Notifier("Gestab Error","probleme de connection:"+ex,3,dao);
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Requettes con = new Requettes(c);
                    try {
                        con.fUpdateClient(client);
                        c.getConnexion().close();
                        dao.getClient().replace(client.getId(), client);
                        dao.labelclientstatut.setText(dao.labelclientstatut.getText().replace("/suspension valider", ""));
                        
                        hbBtn.getChildren().remove(btnNonValider);
                        hbBtn.getChildren().add(btnValider);
                        new Notifier("Gestab Notification",client.getNumTel()+" suspenssion non validée",2,dao);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        client.setValider(true);
                        new Notifier("Gestab Error","probleme de requette:"+ex,3,dao);
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
                    }                   
            }
        });
        
        
        
        
        
        
        
        btnSusp.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    Dialog<Pair<String, String>> dialog = new Dialog<>();
                    dialog.setTitle("Login Dialog");
                    Equipement e = (Equipement) dao.getEquipement().get(client.getEq());
                    dialog.setHeaderText(e.getNom()+", paramètre connection");
                    // Set the icon (must be included in the project).
                    dialog.setGraphic(new ImageView(this.getClass().getResource("/icon/DSLAM-icon.png").toString()));
                    // Set the button types.
                    ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
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
                        client.setStatus("SUSPENDU");
                        if(eq.isSSH()){
                            try {
                                Desactiver modalClient = new Desactiver(t,dao,port,client,usernamePassword.getKey(),usernamePassword.getValue());
                                
                            } catch (Exception ex) {
                                labelT.setText("Le réseau n'est pas accessible");
                                Logger.getLogger(PaneClientView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            ModalTelnetDesactiver modal = new ModalTelnetDesactiver(t,dao,usernamePassword.getKey(),usernamePassword.getValue(),port,client);
                            modal.construire();
                        }
                        
                    });
                        
                }
            });
        btnResi.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                  
                    Dialog<Pair<String, String>> dialog = new Dialog<>();
                    dialog.setTitle("Login Dialog");
                    Equipement e = (Equipement) dao.getEquipement().get(client.getEq());
                    dialog.setHeaderText(e.getNom()+", paramètre connection");
                    // Set the icon (must be included in the project).
                    dialog.setGraphic(new ImageView(this.getClass().getResource("/icon/DSLAM-icon.png").toString()));
                    // Set the button types.
                    ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
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
                        client.setStatus("RESILIE");
                        
                        if(eq.isSSH()){ 
                            try {
                                Desactiver modalClient = new Desactiver(t,dao,port,client,usernamePassword.getKey(),usernamePassword.getValue());
                                
                            } catch (Exception ex) {
                                labelT.setText("Le réseau n'est pas accessible");
                                Logger.getLogger(PaneClientView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{

                            ModalTelnetDesactiver modal = new ModalTelnetDesactiver(t,dao,usernamePassword.getKey(),usernamePassword.getValue(),port,client);//adrien-adrien2016
                            modal.construire();
                        }
                    });
                    
                    

                    
                   
                }
            });
        btnActiver.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    Dialog<Pair<String, String>> dialog = new Dialog<>();
                    dialog.setTitle("Login Dialog");
                    Equipement e = (Equipement) dao.getEquipement().get(client.getEq());
                    dialog.setHeaderText(e.getNom()+", paramètre connection");
                    // Set the icon (must be included in the project).
                    dialog.setGraphic(new ImageView(this.getClass().getResource("/icon/DSLAM-icon.png").toString()));
                    // Set the button types.
                    ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
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
                        client.setStatus("ACTIF");
                        if(eq.isSSH()){

                            try {
                                Activer modalClient = new Activer(t,dao,usernamePassword.getKey(),usernamePassword.getValue(),labelT,port,client);
                                
                            } catch (Exception ex) {
                                labelT.setText("Le réseau n'est pas accessible");
                                new Notifier("Gestab Exception","SSH: Le réseau n'est pas accessible",2,dao);
                                Logger.getLogger(PaneClientView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            ModalTelnetActiver modal = new ModalTelnetActiver(t,dao,usernamePassword.getKey(),usernamePassword.getValue(),port,client);
                            modal.construire();
                        }
                    });
                    
                    
                    
                }
            });
        
        /********/

            btn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    
                    ModalPaneDeleteClient modalClient = new ModalPaneDeleteClient(dao,t,client,hbox,labelT,gridR,paneR);
                    modalClient.construire();
                }
            });

            btn2.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    ModalPaneModifierClient modalClient = new ModalPaneModifierClient(dao,t,grid,client);
                    modalClient.construire();
                }
            });
            
            btn3.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent t){
                    
                    ModalHistorique modalHistorique = new ModalHistorique(t,dao,client);
                    modalHistorique.construire();
                }
            });


        images = new Image(getClass().getResourceAsStream("/images/user.png"),200,200,false,false);
        pics = new ImageView(images);
        
        hbBtn = new HBox(4);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        Button temp1 = null;
        Button temp2 = null;
        if(client.isAsuspendre())
            temp1=btnAactiv;
        else
            temp1=btnAsusp;

        if(client.isValider())
            temp2=btnNonValider;
        else
            temp2=btnValider;
        if(dao.isSuperAdmin(dao.user)){ 
          hbBtn.getChildren().addAll(btn,btn2,btn3,emailbtn,smsbtn,callbtn,new Label("ᚙ"),btnResi,btnSusp,btnActiver,dao.checKandSynchro(dao,port,1,client),temp1,temp2);
        }else if(dao.isTechUser(dao.user)){
            hbBtn.getChildren().addAll(btn2,btn3,emailbtn,smsbtn,callbtn,new Label("ᚙ"),btnResi,btnSusp,btnActiver,dao.checKandSynchro(dao,port,1,client));
        }else if(dao.isAdmin(dao.user)){
            hbBtn.getChildren().addAll(emailbtn,dao.checKandSynchro(dao,port,1,client));
        }else{
            if(dao.isUser(dao.user))
                hbBtn.getChildren().addAll(btn3,emailbtn,smsbtn,callbtn,temp1,temp2);
            else
                hbBtn.getChildren().addAll(btn3,emailbtn,smsbtn,callbtn,temp1);
        }
        
        grid.add(hbBtn, 0, 9,9,1);
        grid.add(pics,3,0);
        
        
        
        
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize((dao.width*60)/100,AUTO);
        sp.setContent(grid);
        hbox.getChildren().setAll(sp);
        
        
        
         
        
        pane.getChildren().addAll(hbox);
        
        
        return pane;
    }
}
