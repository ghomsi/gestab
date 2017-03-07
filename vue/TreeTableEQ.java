/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modele.Equipement;

/**
 *
 * @author ghomsi
 */
public class TreeTableEQ {
    
    private VBox pane;
    private HBox pageHBlabel;
    private final Label label;
    private TreeItem<Equipement> rootR;
    private DAOequipement dao;
    private GridPane grid;
    
    
    
    

    TreeTableEQ(DAOequipement dao,VBox paneDesc,Label lblscene,HBox HBlabel,GridPane grid) {
        this.pane=new VBox();
        this.pane = paneDesc;
        this.label = lblscene;
        this.pageHBlabel = HBlabel;
        this.grid = grid;
        this.dao = dao;
        rootR = new TreeItem<>();
        
    }

    
    void construire(){
        
        dao.treetableviewEq(dao, pane, label, pageHBlabel, grid,"treetablevieweq");
        
        
    } 
    
}
