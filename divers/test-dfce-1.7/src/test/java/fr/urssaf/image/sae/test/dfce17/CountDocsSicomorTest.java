package fr.urssaf.image.sae.test.dfce17;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.SearchQueryParseException;

@RunWith(BlockJUnit4ClassRunner.class)
public class CountDocsSicomorTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(CountDocsSicomorTest.class);
   
   // Integration cliente GNT
   //private String url = "http://hwi69intgntappli1.gidn.recouv:8080/dfce-webapp/toolkit/";
   //private String nomBase = "GNT-INT";
   
   // Integration cliente GNS
   //private String url = "http://hwi69intgnsapp1.gidn.recouv:8080/dfce-webapp/toolkit/";
   //private String nomBase = "SAE-INT";
   
   // Integration nationale GNT
   //private String url = "http://hwi69gingntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String nomBase = "GNT-INT";
   
   // Integration nationale GNS
   //private String url = "http://hwi69ginsaeappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String nomBase = "SAE-INT";
   
   // Validation nationale GNT
   //private String url = "http://hwi69givngntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String nomBase = "GNT-GIVN";
   
   // Validation nationale GNS
   //private String url = "http://hwi69givnsaeappli.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String nomBase = "SAE-GIVN";
   
   // Prod GNT
   //private String url = "http://hwi69gntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String nomBase = "GNT-PROD";
   
   // Prod GNS
   private String url = "http://hwi69saeappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   private String nomBase = "SAE-PROD";

   @Test
   public void countDocsSicomor() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      String[] codesRnd = { "6.1.3.1.1",
            "6.1.3.2.1",
            "6.1.3.4.2",
            "6.1.3.6.1",
            "6.1.4.1.1",
            "6.1.4.1.8",
            "8.2.1.4.3",
            "8.1.2.1.2",
            "8.A.X.X.X",
            "6.1.3.1.2"
      };
      List<String> listeCodeRnd = Arrays.asList(codesRnd);
   
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      provider.connect("_ADMIN", "DOCUBASE", url, 3 * 60 * 1000);
      
      String requeteLucene = "cpt:true AND sco:* AND (SM_DOCUMENT_TYPE:6.1.3.1.1 OR SM_DOCUMENT_TYPE:6.1.3.2.1 OR SM_DOCUMENT_TYPE:6.1.3.4.2"
         + " OR SM_DOCUMENT_TYPE:6.1.3.6.1 OR SM_DOCUMENT_TYPE:6.1.4.1.1 OR SM_DOCUMENT_TYPE:6.1.4.1.8 OR SM_DOCUMENT_TYPE:8.2.1.4.3 OR SM_DOCUMENT_TYPE:8.1.2.1.2"
         + " OR SM_DOCUMENT_TYPE:8.A.X.X.X OR SM_DOCUMENT_TYPE:6.1.3.1.2) ";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = provider
            .getSearchService();
      final Base base = provider.getBaseAdministrationService()
            .getBase(nomBase);
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         BaseCategory categorySiteComptable = base.getBaseCategory("sco");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
            if (!listeCodeRnd.contains(doc.getType())) {
               LOGGER.debug("Code RND non reconnu : {} pour le doc {}", new Object[] {doc.getType(), doc.getUuid().toString()});
            }
            
            List<Criterion> metaSiteComptable = doc.getCriterions(categorySiteComptable);
            if (metaSiteComptable.size() == 1 && StringUtils.isEmpty((String) metaSiteComptable.get(0).getWord())) {
               LOGGER.debug("Site comptable vide pour le doc {}", new Object[] {doc.getUuid().toString()});
            }
            
            /*Document existingDoc = searchService.getDocumentByUUID(base, doc.getUuid());
            
            if (existingDoc == null) {
               LOGGER.error("Le doc {} n'existe pas", new Object[] {doc.getUuid().toString()});
            } else {
               
               InputStream stream = provider.getStoreService().getDocumentFile(existingDoc);
               if (stream == null) {
                  LOGGER.error("Le doc {} n'existe pas", new Object[] {doc.getUuid().toString()});
               }
               try {
                  provider.getStoreService().deleteDocument(doc.getUuid());
               } catch (FrozenDocumentException ex) {
                  
               }
            }*/
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void countDocsSicomorBySiteComptable() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      Map<String, Integer> bySiteComptable = new HashMap<String, Integer>();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      provider.connect("_ADMIN", "DOCUBASE", url, 3 * 60 * 1000);
      
      String requeteLucene = "cpt:true AND sco:* AND (SM_DOCUMENT_TYPE:6.1.3.1.1 OR SM_DOCUMENT_TYPE:6.1.3.2.1 OR SM_DOCUMENT_TYPE:6.1.3.4.2"
         + " OR SM_DOCUMENT_TYPE:6.1.3.6.1 OR SM_DOCUMENT_TYPE:6.1.4.1.1 OR SM_DOCUMENT_TYPE:6.1.4.1.8 OR SM_DOCUMENT_TYPE:8.2.1.4.3 OR SM_DOCUMENT_TYPE:8.1.2.1.2"
         + " OR SM_DOCUMENT_TYPE:8.A.X.X.X OR SM_DOCUMENT_TYPE:6.1.3.1.2) ";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = provider
            .getSearchService();
      final Base base = provider.getBaseAdministrationService()
            .getBase(nomBase);
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         BaseCategory categorySiteComptable = base.getBaseCategory("sco");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
            List<Criterion> metaSiteComptable = doc.getCriterions(categorySiteComptable);
            if (metaSiteComptable.size() == 1 && StringUtils.isEmpty((String) metaSiteComptable.get(0).getWord())) {
               LOGGER.debug("Site comptable vide pour le doc {}", new Object[] {doc.getUuid().toString()});
            } else {
               String siteComptable = (String) metaSiteComptable.get(0).getWord();
               
               if (bySiteComptable.containsKey(siteComptable)) {
                  bySiteComptable.put(siteComptable, bySiteComptable.get(siteComptable) + 1);
               } else {
                  bySiteComptable.put(siteComptable, 1);
               }
            }
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      for (String siteComptable : bySiteComptable.keySet()) {
         LOGGER.debug("{} : {}", new Object[] {siteComptable, bySiteComptable.get(siteComptable)});
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void countDocsSicomorInAll() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
   
      // calcule la date du jour
      Date now = new GregorianCalendar().getTime();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      String dateFin = formatter.format(now);
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      provider.connect("_ADMIN", "DOCUBASE", url, 3 * 60 * 1000);
      
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20150601 TO "+ dateFin + "]";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = provider
            .getSearchService();
      final Base base = provider.getBaseAdministrationService()
            .getBase(nomBase);
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void verifDocsSicomor() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      long nbDocsInexistant = 0;
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      provider.connect("_ADMIN", "DOCUBASE", url, 3 * 60 * 1000);
      
      //String requeteLucene = "SM_ARCHIVAGE_DATE:[20150601 TO "+ dateFin + "]";
      String requeteLucene = "cpt:true AND sco:* AND (SM_DOCUMENT_TYPE:6.1.3.1.1 OR SM_DOCUMENT_TYPE:6.1.3.2.1 OR SM_DOCUMENT_TYPE:6.1.3.4.2"
         + " OR SM_DOCUMENT_TYPE:6.1.3.6.1 OR SM_DOCUMENT_TYPE:6.1.4.1.1 OR SM_DOCUMENT_TYPE:6.1.4.1.8 OR SM_DOCUMENT_TYPE:8.2.1.4.3 OR SM_DOCUMENT_TYPE:8.1.2.1.2"
         + " OR SM_DOCUMENT_TYPE:8.A.X.X.X OR SM_DOCUMENT_TYPE:6.1.3.1.2) ";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = provider
            .getSearchService();
      final Base base = provider.getBaseAdministrationService()
            .getBase(nomBase);
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         BaseCategory categorySiteComptable = base.getBaseCategory("sco");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
            List<Criterion> metaSiteComptable = doc.getCriterions(categorySiteComptable);
            String siteComptable = (String) metaSiteComptable.get(0).getWord();
            
            //if (doc.getUuid().toString().toLowerCase().equals("e876fdd1-71f6-4295-8530-17acefa87f0d")) {
               Document existingDoc = searchService.getDocumentByUUID(base, doc.getUuid());
               
               if (existingDoc == null) {
                  
                     StringBuffer buffer = new StringBuffer();
                     
                     buffer.append("SM_DOCUMENT_TYPE");
                     buffer.append(':');
                     buffer.append(doc.getType());
                     buffer.append("\n");
                     
                     buffer.append("SM_ARCHIVAGE_DATE");
                     buffer.append(':');
                     buffer.append(doc.getArchivageDate());
                     buffer.append("\n");
                     
                     for (Criterion criterion : doc.getAllCriterions()) {
                        buffer.append("\n");
                        buffer.append(criterion.getCategoryName());
                        buffer.append(':');
                        buffer.append(criterion.getWord());
                     }
                     
                     LOGGER.error("Le doc {} n'existe pas (info {})", new Object[] {doc.getUuid().toString(), buffer.toString()});
                  /*} else {
                     //LOGGER.error("Le doc {} n'existe pas", new Object[] {doc.getUuid().toString()});
                  }*/
                  nbDocsInexistant++;
               } 
            //}
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs inexistant mais trouve", new Object[] {nbDocsInexistant});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
      
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void extractDocsSicomor() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("c:/tmp/docs-sicomor-" + nomBase + ".csv")));
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      provider.connect("_ADMIN", "DOCUBASE", url, 3 * 60 * 1000);
      
      String requeteLucene = "cpt:true AND sco:* AND (SM_DOCUMENT_TYPE:6.1.3.1.1 OR SM_DOCUMENT_TYPE:6.1.3.2.1 OR SM_DOCUMENT_TYPE:6.1.3.4.2"
         + " OR SM_DOCUMENT_TYPE:6.1.3.6.1 OR SM_DOCUMENT_TYPE:6.1.4.1.1 OR SM_DOCUMENT_TYPE:6.1.4.1.8 OR SM_DOCUMENT_TYPE:8.2.1.4.3 OR SM_DOCUMENT_TYPE:8.1.2.1.2"
         + " OR SM_DOCUMENT_TYPE:8.A.X.X.X OR SM_DOCUMENT_TYPE:6.1.3.1.2) ";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = provider
            .getSearchService();
      final Base base = provider.getBaseAdministrationService()
            .getBase(nomBase);
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
            out.write(doc.getUuid().toString() + ";");
            out.write(doc.getFileUUID().toString() + ";\n");
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
      
      out.close();
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void getInfosDocs() throws IOException, SearchQueryParseException {
      
      UUID idDoc = UUID.fromString("93A56DFD-BD78-4D9C-9836-BDA15C9F1FA4");
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      provider.connect("_ADMIN", "DOCUBASE", url, 3 * 60 * 1000);
      
      final SearchService searchService = provider
            .getSearchService();
      final Base base = provider.getBaseAdministrationService()
            .getBase(nomBase);
      
      Document doc = searchService.getDocumentByUUID(base, idDoc);
      String siteComptable = (String) doc.getCriterions("sco").get(0).getWord();
      String codeRnd = doc.getType();
      
      LOGGER.debug("{} {}", siteComptable, codeRnd);
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void countDocsScribe() throws IOException, SearchQueryParseException {
      
      long nbDocsTrouve = 0;
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      provider.connect("_ADMIN", "DOCUBASE", url, 3 * 60 * 1000);
      
      String requeteLucene = "cot:true AND apr:SCRIBE AND atr:PRODOCS AND cop:UR827 AND SM_DOCUMENT_TYPE:2.2.3.2.2 AND SM_ARCHIVAGE_DATE:[20151201 TO 20160131]";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = provider
            .getSearchService();
      final Base base = provider.getBaseAdministrationService()
            .getBase(nomBase);
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         iterateur.next();
         nbDocsTrouve++;
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      LOGGER.debug("{} docs total", new Object[] {nbDocsTrouve});
         
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
}
