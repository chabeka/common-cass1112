package fr.urssaf.image.sae.metadata.validation;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.messages.MetadataMessageHandler;

/**
 * Fournit des méthodes de validation des arguments des services de controle des
 * métadonnées.par aspect.
 * 
 * @author akenore
 * 
 */
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods" })
@Aspect
public class MetadataControlServiceValidation {

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.control.services.MetadataControlServices#checkArchivableMetadata(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * checkArchivableMetadata}. <br>
    * 
    * @param saeDocument
    *           : Un objet de type {@link SAEDocument}
    */
   @Before(value = "execution( java.util.List<fr.urssaf.image.sae.bo.model.MetadataError>  fr.urssaf.image.sae.metadata.control.services.MetadataControlServices.checkArchivableMetadata(..)) && args(saeDocument)")
   public final void checkArchivableMetadata(final SAEDocument saeDocument) {
      validateSaeDocument(saeDocument);

   }

   /**
    * 
    * @param saeDocument
    *           : Un objet de type {@link SAEDocument}
    */
   private void validateSaeDocument(final SAEDocument saeDocument) {
      Validate.notNull(saeDocument, MetadataMessageHandler.getMessage(
            "document.required", SAEDocument.class.getName()));
      Validate.notNull(saeDocument.getMetadatas(), MetadataMessageHandler
            .getMessage("metadatas.required", SAEDocument.class.getName()));
   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.control.services.MetadataControlServices#checkRequiredMetadata(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * checkRequiredMetadata}. <br>
    * 
    * @param saeDocument
    *           : Un objet de type {@link SAEDocument}
    */
   @Before(value = "execution( java.util.List<fr.urssaf.image.sae.bo.model.MetadataError>  fr.urssaf.image.sae.metadata.control.services.MetadataControlServices.checkRequiredForArchivalMetadata(..)) && args(saeDocument)")
   public final void checkRequiredForArchivalMetadata(
         final SAEDocument saeDocument) {
      validateSaeDocument(saeDocument);

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.control.services.MetadataControlServices#checkRequiredMetadata(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * checkRequiredMetadata}. <br>
    * 
    * @param saeDocument
    *           : Un objet de type {@link SAEDocument}
    */
   @Before(value = "execution( java.util.List<fr.urssaf.image.sae.bo.model.MetadataError>  fr.urssaf.image.sae.metadata.control.services.MetadataControlServices.checkRequiredForStorageMetadata(..)) && args(saeDocument)")
   public final void checkRequiredForStorageMetadata(
         final SAEDocument saeDocument) {
      validateSaeDocument(saeDocument);

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.control.services.MetadataControlServices#checkExistingMetadata(fr.urssaf.image.sae.bo.model.bo.untyped.UntypedDocument)}
    * checkExistingMetadata}. <br>
    * 
    * @param untypedDoc
    *           : Un objet de type {@link UntypedDocument}
    */
   @Before(value = "execution( java.util.List<fr.urssaf.image.sae.bo.model.MetadataError>  fr.urssaf.image.sae.metadata.control.services.MetadataControlServices.checkExistingMetadata(..)) && args(untypedDoc)")
   public final void checkExistingMetadata(final UntypedDocument untypedDoc) {
      validateUntypedDocument(untypedDoc);

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.control.services.MetadataControlServices#checkExistingQueryTerms(List)}
    * checkExistingQueryTerms}. <br>
    * 
    * @param codes
    *           : La liste des termes de la requête(code long)
    */
   @Before(value = "execution( java.util.List<fr.urssaf.image.sae.bo.model.MetadataError>  fr.urssaf.image.sae.metadata.control.services.MetadataControlServices.checkExistingQueryTerms(..)) && args(codes)")
   public final void checkExistingQueryTerms(final List<String> codes) {
      Validate.notNull(codes, MetadataMessageHandler
            .getMessage("terms.required"));

   }

   /**
    * 
    * @param untypedDoc
    *           : Un objet de type {@link UntypedDocument}
    */
   private void validateUntypedDocument(final UntypedDocument untypedDoc) {
      Validate.notNull(untypedDoc, MetadataMessageHandler.getMessage(
            "document.required", UntypedDocument.class.getName()));
      Validate.notNull(untypedDoc.getUMetadatas(), MetadataMessageHandler
            .getMessage("metadatas.required", UntypedDocument.class.getName()));
   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.control.services.MetadataControlServices#checkMetadataValueTypeAndFormat(fr.urssaf.image.sae.bo.model.bo.untyped.UntypedDocument)}
    * checkMetadataValueTypeAndFormat}. <br>
    * 
    * @param untypedDoc
    *           : un objet de type {@link UntypedDocument}
    */
   @Before(value = "execution( java.util.List<fr.urssaf.image.sae.bo.model.MetadataError>  fr.urssaf.image.sae.metadata.control.services.MetadataControlServices.checkMetadataValueTypeAndFormat(..)) && args(untypedDoc)")
   public final void checkMetadataValueTypeAndFormat(
         final UntypedDocument untypedDoc) {
      validateUntypedDocument(untypedDoc);

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.control.services.MetadataControlServices#checkMetadataRequiredValue(fr.urssaf.image.sae.bo.model.bo.untyped.UntypedDocument)}
    * checkMetadataValueTypeAndFormat}. <br>
    * 
    * @param untypedDoc
    *           : un objet de type {@link UntypedDocument}
    */
   @Before(value = "execution( java.util.List<fr.urssaf.image.sae.bo.model.MetadataError>  fr.urssaf.image.sae.metadata.control.services.MetadataControlServices.checkMetadataRequiredValue(..)) && args(untypedDoc)")
   public final void checkMetadataRequiredValue(final UntypedDocument untypedDoc) {
      validateUntypedDocument(untypedDoc);

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.control.services.MetadataControlServices#checkSearchableMetadata(java.util.List)}
    * checkSearchableMetadata}. <br>
    * 
    * @param metadatas
    *           : La liste des métadonnées métiers.
    */
   @Before(value = "execution( java.util.List<fr.urssaf.image.sae.bo.model.MetadataError>  fr.urssaf.image.sae.metadata.control.services.MetadataControlServices.checkSearchableMetadata(..)) && args(metadatas)")
   public final void checkSearchableMetadata(final List<SAEMetadata> metadatas) {
      Validate.notNull(metadatas, MetadataMessageHandler.getMessage(
            "metadatas.required", SAEMetadata.class.getName()));

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.control.services.MetadataControlServices#checkSearchableMetadata(java.util.List)}
    * checkSearchableMetadata}. <br>
    * 
    * @param metadatas
    *           : La liste des métadonnées métiers.
    */
   @Before(value = "execution( java.util.List<fr.urssaf.image.sae.bo.model.MetadataError>  fr.urssaf.image.sae.metadata.control.services.MetadataControlServices.checkConsultableMetadata(..)) && args(metadatas)")
   public final void checkConsultableMetadata(final List<SAEMetadata> metadatas) {
      Validate.notNull(metadatas, MetadataMessageHandler.getMessage(
            "metadatas.required", SAEMetadata.class.getName()));

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.control.services.MetadataControlServices#checkDuplicateMetadata(java.util.List)}
    * checkDuplicateMetadata}. <br>
    * 
    * @param metadatas
    *           : La liste des métadonnées conteneur.
    */
   @Before(value = "execution( java.util.List<fr.urssaf.image.sae.bo.model.MetadataError>  fr.urssaf.image.sae.metadata.control.services.MetadataControlServices.checkDuplicateMetadata(..)) && args(metadatas)")
   public final void checkDuplicateMetadata(
         final List<UntypedMetadata> metadatas) {
      Validate.notNull(metadatas, MetadataMessageHandler.getMessage(
            "metadatas.required", UntypedMetadata.class.getName()));

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService#longCodeToShortCode(List)}
    * .<br>
    * *
    * 
    * @param listCodeMetadata
    *           : La liste des métadonnées en code long.
    */
   @Before(value = "execution( java.util.Map<java.lang.String, java.lang.String>  fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService.longCodeToShortCode(..)) && args(listCodeMetadata)")
   public final void checkConvertLongCodeToShortCode(
         final List<String> listCodeMetadata) {
      Validate.notNull(listCodeMetadata, MetadataMessageHandler.getMessage(
            "metadatas.required", List.class.getName()));

   }
}
