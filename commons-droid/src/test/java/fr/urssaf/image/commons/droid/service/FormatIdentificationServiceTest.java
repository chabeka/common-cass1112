package fr.urssaf.image.commons.droid.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.droid.exception.FormatIdentificationRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-commons-droid.xml")
@SuppressWarnings("PMD")
public class FormatIdentificationServiceTest {

   private static Logger LOGGER = LoggerFactory
         .getLogger(FormatIdentificationServiceTest.class);

   @Autowired
   private FormatIdentificationService formatIdService;

   private String getRessourceFilePath(String cheminRessource) {

      try {
         return this.getClass().getResource(cheminRessource).toURI().getPath();
      } catch (URISyntaxException e) {
         throw new RuntimeException(e);
      }

   }

   @Test
   public void test_Texte() {

      String cheminFichier = getRessourceFilePath("/jeuTest/FichierTexte.txt");
      File file = new File(cheminFichier);

      String idPronom = formatIdService.identifie(file);

      assertEquals("Le format identifié n'est pas celui attendu", "x-fmt/111",
            idPronom);

   }

   @Test
   public void test_Word2003() {

      String cheminFichier = getRessourceFilePath("/jeuTest/FichierWord.doc");
      File file = new File(cheminFichier);

      String idPronom = formatIdService.identifie(file);

      assertEquals("Le format identifié n'est pas celui attendu", "fmt/40",
            idPronom);

   }

   @Test
   public void test_Xml() {

      String cheminFichier = getRessourceFilePath("/jeuTest/FichierXml1.xml");
      File file = new File(cheminFichier);

      String idPronom = formatIdService.identifie(file);

      assertEquals("Le format identifié n'est pas celui attendu", "fmt/101",
            idPronom);

   }

   @Test
   public void test_Xml_SansEnTete() {

      String cheminFichier = getRessourceFilePath("/jeuTest/FichierXml2.xml");
      File file = new File(cheminFichier);

      String idPronom = formatIdService.identifie(file);

      assertNull("Le format identifié n'est pas celui attendu", idPronom);

   }

   @Test
   public void test_FichierPdf() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.pdf");
      File file = new File(cheminFichier);

      String idPronom = formatIdService.identifie(file);

      assertEquals("Le format identifié n'est pas celui attendu", "fmt/18",
            idPronom);

   }

   @Test
   public void test_ArchiveGzip() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.pdf.gz");
      File file = new File(cheminFichier);

      String idPronom = formatIdService.identifie(file);

      assertEquals("Le format identifié n'est pas celui attendu", "x-fmt/266",
            idPronom);

   }

   @Test
   public void test_ArchiveTar() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.tar");
      File file = new File(cheminFichier);

      String idPronom = formatIdService.identifie(file);

      assertEquals("Le format identifié n'est pas celui attendu", "x-fmt/265",
            idPronom);

   }

   @Test
   public void test_ArchiveZip() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.zip");
      File file = new File(cheminFichier);

      String idPronom = formatIdService.identifie(file);

      assertEquals("Le format identifié n'est pas celui attendu", "x-fmt/263",
            idPronom);

   }

   @Test
   @Ignore("Ceci n'est pas un vrai TU")
   public void analyseRepertoire() {

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

      // Récupère la liste des fichiers présents dans le répertoire
      // String[] filtresExtension = new String[] { "pdf" };
      String[] filtresExtension = null;
      boolean recurse = true;
      List<File> files = (List<File>) FileUtils.listFiles(new File(path),
            filtresExtension, recurse);

      // Tri par ordre alphabétique de nom de fichier, case insensitive
      Collections.sort(files);

      // On va mettre dans un StringBuilder l'ensemble des résultats
      // d'identification, dans un format CSV :
      // chemin_fichier;idPronom
      StringBuilder sbResult = new StringBuilder();

      // Boucle sur la liste des fichiers
      String cheminFichier;
      String idPronom;
      String crlf = "\r\n";
      int nbFmt354 = 0;
      for (File file : files) {

         cheminFichier = file.getAbsolutePath();

         LOGGER.debug("Validation de {}", cheminFichier);

         try {

            idPronom = formatIdService.identifie(file);

            sbResult.append(String.format("%s;%s", cheminFichier, idPronom));
            
            if (StringUtils.equalsIgnoreCase(idPronom, "fmt/354")) {
               nbFmt354++;
            }

         } catch (FormatIdentificationRuntimeException ex) {

            LOGGER.debug("Une exception de validation a été levée : {}", ex);
            sbResult.append(String.format("%s;Erreur", cheminFichier));

         }

         sbResult.append(crlf);

      }

      LOGGER.debug("Nombre fmt/354 : {}", nbFmt354);
      LOGGER.debug("Nombre autres : {}", files.size()-nbFmt354);
      LOGGER.debug("Résumé du résultat en CSV : \r\n{}", sbResult);

   }

}
