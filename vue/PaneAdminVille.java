/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeTableView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import modele.Equipement;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class PaneAdminVille {
    
    VBox pane,paneDesc;
    HBox pageHBlabel;
    Label label,lblscene,lblscene2;
    private final Ville ville;
    private DAOequipement dao;
    private PieChart chart;
    private HBox hbox;
    private GridPane gridR;
    TreeTableView<Equipement> treeTableView;
    VBox paneR;
    
    public PaneAdminVille(DAOequipement dao,Equipement EQ,Label label,HBox HBlabel,GridPane grid,VBox pane){
        this.ville = (Ville) dao.getVille().get(EQ.getIdEQ());
        this.dao = dao;
        this.label = label;
        this.pageHBlabel = HBlabel;
        this.hbox = hbox;
        this.gridR = grid;
        this.treeTableView = dao.getTreeTE();
        this.paneR = pane;
        this.chart = null;
                
    }
    
    
    
    public VBox construire(){
        pane=new VBox();
        //pane.minHeightProperty().set(dao.getHeigth());
 
        lblscene=new Label("Etats");
        lblscene.setTextFill(Color.web(dao.colordeAdminEq()));
        lblscene2=new Label("");
        
        
        
        GridPane grid = new GridPane(); 
        grid.setAlignment(Pos.CENTER);
                 
        grid.setVgap(10);
        grid.setHgap(10);
        
                     
        
          
        
        Label nbEQ = new Label ("Nombre Equipements:");
        grid.add(nbEQ, 1,2 );
        Label nbEQL = new Label (""+ville.getNbEq());
        grid.add(nbEQL, 2, 2);
            
        Label nbCart = new Label ("Nombre Cartes:");
        grid.add(nbCart, 1, 3);
        Label nbCartL = new Label (""+ville.getNbCarte());
        grid.add(nbCartL, 2, 3);
            
        Label nbPort = new Label("Nombre Ports:");
        grid.add(nbPort, 1, 4);
        Label nbPortL = new Label (""+ville.getNbPort());
        grid.add(nbPortL, 2,4);
            
        Label nbPLibre = new Label ("Nombre Ports Libre:");
        grid.add(nbPLibre, 1, 5);
        Label nbPLibreL = new Label (""+ville.getNbPortLibre());
        grid.add(nbPLibreL, 2, 5);
             
        Label nbPOcc = new Label ("Nombre Ports Occupé:");
        grid.add(nbPOcc, 1, 6);
        Label nbPOccL = new Label (""+ville.getNbPortOccupe());
        grid.add(nbPOccL, 2, 6);
        
        Label nbPDef = new Label ("Nombre Ports Défectueux:");
        grid.add(nbPDef, 1, 7);
        Label nbPDefL = new Label (""+ville.getNbPortDefect());
        grid.add(nbPDefL, 2, 7);
        
        Label actiontarget = new Label("");
        grid.add(actiontarget, 2, 9);     
             
        Button deletebtn = new Button (" supprimer ");
        deletebtn.getStyleClass().add(dao.colordanger());
        
        deletebtn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        //stage.close();
                        ModalPaneDeleteVille modalVille = new ModalPaneDeleteVille(dao,t,ville,label,pageHBlabel,gridR,paneR);
                        
                        modalVille.construire();
                            
                        
                        
                    }
         });
        
        
        
        Button modifbtn = new Button (" Modifier ");
        modifbtn.getStyleClass().add(dao.boutonColor);
        
                
        modifbtn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        //stage.close();
                        ModalPaneModifierVille modalVille = new ModalPaneModifierVille(dao,t,ville,label);
                        
                        try {
                            modalVille.construire();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(PaneAdminVille.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(PaneAdminVille.class.getName()).log(Level.SEVERE, null, ex);
                        }
                            
                        
                        
                    }
         });        
        
        
        
        
          //---- Button Ajouter une ville       
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        if(dao.isSuperAdmin(dao.user)){
            hbBtn.getChildren().addAll(deletebtn,modifbtn);
        }else{
            hbBtn.getChildren().addAll(modifbtn);
        }    
        grid.add(hbBtn, 4, 10);
        
        
        
        
        FlowPane paneText = new FlowPane();
        lblscene.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        paneText.getChildren().addAll(lblscene,lblscene2);
        
        grid.add(paneText, 1, 0);
        VBox vb = new VBox();
        vb.setPadding(new Insets(1, 10, 10, 10));
        
        if(ville.getNbPortLibre()!=0||ville.getNbPortOccupe()!=0||ville.getNbPortDefect()!=0){
            chart = dao.chart("Graphe: ",nbPLibre.getText() , ville.getNbPortLibre(), nbPOcc.getText(), ville.getNbPortOccupe(), nbPDef.getText(), ville.getNbPortDefect());
            vb.getChildren().addAll(grid,chart);
        }else{
            vb.getChildren().addAll(grid);
        }
        
        
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(vb);
        sp.setPadding(new Insets(10, 10, 10, 10));
        
        
        
        
        
        
        
        pane.getChildren().addAll(sp);
        pane.autosize();
        //pane.setVgap(30);
        return pane;
    }
}
