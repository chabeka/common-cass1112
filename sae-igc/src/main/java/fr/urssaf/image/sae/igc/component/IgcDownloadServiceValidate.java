package fr.urssaf.image.sae.igc.component;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.util.TextUtils;

/**
 * Classe de validation de
 * <code>fr.urssaf.image.sae.igc.service.IgcDownloadService</code>
 * 
 * 
 */
@Aspect
public class IgcDownloadServiceValidate {

   @SuppressWarnings("PMD.LongVariable")
   public static final String IGC_CONFIG_NOTEXIST = "Le fichier de configuration IGC est introuvable (${0})";

   private static final String METHODE = "execution(* fr.urssaf.image.sae.igc.service.IgcDownloadService.telechargeCRLs(..))";

   /**
    * Vérification de la validité des arguments entrée de la méthode
    * <code>telechargeCRLs</code>
    * <ul>
    * <li><code>igcConfig</code> doit être renseigné</li>
    * </ul>
    * 
    * 
    * @param joinPoint
    *           jointure de la méthode <code>telechargeCRLs</code>
    */
   @Before(METHODE)
   public final void telechargeCRLsBefore(JoinPoint joinPoint) {

      IgcConfigs igcConfigs = (IgcConfigs) joinPoint.getArgs()[0];

      if (igcConfigs == null) {
         throw new IllegalArgumentException(TextUtils.getArgEmpty("igcConfig"));
      }

   }
}
