package fr.urssaf.image.sae.metadata.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService;

/**
 * 
 * @author akenore
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
@SuppressWarnings("PMD.TooManyMethods")
public class ValidationServiceImplTest {
   @Autowired
   @Qualifier("metadataControlServices")
   private MetadataControlServices controlService;

   @Autowired
   private SAEConvertMetadataService convertService;

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkArchivableMetadata(SAEDocument)
    * checkArchivableMetadata }
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkArchivableDocument() {
      controlService.checkArchivableMetadata(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkArchivableMetadata(SAEDocument)
    * checkArchivableMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkArchivableMetadata() {
      controlService.checkArchivableMetadata(new SAEDocument());
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkConsultableMetadata(java.util.List)
    * checkConsultableMetadata }
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkConsultableMetadata() {
      controlService.checkConsultableMetadata(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkDuplicateMetadata(java.util.List)
    * checkDuplicateMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkDuplicateMetadata() {
      controlService.checkDuplicateMetadata(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkExistingMetadata(UntypedDocument)
    * checkExistingMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkExistingDocument() {
      controlService.checkExistingMetadata(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkExistingQueryTerms(java.util.List)
    * checkExistingQueryTerms}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkExistingQueryTerms() {
      controlService.checkExistingQueryTerms(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkExistingMetadata(UntypedDocument)
    * checkExistingMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkExistingMetadata() {
      controlService.checkExistingMetadata(new UntypedDocument());
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkMetadataValueTypeAndFormat(UntypedDocument)
    * checkMetadataValueTypeAndFormat}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkMetadataValueTypeAndFormatDoc() {
      controlService.checkMetadataValueTypeAndFormat(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkMetadataListValueTypeAndFormat(java.util.List)
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkMetadataListValueTypeAndFormatDoc() {
      controlService.checkMetadataListValueTypeAndFormat(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkMetadataValueTypeAndFormat(UntypedDocument)
    * checkMetadataValueTypeAndFormat}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkMetadataValueTypeAndFormatMetadata() {
      controlService.checkMetadataValueTypeAndFormat(new UntypedDocument());
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkRequiredForStorageMetadata(SAEDocument)
    * checkRequiredForStorageMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkRequiredForStorageMetadata() {
      controlService.checkRequiredForStorageMetadata(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkRequiredForArchivalMetadata(SAEDocument)
    * checkRequiredForArchivalMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkRequiredForArchivalMetadata() {
      controlService.checkRequiredForArchivalMetadata(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkSearchableMetadata(SAEDocument)
    * checkSearchableMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkSearchableMetadata() {
      controlService.checkSearchableMetadata(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkMetadataRequiredValue(UntypedDocument)
    * checkSearchableMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkMetadataRequiredValue() {
      controlService.checkMetadataRequiredValue(new UntypedDocument());
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkMetadataValueFromDictionary(UntypedDocument)}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkMetadataValueFromDictionary() {
      controlService.checkMetadataValueFromDictionary(new UntypedDocument());
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkMetadataRequiredValue(UntypedDocument)
    * checkSearchableMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkMetadataListValueFromDictionary() {
      controlService.checkMetadataListValueFromDictionary(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkMetadataRequiredValue(UntypedDocument)
    * checkSearchableMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkMetadataListRequiredValue() {
      controlService.checkMetadataListRequiredValue(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkMetadataRequiredValue(UntypedDocument)
    * checkSearchableMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkRequiredForArchivalMetadataList() {
      controlService.checkRequiredForArchivalMetadataList(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkMetadataRequiredValue(UntypedDocument)
    * checkSearchableMetadata}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkArchivableMetadataList() {
      controlService.checkArchivableMetadataList(null);
   }

   /**
    * @return Le service de contrôle des métadonnées.
    */
   public final MetadataControlServices getControlService() {
      return controlService;
   }

   /**
    * @param controlService
    *           : Le service de contrôle des métadonnées.
    */
   public final void setControlService(
         final MetadataControlServices controlService) {
      this.controlService = controlService;
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkMetadataRequiredValue(UntypedDocument)
    * checkSearchableMetadata}
    * 
    * @throws LongCodeNotFoundException
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkConvertMetadataRequiredValue()
         throws LongCodeNotFoundException {
      convertService.longCodeToShortCode(null);
   }

   /**
    * validation de l'argument du service
    * {@link fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl#checkExistingMetadataList(java.util.List)
    * ) checkExistingMetadataList}
    */
   @Test(expected = IllegalArgumentException.class)
   public void checkExistingMetadataList() {
      controlService.checkExistingMetadataList(null);
   }

}
