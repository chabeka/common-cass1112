package sae.integration.auto.suppression;

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
import sae.integration.util.SoapBuilder;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Suppression de masse de {@link #NOMBRE_DOC_ARCHIVER} documents en GNT
 */
public class SuppressionMasseTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(SuppressionMasseTest.class);

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
   /**
    * Lancer le test de suppression de masse
    * 
    * @throws Exception
    */
   public void suppressionMasseInGNT() throws Exception {

      final String luceneRequest = LUCENE_META_CODE + ":" + LUCENE_META_VALEUR;

      archivageUnitaireInGNT();

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

         for (int i = 0; i < NOMBRE_DOC_ARCHIVER; i++) {
            LOGGER.info("Test d'existence en GNT du document {} index {}", new Object[] {uuids.get(i), i});
            final boolean existsInGNT = ArchivageValidationUtils.docExists(serviceGNT, uuids.get(i));
            Assert.assertEquals(false, existsInGNT);
         }
      }
      finally {
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

}
