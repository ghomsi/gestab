/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.DAOequipement;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import jcterm.ConfigurationRepositoryFS;
import jcterm.JCTermSwing;
import jcterm.JCTermSwingFrame;
import jcterm.JSchSession;
import jssh.SSHMultiCommands;
import jssh.Shell;
import notifier.Notifier;
import telnet.ModalTelnet;

/**
 *
 * @author ghomsi
 */
public class PaneSSH {
    
    VBox pane,paneDesc;
    Button shellBtn,execBtn,termBtn,telnetBtn;
    Label lblscene,lblscene2;
    private final DAOequipement dao;
    
   
    

    PaneSSH(DAOequipement dao) {
        this.dao = dao;
    }
      
    
    
    VBox construire() throws ClassNotFoundException, SQLException{
        
        pane=new VBox();
        lblscene = new Label();
        lblscene.setTextFill(Color.web(dao.colordeAdminU()));
        lblscene.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        
        pane.minHeightProperty().set(dao.getHeigth());
        pane.setPadding(new Insets(5, 5, 10, 10));
        VBox vb = new VBox(5);
        HBox hb = new HBox(10);
        
        
        ImageView depIcon = new ImageView (
            new Image(getClass().getResourceAsStream("/images/shell.png"))
        );
        depIcon.preserveRatioProperty();
        vb.getChildren().addAll(lblscene,hb,depIcon);
        
        
        //----- Button Ajouter un Secteur
        
        execBtn = new Button("Exec");
        execBtn.getStyleClass().add("buttonAdminU");
        final Tooltip tooltipExec = new Tooltip();
        tooltipExec.setText("Exécuter une commende SSH");
        execBtn.setTooltip(tooltipExec);
        execBtn.setPrefWidth(250);
        
        execBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                try {
                    //Exec myprompt = new Exec(dao,lblscene);
                    SSHMultiCommands myprompt = new SSHMultiCommands(dao,lblscene);
                } catch (IOException ex) {
                    lblscene.setText(ex.toString());
                    Logger.getLogger(PaneSSH.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    lblscene.setText(ex.toString());
                    Logger.getLogger(PaneSSH.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        });
        
        shellBtn = new Button("Shell");
        shellBtn.getStyleClass().add("buttonAdminU");
        final Tooltip tooltipShell = new Tooltip();
        tooltipShell.setText("Exécuter une commende SSH");
        shellBtn.setTooltip(tooltipShell);
        shellBtn.setPrefWidth(250);
        
        shellBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Shell myshell = new Shell(dao,lblscene);
            }
        
        });
        
        termBtn = new Button("Terminal");
        termBtn.getStyleClass().add("buttonAdminU");
        final Tooltip tooltiptermBtn = new Tooltip();
        tooltiptermBtn.setText("Exécuter une commende SSH");
        termBtn.setTooltip(tooltipShell);
        termBtn.setPrefWidth(250);
        
        termBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                /*ModalTermin term = new ModalTermin(event,dao);
                try {
                    term.construire();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PaneSSH.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PaneSSH.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(PaneSSH.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                new Notifier("GESTAB!", "Gesterm lancé!",1,dao);
                JCTermSwing.setCR(new ConfigurationRepositoryFS());

                String s = System.getProperty("jcterm.config.use_ssh_agent");
                if(s != null && s.equals("true"))
                  JSchSession.useSSHAgent(true);

                final JCTermSwingFrame frame=new JCTermSwingFrame("GESTerm");
                frame.setCloseOnExit(false);
                frame.setVisible(true);
                frame.setResizable(true);
            }
        
        });
        
        telnetBtn = new Button("Telnet");
        telnetBtn.getStyleClass().add("buttonAdminU");
        final Tooltip tooltipTelnet = new Tooltip();
        tooltipTelnet.setText("Exécuter une commende Telnet");
        telnetBtn.setTooltip(tooltipTelnet);
        telnetBtn.setPrefWidth(250);
        
        telnetBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                /*ModalTelnet modal = new ModalTelnet(event,dao,"adrien","adrien2016");
                modal.construire();*/
            }
        
        });
        
        //hb.getChildren().addAll(shellBtn,execBtn,termBtn,telnetBtn);
        hb.getChildren().addAll(termBtn);
        
        pane.getChildren().addAll(vb);
        pane.autosize();
        return pane;
    }
}
