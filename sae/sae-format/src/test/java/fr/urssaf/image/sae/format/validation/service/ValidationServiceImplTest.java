package fr.urssaf.image.sae.format.validation.service;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorUnhandledException;
import fr.urssaf.image.sae.format.validation.service.impl.ValidationServiceImpl;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

/**
 * 
 * TU pour la classe {@link ValidationServiceImpl}
 * 
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class ValidationServiceImplTest {

   @Autowired
   private ValidationServiceImpl validationService;

   /**
    * Cas de test : on demande la validation fmt/354 sur un fichier PDF/A1b
    * valide.<br>
    * <br>
    * Résultat attendu : la validation a réussi.
    */
   @Test
   public void validateFile_success_FichierValide()
         throws UnknownFormatException, ValidatorInitialisationException,
         ValidatorUnhandledException, IOException {

      boolean avecFile = Boolean.TRUE;
      validateFileOuStream_success_FichierValide(avecFile);

   }

   /**
    * Cas de test : on demande la validation fmt/354 sur un flux PDF/A1b valide.<br>
    * <br>
    * Résultat attendu : la validation a réussi.
    */
   @Test
   public void validateStream_success_FichierValide()
         throws UnknownFormatException, ValidatorInitialisationException,
         ValidatorUnhandledException, IOException {

      boolean avecFile = Boolean.FALSE;
      validateFileOuStream_success_FichierValide(avecFile);

   }

   private void validateFileOuStream_success_FichierValide(boolean avecFile)
         throws UnknownFormatException, ValidatorInitialisationException,
         ValidatorUnhandledException, IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/validation/PdfaValide.pdf");

      // Appel de la méthode à tester
      ValidationResult result;
      if (avecFile) {
         result = validationService
               .validateFile("fmt/354", ressource.getFile());
      } else {
         result = validationService.validateStream("fmt/354", ressource
               .getInputStream());
      }

      // Vérifications
      Assert.assertNotNull(result);
      Assert.assertTrue("Le fichier aurait dû être validé", result.isValid());
      Assert.assertNull(result.getDetails());

   }

   /**
    * Cas de test : on demande la validation fmt/354 sur un fichier PDF/A1b non
    * valide.<br>
    * <br>
    * Résultat attendu : Le fichier n'est pas validé.
    */
   @Test
   public void validateFile_succes_FichierNonValide()
         throws UnknownFormatException, ValidatorInitialisationException,
         ValidatorUnhandledException, IOException {

      boolean avecFile = Boolean.TRUE;
      validateFileOuFlux_succes_FichierNonValide(avecFile);

   }

   /**
    * Cas de test : on demande la validation fmt/354 sur un flux PDF/A1b non
    * valide.<br>
    * <br>
    * Résultat attendu : Le fichier n'est pas validé.
    */
   @Test
   public void validateStream_succes_FichierNonValide()
         throws UnknownFormatException, ValidatorInitialisationException,
         ValidatorUnhandledException, IOException {

      boolean avecFile = Boolean.FALSE;
      validateFileOuFlux_succes_FichierNonValide(avecFile);

   }

   private void validateFileOuFlux_succes_FichierNonValide(boolean avecFile)
         throws UnknownFormatException, ValidatorInitialisationException,
         ValidatorUnhandledException, IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/validation/Test.doc");

      // Appel de la méthode à tester
      ValidationResult result;
      if (avecFile) {
         result = validationService
               .validateFile("fmt/354", ressource.getFile());
      } else {
         result = validationService.validateStream("fmt/354", ressource
               .getInputStream());
      }

      // Vérifications
      Assert.assertNotNull(result);
      Assert.assertFalse("Le fichier n'aurait pas dû être validé", result
            .isValid());
      Assert
            .assertTrue(
                  "On aurait dû avoir des traces dans les détails de l'échec de validation",
                  result.getDetails().size() > 0);

   }

   /**
    * Cas de test : on demande la validation fmt/354 sur un fichier qui n'existe
    * pas<br>
    * <br>
    * Résultat attendu : Levée d'une exception avec un message précis
    */
   @Test
   public void validateFile_failure_FichierInexistant()
         throws UnknownFormatException, ValidatorInitialisationException,
         ValidatorUnhandledException {

      try {

         validationService.validateFile("fmt/354",
               new File("fichierInexistant"));

         Assert.fail("Une exception aurait dû être levée.");

      } catch (IllegalArgumentException ex) {

         Assert.assertEquals("Le message de l'exception est incorrect",
               "Le fichier passé en paramètre est introuvable.", ex
                     .getMessage());

      }
   }

   /**
    * Cas de test : on demande la validation d'un fichier par rapport à un
    * format qui n'existe pas dans le référentiel des formats<br>
    * <br>
    * Résultat attendu : Levée d'une exception avec un message spécifique
    */
   @Test
   public void validateFile_failure_FormatInexistantDansReferentielFormats()
         throws ValidatorInitialisationException, ValidatorUnhandledException,
         IOException {

      boolean avecFile = Boolean.TRUE;
      validateFileOuStream_failure_FormatInexistantDansReferentielFormats(avecFile);

   }

   /**
    * Cas de test : on demande la validation d'un flux par rapport à un format
    * qui n'existe pas dans le référentiel des formats<br>
    * <br>
    * Résultat attendu : Levée d'une exception avec un message spécifique
    */
   @Test
   public void validateStream_failure_FormatInexistantDansReferentielFormats()
         throws ValidatorInitialisationException, ValidatorUnhandledException,
         IOException {

      boolean avecFile = Boolean.FALSE;
      validateFileOuStream_failure_FormatInexistantDansReferentielFormats(avecFile);

   }

   private void validateFileOuStream_failure_FormatInexistantDansReferentielFormats(
         boolean avecFile) throws ValidatorInitialisationException,
         ValidatorUnhandledException, IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/validation/PdfaValide.pdf");

      try {

         if (avecFile) {
            validationService.validateFile("fmt/Inexistant", ressource
                  .getFile());
         } else {
            validationService.validateStream("fmt/Inexistant", ressource
                  .getInputStream());
         }

         Assert.fail("Une exception aurait dû être levée.");

      } catch (UnknownFormatException ex) {

         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "Aucun format n'a été trouvé avec l'identifiant : fmt/Inexistant.",
                     ex.getMessage());

      }

   }

}
