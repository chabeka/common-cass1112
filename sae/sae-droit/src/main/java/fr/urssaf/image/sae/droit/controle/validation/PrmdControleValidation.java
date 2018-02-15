/**
 * 
 */
package fr.urssaf.image.sae.droit.controle.validation;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Classe de validation des arguments entrée des implémentations du service
 * PrmdControle
 * 
 */
@Aspect
public class PrmdControleValidation {
   
   private static final String ARGUMENT_REQUIRED = "argument.required";

   private static final String CHECK = "execution(boolean fr.urssaf.image.sae.droit.controle.PrmdControle.isPermitted(*,*))"
         + "&& args(metadatas, parametres)";

   private static final String CREATE = "execution(java.lang.String fr.urssaf.image.sae.droit.controle.PrmdControle.createLucene(*))"
         + "&& args(parametres)";
   
   private static final String ADD_DOMAIN = "execution(void fr.urssaf.image.sae.droit.controle.PrmdControle.addDomaine(*,*))"
      + "&& args(metadatas, values)";

   /**
    * Méthode de vérification des arguments passés en entrée de l'implémentation
    * de la méthode isPermitted de l'interface PrmdControle
    * 
    * @param metadatas
    *           liste des métadonnées
    * @param parametres
    *           valeurs des paramètres dynamiques
    */
   @Before(CHECK)
   public final void checkIsPermitted(List<UntypedMetadata> metadatas,
         Map<String, String> parametres) {

      if (CollectionUtils.isEmpty(metadatas)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "métadonnées"));
      }

      if (parametres == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "paramètres"));
      }
   }

   /**
    * Méthode de vérification des arguments passés en entrée de l'implémentation
    * de la méthode createLucene de l'interface PrmdControle
    * 
    * @param parametres
    *           valeurs des paramètres dynamiques
    */
   @Before(CREATE)
   public final void checkCreateLucene(Map<String, String> parametres) {
      if (parametres == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "paramètres"));
      }
   }
   
   /**
    * Méthode de vérification des arguments passés en entrée de l'implémentation
    * de la méthode createLucene de l'interface PrmdControle
    * 
    * @param metadatas
    *           liste des métadonnées
    * @param values
    *           valeurs des paramètres dynamiques
    */
   @Before(ADD_DOMAIN)
   public final void checkAddDomaine(List<UntypedMetadata> metadatas,
         Map<String, String> values) {

      if (metadatas == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "métadonnées"));
      }

      if (values == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "valeurs des métadonnées"));
      }
   }
}