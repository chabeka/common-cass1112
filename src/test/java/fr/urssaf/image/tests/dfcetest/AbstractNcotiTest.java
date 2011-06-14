package fr.urssaf.image.tests.dfcetest;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.docubase.toolkit.Authentication;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.tests.dfcetest.helpers.DocubaseHelper;
import fr.urssaf.image.tests.dfcetest.helpers.NcotiHelper;

public abstract class AbstractNcotiTest {

   /**
    * Chaque classe fille a son logger.
    */
   protected Logger log = LoggerFactory.getLogger(getClass());

   /**
    * Nom de la base GED pour ce test. Tronqué à 8 caractères dans Docubase. On
    * laisse une valeur en dur pour éviter de créer trop de base car il
    * semblerait que la version 0.9.1 n'apprécie pas.
    */
   protected static final String BASE_ID = "NCOTI_99";// + System.nanoTime();
   protected static ToolkitFactory toolkit;
   protected static final String ADM_LOGIN = "_ADMIN";
   protected static final String ADM_PASSWORD = "DOCUBASE";
   protected static final Integer DOMAIN_ID = Integer.valueOf(1);
   protected static final String AMF_HOST = "cer69-ds4int";
   protected static final Integer AMF_PORT = Integer.valueOf(4020);

   @BeforeClass
   public static void beforeClass() throws Exception {
      boolean openSession = Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, AMF_HOST, AMF_PORT,
            DOMAIN_ID);
      if (!openSession) {
         fail("Impossible d'ouvrir une session");
      }
      NcotiHelper.createOrReplaceBase(BASE_ID);
      toolkit = ToolkitFactory.getInstance();
   }

   @AfterClass
   public static void afterClass() {
      DocubaseHelper.dropBase(BASE_ID);

      if (Authentication.isSessionActive()) {
         Authentication.closeSession();
      }
   }

   /**
    * Déclare égaux, deux documents ayant le même UUID et le même hash.
    * On pourrait aller plus loin en implémentant un equals dans une implémentation de Document.
    * @param expectedDoc
    * @param actualDoc
    */
   public static void assertDocumentEquals(Document expectedDoc, Document actualDoc) {
      assertEquals("Les UUID des documents sont différents", 
            expectedDoc.getUUID(), actualDoc.getUUID());
      assertEquals("Les hash des fichiers sont différents", 
            expectedDoc.getVersionDigest(), actualDoc.getVersionDigest());
      assertEquals("Les titres des documents sont différents", 
            expectedDoc.getDocTitle(), actualDoc.getDocTitle());   
   }
   

   /**
    * Deux collections de Document seront réputées égales si leurs UUID et les hash correspondants
    * sont identiques.
    * @param expectedDocs
    * @param actualDocs
    */
   public static void assertDocumentsEquals(Collection<Document> expectedDocs, Collection<Document> actualDocs) {
      Map<UUID, String>expectedDigests = new HashMap<UUID, String>();
      
      assertEquals("Le nombre de documents est différent", expectedDocs.size(), actualDocs.size());
      
      for (Document doc : expectedDocs) {
         expectedDigests.put(doc.getUUID(), doc.getVersionDigest());
      }
      
      for (Document actualDoc : actualDocs) {
         String expectedDigest = expectedDigests.get(actualDoc.getUUID());
         
         if (expectedDigest == null) {
            fail("L'UUID " + actualDoc.getUUID() + " ne fait pas partie des documents attendus");
         }
         assertEquals("Les hash du document " + actualDoc.getUUID() + " ne correspondent pas", 
               expectedDigest, actualDoc.getVersionDigest());
      }
   } 
}
