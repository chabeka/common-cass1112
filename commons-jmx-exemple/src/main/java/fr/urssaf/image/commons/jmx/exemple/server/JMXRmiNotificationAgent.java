package fr.urssaf.image.commons.jmx.exemple.server;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import fr.urssaf.image.commons.jmx.exemple.server.mbeans.ExempleNotification;

public class JMXRmiNotificationAgent {

   public static void main(String[] args) throws Exception { 
      
      // Création d'un MBeanServer
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
      
      // Déclaration d'un MBean
      // 1) On lui donne un nom
      ObjectName name = new ObjectName("fr.urssaf.image.commons.jmx:type=ExempleNotification"); 
      // 2) On le déclare dans le MBeanServer 
      ExempleNotification mbean = new ExempleNotification(); 
      mbs.registerMBean(mbean, name); 
      
      // Démarrage d'un serveur RMI sur le port 9998
      LocateRegistry.createRegistry(9998);
      String serviceUrl = "service:jmx:rmi:///jndi/rmi://localhost:9998/server";
      JMXServiceURL url = new JMXServiceURL(serviceUrl);
      JMXConnectorServer jmxConnector = 
         JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
      jmxConnector.start();
      
      // Boucle pour se laisser le temps
      // d'aller voir le MBean
      mbean.setValeur(1);
      while (mbean.getValeur() < 50000) {
         System.out.println(mbean.getValeur());
         mbean.setValeur(mbean.getValeur()+1);
         Thread.sleep(1000);
      }
      
      // Arrêt du serveur RMI
      jmxConnector.stop();
      
   }
   
}
