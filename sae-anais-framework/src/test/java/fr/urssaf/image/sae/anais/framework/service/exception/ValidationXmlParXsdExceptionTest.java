package fr.urssaf.image.sae.anais.framework.service.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

import fr.urssaf.image.sae.vi.exception.VIException;
import fr.urssaf.image.sae.vi.modele.DroitApplicatif;
import fr.urssaf.image.sae.vi.service.VIService;

public class ValidationXmlParXsdExceptionTest {

   private static final String FAIL_MESSAGE = "le test ne doit pas passer";

   @SuppressWarnings("unchecked")
   @Test
   public void validationXmlParXsdException() throws VIException,
         AucunDroitException, IOException {

      VIService service = new VIService();

      try {
         service.createVI(null, null, new ArrayList<DroitApplicatif>());
         fail(FAIL_MESSAGE);
      } catch (VIException e) {
         ValidationXmlParXsdException exception = new ValidationXmlParXsdException(
               e);

         assertEquals("sae-anais.xsd", exception.getNomDuSchemaXsd());
         assertEquals("VECTEUR IDENTIFICATION", exception.getNomDuDocumentXml());
         assertEquals(2, exception.getErreurs().length);

         List<String> lines = FileUtils.readLines(new File(
               "src/test/resources/ValidationXmlParXsdException.txt"), "UTF-8");

         String[] str = StringUtils.split(exception.getMessage(),
               SystemUtils.LINE_SEPARATOR);

         int index = 0;
         for (String line : lines) {

            assertTrue("le message doit commencer par " + line
                  + " à la ligne " + index, str[index].startsWith(line));
            index++;
         }

         assertEquals(lines.size(), str.length);

      }

   }
}
