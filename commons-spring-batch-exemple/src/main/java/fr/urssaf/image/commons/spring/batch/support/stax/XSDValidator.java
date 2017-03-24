package fr.urssaf.image.commons.spring.batch.support.stax;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public final class XSDValidator {

   private XSDValidator() {

   }

   private static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

   private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

   private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

   private static class SaxErrorHandler extends DefaultHandler {

      @Override
      public void warning(SAXParseException exception) throws SAXParseException {
         throw exception;
      }

      @Override
      public void error(SAXParseException exception) throws SAXParseException {
         throw exception;
      }

      @Override
      public void fatalError(SAXParseException exception)
            throws SAXParseException {

         throw exception;

      }

   }

   public static void validXMLFileWithSAX(File xmlFile, File xsdFile)
         throws ParserConfigurationException, SAXException, IOException {

      SAXParser saxParser = factorySAXParser(xsdFile);
      saxParser.parse(xmlFile, new SaxErrorHandler());

   }

   private static SAXParser factorySAXParser(File xsdFile)
         throws ParserConfigurationException, SAXException {

      SAXParserFactory spf = SAXParserFactory.newInstance();

      spf.setNamespaceAware(true);
      spf.setValidating(true);

      SAXParser saxParser = spf.newSAXParser();
      saxParser.setProperty(SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
      saxParser.setProperty(SCHEMA_SOURCE, xsdFile.getAbsolutePath());

      return saxParser;
   }

}
