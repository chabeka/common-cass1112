package fr.urssaf.image.commons.pdfbox.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import javax.activation.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.preflight.utils.ByteArrayDataSource;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.commons.pdfbox.service.impl.FormatValidationServiceImpl;

/**
 * Tests unitaires de la classe FormatValidationService
 */
@SuppressWarnings("PMD")
public class FormatValidationServiceTest {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(FormatValidationServiceTest.class);

   private File getFileFromResource(String ressourcePath) {
      try {
         return new File(this.getClass().getResource(ressourcePath).toURI()
               .getPath());
      } catch (URISyntaxException ex) {
         throw new RuntimeException(ex);
      }
   }

   @Test
   public void test_conforme() throws FormatValidationException {

      File file = getFileFromResource("/pdf/conformes/text_to_pdfa.pdf");

      FormatValidationService formatValService = new FormatValidationServiceImpl();

      List<String> erreurs = formatValService.validate(file);

      assertNotNull(
            "La liste renvoyée par le service ne devrait pas être null",
            erreurs);

      assertTrue("La validation aurait dû réussir", erreurs.isEmpty());

   }

   @Test
   public void test_nonconforme() throws FormatValidationException {

      File file = getFileFromResource("/pdf/nonconformes/bug1771.pdf");

      FormatValidationService formatValService = new FormatValidationServiceImpl();

      List<String> erreurs = formatValService.validate(file);

      assertNotNull(
            "La liste renvoyée par le service ne devrait pas être null",
            erreurs);

      assertFalse("La validation aurait dû échoué", erreurs.isEmpty());

   }

   @Test
   public void test_pas_pdf() throws FormatValidationException {

      File file = getFileFromResource("/pdf/autres/toto.xml");

      FormatValidationService formatValService = new FormatValidationServiceImpl();

      List<String> erreurs = formatValService.validate(file);

      assertNotNull(
            "La liste renvoyée par le service ne devrait pas être null",
            erreurs);

      assertFalse("La validation aurait dû échoué", erreurs.isEmpty());

   }

   /**
    * Le fichier pdfa1b.pdf fait planter la validation
    */
   @Test(expected = FormatValidationException.class)
   public void test_plantage() throws FormatValidationException {

      File file = getFileFromResource("/pdf/conformes/pdfa1b.pdf");

      FormatValidationService formatValService = new FormatValidationServiceImpl();

      formatValService.validate(file);

   }

   @Test
   public void test_from_Datasource() throws IOException,
         FormatValidationException {

      File file = getFileFromResource("/pdf/conformes/text_to_pdfa.pdf");

      InputStream inputStream = new FileInputStream(file);

      DataSource dataSource = new ByteArrayDataSource(inputStream);

      FormatValidationService formatValService = new FormatValidationServiceImpl();

      List<String> erreurs = formatValService.validate(dataSource);

      assertNotNull(
            "La liste renvoyée par le service ne devrait pas être null",
            erreurs);

      assertTrue("La validation aurait dû réussir", erreurs.isEmpty());

   }

   @Test
   public void test_from_InputStream() throws IOException,
         FormatValidationException {

      File file = getFileFromResource("/pdf/conformes/text_to_pdfa.pdf");

      InputStream inputStream = new FileInputStream(file);

      FormatValidationService formatValService = new FormatValidationServiceImpl();

      List<String> erreurs = formatValService.validate(inputStream);

      assertNotNull(
            "La liste renvoyée par le service ne devrait pas être null",
            erreurs);

      assertTrue("La validation aurait dû réussir", erreurs.isEmpty());

   }

   @Test
   public void test_from_ByteArray() throws IOException,
         FormatValidationException {

      File file = getFileFromResource("/pdf/conformes/text_to_pdfa.pdf");

      InputStream inputStream = new FileInputStream(file);

      byte[] data = IOUtils.toByteArray(inputStream);

      FormatValidationService formatValService = new FormatValidationServiceImpl();

      List<String> erreurs = formatValService.validate(data);

      assertNotNull(
            "La liste renvoyée par le service ne devrait pas être null",
            erreurs);

      assertTrue("La validation aurait dû réussir", erreurs.isEmpty());

   }

