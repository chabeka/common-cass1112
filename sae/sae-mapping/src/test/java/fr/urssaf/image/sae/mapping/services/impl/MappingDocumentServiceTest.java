package fr.urssaf.image.sae.mapping.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.dataprovider.MappingDataProviderUtils;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.mapping.test.constants.Constants;
import fr.urssaf.image.sae.mapping.utils.Utils;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Classe qui permet de faire les tests sur les services
 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService
 * MappingDocumentService}
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-mapping-test.xml" })
public class MappingDocumentServiceTest {

   @Autowired
   private MappingDocumentService mappingService;

   /**
    * Fournit les données de test
    * 
    * @param xmlfile
    *           : Le fichier xml.
    * @return Une document non typé
    * @throws FileNotFoundException
    *            Exception levé lorsque le fichier n'existe pas.
    */
   private final UntypedDocument getUntypedDocument(final File xmlfile)
         throws FileNotFoundException {
      List<UntypedMetadata> metadatas = null;
      final byte[] content = "fichier Test".getBytes();
      metadatas = MappingDataProviderUtils.getUntypedMetadata(xmlfile);
      return new UntypedDocument(content, metadatas);
   }

   /**
    * Test de la méthode untypedDocumentToSaeDocument
    * 
    * @throws FileNotFoundException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws InvalidSAETypeException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws MappingFromReferentialException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    */
   @Test
   public void untypedDocumentToSaeDocument() throws FileNotFoundException,
         InvalidSAETypeException, MappingFromReferentialException {
      final UntypedDocument untyped = getUntypedDocument(Constants.MAPPING_FILE_1);
      final SAEDocument saeDoc = mappingService
            .untypedDocumentToSaeDocument(untyped);
      Assert.assertNotNull(saeDoc);
      Assert.assertNotNull(saeDoc.getMetadatas());
      for (SAEMetadata metadata : Utils.nullSafeIterable(saeDoc.getMetadatas())) {

         if (metadata.getLongCode().equals("DateArchivage")) {
            Assert.assertEquals("Date", metadata.getValue().getClass()
                  .getSimpleName());
         }

         if (metadata.getLongCode().equals("DateCreation")) {
            Assert.assertEquals("Date", metadata.getValue().getClass()
                  .getSimpleName());
         }
         if (metadata.getLongCode().equals("VersionNumber")) {
            Assert.assertEquals("Integer", metadata.getValue().getClass()
                  .getSimpleName());
         }
         if (metadata.getLongCode().equals("CodeRND")) {
            Assert.assertEquals("String", metadata.getValue().getClass()
                  .getSimpleName());
         }
      }

   }

   /**
    * Test de la méthode saeDocumentToUntypedDocument
    * 
    * @throws FileNotFoundException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws InvalidSAETypeException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws MappingFromReferentialException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    */
   @Test
   public void saeDocumentToUntypedDocument() throws FileNotFoundException,
         InvalidSAETypeException, MappingFromReferentialException {
      UntypedDocument untyped = getUntypedDocument(Constants.MAPPING_FILE_1);
      final SAEDocument saeDoc = mappingService
            .untypedDocumentToSaeDocument(untyped);
      untyped = mappingService.saeDocumentToUntypedDocument(saeDoc);
      Assert.assertNotNull(untyped);
      Assert.assertNotNull(untyped.getUMetadatas());
      for (UntypedMetadata metadata : Utils.nullSafeIterable(untyped
            .getUMetadatas())) {
         if (metadata.getLongCode().equals("DateArchivage")) {
            Assert.assertTrue(metadata.getValue().equals("2011-06-03"));
         }

         if (metadata.getLongCode().equals("DateCreation")) {
            Assert.assertTrue(metadata.getValue().equals("2011-06-03"));
         }
         if (metadata.getLongCode().equals("VersionNumber")) {
            Assert.assertTrue(metadata.getValue().equals("120"));
         }
         if (metadata.getLongCode().equals("CodeRND")) {
            Assert.assertTrue(metadata.getValue().equals("3.1.3.1.1"));
         }
      }

   }

