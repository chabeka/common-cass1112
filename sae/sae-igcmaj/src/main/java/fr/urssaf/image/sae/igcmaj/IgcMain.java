package fr.urssaf.image.sae.igcmaj;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.service.IgcConfigService;
import fr.urssaf.image.sae.igc.service.IgcDownloadService;
import fr.urssaf.image.sae.igcmaj.exception.IgcMainException;

/**
 * Classe contenant la méthode main qui fait office de point d'entrée au JAR
 * exécutable<br>
 * <br>
 * 
 */
public final class IgcMain {

   private final String contextConfig;

   private static final Logger LOG = LoggerFactory.getLogger(IgcMain.class);

   protected IgcMain(String contextConfig) {
      this.contextConfig = contextConfig;
   }

   public static final String CONFIG_EMPTY = "Il faut préciser, dans la ligne de commande, le chemin complet des fichiers de configuration du SAE et de l'IGC";
   public static final String SAE_CONFIG_EMPTY = "Il faut préciser, dans la ligne de commande, le chemin complet du fichier de configuration du SAE";
   public static final String IGC_CONFIG_EMPTY = "Il faut préciser, dans la ligne de commande, le chemin complet du fichier de configuration de l'IGC";

   /**
    * Point d’entrée du programme. <br>
    * Téléchargement des CRL à partir du fichier de configuration indiqué en
    * arguments<br>
    * <br>
    * paramètres ordonnés:
    * <ul>
    * <li>arg[0] : chemin du fichier de configuration, doit être renseigné</li>
    * </ul>
    * 
    * @param args
    *           arguments de la méthode exécutable
    */
   public static void main(String[] args) {

      IgcMain instance = new IgcMain("/applicationContext-sae-igcmaj.xml");

      instance.execute(args);

   }

   protected final void execute(String args[]) {

      if (ArrayUtils.isEmpty(args) || ArrayUtils.getLength(args) != 2) {
         throw new IllegalArgumentException(CONFIG_EMPTY);
      }

      if (!StringUtils.isNotBlank(args[0])) {
         throw new IllegalArgumentException(SAE_CONFIG_EMPTY);
      }
      if (!StringUtils.isNotBlank(args[1])) {
         throw new IllegalArgumentException(IGC_CONFIG_EMPTY);
      }

      String pathConfigFile = args[0];

      // instanciation du contexte de SPRING
      ClassPathXmlApplicationContext context = ContextFactory
            .createSAEApplicationContext(contextConfig, pathConfigFile);

      try {

         String pathIgcConfigFile = args[1];
         IgcConfigService igcConfigService = (IgcConfigService) context
               .getBean(IgcConfigService.class);
         IgcConfigs igcConfigs = igcConfigService.loadConfig(pathIgcConfigFile);

         IgcDownloadService igcDownloadService = (IgcDownloadService) context
               .getBean(IgcDownloadService.class);
         igcDownloadService.telechargeCRLs(igcConfigs);

      } catch (IgcConfigException e) {
         LOG.error(e.getMessage(), e);
         throw new IgcMainException(e);

      } catch (IgcDownloadException e) {
         LOG.error(e.getMessage(), e);
         throw new IgcMainException(e);

      }

      finally {

         // on force ici la fermeture du contexte de Spring
         // ceci a pour but de forcer la déconnexion avec Cassandra, la SGBD
         // chargé de la persistance de la pile des travaux
         LOG.debug("execute - fermeture du contexte d'application");
         context.close();
         LOG.debug("execute - fermeture du contexte d'application effectuée");
      }

   }

}
