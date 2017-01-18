/**
 * 
 */
package fr.urssaf.image.sae.commons.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.commons.exception.StaxRuntimeException;

/**
 * Classe utilitaire de lecture de fichier XML
 * 
 */
public final class StaxReadUtils {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StaxReadUtils.class);

   /**
    * Constructeur
    */
   private StaxReadUtils() {
   }

   /**
    * méthode permettant de compter le nombre d'élements définis par le nom de
    * balise donnée dans un fichier donné
    * 
    * @param file
    *           fichier XML où effectuer la recherche
    * @param balise
    *           balise à rechercher et compter
    * @return le nombre d'occurence de la balise
    * @throws FileNotFoundException
    *            erreur soulevée lorsque le fichier n'est pas trouvé
    * @throws XMLStreamException
    *            erreur soulevée lorsqu'une erreur survient lors de la lecture
    */
   public static int compterElements(File file, String balise)
         throws FileNotFoundException, XMLStreamException {

      int nbreElem = 0;

      FileInputStream stream = null;
      XMLEventReader reader = null;
      final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

      try {
         stream = new FileInputStream(file);
         reader = xmlInputFactory.createXMLEventReader(stream);
         XMLEvent event;

         while (reader.hasNext()) {
            event = reader.nextEvent();

            if (event.isStartElement()
                  && balise.equals(event.asStartElement().getName()
                        .getLocalPart())) {
               nbreElem++;
            }
         }
      } finally {

         if (reader != null) {
            try {
               reader.close();
            } catch (XMLStreamException e) {
               LOGGER.debug("erreur de fermeture du reader "
                     + file.getAbsolutePath());
            }
         }

         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               LOGGER.debug("erreur de fermeture du flux "
                     + file.getAbsolutePath());
            }
         }
      }

      return nbreElem;

   }

   /**
    * Renvoie la valeur de la première balise avec le nom donné
    * 
    * @param file
    *           Fichier dans lequel effectuer la recherche
    * @param balise
    *           Nom de la balise dont la valeur est à récupérer
    * @return Valeur de la balise. Null si non trouvé ou ne correspondant pas à
    *         un élément texte
    * @throws FileNotFoundException
    *            erreur soulevée lorsque le fichier n'est pa trouvé
    * @throws XMLStreamException
    *            erreur soulevée lors d'un problème de lecture
    */
   public static String getElementValue(File file, String balise)
         throws FileNotFoundException, XMLStreamException {
      FileInputStream stream = null;
      XMLEventReader reader = null;
      // Fabrique de parseur
      final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

      // Ouverture du fichier XML
      try {
         stream = new FileInputStream(file);
         // Création d'un parseur de type évenementiel
         reader = xmlInputFactory.createXMLEventReader(stream);
         XMLEvent event;
         while (reader.hasNext()) {
            event = reader.nextEvent();
            if (event.isStartElement()
                  && balise.equals(event.asStartElement().getName()
                        .getLocalPart())) {
               // Récupération de la valeur
               event = reader.nextEvent();
               // Si c'est une balise texte
               if (event.isCharacters()) {
                  if (event.asCharacters().isWhiteSpace()) {
                     return null;
                  } else {
                     return event.asCharacters().getData();
                  }
               } else {
                  return null;
               }
            }
         }
         // Si la balise n'est pas trouvée
         return null;

      } finally {

         if (reader != null) {
            try {
               reader.close();
            } catch (XMLStreamException e) {
               LOGGER.debug("erreur de fermeture du reader "
                     + file.getAbsolutePath());
            }
         }

         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               LOGGER.debug("erreur de fermeture du flux "
                     + file.getAbsolutePath());
            }
         }
      }
   }

   /**
    * création du writer du fichier XML
    * 
    * @param inputStream
    *           flux d'entrée
    * @return le reader créé à partir de l'inputStream
    */
   public static XMLEventReader loadReader(InputStream inputStream) {

      final XMLInputFactory inputFactory = XMLInputFactory.newInstance();

      try {
         final XMLEventReader reader = inputFactory
               .createXMLEventReader(inputStream);
         return reader;

      } catch (XMLStreamException exception) {
         throw new StaxRuntimeException(exception);
      }
   }

}
