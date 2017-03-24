package fr.urssaf.image.commons.jmx.exemple.client;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXRmiSimpleClient {

   public static void main(String[] args) throws Exception {
      
      // Construction de l'URL JMX
      String serviceUrl = "service:jmx:rmi://localhost/jndi/rmi://localhost:9998/server";
      JMXServiceURL url = new JMXServiceURL(serviceUrl);
      
      // Connexion au serveur RMI
      JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

      // Récupération du MBean Server
      MBeanServerConnection mbs = jmxc.getMBeanServerConnection();
      
      // Construction du nom du MBean auquel on veut accéder
      ObjectName name = new ObjectName("fr.urssaf.image.commons.jmx:type=ExempleStandard");
      
      // Lecture de l'attribut Nom
      String nom = (String) mbs.getAttribute(name, "Nom");
      System.out.println("Nom : " + nom);
      
      // Lecture et modification de l'attribut Valeur
      int valeur = (Integer) mbs.getAttribute(name, "Valeur");
      System.out.println("Valeur avant modif : " + valeur);
      Attribute valeurAttribut = new Attribute("Valeur",1);
      mbs.setAttribute(name, valeurAttribut);
      valeur = (Integer) mbs.getAttribute(name, "Valeur");
      System.out.println("Valeur après modif : " + valeur);
      
      // Invocation de l'opération rafraichir
      mbs.invoke(name, "rafraichir",new Object[0], new String[0]);
      
   }
   
}
