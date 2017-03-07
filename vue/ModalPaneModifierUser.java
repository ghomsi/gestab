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

/**
 *
 * @author ghomsi
 */
public class ModalPaneModifierUser {
    
    private final ActionEvent event;
    Scene scene,scene2;
    Image images;
    ImageView pics;
    private User user;
    private DAOequipement dao;
    private Ville villeR;
    
    public ModalPaneModifierUser(ActionEvent event,User user,DAOequipement dao){
        this.event = event;
        this.images = new Image(getClass().getResourceAsStream("/images/user.png"));
        this.user= user;
        this.dao = dao;
    }
    
    void construire() throws ClassNotFoundException, SQLException{
    
       Stage stage = new Stage();
       
       images = new Image(getClass().getResourceAsStream("/images/user.png"),100,100,false,false);
       pics = new ImageView(images);
       
       
       Label userTypeLbl = new Label("Type:");
       Label villeUserLbl = new Label("Ville:");
       
       List<String> cats = dao.getCategorie();
       ObservableList<String> optionscat =FXCollections.observableArrayList();
       
       cats.stream().forEach((Consumer<? super String>) (elt) -> {
            optionscat.add(elt);
        });
       ComboBox userType = new ComboBox(optionscat);
       userType.getStyleClass().add("comboAdminU");
       userType.promptTextProperty().set("confirmer "+user.getCategorie());
       
       Hashtable villes =  dao.getVille();
       
       //---- combo Box villes
        ObservableList<Ville> options =FXCollections.observableArrayList();
        
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
       
       
       villeUserCB.valueProperty().addListener(new ChangeListener<Ville>() {
            @Override public void changed(ObservableValue ov, Ville t, Ville t1) {
                
              villeR = t1; 
              //System.out.println("ville:"+ville.getId());
              
                
                
            }    
        });
       
       
      
       Ville ville = (Ville) dao.getVille().get(user.getVille());
       villeUserCB.promptTextProperty().set("confirmer "+ville.getNom());
       
       Label TelephoneLbl = new Label("TEL :");
       TextField telephoneTfd = new TextField();
       telephoneTfd.setText(user.getNumTel());
       
       Label nomLbl = new Label("NOM :");
       TextField nomTfd = new TextField();
       nomTfd.setText(user.getNom());
       
       Label prenomLbl = new Label("PRENOM :");
       TextField prenomTfd = new TextField();
       prenomTfd.setText(user.getPrenom());
       
       Label quartierLbl = new Label("QUARTIER :");
       TextField quartierTfd = new TextField();
       quartierTfd.setText(user.getQuartier());
       
       Label loginLbl = new Label("LOGIN :");
       TextField loginTfd = new TextField();
       loginTfd.setText(user.getLogin());
       
       Label passwordLbl = new Label("PASSWORD :");
       PasswordField passwordFld = new PasswordField();
       //passwordFld.setText(user.getPasswd());
       
       
       Label textAreaLbl = new Label("Raison Social :");
       TextArea textArea = new TextArea();
       textArea.setText(user.getDesc());
       textArea.prefWidthProperty().set(150);
       textArea.prefHeightProperty().set(100);
       
       Label emailLbl = new Label("Email :");
       TextField emailTfd = new TextField();
       emailTfd.setText(user.getEmail());
       
       Label actiontarget = new Label("");
        
       GridPane grid= new GridPane();
       grid.setAlignment(Pos.CENTER);
       grid.setGridLinesVisible(false);
       grid.setVgap(10);
       grid.setHgap(10);
       
       grid.add(pics,1,0);
       
       grid.add(userTypeLbl,1,1);
       grid.add(userType,2,1);
       
       grid.add(villeUserLbl,1,2);
       grid.add(villeUserCB,2,2);
       
       grid.add(TelephoneLbl,1,3);
       grid.add(telephoneTfd,2,3);
       
       grid.add(nomLbl,1,4);
       grid.add(nomTfd,2,4);
       
       grid.add(prenomLbl,1,5);
       grid.add(prenomTfd,2,5);
       
       grid.add(quartierLbl,1,6);
       grid.add(quartierTfd,2,6);
       
       grid.add(loginLbl,1,7);
       grid.add(loginTfd,2,7);
       
       grid.add(passwordLbl,1,8);
       grid.add(passwordFld,2,8);
       
       Label actionTarget = new Label();
       grid.add(actionTarget,3,5);
       
       
       grid.add(textAreaLbl,3,1);
       grid.add(textArea,3,2);
       grid.add(emailLbl,3,3);
       grid.add(emailTfd,3,4);

        //BUTTON
        Button resetbt = new Button ("Annuler");
        resetbt.getStyleClass().add("buttonAdminU");
        
        
        resetbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
            });

        Button validerbt = new Button ("Sauvegarder");
        validerbt.getStyleClass().add("buttonAdminU");
        
        validerbt.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if(nomTfd.getText().isEmpty()){
                        actionTarget.setText(nomLbl.getText()+" Champ vide");
                    }else if(prenomTfd.getText().isEmpty()){
                        actionTarget.setText(prenomLbl.getText()+" Champ vide");
                    }else if(telephoneTfd.getText().isEmpty()){
                        actionTarget.setText(TelephoneLbl.getText()+" Champ vide");
                    }else if(quartierTfd.getText().isEmpty()){
                        actionTarget.setText(quartierLbl.getText()+" Champ vide");
                    }else if(loginTfd.getText().isEmpty()){
                        actionTarget.setText(loginLbl.getText()+" Champ vide");
                    }else if(passwordFld.getText().isEmpty()){
                        actionTarget.setText(passwordLbl.getText()+" Champ vide");
                    }else if(textArea.getText().isEmpty()){
                        actionTarget.setText(textAreaLbl.getText()+" Champ vide");
                    }else if(emailTfd.getText().isEmpty()){
                        actionTarget.setText(emailLbl.getText()+" Champ vide");
                    }else{
                        if(villeR!=null){
                            user.setVille(villeR.getId());
                        }
                        if(userType.getValue()!=null){
                            user.setNivoAccess(user.getintCategorie(userType.getValue().toString()));
                        }
                        /*if(passwordFld.getText().equals(event)){
                            actionTarget.setText(nomTfd.getText().toUpperCase()+" "+prenomTfd.getText().toUpperCase()+" existe déja!");
                        }else{*/
                            Connecter c=null;
                            try {
                                c = new Connecter();
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ModalPaneModifierUser.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(ModalPaneModifierUser.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            Requettes con = new Requettes(c);
                               
                               //User user = new User();
                               user.setNom(nomTfd.getText().toUpperCase());
                               user.setPrenom(prenomTfd.getText().toUpperCase());
                               user.setNumTel(telephoneTfd.getText());
                               user.setDesc(textArea.getText());
                               user.setQuartier(quartierTfd.getText().toUpperCase());
                               user.setLogin(loginTfd.getText());
                               user.setPasswd(passwordFld.getText());
                               user.setEmail(emailTfd.getText());
                               
                               
                               try { 
                                   con.fUpdateUser(user);
                                   c.getConnexion().close();
                                   dao.getUser().replace(user.getId(), user);
                                   
                                   
                                   actionTarget.setText("Utilisateur mis a jour!");
                                   stage.close();
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalPaneModifierUser.class.getName()).log(Level.SEVERE, null, ex);
                                   actionTarget.setText("Echec d'insertion.");
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalPaneModifierUser.class.getName()).log(Level.SEVERE, null, ex);
                                   actionTarget.setText("Echec d'insertion.");
                               }
                        /*}*/
                    }
                }
            });
        
        

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(resetbt,validerbt);
        grid.add(hbBtn, 1, 10);
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(grid);
        sp.setPadding(new Insets(10, 10, 10, 10));
                
        scene = new Scene(sp, (dao.width*65)/100, (dao.height*72)/100);
        scene.getStylesheets().add("css/style.css");
                
               
                
                
        stage.setScene(scene);
        stage.setTitle("Modifier un Utilisateur");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(
            ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
}
