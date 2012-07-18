package fr.urssaf.image.sae.regionalisation.dao;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import junit.framework.Assert;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-service-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SaeDocumentDaoTest {

   @Autowired
   private SaeDocumentDao dao;

   @Autowired
   private ServiceProviderSupport serviceProvider;

   @Autowired
   @Qualifier("base_regionalisation")
   private String baseName;

   private Base base;

   private UUID storeDocument(String documentTitle, String den) {

      try {

         Document document = ToolkitFactory.getInstance().createDocumentTag(
               base);

         Map<String, Object> metadatas = new HashMap<String, Object>();

         metadatas.put("apr", "ADELAIDE");
         metadatas.put("cop", "CER69");
         metadatas.put("cog", "UR750");
         metadatas.put("vrn", "11.1");
         metadatas.put("dom", "2");
         metadatas.put("act", "3");
         metadatas.put("nbp", "2");
         metadatas.put("ffi", "fmt/1354");
         metadatas.put("cse", "ATT_PROD_001");
         metadatas.put("dre", java.sql.Date.valueOf("2007-07-12"));
         metadatas.put("dfc", java.sql.Date.valueOf("2007-07-12"));
         metadatas.put("den", den);

         String documentType = "pdf";
         String codeRND = "2.3.1.1.12";
         String title = "Attestation de vigilance";

         document.setCreationDate(new Date());
         document.setTitle(title);
         document.setType(codeRND);
         document.setLifeCycleReferenceDate(new Date());

         for (Entry<String, Object> entry : metadatas.entrySet()) {
            BaseCategory baseCategory = base.getBaseCategory(entry.getKey());
            document.addCriterion(baseCategory, entry.getValue());

         }

         String content = RandomStringUtils.random(1000);

         InputStream docContent = IOUtils.toInputStream(content);
         return serviceProvider.getStoreService().storeDocument(document,
               documentTitle, documentType, docContent).getUuid();

      } catch (TagControlException e) {
         throw new NestableRuntimeException(e);
      }

   }

   private List<UUID> uuids;

   private String den;

   @Before
   public void before() {

      den = UUID.randomUUID().toString();

      serviceProvider.connect();

      base = serviceProvider.getBaseAdministrationService().getBase(baseName);

      uuids = new ArrayList<UUID>();

      uuids.add(storeDocument("doc1", den));
      uuids.add(storeDocument("doc2", den));
      uuids.add(storeDocument("doc3", den));
      uuids.add(storeDocument("doc4", den));

   }

   @After
   public void after() {

      try {

         for (UUID paramUUID : uuids) {

            try {
               serviceProvider.getStoreService().deleteDocument(paramUUID);
            } catch (FrozenDocumentException e) {
               throw new NestableRuntimeException(e);
            }

         }

      } finally {

         serviceProvider.disconnect();
      }

   }

   @Test
   public void update() {

      Document document = serviceProvider.getSearchService().getDocumentByUUID(
            base, uuids.get(0));

      document.setTitle("new title");

      dao.update(document);

      document = serviceProvider.getSearchService().getDocumentByUUID(base,
            uuids.get(0));

      Assert.assertEquals("le titre du document est inattendu", "new title",
            document.getTitle());

   }

   private void assertDocument(List<Document> documents, UUID uuid) {

      for (Document document : documents) {

         if (document.getUuid().equals(uuid)) {
            return;
         }
      }

      Assert.fail("le document n°" + uuid + " n'a pas été trouvé");

   }

   @Test
   public void getDocuments() {

      String lucene = "den:" + den;
      List<Document> documents = dao.getDocuments(lucene);

      Assert.assertEquals("le nombre de document est inattendu", 4, documents
            .size());

      for (UUID uuid : uuids) {
         assertDocument(documents, uuid);
      }
   }

}
