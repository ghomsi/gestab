/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mise_a_jour.Updater;
import modele.User;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class PaneUserProfil {
    
    VBox pane,paneDesc;
    Label lblscene,lblscene2;
    private final ActionEvent event;
    Scene scene,scene2;
    Image images;
    ImageView pics;
    private User user;
    private DAOequipement dao;
    
    public PaneUserProfil(ActionEvent event,DAOequipement dao){
        this.event = event;
        this.images = new Image(getClass().getResourceAsStream("/images/user.png"));
        this.user = dao.user;
        this.dao=dao;
    }
    
    
    
    VBox construire(){
        pane=new VBox();
        //pane.minHeightProperty().set(dao.getHeigth());
        
        
        
        
        
        lblscene=new Label("Profil");
        lblscene2=new Label("");
        
        images = new Image(getClass().getResourceAsStream("/images/user.png"),100,100,false,false);
        pics = new ImageView(images);
        
        
        GridPane gridpan = new GridPane();
        GridPane gridpan2 = new GridPane(); 
        gridpan.setAlignment(Pos.CENTER);
                   
        gridpan.setPadding(new Insets (15));
        gridpan.setVgap(5);
        gridpan.setHgap(10);
        
        gridpan2.setVgap(5);
        
        Label actionAnchor1 = new Label ("");
        Label actionAnchor2 = new Label ("");
        gridpan.add(actionAnchor1,3,1);
        gridpan.add(actionAnchor2,5,4);
                     
        Label userL = new Label ("Utilisateur");
        userL.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        
        Label pwdL = new Label ("Mot de Passe");
        pwdL.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        
        gridpan.add(userL, 3,2 );
        gridpan.add(pwdL, 5,1 );
          
        
        Label nomLB = new Label ("Nom:");
        gridpan.add(nomLB, 3,3 );
        TextField nomTF = new TextField ();
        nomTF.setText(user.getNom());
        nomTF.setDisable(true);
        gridpan.add(nomTF, 4, 3);
            
        Label prenomLB = new Label ("Prenom:");
        gridpan.add(prenomLB, 3, 4);
        TextField prenomTF = new TextField ();
        prenomTF.setText(user.getPrenom());
        prenomTF.setDisable(true);
        gridpan.add(prenomTF, 4, 4);
            
        Label nutellLB = new Label("Numero Télephone:");
        gridpan.add(nutellLB, 3, 5);
        TextField nutellTF = new TextField ();
        nutellTF.setText(user.getNumTel());
        nutellTF.setDisable(true);
        gridpan.add(nutellTF, 4,5);
            
        Label villeLB = new Label ("Ville:");
        gridpan.add(villeLB, 3, 7);
        TextField villeTF = new TextField ();
        Ville ville = (Ville) dao.getVille().get(user.getVille());
        if(ville!=null){
            villeTF.setText(ville.getNom());
        }
        villeTF.setDisable(true);
        gridpan.add(villeTF, 4, 7);
             
        Label quatierLB = new Label ("Quatier:");
        gridpan.add(quatierLB, 3, 8);
        TextField quatierTF = new TextField ();
        quatierTF.setText(user.getQuartier());
        quatierTF.setDisable(true);
        gridpan.add(quatierTF, 4, 8);
             
             
        Button modifierbtn = new Button (" Modifier ");
        modifierbtn.getStyleClass().add("buttonAdminU");
        Button savebtn = new Button (" Sauvegarder ");
        savebtn.getStyleClass().add("buttonAdminU");
        savebtn.setDisable(true);
        
        modifierbtn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        nomTF.setDisable(false);
                        prenomTF.setDisable(false);
                        nutellTF.setDisable(false);
                        //villeTF.setDisable(false);
                        quatierTF.setDisable(false);
                        modifierbtn.setDisable(true);
                        savebtn.setDisable(false);
                    }
        });
        
        savebtn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        if(nomTF.getText().isEmpty()){
                            actionAnchor1.setText("Champ "+nomLB.getText()+" Vide");
                        }else if(prenomTF.getText().isEmpty()){
                            actionAnchor1.setText("Champ "+prenomLB.getText()+" Vide");
                        }else if(nutellTF.getText().isEmpty()){
                            actionAnchor1.setText("Champ "+nutellLB.getText()+" Vide");
                        }else if(villeTF.getText().isEmpty()){
                            actionAnchor1.setText("Champ "+villeLB.getText()+" Vide");
                        }else if(quatierTF.getText().isEmpty()){
                            actionAnchor1.setText("Champ "+quatierLB.getText()+" Vide");
                        }else{
                            user.setNom(nomTF.getText());
                            user.setPrenom(prenomTF.getText());
                            user.setNumTel(nutellTF.getText());
                            user.setQuartier(quatierTF.getText());
                            Connecter c=null;
                               try {
                                   c = new Connecter();
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(PaneUserProfil.class.getName()).log(Level.SEVERE, null, ex);
                                   actionAnchor1.setText("Probleme connection");
                               } catch (SQLException ex) {
                                   Logger.getLogger(PaneUserProfil.class.getName()).log(Level.SEVERE, null, ex);
                                   actionAnchor1.setText("Probleme connection");
                               }
                               Requettes con = new Requettes(c);
                               
                               try { 
                                   con.fUpdateUser2(user);
                                   c.getConnexion().close();
                                   
                                   
                                   actionAnchor1.setText("Vos donnés ont été mise à jour." );
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(PaneUserProfil.class.getName()).log(Level.SEVERE, null, ex);
                                   actionAnchor1.setText("Echec d'insertion.");
                               } catch (SQLException ex) {
                                   Logger.getLogger(PaneUserProfil.class.getName()).log(Level.SEVERE, null, ex);
                                   actionAnchor1.setText("Echec d'insertion.");
                               }
                               
                            
                        }
                    }
        });
       
        
        
        
        Button pwdbtn1 = new Button (" Modifier ");
        pwdbtn1.getStyleClass().add("buttonAdminU");
       
        
        Button pwdbtn2 = new Button (" Sauvegarder ");
        pwdbtn2.setDisable(true);
        pwdbtn2.getStyleClass().add("buttonAdminU");
        
        //---- mot de passe
        
        Label old_pwd = new Label ("Ancien Mot de passe:");
        gridpan2.add(old_pwd, 3,3 );
        PasswordField old = new PasswordField();
        old.promptTextProperty().set("Ancien Mot de passe");
        old.setDisable(true);
        gridpan2.add(old, 4, 3);
            
        Label new_pwd = new Label ("Nouveau Mot de passe:");
        gridpan2.add(new_pwd, 3, 4);
        PasswordField newp = new PasswordField();
        newp.setDisable(true);
        newp.promptTextProperty().set("Nouveau Mot de passe");
        gridpan2.add(newp, 4, 4);
        
        
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 25, 25, 10));
        Label actiontarget = new Label("");
        grid.add(actiontarget, 2, 6);
             
             Label sshL = new Label("Mettre a jour Gestab"); 
             sshL.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));   
             grid.add(sshL,1,0);
             


                Button btn = new Button("Ajourner");
                btn.getStyleClass().add("buttonLogin");
                
                TextField urlxml = new TextField();
                urlxml.setText("chemin vers le fichier xml de mise à jour");
                
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        Updater update = new Updater(dao,urlxml.getText());
                        update.update();
                    }
                });
                
                
                
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_LEFT);
                hbBtn.getChildren().addAll(urlxml,btn);
                grid.add(hbBtn, 1, 1);
        
        
                
        
        
        pwdbtn1.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        old.setDisable(false);
                        newp.setDisable(false);
                        pwdbtn1.setDisable(true);
                        pwdbtn2.setDisable(false);
                    }
        });
        
        pwdbtn2.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        if(dao.textCompare(old.getText(), user.getPasswd())){
                            if(newp.getText().isEmpty()){
                                actionAnchor2.setText("Champ "+new_pwd.getText()+" vide.");
                            }else{
                                
                            user.setPasswd(newp.getText());
                            Connecter c=null;
                               try {
                                   c = new Connecter();
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(PaneUserProfil.class.getName()).log(Level.SEVERE, null, ex);
                                   actionAnchor1.setText("Probleme connection");
                               } catch (SQLException ex) {
                                   Logger.getLogger(PaneUserProfil.class.getName()).log(Level.SEVERE, null, ex);
                                   actionAnchor1.setText("Probleme connection");
                               }
                               Requettes con = new Requettes(c);
                               
                               try { 
                                   con.fUpdateUser3(user);
                                   c.getConnexion().close();
                                   
                                   
                                   actionAnchor1.setText("Votre mot de passe a été mis à jour." );
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(PaneUserProfil.class.getName()).log(Level.SEVERE, null, ex);
                                   actionAnchor1.setText("Echec d'insertion.");
                               } catch (SQLException ex) {
                                   Logger.getLogger(PaneUserProfil.class.getName()).log(Level.SEVERE, null, ex);
                                   actionAnchor1.setText("Echec d'insertion.");
                               }
                                
                                actionAnchor2.setText("Mot de passe mis a jour...");
                            }
                        }else{
                            
                             actionAnchor2.setText("Ancien Mot de passe invalide...");
                            
                        }
                    }
        }); 
        
        
        
        HBox hbBtnU = new HBox(10);
        hbBtnU.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnU.getChildren().addAll(modifierbtn,savebtn);
        gridpan.add(hbBtnU, 4, 10);
        
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().addAll(pwdbtn1,pwdbtn2);
        gridpan.add(hbBtn2, 5, 3);
        
        
        
        ScrollPane sp2 = new ScrollPane();

        sp2.setPrefSize(AUTO, AUTO);
        sp2.setContent(gridpan2);
        sp2.setPadding(new Insets(10, 10, 10, 10));
        
        gridpan.add(sp2,5,2);
        
        
        
        ScrollPane sp = new ScrollPane();
        
        
        
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(gridpan);
        sp.setPadding(new Insets(10, 10, 10, 10));
        
        
        
        
        FlowPane paneText = new FlowPane();
        lblscene.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
        paneText.getChildren().addAll(pics,lblscene,lblscene2);
        
        VBox vb = new VBox(10);
        vb.setPadding(new Insets(1, 10, 10, 10));
        vb.getChildren().addAll(paneText,sp);
        
        HBox hb = new HBox(5);
        VBox vgrid = new VBox();
        vgrid.getChildren().addAll(grid,new ModalSMTPParam(dao).contruire());
        hb.getChildren().addAll(vb,vgrid);
        
        pane.getChildren().addAll(hb);
        pane.autosize();
        //pane.setVgap(30);
        return pane;
    }
    
    
}
