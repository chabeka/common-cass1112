package fr.urssaf.image.sae.webservices.security.igc;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.igc.IgcServiceFactory;
import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.service.IgcConfigService;
import fr.urssaf.image.sae.igc.util.TextUtils;

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
   public static final String IGC_CONFIG_REQUIRED = "Le fichier de configuration générale du SAE ne contient pas le chemin du fichier de configuration IGC";

   @SuppressWarnings("PMD.LongVariable")
   public static final String IGC_CONFIG_NOTEXIST = "Le fichier de configuration IGC indiqué dans le fichier de configuration générale est introuvable (${0})";

   /**
    * initialisation des répertoires des fichiers AC racine et des CRL à partir
    * d'un fichier de configuration
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
    * Une exception {@link IllegalArgumentException} avec le message peut-être
    * levée
    * <ul>
    * <li><code>{@value #IGC_CONFIG_REQUIRED}</code>: le fichier igcConfig.xml
    * doit être renseigné</li>
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
   public static IgcConfig createIgcConfig(FileSystemResource igcConfigResource) {

      Assert.hasText(igcConfigResource.getPath(), IGC_CONFIG_REQUIRED);

      Assert.isTrue(igcConfigResource.getFile().isFile(), TextUtils.getMessage(
            IGC_CONFIG_NOTEXIST, igcConfigResource.getPath()));

      IgcConfigService service = IgcServiceFactory.createIgcConfigService();

      try {
         return service.loadConfig(igcConfigResource.getPath());
      } catch (IgcConfigException e) {

         throw new IllegalArgumentException(ICG_CONFIG_ERROR, e);
      }
   }
}
