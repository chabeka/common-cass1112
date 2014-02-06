package fr.urssaf.image.sae.documents.executable.multithreading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.documents.executable.exception.FormatValidationRuntimeException;
import fr.urssaf.image.sae.documents.executable.service.FormatFichierService;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class FormatRunnableTest {

   private final File file = new File(
      "src/test/resources/identification/PdfaValide.pdf");

   @Autowired
   private FormatFichierService formatFichierService;
   
   private Document createDocument(String idFormat) {
      DocumentImpl document = new DocumentImpl();
      document.setUuid(UUID.fromString("00000000-0000-0000-0000-000000000000"));
      document.setArchivageDate(new Date());
      document.setType("2.3.1.1.12");
      document.addCriterion("cse", "CS1");
      document.addCriterion("apr", "GED");
      document.addCriterion("atr", "GED");
      document.addCriterion("ffi", idFormat);
      return document;
   }
   
   @Test
   public void run() throws FileNotFoundException, IOException {
      try {
         FormatRunnable runnable = new FormatRunnable(createDocument("fmt/354"), file, formatFichierService);
         runnable.run();
         
         Assert.assertNotNull("L'objet ValidationResult n'aurait pas du être null", runnable.getResultat());
         Assert.assertTrue("Le document aurait du être valide", runnable.getResultat().isValid());
      } catch (FormatValidationRuntimeException ex) {
         Assert.fail("L'exception FormatValidationRuntimeException n'aurait pas dû être levée");
      }
   }
   
   @Test
   public void runWithUnknownFormatException() throws FileNotFoundException, IOException {
      try {
         FormatRunnable runnable = new FormatRunnable(createDocument("idFormat"), file, formatFichierService);
         runnable.run();
         
         Assert.fail("L'exception FormatValidationRuntimeException aurait dû être levée");
      } catch (FormatValidationRuntimeException ex) {
         Assert.assertEquals("Une UnknownFormatException aurait du être levée", "fr.urssaf.image.sae.format.exception.UnknownFormatException: Aucun format n'a été trouvé avec l'identifiant : idFormat.", ex.getMessage());
      }
   }
   
   @Test
   public void runWithValidatorInitialisationException() throws FileNotFoundException, IOException {
      try {
         FormatRunnable runnable = new FormatRunnable(createDocument("format1"), file, formatFichierService);
         runnable.run();
         
         Assert.fail("L'exception FormatValidationRuntimeException aurait dû être levée");
      } catch (FormatValidationRuntimeException ex) {
         Assert.assertEquals("Une ValidatorInitialisationException aurait du être levée", "fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException: Il n'est pas possible de récupérer une instance du validateur.", ex.getMessage());
      }
   }
   
   @Test
   public void runWithIOException() throws FileNotFoundException, UnknownFormatException, ValidatorInitialisationException, IOException {
      String idFormat = "fmt/354";
      
      FormatFichierService mockService = EasyMock.createNiceMock(FormatFichierService.class);
      EasyMock.expect(mockService.validerFichier(idFormat, file)).andThrow(new IOException("Ceci est un test"));
      EasyMock.replay(mockService);
      
      try {
         FormatRunnable runnable = new FormatRunnable(createDocument(idFormat), file, mockService);
         runnable.run();
         
         Assert.fail("L'exception FormatValidationRuntimeException aurait dû être levée");
      } catch (FormatValidationRuntimeException ex) {
         Assert.assertEquals("Une IOException aurait du être levée", "java.io.IOException: Ceci est un test", ex.getMessage());
      }
   }
}
