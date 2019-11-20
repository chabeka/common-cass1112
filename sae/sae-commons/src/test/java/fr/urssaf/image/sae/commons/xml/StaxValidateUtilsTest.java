package fr.urssaf.image.sae.commons.xml;

import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXParseException;

public class StaxValidateUtilsTest {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(StaxValidateUtilsTest.class);
  @Test
  public void testSucces() {

    boolean succes = false;
    try {
      final ClassPathResource xmlResource = new ClassPathResource(
                                                                  "xml/fichierLecture.xml");
      final File xmlFile = xmlResource.getFile();

      final ClassPathResource xsdResource = new ClassPathResource("xml/schema.xsd");
      final URL xsdURL = xsdResource.getURL();

      StaxValidateUtils.parse(xmlFile, xsdURL);
      succes = true;

    } catch (final Exception exception) {
      LOGGER.error(exception.getMessage());

    }

    Assert.assertTrue("le traitement doit etre un succes", succes);

  }

  @Test
  public void testErreur() {

    boolean traitementOk = false;
    try {
      final ClassPathResource xmlResource = new ClassPathResource(
          "xml/fichierErreur.xml");
      final File xmlFile = xmlResource.getFile();

      final ClassPathResource xsdResource = new ClassPathResource("xml/schema.xsd");
      final URL xsdURL = xsdResource.getURL();

      StaxValidateUtils.parse(xmlFile, xsdURL);
      Assert.fail("une SaxParseException est attendue");

    } catch (final SAXParseException exception) {
      traitementOk = true;

    } catch (final Exception exception) {
      Assert.fail("une SAXParseException est attendue");
    }

    Assert.assertTrue("le traitement doit avoir echoué en SaxParseException",
                      traitementOk);

  }

}
