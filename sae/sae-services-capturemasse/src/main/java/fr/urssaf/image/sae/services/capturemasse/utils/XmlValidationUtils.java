package fr.urssaf.image.sae.services.capturemasse.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Classe utilitaire de validation des fichiers XML
 * 
 */
@Deprecated
public final class XmlValidationUtils {

   private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

   private XmlValidationUtils() {

   }

   /**
    * Validation de fichier XML par rapport au fichier XSD fourni
    * 
    * @param xmlFile
    *           fichier XML à valider
    * @param xsdURL
    *           URL du fichier XSD
    * @throws ParserConfigurationException
    *            Erreur levée pour la configuration du parser
    * @throws SAXException
    *            erreur de parsing du fichier XML
    * @throws IOException
    *            erreur de récupération du fichier XML
    */
   public static void parse(File xmlFile, URL xsdURL)
         throws ParserConfigurationException, SAXException, IOException {

      SAXParser saxParser = factorySAXParser(xsdURL);

      saxParser.parse(xmlFile, new SaxErrorHandler());
   }

   private static SAXParser factorySAXParser(URL xsdURL)
         throws ParserConfigurationException, SAXException {

      SAXParserFactory spf = SAXParserFactory.newInstance();

      spf.setNamespaceAware(true);
      spf.setValidating(false);

      SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA);
      Schema schema = factory.newSchema(xsdURL);
      spf.setSchema(schema);

      SAXParser saxParser = spf.newSAXParser();

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
