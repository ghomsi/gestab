/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import modele.Client;
import modele.Historique;

/**
 *
 * @author ghomsi
 */
public class ModalHistorique {
    
    private ActionEvent event;
    private DAOequipement dao;
    private Client client;
    
    public ModalHistorique(ActionEvent event,DAOequipement fdao,Client client){
        this.event = event;
        this.dao = fdao;
        this.client = client;
    }
    
    void construire(){
    
        Stage stage = new Stage();
                
                VBox Vlist = new VBox(2);
                //Creating a treeitem table view
                TreeItem<Historique> rootDate =  new TreeItem<>();
                rootDate.setExpanded(true);
                
                List list = (List) dao.getClientHistorique().get(client.getId());
                Iterator<Historique> it = list.iterator();
                while (it.hasNext()) {
                       Historique s = it.next();
                       TreeItem<Historique>root =  new TreeItem<>(s);
                       rootDate.getChildren().add(root);
                }
                
                TreeTableView<Historique> treeTableViewDate = new TreeTableView<>(rootDate);
                
                //Creating a column
                TreeTableColumn<Historique,String> sectColumnDate1 = new TreeTableColumn<>("Activer le");
                TreeTableColumn<Historique,String> sectColumnDate2 = new TreeTableColumn<>("Suspendu le");
                TreeTableColumn<Historique,String> sectColumnDate3 = new TreeTableColumn<>("Résilié le");
                
                treeTableViewDate.getColumns().addAll(sectColumnDate1,sectColumnDate2,sectColumnDate3);
                treeTableViewDate.setMinWidth(800);
                treeTableViewDate.setMaxWidth(830);
                treeTableViewDate.setShowRoot(false);
                //Defining cell content
                //--dates suspensions
                sectColumnDate1.prefWidthProperty().bind(treeTableViewDate.widthProperty().multiply(0.3));
                sectColumnDate1.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Historique, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Historique, String> p) {
                        if(p.getValue().getValue().getStatus()==1){
                            return new ReadOnlyStringWrapper(p.getValue().getValue().getDate());
                        }else{
                            return new ReadOnlyStringWrapper("RAS");
                        } 
                    }
                });
                //--dates résiliations
                sectColumnDate2.prefWidthProperty().bind(treeTableViewDate.widthProperty().multiply(0.3));
                sectColumnDate2.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Historique, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Historique, String> p) {
                        if(p.getValue().getValue().getStatus()==2){
                            return new ReadOnlyStringWrapper(p.getValue().getValue().getDate());
                        }else{
                            return new ReadOnlyStringWrapper("RAS");
                        } 
                    }
                });
                //--numero de telephone
                sectColumnDate3.prefWidthProperty().bind(treeTableViewDate.widthProperty().multiply(0.3));
                sectColumnDate3.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Historique, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Historique, String> p) {
                        if(p.getValue().getValue().getStatus()==3){
                            return new ReadOnlyStringWrapper(p.getValue().getValue().getDate());
                        }else{
                            return new ReadOnlyStringWrapper("RAS");
                        }    
                    }
                });
                
                
                        
                        
                
                
                
                ScrollPane spDate = new ScrollPane();
        
                spDate.setPrefSize(AUTO,AUTO);
                spDate.setContent(treeTableViewDate);
                spDate.setPadding(new Insets(10, 10, 10, 10));
                spDate.setMinWidth(800);
                spDate.setMaxWidth(850);
                
                Button btn = new Button("Annuler");
                btn.getStyleClass().add(dao.boutonColor);
                
                
                
                
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        stage.close();
                    }
                });
                
                
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().addAll(btn);
                
                //conteneur vertical principal
                Label date = new Label("Date");
                date.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                date.setTextFill(Color.web(dao.couleur));
                Vlist.getChildren().addAll(date,hbBtn,spDate);
                Vlist.setMinWidth(800);



                ScrollPane sp = new ScrollPane();
        
                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(Vlist);
                sp.setPadding(new Insets(10, 10, 10, 10));
                
                Scene scene = new Scene(sp, (dao.width*85)/100, (dao.height*65)/100);
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Historiques");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
