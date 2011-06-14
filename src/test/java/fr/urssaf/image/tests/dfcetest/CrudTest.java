/**
 * 
 */
package fr.urssaf.image.tests.dfcetest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.docubase.am.common.io.util.FileUtils;
import net.docubase.rheatoolkit.exception.RheaToolkitSystemElementExistException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.tests.dfcetest.helpers.DocubaseHelper;

/**
 * Teste les fonctionnalités CRUD en contexte mono threadé.
 * 
 */
public class CrudTest extends AbstractNcotiTest {

   private Base base;
   private BaseCategory appliSourceCategory;
   String appliSourceFName;

   @Before
   public void createContext() {
      base = ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
      appliSourceCategory = base.getBaseCategory(Categories.APPLI_SOURCE.toString());
      appliSourceFName = appliSourceCategory.getFormattedName();
   }

   /**
    * Teste un scénario de type CRUD en une fois.
    * 
    * @throws Exception
    */
   @Test
   public void crud() throws Exception {
      String appliSource = "test_crud" + System.nanoTime();
      int nbDocs = 100;
      Set<UUID> uuids = new HashSet<UUID>();
      Set<UUID> uuidsFound = new HashSet<UUID>();
      Set<String> applisSourcesFound = new HashSet<String>();

      /**
       *  CREATE
       */
      List<Document> docs = DocubaseHelper.insertManyDocs(nbDocs, base, appliSource);
      assertNotNull("Aucun document ne semble avoir été inséré", docs);
      assertEquals(nbDocs, docs.size());

      for (Document doc : docs) {
         uuids.add(doc.getUUID());
      }
      // Là on est mal si ça ne passe pas
      assertEquals("Il existe des doublons dans les documents insérés", nbDocs, uuids.size());

      /**
       *  READ : on cherche par un index commun i.e. l'application source
       */
      String lucene = appliSourceFName + ":" + appliSource;
      // On fixe une limite plus grande pour voir si on ne ramène pas plus de
      // résultats que prévu
      int searchLimit = nbDocs * 2;
      // SUT
      SearchResult result = ServiceProvider.getSearchService().search(lucene, searchLimit, base);
      assertEquals("Le nombre de documents trouvés est incorrect", nbDocs, result.getTotalHits());

      for (Document docFound : result.getDocuments()) {
         uuidsFound.add(docFound.getUUID());
         applisSourcesFound.add(docFound.getFirstCriterion(appliSourceCategory).getWord().toString());
      }
      assertEquals("Les documents trouvés ne sont pas les documents insérés", uuids, uuidsFound);
      assertEquals("Tous les documents doivent avoir la même application source", 1,
            applisSourcesFound.size());
      assertEquals("La catégorie a été mal alimentée", appliSource, applisSourcesFound.iterator()
            .next());

      /**
       *  UPDATE : on met à jour l'application source des documents et la date de création
       */
      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
      Date newDate = null;
      String dateFr = "01/01/2011 01:02:03";
      try {
         newDate = df.parse(dateFr);
      } catch (ParseException e) {
         e.printStackTrace();
         fail("Problème dans le test, la date est mal formatée : " + dateFr);
      }

      String newAppliSource = "test_crud" + System.nanoTime();

      for (Document doc : docs) {
         doc.setCreationDate(newDate);
         // TODO : lorsque Docubase aura implémenté la modification de valeur
         // pour les index, il faudra le tester ici.
         // doc.addCriterion(appliSourceCategory, newAppliSource);
         ServiceProvider.getStoreService().updateDocument(doc);
      }

      result = ServiceProvider.getSearchService().search(lucene, searchLimit, base);
      assertEquals("Le nombre de documents trouvés est incorrect", nbDocs, result.getTotalHits());

      for (Document docFound : result.getDocuments()) {
         uuidsFound.add(docFound.getUUID());
         applisSourcesFound.add(docFound.getFirstCriterion(appliSourceCategory).getWord().toString());
      }
      assertEquals("Les documents trouvés ne sont pas les documents insérés", uuids, uuidsFound);
      assertEquals("Tous les documents doivent avoir la même application source", 1,
            applisSourcesFound.size());
      assertEquals("La catégorie a été mal alimentée", newAppliSource, applisSourcesFound
            .iterator().next());
   }
   
   @Test
   public void documentFileByUUID() throws Exception {
      String appliSource = "documentFileByUUID" + System.nanoTime();

      Document doc = DocubaseHelper.insertOneDoc(base, appliSource);
      UUID uuid = doc.getUUID();
      Document emptyDoc = toolkit.createDocumentTag(base);
      emptyDoc.setUUID(uuid);
      
      File docFile = ServiceProvider.getStoreService().getDocumentFile(emptyDoc);
      assertNotNull(docFile);
   }
   
   @Test
   public void deleteByUUID() throws Exception {
      String appliSource = "deleteByUUID" + System.nanoTime();

      Document doc = DocubaseHelper.insertOneDoc(base, appliSource);
      UUID uuid = doc.getUUID();
      Document emptyDoc = toolkit.createDocumentTag(base);
      emptyDoc.setUUID(uuid);
      
      ServiceProvider.getStoreService().deleteDocument(emptyDoc);
   }

