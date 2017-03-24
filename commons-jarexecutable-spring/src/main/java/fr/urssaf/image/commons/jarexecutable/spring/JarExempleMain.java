package fr.urssaf.image.commons.jarexecutable.spring;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.commons.jarexecutable.spring.bean.JarBean;
import fr.urssaf.image.commons.jarexecutable.spring.factory.ApplicationContextFactory;

/**
 * Classe principale du JAR executable
 * 
 */
public final class JarExempleMain {
   
   private JarExempleMain() {

   }

   private static final Logger LOG = Logger.getLogger(JarExempleMain.class);
   
   
   public static final String FILE_CONFIG_EMPTY = "Il faut préciser, dans la ligne de commande, " +
   		                                        "le chemin complet du fichier de parametres.";
   
   /**
    * Point d'entrée du programme.<br>
    * A partir d'un fichier donné en paramètre on récupére la string a afficher et les deux valeurs<br> 
    * à additionner.
    * @param args
    *          paramétre de la ligne de commande
    */
   public static void main(String []args) {
      
      
     if (ArrayUtils.isEmpty(args) || !StringUtils.isNotBlank(args[0])) {
         throw new IllegalArgumentException(FILE_CONFIG_EMPTY);
     }
               
     // 1. Récupération du chemin complet du fichier de paramétre,
     // jarexe.properties
     String pathConfigFile = args[0];
         
     // 2. Mapping de ce chemin en un objet de type  Properties
     // 3. Récupération de la valeur de la clé jar.exe.stringaafficher
     //    contenue dans l'objet Properties representant le fichier transmis en parametre
     String stringAAfficher = getString(pathConfigFile, "jar.exe.stringaafficher");
     
     int entier1 = Integer.parseInt(getString(pathConfigFile, "jar.exe.entier1"));
     int entier2 = Integer.parseInt(getString(pathConfigFile, "jar.exe.entier2"));
         
     // Création d'un contexte Spring
     ApplicationContext appliContext = ApplicationContextFactory
                                       .createApplicationContext("/applicationContext-jar-exe.xml");
     // Appel du jarBean
     JarBean jarBean =
               (JarBean) appliContext.getBean("jarBean");
         
     // apppel de la méthode d'affichage du texte
     jarBean.stringAAfficher(stringAAfficher);    
     
     // appel de la méthode d'addition de deux valeurs
     int somme = jarBean.add(entier1, entier2);
     System.out.println("La somme des deux entiers contenus dans le fichier "
                      + "transmis en parametre est egale à " + somme);

   }

   /**
    * Récupération du string à afficher
    * La clé de la string à afficher est jar.exe.stringaafficher 
    * 
    * @param pathConfigFile
    *             chemin complet du fichier de config
    * @param cle
    *             cle contenu dans le fichier de config
    *             
    * @return
    *    valeur associée à la clé dans le fichier de config
    */
   public static String getString(String pathConfigFile, String cle) {
      try {
            
         Properties props = new Properties();
         FileInputStream fis = new FileInputStream(pathConfigFile);
         try {
            props.load(fis);
         }
         finally {
            fis.close();
         }
         
         return props.getProperty(cle);
            
      } catch (IOException e) {
         LOG.error(e.getMessage(), e);
         throw new RuntimeException(e);
      }
   }
   
   

}
