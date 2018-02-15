package fr.urssaf.image.sae.services.controles.validation;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * SAEControlesCaptureService
 * 
 * 
 */
@Aspect
public class SAEControlesModificationServiceValidation {

   private static final String DELETE_CONTROLES_METHOD = "execution(void fr.urssaf.image.sae.services.controles.SAEControlesModificationService.checkSaeMetadataForDelete(*))"
         + "&& args(metas)";
   private static final String MODIFICATION_CONTROLES_METHOD = "execution(void fr.urssaf.image.sae.services.controles.SAEControlesModificationService.checkSaeMetadataForUpdate(*))"
         + "&& args(metas)";

   /**
    * Vérifie la signature de la méthode checkSaeMetadataForUpdate
    * 
    * @param metas
    *           la liste des métadonnées
    */
   @Before(MODIFICATION_CONTROLES_METHOD)
   public final void checkUpdate(List<UntypedMetadata> metas) {

      if (CollectionUtils.isEmpty(metas)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "métadonnées"));
      }
   }

   /**
    * Vérifie la signature de la méthode checkSaeMetadataForDelete
    * 
    * @param metas
    *           la liste des métadonnées
    */
   @Before(DELETE_CONTROLES_METHOD)
   public final void checkDelete(List<UntypedMetadata> metas) {

      if (CollectionUtils.isEmpty(metas)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "métadonnées"));
      }
   }

}
