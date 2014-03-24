package fr.urssaf.image.sae.services.controles.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.services.controles.model.ControleFormatSucces;
import fr.urssaf.image.sae.services.exception.format.FormatRuntimeException;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;

/**
 * Tests unitaires de la classe {@link SAEControlesCaptureFormatSupport}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAEControlesCaptureFormatSupportTest {

   @Autowired
   private SAEControlesCaptureFormatSupport controleFormatSupport;

   private SAEDocument buildSaeDocument() {
      SAEDocument doc = new SAEDocument();
      List<SAEMetadata> metas = new ArrayList<SAEMetadata>();
      doc.setMetadatas(metas);
      return doc;
   }

   private void ajouteMeta(SAEDocument doc, String codeLong, String valeur) {
      SAEMetadata meta = new SAEMetadata(codeLong, valeur);
      doc.getMetadatas().add(meta);
   }

   private void ajoutProfilPdf(List<FormatControlProfil> listControlProfil,
         boolean avecIdent, boolean avecValid, boolean modeStrict) {
      FormatControlProfil formatControlProfil = new FormatControlProfil();
      listControlProfil.add(formatControlProfil);
      formatControlProfil.setFormatCode("LE_PROFIL");
      formatControlProfil.setDescription("Identification script PDF/A 1b");
      FormatProfil controlProfil = new FormatProfil();
      controlProfil.setFileFormat("fmt/354");
      controlProfil.setFormatIdentification(avecIdent);
      controlProfil.setFormatValidation(avecValid);
      if (modeStrict) {
         controlProfil.setFormatValidationMode("STRICT");
      } else {
         controlProfil.setFormatValidationMode("MONITOR");
      }
      formatControlProfil.setControlProfil(controlProfil);
   }

   private void ajoutProfilFormatBidonIdentStrict(
         List<FormatControlProfil> listControlProfil) {
      FormatControlProfil formatControlProfil = new FormatControlProfil();
      listControlProfil.add(formatControlProfil);
      formatControlProfil.setFormatCode("LE_PROFIL");
      formatControlProfil.setDescription("Identification script PDF/A 1b");
      FormatProfil controlProfil = new FormatProfil();
      controlProfil.setFileFormat("fmt/123");
      controlProfil.setFormatIdentification(true);
      controlProfil.setFormatValidation(true);
      controlProfil.setFormatValidationMode("STRICT");
      formatControlProfil.setControlProfil(controlProfil);
   }

   private void definitFluxDansDocument(SAEDocument saeDocument,
         String ressource) {
      ClassPathResource resource = new ClassPathResource(ressource);
      byte[] data = null;
      try {
         data = IOUtils.toByteArray(resource.getInputStream());
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      ByteArrayDataSource bads = new ByteArrayDataSource(data, "typeMIME");
      DataHandler dataHandler = new DataHandler(bads);
      saeDocument.setContent(dataHandler);
      saeDocument.setFileName(resource.getFilename());
   }

   private void definitCheminFichierDansDocument(SAEDocument saeDocument,
         String ressource) {
      ClassPathResource resource = new ClassPathResource(ressource);
      try {
         saeDocument.setFilePath(resource.getFile().getAbsolutePath());
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * Cas de test : Aucun profil de contrôle passé à la méthode de contrôle.<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_AucunProfilDeControle()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");

      // Construit une liste de profil vide, cad le mininum requis pour le test
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();

      // Appel de la méthode à tester
      ControleFormatSucces resultatControle = controleFormatSupport
            .checkFormat("junit", saeDocument, listControlProfil);

      // Vérification du résultat attendu
      Assert
            .assertNull(
                  "L'identifiant du format du profil de contrôle ne devrait pas être renseigné",
                  resultatControle.getIdFormatDuProfilControle());
      Assert.assertFalse("L'identification ne devrait pas être activée",
            resultatControle.isIdentificationActivee());
      Assert.assertFalse("L'identification n'aurait pas dû être réalisée",
            resultatControle.isIdentificationRealisee());
      Assert.assertFalse(
            "L'identification n'aurait pas dû échoué en mode monitor",
            resultatControle.isIdentificationEchecMonitor());
      Assert.assertFalse("La validation ne devrait pas être activée",
            resultatControle.isValidationActivee());
      Assert.assertFalse("La validation n'aurait pas dû être réalisée",
            resultatControle.isValidationRealisee());
      Assert.assertFalse(
            "La validation n'aurait pas dû échoué en mode monitor",
            resultatControle.isValidationEchecMonitor());

   }

   /**
    * Cas de test : La métadonnée FormatFichier n'est pas renseignée.<br>
    * Résultat attendu : Une exception UnknownFormatException avec un message
    * précis
    */
   @Test
   public final void checkFormat_failure_MetaFormatFichierNonRenseigne() {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      // NB: on ne spécifie pas la métadonnée FormatFichier

      // Construit une liste de profil avec au moins 1 profil
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.FALSE;
      boolean modeStrict = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      try {

         // Appel de la méthode
         controleFormatSupport.checkFormat("junit", saeDocument,
               listControlProfil);

         // Le test échoue si on arrive ici : on aurait dû avoir une exception
         Assert.fail("On aurait dû avoir une levée d'exception");

      } catch (UnknownFormatException e) {

         // Contrôle le message de l'exception
         Assert
               .assertEquals(
                     "Le message de l'exception n'est pas celui attendu",
                     "La métadonnée FormatFichier n'est pas renseignée : impossible de continuer le contrôle sur le format de fichier",
                     e.getMessage());

      } catch (ValidationExceptionInvalidFile e) {

         Assert
               .fail("L'exception attendue était une UnknownFormatException alors qu'on a obtenu : "
                     + e);

      }

   }

   /**
    * Cas de test : La métadonnée FormatFichier fait référence à un format de
    * fichier inexistant dans le référentiel des formats<br>
    * Résultat attendu : Levée d'une exception UnknownFormatException avec un
    * message précis.
    */
   @Test
   public final void checkFormat_failure_FormatInexistantDansReferentiel() {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/inexistant");

      // Construit une liste de profil avec au moins 1 profil
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.FALSE;
      boolean modeStrict = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      try {

         // Appel de la méthode
         controleFormatSupport.checkFormat("junit", saeDocument,
               listControlProfil);

         // Le test échoue si on arrive ici : on aurait dû avoir une exception
         Assert.fail("On aurait dû avoir une levée d'exception");

      } catch (UnknownFormatException e) {

         // Contrôle le message de l'exception
         Assert
               .assertEquals(
                     "Le message de l'exception n'est pas celui attendu",
                     "Le format du fichier n'existe pas dans le référentiel : fmt/inexistant",
                     e.getMessage());

      } catch (ValidationExceptionInvalidFile e) {

         Assert
               .fail("L'exception attendue était une UnknownFormatException alors qu'on a obtenu : "
                     + e);

      }

   }

   /**
    * Cas de test : La métadonnée FormatFichier fait référence à un format de
    * fichier inexistant dans le référentiel des formats. De plus, aucun profil
    * de contrôle n'est passé à la méthode<br>
    * Résultat attendu : Levée d'une exception UnknownFormatException avec un
    * message précis.
    */
   @Test
   public final void checkFormat_failure_FormatInexistantDansReferentiel2() {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/inexistant2");

      // Construit une liste de profil avec au moins 1 profil
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();

      // Appel de la méthode à tester
      try {

         // Appel de la méthode
         controleFormatSupport.checkFormat("junit", saeDocument,
               listControlProfil);

         // Le test échoue si on arrive ici : on aurait dû avoir une exception
         Assert.fail("On aurait dû avoir une levée d'exception");

      } catch (UnknownFormatException e) {

         // Contrôle le message de l'exception
         Assert
               .assertEquals(
                     "Le message de l'exception n'est pas celui attendu",
                     "Le format du fichier n'existe pas dans le référentiel : fmt/inexistant2",
                     e.getMessage());

      } catch (ValidationExceptionInvalidFile e) {

         Assert
               .fail("L'exception attendue était une UnknownFormatException alors qu'on a obtenu : "
                     + e);

      }

   }

   /**
    * Cas de test : Dans la liste des profils de contrôle, aucun ne correspond
    * au format déclaré par le client.<br>
    * Résultat attendu : Aucune erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PasProfilDeControleCorrespondantAFormatFichier()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");

      // Construit une liste avec 1 profil qui n'est pas lié au format fmt/354
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      ajoutProfilFormatBidonIdentStrict(listControlProfil);

      // Appel de la méthode à tester
      ControleFormatSucces resultatControle = controleFormatSupport
            .checkFormat("junit", saeDocument, listControlProfil);

      // Vérification du résultat attendu
      Assert
            .assertNull(
                  "L'identifiant du format du profil de contrôle ne devrait pas être renseigné",
                  resultatControle.getIdFormatDuProfilControle());
      Assert.assertFalse("L'identification ne devrait pas être activée",
            resultatControle.isIdentificationActivee());
      Assert.assertFalse("L'identification n'aurait pas dû être réalisé",
            resultatControle.isIdentificationRealisee());
      Assert.assertFalse(
            "L'identification n'aurait pas dû échoué en mode monitor",
            resultatControle.isIdentificationEchecMonitor());
      Assert.assertFalse("La validation ne devrait pas être activée",
            resultatControle.isValidationActivee());
      Assert.assertFalse("La validation n'aurait pas dû être réalisé",
            resultatControle.isValidationRealisee());
      Assert.assertFalse(
            "La validation n'aurait pas dû échoué en mode monitor",
            resultatControle.isValidationEchecMonitor());

   }

   /**
    * Cas de test : 2 profils de contrôle dans la liste correspondent au format
    * de fichier spécifié par le client.<br>
    * Résultat attendu : Levée d'une exception FormatRuntimeException avec un
    * message spécifique
    * 
    */
   @Test
   public final void checkFormat_failure_DeuxProfilsDeControleSurMemeFormatDeFichier() {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");

      // Construit une liste de profil avec 2 profils pointant sur le format de
      // fichier fmt/354
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.FALSE;
      boolean modeStrict = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);
      avecValid = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      try {

         // Appel de la méthode
         controleFormatSupport.checkFormat("junit", saeDocument,
               listControlProfil);

         // Le test échoue si on arrive ici : on aurait dû avoir une exception
         Assert.fail("On aurait dû avoir une levée d'exception");

      } catch (UnknownFormatException e) {

         Assert
               .fail("L'exception attendue était une FormatRuntimeException alors qu'on a obtenu : "
                     + e);

      } catch (ValidationExceptionInvalidFile e) {

         Assert
               .fail("L'exception attendue était une FormatRuntimeException alors qu'on a obtenu : "
                     + e);

      } catch (FormatRuntimeException e) {

         // Contrôle le message de l'exception
         Assert
               .assertEquals(
                     "Le message de l'exception n'est pas celui attendu",
                     "Erreur technique : Plusieurs profils de contrôle (2) peuvent s'appliquer au format de fichier (fmt/354) : on ne sait pas lequel choisir.",
                     e.getMessage());

      }

   }

   /**
    * Cas de test : Identification strict d'un PDF/A 1b valide avec un flux<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfIdentStrictSurFlux()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      boolean avecFlux = true;
      checkFormat_success_PdfIdentStrict(avecFlux);

   }

   /**
    * Cas de test : Identification strict d'un PDF/A 1b valide avec un chemin de
    * fichier<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfIdentStrictSurCheminFichier()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      boolean avecFlux = false;
      checkFormat_success_PdfIdentStrict(avecFlux);

   }

   private void checkFormat_success_PdfIdentStrict(boolean avecFlux)
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      if (avecFlux) {
         definitFluxDansDocument(saeDocument,
               "controleFormat/fmt354_ident-OK_valid-OK.pdf");
      } else {
         definitCheminFichierDansDocument(saeDocument,
               "controleFormat/fmt354_ident-OK_valid-OK.pdf");
      }

      // Construit une liste avec 1 profil d'identification strict du PDF/A 1b
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.FALSE;
      boolean modeStrict = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      ControleFormatSucces resultatControle = controleFormatSupport
            .checkFormat("junit", saeDocument, listControlProfil);

      // Vérification du résultat attendu
      Assert
            .assertEquals(
                  "L'identifiant du format du profil de contrôle à appliquer n'est pas juste",
                  "fmt/354", resultatControle.getIdFormatDuProfilControle());
      Assert.assertTrue("L'identification devrait être activée",
            resultatControle.isIdentificationActivee());
      Assert.assertTrue("L'identification aurait dû être réalisée",
            resultatControle.isIdentificationRealisee());
      Assert.assertFalse(
            "L'identification n'aurait pas dû échoué en mode monitor",
            resultatControle.isIdentificationEchecMonitor());
      Assert.assertFalse("La validation ne devrait pas être activée",
            resultatControle.isValidationActivee());
      Assert.assertFalse("La validation n'aurait pas dû être réalisée",
            resultatControle.isValidationRealisee());
      Assert.assertFalse(
            "La validation n'aurait pas dû échoué en mode monitor",
            resultatControle.isValidationEchecMonitor());
      Assert.assertEquals(avecFlux, resultatControle.isSurFlux());

   }

   /**
    * Cas de test : Validation strict d'un PDF/A 1b valide avec un flux<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfValidStrictSurFlux()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      boolean avecFlux = true;
      checkFormat_success_PdfValidStrict(avecFlux);

   }

   /**
    * Cas de test : Validation strict d'un PDF/A 1b valide avec un chemin de
    * fichier<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfValidStrictSurCheminFichier()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      boolean avecFlux = false;
      checkFormat_success_PdfValidStrict(avecFlux);

   }

   private void checkFormat_success_PdfValidStrict(boolean avecFlux)
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      if (avecFlux) {
         definitFluxDansDocument(saeDocument,
               "controleFormat/fmt354_ident-OK_valid-OK.pdf");
      } else {
         definitCheminFichierDansDocument(saeDocument,
               "controleFormat/fmt354_ident-OK_valid-OK.pdf");
      }

      // Construit une liste avec 1 profil de validation strict du PDF/A 1b
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.FALSE;
      boolean avecValid = Boolean.TRUE;
      boolean modeStrict = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      ControleFormatSucces resultatControle = controleFormatSupport
            .checkFormat("junit", saeDocument, listControlProfil);

      // Vérification du résultat attendu
      Assert
            .assertEquals(
                  "L'identifiant du format du profil de contrôle à appliquer n'est pas juste",
                  "fmt/354", resultatControle.getIdFormatDuProfilControle());
      Assert.assertFalse("L'identification ne devrait pas être activée",
            resultatControle.isIdentificationActivee());
      Assert.assertFalse("L'identification n'aurait pas dû être réalisée",
            resultatControle.isIdentificationRealisee());
      Assert.assertFalse(
            "L'identification n'aurait pas dû échoué en mode monitor",
            resultatControle.isIdentificationEchecMonitor());
      Assert.assertTrue("La validation devrait être activée", resultatControle
            .isValidationActivee());
      Assert.assertTrue("La validation aurait dû être réalisée",
            resultatControle.isValidationRealisee());
      Assert.assertFalse(
            "La validation n'aurait pas dû échoué en mode monitor",
            resultatControle.isValidationEchecMonitor());
      Assert.assertEquals(avecFlux, resultatControle.isSurFlux());

   }

   /**
    * Cas de test : Identification et validation strict d'un PDF/A 1b valide
    * avec un flux<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfIdentValidStrictSurFlux()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      boolean avecFlux = true;
      checkFormat_success_PdfIdentValidStrict(avecFlux);

   }

   /**
    * Cas de test : Identification et validation strict d'un PDF/A 1b valide
    * avec un chemin de fichier<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfIdentValidStrictSurCheminFichier()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      boolean avecFlux = false;
      checkFormat_success_PdfIdentValidStrict(avecFlux);

   }

   private void checkFormat_success_PdfIdentValidStrict(boolean avecFlux)
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      if (avecFlux) {
         definitFluxDansDocument(saeDocument,
               "controleFormat/fmt354_ident-OK_valid-OK.pdf");
      } else {
         definitCheminFichierDansDocument(saeDocument,
               "controleFormat/fmt354_ident-OK_valid-OK.pdf");
      }

      // Construit une liste avec 1 profil d'identification et de validation
      // strict du PDF/A 1b
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.TRUE;
      boolean modeStrict = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      ControleFormatSucces resultatControle = controleFormatSupport
            .checkFormat("junit", saeDocument, listControlProfil);

      // Vérification du résultat attendu
      Assert
            .assertEquals(
                  "L'identifiant du format du profil de contrôle à appliquer n'est pas juste",
                  "fmt/354", resultatControle.getIdFormatDuProfilControle());
      Assert.assertTrue("L'identification devrait être activée",
            resultatControle.isIdentificationActivee());
      Assert.assertTrue("L'identification aurait dû être réalisée",
            resultatControle.isIdentificationRealisee());
      Assert.assertFalse(
            "L'identification n'aurait pas dû échoué en mode monitor",
            resultatControle.isIdentificationEchecMonitor());
      Assert.assertTrue("La validation devrait être activée", resultatControle
            .isValidationActivee());
      Assert.assertTrue("La validation aurait dû être réalisée",
            resultatControle.isValidationRealisee());
      Assert.assertFalse(
            "La validation n'aurait pas dû échoué en mode monitor",
            resultatControle.isValidationEchecMonitor());
      Assert.assertEquals(avecFlux, resultatControle.isSurFlux());

   }

   /**
    * Cas de test : Identification strict d'un mauvais PDF/A 1b avec un flux.<br>
    * Résultat attendu : Levée d'une exception UnknownFormatException avec un
    * message précis
    */
   @Test
   public final void checkFormat_failure_PdfIdentStrictSurFlux_EchecIndent() {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      definitFluxDansDocument(saeDocument, "controleFormat/document_word.doc");

      // Construit une liste de profil avec au moins 1 profil
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.FALSE;
      boolean modeStrict = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      try {

         // Appel de la méthode
         controleFormatSupport.checkFormat("junit", saeDocument,
               listControlProfil);

         // Le test échoue si on arrive ici : on aurait dû avoir une exception
         Assert.fail("On aurait dû avoir une levée d'exception");

      } catch (UnknownFormatException e) {

         // Contrôle le message de l'exception
         Assert.assertEquals(
               "Le message de l'exception n'est pas celui attendu",
               "Le fichier à archiver ne correspond pas au format spécifié.", e
                     .getMessage());

      } catch (ValidationExceptionInvalidFile e) {

         Assert
               .fail("L'exception attendue était une UnknownFormatException alors qu'on a obtenu : "
                     + e);

      }

   }

   /**
    * Cas de test : Validation strict d'un mauvais PDF/A 1b avec un flux.<br>
    * Résultat attendu : Levée d'une exception UnknownFormatException avec un
    * message précis
    */
   @Test
   public final void checkFormat_failure_PdfValidStrictSurFlux_EchecValid() {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      definitFluxDansDocument(saeDocument, "controleFormat/fmt354_valid-KO.pdf");

      // Construit une liste de profil avec au moins 1 profil
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.FALSE;
      boolean avecValid = Boolean.TRUE;
      boolean modeStrict = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      try {

         // Appel de la méthode
         controleFormatSupport.checkFormat("junit", saeDocument,
               listControlProfil);

         // Le test échoue si on arrive ici : on aurait dû avoir une exception
         Assert.fail("On aurait dû avoir une levée d'exception");

      } catch (UnknownFormatException e) {

         // Contrôle le message de l'exception
         Assert.assertEquals(
               "Le message de l'exception n'est pas celui attendu",
               "Le fichier à archiver ne correspond pas au format spécifié.", e
                     .getMessage());

      } catch (ValidationExceptionInvalidFile e) {

         Assert
               .fail("L'exception attendue était une UnknownFormatException alors qu'on a obtenu : "
                     + e);

      }

   }

   /**
    * Cas de test : Identification et validation strict PDF/A 1b avec un chemin
    * de fichier, dont l'identification passe mais pas la validation<br>
    * Résultat attendu : Levée d'une exception UnknownFormatException avec un
    * message précis
    */
   @Test
   public final void checkFormat_failure_PdfIdentValidStrictSurCheminFichier_ReussiteIdent_EchecValid() {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      definitCheminFichierDansDocument(saeDocument,
            "controleFormat/fmt354_ident-OK_valid-KO.pdf");

      // Construit une liste de profil avec au moins 1 profil
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.TRUE;
      boolean modeStrict = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      try {

         // Appel de la méthode
         controleFormatSupport.checkFormat("junit", saeDocument,
               listControlProfil);

         // Le test échoue si on arrive ici : on aurait dû avoir une exception
         Assert.fail("On aurait dû avoir une levée d'exception");

      } catch (UnknownFormatException e) {

         // Contrôle le message de l'exception
         Assert.assertEquals(
               "Le message de l'exception n'est pas celui attendu",
               "Le fichier à archiver ne correspond pas au format spécifié.", e
                     .getMessage());

      } catch (ValidationExceptionInvalidFile e) {

         Assert
               .fail("L'exception attendue était une UnknownFormatException alors qu'on a obtenu : "
                     + e);

      }

   }

   /**
    * Cas de test : Identification Monitor d'un PDF/A 1b non valide avec un
    * chemin de fichier<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfIdentMonitorSurCheminFichier()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      definitCheminFichierDansDocument(saeDocument,
            "controleFormat/document_word.doc");

      // Construction du profil de contrôle
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.FALSE;
      boolean modeStrict = Boolean.FALSE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      ControleFormatSucces resultatControle = controleFormatSupport
            .checkFormat("junit", saeDocument, listControlProfil);

      // Vérification du résultat attendu
      Assert
            .assertEquals(
                  "L'identifiant du format du profil de contrôle à appliquer n'est pas juste",
                  "fmt/354", resultatControle.getIdFormatDuProfilControle());
      Assert.assertTrue("L'identification devrait être activée",
            resultatControle.isIdentificationActivee());
      Assert.assertTrue("L'identification aurait dû être réalisée",
            resultatControle.isIdentificationRealisee());
      Assert.assertTrue("L'identification aurait dû échoué en mode monitor",
            resultatControle.isIdentificationEchecMonitor());
      Assert.assertFalse("La validation ne devrait pas être activée",
            resultatControle.isValidationActivee());
      Assert.assertFalse("La validation n'aurait pas dû être réalisée",
            resultatControle.isValidationRealisee());
      Assert.assertFalse(
            "La validation n'aurait pas dû échoué en mode monitor",
            resultatControle.isValidationEchecMonitor());
      Assert.assertFalse(resultatControle.isSurFlux());

   }

   /**
    * Cas de test : Validation Monitor d'un PDF/A 1b non valide avec un chemin
    * de fichier<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfValidMonitorSurCheminFichier()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      definitCheminFichierDansDocument(saeDocument,
            "controleFormat/document_word.doc");

      // Construction du profil de contrôle
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.FALSE;
      boolean avecValid = Boolean.TRUE;
      boolean modeStrict = Boolean.FALSE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      ControleFormatSucces resultatControle = controleFormatSupport
            .checkFormat("junit", saeDocument, listControlProfil);

      // Vérification du résultat attendu
      Assert
            .assertEquals(
                  "L'identifiant du format du profil de contrôle à appliquer n'est pas juste",
                  "fmt/354", resultatControle.getIdFormatDuProfilControle());
      Assert.assertFalse("L'identification ne devrait pas être activée",
            resultatControle.isIdentificationActivee());
      Assert.assertFalse("L'identification n'aurait pas dû être réalisée",
            resultatControle.isIdentificationRealisee());
      Assert.assertFalse(
            "L'identification n'aurait pas dû échoué en mode monitor",
            resultatControle.isIdentificationEchecMonitor());
      Assert.assertTrue("La validation devrait être activée", resultatControle
            .isValidationActivee());
      Assert.assertTrue("La validation aurait dû être réalisée",
            resultatControle.isValidationRealisee());
      Assert.assertTrue("La validation aurait dû échoué en mode monitor",
            resultatControle.isValidationEchecMonitor());

   }

   /**
    * Cas de test : Identification et validation Monitor d'un PDF/A 1b non
    * valide avec un chemin de fichier<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfIdentValidMonitorSurCheminFichier()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      definitCheminFichierDansDocument(saeDocument,
            "controleFormat/document_word.doc");

      // Construction du profil de contrôle
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.TRUE;
      boolean modeStrict = Boolean.FALSE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      ControleFormatSucces resultatControle = controleFormatSupport
            .checkFormat("junit", saeDocument, listControlProfil);

      // Vérification du résultat attendu
      Assert
            .assertEquals(
                  "L'identifiant du format du profil de contrôle à appliquer n'est pas juste",
                  "fmt/354", resultatControle.getIdFormatDuProfilControle());
      Assert.assertTrue("L'identification devrait être activée",
            resultatControle.isIdentificationActivee());
      Assert.assertTrue("L'identification aurait dû être réalisée",
            resultatControle.isIdentificationRealisee());
      Assert.assertTrue("L'identification aurait dû échoué en mode monitor",
            resultatControle.isIdentificationEchecMonitor());
      Assert.assertTrue("La validation devrait être activée", resultatControle
            .isValidationActivee());
      Assert.assertTrue("La validation aurait dû être réalisée",
            resultatControle.isValidationRealisee());
      Assert.assertTrue("La validation aurait dû échoué en mode monitor",
            resultatControle.isValidationEchecMonitor());
      Assert.assertFalse(resultatControle.isSurFlux());

   }

   /**
    * Cas de test : Identification Monitor d'un PDF/A 1b non identifiable avec
    * un chemin de fichier<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfNonIdentifiable_IdentMonitorSurCheminFichier()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      definitCheminFichierDansDocument(saeDocument,
            "controleFormat/non_identifie_fichier_texte_renomme_en_pdf.pdf");

      // Construction du profil de contrôle
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.FALSE;
      boolean modeStrict = Boolean.FALSE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      ControleFormatSucces resultatControle = controleFormatSupport
            .checkFormat("junit", saeDocument, listControlProfil);

      // Vérification du résultat attendu
      Assert
            .assertEquals(
                  "L'identifiant du format du profil de contrôle à appliquer n'est pas juste",
                  "fmt/354", resultatControle.getIdFormatDuProfilControle());
      Assert.assertTrue("L'identification devrait être activée",
            resultatControle.isIdentificationActivee());
      Assert.assertTrue("L'identification aurait dû être réalisée",
            resultatControle.isIdentificationRealisee());
      Assert.assertTrue("L'identification aurait dû échoué en mode monitor",
            resultatControle.isIdentificationEchecMonitor());
      Assert.assertFalse("La validation ne devrait pas être activée",
            resultatControle.isValidationActivee());
      Assert.assertFalse("La validation n'aurait pas dû être réalisée",
            resultatControle.isValidationRealisee());
      Assert.assertFalse(
            "La validation n'aurait pas dû échoué en mode monitor",
            resultatControle.isValidationEchecMonitor());
      Assert.assertFalse(resultatControle.isSurFlux());

   }

   /**
    * Cas de test : Identification et Validation Monitor d'un PDF/A 1b non
    * identifiable avec un chemin de fichier<br>
    * Résultat attendu : aucun erreur de la méthode checkFormat
    */
   @Test
   public final void checkFormat_success_PdfNonIdentifiable_IdentValidMonitorSurCheminFichier()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      definitCheminFichierDansDocument(saeDocument,
            "controleFormat/non_identifie_fichier_texte_renomme_en_pdf.pdf");

      // Construction du profil de contrôle
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.TRUE;
      boolean modeStrict = Boolean.FALSE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      ControleFormatSucces resultatControle = controleFormatSupport
            .checkFormat("junit", saeDocument, listControlProfil);

      // Vérification du résultat attendu
      Assert
            .assertEquals(
                  "L'identifiant du format du profil de contrôle à appliquer n'est pas juste",
                  "fmt/354", resultatControle.getIdFormatDuProfilControle());
      Assert.assertTrue("L'identification devrait être activée",
            resultatControle.isIdentificationActivee());
      Assert.assertTrue("L'identification aurait dû être réalisée",
            resultatControle.isIdentificationRealisee());
      Assert.assertTrue("L'identification aurait dû échoué en mode monitor",
            resultatControle.isIdentificationEchecMonitor());
      Assert.assertTrue("La validation devrait être activée", resultatControle
            .isValidationActivee());
      Assert.assertTrue("La validation aurait dû être réalisée",
            resultatControle.isValidationRealisee());
      Assert.assertTrue("La validation aurait dû échoué en mode monitor",
            resultatControle.isValidationEchecMonitor());
      Assert.assertFalse(resultatControle.isSurFlux());

   }

   /**
    * Cas de test : Identification Strict d'un PDF/A 1b non identifiable avec un
    * chemin de fichier<br>
    * Résultat attendu : Levée d'une exception UnknownFormatException
    */
   @Test
   public final void checkFormat_success_PdfNonIdentifiable_IdentStrictSurCheminFichier()
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Construit l'objet Document avec le minimum requis pour le test
      SAEDocument saeDocument = buildSaeDocument();
      ajouteMeta(saeDocument, "FormatFichier", "fmt/354");
      definitCheminFichierDansDocument(saeDocument,
            "controleFormat/non_identifie_fichier_texte_renomme_en_pdf.pdf");

      // Construction du profil de contrôle
      List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();
      boolean avecIdent = Boolean.TRUE;
      boolean avecValid = Boolean.FALSE;
      boolean modeStrict = Boolean.TRUE;
      ajoutProfilPdf(listControlProfil, avecIdent, avecValid, modeStrict);

      // Appel de la méthode à tester
      try {

         controleFormatSupport.checkFormat("junit", saeDocument,
               listControlProfil);

         // Le test échoue si on arrive ici : on aurait dû avoir une exception
         Assert.fail("On aurait dû avoir une levée d'exception");

      } catch (UnknownFormatException e) {

         // Contrôle le message de l'exception
         Assert.assertEquals(
               "Le message de l'exception n'est pas celui attendu",
               "Le fichier à archiver ne correspond pas au format spécifié.", e
                     .getMessage());

      } catch (ValidationExceptionInvalidFile e) {

         Assert
               .fail("L'exception attendue était une UnknownFormatException alors qu'on a obtenu : "
                     + e);

      }

   }

}
