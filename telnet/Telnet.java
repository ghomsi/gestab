/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telnet;

/**
 *
 * @author ghomsi
 */
import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import modele.Client;
import modele.Historique;
import notifier.Notifier;
import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SimpleOptionHandler;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetNotificationHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
import vue.ModalClientNotifier;
// for Speedway R100
public class Telnet extends TelnetTerminal implements Runnable, TelnetNotificationHandler {

    
   

    static TelnetClient tc = null;
    private static PrintStream out;
    private final int defaultTelnetPort = 23;
    private static String server;
    private final String ctrlC = "\003";
    private DAOequipement dao;
    String  text;
    public Boolean isconnected = false;
    private int i;
    public String line ="";
    private Stage stage;
     Client client;

    private char prompt = '#';
    public Telnet (DAOequipement fdao,Stage stag,String server,PrintStream out,int i,Client... client)
    {
        this.out = out;
        this.server = server;
        this.dao = fdao;
        this.i = i;
        this.stage = stag;
        if(client.length>0){
            
            this.client = client[0];
            System.out.println("client ok:"+this.client.getNumTel());
        }else{
            this.client = null;
        }
    }
    
    public void execute() throws IOException {
        
        FileOutputStream fout = null;

        /*if(args.length < 1)
        {
            System.err.println("Usage: TelnetClientExample <remote-ip> [<remote-port>]");
            System.exit(1);
        }*/

        String remoteip = server;
        
        

        int remoteport;
        remoteport = 23;
        

        try
        {
            fout = new FileOutputStream ("gestab.log", true);
        }
        catch (IOException e)
        {
            System.err.println(
                "Exception while opening the gestablog file: "
                + e.getMessage());
            new Notifier("Gestab exception","Telnet:Exception while opening the gestablog file: "+e.getMessage(),3,dao);
        }

        tc = new TelnetClient();
        /*
        TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);
        EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);
        SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);

        try
        {
            tc.addOptionHandler(ttopt);
            tc.addOptionHandler(echoopt);
            tc.addOptionHandler(gaopt);
        }
        catch (InvalidTelnetOptionException e)
        {
            System.err.println("Error registering option handlers: " + e.getMessage());
            new Notifier("Gestab exception","Error registering option handlers: " + e.getMessage(),3,dao);
        }
        
        
        final LinkedBlockingQueue<Character> sb = new LinkedBlockingQueue<Character>();*/

        

        while (true)
        {
            boolean end_loop = false;
            try
            {
                tc.connect(remoteip, remoteport);
                

                Thread reader = new Thread (new Telnet(dao,stage,server,out,i));
                tc.registerNotifHandler(new Telnet(dao,stage,server,out,i));
                
                
                System.out.println("TelnetClientConnection");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                            dao.textArea.setText("Connection établie...");
                            dao.btn.setDisable(false);
                            dao.btn.getStyleClass().add(dao.colornodanger());


                    }
                });
                /*System.out.println("Type AYT to send an AYT telnet command");
                System.out.println("Type OPT to print a report of status of options (0-24)");
                System.out.println("Type REGISTER to register a new SimpleOptionHandler");
                System.out.println("Type UNREGISTER to unregister an OptionHandler");
                System.out.println("Type SPY to register the spy (connect to port 3333 to spy)");
                System.out.println("Type UNSPY to stop spying the connection");
                System.out.println("Type ^[A-Z] to send the control character; use ^^ to send ^");*/

                reader.start();
                OutputStream outstr = tc.getOutputStream();

                byte[] buff = new byte[1024];
                int ret_read = 0;
                ByteArrayInputStream in;

