package fr.urssaf.image.sae.igcmaj;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.igc.IgcServiceFactory;
import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igcmaj.exception.IgcMainException;

/**
 * Classe contenant la méthode main qui fait office de point d'entrée au JAR
 * exécutable<br>
 * <br>
 * 
 */
public final class IgcMain {

   private IgcMain() {

   }

   private static final Logger LOG = LoggerFactory.getLogger(IgcMain.class);

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

      if (ArrayUtils.isEmpty(args) || !StringUtils.isNotBlank(args[0])) {
         throw new IllegalArgumentException(IGC_CONFIG_EMPTY);
      }

      String pathConfigFile = args[0];

      try {
         IgcConfigs igcConfigs = IgcServiceFactory.createIgcConfigService()
               .loadConfig(pathConfigFile);

         IgcServiceFactory.createIgcDownloadService()
               .telechargeCRLs(igcConfigs);

      } catch (IgcConfigException e) {
         LOG.error(e.getMessage(), e);
         throw new IgcMainException(e);

      } catch (IgcDownloadException e) {
         LOG.error(e.getMessage(), e);
         throw new IgcMainException(e);

      }

   }
}
