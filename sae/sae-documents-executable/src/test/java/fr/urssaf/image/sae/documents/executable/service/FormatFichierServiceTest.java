package fr.urssaf.image.sae.documents.executable.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.documents.executable.utils.Constantes;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class FormatFichierServiceTest {

   @Autowired
   private FormatFichierService formatFichierService;
   
   private final File file = new File(
         "src/test/resources/identification/PdfaValide.pdf");

   private final File doc = new File(
         "src/test/resources/identification/word.doc");
   
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
   
   private List<String> createMetadonnees() {
      return Arrays.asList(Constantes.METADONNEES_DEFAULT);
   }

   @Test
   public void identifierFichierWithUnknownFormatException() throws FileNotFoundException, IOException {
      FileInputStream streamPdf = new FileInputStream(file);
      
      Assert.assertFalse("Le format 'idFormat' ne devrait pas être connu dans le référentiel des formats", formatFichierService.identifierFichier("idFormat", streamPdf, createDocument("idFormat"), createMetadonnees()));
      
      streamPdf.close();
   }
   
   @Test
   public void identifierFichierWithIdentifierInitialisationException() throws FileNotFoundException, IOException {
      FileInputStream streamPdf = new FileInputStream(file);
      
      Assert.assertFalse("L'identificateur du format 'format1' n'aurait pas du être instancié", formatFichierService.identifierFichier("format1", streamPdf, createDocument("format1"), createMetadonnees()));
      
      streamPdf.close();
   }
   
   
   @Test
   public void identifierFichierFormatNonValid() throws FileNotFoundException, IOException {
      FileInputStream streamDoc = new FileInputStream(doc);
      
      Assert.assertFalse("L'identification du document n'aurait pas du réussir car ce n'est pas un pdf/a", formatFichierService.identifierFichier("fmt/354", streamDoc, createDocument("fmt/354"), createMetadonnees()));
      
      streamDoc.close();
   }
   
   @Test
   public void identifierFichierFormatValid() throws FileNotFoundException, IOException {
      FileInputStream streamPdf = new FileInputStream(file);
      
      Assert.assertTrue("L'identification du document aurait du réussir", formatFichierService.identifierFichier("fmt/354", streamPdf, createDocument("fmt/354"), createMetadonnees()));
      
      streamPdf.close();
   }

   @Test
   public void validerFichier() throws UnknownFormatException, ValidatorInitialisationException, IOException {
      FileInputStream streamPdf = new FileInputStream(file);
      
      ValidationResult validationResult = formatFichierService.validerFichier("fmt/354", streamPdf);
      Assert.assertNotNull("Le résultat de la validation ne devrait pas être null", validationResult);
      Assert.assertTrue("Le fichier aurait du être valide", validationResult.isValid());
      
      streamPdf.close();
   }
}
