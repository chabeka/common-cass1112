package fr.urssaf.image.sae.metadata.control.services.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.test.constants.Constants;
import fr.urssaf.image.sae.metadata.test.dataprovider.MetadataDataProviderUtils;

/**
 * 
 * Cette classe permet de tester le service
 * {@link MetadataControlServices#checkConsultableMetadata(List)}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
public class TrimMetaControlServicesImplTest {

   @Autowired
   @Qualifier("metadataControlServices")
   private MetadataControlServices controlService;

   /**
    * Vérifie que les métadonnées devant être trimées l'ont bien été
    */
   @Test
   public void checkTrimMetadata()
         throws FileNotFoundException {
      
      List<SAEMetadata> metadatas = MetadataDataProviderUtils.getSAEMetadata(Constants.TRIM_FILE_1);
      String contratService = "";
      List<String> listePagm = new ArrayList<String>();
      String login = "";
      List<SAEMetadata> trimMetadatas = controlService.trimMetadata(metadatas, contratService, listePagm, login);
      
      for (SAEMetadata saeMetadata : trimMetadatas) {
         if (saeMetadata.getLongCode().equals("Denomination")) {
            Assert.assertEquals("La dénomination doit avoir été trimée", "Denomination", saeMetadata.getValue());      
         }
      }
      
   }

 }
