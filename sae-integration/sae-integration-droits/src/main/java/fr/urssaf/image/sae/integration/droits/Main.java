package fr.urssaf.image.sae.integration.droits;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;

import fr.urssaf.image.sae.integration.droits.service.DroitService;



/**
 * Classe principale pour l'exécution du JAR exécutable
 */
public final class Main {

   private static final Logger LOG = LoggerFactory.getLogger(Main.class);
   
   private static final String LINE_SEPARATOR = System.getProperty("line.separator");
   
   private Main() {

   }

   
   /**
    * Méthode principal
    * 
    * @param args
    *           arguments de l'exécutable. 2 arguments attendus : 
    *           <ol>
    *             <li>le chemin complet du fichier xml contenant les droits à créer</li>
    *             <li>le chemin complet du fichier de configuration Cassandra</li>
    *           </ol>
    */
   public static void main(String[] args) {
      
      // Si aucun argument, on affiche dans la console 
      // les arguments à renseigner dans la ligne de commande
      if (ArrayUtils.getLength(args)==0) {
         
         afficheUsage();
         
      } else {
         
         // Vérification succinte des 2 arguments
         if (!isNotBlank(args, 0)) {
            throw new IllegalArgumentException(
                  "Il faut spécifier en 1er argument de la ligne de commande le chemin complet du fichier XML contenant les PRMD et les CS à créer");
         }
         if (!isNotBlank(args, 1)) {
            throw new IllegalArgumentException(
                  "Il faut spécifier en 2ème argument de la ligne de commande le chemin complet du fichier de configuration Cassandra");
         }
         
         // Traitement
         File fichierXmlDroits = new File(args[0]);
         File fichierConfCassandra = new File(args[1]);
         creerDroits(fichierXmlDroits, fichierConfCassandra);
         
      }
      
   }
   
   
   private static void afficheUsage() {
      
      
      StringBuilder sBuilder = new StringBuilder();
      
      sBuilder.append("Utilisation de sae-integration-droits.jar :");
      sBuilder.append(LINE_SEPARATOR);
      
      sBuilder.append("java -jar -Dlogback.configurationFile=[logback] sae-integration-droits.jar [0] [1]");
      sBuilder.append(LINE_SEPARATOR);
      
      sBuilder.append("[logback] : le chemin complet du fichier de configuration Logback");
      sBuilder.append(LINE_SEPARATOR);
      
      sBuilder.append("[0] : le chemin complet du fichier XML contenant les PRMD et les CS à créer");
      sBuilder.append(LINE_SEPARATOR);
      
      sBuilder.append("[1] : le chemin complet du fichier de configuration Cassandra");
      sBuilder.append(LINE_SEPARATOR);
      
      sBuilder.append("Exemple :");
      sBuilder.append(LINE_SEPARATOR);
      
      sBuilder.append("java -jar -Dlogback.configurationFile=/appl/sae/sae-integration-droits/logback-sae-integration-droits.xml /appl/sae/sae-integration-droits/sae-integration-droits.jar /appl/sae/sae-integration-droits/saedroits.xml /appl/sae/sae-integration-droits/cassandra-config.properties");
      // sb.append(LINE_SEPARATOR);
      
      LOG.info(sBuilder.toString());
      
   }

   
   private static boolean isNotBlank(String[] args, int index) {

      return ArrayUtils.getLength(args) > index
            && StringUtils.isNotBlank(args[index]);
   }
   
   
   private static void creerDroits(
         File fichierXmlDroits,
         File fichierConfCassandra) {
    
      // Chargement du fichier de contexte Spring
      ClassPathXmlApplicationContext context = chargementContexteSpring(fichierConfCassandra);
      try {
      
         // Récupération du service "principal"
         DroitService droitService = context.getBean(DroitService.class);
         
         // Lancement du traitement
         droitService.creationDesDroits(fichierXmlDroits);
      
      } finally {
         
         // on force ici la fermeture du contexte de Spring
         // ceci a pour but de forcer la déconnexion avec Cassandra
         context.close();
         
      }
      
      
   }
   
   
   private static ClassPathXmlApplicationContext chargementContexteSpring(File fichierConfCassandra) {
      
      String contextConfig = "applicationContext-sae-integration-droits.xml";
      
      GenericApplicationContext genericContext = new GenericApplicationContext();
      
      BeanDefinitionBuilder saeConfigBean = BeanDefinitionBuilder
            .genericBeanDefinition(FileSystemResource.class);
      saeConfigBean.addConstructorArgValue(fichierConfCassandra);

      genericContext.registerBeanDefinition("cassandraConfigResource", saeConfigBean.getBeanDefinition());
      genericContext.refresh();

      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfig }, genericContext);
      
      return context;
      
   }
   
   
}
