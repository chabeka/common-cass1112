package fr.urssaf.image.sae.format.identification.identifiers.pdfa;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.schlichtherle.io.FileInputStream;
import fr.urssaf.image.sae.format.identification.exceptions.IdentificationRuntimeException;
import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;
import fr.urssaf.image.sae.format.model.EtapeEtResultat;

/**
 * 
 * Classe testant les services de la classe {@link PdfaIdentifierImpl}
 * 
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class PdfaIdentifierImplTest {

   @Autowired
   private PdfaIdentifierImpl pdfaIdentifier;

   private static final String MESSAGE_ERRONE = "message erroné";
   private static final String RESULTAT_ERRONE = "résultat erroné";
   private static final String ETAPE1 = "Etape 1 : R\u00E9cup\u00E9ration du PUUID à partir de DROID.";
   private static final String ETAPE2 = "Etape 2 : Comparaison du PUUID avec idFormat.";
   private static final String PUUID_FMT354 = "PUUID : fmt/354";
   private static final String PUUID_FMT18 = "PUUID : fmt/18";
   private static final String PUUID_DIFF_IDFORMAT = "PUUID différent de IDFORMAT mais fait partie de la liste des formats compatibles.";

   @Test
   public void identifyFileFailureIdFormatErrone()
         throws IdentificationRuntimeException, IOException {

      File file = new File("src/test/resources/identification/PdfaValide.pdf");
      IdentificationResult identificationResult = pdfaIdentifier.identifyFile(
            "idFormat", file);

      EtapeEtResultat etape0 = identificationResult.getDetails().get(0);
      EtapeEtResultat etape1 = identificationResult.getDetails().get(1);
      Assert.assertEquals(MESSAGE_ERRONE, ETAPE1, etape0.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, PUUID_FMT354, etape0.getResultat());

      Assert.assertEquals(MESSAGE_ERRONE, ETAPE2, etape1.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, "PUUID diff\u00E9rent de IDFORMAT.",
            etape1.getResultat());

      Assert.assertEquals(RESULTAT_ERRONE, false, identificationResult
            .isIdentified());

   }

   @Test
   public void identifyFileSuccess() throws IdentificationRuntimeException,
         IOException {
      File file = new File("src/test/resources/identification/PdfaValide.pdf");
      IdentificationResult identificationResult = pdfaIdentifier.identifyFile(
            "fmt/354", file);

      EtapeEtResultat etape0 = identificationResult.getDetails().get(0);
      EtapeEtResultat etape1 = identificationResult.getDetails().get(1);
      Assert.assertEquals(MESSAGE_ERRONE, ETAPE1, etape0.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, PUUID_FMT354, etape0.getResultat());

      Assert.assertEquals(MESSAGE_ERRONE, ETAPE2, etape1.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, "PUUID = IDFORMAT.", etape1
            .getResultat());

      Assert.assertEquals(RESULTAT_ERRONE, true, identificationResult
            .isIdentified());

   }

   @Test
   public void identifyFileSuccessCompatiblefmt18()
         throws IdentificationRuntimeException, IOException {

      File file = new File(
            "src/test/resources/identification/pdfaCompatible.pdf");
      IdentificationResult identificationResult = pdfaIdentifier.identifyFile(
            "fmt/354", file);

      EtapeEtResultat etape0 = identificationResult.getDetails().get(0);
      EtapeEtResultat etape1 = identificationResult.getDetails().get(1);

      Assert.assertEquals(MESSAGE_ERRONE, ETAPE1, etape0.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, PUUID_FMT18, etape0.getResultat());

      Assert.assertEquals(MESSAGE_ERRONE, ETAPE2, etape1.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, PUUID_DIFF_IDFORMAT, etape1
            .getResultat());

      Assert.assertEquals(RESULTAT_ERRONE, true, identificationResult
            .isIdentified());

   }

   /*************************************************************************************************************************/
   /********************************** STREAM **********************************************************/
   /*************************************************************************************************************************/
   @Test
   public void identifyStreamFailureIdFormatErrone() throws IOException {
      File file = new File("src/test/resources/identification/PdfaValide.pdf");
      InputStream inputStream = new FileInputStream(file);

      IdentificationResult identificationResult = pdfaIdentifier
            .identifyStream("idFormat", inputStream);

      EtapeEtResultat etape0 = identificationResult.getDetails().get(0);
      EtapeEtResultat etape1 = identificationResult.getDetails().get(1);
      Assert.assertEquals(MESSAGE_ERRONE, ETAPE1, etape0.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, PUUID_FMT354, etape0.getResultat());

      Assert.assertEquals(MESSAGE_ERRONE, ETAPE2, etape1.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, "PUUID diff\u00E9rent de IDFORMAT.",
            etape1.getResultat());

      Assert.assertEquals(RESULTAT_ERRONE, false, identificationResult
            .isIdentified());

      inputStream.close();

   }

   @Test
   public void identifyStreamSuccess() throws IOException {
      File file = new File("src/test/resources/identification/PdfaValide.pdf");
      InputStream inputStream = new FileInputStream(file);

      IdentificationResult identificationResult = pdfaIdentifier
            .identifyStream("fmt/354", inputStream);

      EtapeEtResultat etape0 = identificationResult.getDetails().get(0);
      EtapeEtResultat etape1 = identificationResult.getDetails().get(1);
      Assert.assertEquals(MESSAGE_ERRONE, ETAPE1, etape0.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, PUUID_FMT354, etape0.getResultat());

      Assert.assertEquals(MESSAGE_ERRONE, ETAPE2, etape1.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, "PUUID = IDFORMAT.", etape1
            .getResultat());

      Assert.assertEquals(RESULTAT_ERRONE, true, identificationResult
            .isIdentified());

      inputStream.close();
   }

   @Test
   public void identifyStreamSuccessCompatiblefmt18() throws IOException {
      File file = new File(
            "src/test/resources/identification/pdfaCompatible.pdf");
      InputStream inputStream = new FileInputStream(file);

      IdentificationResult identificationResult = pdfaIdentifier
            .identifyStream("fmt/354", inputStream);

      EtapeEtResultat etape0 = identificationResult.getDetails().get(0);
      EtapeEtResultat etape1 = identificationResult.getDetails().get(1);

      Assert.assertEquals(MESSAGE_ERRONE, ETAPE1, etape0.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, PUUID_FMT18, etape0.getResultat());

      Assert.assertEquals(MESSAGE_ERRONE, ETAPE2, etape1.getEtape());
      Assert.assertEquals(RESULTAT_ERRONE, PUUID_DIFF_IDFORMAT, etape1
            .getResultat());

      Assert.assertEquals(RESULTAT_ERRONE, true, identificationResult
            .isIdentified());

      inputStream.close();

   }

}
