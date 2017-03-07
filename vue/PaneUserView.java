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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeTableView;
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
import modele.User;
import modele.Ville;

/**
 *
 * @author ghomsi
 */
public class PaneUserView {
    
    private VBox pane,paneR;
    private Image images;
    private ImageView pics;
    private TitledPane tps;
    private final User user;
    private final Label labelT;
    private final GridPane gridR;
    private final TreeTableView<User> treeTableView;
    private DAOequipement dao;
    
      
    
    
    

    public PaneUserView(DAOequipement dao,User user,Label label,GridPane grid,VBox pane) {
        this.tps = new TitledPane();
        this.pics = new ImageView();
        this.images = new Image(getClass().getResourceAsStream("/images/user.png"));
        this.user = user;
        this.labelT = label;
        this.gridR = grid;
        this.treeTableView = dao.getTreeTU();
        this.paneR = pane;
        this.dao = dao;
    }
    
    public VBox construire(){
        pane=new VBox();
        pane.minHeightProperty().set(dao.getHeigth());
        
        // --- GridPane container
        GridPane grid = new GridPane();
        grid.getStyleClass().add("gridCarte");
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("Description: "), 0, 0);
        
        final TextArea textArea = new TextArea(user.getDesc());
        textArea.getStyleClass().add("textArea");
        textArea.setEditable(false);
        textArea.prefWidthProperty().set(150);
        textArea.prefHeightProperty().set(50);
        
        Label attach = new Label("ᚂ Attachement: ");
        attach.setTextFill(Color.web(dao.colordeAdminU()));
        attach.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        grid.add(attach, 0, 3);
        
        Ville ville = (Ville) dao.getVille().get(user.getVille());
        final Label label = new Label(ville.getNom());
        
        grid.add(new Label("Ville: "), 0, 4);
        grid.add(new Label("Quartier: "), 0, 5);
        final Label label1 = new Label(user.getQuartier());
        
        //grid.add(new Label("Carte: "), 0, 6);
        //final Label label2 = new Label("N/A");
        
        //grid.add(new Label("Port: "), 0, 7);
        //final Label label3 = new Label("N/A");
        
        
        
        grid.add(new Label("Numero Telephone: "), 3, 1);
        final Label label6 = new Label(user.getNumTel());
        label6.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        
        grid.add(new Label("Email: "), 3, 2);
        final Label label7 = new Label(user.getEmail());
        label7.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        
        grid.add(new Label("Type: "), 3, 3);
        final Label label8 = new Label(user.getCategorie());
        label8.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 12));
        
        //grid.add(new Label("Statut: "), 3, 4);
        //final Label label9 = new Label("N/A");
        
        //grid.add(new Label("Date de Creation: "), 3, 5);
        //final Label label10 = new Label("N/A");
        
        //grid.add(new Label("Ville de résidance: "), 3, 6);
        //final Label label11 = new Label("N/A");
        
        
        
        grid.add(label,1, 4);
        grid.add(label1,1, 5);
        //grid.add(label2,1, 6);
        //grid.add(label3,1, 7);
        grid.add(textArea,1, 0);
        grid.add(label6,4, 1);
        grid.add(label7,4, 2);
        grid.add(label8,4, 3);
        //grid.add(label9,4, 4);
        //grid.add(label10,4, 5);
        //grid.add(label11,4, 6);
        
        
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(20, 0, 0, 20));
        
        Button btn = new Button("Supprimer");
        btn.getStyleClass().add("buttonAdminU");
        Button btn2 = new Button("Modifier");
        btn2.getStyleClass().add("buttonAdminU");

            btn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    System.out.println("Supprimer");
                    ModalPaneDeleteUser modalUser = new ModalPaneDeleteUser(dao,t,user,hbox,labelT,gridR,paneR);
                    modalUser.construire();
                }
            });

            btn2.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    System.out.println("Modifier");
                    ModalPaneModifierUser modalUser = new ModalPaneModifierUser(t,user,dao);
                    try {
                        modalUser.construire();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(PaneUserView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(PaneUserView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });


        images = new Image(getClass().getResourceAsStream("/images/user.png"),200,200,false,false);
        pics = new ImageView(images);
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btn,btn2);
        grid.add(hbBtn, 0, 9);
        grid.add(pics,3,0);
        
        
        
        
        
        ScrollPane sp = new ScrollPane();
        
        sp.setPrefSize(AUTO,AUTO);
        sp.setContent(grid);
        hbox.getChildren().setAll(sp);
        
        
        
         
        
        pane.getChildren().addAll(hbox);
        
        
        return pane;
    }
}
