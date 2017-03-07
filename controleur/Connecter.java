/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import notifier.Notifier;

/**
 *
 * @author ghomsi
 */
public class Connecter {
    
    private Connection con ;
    private PreparedStatement stm;      // le statement pour la requete
    private String route;
    
    public Connecter()throws ClassNotFoundException, SQLException{
        try {
        Class.forName("org.postgresql.Driver"); 
         
         String[] frac = LireFichier();
         //System.out.println(frac[0]+frac[1]+frac[2]+frac[3]);
         if(frac!=null){
            con = DriverManager.getConnection("jdbc:postgresql://"+frac[0]+":"+frac[1]+"/camtel.db",frac[2], frac[3]);
            route=frac[0];
            //new Notifier("Gestab","Welcome\n",0);
         }else{
            con = DriverManager.getConnection("jdbc:postgresql://localhost:80/camtel.db","username", "none"); 
         }
        } catch (Exception e) {
           e.printStackTrace();
           System.err.println(e.getClass().getName()+": "+e.getMessage());
           new Notifier("Gestab exception","probleme de connection:\n"+e.getMessage(),3);
           //System.exit(0);
        }
      System.out.println("Opened database successfully");
    }
    
    public Connection getConnexion(){
        return con;
    }
    public void setConnexion(Connection fcon){
        con=fcon;
    }
    
    public PreparedStatement getStatement(){
        return stm;
    }
    public void setStatement(PreparedStatement fstm){
        stm= fstm;
    }
    public String getRoute(){
        return route;
    }
    
    public String[] LireFichier(){
        FileReader fichier=null;
     
        BufferedReader tampon=null;
        String[] frac=null;
        try{
          
            fichier=new FileReader("infos_comptes.tcp");
            tampon=new BufferedReader(fichier);
            String chaine="";
            while(true){
            chaine=tampon.readLine();
            if(chaine!=null){
                frac=chaine.split("~");
                
            }else{
                break;
            }
                
            }
          
        }catch(IOException e){
            System.out.println("erreur de lecture");
            /*frac[0]="localhost";
            frac[1]="80";
            frac[2]="username";
            frac[3]="";*/
            new Notifier("Gestab exception","probleme de fichier 'infos_comptes.tcp':\n"+e,3);
       
        }
        return frac;
    }
    
}
