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
/*******************************************************************************
 * This file is part of OpenNMS(R). 
 * 
 * Copyright (C) 2009-2011 The OpenNMS Group, Inc. 
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc. 
 * 
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc. 
 * 
 * OpenNMS(R) is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * OpenNMS(R) is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with OpenNMS(R).  If not, see: 
 *      http://www.gnu.org/licenses/ 
 * 
 * For more information contact: 
 *     OpenNMS(R) Licensing <license@opennms.org> 
 *     http://www.opennms.org/ 
 *     http://www.opennms.com/ 
 *******************************************************************************/
 
 
//import examples.bulksms.SendMessage;
//import examples.modem.SendMessage;
import org.smslib.IOutboundMessageNotification; 
import org.smslib.Library; 
import org.smslib.OutboundMessage; 
import org.smslib.Service; 
import org.smslib.modem.SerialModemGateway;
 
public class SendMessage { 
 
    public void doIt() throws Exception 
 { 
  Service srv; 
  OutboundMessage msg; 
  OutboundNotification outboundNotification = new OutboundNotification(); 
  System.out.println("Example: Send message from a serial gsm modem."); 
  System.out.println(Library.getLibraryDescription()); 
  System.out.println("Version: " + Library.getLibraryVersion()); 
  
  /*String serialPortID = "/dev/ttyUSB0";
  System.setProperty("gnu.io.rxtx.SerialPorts", serialPortID);*/

  srv = new Service(); 
  SerialModemGateway gateway = new SerialModemGateway("modem.com1", "/dev/ttyUSB0", 115200, "", ""); 
  
  gateway.setInbound(true); 
  gateway.setOutbound(true); 
  gateway.setSimPin("0000"); 
  gateway.setSmscNumber("+23769900929");
  srv.setOutboundNotification(outboundNotification); 
  srv.addGateway(gateway); 
  srv.startService(); 
  System.out.println(); 
  System.out.println("Modem Information:"); 
  System.out.println("  Manufacturer: " + gateway.getManufacturer()); 
  System.out.println("  Model: " + gateway.getModel()); 
  System.out.println("  Serial No: " + gateway.getSerialNo()); 
  System.out.println("  SIM IMSI: " + gateway.getImsi()); 
  System.out.println("  Signal Level: " + gateway.getSignalLevel() + "%"); 
  System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%"); 
  System.out.println(); 
  // Send a message synchronously. 
  msg = new OutboundMessage("+237656288838", "Hello from SMSLib!"); 
  srv.sendMessage(msg); 
  System.out.println(msg); 
  // Or, send out a WAP SI message. 
  //OutboundWapSIMessage wapMsg = new OutboundWapSIMessage("+306948494037",  new URL("https://mail.google.com/"), "Visit GMail now!"); 
  //srv.sendMessage(wapMsg); 
  //System.out.println(wapMsg); 
  // You can also queue some asynchronous messages to see how the callbacks 
  // are called... 
  //msg = new OutboundMessage("+309999999999", "Wrong number!"); 
  //msg.setPriority(OutboundMessage.Priorities.LOW); 
  //srv.queueMessage(msg, gateway.getGatewayId()); 
  //msg = new OutboundMessage("+308888888888", "Wrong number!"); 
  //msg.setPriority(OutboundMessage.Priorities.HIGH); 
  //srv.queueMessage(msg, gateway.getGatewayId()); 
  System.out.println("Now Sleeping - Hit <enter> to terminate."); 
  System.in.read(); 
  srv.stopService(); 
 } 
 
 public class OutboundNotification implements IOutboundMessageNotification 
 { 
  public void process(String gatewayId, OutboundMessage msg) 
  { 
   System.out.println("Outbound handler called from Gateway: " + gatewayId); 
   System.out.println(msg); 
  } 
 } 
 
 public static void main(String args[]) 
 { 
  SendMessage app = new SendMessage(); 
  try 
  { 
   app.doIt(); 
  } 
  catch (Exception e) 
  { 
   e.printStackTrace(); 
  } 
 }  
 
}
