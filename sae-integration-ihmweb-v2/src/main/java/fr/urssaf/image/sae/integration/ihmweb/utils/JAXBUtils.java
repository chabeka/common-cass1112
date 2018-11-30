package fr.urssaf.image.sae.integration.ihmweb.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

/**
 * Cette classe permet de pratiquer le marshalling et le unmarshalling <br>
 * à savoir JAVA->XML et XML->JAVA. Les methodes respectent egalement le format
 * des schemas XSD.
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
    *           le fichier dans lequel écrire le résultat du marshalling
    * @param xsdSchema
    *           le schéma XSD. Peut être null
    * @param validationHandler
    *           le handler de validation. Peut être null
    * @throws JAXBException
    *            en cas d'exception levée par JAXB
    * @throws SAXException
    *            en cas d'exception levée par le parser SAX
    */
   public static <T> void marshal(JAXBElement<T> rootElement, File output,
         File xsdSchema, ValidationEventHandler validationHandler)
         throws JAXBException, SAXException {

      // Création des objets nécessaires
      JAXBContext context = JAXBContext.newInstance(rootElement
            .getDeclaredType().getPackage().getName());
      Marshaller marshaller = context.createMarshaller();

      // Option pour indenter le XML en sortie
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      // Affectation du schéma XSD si spécifié
      if (xsdSchema != null) {
//         SchemaFactory schemaFactory = SchemaFactory
//               .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
         SchemaFactory schemaFactory = SchemaFactory.newInstance(
               "http://www.w3.org/2001/XMLSchema");
         Schema schema = schemaFactory.newSchema(xsdSchema);
         marshaller.setSchema(schema);
      }

      // Ajout d'un handler pour les erreurs de validation
      marshaller.setEventHandler(validationHandler);

      // Déclenche le marshalling
      marshaller.marshal(rootElement, output);

   }

   
   /**
    * Méthode générique d'unmarshalling avec JAXB
    * 
    * @param <T>
    *           le type de l'élément racine
    * @param docClass
    *           la classe de l'élément racine
    * @param input
    *           le fichier depuis lequel lire le XML
    * @param xsdSchema
    *           le schéma XSD. Peut être null
    * @param validationHandler
    *           le handler de validation. Peut être null
    * @return l'élément racine
    * @throws JAXBException
    *            en cas d'exception levée par JAXB
    * @throws SAXException
    *            en cas d'exception levée par le parser SAX
    */
   @SuppressWarnings("unchecked")
   public static <T> T unmarshal(
         Class<T> docClass, 
         InputStream input, 
         File xsdSchema,
         ValidationEventHandler validationHandler)
      throws JAXBException,SAXException {

      // Création des objets nécessaires
      String packageName = docClass.getPackage().getName();
      JAXBContext context = JAXBContext.newInstance(packageName);
      Unmarshaller unmarshaller = context.createUnmarshaller();

      // Affectation du schéma XSD si spécifié
      if (xsdSchema != null) {
//         SchemaFactory schemaFactory = SchemaFactory
//               .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
         SchemaFactory schemaFactory = SchemaFactory.newInstance(
               "http://www.w3.org/2001/XMLSchema");
         Schema schema = schemaFactory.newSchema(xsdSchema);
         unmarshaller.setSchema(schema);
      }

      // Déclenche le unmarshalling
      JAXBElement<T> doc = (JAXBElement<T>) unmarshaller.unmarshal(input);

      // Renvoie de la valeur de retour
      return doc.getValue();

   }
   
   

   /**
    * Méthode générique d'unmarshalling d'un fichier XML embarqué dans le projet
    * en tant que ressource, avec un fichier XSD correspondant, également embarqué
    * dans le projet
    * 
    * @param <T>
    *           le type de l'élément racine
    * @param docClass
    *           la classe de l'élément racine
    * @param cheminRessourceXml
    *           le chemin du fichier XML embarqué dans les ressources. Exemple : "/repertoire/fichier.xml" 
    * @param cheminRessourceXsd
    *           le chemin du fichier XSD embarqué dans les ressources. Exemple : "/repertoire/fichier.xsd"
    * @return l'élément racine
    * @throws JAXBException
    *            en cas d'exception levée par JAXB
    * @throws SAXException
    *            en cas d'exception levée par le parser SAX
    * @throws IOException
    *            en cas de problème de lecture des fichiers de ressource
    */
   public static <T> T unmarshalResource(
         Class<T> docClass, 
         String cheminRessourceXml,
         String cheminRessourceXsd)
      throws JAXBException, SAXException, IOException {
      
      ClassPathResource ressourceXml = new ClassPathResource(cheminRessourceXml);
      InputStream inputStreamXml = ressourceXml.getInputStream();
      
      ClassPathResource ressourceXsd = new ClassPathResource(cheminRessourceXsd);
      String xsdSchemaPath = ressourceXsd.getFile().getAbsolutePath();
      
      File xsdSchema = new File(xsdSchemaPath);
      
      return unmarshal(docClass,inputStreamXml,xsdSchema,null);
      
   }
   

}
