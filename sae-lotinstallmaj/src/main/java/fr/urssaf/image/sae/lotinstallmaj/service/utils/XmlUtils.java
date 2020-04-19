package fr.urssaf.image.sae.lotinstallmaj.service.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

/**
 * Classe utilitaire de manipulation xml
 * 
 *
 */
public class XmlUtils {
   
   /**
    * Méthode générique d'unmarshalling d'un fichier XML
    * 
    * @param <T>
    *           le type de l'élément racine
    * @param docClass
    *           la classe de l'élément racine
    * @param fichierXml
    *           le chemin du fichier XML externe. Exemple : "c:/repertoire/fichier.xml" 
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
   @SuppressWarnings("unchecked")
   public static <T> T unmarshalStream(Class<T> target, 
      InputStream xmlStream, ClassPathResource ressourceXsd) 
      throws JAXBException, SAXException, IOException {
      
      String cpackage = target.getPackage().getName();
      JAXBContext context = JAXBContext.newInstance(cpackage);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      
      //-- On set le shema xsd
      URL xsdUrl = ressourceXsd.getURL();
      String nsUri = javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
      SchemaFactory schemaFactory = SchemaFactory.newInstance(nsUri);
      unmarshaller.setSchema(schemaFactory.newSchema(xsdUrl));

      //-- Déclenche le unmarshalling
      JAXBElement<T> element;
      element = (JAXBElement<T>)unmarshaller.unmarshal(xmlStream);

      return element.getValue();
   }

}
