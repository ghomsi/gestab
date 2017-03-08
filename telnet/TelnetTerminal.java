/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telnet;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 *
 * @author ghomsi
 */
public class TelnetTerminal {
    
    static final Logger log = Logger.getLogger(TelnetTerminal.class);
    
    protected String cmd = "";
    protected boolean interruptWaiting = false;
    
    public void sendCommand(String command) {
        
        cmd = command;
        cmd = cmd + "\n";
        interruptWaiting = true;

    }
    public byte[] waitCommand() {
        BasicConfigurator.configure();
        while (!interruptWaiting) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                log.error(ex);
                System.out.println(ex);
            }
        }
        interruptWaiting = false;
        char[] chars = cmd.toCharArray();
        byte[] bytes = Charset.forName("ASCII").encode(CharBuffer.wrap(chars)).array();//encode chars to bytes
        cmd = "";
        return bytes;
    }
}