                do
                {
                    try
                    {
                        //ret_read = System.in.read(buff);
                        buff = this.waitCommand();
                        in = new ByteArrayInputStream(buff);
                        ret_read = in.read(buff);
                        
                        if(ret_read > 0)
                        {
                             line = new String(buff, 0, ret_read); // deliberate use of default charset
                            isconnected = true;
                            /*if(line.startsWith("AYT"))
                            {
                                try
                                {
                                    System.out.println("Sending AYT");

                                    System.out.println("AYT response:" + tc.sendAYT(5000));
                                }
                                catch (IOException e)
                                {
                                    System.err.println("Exception waiting AYT response: " + e.getMessage());
                                } catch (IllegalArgumentException ex) {
                                    Logger.getLogger(Telnet.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Telnet.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            else if(line.startsWith("OPT"))
                            {
                                 System.out.println("Status of options:");
                                 for(int ii=0; ii<25; ii++) {
                                     System.out.println("Local Option " + ii + ":" + tc.getLocalOptionState(ii) +
                                                        " Remote Option " + ii + ":" + tc.getRemoteOptionState(ii));
                                 }
                            }
                            else if(line.startsWith("REGISTER"))
                            {
                                StringTokenizer st = new StringTokenizer(new String(buff));
                                try
                                {
                                    st.nextToken();
                                    int opcode = Integer.parseInt(st.nextToken());
                                    boolean initlocal = Boolean.parseBoolean(st.nextToken());
                                    boolean initremote = Boolean.parseBoolean(st.nextToken());
                                    boolean acceptlocal = Boolean.parseBoolean(st.nextToken());
                                    boolean acceptremote = Boolean.parseBoolean(st.nextToken());
                                    SimpleOptionHandler opthand = new SimpleOptionHandler(opcode, initlocal, initremote,
                                                                    acceptlocal, acceptremote);
                                    tc.addOptionHandler(opthand);
                                }
                                catch (Exception e)
                                {
                                    if(e instanceof InvalidTelnetOptionException)
                                    {
                                        System.err.println("Error registering option: " + e.getMessage());
                                    }
                                    else
                                    {
                                        System.err.println("Invalid REGISTER command.");
                                        System.err.println("Use REGISTER optcode initlocal initremote acceptlocal acceptremote");
                                        System.err.println("(optcode is an integer.)");
                                        System.err.println("(initlocal, initremote, acceptlocal, acceptremote are boolean)");
                                    }
                                }
                            }
                            else if(line.startsWith("UNREGISTER"))
                            {
                                StringTokenizer st = new StringTokenizer(new String(buff));
                                try
                                {
                                    st.nextToken();
                                    int opcode = (new Integer(st.nextToken())).intValue();
                                    tc.deleteOptionHandler(opcode);
                                }
                                catch (Exception e)
                                {
                                    if(e instanceof InvalidTelnetOptionException)
                                    {
                                        System.err.println("Error unregistering option: " + e.getMessage());
                                    }
                                    else
                                    {
                                        System.err.println("Invalid UNREGISTER command.");
                                        System.err.println("Use UNREGISTER optcode");
                                        System.err.println("(optcode is an integer)");
                                    }
                                }
                            }
                            else if(line.startsWith("SPY"))
                            {
                                tc.registerSpyStream(fout);
                            }
                            else if(line.startsWith("UNSPY"))
                            {
                                tc.stopSpyStream();
                            }
                            else if(line.matches("^\\^[A-Z^]\\r?\\n?$"))
                            {
                                byte toSend = buff[1];
                                if (toSend == '^') {
                                    outstr.write(toSend);
                                } else {
                                    outstr.write(toSend - 'A' + 1);
                                }
                                outstr.flush();
                            }
                            else if(line.startsWith(">>User name:"))
                            {
                                isconnected=false;
                            }
                            else
                            {*/
                                try
                                {
                                        outstr.write(buff, 0 , ret_read);
                                        outstr.flush();
                                }
                                catch (IOException e)
                                {
                                        end_loop = true;
                                }
                            /*}*/
                        }else{
                            new Notifier("Gestab Telnet Exception","Telnet:not input stream",3,dao);
                        }
                        
                    }
                    catch (IOException e)
                    {
                        System.err.println("Exception while reading keyboard:" + e.getMessage());
                        new Notifier("Gestab Telnet Exception","Exception while reading keyboard:" + e.getMessage(),3,dao);
                        end_loop = true;
                    }
                }
                while((ret_read > 0) && (end_loop == false));

