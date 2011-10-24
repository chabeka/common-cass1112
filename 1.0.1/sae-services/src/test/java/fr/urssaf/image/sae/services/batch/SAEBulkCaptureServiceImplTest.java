package fr.urssaf.image.sae.services.batch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.CommonsServices;
import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;

@SuppressWarnings("all")
public class SAEBulkCaptureServiceImplTest extends CommonsServices {

   private static final Logger BULK_LOGGER = Logger
         .getLogger(SAEBulkCaptureServiceImplTest.class);
   @Autowired
   @Qualifier("saeBulkCaptureService")
   SAEBulkCaptureService saeBulkCaptureService;
   @Autowired
   private EcdeTestTools ecdeTestTools;

   private static final String SOMMAIRE = getSommaire() + "sommaire.xml";

   private static String getSommaire() {
      return "ecde://ecde.tu.recouv/TU/20110929/0000/";
   }

   @Test
   public final void bulkCapture() throws IOException {
      ecdeTestTools.buildEcdeTestSommaire();
      EcdeTestSommaire ecde = ecdeTestTools.buildEcdeTestSommaire();
      File repertoireEcdeTraitement = ecde.getRepEcde();
      BULK_LOGGER.debug("BULK CAPTURE ECDE TEMP: "
            + repertoireEcdeTraitement.getAbsoluteFile());
      URI urlEcdeSommaire = ecde.getUrlEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      saeBulkCaptureService.bulkCapture(urlEcdeSommaire.toString());
      try {
         Thread.currentThread().sleep(200000);
      } catch (InterruptedException e) {
         BULK_LOGGER.error(e);
      }
   }
}
