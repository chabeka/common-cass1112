package fr.urssaf.image.sae.regionalisation.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-regionalisation-service-test.xml")
@SuppressWarnings("PMD.MethodNamingConventions")
public class MetadataDaoTest {

   @Autowired
   private MetadataDao dao;

   private void assertMetadata(Map<String, Object> values,
         String expectedMetadata, Object expectedValue) {

      Assert.assertTrue("la métadonnées '" + expectedMetadata
            + "' n'est pas retrouvée", values.containsKey(expectedMetadata));

      Assert.assertEquals("la valeur de la métadonnée " + expectedMetadata
            + " est inattendu", expectedValue, values.get(expectedMetadata));

   }

   @Test
   public void getMetadatas_partiel() {

      Map<String, Object> values = dao.getMetadatas(BigDecimal.ZERO);

      Assert.assertEquals("le nombre de resultats de la requête est inattendu",
            3, values.size());

      assertMetadata(values, "npe", "123854");
      assertMetadata(values, "dre", Date.valueOf("2007-07-12"));
      assertMetadata(values, "nbp", 4);

   }

   @Test
   public void getMetadatas_total() {

      Map<String, Object> values = dao.getMetadatas(BigDecimal.valueOf(2));

      Assert.assertEquals("le nombre de resultats de la requête est inattendu",
            19, values.size());

      assertMetadata(values, "nne", "148032541101650");
      assertMetadata(values, "npe", "123856");
      assertMetadata(values, "den", "COUTURIER GINETTE");
      assertMetadata(values, "cv2", "4");
      assertMetadata(values, "scv", "11");
      assertMetadata(values, "nci", "719900");
      assertMetadata(values, "nce", "30148032541101600");
      assertMetadata(values, "srt", "12345678912345");
      assertMetadata(values, "psi", "4914736610005");
      assertMetadata(values, "nst", "000050221");
      assertMetadata(values, "nre", "20080798");
      assertMetadata(values, "nic", "57377");
      assertMetadata(values, "dre", Date.valueOf("2007-07-14"));
      assertMetadata(values, "apr", "ADELAIDE");
      assertMetadata(values, "atr", "ATTESTATIONS");
      assertMetadata(values, "cop", "UR750");
      assertMetadata(values, "cog", "UR42");
      assertMetadata(values, "sac", "CER69");
      assertMetadata(values, "nbp", 6);

   }

   @Test
   public void getMetadatas_aucun() {

      Map<String, Object> values = dao.getMetadatas(BigDecimal.valueOf(6));

      Assert.assertEquals("le nombre de resultats de la requête est inattendu",
            0, values.size());

   }
}
