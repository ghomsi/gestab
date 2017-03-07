/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author ghomsi
 */
public class TreeTableUser {
    
    private VBox pane,paneR;
    private Label label,label2;
    private final GridPane grid;
    
    private DAOequipement dao;
    
    
    private final ImageView depIcon = new ImageView (
            new Image(getClass().getResourceAsStream("/icon/secteuricon.png"))
    );

    TreeTableUser(DAOequipement dao,VBox paneDesc,Label lblscene,Label lblscene2,GridPane grid,VBox pane) {
        this.pane = paneDesc;
        this.label = lblscene;
        this.label2 = lblscene2;
        this.dao = dao;
        
        this.grid = grid;
        this.paneR = pane;
        
    }

    
    public void construire(){
        
        dao.treeTableViewUser(dao, pane, label, grid, "treetableviewU");
        
        
    }
}
