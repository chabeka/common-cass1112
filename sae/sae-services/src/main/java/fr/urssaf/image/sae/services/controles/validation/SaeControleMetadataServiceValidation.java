package fr.urssaf.image.sae.services.controles.validation;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * SaeControleMetadataService
 * 
 * 
 */
@Aspect
public class SaeControleMetadataServiceValidation {

   private static final String CHECK_UNTYPED_METHOD = "execution(void fr.urssaf.image.sae.services.controles.SaeControleMetadataService.checkUntypedMetadatas(*))"
         + "&& args(metadatas)";

   private static final String CHECK_CAPTURE_METHOD = "execution(void fr.urssaf.image.sae.services.controles.SaeControleMetadataService.checkSaeMetadataForCapture(*))"
         + "&& args(metadatas)";

   private static final String CHECK_STORAGE_METHOD = "execution(void fr.urssaf.image.sae.services.controles.SaeControleMetadataService.checkMetadataForStorage(*))"
         + "&& args(metadatas)";

   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la
    * methode checkUntypedMetadatas de l'interface SaeControleMetadataService
    * sont bien correct.
    * 
    * @param untypedDocument
    *           document à traiter
    * 
    */
   @Before(CHECK_UNTYPED_METHOD)
   public final void checkUntypedBinaryDocument(List<UntypedMetadata> metadatas) {

      if (metadatas == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "liste des métadonnées"));
      }
   }

   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la
    * methode checkSaeMetadataForCapture de l'interface
    * SaeControleMetadataService sont bien correct.
    * 
    * @param untypedDocument
    *           document à traiter
    * 
    */
   @Before(CHECK_CAPTURE_METHOD)
   public final void checkSaeMetadataForCapture(List<SAEMetadata> metadatas) {

      if (metadatas == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "liste des métadonnées"));
      }
   }

   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la
    * methode checkSaeMetadataForCapture de l'interface
    * SaeControleMetadataService sont bien correct.
    * 
    * @param untypedDocument
    *           document à traiter
    * 
    */
   @Before(CHECK_STORAGE_METHOD)
   public final void checkSaeMetadataForStorage(List<SAEMetadata> metadatas) {

      if (CollectionUtils.isEmpty(metadatas)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "liste des métadonnées"));
      }
   }

}
