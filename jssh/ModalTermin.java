/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jssh;

import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Equipement;
import modele.Ville;
import vue.ModalAddVille;

/**
 *
 * @author ghomsi
 */
public class ModalTermin {
 
    private DAOequipement dao;
    private ActionEvent event;
    
    public ModalTermin(ActionEvent event,DAOequipement dao){
        this.dao = dao;
        this.event = event;
    }
    
    public void construire() throws ClassNotFoundException, SQLException, IOException{
    
        Stage stage = new Stage();
                
                
              Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
                
                Scene scene = new Scene(root);
                scene.getStylesheets().add("/css/Styles.css");

                
                stage.setScene(scene);
                stage.setTitle("TerminalFX");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
    
}
