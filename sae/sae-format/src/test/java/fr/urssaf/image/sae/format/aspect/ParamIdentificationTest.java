package fr.urssaf.image.sae.format.aspect;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentificationRuntimeException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentifierInitialisationException;
import fr.urssaf.image.sae.format.identification.identifiers.pdfa.PdfaIdentifierImpl;
import fr.urssaf.image.sae.format.identification.service.impl.IdentificationServiceImpl;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import org.junit.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class ParamIdentificationTest {

   private static final String MESSAGE_REF_RUNTIME = "Une exception ReferentielRuntimeException aurait dû être levée";
   private static final String MESSAGE_EXCEPT_INCORRECT = "Le message de l'exception est incorrect";
   private static final String TEST = "test";
   private static final String FICHIER_OBLIG_NUL = "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [fichier].";
   private static final String NOM_FICHIER_OBLIG_NUL = "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [nomFichier].";

   @Autowired
   private PdfaIdentifierImpl identifier;

   @Autowired
   private IdentificationServiceImpl identifierService;

   @Test
   public void validIdentifyFileIdFormatNullFileNotExist()
         throws IdentificationRuntimeException, IOException {
      try {
         identifier.identifyFile(null, new File(TEST));
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, fichier].",
                     ex.getMessage());
      }
   }

   @Test
   public void validIdentifyFileFileNotExist()
         throws IdentificationRuntimeException, IOException {
      try {
         identifier.identifyFile("1", new File(TEST));
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, FICHIER_OBLIG_NUL, ex
               .getMessage());
      }
   }

   @Test
   public void validIdentifyFileFileNull()
         throws IdentificationRuntimeException, IOException {
      try {
         identifier.identifyFile("1", null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, FICHIER_OBLIG_NUL, ex
               .getMessage());
      }
   }

   @Test
   public void validIdentifyStreamIdFormatNullStreamNull()
         throws IdentificationRuntimeException {
      try {
         identifier.identifyStream(null, null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, stream, nomFichier].",
                     ex.getMessage());
      }
   }

   @Test
   public void validIdentifyStreamStreamNull()
         throws IdentificationRuntimeException {
      try {
         identifier.identifyStream("1", null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [stream, nomFichier].",
                     ex.getMessage());
      }
   }

   @Test
   public void validIdentifyStreamNomFichierNull()
         throws IdentificationRuntimeException {
      try {

         byte[] buffer = new byte[1];
         ByteArrayInputStream stream = new ByteArrayInputStream(buffer);

         identifier.identifyStream("fmt/354", stream, null);

         Assert.fail(MESSAGE_REF_RUNTIME);

      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, NOM_FICHIER_OBLIG_NUL,
               ex.getMessage());
      }
   }

   @Test
   public void identifierServiceIdentifyFileIdFormatNullFileNotExist()
         throws IdentificationRuntimeException, UnknownFormatException,
         IdentifierInitialisationException, IOException {
      try {
         identifierService.identifyFile(null, new File(TEST));
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, fichier].",
                     ex.getMessage());
      }
   }

   @Test
   public void identifierServiceIdentifyFileFileNotExist()
         throws IdentificationRuntimeException, UnknownFormatException,
         IdentifierInitialisationException, IOException {
      try {
         identifierService.identifyFile("fmt/354", new File(TEST));
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, FICHIER_OBLIG_NUL, ex
               .getMessage());
      }
   }

   @Test
   public void identifierServiceIdentifyFileFileNull()
         throws IdentificationRuntimeException, UnknownFormatException,
         IdentifierInitialisationException, IOException {

      try {
         identifierService.identifyFile("fmt/354", null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, FICHIER_OBLIG_NUL, ex
               .getMessage());
      }
   }

   @Test
   public void identifierServiceIdentifyStreamIdFormatNullStreamNull()
         throws IdentificationRuntimeException, UnknownFormatException,
         IdentifierInitialisationException {
      try {
         identifierService.identifyStream(null, null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, stream, nomFichier].",
                     ex.getMessage());
      }
   }

   @Test
   public void identifierServiceIdentifyStreamStreamNull()
         throws IdentificationRuntimeException, UnknownFormatException,
         IdentifierInitialisationException {
      try {
         identifierService.identifyStream("fmt/354", null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [stream, nomFichier].",
                     ex.getMessage());
      }
   }

   @Test
   public void identifierServiceIdentifyStreamNomFichierNull()
         throws IdentificationRuntimeException, UnknownFormatException,
         IdentifierInitialisationException {
      try {

         byte[] buffer = new byte[1];
         ByteArrayInputStream stream = new ByteArrayInputStream(buffer);

         identifierService.identifyStream("fmt/354", stream, null);

         Assert.fail(MESSAGE_REF_RUNTIME);

      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, NOM_FICHIER_OBLIG_NUL,
               ex.getMessage());
      }
   }

}
