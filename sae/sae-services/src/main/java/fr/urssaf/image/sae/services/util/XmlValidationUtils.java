/**
 * 
 */
package fr.urssaf.image.sae.services.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Classe utilitaire de validation des fichiers XML
 * 
 */
public final class XmlValidationUtils {

   // private static final String SCHEMA_LANGUAGE =
   // "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
   //
   // private static final String W3C_XML_SCHEMA =
   // "http://www.w3.org/2001/XMLSchema";
   //
   // private static final String SCHEMA_SOURCE =
   // "http://java.sun.com/xml/jaxp/properties/schemaSource";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(XmlValidationUtils.class);

   /**
    * Constructeur
    */
   private XmlValidationUtils() {
   }

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
         final Schema schema = factorySAXParser(xsdFile);

         stream = FileUtils.openInputStream(xmlFile);
         InputSource inputSource = new InputSource(stream);

         Validator validator = schema.newValidator();
         validator.validate(new SAXSource(inputSource));

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

   private static Schema factorySAXParser(final String xsdFile)
         throws ParserConfigurationException, SAXException {

      final SAXParserFactory spf = SAXParserFactory.newInstance();

      spf.setNamespaceAware(true);
      spf.setValidating(true);

      SchemaFactory factory = SchemaFactory
            .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      // factory.setResourceResolver(new XsdResourceresolver());
      Source schemaFile = new StreamSource(XmlValidationUtils.class
            .getClassLoader().getResourceAsStream(xsdFile));
      Schema schema = factory.newSchema(schemaFile);

      return schema;
   }

}
