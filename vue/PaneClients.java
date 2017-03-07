/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class PaneClients {
    
    VBox pane,paneDesc;
    HBox HBlabel;
    Button btnscene,ajouterCL;
    Label lblscene;
    private TreeTableClients treeTableView;
    private DAOequipement dao;
    GridPane grid;
    
   
    

    PaneClients(DAOequipement dao) {
        
        dao.couleur =dao.colordeGestionCl();
        dao.boutonColor="buttonGestCl";
        dao.comboColor ="comboAdminCl";
        this.dao = dao;
    }
      
    
    
    public VBox construire() throws ClassNotFoundException, SQLException{
        pane=new VBox();
        HBlabel = new HBox();
        pane.minHeightProperty().set(dao.getHeigth());
        paneDesc =new VBox();
        paneDesc.minHeightProperty().set(dao.getHeigth());
        pane.setPadding(new Insets(5, 5, 10, 10));
        //pane.setHgap(10);
        
        grid = new GridPane();
        //grid.setHgap(10);
        //grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        
        
        lblscene=new Label("Gestion Clients");
        lblscene.setTextFill(Color.web(dao.colordeGestionCl()));
        
        
        Button btnStat = new Button();
        ImageView depIconStat = new ImageView (
                        new Image(getClass().getResourceAsStream("/images/iconstatwhite.png"),30,30,false,false)
        );
        btnStat.setGraphic(depIconStat );
        btnStat.getStyleClass().add(dao.boutonColor);
        btnStat.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ModalStatClient modal = new ModalStatClient(t,dao);
                try {
                    modal.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PaneEquipement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PaneEquipement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        Image eqimages = new Image(getClass().getResourceAsStream("/images/ville1.jpg"));
        
        ImageView eqpics = new ImageView(eqimages);
        eqpics.setPreserveRatio(true);
        
        HBox imhb = new HBox();
        imhb.getChildren().add(eqpics);
        imhb.setPadding(new Insets(50, 50, 10, 10));
        paneDesc.getChildren().addAll(imhb);
        
        
        
        Image images = new Image(getClass().getResourceAsStream("/images/recherche.png"),200,300,false,false);
        ImageView pics = new ImageView(images);
        pics.setPreserveRatio(true);
        
        grid.add(pics, 2, 1);
        
        Hashtable villes = dao.getVille();
        
        treeTableView = new TreeTableClients(dao,paneDesc,lblscene,grid,pane);
        treeTableView.construire();
        //---- combo Box villes
        Enumeration e =villes.elements();
        ObservableList<Ville> options = FXCollections.observableArrayList();
        options.add(new Ville("Tout"));
        
        while(e.hasMoreElements()){
            Ville ville =(Ville) e.nextElement();
            options.add(ville);
        }
        
        
        final ComboBox clientCB = new ComboBox(options);
        clientCB.setPadding(new Insets(10, 10, 10, 10));
                
                
        clientCB.setPromptText("Filtrer...");
        clientCB.setEditable(true);
        clientCB.setCellFactory(new Callback<ListView<Ville>,ListCell<Ville>>(){
 
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
        
        
        
        clientCB.valueProperty().addListener(new ChangeListener<Ville>() {
                    @Override public void changed(ObservableValue ov, Ville t, Ville t1) {
                        if(t1!=null){
                            clientCB.setEditable(false);
                            if(t1.getNom()==null||t1.getNom().equals("Tout")){
                                dao.treeTableViewClient(dao, paneDesc, lblscene, grid);
                            }else{
                                dao.treeTableViewClient(dao, paneDesc, lblscene, grid,t1);
                            }   
                        }
                        
                    }    
                }); 
        
        
        
       
        
        //----- Button Ajouter un Secteur
        
        ajouterCL = new Button("Ajouter un client");
        ajouterCL.getStyleClass().add("buttonGestCl");
        final Tooltip tooltipCL = new Tooltip();
        tooltipCL.setText("Ajouter un client");
        ajouterCL.setTooltip(tooltipCL);
        ajouterCL.setPrefWidth((dao.width*20)/100);
        
        ajouterCL.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                ModalAddClient modal = new ModalAddClient(event,dao,lblscene,grid,paneDesc);
                modal.construire();
            }
        
        });
        
        grid.add(clientCB, 0, 0);
        
        //grid.getColumnConstraints().addAll(column1, column2);
        FlowPane paneText = new FlowPane();
        lblscene.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
        paneText.getChildren().addAll(lblscene,HBlabel);
        
        HBox head = new HBox(5);
        head.getChildren().addAll(btnStat,paneText);
        grid.add(head, 1, 0);
        //grid.add(treeTableView, 0, 1);
        grid.add(ajouterCL,0,2);
        
        
        
        
        grid.add(paneDesc,1,1);
        
        /*ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        grid.maxWidth((dao.width*30)/100);
        sp.setContent(grid);*/
        pane.getChildren().addAll(grid);
        pane.autosize();
        return pane;
    }
    
    
     
}
