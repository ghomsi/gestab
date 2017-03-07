/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import modele.Client;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class PaneClientVille {
    
    FlowPane pane,paneDesc;
    Label lblscene,lblscene2;
    private final Ville ville;
    private DAOequipement dao;
    private PieChart chart;
    
    public PaneClientVille(Client clt,DAOequipement dao){
        this.ville = (Ville) dao.getVille().get(clt.getId());
        this.dao = dao;
        this.chart = null;
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
        
                     
        
          
        
        Label nbEQ = new Label ("Nombre Clients:");
        grid.add(nbEQ, 1,2 );
        Label nbEQL = new Label (""+ville.getNbClients());
        grid.add(nbEQL, 2, 2);
            
        /*Label nbCart = new Label ("Clients 'OK':");
        grid.add(nbCart, 1, 3);
        Label nbCartL = new Label (""+ville.getNbCarte());
        grid.add(nbCartL, 2, 3);
            
        Label nbPort = new Label("Clients 'SUSPENDU':");
        grid.add(nbPort, 1, 4);
        Label nbPortL = new Label (""+ville.getNbPort());
        grid.add(nbPortL, 2,4);*/
        
        VBox vb = new VBox();
        vb.setPadding(new Insets(1, 10, 10, 10));
        if(ville.getNbClients()!=0){
            chart = dao.chart("Graphe: ","Client(s) "+ville.getNom() , ville.getNbClients(), "clients", dao.getClient().size());
            vb.getChildren().addAll(grid,chart);
        }else{
            vb.getChildren().addAll(grid);
        }
        
        
        
        
             
        
        
        
        
        
        
        
        
        
        FlowPane paneText = new FlowPane();
        lblscene.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        paneText.getChildren().addAll(lblscene,lblscene2);
        
        grid.add(paneText, 1, 0);
        
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(vb);
        sp.setPadding(new Insets(10, 10, 10, 10));
        
        
        
        
        
        
        
        pane.getChildren().addAll(sp);
        pane.autosize();
        pane.setVgap(30);
        return pane;
    }
}
