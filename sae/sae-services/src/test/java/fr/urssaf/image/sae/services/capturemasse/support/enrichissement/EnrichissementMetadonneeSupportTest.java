/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class EnrichissementMetadonneeSupportTest {

   @Autowired
   private EnrichissementMetadonneeSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void checkDocumentObligatoire() {

      support.enrichirMetadonnee(null);

      Assert.fail("Sortie aspect attendue");
   }

   @Test(expected = CaptureMasseRuntimeException.class)
   public void testCodeRndErrone() {

      SAEDocument document = new SAEDocument();

      document.setFilePath(null);
      document.setMetadatas(getSaeMetadatas());

      document.getMetadatas().get(3).setValue("0.1.1.12");

      support.enrichirMetadonnee(document);

   }
   
   @Test
   public void testEnrichissementSucces() {
      SAEDocument document = new SAEDocument();

      document.setFilePath("doc1.PDF");
      document.setMetadatas(getSaeMetadatas());

      support.enrichirMetadonnee(document);
   }

   /**
    * @return
    */
   private List<SAEMetadata> getSaeMetadatas() {
      List<SAEMetadata> list = new ArrayList<SAEMetadata>();

      list.add(new SAEMetadata("SiteAcquisition", "CER69"));
      list.add(new SAEMetadata("Titre",
            "NOTIFICATIONS DE REMBOURSEMENT du 41882050200023"));
      list.add(new SAEMetadata("DateCreation", "2012-01-01"));
      list.add(new SAEMetadata("CodeRND", "2.3.1.1.12"));
      list.add(new SAEMetadata("Hash",
            "a2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      list.add(new SAEMetadata("TypeHash", "SHA-1"));
      list.add(new SAEMetadata("TracabilitrePreArchivage", "P"));
      list.add(new SAEMetadata("ApplicationProductrice", "GED"));
      list.add(new SAEMetadata("FormatFichier", "fmt/18"));
      list.add(new SAEMetadata("NbPages", "2"));
      list.add(new SAEMetadata("CodeOrganismeProprietaire", "UR030"));
      list.add(new SAEMetadata("CodeOrganismeGestionnaire", "UR030"));
      return list;
   }

}
