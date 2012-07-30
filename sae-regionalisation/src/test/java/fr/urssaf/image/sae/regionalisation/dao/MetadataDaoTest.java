package fr.urssaf.image.sae.regionalisation.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.sae.regionalisation.bean.Metadata;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-regionalisation-service-test.xml")
@SuppressWarnings("PMD.MethodNamingConventions")
public class MetadataDaoTest {

   @Autowired
   private MetadataDao dao;

   private static final String NB_RESULTATS_MESSAGE = "le nombre de resultats de la requête est inattendu";

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

      Assert.assertEquals(NB_RESULTATS_MESSAGE, 3, values.size());

      assertMetadata(values, "npe", "123854");
      assertMetadata(values, "dre", Date.valueOf("2007-07-12"));
      assertMetadata(values, "nbp", 4);

   }

   @Test
   public void getMetadatas_total() {

      Map<String, Object> values = dao.getMetadatas(BigDecimal.valueOf(2));

      Assert.assertEquals(NB_RESULTATS_MESSAGE, 19, values.size());

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

      Assert.assertEquals(NB_RESULTATS_MESSAGE, 0, values.size());

   }

   @Test
   @Transactional
   public void save() {

      BigDecimal idCritere = BigDecimal.valueOf(6);
      List<Metadata> metadatas = new ArrayList<Metadata>();

      metadatas.add(createMetadata("nne", "148032541101650", true));
      metadatas.add(createMetadata("npe", "123856", false));
      metadatas.add(createMetadata("den", "COUTURIER GINETTE", true));
      metadatas.add(createMetadata("cv2", "4", false));
      metadatas.add(createMetadata("scv", "11", true));
      metadatas.add(createMetadata("nci", "719900", false));
      metadatas.add(createMetadata("nce", "30148032541101600", true));
      metadatas.add(createMetadata("srt", "12345678912345", false));
      metadatas.add(createMetadata("psi", "4914736610005", true));
      metadatas.add(createMetadata("nst", "000050221", false));
      metadatas.add(createMetadata("nre", "20080798", true));
      metadatas.add(createMetadata("nic", "57377", false));
      metadatas.add(createMetadata("dre", Date.valueOf("2007-07-14"), true));
      metadatas.add(createMetadata("apr", "ADELAIDE", false));
      metadatas.add(createMetadata("atr", "ATTESTATIONS", true));
      metadatas.add(createMetadata("cop", "UR750", false));
      metadatas.add(createMetadata("cog", "UR42", true));
      metadatas.add(createMetadata("sac", "CER69", false));
      metadatas.add(createMetadata("nbp", 6, true));

      dao.save(idCritere, metadatas);

      List<Metadata> values = dao.getAllMetadatas(idCritere);

      Assert.assertEquals(NB_RESULTATS_MESSAGE, 19, values.size());

      // on trie traces en fonction de la métadonnée et de l'index
      Comparator<Metadata> comparator = new Comparator<Metadata>() {
         @Override
         public int compare(Metadata metadata1, Metadata metadata2) {

            return metadata1.getCode().compareTo(metadata2.getCode());

         }
      };
      Collections.sort(values, comparator);

      assertMetadata(values.get(0), "apr", "ADELAIDE", false);
      assertMetadata(values.get(1), "atr", "ATTESTATIONS", true);
      assertMetadata(values.get(2), "cog", "UR42", true);
      assertMetadata(values.get(3), "cop", "UR750", false);
      assertMetadata(values.get(4), "cv2", "4", false);
      assertMetadata(values.get(5), "den", "COUTURIER GINETTE", true);
      assertMetadata(values.get(6), "dre", Date.valueOf("2007-07-14"), true);
      assertMetadata(values.get(7), "nbp", 6, true);
      assertMetadata(values.get(8), "nce", "30148032541101600", true);
      assertMetadata(values.get(9), "nci", "719900", false);
      assertMetadata(values.get(10), "nic", "57377", false);
      assertMetadata(values.get(11), "nne", "148032541101650", true);
      assertMetadata(values.get(12), "npe", "123856", false);
      assertMetadata(values.get(13), "nre", "20080798", true);
      assertMetadata(values.get(14), "nst", "000050221", false);
      assertMetadata(values.get(15), "psi", "4914736610005", true);
      assertMetadata(values.get(16), "sac", "CER69", false);
      assertMetadata(values.get(17), "scv", "11", true);
      assertMetadata(values.get(18), "srt", "12345678912345", false);

   }

   @Test
   @Transactional
   public void save_sans_metadonnee() {

      BigDecimal idCritere = BigDecimal.valueOf(6);
      List<Metadata> metadatas = new ArrayList<Metadata>();

      dao.save(idCritere, metadatas);

      List<Metadata> values = dao.getAllMetadatas(idCritere);

      Assert.assertEquals(NB_RESULTATS_MESSAGE, 19, values.size());

      for (Metadata metadata : values) {

         Assert.assertNull("la valeur de la métadonnée '" + metadata.getCode()
               + "' est inattendue", metadata.getValue());

         Assert.assertFalse("le flag de la métadonnée " + metadata.getCode()
               + " est inattendu", metadata.isFlag());

      }

   }

   private Metadata createMetadata(String code, Object value, boolean flag) {

      Metadata metadata = new Metadata();
      metadata.setCode(code);
      metadata.setFlag(flag);
      metadata.setValue(value);

      return metadata;
   }

   private void assertMetadata(Metadata metadata, String expectedCode,
         Object expectedValue, boolean expectedFlag) {

      Assert.assertEquals("le code de la métadonnée '" + expectedCode
            + "' est inattendu", expectedCode, metadata.getCode());

      Assert.assertEquals("la valeur de la métadonnée '" + expectedCode
            + "' est inattendue", expectedValue, metadata.getValue());

      Assert.assertEquals("le flag de la métadonnée " + expectedCode
            + " est inattendu", expectedFlag, metadata.isFlag());

   }
}
