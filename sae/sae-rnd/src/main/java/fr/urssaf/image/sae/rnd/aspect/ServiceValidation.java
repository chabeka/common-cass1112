package fr.urssaf.image.sae.rnd.aspect;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.rnd.modele.Correspondance;

/**
 * Classe de validation
 * 
 * 
 */
@Aspect
public class ServiceValidation {

   private static final String AJOUTER_CORRES = "execution(void "
         + "fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport.ajouterCorrespondance(*,*))"
         + "&& args(correspondance, clock)";

   /**
    * Vérification des arguments de la méthode ajouterCorrespondance
    * 
    * @param correspondance
    *           la correspondance
    * @param code
    *           Horloge de la création
    */
   @Before(AJOUTER_CORRES)
   public final void validationArgRechercherDoc(Correspondance correspondance,
         Long clock) {

      if (clock == null) {
         throw new IllegalArgumentException("l'argument clock n'est pas valide");
      }

      if (correspondance == null) {
         throw new IllegalArgumentException(
               "l'argument correspondance n'est pas valide");
      } else {
         if (StringUtils.isBlank(correspondance.getCodeDefinitif())) {
            throw new IllegalArgumentException(
                  "l'argument correspondance n'est pas valide : code définitif null ou vide");
         }

         if (StringUtils.isBlank(correspondance.getCodeTemporaire())) {
            throw new IllegalArgumentException(
                  "l'argument correspondance n'est pas valide : code temporaire null ou vide");
         }
      }
   }

}
