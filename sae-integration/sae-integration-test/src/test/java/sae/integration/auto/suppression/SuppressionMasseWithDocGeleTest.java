package sae.integration.auto.suppression;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.job.JobManager;
import sae.integration.util.ArchivageUtils;
import sae.integration.util.ArchivageValidationUtils;
import sae.integration.util.CleanHelper;
import sae.integration.util.GelUtils;
import sae.integration.util.SoapBuilder;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Suppression de masse de {@link #NOMBRE_DOC_ARCHIVER} parmis lesquels 3 sont gelés
 */
public class SuppressionMasseWithDocGeleTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(SuppressionMasseWithDocGeleTest.class);

   private static SaeServicePortType serviceGNT;

   private static Environment environment;

   /**
    * Liste des uuids des documents archivés
    */
   private static List<String> uuids;

   /**
    * Code de la meta sur laquelle la requête Lucene est effectuée
    */
   private final static String LUCENE_META_CODE = "Denomination";

   /**
    * Valeur de la meta sur laquelle la requête Lucene est effectuée
    */
   private final static String LUCENE_META_VALEUR = "SUPP_00-SuppressionMasse-OK-0";

   /**
    * Nombre de documents initial à archiver
    */
   private final static int NOMBRE_DOC_ARCHIVER = 6;

   /**
    * Index des trois documents qui seront gelés parmis les {@link #NOMBRE_DOC_ARCHIVER}
    */
   private final static int[] ARRAY_INDEX_DOC_GELE = new int[] {0, 2, 5};

   private final boolean withRemoteDebug = true;

   @BeforeClass
   public static void setup() {
      // Setup LOCAL_BATCH pour lancer le test localement
      // L'ecde sera local
      // L"execution du traitement masse se fera manuellement avec l'id du job dans Cassandra
      environment = Environments.GNT_INT_PAJE;
      serviceGNT = SaeServiceStubFactory.getServiceForDevToutesActions(environment.getUrl());
      uuids = new ArrayList<>();
   }

   @Test
   public void suppressionMasseInGNT() throws Exception {

      final String luceneRequest = LUCENE_META_CODE + ":" + LUCENE_META_VALEUR;

      archivageUnitaireInGNT();
      gelDocuments(ARRAY_INDEX_DOC_GELE, true);

      try (final JobManager job = new JobManager(environment)) {
         // Setter à false pour du debug local (c'est-à dire lorsqu'on est sur l'env LOCAL_BATCH)
         job.setRemoteDebug(withRemoteDebug);
         final UUID jobId = job.getJobId();
         LOGGER.info("Lancement du job de suppression {}", jobId);
         job.launchSuppressionMasse(luceneRequest);
         final String log = job.getJobLog();
         LOGGER.debug("Log du traitement :\r\n\r\n{}\r\n", log);
         LOGGER.info("\r\n\r\n===== Fin log");

         LOGGER.info("Verification des documents supprimés");

         // Doc d'index 0 est gelé, donc doit être présent en GNT après la suppression de masse
         LOGGER.info("Test d'existence en GNT du document {} index 0", uuids.get(0));
         boolean existsInGNT = ArchivageValidationUtils.docExists(serviceGNT, uuids.get(0));
         Assert.assertEquals(true, existsInGNT);

         // Doc d'index 1 n'est pas gelé
         LOGGER.info("Test d'existence en GNT du document {} index 1", uuids.get(1));
         existsInGNT = ArchivageValidationUtils.docExists(serviceGNT, uuids.get(1));
         Assert.assertEquals(false, existsInGNT);

         // Doc d'index 2 est gelé, donc doit être présent en GNT après la suppression de masse
         LOGGER.info("Test d'existence en GNT du document {} index 2", uuids.get(2));
         existsInGNT = ArchivageValidationUtils.docExists(serviceGNT, uuids.get(2));
         Assert.assertEquals(true, existsInGNT);

         // Doc d'index 3 n'est pas gelé
         LOGGER.info("Test d'existence en GNT du document {} index 3", uuids.get(3));
         existsInGNT = ArchivageValidationUtils.docExists(serviceGNT, uuids.get(3));
         Assert.assertEquals(false, existsInGNT);

         // Doc d'index 4 n'est pas gelé
         LOGGER.info("Test d'existence en GNT du document {} index 4", uuids.get(4));
         existsInGNT = ArchivageValidationUtils.docExists(serviceGNT, uuids.get(4));
         Assert.assertEquals(false, existsInGNT);

         // Doc d'index 5 est gelé, donc doit être présent en GNT après la suppression de masse
         LOGGER.info("Test d'existence en GNT du document {} index 5", uuids.get(5));
         existsInGNT = ArchivageValidationUtils.docExists(serviceGNT, uuids.get(5));
         Assert.assertEquals(true, existsInGNT);

      }
      finally {
         // Degel des documents gelés + suppression de tous les docs archivés
         gelDocuments(ARRAY_INDEX_DOC_GELE, false);
         // Suppression de tous les documents
         LOGGER.info("suppression de tous les documents");
         for (final String uuid : uuids) {
            CleanHelper.deleteOneDocument(serviceGNT, uuid);
         }
      }
   }

   /**
    * Archivage unitaire de {@value #NOMBRE_DOC_ARCHIVER} documents en GNT
    * 
    * @throws Exception
    */
   private void archivageUnitaireInGNT() throws Exception {
      // Archivage de 8 documents en GNT
      final List<ListeMetadonneeType> metasList = new ArrayList<>();
      for (int i = 0; i < NOMBRE_DOC_ARCHIVER; i++) {
         final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
         final ListeMetadonneeType metas = RandomData.getRandomMetadatas();
         request.setMetadonnees(metas);
         request.setDataFile(TestData.getTxtFile(metas));
         metasList.add(metas);
         SoapBuilder.setMetaValue(metas, LUCENE_META_CODE, LUCENE_META_VALEUR);
         // Lancement de l'archivage
         uuids.add(ArchivageUtils.sendArchivageUnitaire(serviceGNT, request));
      }
      System.out.println(uuids);
   }

   /**
    * Gel ou dégel de plusieurs documents à partir de leurs uuids respectifs
    * Remarque le gel et le degel ne peut pas être fait sur l'environnement LOCAL_BATCH utilisé
    * du jar localement
    * Il faut le faire sur l'evironnement remote utilisé pour les serveurs
    * d'appli et cassandra dans mon cas env GNT_INT_PAJE
    * 
    * @param indexDocuments
    *           liste des uuids des documents à geler
    * @throws Exception
    */
   private void gelDocuments(final int[] indexDocuments, final boolean isGel) {
      // Gel de 3 de ces documents
      if (isGel) {

      } else {
         LOGGER.debug("Dégel de {} documents parmis les {} archivés", new Object[] {indexDocuments.length, NOMBRE_DOC_ARCHIVER});
      }

      try {
         System.out.println(uuids);
         for (final int indexDocGele : indexDocuments) {
            if (isGel) {
               System.out.println("Gel doc index : " + indexDocGele);
               GelUtils.gelDocument(Environments.GNT_INT_PAJE, uuids.get(indexDocGele));
            } else {
               GelUtils.degelDocument(Environments.GNT_INT_PAJE, uuids.get(indexDocGele));
            }
         }
      }
      catch (final Exception e) {
         // Si echec lors du gel d'un document
         for (final String uuid : uuids) {
            CleanHelper.deleteOneDocument(serviceGNT, uuid);
         }
         e.printStackTrace();
         fail("Echec du test dû au gel ou dégel de documents");
      }
   }

}
