package fr.urssaf.image.commons.spring.batch.support;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import fr.urssaf.image.commons.spring.batch.support.stax.XSDValidator;

@SuppressWarnings("PMD.MethodNamingConventions")
public class XSDValidatorTest {

   private static final Logger LOGGER = Logger
         .getLogger(XSDValidatorTest.class);

   @Test
   public void validate_success() throws SAXException, IOException,
         ParserConfigurationException {

      File xmlPath = new File("src/test/resources/data/bibliotheque.xml");

      File xsdPath = new File("src/main/resources/schemas/bibliotheque.xsd");

      XSDValidator.validXMLFileWithSAX(xmlPath, xsdPath);

   }

   @Test
   public void validate_failure() throws SAXException, IOException,
         ParserConfigurationException {

      File xmlPath = new File(
            "src/test/resources/data/bibliotheque_failure.xml");
      File xsdPath = new File("src/main/resources/schemas/bibliotheque.xsd");

      try {
         XSDValidator.validXMLFileWithSAX(xmlPath, xsdPath);

         Assert.fail("une SAXParseException doit être levée");

      } catch (SAXParseException e) {

         LOGGER.debug("colonne:" + e.getColumnNumber() + " ligne:"
               + e.getLineNumber() + " " + e.getMessage());
      }

   }

}
