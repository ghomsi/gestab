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

/**
 *
 * @author ghomsi
 */
import com.client.login.MainLauncher;
import controleur.DAOequipement;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
//import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import notifier.Notifier;

public class ModalEmail{

    static Session sesh;
    static Properties prop = new Properties();

    static Button btnLOG, btnSEND, btnBACK;
    static Label lblUN, lblPW, lblTO, lblSUB, lblTEXT, lblHEAD, lblHOST, lblTYPE;
    static Scene log, edit, sent;
    static Text UNfail, tSUC, tSENT;
    static TextField tUN, tto, tsub, thead;
    static TextArea ttext;
    static PasswordField tPW;
    static ComboBox<String> cmbHOST, cmbTYPE;
    static String UN, PW, host, port, mto, mhead, msub, cTEXT, cTYPE;
    static GridPane layLOG, layEDIT;
    static VBox laySENT;
    static Stage win;
    private ActionEvent event;
    private DAOequipement dao;
    private String email;
    private double xOffset;
    private double yOffset;

    /*public static void main(String args[]) {
        launch(args);
    }*/

    //@SuppressWarnings("unused")
    public ModalEmail(DAOequipement dao,ActionEvent event,String... email){
        this.event = event;
        this.dao = dao;
        if(email.length>0){
            this.email = email[0];
        }else{
            this.email= null;
        }
    }
    
    public void contruire(){
        win = new Stage();

        

        //

        layLOG = new GridPane();
        layLOG.setAlignment(Pos.CENTER);
        layLOG.setHgap(30);
        layLOG.setVgap(15);
        layLOG.setPadding(new Insets(20, 10, 20, 10));
        layLOG.setStyle("-fx-font-size: 13px;"
                    + "-fx-background-radius: 2, 29;");
        /*layLOG.setStyle("-fx-font-size: 13px; -fx-background-color: linear-gradient(#ff2, #e5bb00), linear-gradient(#e43, #f84);"
                    + "-fx-background-radius: 2, 29;");*/
        //
        layEDIT = new GridPane();
        layEDIT.setAlignment(Pos.CENTER_LEFT);
        layEDIT.setHgap(10);
        layEDIT.setVgap(25);
        layEDIT.setPadding(new Insets(10, 15, 20, 10));
        layEDIT.setStyle("-fx-font-size: 13px;"
                + "-fx-background-radius: 2, 29;");
        /*layEDIT.setStyle("-fx-font-size: 13px; -fx-background-color: linear-gradient(#ff2, #e5bb00), linear-gradient(#f53, #f86);"
                + "-fx-background-radius: 2, 29;");*/
        //
        laySENT = new VBox(21);
        laySENT.setAlignment(Pos.CENTER);
        laySENT.setStyle("-fx-background-color: linear-gradient(#ff2, #e5bb00), linear-gradient(#f53, #f86);"
                + "-fx-background-radius: 2, 29;");
        //
        // layLOG layout //
        ObservableList<String> hostOps = 
                FXCollections.observableArrayList(
                    "smtp.gmail.com",
                    "smtp.mail.yahoo.com"
                );
        cmbHOST = new ComboBox<String>(hostOps);
        cmbHOST.setValue(" ");
        layLOG.add(cmbHOST, 1, 0);
        //
        lblHOST = new Label("SMTP Server: ");
        layLOG.add(lblHOST, 0, 0);
        lblUN = new Label("Username/email: ");
        layLOG.add(lblUN, 0, 1);
        lblPW = new Label("Password: ");
        layLOG.add(lblPW, 0, 2);
        //
        tUN = new TextField();
        if(!dao.user.getEmail().isEmpty()){
            tUN.setText(dao.user.getEmail());
        }
        tPW = new PasswordField();
        layLOG.add(tUN, 1, 1);
        layLOG.add(tPW, 1, 2);
        UNfail = new Text("Cannot authenticate");
        //
        btnLOG = new Button();
        btnLOG.setText("Verify");
        btnLOG.setOnAction(e -> {
            UN = tUN.getText();
            PW = tPW.getText();
            host = cmbHOST.getValue();
            if (host.equals("smtp.gmail.com")) {
                port = "587";
            } else if (host.equals("smtp.mail.yahoo.com")) {
                port = "465";
            } else {
                port = "80";
            }
            if(layLOG.getChildren().contains(UNfail)) {
                System.out.print("y");
                layLOG.getChildren().remove(UNfail);
            }
            auth();
        });
        layLOG.add(btnLOG, 1, 3);
        //
        // layEDIT layout //
        ObservableList<String> typeOps = FXCollections.observableArrayList(
                "text/plain",
                "text/html"
                );
        cmbTYPE = new ComboBox<String>(typeOps);
        lblTO = new Label("To: ");
        lblHEAD = new Label("Heading: ");
        lblSUB = new Label("Message subject: ");
        lblTEXT = new Label(" \u0020 \u0020 \u0020\t # # # # # \t > \t # \t Body \t # \t < \t # # # # #");
        lblTYPE = new Label("Message type: ");
        tto = new TextField();
        if(email!=null){
            tto.setText(email);
            tto.setDisable(true);
        }
        thead = new TextField();
        tsub = new TextField();
        ttext = new TextArea();
        tto.setPrefWidth(212);
        thead.setPrefWidth(212);
        tsub.setPrefWidth(212);
        ttext.setPrefSize(380, 510);
        ttext.setWrapText(true);
        layEDIT.add(lblTO, 0, 1);
        layEDIT.add(lblHEAD, 0, 2);
        layEDIT.add(lblSUB, 0, 3);
        layEDIT.add(lblTEXT, 0, 4);
        layEDIT.add(tto, 1, 1);
        layEDIT.add(thead, 1, 2);
        layEDIT.add(tsub, 1, 3);
        layEDIT.add(ttext, 0, 5);
        layEDIT.add(lblTYPE, 0, 0);
        layEDIT.add(cmbTYPE, 1, 0);
        cmbTYPE.setValue("text/plain");
        cmbTYPE.setPrefWidth(212);
        //
//  ##  //  ##  //
    //  ##  //  ##  //
        //  ##  //  ##  //
        //
        btnSEND = new Button();
        btnSEND.setText("Send eMail");
        btnSEND.setOnAction(e -> {
            cTYPE = cmbTYPE.getValue();
            mto = tto.getText();
            mhead = thead.getText();
            msub = tsub.getText();
            cTEXT = ttext.getText();
            Mail(mto, msub, cTEXT);
            if(!mto.isEmpty() || !cTEXT.isEmpty() || !msub.isEmpty()){
                win.setScene(sent);
            }
        });
        VBox vb = new VBox();
        vb.setAlignment(Pos.BOTTOM_CENTER);
        vb.getChildren().add(btnSEND);
        layEDIT.add(vb, 1, 5);

        //
        // laySENT layout //
        tSUC = new Text("Message mailed to recipient(s).");
        tSENT = new Text("Don't forget to check your inbox regarding \n send errors and replies.");
        tSUC.setStyle("-fx-font-size: 32px;");
        tSUC.setTextAlignment(TextAlignment.CENTER);
        tSUC.setFill(Color.web("#2b3"));
        tSENT.setStyle("-fx-font-size: 10px;");
        //tSENT.setFill(Color.web("#676767"));
        tSENT.setTextAlignment(TextAlignment.CENTER);

        tSUC.setLayoutX(80);

        btnBACK = new Button();
        btnBACK.setText("Return to Editor");
        btnBACK.setOnAction(e -> {
            win.setScene(edit);
            tto.setText(mto);
            ttext.setText(cTEXT);
            tsub.setText(msub);
            thead.setText(mhead);
        });

        laySENT.getChildren().addAll(tSUC, tSENT, btnBACK);

        //
        // Scenes and Stages //
        //

        edit = new Scene(layEDIT, 640, 710);
        BorderPane borderPane = new BorderPane();
        //borderPane.getChildren().add(layLOG);
        borderPane.prefHeight(210);
        borderPane.prefWidth(480);
        borderPane.setCenter(layLOG);
        log = new Scene(borderPane, 480, 210);
        
        sent = new Scene(laySENT, 500, 260);
        //
        win.setX(150);
        win.setY(200);
        win.setScene(log);
        //win.setOnCloseRequest(e -> Platform.exit());
        win.setTitle("SendMail \u0020\u0020 | | \u0020\u0020 Gestab");
        win.setResizable(false);
        win.centerOnScreen();
        /* Drag and Drop */
        
        
        int numberOfSquares = 30;
        while (numberOfSquares > 0){
            dao.generateAnimation(borderPane,dao);
            numberOfSquares--;
        }

        win.initModality(Modality.APPLICATION_MODAL);
        win.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
        win.show();

    }

