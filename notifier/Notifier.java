/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifier;

import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import static javafx.scene.layout.BackgroundSize.AUTO;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import vue.ModalPaneDeleteSecteur;
import vue.ModalPaneDeleteVille;

/**
 *
 * @author ghomsi
 */
public class Notifier{
    
    public Notifier(String pTitle, String pMessage,int i,DAOequipement... dao){
        Platform.runLater(() -> {
                    /*Stage owner = new Stage(StageStyle.TRANSPARENT);
                    StackPane root = new StackPane();
                    root.setStyle("-fx-background-color: TRANSPARENT");
                    Scene scene = new Scene(root, 1, 1);
                    scene.setFill(Color.TRANSPARENT);
                    owner.setScene(scene);
                    owner.setWidth(1);
                    owner.setHeight(1);
                    owner.toBack();
                    owner.show();*/
                    if(i==1){
                        Notifications.create().title(pTitle).text(pMessage).showConfirm();
                    }else if(i==2){
                        Notifications.create().title(pTitle).text(pMessage).showInformation();
                    }else if(i==3){
                        Notifications.create().title(pTitle).text(pMessage).showError();
                        Stage stage = new Stage();
                        ///Creation des Variales
                        Label lajout = new Label(pTitle);
                        lajout.setFont(Font.font("Times New ROmman", FontWeight.BOLD, 15));
                        lajout.setTextFill(Color.RED);
                        lajout.setAlignment(Pos.BOTTOM_LEFT);
                        // --- GridPane container
                        GridPane grid = new GridPane();
                        grid.setVgap(10);
                        grid.setHgap(10);
                        grid.setPadding(new Insets(5, 5, 5, 5)); 





                        TextArea cssEditorFld = new TextArea();
                        cssEditorFld.setPrefRowCount(10);
                        cssEditorFld.setPrefColumnCount(200);
                        cssEditorFld.setWrapText(true);
                        cssEditorFld.setPrefWidth(350);
                        cssEditorFld.setText(pMessage);
                        cssEditorFld.setEditable(false);



                        Button bannul=new Button();
                        bannul.setText("Fermer");
                        //bannul.getStyleClass().add(dao.boutonColor);

                        bannul.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent t) {
                                    stage.close();
                                }
                        });
                        HBox hbBtn = new HBox(10);
                        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                        hbBtn.getChildren().addAll(bannul);
                        grid.add(hbBtn, 0, 0);


                        VBox VbBtn = new VBox(10);
                        VbBtn.setAlignment(Pos.BOTTOM_RIGHT);

                        HBox Lb = new HBox(10);
                        Lb.setAlignment(Pos.BOTTOM_RIGHT);
                        Lb.getChildren().addAll(cssEditorFld);

                        VbBtn.getChildren().addAll(lajout,Lb,grid);



                        ScrollPane sp = new ScrollPane();

                        sp.setPrefSize(AUTO,AUTO);
                        sp.setContent(VbBtn);
                        sp.setPadding(new Insets(10, 10, 10, 10));
                        Scene scene = null;
                        if(dao.length>0){
                            scene = new Scene(sp, (dao[0].width*50)/100, (dao[0].height*36)/100);
                        }else{
                            scene = new Scene(sp, 500, 300);
                        }
                        scene.getStylesheets().add("css/style.css");
                        stage.setScene(scene);
                        stage.setTitle("Gestab Notification");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        /*stage.initOwner(
                            ((Node)event.getSource()).getScene().getWindow() );*/
                        stage.show();
                    }else{
                        Notifications.create().title(pTitle).text(pMessage).show();
                    }
                    
                    
                    
                }
        );
    }

   
    
}
