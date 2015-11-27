package fr.urssaf.image.sae.services.controles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensaml.util.resource.ResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.utils.InputStreamSource;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-metadata-test.xml" })
public class SAEControlesCaptureServiceImplMetaTest {

   @Autowired
   SAEControlesCaptureService saeControlesCaptureService;

   @Autowired
   private CassandraServerBean server;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   /**
    * Test permettant que vérifier que des valeurs de métadonnées non associées
    * à un dictionnaire sont rejettes
    * 
    * @throws UnknownMetadataEx
    * @throws DuplicatedMetadataEx
    * @throws InvalidValueTypeAndFormatMetadataEx
    * @throws SAECaptureServiceEx
    * @throws IOException
    * @throws ParseException
    * @throws RequiredArchivableMetadataEx
    * @throws MetadataValueNotInDictionaryEx
    * @throws URISyntaxException
    * @throws DictionaryNotFoundException
    * @throws ResourceException
    */
   @Test
   public final void checkUntypedMetadataNotInDictionary()
         throws UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, SAECaptureServiceEx, IOException,
         ParseException, RequiredArchivableMetadataEx, URISyntaxException,
         DictionaryNotFoundException, ResourceException {

      ClassPathResource ressource = new ClassPathResource("doc/doc1.PDF");
      List<UntypedMetadata> metas = new ArrayList<UntypedMetadata>();
      metas.add(new UntypedMetadata("CodeRND", "1.6"));
      metas.add(new UntypedMetadata("Hash", "hash"));
      DataHandler dataHandler = new DataHandler(new InputStreamSource(FileUtils
            .openInputStream(ressource.getFile())));
      UntypedDocument untypedDocument = new UntypedDocument(dataHandler, metas);
      try {
         saeControlesCaptureService.checkUntypedMetadata(untypedDocument);
      } catch (MetadataValueNotInDictionaryEx ex) {
         Assert
               .assertEquals(
                     "La valeur de la métadonnée CodeRND est incorrecte: elle n'est pas comprise dans le dictionnaire de données associé",
                     ex.getMessage());
      }

   }

   /**
    * Test permettant de vérifier qu'une exception est levée si le dictionnaire
    * n'est pas trouvé
    * 
    * @throws UnknownMetadataEx
    * @throws DuplicatedMetadataEx
    * @throws InvalidValueTypeAndFormatMetadataEx
    * @throws SAECaptureServiceEx
    * @throws IOException
    * @throws ParseException
    * @throws RequiredArchivableMetadataEx
    * @throws URISyntaxException
    * @throws MetadataValueNotInDictionaryEx
    * @throws ResourceException
    */

   @Test(expected = MetadataRuntimeException.class)
   public final void checkUntypedMetadataDictionaryNotExist()
         throws UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, SAECaptureServiceEx, IOException,
         ParseException, RequiredArchivableMetadataEx, URISyntaxException,
         MetadataValueNotInDictionaryEx, ResourceException {

      ClassPathResource ressource = new ClassPathResource("doc/doc1.PDF");

      List<UntypedMetadata> metas = new ArrayList<UntypedMetadata>();
      metas.add(new UntypedMetadata("CodeRND", "1.1"));
      metas.add(new UntypedMetadata("Hash", "hash"));
      metas.add(new UntypedMetadata("Siret", "siret"));
      DataHandler dataHandler = new DataHandler(new InputStreamSource(FileUtils
            .openInputStream(ressource.getFile())));
      UntypedDocument untypedDocument = new UntypedDocument(dataHandler, metas);
      saeControlesCaptureService.checkUntypedMetadata(untypedDocument);
   }

}