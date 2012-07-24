package fr.urssaf.image.sae.regionalisation.support;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.dao.MetadataDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-dfce-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ServiceProviderSupportTest {

   @Autowired
   private ServiceProviderSupport support;

   @Before
   public void before() {

      support.connect();

   }

   @After
   public void after() {

      support.disconnect();

   }

   @Test
   public void updateCriterion() {

      Document document = ToolkitFactory.getInstance().createDocumentTag(
            support.getBase());

      Map<String, Object> metadatas = new HashMap<String, Object>();

      metadatas.put("nne", "148032541101650");
      metadatas.put("npe", "123856");
      metadatas.put("den", "COUTURIER GINETTE");
      metadatas.put("cv2", "4");
      metadatas.put("scv", "11");
      metadatas.put("nci", "719900");
      metadatas.put("nce", "30148032541101600");
      metadatas.put("srt", "12345678912345");
      metadatas.put("psi", "4914736610005");
      metadatas.put("nst", "000050221");
      metadatas.put("nre", "20080798");
      metadatas.put("nic", "57377");
      metadatas.put("dre", Date.valueOf("2007-07-14"));
      metadatas.put("apr", "ADELAIDE");
      metadatas.put("atr", "ATTESTATIONS");
      metadatas.put("cop", "UR750");
      metadatas.put("cog", "UR42");
      metadatas.put("sac", "CER69");
      metadatas.put("nbp", 6);

      for (Entry<String, Object> metadata : metadatas.entrySet()) {

         support.updateCriterion(document, metadata.getKey(), metadata
               .getValue());

      }

      // on vérifie que toutes les métadonnées modifiables soient bien prises en
      // compte

      Assert.assertEquals("le nombre de métadonnées est inattendu",
            MetadataDao.METADATAS.length, metadatas.size());

      for (String code : MetadataDao.METADATAS) {

         if (!metadatas.containsKey(code)) {
            Assert.fail("la métadonnée " + code + " n'est pas prise en compte");
         }
      }

   }

   private static final String METADATA = "nne";

   @Test
   public void updateCriterion_updateMetadonnee() {

      Document document = ToolkitFactory.getInstance().createDocumentTag(
            support.getBase());

      support.updateCriterion(document, METADATA, "first value");

      Assert.assertEquals("la métadonnée '" + METADATA
            + "' du document est inattendu", "first value", document
            .getSingleCriterion(METADATA).getWord());

      support.updateCriterion(document, METADATA, "second value");

      Assert.assertEquals("la métadonnée '" + METADATA
            + "' du document est inattendu", "second value", document
            .getSingleCriterion(METADATA).getWord());

   }

}
