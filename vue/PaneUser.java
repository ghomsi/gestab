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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
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
public class PaneUser {
    
    VBox pane,paneDesc;
    Button btnscene,ajouterU;
    Label lblscene,lblscene2;
    private DAOequipement dao;
    
   
    

    PaneUser(DAOequipement dao) {
        this.dao = dao;
    }
      
    
    
    VBox construire() throws ClassNotFoundException, SQLException{
        
        pane=new VBox();
        pane.minHeightProperty().set(dao.getHeigth());
        paneDesc =new VBox();
        paneDesc.minHeightProperty().set(dao.getHeigth());
        pane.setPadding(new Insets(5, 5, 10, 10));
        //pane.setVgap(10);
        //pane.setHgap(10);
        
        GridPane grid = new GridPane();
       
        //grid.setHgap(10);
        //grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(5);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        
        
        
        
        
       
        
        lblscene=new Label("Administration Utilisateurs");
        lblscene.setTextFill(Color.web(dao.colordeAdminU()));
        lblscene2=new Label("");
        
        Image eqimages = new Image(getClass().getResourceAsStream("/images/ville.png"));
        
        ImageView eqpics = new ImageView(eqimages);
      
        eqpics.setPreserveRatio(true);
        
        HBox imhb = new HBox();
        imhb.getChildren().add(eqpics);
        imhb.setPadding(new Insets(50, 50, 10, 10));
        paneDesc.getChildren().addAll(imhb);
        
       
        
        
        
        Hashtable villes = dao.getVille();
        
        TreeTableUser treeTableView = new TreeTableUser(dao,paneDesc,lblscene,lblscene2,grid,pane);
        treeTableView.construire();
        
        //---- combo Box villes
        Enumeration e =villes.elements();
        ObservableList<Ville> options = FXCollections.observableArrayList();
        options.add(new Ville("Tout"));
        
        while(e.hasMoreElements()){
            Ville ville =(Ville) e.nextElement();
            options.add(ville);
        }
        
        final ComboBox comboBox = new ComboBox(options);
        comboBox.setPadding(new Insets(10, 10, 10, 10));
                
                
        comboBox.setPromptText("Filtre");
        comboBox.setEditable(true);        
        comboBox.setCellFactory(new Callback<ListView<Ville>,ListCell<Ville>>(){
 
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
        
        
        
        comboBox.valueProperty().addListener(new ChangeListener<Ville>() {
                    @Override public void changed(ObservableValue ov, Ville t, Ville t1) {
                        comboBox.setEditable(false);
                       if(t1!=null){ 
                            if(t1.getNom().equals("Tout")){
                                dao.treeTableViewUser(dao, paneDesc, lblscene, grid, "treetableviewU");
                            }else{
                                dao.treeTableViewUser(dao, paneDesc, lblscene, grid, "treetableviewU",t1);
                            }
                       } 
                    }    
                });  
        
        
        
        
        
        
        //----- Button Ajouter un Secteur
        
        ajouterU = new Button("Ajouter un utilisateur");
        ajouterU.getStyleClass().add("buttonAdminU");
        final Tooltip tooltipUser = new Tooltip();
        tooltipUser.setText("Ajouter un utilisateur");
        ajouterU.setTooltip(tooltipUser);
        ajouterU.setPrefWidth((dao.width*20)/100);
        
        ajouterU.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Ajouter un utilisateur");
                ModalAddUser modal = new ModalAddUser(event,dao);
                try {
                    modal.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PaneUser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PaneUser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        });
        
        grid.add(comboBox, 0, 0);
        
        //grid.getColumnConstraints().addAll(column1, column2);
        FlowPane paneText = new FlowPane();
        lblscene.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
        paneText.getChildren().addAll(lblscene,lblscene2);
        grid.add(paneText, 1, 0);
        //grid.add(treeTableView, 0, 1);
        grid.add(ajouterU,0,2);
        grid.add(paneDesc,1,1);
        //pane.getChildren().addAll(lblscene, btnscene);
        pane.getChildren().addAll(grid);
        pane.autosize();
        return pane;
    }
    
    
}