                try
                {
                    tc.disconnect();
                }
                catch (IOException e)
                {
                          System.err.println("Exception while connecting:" + e.getMessage());
                          new Notifier("Gestab Telnet Exception","Exception while disconnecting:" + e.getMessage(),3,dao);
                }
                
            }
            catch (IOException e)
            {
                    System.err.println("Exception while connecting:" + e.getMessage());
                    
                    Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                    stage.close();
                                    //dao.textArea.setText("Exception while connecting:" + e.getMessage());
                                    new Notifier("Gestab Telnet Exception","Exception while connecting:" + e.getMessage(),3,dao);                           
                                    
                                    //dao.textArea.setText("Echec de connection...");
                                

                            }
                        });
                    break;
                    //System.exit(1);
            }
        }
        
       
        }
    
        
    /***
     * Callback method called when TelnetClient receives an option
     * negotiation command.
     *
     * @param negotiation_code - type of negotiation command received
     * (RECEIVED_DO, RECEIVED_DONT, RECEIVED_WILL, RECEIVED_WONT, RECEIVED_COMMAND)
     * @param option_code - code of the option negotiated
     ***/
    @Override
    public void receivedNegotiation(int negotiation_code, int option_code)
    {
        String command = null;
        switch (negotiation_code) {
            case TelnetNotificationHandler.RECEIVED_DO:
                command = "DO";
                break;
            case TelnetNotificationHandler.RECEIVED_DONT:
                command = "DONT";
                break;
            case TelnetNotificationHandler.RECEIVED_WILL:
                command = "WILL";
                break;
            case TelnetNotificationHandler.RECEIVED_WONT:
                command = "WONT";
                break;
            case TelnetNotificationHandler.RECEIVED_COMMAND:
                command = "COMMAND";
                break;
            default:
                command = Integer.toString(negotiation_code); // Should not happen
                break;
        }
        System.out.println("Received " + command + " for option code " + option_code);
   }

    /***
     * Reader thread.
     * Reads lines from the TelnetClient and echoes them
     * on the screen.
     ***/
    @Override
    public void run()
    {
        InputStream instr = tc.getInputStream();

        try
        {
            byte[] buff = new byte[1024];
            int ret_read = 0;
            
            text = new String();
            do
            {
                ret_read = instr.read(buff);
                if(ret_read > 0)
                {
                   System.out.print(new String(buff, 0, ret_read));
                   //out.append(new String(buff, 0, ret_read));
                   text=new String(buff, 0, ret_read);
                   
                   Platform.runLater(new Runnable() {
                       @Override
                       public void run() {
                           //String text="";
                          if(text.contains("invalid")||text.contains("console time out")||text.contains("timeout expired")){ 
                              new Notifier("Gestab MSAN Exception",text,3,dao);
                              stage.close();
                              disconnect();
                              
                          }else{
                            if(i==1){
                             if(text.contains("Deactivated")||text.contains("Activated")||text.contains("Activating")){
                                 text.trim();

                                  //dao.textArea.appendText(text+"\n");
                                  String port = (String) text.subSequence(1, 10);
                                  dao.textArea.setText("Port:"+port.trim()+"\n");
                                  String status = (String) text.subSequence(10, 30);
                                  dao.textArea.appendText("Status:"+status.trim()+"\n");
                                  String debit = (String) text.subSequence(40, 50);
                                  dao.textArea.appendText("Line_Profile:"+debit.trim()+"\n");
                                  String profile = (String) text.subSequence(50, 60);
                                  dao.textArea.appendText("Alm_Profile:"+profile.trim()+"\n");


                              }
                            }else{
                                dao.textArea.appendText("\n");
                                dao.textArea.appendText(text.trim());
                                /*if(text.contains("#")||text.contains(">")){
                                    dao.textArea.appendText("\n");
                                }*/
                                System.out.println(text);
                                if(text.contains("successfully")&&!text.contains("failure")){
                                    
                                    System.out.println("client ok:"+dao.client.getNumTel());
                                    if(dao.client!=null){
                                        updateBD(dao.client);
                                    }    
                                }
                            }
                          }
                           
                       }
                   });
                }
            }
            while (ret_read >= 0);
            //dao.textArea.setText(text);
        }
        catch (IOException e)
        {
            System.err.println("Exception while reading socket:" + e.getMessage());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    new Notifier("Gestab Telnet Exception","Exception while reading socket:" + e.getMessage(),3,dao);
                     stage.close();

                }
            });
            
        }
        
        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
                
                try
                {
                    if(!tc.equals(null)){
                        stage.close();
                        tc.disconnect();
                        System.out.println("yes");
                        //tc = null;
                    }    
                }
                catch (IOException e)
                {
                    System.err.println("Exception while closing telnet:" + e.getMessage());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.close();
                            new Notifier("Gestab Telnet Exception","Exception while closing telnet:" + e.getMessage(),3,dao);

                        }
                    });
                }

            }
        });*/
        
    }
    
    public void disconnect(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                
                try
                {
                    if(!tc.equals(null)){
                        tc.disconnect();
                        //tc=null;
                    }    
                }
                catch (IOException e)
                {
                    System.err.println("Exception while closing telnet:" + e.getMessage());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            new Notifier("Gestab Telnet Exception","Exception while closing telnet:" + e.getMessage(),3,dao);
                            stage.close();
                        }
                    });
        }

            }
        });
        
    }
    
    
    public void updateBD(Client client){
        Connecter c=null;
        try {
            c = new Connecter();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
        }
            Requettes con = new Requettes(c);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            //System.out.println("today:"+dateFormat.format(date));

            Historique hist = new Historique(dateFormat.format(date),"RAS",client.getIntStatut());
            int id=0;
        try {
            con.fUpdateClient(client);
            id = con.fInsertHistorique(client.getId(),hist );
            c.getConnexion().close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ModalTelnetActiver.class.getName()).log(Level.SEVERE, null, ex);
        }
            hist.setId(id);
            Hashtable tables = dao.getClientHistorique();
            if(tables.get(client.getId())!=null){
                ArrayList fhistoriques = (ArrayList) tables.get(client.getId());
                fhistoriques.add(hist);
            }else{
                ArrayList fhistoriques = new ArrayList();
                fhistoriques.add(hist);
                tables.put(client.getId(), fhistoriques);
            }

        new Notifier("Gestab Notification","port "+client.getStatut(),2,dao);

        ModalClientNotifier modalClientNotifier = new ModalClientNotifier(dao,"Mr/Mme:"+client.getNom()+" "+client.getPrenom()+" </br>votre ligne a été "+client.getStatut(),client);
        modalClientNotifier.construire();
    
    }
    
    

}
