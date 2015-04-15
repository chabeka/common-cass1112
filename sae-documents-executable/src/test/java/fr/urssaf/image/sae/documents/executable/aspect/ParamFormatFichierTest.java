package fr.urssaf.image.sae.documents.executable.aspect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.documents.executable.exception.ParametreRuntimeException;
import fr.urssaf.image.sae.documents.executable.service.FormatFichierService;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorUnhandledException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class ParamFormatFichierTest {

   private final File file = new File(
         "src/test/resources/identification/PdfaValide.pdf");

   @Autowired
   private FormatFichierService formatFichierService;

   @Test
   public void testValidIdentifierFichierIdFormatNullStreamNullDocumentNullMetadonneesNull() {
      try {
         formatFichierService.identifierFichier(null, null, null, null);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, file, document, metadonnees].",
                     ex.getMessage());
      }
   }

   @Test
   public void testValidIdentifierFichierIdFormatNullStreamNullDocumentNull() {
      try {
         formatFichierService.identifierFichier(null, null, null,
               new ArrayList<String>());
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, file, document].",
                     ex.getMessage());
      }
   }

   @Test
   public void testValidIdentifierFichierIdFormatNullStreamNull() {
      try {
         formatFichierService.identifierFichier(null, null, new DocumentImpl(),
               new ArrayList<String>());
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, file].",
                     ex.getMessage());
      }
   }

   @Test
   public void testValidIdentifierFichierIdFormatNull()
         throws FileNotFoundException {
      try {
         formatFichierService.identifierFichier(null, file, new DocumentImpl(),
               new ArrayList<String>());
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat].",
                     ex.getMessage());
      }
   }

   @Test
   public void testValidIdentifierFichierMetadonneesNonAutorisees()
         throws FileNotFoundException {
      List<String> metadonnees = Arrays.asList(new String[] {
            "SM_DIGEST_ALGORITHM", "SM_VERSION", "SM_MODIFICATION_DATE" });

      try {
         formatFichierService.identifierFichier("fmt/354", file,
               new DocumentImpl(), metadonnees);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La liste des métadonnées ne doit pas contenir une ou plusieurs métadonnées non autorisées : [SM_DIGEST_ALGORITHM, SM_VERSION, SM_MODIFICATION_DATE].",
                     ex.getMessage());
      }
   }

   @Test
   public void testValidValiderFichierIdFormatNullStreamNull()
         throws UnknownFormatException, ValidatorInitialisationException,
         IOException, ValidatorUnhandledException {
      try {
         formatFichierService.validerFichier(null, null);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, file].",
                     ex.getMessage());
      }
   }

   @Test
   public void testValidValiderFichierIdFormatNull()
         throws UnknownFormatException, ValidatorInitialisationException,
         IOException, ValidatorUnhandledException {
      try {
         formatFichierService.validerFichier(null, file);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat].",
                     ex.getMessage());
      }
   }
}