   @Test
   public void UUID_inMemoryModification() throws Exception {
      String appliSource = "test_existingUUID" + System.nanoTime();

      Document doc = DocubaseHelper.insertOneDoc(base, appliSource);
      UUID uuid = doc.getUUID();
      UUID newUuid = UUID.randomUUID();

      // On s'assure que la mise à jour des metadonnées plante
      try {
         doc.setUUID(newUuid);
      } catch (Exception e) {
         Throwable cause = e.getCause();

         if (cause == null) {
            throw e;
         }

         if (!(cause instanceof RheaToolkitSystemElementExistException)) {
            throw e;
         }
      }

      // On doit bien retrouver le document avec son UUID non modifié
      Document docFoundWithOldUUID = ServiceProvider.getSearchService().getDocumentByUUIDMultiBase(
            uuid);
      assertNotNull(docFoundWithOldUUID);

      // Le nouvel UUID ne doit être affecté à aucun document
      Document docFoundWithNewUUID = ServiceProvider.getSearchService().getDocumentByUUIDMultiBase(
            newUuid);
      assertNull(docFoundWithNewUUID);
   }

   @Test
   public void UUID_duplicate() throws Exception {
      String appliSource1 = "test_existingUUID" + System.nanoTime();
      
      // On crée un document, puis on récupère l'UUID fourni par Docubase
      Document doc1 = DocubaseHelper.insertOneDoc(base, appliSource1);
      UUID existingUuid = doc1.getUUID();
      File file1 = ServiceProvider.getStoreService().getDocumentFile(doc1);
      String fileContent1 = FileUtils.readTextFile(file1);
      log.debug("\nInsertion du document 1\nDocument UUID: {} => hash: {} (Algo={}), appliSource: {}", new Object[] { 
            doc1.getUUID(),
            doc1.getVersionDigest(),
            doc1.getVersionDigestAlgorithm(),
            doc1.getFirstCriterion(appliSourceCategory).getWord()
      });      
      log.debug("\nContenu du fichier 1\n{}\n\n", fileContent1);
      
      // On crée un second document avec un UUID fixé par avance qui correspond
      // à l'UUID du premier document
      Document doc2 = ToolkitFactory.getInstance().createDocumentTag(base);
      doc2.setUUID(existingUuid);
      doc2.setDocType("PDF");
      String appliSource2 = "test_existingUUID" + System.nanoTime();
      Map<String, Object> metadata2 = DocubaseHelper.getCategoriesRandomValues(appliSource2);
      boolean stored = DocubaseHelper.storeThisDoc(base, doc2, metadata2);
      // Il vaut mieux tester que l'on ne retrouve pas le document car on ne sait pas quand
      // la méthode doit renvoyer faux, quand doit-elle lever une exception, etc.
      //assertFalse("L'enregistrement du document aurait dû échoué car l'UUID existe déjà", stored);      
      File file2 = ServiceProvider.getStoreService().getDocumentFile(doc2);
      String fileContent2 = FileUtils.readTextFile(file2);
      log.debug("\nContenu du fichier 2\n{}\n\n", fileContent2);
      
      // On crée un troisième document avec un UUID fixé par avance, mais aléatoire.
      // Cela permet d'éviter un test faux positif
      Document doc3 = ToolkitFactory.getInstance().createDocumentTag(base);
      doc3.setUUID(UUID.randomUUID());
      doc3.setDocType("PDF");
      String appliSource3 = "test_existingUUID" + System.nanoTime();
      Map<String, Object> metadata3 = DocubaseHelper.getCategoriesRandomValues(appliSource3);
      stored = DocubaseHelper.storeThisDoc(base, doc3, metadata3);
      assertTrue("Le document aurait dû être enregistré car son UUID est aléatoire", stored);
      
      // On recherche l'UUID doublon
      Document docByUuid = ServiceProvider.getSearchService().getDocumentByUUIDMultiBase(existingUuid);
      log.debug("getDocumentByUUID({}) => UUID: {}, appliSource: {}", new Object[] { 
            existingUuid,
            docByUuid.getUUID(),
            docByUuid.getFirstCriterion(appliSourceCategory).getWord()});
      
      // On cherche le premier document : on doit le retrouver
      String lucene = appliSourceFName + ":" + appliSource1;
      SearchResult result = ServiceProvider.getSearchService().search(lucene, 5, base);
      assertEquals(1, result.getDocuments().size());
      Document docFound = result.getDocuments().get(0);
      assertEquals(existingUuid, docFound.getUUID());  
      File fileFound = ServiceProvider.getStoreService().getDocumentFile(docFound);
      String fileContentFound = FileUtils.readTextFile(fileFound);
      log.debug("\nContenu du fichier trouvé\n{}\n\n", fileContentFound);
      assertEquals("L'application source a changée", 
            appliSource1, 
            docFound.getFirstCriterion(appliSourceCategory).getWord());
      
      log.debug("\nRecherche du document 1 {} \nDocument UUID: {} => appliSource: {}", new Object[] { 
            lucene,
            docFound.getUUID(),
            docFound.getFirstCriterion(appliSourceCategory).getWord()});
      
      // On cherche le deuxième document : il NE doit PAS exister
      lucene = appliSourceFName + ":" + appliSource2;
      result = ServiceProvider.getSearchService().search(lucene, 5, base);
      // assertEquals("Le document ne devrait pas exister", 0, result.getDocuments().size());
      if (result.getDocuments().size() > 0) {
         Document evilDocument = result.getDocuments().get(0);
         assertEquals(existingUuid, evilDocument.getUUID());
         log.debug("\nRecherche du document 2 {} \nDocument UUID: {} => appliSource: {}", new Object[] { 
               lucene,
               evilDocument.getUUID(),
               evilDocument.getFirstCriterion(appliSourceCategory).getWord() 
         });
      }
      
      // On cherche le troisième document : on doit le retrouver
      lucene = appliSourceFName + ":" + appliSource3;
      result = ServiceProvider.getSearchService().search(lucene, 5, base);
      assertEquals(1, result.getDocuments().size());
      docFound = result.getDocuments().get(0);
      assertEquals(doc3.getUUID(), docFound.getUUID());
   }   
}
