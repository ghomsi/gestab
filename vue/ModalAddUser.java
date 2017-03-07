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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import modele.User;
import modele.Ville;
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class ModalAddUser {
    
    private final ActionEvent event;
    Scene scene,scene2;
    Image images;
    ImageView pics;
    private DAOequipement dao;
    private Ville ville;
    
    public ModalAddUser(ActionEvent event,DAOequipement dao){
        this.event = event;
        this.images = new Image(getClass().getResourceAsStream("/images/user.png"));
        this.dao = dao;
    }
    
    void construire() throws ClassNotFoundException, SQLException{
    
       Stage stage = new Stage();
       
       images = new Image(getClass().getResourceAsStream("/images/user.png"),100,100,false,false);
       pics = new ImageView(images);
       
       
       Label userTypeLbl = new Label("Type:");
       Label villeUserLbl = new Label("Ville:");
       Label villeUserLblView = new Label("");
       
       List<String> cats = dao.getCategorie();
       ObservableList<String> optionscat =FXCollections.observableArrayList();
       
       cats.stream().forEach((Consumer<? super String>) (elt) -> {
            optionscat.add(elt);
        });
       ComboBox userTypeCB = new ComboBox(optionscat);
       userTypeCB.getStyleClass().add("comboAdminU");
       userTypeCB.setPromptText("choix");
       
       
       //---- combo Box villes
        Hashtable villes = dao.getVille();

        ObservableList<Ville> options = FXCollections.observableArrayList();
        Enumeration e = villes.elements();
        while(e.hasMoreElements()) {
            Ville elt = (Ville) e.nextElement();
            options.add(elt);
        }
       
       ComboBox villeUserCB = new ComboBox(options); 
       villeUserCB.getStyleClass().add("comboAdminU");
       villeUserCB.setCellFactory(new Callback<ListView<Ville>,ListCell<Ville>>(){
 
            @Override
            public ListCell<Ville> call(ListView<Ville> p) {
                 
                final ListCell<Ville> cell = new ListCell<Ville>(){
 
                    @Override
                    protected void updateItem(Ville t, boolean bln) {
                        super.updateItem(t, bln);
                         
                        if(t != null){
                            setText(t.getNom());
                        }else{
                            setText(null);
                        }
                    }
  
                };
                 
                return cell;
            }
        });
       
       villeUserCB.setPromptText("choix");
       
       villeUserCB.valueProperty().addListener(new ChangeListener<Ville>() {
            @Override public void changed(ObservableValue ov, Ville t, Ville t1) {
                
              ville = t1; 
              villeUserLblView.setText(t1.getNom());
              
                
                
            }    
        });
       
       
       Label TelephoneLbl = new Label("TEL :");
       TextField telephoneTfd = new TextField();
       Label nomLbl = new Label("NOM :");
       TextField nomTfd = new TextField();
       Label prenomLbl = new Label("PRENOM :");
       TextField prenomTfd = new TextField();
       Label quartierLbl = new Label("QUARTIER :");
       TextField quartierTfd = new TextField();
       Label loginLbl = new Label("LOGIN :");
       TextField loginTfd = new TextField();
       Label passwordLbl = new Label("PASSWORD :");
       PasswordField passwordFld = new PasswordField();
       passwordFld.setPromptText("Saisir Mot de Passe");
       
       Label passwordFld2 = new Label("ENCORE PASSWORD :");// Déclaration des variables 
       
       PasswordField passwordTld2 = new PasswordField();
       passwordTld2.setPromptText("Resaisir Mot de Passe:");
       
       Label textAreaLbl = new Label("Raison Social :");
       TextArea textArea = new TextArea();
       textArea.prefWidthProperty().set(150);
       textArea.prefHeightProperty().set(100);
       
       Label emailLbl = new Label("Email :");
       TextField emailTfd = new TextField();
       
       Label actiontarget = new Label("");
        
       GridPane grid= new GridPane();
       grid.setAlignment(Pos.CENTER);
       grid.setGridLinesVisible(false);
       //grid.setPadding( new Insets (40,20,40,20 ));
       grid.setVgap(10);
       grid.setHgap(10);
       
       grid.add(pics,1,0);
       
       grid.add(userTypeLbl,1,1);
       grid.add(userTypeCB,2,1);
       
       grid.add(villeUserLbl,1,2);
       grid.add(villeUserCB,2,2);
       grid.add(villeUserLblView, 2, 3);
       
       grid.add(TelephoneLbl,1,4);
       grid.add(telephoneTfd,2,4);
       
       grid.add(nomLbl,1,5);
       grid.add(nomTfd,2,5);
       
       grid.add(prenomLbl,1,6);
       grid.add(prenomTfd,2,6);
       
       grid.add(quartierLbl,1,7);
       grid.add(quartierTfd,2,7);
       
       grid.add(loginLbl,1,8);
       grid.add(loginTfd,2,8);
       
       grid.add(passwordLbl,1,9);
       grid.add(passwordFld,2,9);
       
       
       grid.add(passwordFld2,1,10);
       grid.add(passwordTld2,2,10);
       
       grid.add(textAreaLbl,3,1);
       grid.add(textArea,3,2);
       grid.add(emailLbl,3,3);
       grid.add(emailTfd,3,4);

        //BUTTON
        Button resetbt = new Button ("Annuler");
        resetbt.getStyleClass().add(dao.boutonColor);
        
        
        resetbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
            });

        Button validerbt = new Button ("Ajouter");
        validerbt.getStyleClass().add(dao.colornodanger());
        
        validerbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if(telephoneTfd.getText().isEmpty() || !dao.isNumeroCamtel(telephoneTfd.getText())){
                        actiontarget.setText(TelephoneLbl.getText()+" Champ vide/numero non camtel");
                    }else if(nomTfd.getText().isEmpty()){
                        actiontarget.setText(nomLbl.getText()+" Champ vide");
                    }else if(prenomTfd.getText().isEmpty()){
                        actiontarget.setText(prenomLbl.getText()+" Champ vide");
                    }else if(quartierTfd.getText().isEmpty()){
                        actiontarget.setText(quartierLbl.getText()+" Champ vide");
                    }else if(loginTfd.getText().isEmpty()){
                        actiontarget.setText(loginLbl.getText()+" Champ vide");
                    }else if(passwordFld.getText().isEmpty()){
                        actiontarget.setText(passwordLbl.getText()+" Champ vide");
                    }else if(textArea.getText().isEmpty()){
                        actiontarget.setText(textAreaLbl.getText()+" Champ vide");
                    }else if(emailTfd.getText().isEmpty() || !dao.isEmail(emailTfd.getText())){
                        actiontarget.setText(emailLbl.getText()+" Champ vide/email invalide");
                    }else if(villeUserCB.getValue()==null){
                        actiontarget.setText(villeUserLbl.getText()+" Champ vide");
                    }else if(userTypeCB.getValue()==null){
                        actiontarget.setText(userTypeLbl.getText()+" Champ vide");
                    }else{
                        if(!passwordFld.getText().equals(passwordTld2.getText())){
                            actiontarget.setText("Les mots de passe ne corresponde pas!");
                        }else{
                            Connecter c=null;
                            try {
                                c = new Connecter();
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ModalAddUser.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(ModalAddUser.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            Requettes con = new Requettes(c);


                            User user = new User();    
                            user.setNom(nomTfd.getText());
                            user.setPrenom(prenomTfd.getText());
                            user.setVille(ville.getId());
                            user.setQuartier(quartierTfd.getText());
                            user.setEmail(emailTfd.getText());
                            user.setNumTel(telephoneTfd.getText());
                            user.setLogin(loginTfd.getText());
                            user.setNivoAccess(dao.getIntType(userTypeCB.getValue().toString()));
                            user.setPasswd(passwordFld.getText());
                            user.setDesc(textArea.getText());

                            try {
                                int id =con.fInsertUser(user);
                                c.getConnexion().close();

                                dao.getUser().put(id, user);
                                ArrayList ht = (ArrayList) dao.getUserVille().get(user.getVille());
                                ht.add(id);

                                actiontarget.setText(nomTfd.getText()+" "+prenomTfd.getText()+" Ajouter!");
                                stage.close();
                                new Notifier("Gestab Notification",nomTfd.getText()+" "+prenomTfd.getText()+" Ajouter!",2);
                            } catch (SQLException ex) {
                                Logger.getLogger(ModalAddUser.class.getName()).log(Level.SEVERE, null, ex);
                                actiontarget.setText("Erreur lors de l'insertion");
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ModalAddUser.class.getName()).log(Level.SEVERE, null, ex);
                                actiontarget.setText("Erreur lors de l'insertion");
                            }
                        }    
                    }
                }
            });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(resetbt,validerbt);
        grid.add(hbBtn, 1, 11);
        grid.add(actiontarget,3,0);
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(grid);
        sp.setPadding(new Insets(10, 10, 10, 10));
                
        scene = new Scene(sp, (dao.width*62)/100, (dao.height*80)/100);
        scene.getStylesheets().add("css/style.css");
                
               
                
                
        stage.setScene(scene);
        stage.setTitle("Ajouter un Utilisateur");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(
        ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
}
