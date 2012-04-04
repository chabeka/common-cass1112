package fr.urssaf.image.commons.commons.pronom.exemple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import fr.urssaf.image.commons.commons.pronom.exemple.exception.SignatureFileException;
import fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml.Format;
import fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml.FormatCallback;
import fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml.SaxSignatureFileParser;

public class SignatureFileParserTest {
   
   
   @Test
   public void testParseAllFileFormatsGivesCollectionOfAllFileFormats()
       throws SignatureFileException {
       
      URI uri = new File("src/test/resources/test_sig_files/DROID_SignatureFile_V59.xml").toURI();
       
      SaxSignatureFileParser parser = new SaxSignatureFileParser(uri);

      FormatCallback callback = mock(FormatCallback.class);

      ArgumentCaptor<Format> formatCaptor = ArgumentCaptor.forClass(Format.class);
      
      parser.formats(callback);

      verify(callback, times(864)).onFormat(formatCaptor.capture());

      List<Format> formats = formatCaptor.getAllValues();
      boolean foundPDF = false;
      for (Format format : formats) {
          if ("fmt/354".equals(format.getPuid())) {
               assertEquals(
                       "Acrobat PDF/A - Portable Document Format",
                       format.getName());
               assertEquals("application/pdf", format.getMimeType());
               foundPDF = true;
          }
       }

       assertTrue(foundPDF);
   }

}
