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
import controleur.DAOequipement;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import javafx.scene.control.Label;
import modele.Client;
import modele.Equipement;
import modele.Port;
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class TelnetCommand {
    public TelnetCommand(DAOequipement dao,Equipement elt) throws JSchException, IOException, Exception{
        JSch jsch = new JSch();
        /*String[] frac = dao.LireFichier("infos_comptes.ssh",label);
        String user = frac[2];            //CHANGE ME
        String host = frac[0]; //CHANGE ME
        String passwd = frac[3];      //CHANGE ME
        int port = 22;*/    
        Session session = jsch.getSession("adrien", "10.50.131.150", 23);
        session.setPassword("adrien2016");

        session.setConfig("StrictHostKeyChecking", "no");

        session.connect();

        Channel channel = session.openChannel("shell");
        OutputStream ops = channel.getOutputStream();
        PrintStream ps = new PrintStream(ops, true);

        channel.connect();
        InputStream input = channel.getInputStream();

        //commands
        ps.println("enable");
        //ps.println("username lino privilege 15 secret lino");
        /*ps.println("ls");*/
        ps.println("quit");
        ps.println("y");
        ps.close();

        printResult(input, channel,elt);

        channel.disconnect();
        session.disconnect();
    }
    
    
       /**
    * @param input
    * @param channel
    */
   private static void printResult(InputStream input,
                                   Channel channel,Equipement eq) throws Exception
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
            new Notifier("GESTAB","port/ "+eq.getNom()+ "Enable",0);
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
