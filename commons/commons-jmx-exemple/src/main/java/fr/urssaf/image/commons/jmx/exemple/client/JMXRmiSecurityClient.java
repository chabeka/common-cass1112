package fr.urssaf.image.commons.jmx.exemple.client;

import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXRmiSecurityClient {

   public static void main(String[] args) throws Exception {
      
      // Construction de l'URL JMX
      String serviceUrl = "service:jmx:rmi://localhost/jndi/rmi://localhost:9998/server";
      JMXServiceURL url = new JMXServiceURL(serviceUrl);
      
      // Login/password de connexion
      String[] credentials = new String[] { "controlRole" , "controlRolePassword" };
      Map<String, String[]> env = new HashMap<String, String[]>();
      env.put("jmx.remote.credentials", credentials);
      
      // Connexion au serveur RMI
      // avec le login/password de connexion
      JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
      
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