   /**
    * Il ne s'agit pas d'un vrai TU, mais juste d'un moyen de lancer une
    * validation sur un fichier
    */
   @Test
   @Ignore("Ceci n'est pas un vrai TU")
   public void verif_conformite_sur_un_fichier()
         throws FormatValidationException {

      String path = "S:/produits/Qualite/Projet_ae/Documentation refonte/Refonte/Développement/00015 - Gestion des formats/01 - Eléments de support/Isartor testsuite/PDFA-1b/6.3 Fonts/6.3.3 Composite fonts/6.3.3.3 CMaps/isartor-6-3-3-3-t01-fail-a.pdf";
      File file = new File(path);

      FormatValidationService formatValService = new FormatValidationServiceImpl();

      LOGGER.debug("Validation du fichier {}", path);
      List<String> erreurs = formatValService.validate(file);

      if (erreurs.size() == 0) {
         LOGGER.debug("Fichier valide");
      } else {
         LOGGER.debug("Fichier non valide");
         LOGGER.debug("Erreurs : {}", erreurs);
      }

   }

   /**
    * Il ne s'agit pas d'un vrai TU, mais juste d'un moyen de lancer une
    * validation sur tous les fichiers d'un répertoire
    */
   @SuppressWarnings("unchecked")
   @Test
   @Ignore("Ceci n'est pas un vrai TU")
   public void verif_conformite_sur_un_repertoire() {

      // Chemin du répertoire à tester

      // Bavaria - fichiers conformes
      // String path =
      // "S:/produits/Qualite/Projet_ae/Documentation refonte/Refonte/Développement/00015 - Gestion des formats/01 - Eléments de support/Bavaria testsuite/conforming";

      // Bavaria - fichiers non conformes
      // String path =
      // "S:/produits/Qualite/Projet_ae/Documentation refonte/Refonte/Développement/00015 - Gestion des formats/01 - Eléments de support/Bavaria testsuite/nonconforming";

      // Bavaria - fichiers unclear
      // String path =
      // "S:/produits/Qualite/Projet_ae/Documentation refonte/Refonte/Développement/00015 - Gestion des formats/01 - Eléments de support/Bavaria testsuite/unclear";

      // Isartor
      // String path =
      // "S:/produits/Qualite/Projet_ae/Documentation refonte/Refonte/Développement/00015 - Gestion des formats/01 - Eléments de support/Isartor testsuite/PDFA-1b/";

      // CIRTIL
      // String path =
      // "S:/produits/Qualite/Projet_ae/Documentation refonte/Refonte/Développement/00015 - Gestion des formats/01 - Eléments de support/PDF_récupéré_serveur_intégration_saeint3/";
      String path = "S:/produits/Qualite/Projet_ae/Documentation refonte/Refonte/Développement/00015 - Gestion des formats/01 - Eléments de support/PDF_dans_le_SAE_de_prod/";

      // Instantiation du service
      FormatValidationService formatValService = new FormatValidationServiceImpl();

      // Récupère la liste des fichiers *.pdf présents dans le répertoire
      List<File> files = (List<File>) FileUtils.listFiles(new File(path),
            new String[] { "pdf" }, true);

      // Tri par ordre alphabétique de nom de fichier, case insensitive
      Collections.sort(files);

      // On va mettre dans un StringBuilder l'ensemble des résultats
      // de validation, dans un format CSV :
      // chemin_fichier;OK/KO/Erreur
      StringBuilder sbResult = new StringBuilder();

      // Boucle sur la liste des fichiers
      String cheminFichier;
      List<String> erreurs;
      String crlf = "\r\n";
      int nbOk = 0;
      int nbKo = 0;
      int nbErreur = 0;
      for (File file : files) {

         cheminFichier = file.getAbsolutePath();

         LOGGER.debug("Validation de {}", cheminFichier);

         try {

            erreurs = formatValService.validate(file);

            if (erreurs.isEmpty()) {
               sbResult.append(String.format("%s;OK", cheminFichier));
               nbOk++;
            } else {
               sbResult.append(String.format("%s;KO", cheminFichier));
               nbKo++;
            }

         } catch (FormatValidationException ex) {

            LOGGER.debug("Une exception de validation a été levée : {}", ex);
            sbResult.append(String.format("%s;Erreur", cheminFichier));
            nbErreur++;
         }

         sbResult.append(crlf);

      }

      LOGGER.debug("Nombre OK : {}", nbOk);
      LOGGER.debug("Nombre KO : {}", nbKo);
      LOGGER.debug("Nombre Erreur : {}", nbErreur);
      LOGGER.debug("Résumé du résultat en CSV : \r\n{}", sbResult);

   }

}
