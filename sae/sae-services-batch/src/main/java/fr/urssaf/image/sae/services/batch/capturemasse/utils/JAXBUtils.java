package fr.urssaf.image.sae.services.batch.capturemasse.utils;

import java.io.OutputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import fr.urssaf.image.sae.services.batch.capturemasse.modele.resultats.ObjectFactory;

/**
 * Cette classe permet de pratiquer le marshalling et le unmarshalling <br>
 * à savoir JAVA->XML et XML->JAVA. Les methodes respectent egalement le format
 * des schemas XSD.
 * 
 * Utilitaire principalement utilisé pour les services SommaireXMLService et
 * ResultatsXMLService.
 */
public final class JAXBUtils {

   private JAXBUtils() {
   }

   /**
    * Méthode générique de marshalling avec JAXB
    * 
    * @param <T>
    *           le type de l'élément racine
    * @param rootElement
    *           l'élément racine
    * @param output
    *           le flux dans lequel écrire le résultat du marshalling
    * @param xsdSchema
    *           le schéma XSD. Peut être null
    * @throws JAXBException
    *            en cas d'exception levée par JAXB
    * @throws SAXException
    *            en cas d'exception levée par le parser SAX
    */
   public static <T> void marshal(JAXBElement<T> rootElement,
         OutputStream output, URL xsdSchema) throws JAXBException, SAXException {

      // Création des objets nécessaires
      JAXBContext context = JAXBContext
            .newInstance(new Class[] { ObjectFactory.class });
      Marshaller marshaller = context.createMarshaller();

      // Option pour indenter le XML en sortie
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      // marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new
      // JAXBNamespaceMapper());

      // Affectation du schéma XSD si spécifié
      if (xsdSchema != null) {
         SchemaFactory schemaFactory = SchemaFactory
               .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
         Schema schema = schemaFactory.newSchema(xsdSchema);
         marshaller.setSchema(schema);
      }
      // Déclenche le marshalling
      marshaller.marshal(rootElement, output);
   }

}
