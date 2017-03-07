/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import com.client.login.MainLauncher;
import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import modele.Notification;
import modele.User;
import notifier.Notifier;
import org.controlsfx.control.PopOver;

/**
 *
 * @author ghomsi
 */
public class Index extends Application {
    private Button btnscene1, btnscene2;
    private Button tempButton = null;
    private Label lblscene1, lblscene2;
    private String headHome,headLogin;
    private VBox pane1, pane2,mainContent;
    private Scene scene1, scene2;
    private Stage thestage;
    private ScrollPane sp;
    private Image logo,iconPasswd,iconLogin;
    private ImageView logoView,passwdView,loginView;
    GridPane gridProfil;
    
    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    private final double wh =primScreenBounds.getWidth();//1000;
    private final double ht =primScreenBounds.getHeight();//550;
    private User user = new User();
    private DAOequipement dao;
    Connecter c = null;
    private User touser = null;
  
 
    final ImageView pic = new ImageView();
    final Label name = new Label();
    final Label binName = new Label();
    final Label description = new Label();
    private int currentIndex = -1;
    private ResultSet rs;
    
    ImageView depIconNotif = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/notif1.png"),30,30,false,false)
                );

    public Index() throws ClassNotFoundException, SQLException {
        
        /*Connexion con = null;
        
        con = new Connexion("","","","");*/
        this.dao = new DAOequipement();
        //con.getConnexion().close();
    }
    
    public DAOequipement getDao(){
        return dao;
    }
    public void setDao(DAOequipement fdao){
        this.dao = fdao;
    }
 
    
    @Override
    public void start(Stage primaryStage) {
        headHome="Camtel";
        headLogin="Camtel bienvenue!";
        
        thestage=primaryStage;
        thestage.centerOnScreen();
        //thestage.initStyle(StageStyle.UNDECORATED);
        GridPane grid = new GridPane();
        //grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 40));
       
       logo = new Image(getClass().getResourceAsStream("/images/logo.png"),152,185,false,false);
       logoView = new ImageView(logo);
       
       iconLogin = new Image(getClass().getResourceAsStream("/images/usersmall.png"),25,25,false,false);
       loginView = new ImageView(iconLogin);
       
       
       iconPasswd = new Image(getClass().getResourceAsStream("/images/cadenas.png"),25,25,false,false);
       passwdView = new ImageView(iconPasswd);
       
       
       
       
       
       Text scenetitle = new Text("Welcome");
       scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
       //grid.add(scenetitle, 1, 1, 2, 1);
       
       Label userName = new Label("Login:");
       grid.add(loginView, 0, 1);
       
       final Tooltip tooltipLogin = new Tooltip();
       tooltipLogin.setText("Login");
       TextField userTextField = new TextField();
       userTextField.setTooltip(tooltipLogin);
       userTextField.promptTextProperty().set("Login");
       grid.add(userTextField, 1, 1);
       
       Label pw = new Label("Mot de passe:");
       grid.add(passwdView, 0, 2);
       
       final Tooltip tooltipPasswd = new Tooltip();
       tooltipPasswd.setText("Mot de passe");
       PasswordField pwBox = new PasswordField();
       pwBox.setTooltip(tooltipPasswd);
       pwBox.promptTextProperty().set("Mot De Passe");
       grid.add(pwBox, 1, 2);
       
       

        
        
        Button btn = new Button("Se connecter");
        btn.getStyleClass().add("buttonLogin");
        Button btnParam = new Button(">");
        btnParam.getStyleClass().add("buttonLogin");
        
        
        //btn.setStyle("-fx-button-box-border: transparent;");
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btn,btnParam);
        grid.add(hbBtn, 1, 4);
        
       
        
        pane1=new VBox();
        FlowPane imgpane = new FlowPane();
        imgpane.getChildren().addAll(logoView);
        imgpane.setPadding(new Insets(5, 0, 0,70));
        
        final Text actiontarget = new Text();
        
        VBox main = new VBox(4);
        HBox htarget = new HBox();
        htarget.setPadding(new Insets(10, 10, 10, 40));
        htarget.getChildren().add(actiontarget);
        main.getChildren().addAll(imgpane,grid,htarget);
        pane1.getChildren().addAll(main);
        
        
        Scene connection_scene = new Scene(pane1, 300, 375);
        connection_scene.getStylesheets().add("css/style.css");
        
        //service
        
                   final ScheduledService<Void> connectionService = new ScheduledService<Void>() {
                        @Override
                        protected Task<Void> createTask() {
                            return new Task<Void>(){
                                @Override
                                protected Void call() throws Exception {
                                    int oldtaille = dao.getNotificationRecus().size();
                                    dao.user = dao.getConnection(userTextField.getText(), pwBox.getText());
                                    
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(dao.getNotificationRecus().size()>oldtaille){
                                                int a = dao.getNotificationRecus().size()-oldtaille;
                                                tempButton.setText("notifications("+a+")");
                                                tempButton.setGraphic(depIconNotif );
                                                new Notifier("Gestab","Vous aviez réçu de nouvelles notifications... \n",2);
                                            }

                                        }
                                    });
                                    return null;
                                }
                            };
                        }
                    };
                   connectionService.stateProperty().addListener(new ChangeListener<Worker.State>(){
                       @Override
                       public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                           switch (newValue){
                               case FAILED:
                                        System.out.println("nombre d'echec"+connectionService.getCurrentFailureCount());
                                        //new Notifier("Gestab","Le réseau n'est pas accessible",3,dao);
                                        if(connectionService.getCurrentFailureCount()==7){
                                            thestage.setScene(connection_scene);
                                            thestage.setTitle(headLogin);
                                            try {
                                                dao.deconnection();
                                            } catch (ClassNotFoundException ex) {
                                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                                            } catch (SQLException ex) {
                                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                             Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                                                thestage.setX((primScreenBounds.getWidth() - thestage.getWidth()) / 2);
                                                thestage.setY((primScreenBounds.getHeight() - thestage.getHeight()) / 2);
                                             new Notifier("Gestab Notification","Probleme de connection \n"+dao.user.getNom()+" "+dao.user.getPrenom(),2);
                                        }
                                        break;
                               case CANCELLED:
                               case SUCCEEDED:
                           }
                       }
                       
                   });
                   // delai avant exécution 5minutes car la connection initial  viens d'êtres faite
                   connectionService.setDelay(Duration.seconds(300));
                   //période d'exécution tous les 5 minute(300s)
                   connectionService.setPeriod(Duration.seconds(300));
                   //relancer le service en cas d'échec
                   connectionService.setRestartOnFailure(true);
                   // Au maximum on acceptera 100 échecs
                   connectionService.setMaximumFailureCount(7);
                   
        
        btnParam.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FlowPane modalPaneModifSect = new ModalPaneParamatreConnection(dao,primaryStage,connection_scene).contruire();
                scene2 = new Scene(modalPaneModifSect, 400, 275);
                primaryStage.setTitle("Parametre connection");
                primaryStage.setScene(scene2);
            }
        });
        
        
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               if(userTextField.getText().isEmpty()){
                   actiontarget.setText(userName.getText()+" champ vide");
               }else if(pwBox.getText().isEmpty()){
                   actiontarget.setText(pw.getText()+" champ vide");
               }else{
                   final Cursor oldCursor = connection_scene.getCursor();
                   connection_scene.setCursor(Cursor.WAIT);
                    try {
                        user = dao.getConnection(userTextField.getText(), pwBox.getText());
                        
                        //if(rs.next()){
                        if(user!=null){    
                            dao.user =user;
                            Scene scene = construire(connection_scene,dao);
                            thestage.setScene(scene);
                            thestage.setTitle(headHome);
                            connectionService.reset();
                            connectionService.start();
                            
                            /*Thread myThready = new Thread(new Runnable() {
                                                @Override
                                                    public void run() {
                                                        
                                                        timeWonder.setCycleCount(Timeline.INDEFINITE);
                                                        timeWonder.play();
                                                    }
                                                });
                             myThready.start();   */                
                            //try {
                                //myThready.sleep(300000); //5minutes
                                
                            /*} catch (InterruptedException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                                new Notifier("Gestab",""+ex,3);
                            }*/
                            
                        }else{
                            actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("Utilisateur Inconnu");
                        }

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Probleme de connection");
                        new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                    } catch (SQLException ex) {
                        Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Probleme de connection");
                    }
                    connection_scene.setCursor(oldCursor);
               }
              
            }

            private Scene construire(Scene connection_scene,DAOequipement dao) throws ClassNotFoundException, SQLException {
                /*************_HomeImage********************************************/
                Scene scene = new Scene(new VBox(), dao.width, dao.height);
                scene.getStylesheets().add("css/style.css");
                scene.setFill(Color.OLDLACE);

                name.setFont(new Font("Verdana Bold", 22));
                binName.setFont(new Font("Arial Italic", 10));
                pic.setFitHeight(150);
                pic.setPreserveRatio(true);
                description.setWrapText(true);
                description.setTextAlignment(TextAlignment.JUSTIFY);
               
                

                MenuBar menuBar = new MenuBar();
                final VBox vbox = new VBox();
                vbox.setAlignment(Pos.CENTER);
                vbox.setSpacing(10);
                vbox.setPadding(new Insets(0, 10, 0, 10));
                vbox.getChildren().addAll(name, binName, pic, description);

                // --- Menu File
                Menu menuFile = new Menu("Gestab");
                //MenuItem add = new MenuItem("Shuffle",new ImageView(new Image("menusample/new.png")));
                MenuItem setting = new MenuItem("Reglage");
                setting.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        System.out.println("rafraîchir");
                        //vbox.setVisible(true);
                    }
                });

                MenuItem exit = new MenuItem("Exit");
                exit.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        System.exit(0);
                    }
                });
                MenuItem deconnection = new MenuItem("Deconnection");
                deconnection.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));    
                deconnection.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        thestage.setScene(connection_scene);
                        thestage.setTitle(headLogin);
                        try {
                            dao.deconnection();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        connectionService.cancel();
                         Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                            thestage.setX((primScreenBounds.getWidth() - thestage.getWidth()) / 2);
                            thestage.setY((primScreenBounds.getHeight() - thestage.getHeight()) / 2);
                         new Notifier("Gestab Notification","Good Bye \n"+dao.user.getNom()+" "+dao.user.getPrenom(),2);   
                    }
                });

                MenuItem profil = new MenuItem("Profil");
                profil.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));    
                profil.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        System.out.println("user profil");
                        thestage.setTitle("Profil Utilisateur");
                        pane2.getChildren().setAll(new PaneUserProfil(t,dao).construire());

                        //sp.setContent(pane2);

                    }
                });

                //menuFile.getItems().addAll(setting);
                menuFile.getItems().addAll(profil);
                menuFile.getItems().addAll(deconnection);
                menuFile.getItems().addAll(exit);




                // --- Menu Equipements
                Menu menuAD = new Menu("Administration");
                MenuItem listerEQ = new MenuItem("Equipements");
                listerEQ.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
                listerEQ.setOnAction(new EventHandler<ActionEvent>() {
                        
                        public void handle(ActionEvent t) {
                            System.out.println("Administration equipement");
                            thestage.setTitle("Administration des Equipements");
                            try {
                                
                               
                            pane2.getChildren().setAll(new PaneAdminEquipement(dao).construire());
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                                new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                            }
                            //sp.setContent(pane2);
                        }
                });
                MenuItem listerU = new MenuItem("Utilisateurs");
                listerU.setAccelerator(KeyCombination.keyCombination("Ctrl+U"));
                listerU.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            System.out.println("Administration Utilisateurs");
                            try {
                                pane2.getChildren().setAll(new PaneUser(dao).construire());
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                                new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                            }
                            //sp.setContent(pane2);
                        }
                });
                
                MenuItem listerSSH = new MenuItem("SSH");
                listerSSH.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
                listerSSH.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            System.out.println("Requettes SSH");
                            try {
                                pane2.getChildren().setAll(new PaneSSH(dao).construire());
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                                new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                            }
                            //sp.setContent(pane2);
                        }
                });

                Menu menuHelp = new Menu("Aide");
                MenuItem menuHItem = new MenuItem("Documentation");
                menuHItem.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
                menuHItem.setOnAction(new EventHandler<ActionEvent>() {
                        
                        public void handle(ActionEvent t) {
                            
                            ModalPaneAide modalAide = new ModalPaneAide(dao,t);
                            try {
                                modalAide.construire();
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                                new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                            }
                            
                        }
                });
                
                menuHelp.getItems().add(menuHItem);


                menuAD.getItems().addAll(listerEQ);
                if(dao.isSuperAdmin(dao.user)){
                    menuAD.getItems().addAll(listerU);
                    menuAD.getItems().addAll(listerSSH);
                }
                

                sp = new ScrollPane();
                sp.setPrefSize(wh,AUTO);

                // --- Menu Secteur
                Menu menuSect = new Menu("Gestions");
                MenuItem gestEQ = new MenuItem("Equipements");
                gestEQ.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
                gestEQ.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            //System.out.println("lister secteur");
                            thestage.setTitle("Gestions des Equipements");
                            try {
                                 pane2.getChildren().setAll(new PaneEquipement(dao).construire());
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                                new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                            }
                            //sp.setContent(pane2);
                        }
                });

                MenuItem gestCL = new MenuItem("Clients");
                gestCL.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
                gestCL.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            //System.out.println("lister secteur");
                            thestage.setTitle("Gestions des Clients");
                            try {
                                 pane2.getChildren().setAll(new PaneClients(dao).construire());
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                                new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                            }
                            
                            //sp.setContent(pane2);
                        }
                });
                if(dao.isSuperAdmin(dao.user)||dao.isTechUser(dao.user))
                    menuSect.getItems().addAll(gestEQ);
                if(dao.isSuperAdmin(dao.user)||dao.isUser(dao.user)||dao.isSubUser(dao.user))
                    menuSect.getItems().addAll(gestCL);

                //System.out.println(dao.isUser(dao.user));

                if(dao.isSuperAdmin(dao.user)){
                    menuBar.getMenus().addAll(menuFile, menuAD, menuSect,menuHelp);
                }else if(dao.isAdmin(dao.user)||dao.isTechAdmin(dao.user)){
                    menuBar.getMenus().addAll(menuFile, menuAD, menuHelp);
                }else if(dao.isUser(dao.user)||dao.isSubUser(dao.user)||dao.isTechUser(dao.user)){
                     menuBar.getMenus().addAll(menuFile, menuSect,menuHelp);
                }

                ((VBox) scene.getRoot()).getChildren().addAll(menuBar);
                
                
                
                ImageView depIcon = new ImageView (
                    new Image(getClass().getResourceAsStream("/images/img4.jpg"))
                );
                pane2 = new VBox();
                
                gridProfil = new GridPane();
                gridProfil.setHgap(10);
                gridProfil.setVgap(10);
                gridProfil.setPadding(new Insets(25, 25, 25, 25));
                pane2.getChildren().addAll(depIcon);
                
                
                gridProfil.add(new Label("Connecter en tant que"),0,0);
                gridProfil.add(new Label("nom:"),0,1);
                gridProfil.add(new Label(dao.user.getNom()+" "+dao.user.getPrenom()),1,1);
                gridProfil.add(new Label("Numeros de Telephone:"),0,2);
                gridProfil.add(new Label(dao.user.getNumTel()),1,2);
                mainContent = new VBox();
                
                ImageView depIconEmail = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/email.png"),30,30,false,false)
                );
                Button emailbtn = new Button("Email");
                emailbtn.setGraphic(depIconEmail );
                emailbtn.getStyleClass().add(dao.boutonColor);
                
                emailbtn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    ModalEmail sendMail = new ModalEmail(dao,t);
                            sendMail.contruire();                   
                        }
                    });
                ImageView depIconChat = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/chat.png"),30,30,false,false)
                );
                Button chatbtn = new Button("Chat");
                chatbtn.setGraphic(depIconChat );
                chatbtn.getStyleClass().add(dao.boutonColor);
                
                chatbtn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        MainLauncher chat =new MainLauncher(dao,t);  
                        try {
                            chat.contruire();
                        } catch (IOException ex) {
                            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                
                
                ImageView depIconNotif2 = new ImageView (
                        new Image(getClass().getResourceAsStream("/icon/bell.png"),30,30,false,false)
                );
                
                Button notifbtn = new Button("notifications("+dao.getNotificationRecus().size()+")");
                
                tempButton = notifbtn;
                notifbtn.setGraphic(depIconNotif );
                notifbtn.getStyleClass().add(dao.boutonColor);
                //PopOverController<PopOver, Button> controller = new MyController();
                
                if(dao.getUser()!=null){
                    ImageView depIcon2 = new ImageView (
                                new Image(getClass().getResourceAsStream("/images/usersmall.png"))
                            );
                    Button detailbtn = new Button("mes notifications("+dao.getNotificationRecus().size()+")");
                    detailbtn.setCenterShape(true);
                    detailbtn.setGraphic(depIcon2 );
                            //Adding GridPane

                        GridPane gridPane = new GridPane();
                        gridPane.setPadding(new Insets(20,20,20,20));
                        gridPane.setHgap(5);
                        gridPane.setVgap(5);
                        //Implementing Nodes for GridPane
                        Label lblto = new Label("to");
                        final TextField txtto = new TextField();
                        txtto.setEditable(false);
                        //---- combo Box users

                        Enumeration e =dao.getUser().elements();
                            ObservableList<User> options = FXCollections.observableArrayList();


                            while(e.hasMoreElements()){
                                User user =(User) e.nextElement();
                                options.add(user);
                            }


                            final ComboBox comboBox = new ComboBox(options);
                            comboBox.setPadding(new Insets(10, 10, 10, 10));


                            comboBox.setPromptText("choix recepteur");     
                            comboBox.setCellFactory(new Callback<ListView<User>,ListCell<User>>(){

                                        @Override
                                        public ListCell<User> call(ListView<User> p) {

                                            final ListCell<User> cell = new ListCell<User>(){

                                                @Override
                                                protected void updateItem(User t, boolean bln) {
                                                    super.updateItem(t, bln);

                                                    if(t != null){
                                                        setText(t.getNom()+" "+t.getPrenom());

                                                    }else{
                                                        setText(null);
                                                    }
                                                }

                                            };

                                            return cell;
                                        }
                                    });



                            comboBox.valueProperty().addListener(new ChangeListener<User>() {
                                        @Override public void changed(ObservableValue ov, User t, User t1) {

                                           if(t1!=null){ 
                                             txtto.setText(t1.getNom()+" "+t1.getPrenom());
                                             touser =t1;
                                           } 
                                        }    
                                    });
                            /*new AutoCompletionTextFieldBinding(txtto, new Callback<AutoCompletionBinding.ISuggestionRequest, Collection>() {
                                @Override
                                public Collection call(AutoCompletionBinding.ISuggestionRequest param) {
                                    return Arrays.asList("Option 1", "Option 2",elt);
                                }
                            });*/
                            Label lblobjet = new Label("objet");
                            final TextField txtobjet = new TextField();
                            Button btnEnvoyer = new Button("envoyer");
                            TextArea textA = new TextArea();
                            textA.setPrefSize(300, 210);
                            textA.setWrapText(true);
                            final Label lblMessage = new Label();
                            //lblMessage.setTextFill(Color.web(dao.colordanger()));
                            //Adding Nodes to GridPane layout

                            HBox hb = new HBox(5);
                            hb.getChildren().addAll(lblto);
                            gridPane.add(hb, 0, 0);

                            gridPane.add(txtto, 1, 0);

                            gridPane.add(lblobjet, 0, 1);

                            gridPane.add(txtobjet, 1, 1);

                            gridPane.add(textA, 1, 3);

                            gridPane.add(btnEnvoyer, 1, 4);

                            ScrollPane spnotif = new ScrollPane();
                            spnotif.setMaxHeight((dao.height*36)/100);
                            spnotif.setContent(gridPane);

                            VBox notifHb = new VBox(5);
                            Label mytext =new Label("");
                            ImageView smsIcon = new ImageView (
                                    new Image(getClass().getResourceAsStream("/icon/notif1.png"),50,50,false,false)
                            );
                            ImageView smsIcon2 = new ImageView (
                                    new Image(getClass().getResourceAsStream("/icon/notif2.png"),50,50,false,false)
                            );
                            mytext.setGraphic(smsIcon);

                            notifHb.getChildren().addAll(mytext,comboBox,spnotif,lblMessage,detailbtn);    

                            PopOver popOver = new PopOver(notifHb);
                            popOver.setTitle("Notifications");
                            popOver.setAnimated(true);
                            popOver.setDetachable(true);
                            popOver.setDetached(true);
                            popOver.setCornerRadius(4);

                        btnEnvoyer.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent t) {



                                    if(!txtobjet.getText().isEmpty()&&!txtto.getText().isEmpty()&&!textA.getText().isEmpty()){


                                           Connecter c=null;
                                           try {
                                               c = new Connecter();
                                           } catch (ClassNotFoundException ex) {
                                               Logger.getLogger(ModalAddEQ.class.getName()).log(Level.SEVERE, null, ex);
                                           } catch (SQLException ex) {
                                               Logger.getLogger(ModalAddEQ.class.getName()).log(Level.SEVERE, null, ex);
                                               new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                                           }
                                           Requettes con = new Requettes(c);

                                           Notification notif = new Notification();
                                           //utile pour la différentiation lors de l'affichage dans le treeTableView 
                                           notif.setIdEmet(dao.user.getId());
                                           notif.setIdRecep(touser.getId());
                                           notif.setObjet(txtobjet.getText());
                                           notif.setMessage(textA.getText());
                                           notif.setVue(Boolean.FALSE);
                                           try { 
                                               int id =con.fInsertNotification(notif);

                                               // ajout de l'objet secteur a la liste d'objet
                                               notif.setId(id);
                                               dao.getNotificationEmis().put(notif.getId(), notif);

                                               new Notifier("Gestab","Notification envoyé à :"+touser.getNom()+" "+touser.getPrenom(),2,dao);

                                               popOver.hide();
                                           } catch (ClassNotFoundException ex) {
                                               Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                               lblMessage.setText("Echec d'envoie.");
                                           } catch (SQLException ex) {
                                               Logger.getLogger(ModalAddVille.class.getName()).log(Level.SEVERE, null, ex);
                                               lblMessage.setText("Echec d'envoie.");
                                           } catch (ParseException ex) {
                                            Logger.getLogger(FonctionVue.class.getName()).log(Level.SEVERE, null, ex);
                                            lblMessage.setText("Echec d'envoie.");
                                           }
                                       }else{
                                            lblMessage.setText("Echec: le(s) Champ(s) de texte vide!");
                                       } 


                                }
                            });
                        detailbtn.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent t) {
                                    popOver.detach();
                                    ModalNotificationTable modalNotification = new ModalNotificationTable(t,dao);
                                    modalNotification.construire();

                                }
                            });
                        notifbtn.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent t) {

                                popOver.show(notifbtn);
                                notifbtn.setGraphic(depIconNotif2 );
                                notifbtn.setText("notifications");
                            }


                        });
                }
                
                
                
                HBox userprofilHB = new HBox(10);
                userprofilHB.getChildren().addAll(gridProfil,emailbtn,chatbtn,notifbtn);
                mainContent.getChildren().setAll(pane2,userprofilHB);
                
                sp.setContent(mainContent);
                sp.minHeightProperty().set(ht-30);

                ((VBox) scene.getRoot()).getChildren().addAll(sp);

                return scene;
            }
        });
        dao.hostservice =getHostServices(); 
        System.out.println("service:"+dao.hostservice);
        primaryStage.setTitle(headLogin);
        primaryStage.setScene(connection_scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/gg.png")));
        primaryStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        primaryStage.setOnCloseRequest(event ->{
            System.exit(1);
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    //5 minutes
   /* Timeline timeWonder = new Timeline(new KeyFrame(Duration.seconds(300), new EventHandler<ActionEvent>() {

    @Override
    public void handle(ActionEvent event) {
        System.out.println("Mise à jour objet-base de donnée ");
        try {
            Connecter c = new Connecter();
            Requettes con = new Requettes(c);
            dao.refresh(c,dao.user);
            c.getConnexion().close();
            new Notifier("Gestab","Mise à jour de la base de Donnée éffectuée",2,dao);

        } catch (Exception ex) {
            System.out.println(ex);
            new Notifier("Gestab Mise à jour",""+ex,3,dao);
        }
    }
}));*/
  
    
    
}
