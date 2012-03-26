/**
 * 
 */
package fr.urssaf.image.sae.services.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;

/**
 * Classe utilitaire de validation des fichiers XML
 * 
 */
public class XmlValidationUtils {

   private static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

   private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

   private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(XmlValidationUtils.class);

   /**
    * Validation de fichier XML par rapport au fichier XSD fourni
    * 
    * @param xmlFile
    *           fichier XML à valider
    * @param xsdFile
    *           chemin relatif du fichier XSD
    * @throws ParserConfigurationException
    *            Erreur levée pour la configuration du parser
    * @throws SAXException
    *            erreur de parsing du fichier XML
    * @throws IOException
    *            erreur de récupération du fichier XML
    */
   public static void parse(File xmlFile, String xsdFile)
         throws ParserConfigurationException, SAXException, IOException {

      FileInputStream stream = null;

      try {

         final SAXParser saxParser = factorySAXParser(xsdFile);
         stream = new FileInputStream(xmlFile);
         saxParser.parse(stream, new SaxErrorHandler());

      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (Exception e) {
               LOGGER.info("Erreur de fermeture de flux", e);
            }
         }
      }
   }

   private static SAXParser factorySAXParser(final String xsdFile)
         throws ParserConfigurationException, SAXException {

      final SAXParserFactory spf = SAXParserFactory.newInstance();

      spf.setNamespaceAware(true);
      spf.setValidating(true);

      ClassPathResource resource = new ClassPathResource(xsdFile);
      final SAXParser saxParser = spf.newSAXParser();
      try {
         saxParser.setProperty(SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
         saxParser.setProperty(SCHEMA_SOURCE, resource.getFile()
               .getAbsolutePath());
      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);
      }

      return saxParser;
   }

   private static class SaxErrorHandler extends DefaultHandler {

      @Override
      public void warning(final SAXParseException exception)
            throws SAXParseException {
         throw exception;
      }

      @Override
      public void error(final SAXParseException exception)
            throws SAXParseException {
         throw exception;
      }

      @Override
      public void fatalError(final SAXParseException exception)
            throws SAXParseException {

         throw exception;

      }

   }
}
