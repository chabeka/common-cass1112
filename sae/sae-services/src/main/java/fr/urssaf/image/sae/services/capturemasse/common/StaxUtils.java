/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.common;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;

/**
 * 
 * 
 */
public class StaxUtils {

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
   public StaxUtils(XMLEventFactory eventFactory, XMLEventWriter writer) {
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
   public void addPrefix(String prefix, String uri) throws XMLStreamException {
      writer.add(eventFactory.createNamespace(prefix, uri));
   }

   /**
    * Création du tag de départ
    * 
    * @param name
    *           nom de la balise
    * @param prefix
    *           préfixe
    * @param URL
    *           namespace
    * @throws XMLStreamException
    *            exception levée si erreur d'écriture
    */
   public void addStartElement(String name, String prefix, String URL)
         throws XMLStreamException {
      writer.add(eventFactory.createStartElement(prefix, URL, name));
   }

   public void addEndElement(String name, String prefix, String URL)
         throws XMLStreamException {
      writer.add(eventFactory.createEndElement(prefix, URL, name));
   }

   /**
    * Ecriture du début du document
    * 
    * @throws XMLStreamException
    */
   public void startDocument() throws XMLStreamException {
      writer.add(eventFactory.createStartDocument());
   }

   /**
    * Création du début de tag
    * 
    * @param name
    *           Nom de la balise
    * @throws XMLStreamException
    *            exception levée s'il est impossible de créer le tag
    */
   public void addStartTag(String name, String prefix, String URL)
         throws XMLStreamException {
      writer.add(eventFactory.createStartElement(prefix,
            "http://www.cirtil.fr/sae/commun_sommaire_et_resultat", name));
   }

   /**
    * création du tag de fin
    * 
    * @param name
    *           nom de la balise
    * @throws XMLStreamException
    *            exception levée s'il est impossible de créer le tag
    */
   public void addEndTag(String name, String prefix, String url)
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
   public void addValue(String value) throws XMLStreamException {
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
    * @throws XMLStreamException
    *            exception levée s'il est impossible de créer le tag
    */
   public void createTag(String name, String value, String prefix, String url)
         throws XMLStreamException {
      // BatchMode
      addStartTag(name, prefix, url);
      addValue(value);
      addEndTag(name, prefix, url);
   }
}
