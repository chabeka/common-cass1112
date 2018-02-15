package fr.urssaf.image.commons.jmx.exemple.client;

import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXRmiNotificationClient implements NotificationListener  {

   public static void main(String[] args) throws Exception {
      JMXRmiNotificationClient client = new JMXRmiNotificationClient();
      client.start();
   }
   
   public void start() throws Exception {
      
      // Construction de l'URL JMX
      String serviceUrl = "service:jmx:rmi://localhost/jndi/rmi://localhost:9998/server";
      JMXServiceURL url = new JMXServiceURL(serviceUrl);
      
      // Connexion au serveur RMI
      JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

      // Récupération du MBean Server
      MBeanServerConnection mbs = jmxc.getMBeanServerConnection();
      
      // Construction du nom du MBean auquel on veut accéder
      ObjectName name = new ObjectName("fr.urssaf.image.commons.jmx:type=ExempleNotification");
      
      // Abonnement aux notifications
      mbs.addNotificationListener(name, this , null, null);
      
      // On attend
      while (true) {
         Thread.sleep(1000);
      }
      
   }
   
   public void handleNotification(Notification notif, Object handback ) {
      System.out.println(notif.getSequenceNumber() + " - " + notif.getMessage());
   }
   
}
