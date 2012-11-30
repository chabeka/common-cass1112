package fr.urssaf.image.commons.droid.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.commons.droid.service.impl.FormatIdentificationServiceImpl;

public class FormatIdentificationServiceTest {

   private FormatIdentificationServiceImpl formatIdService = new FormatIdentificationServiceImpl();

   private String getRessourceFilePath(String cheminRessource) {

      try {
         return this.getClass().getResource(cheminRessource).toURI().getPath();
      } catch (URISyntaxException e) {
         throw new RuntimeException(e);
      }

   }

   @Test
   @Ignore("La méthode d'identification par extension n'est pas encore implémentée")
   public void test_Texte() {

      String cheminFichier = getRessourceFilePath("/jeuTest/FichierTexte.txt");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = false;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertEquals("Le format identifié n'est pas celui attendu", "x-fmt/111",
            idPronom);

   }

   @Test
   public void test_Word2003() {

      String cheminFichier = getRessourceFilePath("/jeuTest/FichierWord.doc");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = false;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertEquals("Le format identifié n'est pas celui attendu", "fmt/40",
            idPronom);

   }

   @Test
   public void test_Xml() {

      String cheminFichier = getRessourceFilePath("/jeuTest/FichierXml1.xml");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = false;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertEquals("Le format identifié n'est pas celui attendu", "fmt/101",
            idPronom);

   }

   @Test
   public void test_Xml_SansEnTete() {

      String cheminFichier = getRessourceFilePath("/jeuTest/FichierXml2.xml");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = false;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertNull("Le format identifié n'est pas celui attendu", idPronom);

   }

   @Test
   public void test_FichierPdf() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.pdf");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = false;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertEquals("Le format identifié n'est pas celui attendu", "fmt/18",
            idPronom);

   }

   @Test
   public void test_ArchiveGzip() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.pdf.gz");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = false;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertEquals("Le format identifié n'est pas celui attendu", "x-fmt/266",
            idPronom);

   }

   @Test
   public void test_ArchiveTar() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.tar");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = false;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertEquals("Le format identifié n'est pas celui attendu", "x-fmt/265",
            idPronom);

   }

   @Test
   public void test_ArchiveZip() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.zip");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = false;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertEquals("Le format identifié n'est pas celui attendu", "x-fmt/263",
            idPronom);

   }

   @Test
   @Ignore("L'analyse des archives gzip n'est pas encore implémentée")
   public void test_Pdf_Dans_ArchiveGzip() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.pdf.gz");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = true;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertEquals("Le format identifié n'est pas celui attendu", "fmt/18",
            idPronom);

   }

   @Test
   @Ignore("L'analyse des archives tar n'est pas encore implémentée")
   public void test_Pdf_Dans_ArchiveTar() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.tar");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = true;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertEquals("Le format identifié n'est pas celui attendu", "fmt/18",
            idPronom);

   }

   @Test
   @Ignore("L'analyse des archives zip n'est pas encore implémentée")
   public void test_Pdf_Dans_ArchiveZip() {

      String cheminFichier = getRessourceFilePath("/jeuTest/pdfa1b.zip");
      File file = new File(cheminFichier);

      boolean analyserContenuArchives = true;

      String idPronom = formatIdService
            .identifie(file, analyserContenuArchives);

      assertEquals("Le format identifié n'est pas celui attendu", "fmt/18",
            idPronom);

   }

}
