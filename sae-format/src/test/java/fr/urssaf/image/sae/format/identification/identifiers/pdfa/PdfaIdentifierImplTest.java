package fr.urssaf.image.sae.format.identification.identifiers.pdfa;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentificationRuntimeException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentifierInitialisationException;
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

   /**
    * Cas de test : Identification PDF/A1b pour un fichier qui va être détecté
    * par Droid comme un fmt/354.<br>
    * <br>
    * Résultat attendu : L'identification est positive, et le détail de
    * l'identification précise que Droid a détecté un fmt/354 (pas de passage
    * par les formats compatibles)
    */
   @Test
   public void identifyFile_success() throws IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/identification/fmt-354.pdf");

      // Appel de la méthode à tester
      IdentificationResult result = pdfaIdentifier.identifyFile("fmt/354",
            ressource.getFile());

      // Vérifications

      // Résultat final : le fichier est identifié
      Assert.assertTrue("Le fichier aurait dû être identifié", result
            .isIdentified());

      // Détails, 1ère étape : DROID renvoie fmt/354
      EtapeEtResultat etape1 = result.getDetails().get(0);
      Assert.assertEquals(
            "L'étape 1 de l'identification en correspond pas à l'attendu",
            "Etape 1 : Récupération du PUUID à partir de DROID.", etape1
                  .getEtape());
      Assert.assertEquals(
            "L'étape 1 de l'identification en correspond pas à l'attendu",
            "PUUID : fmt/354", etape1.getResultat());

      // Détails, 2ème étape : Le format identifié par DROID est directement le
      // fmt/354
      EtapeEtResultat etape2 = result.getDetails().get(1);
      Assert.assertEquals(
            "L'étape 2 de l'identification en correspond pas à l'attendu",
            "Etape 2 : Comparaison du PUUID avec idFormat.", etape2.getEtape());
      Assert.assertEquals(
            "L'étape 2 de l'identification en correspond pas à l'attendu",
            "PUUID = IDFORMAT.", etape2.getResultat());

   }

   /**
    * Cas de test : Identification PDF/A1b pour un stream qui va être détecté
    * par Droid comme un fmt/18, qui est dans la liste des formats compatibles
    * au fmt/354.<br>
    * <br>
    * Résultat attendu : L'identification est positive, et le détail de
    * l'identification précise que l'algo est passé par les formats compatibles.
    */
   @Test
   public void identifyStream_success_CompatibleFmt18()
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      identifyStream_success_compatible("/identification/fmt-18.pdf", "fmt/18");

   }

   /**
    * Cas de test : Identification PDF/A1b pour un stream qui va être détecté
    * par Droid comme un fmt/95, qui est dans la liste des formats compatibles
    * au fmt/354.<br>
    * <br>
    * Résultat attendu : L'identification est positive, et le détail de
    * l'identification précise que l'algo est passé par les formats compatibles.
    */
   @Test
   public void identifyStream_success_CompatibleFmt95()
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      identifyStream_success_compatible("/identification/fmt-95.pdf", "fmt/95");

   }

   /**
    * Cas de test : Identification PDF/A1b pour un stream qui va être détecté
    * par Droid comme un fmt/276, qui est dans la liste des formats compatibles
    * au fmt/354.<br>
    * <br>
    * Résultat attendu : L'identification est positive, et le détail de
    * l'identification précise que l'algo est passé par les formats compatibles.
    */
   @Test
   public void identifyStream_success_CompatibleFmt276()
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      identifyStream_success_compatible("/identification/fmt-276.pdf",
            "fmt/276");

   }

   /**
    * Cas de test : Identification PDF/A1b pour un stream qui va être détecté
    * par Droid comme un fmt/20, qui est dans la liste des formats compatibles
    * au fmt/354.<br>
    * <br>
    * Résultat attendu : L'identification est positive, et le détail de
    * l'identification précise que l'algo est passé par les formats compatibles.
    */
   @Test
   public void identifyStream_success_CompatibleFmt20()
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      identifyStream_success_compatible("/identification/fmt-20.pdf", "fmt/20");

   }

   private void identifyStream_success_compatible(String fichierRessource,
         String idFormatDetecteDroid) throws IdentifierInitialisationException,
         UnknownFormatException, IOException {

      ClassPathResource ressource = new ClassPathResource(fichierRessource);
      InputStream inputStream = ressource.getInputStream();

      IdentificationResult result;
      try {
         // Appel de la méthode à tester
         result = pdfaIdentifier.identifyStream("fmt/354", inputStream,
               fichierRessource);
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }
      }

      // Vérifications

      // Résultat final : le fichier est identifié
      Assert.assertTrue("Le fichier aurait dû être identifié", result
            .isIdentified());

      // Détails, 1ère étape : DROID renvoie fmt/95
      EtapeEtResultat etape1 = result.getDetails().get(0);
      Assert.assertEquals(
            "L'étape 1 de l'identification en correspond pas à l'attendu",
            "Etape 1 : Récupération du PUUID à partir de DROID.", etape1
                  .getEtape());
      Assert.assertEquals(
            "L'étape 1 de l'identification en correspond pas à l'attendu",
            String.format("PUUID : %s", idFormatDetecteDroid), etape1
                  .getResultat());

      // Détails, 2ème étape : Le format identifié par DROID fait partie des
      // formats compatibles
      EtapeEtResultat etape2 = result.getDetails().get(1);
      Assert.assertEquals(
            "L'étape 2 de l'identification en correspond pas à l'attendu",
            "Etape 2 : Comparaison du PUUID avec idFormat.", etape2.getEtape());
      Assert
            .assertEquals(
                  "L'étape 2 de l'identification en correspond pas à l'attendu",
                  "PUUID différent de IDFORMAT mais fait partie de la liste des formats compatibles.",
                  etape2.getResultat());

   }

   /**
    * Cas de test : Identification PDF/A1b pour un fichier qui va être détecté
    * par Droid comme un fmt/40 (qui ne fait pas partie des formats
    * compatibles).<br>
    * <br>
    * Résultat attendu : L'identification est négative.
    */
   @Test
   public void identifyFile_success_NonCompatible() throws IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/identification/fmt-40.doc");

      // Appel de la méthode à tester
      IdentificationResult result = pdfaIdentifier.identifyFile("fmt/354",
            ressource.getFile());

      // Vérifications

      // Résultat final : le fichier n'est pas identifié
      Assert.assertFalse("Le fichier n'aurait pas dû être identifié", result
            .isIdentified());

      // Détails, 1ère étape : DROID renvoie fmt/40
      EtapeEtResultat etape1 = result.getDetails().get(0);
      Assert.assertEquals(
            "L'étape 1 de l'identification en correspond pas à l'attendu",
            "Etape 1 : Récupération du PUUID à partir de DROID.", etape1
                  .getEtape());
      Assert.assertEquals(
            "L'étape 1 de l'identification en correspond pas à l'attendu",
            "PUUID : fmt/40", etape1.getResultat());

      // Détails, 2ème étape : Le format identifié par DROID est directement le
      // fmt/354
      EtapeEtResultat etape2 = result.getDetails().get(1);
      Assert.assertEquals(
            "L'étape 2 de l'identification en correspond pas à l'attendu",
            "Etape 2 : Comparaison du PUUID avec idFormat.", etape2.getEtape());
      Assert
            .assertEquals(
                  "L'étape 2 de l'identification en correspond pas à l'attendu",
                  "PUUID différent de IDFORMAT et ne fait pas partie de la liste des formats compatibles.",
                  etape2.getResultat());

   }

   /**
    * Cas de test : Appel de l'identifier PDF/A1b sur un identifiant de format
    * différent de fmt/354<br>
    * <br>
    * Résultat attendu : Levée d'une RuntimeException avec un message précis
    */
   @Test
   public void identifyFile_failure_NonFmt354() throws IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/identification/fmt-354.pdf");

      // Appel de la méthode à tester
      try {

         pdfaIdentifier.identifyFile("NonFmt354", ressource.getFile());

         Assert
               .fail("Une exception IdentificationRuntimeException aurait dû être levée");

      } catch (IdentificationRuntimeException ex) {

         // Vérification du message
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "Erreur technique: Le bean d'identification des PDF/A 1b (fmt/354) a été sollicité pour identifier un autre format (NonFmt354)",
                     ex.getMessage());

      }

   }

   /**
    * Cas de test : Identification PDF/A1b pour un stream issu d'un fichier
    * texte qui doit être détecté par Droid comme un x-fmt/111.<br>
    * Pour cela, Droid a besoin de connaître l'extension du fichier qu'il
    * analyse, sinon pour ce cas il n'est pas capable de déterminer le code
    * Pronom (cas du fichier texte).<br>
    * Or, l'analyse par Stream passe par l'écriture d'un fichier temporaire,
    * dont l'extension doit être la même que celle du fichier dont a été tiré le
    * stream.<br>
    * Ce test couvre le problème décrit dans le Redmine 5416.<br>
    * <br>
    * Résultat attendu : L'identification est négative, et surtout sans plantage
    */
   @Test
   public void identifyStream_success_FichierTexte_LextensionCompte()
         throws IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/identification/x-fmt-111.txt");

      // Appel de la méthode à tester
      IdentificationResult result = pdfaIdentifier.identifyStream("fmt/354",
            ressource.getInputStream(), "x-fmt-111.txt");

      // Vérifications

      // Résultat final : le fichier ne doit pas être identifié
      Assert.assertFalse("Le fichier n'aurait pas dû être identifié", result
            .isIdentified());

   }

   /**
    * Cas de test : Identification PDF/A1b pour un fichier dont Droid n'arrive
    * pas à déterminer le code Pronom<br>
    * <br>
    * Résultat attendu : L'identification est négative, et surtout sans plantage<br>
    * <br>
    * Ce cas couvre le ticket Redmine 5415
    */
   @Test
   public void identifyFile_success_NonIdentifie() throws IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/identification/non_identifie_fichier_texte_renomme_en_pdf.pdf");

      // Appel de la méthode à tester
      IdentificationResult result = pdfaIdentifier.identifyFile("fmt/354",
            ressource.getFile());

      // Vérifications

      // Résultat final : le fichier ne doit pas être identifié
      Assert.assertFalse("Le fichier n'aurait pas dû être identifié", result
            .isIdentified());

   }

}
