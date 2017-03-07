/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import controleur.DAOequipement;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author ghomsi
 */
public class TreeTableEQAdmin {
    
    VBox pane;
    Label label,label2,label3;
    GridPane grid;
    private DAOequipement dao;
    HBox pageHBlabel;
    
    private final ImageView depIcon = new ImageView (
            new Image(getClass().getResourceAsStream("/icon/secteuricon.png"))
    );

    TreeTableEQAdmin(DAOequipement dao,VBox paneDesc,Label lblscene,HBox HBlabel,GridPane grid) {
        this.pane = paneDesc;
        this.label = lblscene;
        this.pageHBlabel = HBlabel;
        this.grid = grid;
        this.dao = dao;
        
    }

    
    void construire(){
        dao.treetableviewAdminEq(dao, pane, label, pageHBlabel, grid,"treetableviewAE");
        
        
    }
}
