package fr.urssaf.image.sae.services.jmx;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;
import fr.urssaf.image.sae.storage.model.jmx.BulkProgress;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-jmx-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SAEJmxIndicatorTest {

   @Autowired
   @Qualifier("jmxIndicatorService")
   private SAEJmxIndicator saeJmxIndicator;

   @Autowired
   @Qualifier("saeBulkCaptureService")
   private SAEBulkCaptureService bulkCapture;

   private JmxIndicator jmxIndicator;

   @Before
   public void before() {

      jmxIndicator = new JmxIndicator();
      EasyMock.expect(bulkCapture.isActive()).andReturn(true);
   }

   @After
   public void after() {
      EasyMock.reset(bulkCapture);
   }

   @Test
   public void retrieveBulkProgress_INSERTION_DOCUMENTS() {

      jmxIndicator.setJmxTreatmentState(BulkProgress.INSERTION_DOCUMENTS);
      jmxIndicator.setJmxStorageIndex(3);
      jmxIndicator.setJmxCountDocument(5);

      EasyMock.expect(bulkCapture.retrieveJmxSAEBulkCaptureIndicator())
            .andReturn(jmxIndicator);
      EasyMock.replay(bulkCapture);

      Assert
            .assertEquals(
                  "Message inattendu quand insertion des documents en cours",
                  "Archivage des documents en cours... [ 3 document(s) sur 5 document(s) ]",
                  saeJmxIndicator.retrieveBulkProgress());
   }

   @Test
   public void retrieveBulkProgress_DELETION_DOCUMENTS() {

      jmxIndicator.setJmxTreatmentState(BulkProgress.DELETION_DOCUMENTS);
      jmxIndicator.setJmxStorageIndex(2);
      jmxIndicator.setJmxCountDocument(5);

      EasyMock.expect(bulkCapture.retrieveJmxSAEBulkCaptureIndicator())
            .andReturn(jmxIndicator);
      EasyMock.replay(bulkCapture);

      Assert
            .assertEquals(
                  "Message inattendu quand suppression des documents en cours",
                  "Suppression des documents en cours... [ 2 document(s) sur 5 document(s) ]",
                  saeJmxIndicator.retrieveBulkProgress());
   }

   @Test
   public void retrieveBulkProgress_CONTROL_DOCUMENTS() {

      jmxIndicator.setJmxTreatmentState(BulkProgress.CONTROL_DOCUMENTS);
      jmxIndicator.setJmxControlIndex(2);
      jmxIndicator.setJmxCountDocument(5);

      EasyMock.expect(bulkCapture.retrieveJmxSAEBulkCaptureIndicator())
            .andReturn(jmxIndicator);
      EasyMock.replay(bulkCapture);

      Assert
            .assertEquals(
                  "Message inattendu quand contrôle des documents en cours",
                  "Contrôle et enrichissement des documents à archiver en cours... [ 2 document(s) sur 5 document(s) ]",
                  saeJmxIndicator.retrieveBulkProgress());
   }

   @Test
   public void retrieveBulkProgress_INTERRUPTED_TREATMENT() {

      jmxIndicator.setJmxTreatmentState(BulkProgress.INTERRUPTED_TREATMENT);
      jmxIndicator.setInterruptionDelay(1813);
      jmxIndicator.setJmxStorageIndex(1435);
      jmxIndicator.setJmxCountDocument(18750);

      DateTimeFormatter formatter = DateTimeFormat
            .forPattern("dd-MM-yyyy HH:mm:ss");

      DateTime interruptionStart = DateTime.parse("01-01-1999 14:24:36",
            formatter);

      jmxIndicator.setInterruptionStart(interruptionStart);

      DateTime interruptionEnd = DateTime.parse("01-01-1999 14:54:49",
            formatter);

      jmxIndicator.setInterruptionEnd(interruptionEnd);

      EasyMock.expect(bulkCapture.retrieveJmxSAEBulkCaptureIndicator())
            .andReturn(jmxIndicator);
      EasyMock.replay(bulkCapture);

      Assert
            .assertEquals(
                  "Message inattendu quand interruption du traitement en cours",
                  "Capture de masse en pause. Début de la pause : 01/01/1999 14:24:36. Durée de la pause : 1813 secondes. Reprise du traitement : 01/01/1999 14:54:49. Nombre de documents intégrés : 1435/18750",
                  saeJmxIndicator.retrieveBulkProgress());
   }

   @Test
   public void retrieveBulkProgress_RESTART_TREATMENT() {

      jmxIndicator.setJmxTreatmentState(BulkProgress.RESTART_TREATMENT);

      EasyMock.expect(bulkCapture.retrieveJmxSAEBulkCaptureIndicator())
            .andReturn(jmxIndicator);
      EasyMock.replay(bulkCapture);

      Assert.assertEquals(
            "Message inattendu lors de la reprise de la capture en masse",
            "Reprise de la capture en masse.", saeJmxIndicator
                  .retrieveBulkProgress());
   }

   @Test
   public void retrieveExternalTreatmentId_READING_DOCUMENTS() {

      jmxIndicator.setJmxTreatmentState(BulkProgress.READING_DOCUMENTS);

      EasyMock.expect(bulkCapture.retrieveJmxSAEBulkCaptureIndicator())
            .andReturn(jmxIndicator);

      EasyMock.replay(bulkCapture);

      Assert
            .assertEquals(
                  "Identifiant traitement inattendu lors de la lecture des documents",
                  "Aucun identifiant de traitement n'a été trouvé car la lecture du fichier sommaire n'est pas terminée.",
                  saeJmxIndicator.retrieveExternalTreatmentId());

   }

   @Test
   public void retrieveExternalTreatmentId_BEGIN_OF_ARCHIVE() {

      jmxIndicator.setJmxTreatmentState(BulkProgress.BEGIN_OF_ARCHIVE);

      EasyMock.expect(bulkCapture.retrieveJmxSAEBulkCaptureIndicator())
            .andReturn(jmxIndicator);

      EasyMock.replay(bulkCapture);

      Assert
            .assertEquals(
                  "Identifiant traitement inattendu lors du début de l'archivage en masse.",
                  "Aucun identifiant de traitement n'a été trouvé car la lecture du fichier sommaire n'est pas terminée.",
                  saeJmxIndicator.retrieveExternalTreatmentId());

   }

   @Test
   public void retrieveExternalTreatmentId_INSERTION_DOCUMENTS() {

      String idTreatment = "28f5b670-b008-4c0e-882c-1a6c4a7be3dc";

      jmxIndicator.setJmxTreatmentState(BulkProgress.INSERTION_DOCUMENTS);
      jmxIndicator.setJmxExternalIdTreatment(idTreatment);

      EasyMock.expect(bulkCapture.retrieveJmxSAEBulkCaptureIndicator())
            .andReturn(jmxIndicator);

      EasyMock.replay(bulkCapture);

      Assert.assertEquals(
            "Identifiant traitement lors de l'insertion des documents",
            idTreatment, saeJmxIndicator.retrieveExternalTreatmentId());

   }

}
