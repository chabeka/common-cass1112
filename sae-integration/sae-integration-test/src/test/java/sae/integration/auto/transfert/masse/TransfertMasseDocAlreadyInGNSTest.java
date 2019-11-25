package sae.integration.auto.transfert.masse;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.job.JobManager;
import sae.integration.util.ArchivageUtils;
import sae.integration.util.CleanHelper;
import sae.integration.util.SoapBuilder;
import sae.integration.util.TransfertSommaireBuilder;
import sae.integration.util.XMLHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.xml.modele.NonIntegratedDocumentType;
import sae.integration.xml.modele.ResultatsType;

/**
 * Test les traitements de transfert de masse
 */
public class TransfertMasseDocAlreadyInGNSTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(TransfertMasseDocAlreadyInGNSTest.class);


   @Test
   /**
    * On archive un document en GNT, et un en GNS avec le même ID
    * On essaye de transférer le document.
    * On vérifie que ça ne marche pas, et que le message d'erreur est explicite
    */
   public void transfertKO() throws Exception {
      final Environment environnementGNT = Environments.GNT_PIC;
      final Environment environnementGNS = Environments.GNS_PIC;
      final SaeServicePortType gntService = SaeServiceStubFactory.getServiceForDevToutesActions(environnementGNT.getUrl());
      final SaeServicePortType gnsService = SaeServiceStubFactory.getServiceForDevToutesActions(environnementGNS.getUrl());

      // Archivage d'un document en GNT
      final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      final ListeMetadonneeType metasGNT = RandomData.getRandomMetadatas();
      request.setMetadonnees(metasGNT);
      request.setDataFile(TestData.getTxtFile(metasGNT));
      // Lancement de l'archivage
      final String uuid = ArchivageUtils.sendArchivageUnitaire(gntService, request);

      // Archivage d'un document en GNS avec le même id
      final ListeMetadonneeType metasGNS = RandomData.getRandomMetadatas();
      SoapBuilder.setMetaValue(metasGNS, "IdGed", uuid);
      request.setMetadonnees(metasGNS);
      request.setDataFile(TestData.getTxtFile(metasGNS));
      // Lancement de l'archivage
      final String uuidGNS = ArchivageUtils.sendArchivageUnitaire(gnsService, request);
      Assert.assertEquals(uuid, uuidGNS);

      // Création du sommaire
      final TransfertSommaireBuilder builder = new TransfertSommaireBuilder();
      // On demande le transfert du doc, sans modification de méta
      builder.addTransfert(uuid, null);
      final String sommaireContent = builder.build();

      try (final JobManager job = new JobManager(environnementGNT)) {
         // Préparation et lancement du job
         final UUID jobId = job.getJobId();
         LOGGER.info("Lancement du job {}", jobId);
         job.launchTransfertMasse(sommaireContent);

         // Récupération du log du traitement et du resultats.xml pour debug
         final String log = job.getJobLog();
         LOGGER.debug("Log du traitement :\r\n\r\n{}\r\n", log);
         final String resultatsXML = job.getResultatsXML();
         LOGGER.debug("Contenu du fichier resultats.xml :\r\n {}\r\n", resultatsXML);


         // Vérification du contenu du fichier resultats.xml
         final ResultatsType resultat = XMLHelper.parseResultatsXML(resultatsXML);
         Assert.assertEquals(1, (int) resultat.getInitialDocumentsCount());
         Assert.assertEquals(0, (int) resultat.getIntegratedDocumentsCount());
         Assert.assertEquals(1, (int) resultat.getNonIntegratedDocumentsCount());
         final NonIntegratedDocumentType nonIntegratedDocument = resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(0);
         Assert.assertEquals(uuid, nonIntegratedDocument.getObjetNumerique().getUUID());
         final String libErreur = nonIntegratedDocument.getErreurs().getErreur().get(0).getLibelle();
         assertThat(libErreur, containsString("est anormalement présent en GNT et en GNS"));

      }
      finally {
         LOGGER.info("Suppression des documents");
         CleanHelper.deleteOneDocument(gntService, uuid);
         CleanHelper.deleteOneDocument(gnsService, uuid);
      }
   }


}
