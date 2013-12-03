package fr.urssaf.image.sae.format.identification.service;

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
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentificationRuntimeException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentifierInitialisationException;
import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;
import fr.urssaf.image.sae.format.identification.service.impl.IdentificationServiceImpl;
import fr.urssaf.image.sae.format.model.EtapeEtResultat;

/**
 * 
 * Classe testant les services de la classe {@link IdentificationServiceImpl}
 * 
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class IdentificationServiceImplTest {

   @Autowired
   private IdentificationServiceImpl identificationService;

   private final File file = new File(
         "src/test/resources/identification/PdfaValide.pdf");

   private final File doc = new File(
         "src/test/resources/identification/doc.pdf");
   
   private static final String MESS_EXCEPT_ERRONE = "Le message de l'exception est incorrect";
   private static final String FMT_354 = "fmt/354";
   private static final String PUUID_FMT_354 =   "PUUID : fmt/354";
   private static final String MESS_ERRONE = "message erroné";
   private static final String RESULT_ERRONE = "resultat erroné";
   private static final String ETAPE_1 = "Etape 1 : R\u00E9cup\u00E9ration du PUUID à partir de DROID.";
   private static final String ETAPE_2 = "Etape 2 : Comparaison du PUUID avec idFormat.";
   private static final String PUUID_EGAL_IDFORMAT = "PUUID = IDFORMAT.";
   

   @Test
   public void identifyServiceFailureIdFormatErrone()
         throws IdentificationRuntimeException, UnknownFormatException,
         IdentifierInitialisationException, IOException {

      try {
         identificationService.identifyFile("idFormat", file);
         Assert
               .fail("Une exception UnknownFormatException aurait dû être levée");

      } catch (UnknownFormatException ex) {
         Assert.assertEquals(MESS_EXCEPT_ERRONE,
               "Aucun format n'a été trouvé avec l'identifiant : idFormat.", ex
                     .getMessage());
      }

   }

   @Test
   public void identifyServiceSuccessFmt354()
         throws IdentificationRuntimeException,
         IdentifierInitialisationException, UnknownFormatException, IOException {

      IdentificationResult result = identificationService.identifyFile(
            FMT_354, file);

      EtapeEtResultat etape0 = result.getDetails().get(0);
      EtapeEtResultat etape1 = result.getDetails().get(1);
      Assert.assertEquals(MESS_ERRONE,
            ETAPE_1,
            etape0.getEtape());
      Assert.assertEquals(RESULT_ERRONE, PUUID_FMT_354, etape0
            .getResultat());

      Assert.assertEquals(MESS_ERRONE,
            ETAPE_2, etape1.getEtape());
      Assert.assertEquals(RESULT_ERRONE, PUUID_EGAL_IDFORMAT, etape1
            .getResultat());

      Assert.assertEquals(RESULT_ERRONE, true, result.isIdentified());
   }

   @Test
   public void identifyServiceSuccessFmtCompatible18()
         throws IdentificationRuntimeException,
         IdentifierInitialisationException, UnknownFormatException, IOException {
      try {
         identificationService.identifyFile("fmt/18", file);
      } catch (UnknownFormatException ex) {
         Assert.assertEquals(MESS_EXCEPT_ERRONE,
               "Aucun format n'a été trouvé avec l'identifiant : fmt/18.", ex
                     .getMessage());
      }
   }

   @Test
   public void identifyServiceSuccessFmtCompatible95()
         throws IdentificationRuntimeException,
         IdentifierInitialisationException, UnknownFormatException, IOException {

      try {
         identificationService.identifyFile("fmt/95", file);
      } catch (UnknownFormatException ex) {
         Assert.assertEquals(MESS_EXCEPT_ERRONE,
               "Aucun format n'a été trouvé avec l'identifiant : fmt/95.", ex
                     .getMessage());
      }
   }

   @Test
   public void identifyFileServiceCompatibleFmt95()
         throws IdentificationRuntimeException,
         IdentifierInitialisationException, UnknownFormatException, IOException {

      IdentificationResult result = identificationService.identifyFile(
            FMT_354, doc);

      EtapeEtResultat etape0 = result.getDetails().get(0);
      EtapeEtResultat etape1 = result.getDetails().get(1);
      Assert.assertEquals(MESS_ERRONE,
            ETAPE_1,
            etape0.getEtape());
      Assert.assertEquals(RESULT_ERRONE, PUUID_FMT_354, etape0
            .getResultat());

      Assert.assertEquals(MESS_ERRONE,
            ETAPE_2, etape1.getEtape());
      Assert.assertEquals(RESULT_ERRONE, PUUID_EGAL_IDFORMAT, etape1
            .getResultat());

      Assert.assertEquals(RESULT_ERRONE, true, result.isIdentified());

   }

   /*************************************************************************************************************************/
   /**********************************
    * STREAM
    * 
    * @throws IOException
    *            *
    *********************************************************/
   @Test
   public void identifyStreamServiceFailureIdFormatErrone()
         throws IdentificationRuntimeException, UnknownFormatException,
         IdentifierInitialisationException, IOException {
      InputStream inputStream = new FileInputStream(file);
      try {
         identificationService.identifyStream("idFormat", inputStream);
         Assert
               .fail("Une exception IllegalArgumentException aurait dû être levée");

      } catch (UnknownFormatException ex) {
         inputStream.close();
         Assert.assertEquals(MESS_EXCEPT_ERRONE,
               "Aucun format n'a été trouvé avec l'identifiant : idFormat.", ex
                     .getMessage());
      } finally {
         inputStream.close();
      }
   }

   @Test
   public void identifyStreamServiceSuccessFmt354()
         throws IdentificationRuntimeException,
         IdentifierInitialisationException, UnknownFormatException, IOException {
      InputStream inputStream = null;
      try {
         inputStream = new FileInputStream(file);
         IdentificationResult result = identificationService.identifyStream(
               FMT_354, inputStream);

         EtapeEtResultat etape0 = result.getDetails().get(0);
         EtapeEtResultat etape1 = result.getDetails().get(1);
         Assert.assertEquals(MESS_ERRONE,
               ETAPE_1,
               etape0.getEtape());
         Assert.assertEquals(RESULT_ERRONE, PUUID_FMT_354, etape0
               .getResultat());

         Assert.assertEquals(MESS_ERRONE,
               ETAPE_2, etape1
                     .getEtape());
         Assert.assertEquals(RESULT_ERRONE, PUUID_EGAL_IDFORMAT, etape1
               .getResultat());

         Assert.assertEquals(RESULT_ERRONE, true, result.isIdentified());
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }
      }

   }

   @Test
   public void identifyStreamServiceFailureFmtCompatible18()
         throws IdentificationRuntimeException,
         IdentifierInitialisationException, UnknownFormatException, IOException {

      InputStream inputStream = new FileInputStream(file);
      try {
         identificationService.identifyStream("fmt/18", inputStream);

      } catch (UnknownFormatException ex) {
         Assert.assertEquals(MESS_EXCEPT_ERRONE,
               "Aucun format n'a été trouvé avec l'identifiant : fmt/18.", ex
                     .getMessage());
      } finally {
         inputStream.close();
      }
   }

   @Test
   public void identifyStreamServiceCompatibleFmt95()
         throws IdentificationRuntimeException,
         IdentifierInitialisationException, UnknownFormatException, IOException {

      InputStream inputStream = new FileInputStream(doc);
      
      try {
         IdentificationResult result = identificationService.identifyStream(
               FMT_354, inputStream);
   
         EtapeEtResultat etape0 = result.getDetails().get(0);
         EtapeEtResultat etape1 = result.getDetails().get(1);
         Assert.assertEquals(MESS_ERRONE,
               ETAPE_1,
               etape0.getEtape());
         Assert.assertEquals(RESULT_ERRONE, PUUID_FMT_354, etape0
               .getResultat());
   
         Assert.assertEquals(MESS_ERRONE,
               ETAPE_2, etape1.getEtape());
         Assert.assertEquals(RESULT_ERRONE, PUUID_EGAL_IDFORMAT, etape1
               .getResultat());
   
         Assert.assertEquals(RESULT_ERRONE, true, result.isIdentified());
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }
      }

   }
}
