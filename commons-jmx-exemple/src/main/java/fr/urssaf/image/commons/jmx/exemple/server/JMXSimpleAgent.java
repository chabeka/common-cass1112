package fr.urssaf.image.commons.jmx.exemple.server;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import fr.urssaf.image.commons.jmx.exemple.server.mbeans.ExempleStandard;


public class JMXSimpleAgent {

   
   public static void main(String[] args) throws Exception { 
      
      // Création d'un MBeanServer
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
      
      // Déclaration d'un MBean
      // 1) On lui donne un nom
      ObjectName name = new ObjectName("fr.urssaf.image.commons.jmx:type=ExempleStandard"); 
      // 2) On le déclare dans le MBeanServer 
      ExempleStandard mbean = new ExempleStandard(); 
      mbs.registerMBean(mbean, name); 
      
      
      // Boucle pour se laisser le temps
      // d'aller voir le MBean
      mbean.setValeur(1);
      while (mbean.getValeur() < 50000) {
         System.out.println(mbean.getValeur());
         mbean.setValeur(mbean.getValeur()+1);
         Thread.sleep(1000);
      }
      
   }
   
}
