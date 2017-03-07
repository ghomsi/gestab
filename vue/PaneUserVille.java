/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import modele.User;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class PaneUserVille {
    
    FlowPane pane,paneDesc;
    Label lblscene,lblscene2;
    private final Ville ville;
    private DAOequipement dao;
    
    public PaneUserVille(DAOequipement dao,User usr){
        this.ville = new Ville(usr.getNom());
        this.dao = dao;
    }
    
    
    
    public FlowPane construire(){
        pane=new FlowPane();
        pane.minHeightProperty().set(dao.getHeigth());
 
        lblscene=new Label("Etats");
        lblscene2=new Label("");
        
        
        
        GridPane grid = new GridPane(); 
        grid.setAlignment(Pos.CENTER);
                 
        grid.setVgap(10);
        grid.setHgap(10);
        
                     
        
          
        
        Label nbEQ = new Label ("Nombre Utilisateurs:");
        grid.add(nbEQ, 1,2 );
//        Label nbEQL = new Label (""+ville.getUsers().size());
//        grid.add(nbEQL, 2, 2);
          
             
             
        
        
        
        
        
        
        
        
        
        FlowPane paneText = new FlowPane();
        lblscene.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        paneText.getChildren().addAll(lblscene,lblscene2);
        
        grid.add(paneText, 1, 0);
        
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(grid);
        sp.setPadding(new Insets(10, 10, 10, 10));
        
        
        
        
        
        
        
        pane.getChildren().addAll(sp);
        pane.autosize();
        pane.setVgap(30);
        return pane;
    }
}
