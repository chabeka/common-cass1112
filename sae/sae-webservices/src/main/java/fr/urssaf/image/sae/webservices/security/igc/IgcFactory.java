package fr.urssaf.image.sae.webservices.security.igc;

import org.springframework.core.io.FileSystemResource;

import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;

/**
 * Classe d'instanciation de {@link IgcFactory}
 * 
 * 
 */
public final class IgcFactory {

   private IgcFactory() {

   }

   public static final String ICG_CONFIG_ERROR = "Une erreur s'est produite lors du chargement du fichier de configuration IGC";

   @SuppressWarnings("PMD.LongVariable")
   public static final String IGC_CONFIG_REQUIRED = "Le fichier de configuration gÃ©nÃ©rale du SAE ne contient pas le chemin du fichier de configuration IGC";

   @SuppressWarnings("PMD.LongVariable")
   public static final String IGC_CONFIG_NOTEXIST = "Le fichier de configuration IGC indiquÃ© dans le fichier de configuration gÃ©nÃ©rale est introuvable (${0})";

   /**
    * initialisation des rÃ©pertoires des fichiers AC racine et des CRL Ã 
    * partir d'un fichier de configuration
    * 
    * <pre>
    * &lt;?xml version="1.0" encoding="UTF-8"?>
    * &lt;IgcConfig>
    * 
    *    &lt;repertoireACRacines>
    *       /appl/sae/certificats/ACRacine
    *    &lt;/repertoireACRacines>
    *    
    *    &lt;repertoireCRL>
    *       /appl/sae/certificats/CRL
    *    &lt;/repertoireCRL>
    *     
    *    &lt;URLTelechargementCRL>
    *       &lt;url>http://cer69idxpkival1.cer69.recouv/*.crl&lt;/url>
    *    &lt;/URLTelechargementCRL>
    *       
    * &lt;/IgcConfig>
    * </pre>
    * 
    * Une exception {@link IllegalArgumentException} avec le message peut-Ãªtre
    * levÃ©e
    * <ul>
    * <li><code>{@value #IGC_CONFIG_REQUIRED}</code>: le fichier igcConfig.xml
    * doit Ãªtre renseignÃ©</li>
    * <li><code>{@value #IGC_CONFIG_NOTEXIST}</code>: le fichier igcConfig.xml
    * doit exister</li>
    * <li><code>{@value #ICG_CONFIG_ERROR}</code>: toute autre exception sur le
    * fichier igcConfig.xml</li>
    * </ul>
    * 
    * 
    * @param igcConfigResource
    *           fichier de configuration de l'IGC
    * @return instance de {@link IgcConfig}
    * 
    */
   public static IgcConfigs createIgcConfig(FileSystemResource igcConfigResource) {

      // Assert.notNull(igcConfigResource, IGC_CONFIG_REQUIRED);
      //
      // IgcConfigService service = IgcServiceFactory.createIgcConfigService();

      return null;
   }
}