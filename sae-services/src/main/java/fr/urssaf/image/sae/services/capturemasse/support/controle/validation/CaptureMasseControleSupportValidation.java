/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Validation des arguments passés en paramètre des implémentations du service
 * {@link fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport}
 * . La validation est basée sur la programmation Aspect.
 * 
 */
@Aspect
public class CaptureMasseControleSupportValidation {

   private static final String CONTROLES_DOCUMENT_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport.controleSAEDocument(*,*))"
         + " && args(document,ecdeDirectory)";

   private static final String CONTROLES_DOCUMENT_STOCK_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport.controleSAEDocumentStockage(*))"
         + " && args(document)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * controleSAEDocument possède tous les arguments renseignés
    * 
    * @param document
    *           modèle métier du document
    * @param ecdeDirectory
    *           chemin absolu du répertoire de traitement de l'ECDE
    */
   @Before(CONTROLES_DOCUMENT_METHOD)
   public final void checkControleDocument(UntypedDocument document,
         File ecdeDirectory) {

      if (document == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "document"));
      }

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "ecdeDirectory"));
      }

   }

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * controleSAEDocumentStockage possède tous les arguments renseignés
    * 
    * @param document
    *           modèle métier du document
    */
   @Before(CONTROLES_DOCUMENT_STOCK_METHOD)
   public final void checkControleDocumentStorage(SAEDocument document) {

      if (document == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "document"));
      }

   }

}