    public static void auth() {
        boolean auth = chk(UN, PW);
        if(!auth) {
            System.out.print("Not auth");
            layLOG.add(UNfail, 3, 1);
            tUN.clear();
            tPW.clear();
        } else if (auth) {
            System.out.print("Auth");
            win.setScene(edit);
        } else {
            System.out.print("Not auth");
            layLOG.add(UNfail, 3, 1);
            cmbHOST.setValue(" ");
            tUN.clear();
            tPW.clear();
        }



    } // end auth() //

    public static boolean chk(String UN, String PW) /*throws AuthenticationFailedException /*AddressException*/ {

        prop.put("mail.smtp.auth", "true");
        if(host.equals("smtp.gmail.com") || host.equals("smtp.mail.yahoo.com")){
            prop.put("mail.smtp.starttls.enable", "true");
        }
        prop.put("mail.smtp.host", host/*[0]*/);
        prop.put("mail.smtp.port", port);
        if(host.equals("smtp.mail.yahoo.com")) { prop.put("mail.smtp.ssl.enable", "true"); }

        boolean check = true;
        //
        try {
            InternetAddress e = new InternetAddress(UN);
            e.validate();
        } catch (AddressException e) {
            e.getStackTrace();
            check = false;
        }

        if(check) {
            sesh = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(UN, PW);
                    }
                });
        }

        return check;
    } // end chk() //

    public static void Mail(String to, String sub, String cont) /*throws IOException*/ {

        try {

            System.out.println("\n \n>> ?" + mto);
            System.out.println("\n \n>> ?" + to);
            Message m = new MimeMessage(sesh);
            m.setFrom(new InternetAddress(UN));
            m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            m.setSubject(sub);
            m.setSentDate(new Date());
            m.setContent(cont, cTYPE);
            m.setHeader("EMAIL HEAD", mhead);
            System.out.println("\n \n \n \t >> ??????? " + m.getContentType());
            System.out.println("\n \n \n \t >> ??????? " + m.getDataHandler());
            System.out.println("\n \n \n \t >> ??????? " + m.getSubject());

            Transport t;
            if(host.equals("smtp.mail.yahoo.com"))
                t = sesh.getTransport("smtps");
            else 
                t = sesh.getTransport("smtp");
             //
            System.out.println(">> ? smtp(s) ---> ## " + t.getURLName() + " \n>> ?");

            Transport.send(m);
            
            new Notifier("Gestab Notification","Mail envoyé à\n"+tto.getText(),2);


        } catch (MessagingException e) {
            new Notifier("Gestab exception","Mail non envoyé à\n"+tto.getText()+"\n"+e,3);
            throw new RuntimeException(e);
            
        } // end try/catch //

        // });

        } // end Mail() //

} // end Send Client program //
