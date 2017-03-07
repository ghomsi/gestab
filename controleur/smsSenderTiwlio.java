/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

/**
 *
 * @author ghomsi
 */

import com.sun.comm.LinuxDriver;
import java.util.Map;
import java.util.HashMap;

import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import javax.comm.CommPortIdentifier;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class smsSenderTiwlio {
    
    /* Find your sid and token at twilio.com/user/account */
    public static final String ACCOUNT_SID = "ACb36f1b44d46c27a1b7b708ba4d76dda3";
    public static final String AUTH_TOKEN = "a3f80233f6c13af84b3ff36cf81becf9";

    public static void main(String[] args) throws TwilioRestException {
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            System.out.println("madate:"+dateFormat.format(date));
                            
       /* TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        Account account = client.getAccount();

        MessageFactory messageFactory = account.getMessageFactory();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", "+237698263179")); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("From", "+16193777094")); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("Body", "Adrien de son nom de code 'AdrienDev', viens de commander un bouquet de Rose Numerique pour vous 'Paola Djila' suivi de ce, Message: Suis desoler!"));
        Message sms = messageFactory.create(params);*/
        //Win32Driver w32Driver= new Win32Driver();
        //w32Driver.initialize();
        /*LinuxDriver w32Driver= new LinuxDriver();
        w32Driver.initialize();
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        System.out.println("PORTS");
        while (ports.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
            String type;
            switch (port.getPortType()) {
                case CommPortIdentifier.PORT_PARALLEL:
                    type = "Parallel";
                    break;
                case CommPortIdentifier.PORT_SERIAL:
                    type = "Serial";
                    break;
                default: /// Shouldn't happen
                    type = "Unknown";
                    break;
            }
            System.out.println(port.getName() + ": " + type);
        }*/
    }
    
}
