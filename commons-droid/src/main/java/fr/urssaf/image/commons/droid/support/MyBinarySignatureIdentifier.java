package fr.urssaf.image.commons.droid.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import uk.gov.nationalarchives.droid.core.BinarySignatureIdentifier;
import uk.gov.nationalarchives.droid.core.SignatureFileParser;
import uk.gov.nationalarchives.droid.core.signature.droid6.FFSignatureFile;
import uk.gov.nationalarchives.droid.core.signature.xml.SAXModelBuilder;
import fr.urssaf.image.commons.droid.exception.FormatIdentificationRuntimeException;

/**
 * Classe dérivée de la classe DROID BinarySignatureIdentifier, permettant le
 * chargement du fichier des signatures non plus obligatoirement à partir d'un
 * fichier physique et de son chemin complet, mais à partir d'un objet Resource
 * Spring.<br>
 * <br>
 * Cela permettant par exemple de charger un fichier de signatures depuis les
 * ressources du JAR.
 */
public class MyBinarySignatureIdentifier extends BinarySignatureIdentifier {

   @SuppressWarnings("unchecked")
   private static class AccessVarPrivee implements PrivilegedAction {

      private final Field privateField;

      public AccessVarPrivee(Field privateField) {
         this.privateField = privateField;
      }

      @Override
      public Object run() {
         privateField.setAccessible(true);
         return null;
      }

   }

   private final Resource signaturesResource;

   /**
    * Constructeur
    * 
    * @param signatures
    *           l'objet Resource pointant sur les signatures binaires DROID
    */
   public MyBinarySignatureIdentifier(Resource signatures) {
      super();
      this.signaturesResource = signatures;
   }

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   @Override
   public final void init() {

      // Chargement du fichier des signatures
      FFSignatureFile sigFile = loadSignatures();
      sigFile.prepareForUse();

      // Ecrase par réflexion le champ privé "sigFile" de la classe que l'on
      // dérive
      // TODO : Trouver autre chose que la réflexion
      try {

         final Field privateField = BinarySignatureIdentifier.class
               .getDeclaredField("sigFile");

         // privateField.setAccessible(true);
         AccessController.doPrivileged(new AccessVarPrivee(privateField));

         privateField.set(this, sigFile);

      } catch (SecurityException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      } catch (NoSuchFieldException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      } catch (IllegalArgumentException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      } catch (IllegalAccessException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      }

   }

   private FFSignatureFile loadSignatures() {

      // Code récupéré de la classe :
      // uk.gov.nationalarchives.droid.core.SignatureFileParser
      // et adapté pour le chargement non avec un FileInputStream,
      // mais avec un InputStream récupéré de l'objet Resource

      SAXModelBuilder modelBuilder = new SAXModelBuilder();
      XMLReader parser = getXMLReader(modelBuilder);

      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(
               signaturesResource.getInputStream(), "UTF-8"));
         parser.parse(new InputSource(reader));
      } catch (IOException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      } catch (SAXException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      }

      return (FFSignatureFile) modelBuilder.getModel();

   }

   private XMLReader getXMLReader(SAXModelBuilder modelBuilder) {

      // Code récupéré de la classe :
      // uk.gov.nationalarchives.droid.core.SignatureFileParser

      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setNamespaceAware(true);
      SAXParser saxParser;
      try {
         saxParser = factory.newSAXParser();
         XMLReader parser = saxParser.getXMLReader();
         modelBuilder.setupNamespace(SignatureFileParser.SIGNATURE_FILE_NS,
               true);
         parser.setContentHandler(modelBuilder);
         return parser;
      } catch (ParserConfigurationException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      } catch (SAXException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      }
   }

}
