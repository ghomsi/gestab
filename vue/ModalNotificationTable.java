/*
 * Copyright (C) 2016 ghomsi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package vue;

import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import modele.Debit;
import modele.Notification;
import modele.User;
import notifier.Notifier;
import org.controlsfx.control.MasterDetailPane;

/**
 *
 * @author ghomsi
 */
public class ModalNotificationTable {
    private ActionEvent event;
    private DAOequipement dao;
    private Notification myNotif = null;
    private Boolean notifrecu = true;
    
    public ModalNotificationTable(ActionEvent event,DAOequipement fdao){
        this.event = event;
        this.dao = fdao;
    
    }
    
    void construire(){
    
        Stage stage = new Stage();
        ScrollPane sp = new ScrollPane();
        Scene scene = new Scene(sp, (dao.width*85)/100, (dao.height*65)/100);
                
                VBox Vlist = new VBox(2);
                //Creating a treeitem table view
                TreeItem<Notification> rootDate =  new TreeItem<>();
                rootDate.setExpanded(true);
                Hashtable notifications = dao.getNotificationRecus();
                Enumeration e =  notifications.elements();
                
                while (e.hasMoreElements()) {
                       Notification s = (Notification) e.nextElement();
                       TreeItem<Notification>root =  new TreeItem<>(s);
                       rootDate.getChildren().add(root);
                }
                        
                TreeTableView<Notification> treeTableViewDate = new TreeTableView<>(rootDate);
                
                //Creating a column
                TreeTableColumn<Notification, Image> sectColumnLue = new TreeTableColumn<>("Lue");
                TreeTableColumn<Notification,String> sectColumnDate = new TreeTableColumn<>("Date");
                TreeTableColumn<Notification,String> sectColumnAut = new TreeTableColumn<>("Auteur");
                TreeTableColumn<Notification,String> sectColumnObjet = new TreeTableColumn<>("Objet");
                
                treeTableViewDate.getColumns().addAll(sectColumnLue,sectColumnDate,sectColumnAut,sectColumnObjet);
                treeTableViewDate.setMinWidth(800);
                treeTableViewDate.setMaxWidth(830);
                treeTableViewDate.setShowRoot(false);
                
                HBox hbDate = new HBox();
                VBox vbDate = new VBox();
                TextArea textA = new TextArea();
                textA.setPrefSize(230, 250);
                textA.setWrapText(true);
                textA.setEditable(false);
                HBox dateBtn = new HBox(5);
                ImageView deleteIcon = new ImageView (
                    new Image(getClass().getResourceAsStream("/icon/delete.png"),20,20,false,false)
                );
                ImageView vueIcon = new ImageView (
                    new Image(getClass().getResourceAsStream("/icon/vue.png"),20,20,false,false)
                );
                Button btnDelete = new Button();
                btnDelete.setGraphic(deleteIcon);
                Button btnVue = new Button();
                btnVue.setGraphic(vueIcon);
                
                dateBtn.getChildren().addAll(btnDelete,btnVue);
                vbDate.getChildren().addAll(textA,dateBtn);
                vbDate.setVisible(false);
                hbDate.getChildren().addAll(treeTableViewDate,vbDate);
                
                //Defining cell content
                //--lue
                ImageView lueIcon = new ImageView (
                    new Image(getClass().getResourceAsStream("/icon/vue.png"),30,30,false,false)
                );
                sectColumnLue.setCellValueFactory(param -> { 
                    final TreeItem<Notification> item = param.getValue(); 
                    final Notification data = item.getValue();
                    
                    return (data instanceof Notification) ? new SimpleObjectProperty<>(((Notification) data).getImage()) : null; 
                });
                sectColumnLue.setGraphic(lueIcon);
                sectColumnLue.prefWidthProperty().bind(treeTableViewDate.widthProperty().multiply(0.1));
                sectColumnLue.setCellFactory(column -> new TreeTableCell<Notification, Image>(){ 
                    private final ImageView imageView1 = new ImageView();             
                    private final ImageView imageView2 = new ImageView();             
                    private final Tooltip tooltip = new Tooltip(); 

                    { 
                        imageView1.setFitHeight(25); 
                        imageView1.setPreserveRatio(true); 
                        imageView1.setSmooth(true); 
                        tooltip.setText(null); 
                        tooltip.setGraphic(imageView2); 
                    } 

                    @Override 
                    protected void updateItem(Image item, boolean empty) { 
                        super.updateItem(item, empty);  
                        setGraphic(null); 
                        setText(null); 
                        setTooltip(null); 
                        if (!empty && item != null) { 
                            imageView1.setImage(item); 
                            imageView2.setImage(item); 
                            setGraphic(imageView1); 
                            setTooltip(tooltip); 
                        } 
                    }             
                });
                //--dates notification
                ImageView hourIcon = new ImageView (
                    new Image(getClass().getResourceAsStream("/icon/hour.png"),30,30,false,false)
                );
                sectColumnDate.setGraphic(hourIcon);
                sectColumnDate.prefWidthProperty().bind(treeTableViewDate.widthProperty().multiply(0.2));
                sectColumnDate.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Notification, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Notification, String> p) {
                        
                            return new ReadOnlyStringWrapper(p.getValue().getValue().getDate());
                        
                    }
                });
                //--auteur
                ImageView auteurIcon = new ImageView (
                    new Image(getClass().getResourceAsStream("/icon/author.png"),30,30,false,false)
                );
                sectColumnAut.setGraphic(auteurIcon);
                sectColumnAut.prefWidthProperty().bind(treeTableViewDate.widthProperty().multiply(0.3));
                sectColumnAut.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Notification, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Notification, String> p) {
                            if(dao.getUser().get(p.getValue().getValue().getIdEmet())!=null){
                                User user = (User) dao.getUser().get(p.getValue().getValue().getIdEmet());
                                return new ReadOnlyStringWrapper(user.getNom()+" "+user.getPrenom());
                            }else{
                                return new ReadOnlyStringWrapper("");
                            }
                        
                    }
                });
                //--objet
                sectColumnObjet.prefWidthProperty().bind(treeTableViewDate.widthProperty().multiply(0.4));
                sectColumnObjet.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Notification, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Notification, String> p) {
                        
                            return new ReadOnlyStringWrapper(p.getValue().getValue().getObjet());
                            
                    }
                });
                
                
                
                
                ScrollPane spDate = new ScrollPane();
                
                spDate.setPrefSize(AUTO,AUTO);
                spDate.setContent(hbDate);
                spDate.setPadding(new Insets(10, 10, 10, 10));
                //spDate.setMinWidth((dao.width*85)/100);
                spDate.setMaxWidth((scene.getWidth()*98)/100);
                
                Button btn = new Button("Notifications Envoyer("+dao.getNotificationEmis().size()+")");
                btn.getStyleClass().add(dao.boutonColor);
                
                Button btn2 = new Button("Notifications Réçues("+dao.getNotificationRecus().size()+")");
                btn2.getStyleClass().add(dao.boutonColor);
                btn2.setDisable(true);
                
                
                
                Label dateL = new Label("Reçus");
                btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        Connecter c=null;
                               try {
                                   c = new Connecter();
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalNotificationTable.class.getName()).log(Level.SEVERE, null, ex);
                               } catch (SQLException ex) {
                                   new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                               }
                               Requettes con = new Requettes(c);
                               
                               try { 
                                   con.fDeletenotification(myNotif);
                                   c.getConnexion().close();
                                   // suppression de la notification
                                   Hashtable notifications =null;
                                   Button tempBtn = null;
                                    if(notifrecu){       
                                        notifications = dao.getNotificationRecus();
                                        tempBtn = btn2;
                                    }else{
                                        notifications = dao.getNotificationEmis();
                                        tempBtn = btn;
                                    }       
                                    notifications.remove(myNotif.getId());
                                    Enumeration e =  notifications.elements();
                                    rootDate.getChildren().clear();
                                    tempBtn.setText("Notifications Réçues("+notifications.size()+")");
                                    while (e.hasMoreElements()) {
                                           Notification s = (Notification) e.nextElement();
                                           TreeItem<Notification>root =  new TreeItem<>(s);
                                           rootDate.getChildren().add(root);
                                    }
                                    treeTableViewDate.setRoot(rootDate);
                                    new Notifier("Gestab","Notification supprimer",2,dao);
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddDebit.class.getName()).log(Level.SEVERE, null, ex);
                                   new Notifier("Gestab","echec de suppresion de la Notification \n"+ex,3,dao);
                               }
                        
                    }
                });
                btnVue.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        Connecter c=null;
                               try {
                                   c = new Connecter();
                               } catch (ClassNotFoundException ex) {
                                   Logger.getLogger(ModalNotificationTable.class.getName()).log(Level.SEVERE, null, ex);
                               } catch (SQLException ex) {
                                   new Notifier("Gestab","Le réseau n'est pas accessible:"+ex,3,dao);
                               }
                               Requettes con = new Requettes(c);
                               
                               try {
                                   myNotif.setVue(Boolean.TRUE);
                                   con.fUpdateNotificationVue(myNotif);
                                   c.getConnexion().close();
                                   // suppression de la notification
                                   Hashtable notifications = null;
                                   if(notifrecu){       
                                        notifications = dao.getNotificationRecus();
                                    }else{
                                        notifications = dao.getNotificationEmis();
                                    }
                                    notifications.replace(myNotif.getId(),myNotif);
                                    Enumeration e =  notifications.elements();
                                    rootDate.getChildren().clear();
                                    while (e.hasMoreElements()) {
                                           Notification s = (Notification) e.nextElement();
                                           TreeItem<Notification>root =  new TreeItem<>(s);
                                           rootDate.getChildren().add(root);
                                    }
                                    treeTableViewDate.setRoot(rootDate);
                                    //new Notifier("Gestab","Notification supprimer",2,dao);
                               } catch (SQLException ex) {
                                   Logger.getLogger(ModalAddDebit.class.getName()).log(Level.SEVERE, null, ex);
                                   new Notifier("Gestab","echec de mise à jour de la Notification \n"+ex,3,dao);
                               } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ModalNotificationTable.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        notifrecu = false;
                        Hashtable notifications = dao.getNotificationEmis();
                        Enumeration e =  notifications.elements();
                        rootDate.getChildren().clear();
                        while (e.hasMoreElements()) {
                               Notification s = (Notification) e.nextElement();
                               TreeItem<Notification>root =  new TreeItem<>(s);
                               rootDate.getChildren().add(root);
                        }
                        treeTableViewDate.setRoot(rootDate);
                        btnVue.setVisible(false);
                        btn.setDisable(true);
                        btn2.setDisable(false);
                        dateL.setText("Envoyés");
                    }
                });
                btn2.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        notifrecu = true;
                        Hashtable notifications = dao.getNotificationRecus();
                        Enumeration e =  notifications.elements();
                        rootDate.getChildren().clear();
                        while (e.hasMoreElements()) {
                               Notification s = (Notification) e.nextElement();
                               TreeItem<Notification>root =  new TreeItem<>(s);
                               rootDate.getChildren().add(root);
                        }
                        treeTableViewDate.setRoot(rootDate);
                        btn2.setDisable(true);
                        btn.setDisable(false);
                        btnVue.setVisible(true);
                        dateL.setText("Reçus");
                    }
                });
                
                 //Add change listener
                treeTableViewDate.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                    
                    //System.out.println(treeTableViewDate.getSelectionModel().getSelectedItem().getValue().getObjet());
                    if(treeTableViewDate.getSelectionModel().getSelectedItem()!=null){
                        myNotif = treeTableViewDate.getSelectionModel().getSelectedItem().getValue();
                        textA.setText(treeTableViewDate.getSelectionModel().getSelectedItem().getValue().getMessage());
                        vbDate.setVisible(true);
                    }else{
                        vbDate.setVisible(false);
                    }
                    
                });
                
                
                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().addAll(btn,btn2);
                
                //conteneur vertical principal
                
                ImageView depIcon2 = new ImageView (
                    new Image(getClass().getResourceAsStream("/images/usersmall.png"))
                );
                dateL.setGraphic(depIcon2);
                dateL.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                dateL.setTextFill(Color.web(dao.couleur));
                
                Vlist.getChildren().addAll(dateL,hbBtn,spDate);
                Vlist.setMinWidth(800);
                Vlist.setMaxWidth((scene.getWidth()*98)/100);



                
                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(Vlist);
                sp.setPadding(new Insets(10, 10, 10, 10));
                
                
                scene.getStylesheets().add("css/style.css");
                stage.setScene(scene);
                stage.setTitle("Notifications");
                //stage.initModality(Modality.NONE);
                //stage.initOwner(
                //    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
}
