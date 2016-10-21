package fr.urssaf.image.sae.test.divers.dfce;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caucho.hessian.client.HessianConnectionException;
import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod-gnt.xml" })
public class StatsTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(StatsTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Autowired
   private DFCEConnection dfceConnection;
   
   @Test
   @Ignore
   public void countByDays() throws SearchQueryParseException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      Map<String, Integer> map = new TreeMap<String, Integer>();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      SimpleDateFormat formatterJJMMAAAA = new SimpleDateFormat("dd/MM/yyyy");
      
      //String requeteLucene = "iti:d00b4f20-d1fd-11e3-907e-f8b156992d8b";
      //String requeteLucene = "SM_ARCHIVAGE_DATE:[20140501 TO 20140526]";
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20140603 TO 20140604]";
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         String cle = formatter.format(doc.getArchivageDate());
         int value = 1;
         if (map.containsKey(cle)) {
            value = map.get(cle).intValue() + 1;
         }
         map.put(cle, value);
      }
      
      // boucle de restitution
      Iterator<Entry<String, Integer>> iterRes = map.entrySet().iterator();
      while (iterRes.hasNext()) {
         Entry<String, Integer> entry = iterRes.next();
         try {
            Date date = formatter.parse(entry.getKey());
            LOGGER.debug("{} : {}", new Object[] {formatterJJMMAAAA.format(date), entry.getValue()});
         } catch (ParseException e) {
            LOGGER.error(e.getMessage());
         }
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void countByDays2() throws SearchQueryParseException {
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      for (int index = 0; index < 30; index++) {
         
         try {
            if (serviceProvider.isServerUp()) {
               Map<String, Integer> map = new TreeMap<String, Integer>();
               SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
               SimpleDateFormat formatterJJMMAAAA = new SimpleDateFormat("dd/MM/yyyy");
               
               String requeteLucene = "SM_ARCHIVAGE_DATE:[2014060" + Integer.toString(index + 1) + " TO 2014060" + Integer.toString(index + 1) + "]";
               
               LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
               final SearchService searchService = serviceProvider
                     .getSearchService();
               final Base base = serviceProvider.getBaseAdministrationService()
                     .getBase(dfceConnection.getBaseName());
               final SearchQuery searchQuery = ToolkitFactory.getInstance()
                     .createMonobaseQuery(requeteLucene, base);
               
               if (index == 0) {
                  try {
                     int sleep = 120;
                     LOGGER.debug("Sleep de {} secondes", sleep);
                     Thread.sleep(sleep * 1000);
                  } catch (InterruptedException e) {
                     LOGGER.error("Probleme dans le sleep : {}", e.getMessage());
                  }
               }
               
               Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
               
               // boucle de comptage
               while (iterateur.hasNext()) {
                  Document doc = iterateur.next();
                  String cle = formatter.format(doc.getArchivageDate());
                  int value = 1;
                  if (map.containsKey(cle)) {
                     value = map.get(cle).intValue() + 1;
                  }
                  map.put(cle, value);
               }
               
               if (!map.isEmpty()) {
                  // boucle de restitution
                  Iterator<Entry<String, Integer>> iterRes = map.entrySet().iterator();
                  while (iterRes.hasNext()) {
                     Entry<String, Integer> entry = iterRes.next();
                     try {
                        Date date = formatter.parse(entry.getKey());
                        LOGGER.debug("{} : {}", new Object[] {formatterJJMMAAAA.format(date), entry.getValue()});
                     } catch (ParseException e) {
                        LOGGER.error(e.getMessage());
                     }
                  }
               } else {
                  Date now = new Date();
                  String date = String.format("%02d/%02d/%04d", index + 1, now.getMonth(), now.getYear() + 1900);
                  LOGGER.debug("{} : 0", new Object[] {date});
               }
            } else {
               LOGGER.error("Le serveur DFCE n'est pas up");
               
               LOGGER.debug("Reconnexion à DFCE");
               serviceProvider = dfceConnectionService.openConnection();
            }
            
         } catch (HessianConnectionException ex) {
            LOGGER.error("DFCE est mort : {}", ex.getMessage());
            
            LOGGER.debug("Reconnexion à DFCE");
            serviceProvider = dfceConnectionService.openConnection();
         } 
      }
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void rechercheDocParCodeRnd() throws SearchQueryParseException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      String requeteLucene = "SM_DOCUMENT_TYPE:\"6.H.X.X.X\" AND SM_CREATION_DATE:[20101001 TO 20141231]";
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         String id = doc.getUuid().toString();
         LOGGER.debug("id: {}", id);
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void rechercheDocInexistant() throws SearchQueryParseException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      String requeteLucene = "SM_CREATION_DATE:20120713";
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      long nbDocsTrouve = 0;
      long nbDocsInexistant = 0;
      
      StoreService storeService = serviceProvider.getStoreService();
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         if (storeService.getDocumentFile(doc) == null) {
            String id = doc.getUuid().toString();
            LOGGER.debug("id: {}", id);
            nbDocsInexistant++;
         }
      }
      LOGGER.debug("{} docs inexistant sur {}", new Object[] {nbDocsInexistant, nbDocsTrouve});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void rechercheParIterateur() throws SearchQueryParseException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      //String requeteLucene = "SM_DOCUMENT_TYPE:1.2.1.3.1 AND SM_CREATION_DATE:[20141101 TO 20141117]";
      String requeteLucene = "(SM_DOCUMENT_TYPE:1.2.2.4.12 OR SM_DOCUMENT_TYPE:3.1.3.1.2) AND SM_CREATION_DATE:[20120101 TO 20141202] AND atr:PRODOCS";
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(500);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      long nbDocsTrouve = 0;
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         String id = doc.getUuid().toString();
         String titre = doc.getTitle();
         String codeRnd = doc.getType();
         LOGGER.debug("id: {} -> {} -> {}", new String[] { id, codeRnd, titre});
      }
      LOGGER.debug("{} docs trouves", new Object[] {nbDocsTrouve});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void consultationMeta() {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      UUID idDoc = UUID.fromString("7126a84e-c761-4364-899b-89585bd31b88");
      
      LOGGER.debug("Consultation des metadonnees du document : {}", idDoc);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());

      // recupere le document sans le binaire
      Document doc = searchService.getDocumentByUUID(base, idDoc);
      
      if (doc != null) {
         
         List<Criterion> liste = doc.getAllCriterions();
         
         for (Criterion critere : liste) {
            LOGGER.debug("{} : {}", new Object[] { critere.getCategoryName(), critere.getWord() });
         }
      } else {
         LOGGER.debug("Document non trouve : ", new Object[] {idDoc});
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void rechercherDocSansDomaine() throws SearchQueryParseException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      boolean verify = false;
      
      dfceConnection.setTimeout(120000);
      
      // calcule la date du jour
      Date now = new GregorianCalendar().getTime();
      SimpleDateFormat formatterDtFin = new SimpleDateFormat("yyyyMMdd");
      String dateFin = formatterDtFin.format(now);
      
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20120101 TO " + dateFin + "]";
      //String requeteLucene = "SM_ARCHIVAGE_DATE:[20120101 TO 20121231]";
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      int pasExecution = 10000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      long nbDocsTrouve = 0;
      long nbDocsSansDomaine = 0;
      long nbDocsDomaineCotisant = 0;
      long nbDocsDomaineComptable = 0;
      long nbDocsDomaineRH = 0;
      long nbDocsDomaineTechnique = 0;
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryCotisant = base.getBaseCategory("cot");
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         BaseCategory categoryRH = base.getBaseCategory("drh");
         BaseCategory categoryTechnique = base.getBaseCategory("dte");
         
         List<Criterion> metaCotisant = doc.getCriterions(categoryCotisant);
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         List<Criterion> metaRH = doc.getCriterions(categoryRH);
         List<Criterion> metaTechnique = doc.getCriterions(categoryTechnique);
         if (metaCotisant.isEmpty() && metaComptable.isEmpty() && metaRH.isEmpty() && metaTechnique.isEmpty()) {
            nbDocsSansDomaine++;
            LOGGER.debug("id doc : {}, date d'archivage : {}", new String[] {doc.getUuid().toString(), formatter.format(doc.getArchivageDate())});
            if (verify) {
               if (searchService.getDocumentByUUID(base, doc.getUuid()) == null) {
                  // doc inexistant donc il ne faut plus le compter
                  nbDocsSansDomaine--;
                  nbDocsTrouve--;
               }
            }
            
         } else if (metaCotisant.size() == 1 && Boolean.TRUE.equals(metaCotisant.get(0).getWord())) {
            nbDocsDomaineCotisant++;
         } else if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
         } else if (metaRH.size() == 1 && Boolean.TRUE.equals(metaRH.get(0).getWord())) {
            nbDocsDomaineRH++;
         } else if (metaTechnique.size() == 1 && Boolean.TRUE.equals(metaTechnique.get(0).getWord())) {
            nbDocsDomaineTechnique++;
         }
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      LOGGER.debug("{} docs trouves", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine cotisant", new Object[] {nbDocsDomaineCotisant});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
      LOGGER.debug("{} docs dans le domaine RH", new Object[] {nbDocsDomaineRH});
      LOGGER.debug("{} docs dans le domaine technique", new Object[] {nbDocsDomaineTechnique});
      LOGGER.debug("{} docs sans domaine", new Object[] {nbDocsSansDomaine});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void rechercherDocSicomor() throws IOException, SearchQueryParseException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20120101 TO 20150228]";
      int pasExecution = 1000;
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = serviceProvider
            .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      // pas d'iteration
      searchQuery.setSearchLimit(pasExecution);
      
      Iterator<Document> iterateur = searchService.createDocumentIterator(searchQuery);
      long nbDocsTrouve = 0;
      long nbDocsDomaineComptable = 0;
      long nbPagesTotal = 0;
      long nbPagesMin = Long.MAX_VALUE;
      long nbPagesMax = 0;
      long tailleTotal = 0;
      long tailleMin = Long.MAX_VALUE;
      long tailleMax = 0;
      double montantDevisMin = Double.MAX_VALUE;
      double montantDevisMax = 0;
      double montantRegleMin = Double.MAX_VALUE;
      double montantRegleMax = 0;
      
      FileOutputStream outputStream = new FileOutputStream("c:/tmp/stat-sicomor.csv");
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         BaseCategory categoryComptable = base.getBaseCategory("cpt");
         BaseCategory categoryNbPages = base.getBaseCategory("nbp");
         BaseCategory categoryDocEnAed = base.getBaseCategory("dar");
         BaseCategory categoryMontantDevis = base.getBaseCategory("mde");
         BaseCategory categoryMontantRegle = base.getBaseCategory("mre");
         
         List<Criterion> metaComptable = doc.getCriterions(categoryComptable);
         if (metaComptable.size() == 1 && Boolean.TRUE.equals(metaComptable.get(0).getWord())) {
            nbDocsDomaineComptable++;
            
            List<Criterion> metaNbPages = doc.getCriterions(categoryNbPages);
            if (metaNbPages.size() == 1) {
               long nbPages = (Integer) metaNbPages.get(0).getWord();
               nbPagesTotal += nbPages;
               if (nbPages < nbPagesMin) {
                  nbPagesMin = nbPages;
               }
               if (nbPages > nbPagesMax) {
                  nbPagesMax = nbPages;
               }
            }
            
            if (metaNbPages.size() == 1) {
               tailleTotal += doc.getSize();
               if (doc.getSize() < tailleMin) {
                  tailleMin = doc.getSize();
               }
               if (doc.getSize() > tailleMax) {
                  tailleMax = doc.getSize();
               }
            }
            
            List<Criterion> metaDocEnAed = doc.getCriterions(categoryDocEnAed);
            boolean docArchivable = false;
            if (metaDocEnAed.size() == 1) {
               docArchivable = (Boolean) metaDocEnAed.get(0).getWord();
            }
            
            List<Criterion> metaMontantDevis = doc.getCriterions(categoryMontantDevis);
            Double montantDevis = null;
            if (metaMontantDevis.size() == 1) {
               montantDevis = (Double) metaMontantDevis.get(0).getWord();
               if (montantDevis < montantDevisMin) {
                  montantDevisMin = montantDevis;
               }
               if (montantDevis > montantDevisMax) {
                  montantDevisMax = montantDevis;
               }
            } 
            
            List<Criterion> metaMontantRegle = doc.getCriterions(categoryMontantRegle);
            Double montantRegle = null;
            if (metaMontantRegle.size() == 1) {
               montantRegle = (Double) metaMontantRegle.get(0).getWord();
               if (montantRegle < montantRegleMin) {
                  montantRegleMin = montantRegle;
               }
               if (montantRegle > montantRegleMax) {
                  montantRegleMax = montantRegle;
               }
            }

            StringBuffer buffer = new StringBuffer();
            buffer.append(doc.getUuid().toString());
            buffer.append(';');
            buffer.append(((Integer) metaNbPages.get(0).getWord()).intValue());
            buffer.append(';');
            buffer.append(calculerTaille(doc.getSize()));
            buffer.append(';');
            buffer.append(docArchivable);
            buffer.append(';');
            buffer.append(montantDevis);
            buffer.append(';');
            buffer.append(montantRegle);
            buffer.append('\n');
            outputStream.write(buffer.toString().getBytes());
         } 
         
         if (nbDocsTrouve % pasExecution == 0) {
            LOGGER.debug("En cours : {} docs trouves", new Object[] {nbDocsTrouve});
         }
      }
      
      outputStream.close();
      
      LOGGER.debug("{} docs trouves", new Object[] {nbDocsTrouve});
      LOGGER.debug("{} docs dans le domaine comptable", new Object[] {nbDocsDomaineComptable});
      LOGGER.debug("{} de pages moyenne", new Object[] {nbPagesTotal / nbDocsDomaineComptable});
      LOGGER.debug("{} de pages min", new Object[] {nbPagesMin});
      LOGGER.debug("{} de pages max", new Object[] {nbPagesMax});
      LOGGER.debug("{} de taille moyenne", new Object[] { calculerTaille(tailleTotal / nbDocsDomaineComptable)});
      LOGGER.debug("{} de taille min", new Object[] { calculerTaille(tailleMin)});
      LOGGER.debug("{} de taille max", new Object[] { calculerTaille(tailleMax)});
      LOGGER.debug("{} de montant devis min", new Object[] { montantDevisMin });
      LOGGER.debug("{} de montant devis max", new Object[] { montantDevisMax });
      LOGGER.debug("{} de montant regle min", new Object[] { montantRegleMin });
      LOGGER.debug("{} de montant regle max", new Object[] { montantRegleMax });
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void getDomaineDoc() throws IOException {
      
      long nbDocs = 0;
      long nbDocsDomaineAMettreDomaineCoti = 0;
      long nbDocsDomaineAMettreDomaineTech = 0;
      long nbDocsInexistant = 0;
      try {
         BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:/tmp/20150312_ventilation_date_archivage.csv")));
         
         LOGGER.debug("Ouverture de la connexion à DFCE");
         ServiceProvider serviceProvider = dfceConnectionService.openConnection();
         
         final SearchService searchService = serviceProvider
               .getSearchService();
         
         final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
         
         BaseCategory categorieCse = base.getBaseCategory("cse");
         
         // boucle de suppression
         String line = in.readLine();
         while (line != null) {
            String uuid = line;
            
            Document doc = searchService.getDocumentByUUID(base, UUID.fromString(uuid));
            if (doc != null) {
               String contratService = "";
               if (!doc.getCriterions(categorieCse).isEmpty()) {
                  contratService = (String) doc.getCriterions(categorieCse).get(0).getWord();
               }
               if (doc.getType().equals("7.7.8.8.1") && "SAE".equals(contratService)) {
                  nbDocsDomaineAMettreDomaineTech++;
               } else {
                  nbDocsDomaineAMettreDomaineCoti++;
               }
            } else {
               nbDocsInexistant++;
            }
            nbDocs++;
            line = in.readLine();
         }
         
         in.close();
         
         LOGGER.debug("Nombre de docs dans le fichier : {}", nbDocs);
         LOGGER.debug("Nombre de docs inexistant : {}", nbDocsInexistant);
         LOGGER.debug("Nombre de docs a mettre dans le domaine cotisant : {}", nbDocsDomaineAMettreDomaineCoti);
         LOGGER.debug("Nombre de docs a mettre dans le domaine technique : {}", nbDocsDomaineAMettreDomaineTech);
         
         
         LOGGER.debug("Fermeture de la connexion à DFCE");
         serviceProvider.disconnect();
      } catch (RuntimeException ex) {
         LOGGER.error("Erreur lors de la recuperation {} ", new Object[] {ex.getMessage()});
         throw ex;
      }
   }
   
   private String calculerTaille(long tailleFichier) {
      StringBuffer retour = new StringBuffer();
      long tailleKo = tailleFichier / 1024;
      long tailleMo = tailleKo / 1024;
      long tailleGo = tailleMo / 1024;
      if (tailleGo > 0) {
         retour.append(tailleGo);
         retour.append(" Go");
      } else if (tailleMo > 0) {
         retour.append(tailleMo);
         retour.append(" Mo");
      } else if (tailleKo > 0) {
         retour.append(tailleKo);
         retour.append(" Ko");
      } else {
         retour.append(tailleFichier);
         retour.append(" o");
      }
      return retour.toString();
   }
}
