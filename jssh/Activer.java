/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import controleur.Connecter;
import controleur.DAOequipement;
import controleur.Requettes;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import modele.Carte;
import modele.Client;
import modele.Debit;
import modele.Equipement;
import modele.Historique;
import modele.Port;
import notifier.Notifier;
import telnet.ModalTelnetActiver;
import vue.ModalClientNotifier;

/**
 *
 * @author ghomsi
 */
public class Activer {
    private int portssh = 22; 
    
    public Activer(ActionEvent t,DAOequipement dao,String user,String passwd,Label label,Port port,Client client){
        JSch jsch = new JSch();
        //String[] frac = dao.LireFichier("infos_comptes.ssh",label);
        //String user = frac[2];            //CHANGE ME
        //String host = frac[0]; //CHANGE ME
        //String passwd = frac[3];      //CHANGE ME
        
        Carte carte = (Carte) dao.getCarte().get(port.getIdCarte());
        Equipement eq = (Equipement) dao.getEquipement().get(carte.getIdEQ());
           
        Session session;
        try {
            session = jsch.getSession(user, eq.getRoute(), portssh);
        
        session.setPassword(passwd);

        session.setConfig("StrictHostKeyChecking", "no");

        session.connect();

        Channel channel = session.openChannel("shell");
        OutputStream ops = channel.getOutputStream();
        PrintStream ps = new PrintStream(ops, true);

        channel.connect();
        InputStream input = channel.getInputStream();

        //commands
        //ps.println("configure terminal");
        ps.println("enable");
        ps.println("config");
        ps.println("interface adsl 0/"+carte.getNumCarte());
        Debit debit = (Debit) dao.getDebit().get(client.getDebit());
        ps.println("activate "+port.getNumeroPort()+" profile-index "+debit.getprofil());
        ps.println("save");
        ps.println("quit");
        ps.println("quit");
        ps.close();

        printResult(input, channel,label,client);

        channel.disconnect();
        session.disconnect();
        
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

            new Notifier("Gestab Notification","port desactivé",2,dao);
            ModalClientNotifier modalClientNotifier = new ModalClientNotifier(dao,"Mr/Mme:"+client.getNom()+" "+client.getPrenom()+" </br>votre ligne a été activée",client);
            modalClientNotifier.construire();
        
        } catch (JSchException ex) {
            Logger.getLogger(Activer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Activer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
       /**
    * @param input
    * @param channel
    */
   private static void printResult(InputStream input,
                                   Channel channel,Label label,Client client) throws Exception
   {
      int SIZE = 1024;
      byte[] tmp = new byte[SIZE];
      while (true)
      {
         while (input.available() > 0)
         {
            int i = input.read(tmp, 0, SIZE);
            if(i < 0)
               break;
             System.out.print(new String(tmp, 0, i));
             //label.setText(new String(tmp, 0, i));
         }
         if(channel.isClosed())
         {
            System.out.println("exit-status: " + channel.getExitStatus());
            //label.setText("exit-status: " + channel.getExitStatus());
            new Notifier("GESTAB",client.getNom()+" "+client.getPrenom()+" Activé",2);
            break;
         }
         try
         {
            Thread.sleep(300);
         }
         catch (Exception ee)
         {
         }
      }
   }
}
