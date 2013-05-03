/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedVirtualDocument;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Validation des arguments passés en paramètre des implémentations du service
 * {@link fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport}
 * . La validation est basée sur la programmation Aspect.
 * 
 */
@Aspect
public class CaptureMasseControleSupportValidation {

   private static final String CONTROLE = "execution(void fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport.controleSAEDocument(*,*))"
         + " && args(document,ecdeDirectory)";

   private static final String CONTROLE_STCK = "execution(void fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport.controleSAEDocumentStockage(*))"
         + " && args(document)";

   private static final String CONTROLE_FICHIER = "execution(void fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport.controleFichier(*,*))"
         + " && args(reference,ecdeDirectory)";

   private static final String CONTROLE_VIRTUEL = "execution(void fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport.controleSAEMetadatas(*))"
         + " && args(document)";

   private static final String CONTROLE_STCK_VRTL = "execution(void fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport.controleSAEVirtualDocumentStockage(*))"
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
   @Before(CONTROLE)
   public final void checkControleDocument(final UntypedDocument document,
         final File ecdeDirectory) {

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
   @Before(CONTROLE_STCK)
   public final void checkControleDocumentStorage(final SAEDocument document) {

      if (document == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "document"));
      }

   }

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * controleFichier possède tous les arguments renseignés
    * 
    * @param reference
    *           le fichier de référence
    * @param ecdeDirectory
    *           le répertoire parent
    */
   @Before(CONTROLE_FICHIER)
   public final void checkControleFichier(final VirtualReferenceFile reference,
         final File ecdeDirectory) {

      if (reference == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "fichier de référence"));
      }

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "ecdeDirectory"));
      }

   }

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * controleFichier possède tous les arguments renseignés
    * 
    * @param document
    *           modèle métier du document
    */
   @Before(CONTROLE_VIRTUEL)
   public final void checkControleSAEMetatadatas(
         final UntypedVirtualDocument document) {

      if (document == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "document virtuel"));
      }

      if (document.getReference() == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "fichier de référence du document"));
      }

   }

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * controleSAEDocumentStockage possède tous les arguments renseignés
    * 
    * @param document
    *           modèle métier du document
    */
   @Before(CONTROLE_STCK_VRTL)
   public final void checkControleVirtualDocumentStorage(
         final SAEVirtualDocument document) {

      if (document == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "document virtuel"));
      }

   }

}
