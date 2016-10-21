package fr.urssaf.image.sae.test.divers.dfce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
public class RechercheDocsTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(RechercheDocsTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Autowired
   private DFCEConnection dfceConnection;
   
   @Test
   //@Ignore
   public void rechercheDocAvecDateReception() throws SearchQueryParseException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      // calcule la date du jour
      Date now = new GregorianCalendar().getTime();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      String dateFin = formatter.format(now);
    
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20120101 TO " + dateFin + "]";
      
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
      long nbDocsAvecDateReception = 0;
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         String id = doc.getUuid().toString();
         String titre = doc.getTitle();
         String codeRnd = doc.getType();
         
         List<Criterion> dateReception = doc.getCriterions("dre");
         if (!dateReception.isEmpty()) {
            LOGGER.debug("id: {} -> {} -> {}", new String[] { id, codeRnd, titre});
            nbDocsAvecDateReception++;
         }
      }
      LOGGER.debug("{} docs trouves sur {}", new Object[] {nbDocsAvecDateReception, nbDocsTrouve});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void countDoc() throws SearchQueryParseException {
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20150601 TO 20150615] AND cpt:true";
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
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
      }
      LOGGER.debug("{} docs trouves", new Object[] { nbDocsTrouve});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void rechercheDocsProduitV2() throws SearchQueryParseException {
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20140101 TO 20140131] AND cse:CS_V2";
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
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
         LOGGER.info(doc.getCriterions("iti").get(0).getWordValue());
         nbDocsTrouve++;
      }
      LOGGER.debug("{} docs trouves", new Object[] { nbDocsTrouve});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   } 
   
   @Test
   //@Ignore
   public void rechercheDocPd31EtQd31() throws SearchQueryParseException, IOException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      //DateTime dateDebut = new DateTime().withDate(2015, 10, 23).withTime(0, 0, 0, 0);
      DateTime dateDebut = new DateTime().withDate(2016, 1, 5).withTime(0, 0, 0, 0);
      //DateTime dateFin = new DateTime().withDate(2015, 11, 2).withTime(23, 59, 59, 999);
      DateTime dateFin = new DateTime().withDate(2016, 1, 6).withTime(23, 59, 59, 999);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
      
      // Exemple de code produit et code traitement
      //CodeProduitV2:PD31B
      //CodeTraitementV2:RD31.L04
      
      Map<String, String> produitsEnErreur = new HashMap<String, String>();
      produitsEnErreur.put("RD31.L00", "QD31A");
      produitsEnErreur.put("RD31.L01", "QD31B");
      produitsEnErreur.put("RD31.L02", "QD31C");
      produitsEnErreur.put("RD31.L03", "PD31A");
      produitsEnErreur.put("RD31.L04", "PD31B");
      produitsEnErreur.put("RD31.L05", "PD31C");
      produitsEnErreur.put("QD31.L00", "QD31A");
      produitsEnErreur.put("QD31.L01", "QD31B");
      produitsEnErreur.put("QD31.L02", "QD31C");
      produitsEnErreur.put("PD31.L00", "PD31A");
      produitsEnErreur.put("PD31.L01", "PD31B");
      produitsEnErreur.put("PD31.L02", "PD31C");
      
      FileOutputStream outputStream = new FileOutputStream("c:/tmp/pd31EtQd31-2016.csv");
      
      DateTime dateDebutJour = dateDebut;
      DateTime dateFinJour = dateDebutJour.plusDays(1).minusMillis(1);
      while (dateFinJour.isBefore(dateFin) || dateFinJour.isEqual(dateFin)) {
         
         // effectue la requete jour par jour
          
         String requeteLucene = "SM_ARCHIVAGE_DATE:[" + formatter.format(dateDebutJour.toDate()) + " TO " + formatter.format(dateFinJour.toDate()) + "]";
         
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
         long nbDocsASupprimer = 0;
         
         // boucle de comptage
         while (iterateur.hasNext()) {
            Document doc = iterateur.next();
            nbDocsTrouve++;
            
            String id = doc.getUuid().toString();
            
            List<Criterion> metaCodeProduit = doc.getCriterions("cpr");
            List<Criterion> metaCodeTraitement = doc.getCriterions("ctr");
            List<Criterion> metaIdTraitementMasse = doc.getCriterions("iti");
            List<Criterion> metaCodeOrgaGestionnaire = doc.getCriterions("cog");
            List<Criterion> metaCodeOrgaProprietaire = doc.getCriterions("cop");
            
            // test si on a un produit V2 issu d'un traitement de masse 
            if (!metaCodeProduit.isEmpty() && !metaCodeTraitement.isEmpty() && !metaIdTraitementMasse.isEmpty()) {
               
               // on est dans le cas d'un produit V2 issu d'un traitement de masse
               String codeProduit = (String) metaCodeProduit.get(0).getWord();
               String codeTraitement = (String) metaCodeTraitement.get(0).getWord(); 
               String idTraitementMasse = (String) metaIdTraitementMasse.get(0).getWord();
               String codeOrgaGestionnaire = (String) metaCodeOrgaGestionnaire.get(0).getWord();
               String codeOrgaProprietaire = (String) metaCodeOrgaProprietaire.get(0).getWord();
               if (produitsEnErreur.containsKey(codeTraitement) && produitsEnErreur.get(codeTraitement).equals(codeProduit)) {
                  //LOGGER.debug("id: {} ({}.{}) -> traitement de masse : {}", new String[] { id, codeTraitement, codeProduit, idTraitementMasse});
                  
                  StringBuffer buffer = new StringBuffer();
                  
                  buffer.append(id);
                  buffer.append(';');
                  buffer.append(formatter.format(doc.getArchivageDate()));
                  buffer.append(';');
                  buffer.append(codeTraitement);
                  buffer.append(';');
                  buffer.append(codeProduit);
                  buffer.append(';');
                  buffer.append(codeOrgaGestionnaire);
                  buffer.append(';');
                  buffer.append(codeOrgaProprietaire);
                  buffer.append(';');
                  buffer.append(idTraitementMasse);
                  buffer.append('\n');
                  outputStream.write(buffer.toString().getBytes());
                  
                  nbDocsASupprimer++;
               }
            }
         }
         LOGGER.debug("{} docs trouves sur {}", new Object[] {nbDocsASupprimer, nbDocsTrouve});
         
         // passe au jour suivant
         dateDebutJour = dateFinJour.plusMillis(1);
         dateFinJour = dateDebutJour.plusDays(1).minusMillis(1);
      }
      
      outputStream.close();
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   
   @Test
   //@Ignore
   public void rechercheDocQd73() throws SearchQueryParseException, IOException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      DateTime dateDebut = new DateTime().withDate(2016, 1, 20).withTime(17, 0, 0, 0);
      DateTime dateFin = new DateTime().withDate(2016, 1, 21).withTime(23, 59, 59, 999);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
      
      // Exemple de code produit et code traitement
      //CodeProduitV2:PD31B
      //CodeTraitementV2:RD31.L04
      
      Map<String, String> produitsEnErreur = new HashMap<String, String>();
      produitsEnErreur.put("RD73.L00", "QD73A");
      produitsEnErreur.put("RD73.L01", "QD73B");
      
      FileOutputStream outputStream = new FileOutputStream("c:/tmp/Qd73-2016.csv");
      
      DateTime dateDebutJour = dateDebut;
      DateTime dateFinJour = dateDebutJour.plusDays(1).minusMillis(1);
      while (dateFinJour.isBefore(dateFin) || dateFinJour.isEqual(dateFin)) {
         
         // effectue la requete jour par jour
          
         String requeteLucene = "SM_ARCHIVAGE_DATE:[" + formatter.format(dateDebutJour.toDate()) + " TO " + formatter.format(dateFinJour.toDate()) + "] AND cse:CS_V2";
         
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
         long nbDocsASupprimer = 0;
         
         // boucle de comptage
         while (iterateur.hasNext()) {
            Document doc = iterateur.next();
            nbDocsTrouve++;
            
            String id = doc.getUuid().toString();
            
            List<Criterion> metaCodeProduit = doc.getCriterions("cpr");
            List<Criterion> metaCodeTraitement = doc.getCriterions("ctr");
            List<Criterion> metaIdTraitementMasse = doc.getCriterions("iti");
            List<Criterion> metaCodeOrgaGestionnaire = doc.getCriterions("cog");
            List<Criterion> metaCodeOrgaProprietaire = doc.getCriterions("cop");
            
            // test si on a un produit V2 issu d'un traitement de masse 
            if (!metaCodeProduit.isEmpty() && !metaCodeTraitement.isEmpty() && !metaIdTraitementMasse.isEmpty()) {
               
               // on est dans le cas d'un produit V2 issu d'un traitement de masse
               String codeProduit = (String) metaCodeProduit.get(0).getWord();
               String codeTraitement = (String) metaCodeTraitement.get(0).getWord(); 
               String idTraitementMasse = (String) metaIdTraitementMasse.get(0).getWord();
               String codeOrgaGestionnaire = (String) metaCodeOrgaGestionnaire.get(0).getWord();
               String codeOrgaProprietaire = (String) metaCodeOrgaProprietaire.get(0).getWord();
               if (produitsEnErreur.containsKey(codeTraitement) && produitsEnErreur.get(codeTraitement).equals(codeProduit)) {
                  //LOGGER.debug("id: {} ({}.{}) -> traitement de masse : {}", new String[] { id, codeTraitement, codeProduit, idTraitementMasse});
                  
                  StringBuffer buffer = new StringBuffer();
                  
                  buffer.append(id);
                  buffer.append(';');
                  buffer.append(formatter.format(doc.getArchivageDate()));
                  buffer.append(';');
                  buffer.append(codeTraitement);
                  buffer.append(';');
                  buffer.append(codeProduit);
                  buffer.append(';');
                  buffer.append(codeOrgaGestionnaire);
                  buffer.append(';');
                  buffer.append(codeOrgaProprietaire);
                  buffer.append(';');
                  buffer.append(idTraitementMasse);
                  buffer.append('\n');
                  outputStream.write(buffer.toString().getBytes());
                  
                  nbDocsASupprimer++;
               }
            }
         }
         LOGGER.debug("{} docs trouves sur {}", new Object[] {nbDocsASupprimer, nbDocsTrouve});
         
         // passe au jour suivant
         dateDebutJour = dateFinJour.plusMillis(1);
         dateFinJour = dateDebutJour.plusDays(1).minusMillis(1);
      }
      
      outputStream.close();
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void getStatsPd31EtQd31() throws IOException {
      
      Map<String, Integer> countDocByUR = new TreeMap<String, Integer>();
      Map<String, List<String>> countJobByUR = new TreeMap<String, List<String>>();
      
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:/tmp/pd31EtQd31-ok.csv")));
      String line = in.readLine();
      while (line != null) {
         String[] colonnes = line.split(";");
         String codeOrga = colonnes[5];
         String idJob = colonnes[6];
         
         if (!countDocByUR.containsKey(codeOrga)) {
            countDocByUR.put(codeOrga, Integer.valueOf(1));
         } else {
            countDocByUR.put(codeOrga, countDocByUR.get(codeOrga) + 1);
         }
         
         if (!countJobByUR.containsKey(codeOrga)) {
            List<String> jobs = new ArrayList<String>();
            jobs.add(idJob);
            countJobByUR.put(codeOrga, jobs);
         } else {
            if (!countJobByUR.get(codeOrga).contains(idJob)) {
               countJobByUR.get(codeOrga).add(idJob);
            }
         }
         
         // ligne suivante
         line = in.readLine();
      }
      
      Iterator<Entry<String, Integer>> iterateur = countDocByUR.entrySet().iterator();
      while (iterateur.hasNext()) {
         Entry<String, Integer> entry = iterateur.next();
         LOGGER.debug("{} : {} (soit {} docs)", new String[] { entry.getKey(), Integer.toString(countJobByUR.get(entry.getKey()).size()), entry.getValue().toString() });
      }
      
      in.close();
   }
   
   @Test
   public void countByIdJob() throws IOException {
      
      Map<String, Integer> countDocByIdJob = new LinkedHashMap<String, Integer>();
      
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:/tmp/pd31EtQd31-ok.csv")));
      String line = in.readLine();
      while (line != null) {
         String[] colonnes = line.split(";");
         String idJob = colonnes[6];
         
         if (!countDocByIdJob.containsKey(idJob)) {
            countDocByIdJob.put(idJob, Integer.valueOf(1));
         } else {
            countDocByIdJob.put(idJob, countDocByIdJob.get(idJob) + 1);
         }
         
         // ligne suivante
         line = in.readLine();
      }
      
      Iterator<Entry<String, Integer>> iterateur = countDocByIdJob.entrySet().iterator();
      while (iterateur.hasNext()) {
         Entry<String, Integer> entry = iterateur.next();
         LOGGER.debug("{};{}", new String[] { entry.getKey(), entry.getValue().toString() });
      }
      
      in.close();
   }
   
   @Test
   //@Ignore
   public void rechercheDocAvecNomFichier() throws SearchQueryParseException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
      
      String[] fichiersRecherche = {
            "FCD3LeyqzZwp_20151209071841_aad.tif",
            "FCD3LeyqzZwp_20151209071841_aai.tif",
            "FCD4LeyM9PFM_20151210062107_aab.tif",
            "FCD4LeyM9PFM_20151210062107_aal.tif",
            "FCD4LeyqtaZb_20151209071138_aaa.tif"
      };
      List<String> listeNoms = Arrays.asList(fichiersRecherche);
      
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20151219001000000 TO 20151219003000000] AND cse:CS_DUEFAX";
      
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
      long nbDocsAvecNomFichier = 0;
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         String id = doc.getUuid().toString();
         String nomFichierCourant = doc.getFilename() + "." + doc.getExtension();
         
         if (listeNoms.contains(nomFichierCourant)) {
            LOGGER.debug("id: {} - {} a {}", new String[] { id, nomFichierCourant, formatter.format(doc.getArchivageDate()) });
            nbDocsAvecNomFichier++;
         }
      }
      LOGGER.debug("{} docs trouves sur {}", new Object[] {nbDocsAvecNomFichier, nbDocsTrouve});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void rechercheContraintes() throws SearchQueryParseException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
      
      String requeteLucene = "SM_ARCHIVAGE_DATE:[20120101 TO 20160520]";
      
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
      long nbDocsAvecNomFichier = 0;
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         String id = doc.getUuid().toString();
         
         if (doc.getType().equals("3.2.1.1.1")) {
            LOGGER.debug("id: {}", new String[] { id });
         }
      }
      LOGGER.debug("{} docs trouves sur {}", new Object[] {nbDocsAvecNomFichier, nbDocsTrouve});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void rechercheTmp() throws SearchQueryParseException {
      String[] requetes = new String[] { 
            "den:[   3j2r TO bati]",
            "den:[bati TO cie]",
            "den:[cie TO ecg]",
            "den:[ecg TO goncalves]",
            "den:[goncalves TO lashermes]",
            "den:[lashermes TO millon]",
            "den:[millon TO new]",
            "den:[new TO roubaud]",
            "den:[roubaud TO societe]",
            "den:[societe TO yuruk]",
            "den:[yuruk TO zzzzzzzzzzzzz]",
      };
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      for (String requeteLucene : requetes) {
         
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
            iterateur.next();
            nbDocsTrouve++;
         }
         LOGGER.debug("{} docs trouves", new Object[] { nbDocsTrouve});
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void rechercheDocAvecDateCreation() throws SearchQueryParseException, IOException {
      
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("c:/tmp/range-en-cours-date-creation.csv")));
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      String requeteLucene = "SM_CREATION_DATE:[20160108 TO 20160113]";
      
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
      //long nbDocsAvecDateReception = 0;
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         out.write(doc.getUuid().toString() + ";\n");
      }
      LOGGER.debug("{} docs trouves", new Object[] {nbDocsTrouve});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
      
      out.close();
   }
}
