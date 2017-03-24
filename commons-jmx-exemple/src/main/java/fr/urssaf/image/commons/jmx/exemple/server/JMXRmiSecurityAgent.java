package fr.urssaf.image.commons.jmx.exemple.server;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import fr.urssaf.image.commons.jmx.exemple.server.mbeans.ExempleStandard;

public class JMXRmiSecurityAgent {

   public static void main(String[] args) throws Exception { 
      
      // Création d'un MBeanServer
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
      
      // Déclaration d'un MBean
      // 1) On lui donne un nom
      ObjectName name = new ObjectName("fr.urssaf.image.commons.jmx:type=ExempleStandard"); 
      // 2) On le déclare dans le MBeanServer 
      ExempleStandard mbean = new ExempleStandard(); 
      mbs.registerMBean(mbean, name); 
      
      // Préparation de la sécurisation par mot de passe
      Map<String, String> env = new HashMap<String, String>();
      env.put("jmx.remote.x.access.file", "src/main/config/server/jmx.access");
      env.put("jmx.remote.x.password.file", "src/main/config/server/jmx.password");
      
      // Démarrage d'un serveur RMI sur le port 9998
      // avec les paramètres de sécurisation
      LocateRegistry.createRegistry(9998);
      String serviceUrl = "service:jmx:rmi:///jndi/rmi://localhost:9998/server";
      JMXServiceURL url = new JMXServiceURL(serviceUrl);
      JMXConnectorServer jmxConnector = 
         JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);
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
