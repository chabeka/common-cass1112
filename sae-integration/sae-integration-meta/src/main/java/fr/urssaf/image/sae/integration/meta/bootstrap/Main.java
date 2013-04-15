package fr.urssaf.image.sae.integration.meta.bootstrap;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;

import fr.urssaf.image.sae.integration.meta.service.MetadonneeService;

/**
 * Classe principale pour l'exécution du JAR exécutable
 */
public final class Main {

   private static final Logger LOG = LoggerFactory.getLogger(Main.class);

   private static final String LINE_SEPARATOR = System
         .getProperty("line.separator");

   private Main() {

   }

   /**
    * Méthode principal
    * 
    * @param args
    *           arguments de l'exécutable. 2 arguments attendus :
    *           <ol>
    *           <li>le chemin complet du fichier xml contenant les
    *           métadonnées/dictionnaires</li>
    *           <li>le chemin complet du fichier de configuration Cassandra</li>
    *           </ol>
    */
   public static void main(String[] args) {

      // Si aucun argument, on affiche dans la console
      // les arguments à renseigner dans la ligne de commande
      if (ArrayUtils.getLength(args) == 0) {

         afficheUsage();

      } else {

         // Vérification succinte des 2 arguments
         if (!isNotBlank(args, 0)) {
            throw new IllegalArgumentException(
                  "Il faut spécifier en 1er argument de la ligne de commande le chemin complet du fichier XML contenant les métadonnées/dictionnaires");
         }
         if (!isNotBlank(args, 1)) {
            throw new IllegalArgumentException(
                  "Il faut spécifier en 2ème argument de la ligne de commande le chemin complet du fichier de configuration Cassandra");
         }

         // Traitement
         File fichierXml = new File(args[0]);
         File fichierConfCassandra = new File(args[1]);
         traitement(fichierXml, fichierConfCassandra);

      }

   }

   private static void afficheUsage() {

      StringBuilder sBuilder = new StringBuilder();

      sBuilder.append("Utilisation de sae-integration-meta.jar :");
      sBuilder.append(LINE_SEPARATOR);

      sBuilder
            .append("java -jar -Dlogback.configurationFile=[logback] sae-integration-meta.jar [0] [1]");
      sBuilder.append(LINE_SEPARATOR);

      sBuilder
            .append("[logback] : le chemin complet du fichier de configuration Logback");
      sBuilder.append(LINE_SEPARATOR);

      sBuilder
            .append("[0] : le chemin complet du fichier XML contenant les métadonnées/dictionnaires");
      sBuilder.append(LINE_SEPARATOR);

      sBuilder
            .append("[1] : le chemin complet du fichier de configuration Cassandra");
      sBuilder.append(LINE_SEPARATOR);

      sBuilder.append("Exemple :");
      sBuilder.append(LINE_SEPARATOR);

      sBuilder
            .append("java -jar -Dlogback.configurationFile=/appl/sae/sae-integration-meta/logback-sae-integration-meta.xml /appl/sae/sae-integration-meta/sae-integration-meta.jar /appl/sae/sae-integration-meta/saemeta.xml /appl/sae/sae-integration-meta/cassandra-config.properties");
      // sb.append(LINE_SEPARATOR);

      LOG.info(sBuilder.toString());

   }

   private static boolean isNotBlank(String[] args, int index) {

      return ArrayUtils.getLength(args) > index
            && StringUtils.isNotBlank(args[index]);
   }

   private static void traitement(File fichierXml, File fichierConfCassandra) {

      // Chargement du fichier de contexte Spring
      ClassPathXmlApplicationContext context = chargementContexteSpring(fichierConfCassandra);
      try {

         // Récupération du service "principal"
         MetadonneeService metaService = context
               .getBean(MetadonneeService.class);

         // Lancement du traitement
         metaService.traitement(fichierXml);

      } finally {

         // on force ici la fermeture du contexte de Spring
         // ceci a pour but de forcer la déconnexion avec Cassandra
         context.close();

      }

   }

   private static ClassPathXmlApplicationContext chargementContexteSpring(
         File fichierConfCassandra) {

      String contextConfig = "applicationContext-sae-integration-meta.xml";

      GenericApplicationContext genericContext = new GenericApplicationContext();

      BeanDefinitionBuilder saeConfigBean = BeanDefinitionBuilder
            .genericBeanDefinition(FileSystemResource.class);
      saeConfigBean.addConstructorArgValue(fichierConfCassandra);

      genericContext.registerBeanDefinition("cassandraConfigResource",
            saeConfigBean.getBeanDefinition());
      genericContext.refresh();

      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfig }, genericContext);

      return context;

   }

}
