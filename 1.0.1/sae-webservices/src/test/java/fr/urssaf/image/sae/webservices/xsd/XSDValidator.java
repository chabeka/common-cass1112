package fr.urssaf.image.sae.webservices.xsd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Classe de validation d'un document XML par un schéma XSD
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class XSDValidator {

   private XSDValidator() {

   }

   private static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

   private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

   private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

   private static final Logger LOGGER = Logger.getLogger(XSDValidator.class);

   /**
    * Type d'erreur dans une SAXParseExceptionType
    */
   public enum ExceptionType {

      fatalError("FATAL ERROR"),

      error("ERROR"),

      warning("WARNING");

      private String label;

      /** Constructeur */
      ExceptionType(String label) {
         this.label = label;
      }

      /**
       * Renvoie un texte représentant le type d'exception
       * 
       * @return un texte représentant le type d'exception
       */
      public String getLabel() {
         return this.label;
      }
   }

   /**
    * Représente une erreur de validation du XML avec un schéma XSD.
    */
   public static final class SAXParseExceptionType {

      private final SAXParseException exception;

      private final ExceptionType type;

      /**
       * Constructeur
       * 
       * @param exception
       *           l'exception SAX
       * @param type
       *           le type d'exception
       */
      public SAXParseExceptionType(SAXParseException exception,
            ExceptionType type) {

         this.exception = exception;
         this.type = type;
         // LOGGER.debug(exception.getMessage(), exception);

      }

      /**
       * Renvoie l'exception SAX
       * 
       * @return l'exception SAX
       */
      public SAXParseException getException() {
         return this.exception;
      }

      /**
       * Renvoie le type de l'exception
       * 
       * @return le type de l'exception
       */
      public ExceptionType getType() {
         return this.type;
      }

   }

   private static class DomErrorHandler implements ErrorHandler {

      private final List<SAXParseExceptionType> errors;

      DomErrorHandler(List<SAXParseExceptionType> errors) {
         this.errors = errors;
      }

      @Override
      public void warning(SAXParseException exception) {
         errors
               .add(new SAXParseExceptionType(exception, ExceptionType.warning));
      }

      @Override
      public void error(SAXParseException exception) {
         errors.add(new SAXParseExceptionType(exception, ExceptionType.error));
      }

      @Override
      public void fatalError(SAXParseException exception) {
         errors.add(new SAXParseExceptionType(exception,
               ExceptionType.fatalError));
      }

   }

   private static class SaxErrorHandler extends DefaultHandler {

      private final List<SAXParseExceptionType> errors;

      SaxErrorHandler(List<SAXParseExceptionType> errors) {
         super();
         this.errors = errors;
      }

      @Override
      public void warning(SAXParseException exception) {
         errors
               .add(new SAXParseExceptionType(exception, ExceptionType.warning));
      }

      @Override
      public void error(SAXParseException exception) {
         errors.add(new SAXParseExceptionType(exception, ExceptionType.error));
      }

      @Override
      public void fatalError(SAXParseException exception) {
         errors.add(new SAXParseExceptionType(exception,
               ExceptionType.fatalError));
      }

   }

   /**
    * Valide un fichier XML à l'aide d'un fichier XSD.
    * 
    * @param xmlFile
    *           nom et emplacement du fichier XML à tester
    * @param xsdFile
    *           chemin complet du fichier XSD
    * 
    * @return List<SAXParseExceptionType> liste des erreurs trouvées dans le
    *         document, le cas échéant
    * 
    * @throws ParserConfigurationException
    *            problème de parser
    * @throws IOException
    *            problème d'E/S
    * @throws SAXException
    *            problème SAX
    */
   public static List<SAXParseExceptionType> validXMLWithDOM(String xmlFile,
         String xsdFile) throws ParserConfigurationException, IOException,
         SAXException {

      final ArrayList<SAXParseExceptionType> errors = new ArrayList<SAXParseExceptionType>();

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      // dbf.setCoalescing(true);
      // dbf.setExpandEntityReferences(true);
      dbf.setIgnoringComments(true);
      dbf.setNamespaceAware(true);
      dbf.setValidating(true);

      dbf.setIgnoringElementContentWhitespace(true);
      dbf.setAttribute(SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
      dbf.setAttribute(SCHEMA_SOURCE, xsdFile);

      // Parsage du fichier XML avec DOM
      DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
      documentBuilder.setErrorHandler(new DomErrorHandler(errors));
      documentBuilder.parse(xmlFile);

      // Renvoie des erreurs trouvées dans le XML (le cas échéant)
      return errors;

   }

   /**
    * Valide un fichier XML à l'aide d'un fichier XSD.
    * 
    * @param xmlFile
    *           nom et emplacement du fichier XML à tester
    * @param xsdFile
    *           chemin complet du fichier XSD
    * 
    * @return liste des erreurs trouvées dans le document, le cas échéant
    * 
    * @throws SAXException
    *            problème SAX
    * @throws IOException
    *            problème d'E/S
    * @throws ParserConfigurationException
    *            problème de parser
    */
   public static List<SAXParseExceptionType> validXMLFileWithSAX(
         String xmlFile, String xsdFile) throws SAXException, IOException,
         ParserConfigurationException {

      final ArrayList<SAXParseExceptionType> errors = new ArrayList<SAXParseExceptionType>();

      SAXParser saxParser = factorySAXParser(xsdFile);

      saxParser.parse(xmlFile, new SaxErrorHandler(errors));
      return errors;

   }

   /**
    * Valide une chaine de caractère au format XML à l'aide d'un fichier XSD.
    * 
    * @param xmlString
    *           chaine de caractère au format xml
    * @param xsdFile
    *           chemin complet du fichier XSD
    * 
    * @return liste des erreurs trouvées dans le document, le cas échéant
    * 
    * @throws SAXException
    *            problème SAX
    * @throws IOException
    *            problème d'E/S
    * @throws ParserConfigurationException
    *            problème de parser
    */
   public static List<SAXParseExceptionType> validXMLStringWithSAX(
         String xmlString, String xsdFile) throws SAXException, IOException,
         ParserConfigurationException {

      final ArrayList<SAXParseExceptionType> errors = new ArrayList<SAXParseExceptionType>();

      SAXParser saxParser = factorySAXParser(xsdFile);

      InputStream input = new ByteArrayInputStream(xmlString.getBytes());

      try {
         saxParser.parse(input, new SaxErrorHandler(errors));

         return errors;

      } finally {
         input.close();
      }

   }

   private static SAXParser factorySAXParser(String xsdFile)
         throws ParserConfigurationException, SAXException {

      SAXParserFactory spf = SAXParserFactory.newInstance();

      spf.setNamespaceAware(true);
      spf.setValidating(true);

      SAXParser saxParser = spf.newSAXParser();
      saxParser.setProperty(SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
      saxParser.setProperty(SCHEMA_SOURCE, xsdFile);

      return saxParser;
   }

   /**
    * Affiche dans le Logger au niveau debug la liste d'erreurs passé en
    * paramètre<br>
    * <br>
    * Utile pour le "déboguage"
    * 
    * @param liste
    *           la liste des erreurs
    */
   public static void afficher(List<SAXParseExceptionType> liste) {
      Iterator<SAXParseExceptionType> iter = liste.iterator();
      while (iter.hasNext()) {
         SAXParseExceptionType type = iter.next();
         String log = type.type.getLabel() + " ligne:"
               + type.exception.getLineNumber() + " "
               + type.exception.getMessage();
         if (iter.hasNext()) {
            LOGGER.debug(log);
         } else {
            LOGGER.debug(log + "\n");
         }
      }

   }

}
