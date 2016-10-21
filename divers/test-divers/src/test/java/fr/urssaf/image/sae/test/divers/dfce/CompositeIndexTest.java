package fr.urssaf.image.sae.test.divers.dfce;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.JobAdministrationService;
import net.docubase.toolkit.service.administration.StorageAdministrationService;
import net.docubase.toolkit.service.ged.SearchService;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.batch.DfceJobParametersInvalidException;
import com.docubase.dfce.exception.batch.launch.DfceJobInstanceAlreadyExistsException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobExecutionException;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gnt.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod-gns.xml" })
public class CompositeIndexTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(CompositeIndexTest.class);
   
   private final static int TIMEOUT = 5 * 60 * 1000;
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Test
   public void createCompositeIndex() throws NoSuchDfceJobException, DfceJobInstanceAlreadyExistsException, DfceJobParametersInvalidException, NoSuchDfceJobExecutionException, InterruptedException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      //String[] metas = { "SM_DOCUMENT_TYPE", "sac", "cpt" };
      //String[] metas = { "SM_DOCUMENT_TYPE", "sac" };
      String[] metas = { "sac", "SM_DOCUMENT_TYPE", "dre" };
      Category[] categories = new Category[metas.length];
      
      StorageAdministrationService storageAdminService = serviceProvider.getStorageAdministrationService();
      JobAdministrationService jobAdminService = serviceProvider.getJobAdministrationService();
      
      // recupere les categories
      int index = 0;
      StringBuffer nomIndex = new StringBuffer();
      for (String meta : metas) {
         Category category = storageAdminService.getCategory(meta);
         if (category != null) {
            LOGGER.debug("Category {} récupérée", category.getName());
         } else {
            LOGGER.debug("Impossible de récupérer la Category pour le {}", meta);
            Assert.fail("La category " + meta + " n'a pas ete trouvee");
         }
         categories[index] = category;
         nomIndex.append(meta);
         nomIndex.append('&');
         index++;
      }
      
      // creation de l'index composite
      /*LOGGER.debug("Creation de l'index composite {}", nomIndex.toString());
      CompositeIndex indexComposite = storageAdminService.findOrCreateCompositeIndex(categories);
      if (indexComposite != null) {*/
      
         // lancement de l'indexation
         LOGGER.debug("Lancement du job d'indexation de l'index composite");
         Long idJob = jobAdminService.start("indexCompositesJob","composite.names=" + nomIndex.toString());
         LOGGER.debug("Job lancé : {}", idJob);
         Thread.sleep(TIMEOUT);
         String resultatJob = jobAdminService.getSummary(idJob);
         LOGGER.debug("resultat du job d'indexation : {}", resultatJob);
      /*} else {
         Assert.fail("Index composite non cree");
      }*/
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void createCompositeIndexSicomor() throws NoSuchDfceJobException, DfceJobInstanceAlreadyExistsException, DfceJobParametersInvalidException, NoSuchDfceJobExecutionException, InterruptedException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      String[][] indexACreer = { 
            { "cpt", "sco", "SM_DOCUMENT_TYPE" },
            { "cpt", "sco", "SM_DOCUMENT_TYPE", "dli" },
            { "cpt", "sco", "SM_DOCUMENT_TYPE", "dre" },
            { "cpt", "sco", "SM_DOCUMENT_TYPE", "nbl" },
            { "cpt", "sco", "SM_DOCUMENT_TYPE", "nco" },
            { "cpt", "sco", "SM_DOCUMENT_TYPE", "ndf" },
            { "cpt", "sco", "SM_DOCUMENT_TYPE", "nds" },
            { "cpt", "sco", "SM_DOCUMENT_TYPE", "nfo" },
            { "cpt", "sco", "SM_DOCUMENT_TYPE", "nfs" },
            { "cpt", "sco", "SM_DOCUMENT_TYPE", "nor" },
            { "cpt", "sco", "SM_DOCUMENT_TYPE", "nti" },
            { "dar", "cop", "SM_ARCHIVAGE_DATE" }
      };
      
      boolean dryRun = true;
      if (dryRun) {
         LOGGER.debug("Mode dry run activé : Aucune mise à jour n'est effectuée");
      } else {
         LOGGER.debug("Mode réél : La création des indexes composites va se faire");
      }
      
      StringBuffer nomsIndexes = new StringBuffer();
      int nbIndexes = 0;
      
      StorageAdministrationService storageAdminService = serviceProvider.getStorageAdministrationService();
      JobAdministrationService jobAdminService = serviceProvider.getJobAdministrationService();
            
      for (String[] metas : indexACreer) {
      
         Category[] categories = new Category[metas.length];
         
         // recupere les categories
         int index = 0;
         StringBuffer nomIndex = new StringBuffer();
         
         if (nbIndexes > 0) {
            nomsIndexes.append('|');
         }
         
         for (String meta : metas) {
            Category category = storageAdminService.getCategory(meta);
            if (category != null) {
               LOGGER.debug("Category {} récupérée", category.getName());
            } else {
               LOGGER.debug("Impossible de récupérer la Category pour le {}", meta);
               Assert.fail("La category " + meta + " n'a pas ete trouvee");
            }
            categories[index] = category;
            nomIndex.append(meta);
            nomIndex.append('&');
            index++;
         }
         
         if (dryRun) {
            LOGGER.debug("Mode dry run : L'index composite {} devrait être créé", nomIndex.toString());
         } else {
            LOGGER.debug("Creation de l'index composite {}", nomIndex.toString());
            storageAdminService.findOrCreateCompositeIndex(categories);
         }
         
         // ajout de l'index
         nomsIndexes.append(nomIndex);
         nbIndexes++;
      }
      
      if (dryRun) {
         LOGGER.debug("Mode dry run : l'indexation des l'indexes composites {} devrait être lancé", nomsIndexes.toString());
      } else {
         // lancement de l'indexation
         LOGGER.debug("Lancement du job d'indexation de l'index composite : {}", nomsIndexes.toString());
         Long idJob = jobAdminService.start("indexCompositesJob","composite.names=" + nomsIndexes.toString());
         LOGGER.debug("Job lancé : {}", idJob);
         Thread.sleep(TIMEOUT);
         String resultatJob = jobAdminService.getSummary(idJob);
         LOGGER.debug("resultat du job d'indexation : {}", resultatJob);
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void createCompositeIndexGroom() throws NoSuchDfceJobException, DfceJobInstanceAlreadyExistsException, DfceJobParametersInvalidException, NoSuchDfceJobExecutionException, InterruptedException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      String[][] indexACreer = { 
            { "drh", "cop", "SM_CREATION_DATE" },
            { "drh", "cop", "nma" },
            { "drh", "cop", "nma", "frd" },
            { "drh", "cop", "npa", "SM_CREATION_DATE" },
            { "drh", "cop", "pag", "SM_CREATION_DATE" }
            
      };
      
      boolean dryRun = false;
      if (dryRun) {
         LOGGER.debug("Mode dry run activé : Aucune mise à jour n'est effectuée");
      } else {
         LOGGER.debug("Mode réél : La création des indexes composites va se faire");
      }
      
      StringBuffer nomsIndexes = new StringBuffer();
      int nbIndexes = 0;
      
      StorageAdministrationService storageAdminService = serviceProvider.getStorageAdministrationService();
      JobAdministrationService jobAdminService = serviceProvider.getJobAdministrationService();
            
      for (String[] metas : indexACreer) {
      
         Category[] categories = new Category[metas.length];
         
         // recupere les categories
         int index = 0;
         StringBuffer nomIndex = new StringBuffer();
         
         if (nbIndexes > 0) {
            nomsIndexes.append('|');
         }
         
         for (String meta : metas) {
            Category category = storageAdminService.getCategory(meta);
            if (category != null) {
               LOGGER.debug("Category {} récupérée", category.getName());
            } else {
               LOGGER.debug("Impossible de récupérer la Category pour le {}", meta);
               Assert.fail("La category " + meta + " n'a pas ete trouvee");
            }
            categories[index] = category;
            nomIndex.append(meta);
            nomIndex.append('&');
            index++;
         }
         
         if (dryRun) {
            LOGGER.debug("Mode dry run : L'index composite {} devrait être créé", nomIndex.toString());
         } else {
            LOGGER.debug("Creation de l'index composite {}", nomIndex.toString());
            storageAdminService.findOrCreateCompositeIndex(categories);
         }
         
         // ajout de l'index
         nomsIndexes.append(nomIndex);
         nbIndexes++;
      }
      
      if (dryRun) {
         LOGGER.debug("Mode dry run : l'indexation des l'indexes composites {} devrait être lancé", nomsIndexes.toString());
      } else {
         // lancement de l'indexation
         LOGGER.debug("Lancement du job d'indexation de l'index composite : {}", nomsIndexes.toString());
         Long idJob = jobAdminService.start("indexCompositesJob","composite.names=" + nomsIndexes.toString());
         LOGGER.debug("Job lancé : {}", idJob);
         Thread.sleep(TIMEOUT);
         String resultatJob = jobAdminService.getSummary(idJob);
         LOGGER.debug("resultat du job d'indexation : {}", resultatJob);
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void getListeCompositeIndex() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException, NoSuchJobExecutionException, InterruptedException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      // recupere la liste des index composites
      LOGGER.debug("Recuperation de la liste des index composites");
      Set<CompositeIndex> compositeIndexes = serviceProvider.getStorageAdministrationService().fetchAllCompositeIndex();
      Iterator<CompositeIndex> iter = compositeIndexes.iterator();
      while(iter.hasNext()) {
         CompositeIndex index = iter.next();
         LOGGER.debug("{} : indexé {}", getCompositeIndexName(index), index.isComputed());
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   private String getCompositeIndexName(CompositeIndex index) {
      StringBuffer buffer = new StringBuffer();
      for (Category category : index.getCategories()) {
         buffer.append(category.getName());
         buffer.append('&');
      }
      return buffer.toString();
   }
   
   @Test
   public void rechercheParIterateur() throws SearchQueryParseException {
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      SearchService searchService = serviceProvider.getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService().getBase("SAE-DEV");
      
      SearchQuery requete = ToolkitFactory.getInstance().createMonobaseQuery("dar:true AND cop:CER69 AND SM_ARCHIVAGE_DATE:[20150304133200592 TO 20150304133356249]", base);
      
      Iterator<Document> iterateur =  searchService.createDocumentIterator(requete);
      
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         LOGGER.debug("{}", new String[] { formatter.format(doc.getArchivageDate()) });
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
      
   }
}
