/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Carte;

/**
 *
 * @author ghomsi
 */
public class ModalPaneModifierCarte {
    
    private ActionEvent event;
    private ObservableList<String> options;
    private Carte carte;
    private Label label;
    private DAOequipement dao;
    
    public ModalPaneModifierCarte(ActionEvent event,Carte carte,DAOequipement dao){
        this.event = event;
        this.carte = carte;
        this.dao = dao;
    }
    
    void construire(){
    
        Stage stage = new Stage();
        ///Creation des Variales
        Label lajout = new Label("MODIFIER LA CARTE");
        lajout.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 10));
        lajout.setTextFill(Color.BLACK);
        lajout.setAlignment(Pos.CENTER);
        Label lajoutcarte=new Label("Numero Carte :");
        Label lajoutport =new Label("Nombre de Port :");
        
        TextField tajoutcarte =new TextField();
        tajoutcarte.appendText(""+carte.getNumCarte());
        tajoutcarte.setEditable(false);
        
        TextField tajoutport = new TextField();
        tajoutport.appendText(""+carte.getNbPort());
        
        Label lbMessage = new Label("");
        lbMessage.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
        Label lbMessage2 = new Label("");
        
        Button bvalid = new Button();
        bvalid.setText("Modifier");
        bvalid.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                   
                   System.out.println("modifier"); 
                    
                }
        });
        
        Button bannul=new Button();
        bannul.setText("Annuler");
        
        bannul.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    stage.close();
                }
        });
        
        GridPane root = new GridPane();
        HBox rajout= new HBox();
       
        root.setPadding(new Insets(10,10,10,10));
        root.setGridLinesVisible(false);
        root.setHgap(10);
        root.setVgap(5);
        root.setAlignment(Pos.CENTER);
        root.add(lajout,0,0);
        
        root.add(lajoutcarte,0,3);
        root.add(tajoutcarte,1,3);
        root.add(lajoutport,0,5);
        root.add(tajoutport,1,5);
        
        
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(bannul,bvalid);
        root.add(hbBtn, 1, 7);

        root.add(lbMessage,1,8);
        //FlowPane pane = new FlowPane();
        
        VBox VbBtn = new VBox(10);
        VbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        
        HBox Lb = new HBox(10);
        Lb.setAlignment(Pos.BOTTOM_RIGHT);
        Lb.getChildren().addAll(lbMessage,lbMessage2);
        
        VbBtn.getChildren().addAll(root,Lb);
        
        //pane.getChildren().addAll(root,lbMessage);

              
                
                ScrollPane sp = new ScrollPane();
        
                sp.setPrefSize(AUTO,AUTO);
                sp.setContent(VbBtn);
                sp.setPadding(new Insets(10, 10, 10, 10));
                
                Scene scene = new Scene(sp, (dao.width*40)/100, (dao.height*55)/100);
                stage.setScene(scene);
                stage.setTitle("Modifier une carte");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
    }
    
    
    void ajouterCarte(VBox Vbox,Carte carte,Label titleLabel){
        
        TitledPane gridTitlePane = new TitledPane();
        Label label = new Label("N/A");
        gridTitlePane.expandedProperty().setValue(false);
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setPadding(new Insets(5, 5, 5, 5));

        grid.add(new Label("Nom: "), 0, 0);
        Label nomV = new Label("carte "+carte.getNumCarte());
        grid.add(nomV, 1, 0);

        grid.add(new Label("Description: "), 0, 1);
        Label descV = new Label(carte.getDesc());
        grid.add(descV, 1, 1);

        grid.add(new Label("Attachment: "), 0, 3);

        grid.add(new Label("Nombre de Port: "), 0, 4);
        HBox hb = new HBox(10);
        Label portV = new Label(""+carte.getNbPort());
        hb.setAlignment(Pos.BOTTOM_RIGHT);
        hb.getChildren().add(portV);

        grid.add(hb, 1, 4);

        grid.add(new Label("Ports libres: "), 0, 5);
        Label portLV = new Label(""+carte.getNbPortLibre());
        grid.add(portLV, 1, 5);

        grid.add(new Label("Ports occupés: "), 0, 6);
        Label portLO = new Label(""+carte.getNbPortOccupe());
        grid.add(portLO, 1, 6);

        grid.add(new Label("Ports desactivés/défecteux: "), 0, 7);
        Label portLD = new Label(""+carte.getNbPortDesactive());
        grid.add(portLD, 1, 7);

        final Label labl = new Label("N/A");
        grid.add(labl,1, 3);
        gridTitlePane.setText("Cartes i");
        gridTitlePane.setContent(grid);
            // --- Accordion
            final Accordion accordion = new AccordionPortsAdmin (hb,carte,dao).construire();
            ScrollPane sp = new ScrollPane();

            sp.setPrefSize(AUTO,AUTO);
            sp.setContent(accordion);

            accordion.expandedPaneProperty().addListener(
                (ObservableValue<? extends TitledPane> ov, TitledPane old_val, 
                TitledPane new_val) -> {
                    if (new_val != null) {
                        label.setText(accordion.getExpandedPane().getText() +" i");
                    }
            });
            
            HBox hbox = new HBox(10);
            hbox.setPadding(new Insets(20, 0, 0, 20));
            hbox.getChildren().addAll(gridTitlePane, sp);
            
            Vbox.getChildren().addAll(hbox);
            int a = new Integer(titleLabel.getText())+1;
            titleLabel.setText(""+a);
    }
    
}
