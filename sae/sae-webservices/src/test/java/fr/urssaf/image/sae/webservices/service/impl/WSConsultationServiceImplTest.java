package fr.urssaf.image.sae.webservices.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.schlichtherle.io.FileInputStream;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;

/**
 * Tests unitaires de la classe {@link WSConsultationServiceImpl}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions",
      "PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods" })
public class WSConsultationServiceImplTest {

   @Autowired
   private WSConsultationServiceImpl consultService;

   private static final String FORMAT_FICHIER = "FormatFichier";

   private static final String TYPE_MIME_DEFAUT = "application/octet-stream";

   private static final String TYPE_MIME_PDF = "application/pdf";

   @Test
   public void ajouteSiBesoinMetadonneeFormatFichier_success_ListeNull() {

      boolean ajout = consultService
            .ajouteSiBesoinMetadonneeFormatFichier(null);

      assertEquals("La métadonnée FormatFichier n'aurait pas du être ajoutée",
            false, ajout);

   }

   @Test
   public void ajouteSiBesoinMetadonneeFormatFichier_success_ListeVide() {

      List<String> metas = new ArrayList<String>();

      boolean ajout = consultService
            .ajouteSiBesoinMetadonneeFormatFichier(metas);

      assertEquals("La métadonnée FormatFichier n'aurait pas du être ajoutée",
            false, ajout);

   }

   @Test
   public void ajouteSiBesoinMetadonneeFormatFichier_success_ListeRemplieSansFormatFichier() {

      List<String> metas = new ArrayList<String>();
      metas.add("Toto");
      metas.add("Tata");

      boolean ajout = consultService
            .ajouteSiBesoinMetadonneeFormatFichier(metas);

      assertEquals("La métadonnée FormatFichier aurait du être ajoutée", true,
            ajout);

      assertEquals("La métadonnée FormatFichier aurait du être ajoutée", 3,
            metas.size());

      int idx = metas.indexOf(FORMAT_FICHIER);
      assertNotSame("La métadonnée FormatFichier aurait du être ajoutée", idx,
            -1);

   }

   @Test
   public void ajouteSiBesoinMetadonneeFormatFichier_success_ListeRemplieAvecFormatFichier() {

      List<String> metas = new ArrayList<String>();
      metas.add("Toto");
      metas.add(FORMAT_FICHIER);
      metas.add("Tata");

      boolean ajout = consultService
            .ajouteSiBesoinMetadonneeFormatFichier(metas);

      assertEquals("La métadonnée FormatFichier n'aurait pas du être ajoutée",
            false, ajout);
      assertEquals("La métadonnée FormatFichier n'aurait pas du être ajoutée",
            3, metas.size());

   }

   @Test
   public void convertitPronomEnTypeMime_success_TypePronomNull() {

      String typeMime = consultService.convertitPronomEnTypeMime(null);

      assertEquals("Le type MIME est incorrect", TYPE_MIME_DEFAUT, typeMime);

   }

   @Test
   public void convertitPronomEnTypeMime_success_TypePronomVide() {

      String typeMime = consultService.convertitPronomEnTypeMime("");

      assertEquals("Le type MIME est incorrect", TYPE_MIME_DEFAUT, typeMime);

   }

   @Test
   public void convertitPronomEnTypeMime_success_TypePronomPdfSlashA() {

      String typeMime = consultService.convertitPronomEnTypeMime("fmt/354");

      assertEquals("Le type MIME est incorrect", TYPE_MIME_PDF, typeMime);

   }

   @Test
   public void convertitPronomEnTypeMime_success_TypePronomInconnu() {

      String typeMime = consultService
            .convertitPronomEnTypeMime("gloubi_boulga");

      assertEquals("Le type MIME est incorrect", TYPE_MIME_DEFAUT, typeMime);

   }

   @Test
   public void typeMimeDepuisFormatFichier_success_SansSuppr()
         throws ConsultationAxisFault {

      List<UntypedMetadata> listeMetas = new ArrayList<UntypedMetadata>();
      listeMetas.add(new UntypedMetadata(FORMAT_FICHIER, "fmt/354"));

      boolean supprMetaFmtFic = false;

      String typeMime = consultService.typeMimeDepuisFormatFichier(listeMetas,
            supprMetaFmtFic);

      assertEquals("Le type MIME est incorrect", TYPE_MIME_PDF, typeMime);
      assertEquals(
            "La métadonnée FormatFichier n'aurait pas du être supprimée", 1,
            listeMetas.size());

   }

   @Test
   public void typeMimeDepuisFormatFichier_success_AvecSuppr()
         throws ConsultationAxisFault {

      List<UntypedMetadata> listeMetas = new ArrayList<UntypedMetadata>();
      listeMetas.add(new UntypedMetadata(FORMAT_FICHIER, "fmt/354"));

      boolean supprMetaFmtFic = true;

      String typeMime = consultService.typeMimeDepuisFormatFichier(listeMetas,
            supprMetaFmtFic);

      assertEquals("Le type MIME est incorrect", TYPE_MIME_PDF, typeMime);
      assertEquals("La métadonnée FormatFichier aurait du être supprimée", 0,
            listeMetas.size());

   }

   @Test
   public void typeMimeDepuisFormatFichier_failure_ListeMetasNull() {

      List<UntypedMetadata> listeMetas = null;

      boolean supprMetaFmtFic = true;

      try {

         consultService
               .typeMimeDepuisFormatFichier(listeMetas, supprMetaFmtFic);

         fail("Une exception ConsultationAxisFault aurait dû être levée");

      } catch (ConsultationAxisFault fault) {

         checkSoapFaultErreurInterne(fault);

      }

   }

   private void checkSoapFaultErreurInterne(ConsultationAxisFault fault) {

      assertEquals("Le message de la SoapFault est incorrect",
            "Une erreur interne à l'application est survenue.", fault
                  .getMessage());

      assertEquals("La partie locale du code de la SoapFault est incorrect",
            "ErreurInterne", fault.getFaultCode().getLocalPart());

      assertEquals("Le préfixe du code de la SoapFault est incorrect", "sae",
            fault.getFaultCode().getPrefix());

   }

   @Test
   public void typeMimeDepuisFormatFichier_failure_ListeMetasVide() {

      List<UntypedMetadata> listeMetas = new ArrayList<UntypedMetadata>();

      boolean supprMetaFmtFic = true;

      try {

         consultService
               .typeMimeDepuisFormatFichier(listeMetas, supprMetaFmtFic);

         fail("Une exception ConsultationAxisFault aurait dû être levée");

      } catch (ConsultationAxisFault fault) {

         checkSoapFaultErreurInterne(fault);

      }

   }

   @Test
   public void typeMimeDepuisFormatFichier_failure_ListeMetasSansFormatFichier() {

      List<UntypedMetadata> listeMetas = new ArrayList<UntypedMetadata>();
      listeMetas.add(new UntypedMetadata("codeLong", "valeur"));

      boolean supprMetaFmtFic = true;

      try {

         consultService
               .typeMimeDepuisFormatFichier(listeMetas, supprMetaFmtFic);

         fail("Une exception ConsultationAxisFault aurait dû être levée");

      } catch (ConsultationAxisFault fault) {

         checkSoapFaultErreurInterne(fault);

      }

   }

   @Test
   public void testHash() throws IOException, NoSuchAlgorithmException {
      ClassPathResource resource = new ClassPathResource(
            "storage/attestation.pdf");
      File file = resource.getFile();

      byte[] content = FileUtils.readFileToByteArray(file);
      InputStream stream = new DataHandler(new FileDataSource(file))
            .getInputStream();

      String contentHash = HashUtils.hashHex(content, "SHA-1");
      System.out.println(contentHash);
      String streamHash = HashUtils.hashHex(stream, "SHA-1");
      System.out.println(streamHash);
      stream.close();
      System.out.println(streamHash);

   }

}
