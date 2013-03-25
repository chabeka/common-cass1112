/**
 * 
 */
package fr.urssaf.image.sae.commons.xml;

import java.io.OutputStream;

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;

import fr.urssaf.image.sae.commons.exception.StaxRuntimeException;

/**
 * 
 * 
 */
public class StaxWriteUtils {

   private static final String INDENTATION = "    ";
   private final XMLEventFactory eventFactory;

   private final XMLEventWriter writer;

   /**
    * Constructeur
    * 
    * @param eventFactory
    *           pour la création des tags
    * @param writer
    *           pour l'écriture
    */
   public StaxWriteUtils(XMLEventFactory eventFactory, XMLEventWriter writer) {
      super();
      this.eventFactory = eventFactory;
      this.writer = writer;
   }

   /**
    * Ajout d'un prefixe dans le document
    * 
    * @param prefix
    *           préfixe à utiliser
    * @param uri
    *           uri à utiliser
    * @throws XMLStreamException
    *            exception levée si erreur d'écriture
    */
   public final void addPrefix(String prefix, String uri)
         throws XMLStreamException {
      writer.add(eventFactory.createNamespace(prefix, uri));
   }

   /**
    * Ajout d'un prefixe dans le document
    * 
    * @param uri
    *           uri à utiliser
    * @throws XMLStreamException
    *            exception levée si erreur d'écriture
    */
   public final void addDefaultPrefix(String uri) throws XMLStreamException {
      writer.add(eventFactory.createNamespace(uri));
   }

   /**
    * Création du tag de départ
    * 
    * @param name
    *           nom de la balise
    * @param prefix
    *           préfixe
    * @param url
    *           namespace
    * @throws XMLStreamException
    *            exception levée si erreur d'écriture
    */
   public final void addStartElement(String name, String prefix, String url)
         throws XMLStreamException {
      writer.add(eventFactory.createStartElement(prefix, url, name));
   }

   /**
    * Ajout d'un élément de fin
    * 
    * @param name
    *           nom de la balise
    * @param prefix
    *           préfixe
    * @param url
    *           namespace
    * @throws XMLStreamException
    *            exception levée si erreur d'écriture
    */
   public final void addEndElement(String name, String prefix, String url)
         throws XMLStreamException {
      writer.add(eventFactory.createEndElement(prefix, url, name));
   }

   /**
    * Ecriture du début du document
    * 
    * @throws XMLStreamException
    *            exception levée si erreur d'écriture
    */
   public final void startDocument() throws XMLStreamException {

      StartDocument startDoc = eventFactory.createStartDocument("UTF-8", "1.0");
      writer.add(startDoc);
   }

   /**
    * Création du début de tag
    * 
    * @param name
    *           Nom de la balise
    * @param prefix
    *           préfixe
    * @param url
    *           namespace
    * @throws XMLStreamException
    *            exception levée s'il est impossible de créer le tag
    */
   public final void addStartTag(String name, String prefix, String url)
         throws XMLStreamException {
      writer.add(eventFactory.createStartElement(prefix,
            "http://www.cirtil.fr/sae/commun_sommaire_et_resultat", name));
   }

   /**
    * création du tag de fin
    * 
    * @param name
    *           nom de la balise
    * @param prefix
    *           préfixe
    * @param url
    *           namespace
    * @throws XMLStreamException
    *            exception levée s'il est impossible de créer le tag
    */
   public final void addEndTag(String name, String prefix, String url)
         throws XMLStreamException {
      writer.add(eventFactory.createEndElement(prefix, url, name));
   }

   /**
    * Insertion de la valeur
    * 
    * @param value
    *           valeur
    * @throws XMLStreamException
    *            exception levée s'il est impossible de créer le tag
    */
   public final void addValue(String value) throws XMLStreamException {
      writer.add(eventFactory.createCharacters(value));
   }

   /**
    * Création du tag complet :
    * <ul>
    * <li>balise de début</li>
    * <li>valeur</li>
    * <li>balise de fin</li>
    * </ul>
    * 
    * @param name
    *           nom de la balise
    * @param value
    *           valeur
    * @param prefix
    *           préfixe
    * @param url
    *           namespace
    * @throws XMLStreamException
    *            exception levée s'il est impossible de créer le tag
    */
   public final void createTag(String name, String value, String prefix,
         String url) throws XMLStreamException {
      // BatchMode
      addStartTag(name, prefix, url);
      addValue(value);
      addEndTag(name, prefix, url);
   }

   /**
    * création du writer du fichier XML
    * 
    * @param outputStream
    *           flux de sortie
    * @return le writer créé à partir de l'outputStream
    */
   public static XMLEventWriter loadWriter(OutputStream outputStream) {

      final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

      try {
         final XMLEventWriter writer = outputFactory
               .createXMLEventWriter(outputStream);
         IndentingXMLEventWriter iWriter = new IndentingXMLEventWriter(writer);
         iWriter.setIndent(INDENTATION);
         return iWriter;

      } catch (XMLStreamException exception) {
         throw new StaxRuntimeException(exception);
      }
   }
}
