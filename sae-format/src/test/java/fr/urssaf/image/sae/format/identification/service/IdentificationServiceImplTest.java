package fr.urssaf.image.sae.format.identification.service;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentifierInitialisationException;
import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;
import fr.urssaf.image.sae.format.identification.service.impl.IdentificationServiceImpl;

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

   /**
    * Cas de test : on demande l'identification fmt/354 sur un fichier PDF/A1b
    * valide.<br>
    * <br>
    * Résultat attendu : l'identification a réussi.
    */
   @Test
   public void identifyFile_success_idOK_surFichier()
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/identification/fmt-354.pdf");

      // Appel de la méthode à tester
      IdentificationResult result = identificationService.identifyFile(
            "fmt/354", ressource.getFile());

      // Vérifications
      // On vérifie uniquement que le fichier a été identifié, la partie détails
      // dépend
      // du bean utilisé pour faire l'identification. Cette dernière est donc
      // testé dans
      // la classe de test du bean en question
      Assert.assertTrue("Le fichier aurait dû être identifié", result
            .isIdentified());

   }

   /**
    * Cas de test : on demande l'identification fmt/354 sur un flux PDF/A1b
    * valide.<br>
    * <br>
    * Résultat attendu : l'identification a réussi.
    */
   @Test
   public void identifyFile_success_idOK_surFlux()
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/identification/fmt-354.pdf");

      // Appel de la méthode à tester
      IdentificationResult result = identificationService.identifyStream(
            "fmt/354", ressource.getInputStream(), "fmt-354.pdf");

      // Vérifications
      // On vérifie uniquement que le fichier a été identifié, la partie détails
      // dépend
      // du bean utilisé pour faire l'identification. Cette dernière est donc
      // testé dans
      // la classe de test du bean en question
      Assert.assertTrue("Le fichier aurait dû être identifié", result
            .isIdentified());

   }

   /**
    * Cas de test : on demande l'identification fmt/354 sur un fichier Word.<br>
    * <br>
    * Résultat attendu : l'identification échoue.
    */
   @Test
   public void identifyFile_success_idKO()
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/identification/fmt-40.doc");

      // Appel de la méthode à tester
      IdentificationResult result = identificationService.identifyFile(
            "fmt/354", ressource.getFile());

      // Vérifications
      // On vérifie uniquement que le fichier a été identifié, la partie détails
      // dépend
      // du bean utilisé pour faire l'identification. Cette dernière est donc
      // testé dans
      // la classe de test du bean en question
      Assert.assertFalse("Le fichier n'aurait pas dû être identifié", result
            .isIdentified());

   }

   /**
    * Cas de test : on demande l'identification d'un format inexistant dans le
    * référentiel des formats.<br>
    * <br>
    * Résultat attendu : Levée d'une exception avec un message précis
    */
   @Test
   public void identifyFile_failure_idFormatInexistant()
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/identification/fmt-354.pdf");

      // Appel de la méthode à tester
      try {

         identificationService.identifyFile("fmt/Inexistant", ressource
               .getFile());

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