   /**
    * Test de la méthode saeDocumentToStorageDocument
    * 
    * @throws FileNotFoundException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws InvalidSAETypeException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws MappingFromReferentialException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws ParseException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    */
   @Test
   public void saeDocumentToStorageDocument() throws FileNotFoundException,
         InvalidSAETypeException, MappingFromReferentialException,
         ParseException {
      final UntypedDocument untyped = getUntypedDocument(Constants.MAPPING_FILE_1);
      final SAEDocument saeDoc = mappingService
            .untypedDocumentToSaeDocument(untyped);
      final StorageDocument storageDoc = mappingService
            .saeDocumentToStorageDocument(saeDoc);
      Assert.assertNotNull(storageDoc);
      Assert.assertNotNull(storageDoc.getMetadatas());
      Assert.assertTrue(storageDoc.getMetadatas().size() == 5);

   }

   /**
    * Test de la méthode storageDocumentToSaeDocument
    * 
    * @throws FileNotFoundException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws InvalidSAETypeException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws MappingFromReferentialException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws ParseException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    */
   @Test
   public void storageDocumentToSaeDocument() throws FileNotFoundException,
         InvalidSAETypeException, MappingFromReferentialException,
         ParseException {

      final UntypedDocument untyped = getUntypedDocument(Constants.MAPPING_FILE_1);

      SAEDocument saeDoc = mappingService.untypedDocumentToSaeDocument(untyped);

      final StorageDocument storageDoc = mappingService
            .saeDocumentToStorageDocument(saeDoc);

      saeDoc = mappingService.storageDocumentToSaeDocument(storageDoc);

      Assert.assertNotNull(saeDoc);
      Assert.assertNotNull(saeDoc.getMetadatas());
      Assert.assertTrue(saeDoc.getMetadatas().size() == 5);

      for (SAEMetadata metadata : Utils.nullSafeIterable(saeDoc.getMetadatas())) {

         if (metadata.getLongCode().equals("DateArchivage")) {
            Assert.assertEquals("Date", metadata.getValue().getClass()
                  .getSimpleName());
         }
         if (metadata.getLongCode().equals("VersionNumber")) {
            Assert.assertEquals("Integer", metadata.getValue().getClass()
                  .getSimpleName());
         }
         if (metadata.getLongCode().equals("CodeRND")) {
            Assert.assertEquals("String", metadata.getValue().getClass()
                  .getSimpleName());
         }
      }
   }

   /**
    * Test de la méthode storageDocumentToSaeDocument
    * 
    * @throws FileNotFoundException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws InvalidSAETypeException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws MappingFromReferentialException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    * @throws ParseException
    *            Exception lever lorqu'il y'a un dysfonctionnement.
    */
   @Test
   public void storageDocumentToUntypedDocument() throws FileNotFoundException,
         InvalidSAETypeException, MappingFromReferentialException,
         ParseException {
      UntypedDocument untyped = getUntypedDocument(Constants.MAPPING_FILE_1);
      final SAEDocument saeDoc = mappingService
            .untypedDocumentToSaeDocument(untyped);
      final StorageDocument storageDoc = mappingService
            .saeDocumentToStorageDocument(saeDoc);
      untyped = mappingService.storageDocumentToUntypedDocument(storageDoc);
      Assert.assertNotNull(untyped);
      Assert.assertNotNull(untyped.getUMetadatas());
      Assert.assertTrue(untyped.getUMetadatas().size() == 5);
      for (UntypedMetadata metadata : Utils.nullSafeIterable(untyped
            .getUMetadatas())) {
         if (metadata.getLongCode().equals("DateArchivage")) {
            Assert.assertTrue(metadata.getValue().equals("2011-06-03"));
         }
         if (metadata.getLongCode().equals("VersionNumber")) {
            Assert.assertTrue(metadata.getValue().equals("120"));
         }
         if (metadata.getLongCode().equals("CodeRND")) {
            Assert.assertTrue(metadata.getValue().equals("3.1.3.1.1"));
         }
      }

   }

}
