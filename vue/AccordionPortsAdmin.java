/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import modele.Carte;
import modele.Port;

/**
 *
 * @author ghomsi
 */
public class AccordionPortsAdmin {

    HBox hb;
    Image images;
    ImageView pics;
    Carte carte;
    private DAOequipement dao;

    AccordionPortsAdmin(HBox hb, Carte carte, DAOequipement dao) {
        this.hb = hb;
        this.images = new Image(getClass().getResourceAsStream("/images/user.png"));
        this.pics = new ImageView();
        this.carte = carte;
        this.dao = dao;

    }

    Accordion construire() {
        final Accordion accordion = new Accordion();
        
        ArrayList ports = (ArrayList) dao.getPortCarte().get(carte.getId());

        //---- combo Box cartes
        ObservableList<Port> options = FXCollections.observableArrayList();
        if (ports != null) {
            if (!ports.isEmpty()) {
                options.add(new Port(-1));
            }
            Iterator<Integer> it_port = ports.iterator();

            while (it_port.hasNext()) {
                int id_port = it_port.next();
                Port elt = (Port) dao.getPort().get(id_port);
         
                //combo box port
                options.add(elt);

            }
        }
        
        Button expandBtn = new Button("V");
        expandBtn.getStyleClass().add("buttonAdminEq");
        
        final ComboBox comboBox = dao.comboBoxPort(options, ports);
        comboBox.valueProperty().addListener(new ChangeListener<Port>() {
            @Override
            public void changed(ObservableValue ov, Port t, Port t1) {

                expandBtn.setDisable(false);
                accordion.getPanes().clear();
                if(t1.getNumeroPort()>=0){
                    accordion.getPanes().addAll(dao.portTitledPaneAdmin(dao, t1));
                }else{
                    Iterator<Integer> it_port = ports.iterator();
                    while(it_port.hasNext()) {
                        int id_port = it_port.next();
                        Port elt = (Port) dao.getPort().get(id_port);
                        if(elt!=null)
                            accordion.getPanes().addAll(dao.portTitledPaneAdmin(dao, elt));
                    }
                    
                }
            }
        });

        expandBtn.setDisable(true);
        expandBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                accordion.getPanes().stream().forEach((Consumer<? super TitledPane>) (elt) -> {
                    elt.setVisible(false);
                });
                expandBtn.setDisable(true);
            }
        });

        hb.getChildren().addAll(comboBox, expandBtn);

        return accordion;

    }
}
