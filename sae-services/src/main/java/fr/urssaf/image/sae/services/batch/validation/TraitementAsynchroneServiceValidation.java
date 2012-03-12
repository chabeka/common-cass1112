package fr.urssaf.image.sae.services.batch.validation;

import java.text.MessageFormat;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService}.<br>
 * La validation est basée sur la programmation aspect
 * 
 * 
 */
@Aspect
public class TraitementAsynchroneServiceValidation {

   private static final String CLASS = "fr.urssaf.image.sae.services.batch.TraitementAsynchroneService.";

   private static final String METHOD_1 = "execution(long " + CLASS
         + "ajouterJobCaptureMasse(*,*))" + "&& args(urlEcde,uuid)";

   private static final String ARG_EMPTY = "L''argument ''{0}'' doit être renseigné.";

   /**
    * Validation des arguments d'entrée de la méthode
    * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#ajouterJobCaptureMasse(String, UUID)}
    * 
    * @param urlEcde
    *           doit être renseigné
    * @param uuid
    *           doit être renseigné
    */
   @Before(METHOD_1)
   public final void ajouterJobCaptureMasse(String urlEcde, UUID uuid) {

      if (StringUtils.isBlank(urlEcde)) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "urlEcde"));
      }

      if (uuid == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "uuid"));
      }

   }

}
