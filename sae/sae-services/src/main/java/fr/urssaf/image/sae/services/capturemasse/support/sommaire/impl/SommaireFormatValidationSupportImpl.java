/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport;

/**
 * Implémentation du support {@link SommaireFormatValidationSupport}
 * 
 */
@Component
public class SommaireFormatValidationSupportImpl implements
      SommaireFormatValidationSupport {

   private static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

   private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

   private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

   private static final Logger LOGGER = Logger
         .getLogger(SommaireFormatValidationSupportImpl.class);

   private static final String XSD_FILE = "src/main/resources/xsd_som_res/sommaire.xsd";
   /**
    * {@inheritDoc}
    */
   @Override
   public final void validationSommaire(final File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException {

      

      FileInputStream xmlFile = null;

      try {
         final SAXParser saxParser = factorySAXParser(XSD_FILE);
         xmlFile = new FileInputStream(sommaireFile);

         saxParser.parse(xmlFile, new SaxErrorHandler());

      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (ParserConfigurationException e) {
         throw new CaptureMasseSommaireFormatValidationException(sommaireFile
               .getAbsolutePath(), e);

      } catch (SAXException e) {
         throw new CaptureMasseSommaireFormatValidationException(sommaireFile
               .getAbsolutePath(), e);
      } finally {
         if (xmlFile != null) {
            try {
               xmlFile.close();
            } catch (IOException e) {
               LOGGER.debug("{} - Erreur lors de la fermeture du flux "
                     + sommaireFile.getAbsolutePath());
            }
         }
      }

   }

   private SAXParser factorySAXParser(final String xsdFile)
         throws ParserConfigurationException, SAXException {

      final SAXParserFactory spf = SAXParserFactory.newInstance();

      spf.setNamespaceAware(true);
      spf.setValidating(true);

      final SAXParser saxParser = spf.newSAXParser();
      saxParser.setProperty(SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
      saxParser.setProperty(SCHEMA_SOURCE, xsdFile);

      return saxParser;
   }

   private static class SaxErrorHandler extends DefaultHandler {

      @Override
      public void warning(final SAXParseException exception) throws SAXParseException {
         throw exception;
      }

      @Override
      public void error(final SAXParseException exception) throws SAXParseException {
         throw exception;
      }

      @Override
      public void fatalError(final SAXParseException exception)
            throws SAXParseException {

         throw exception;

      }

   }

}
