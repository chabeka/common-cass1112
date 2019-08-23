/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.utils;

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

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;

/**
 * Classe utilitaire de lecture de fichier XML
 * 
 */
public final class XmlReadUtils {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(XmlReadUtils.class);

   /**
    * Constructeur
    */
   private XmlReadUtils() {
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
    */
   public static int compterElements(File file, String balise) {

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

      } catch (FileNotFoundException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (XMLStreamException e) {
         throw new CaptureMasseRuntimeException(e);

      } finally {
         closeReader(reader, file);
         closeStream(stream, file);
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
    */
   public static String getElementValue(File file, String balise) {
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
      } catch (FileNotFoundException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (XMLStreamException e) {
         throw new CaptureMasseRuntimeException(e);

      } finally {
         closeReader(reader, file);
         closeStream(stream, file);
      }
   }

   private static void closeReader(XMLEventReader reader, File file) {
      if (reader != null) {
         try {
            reader.close();
         } catch (XMLStreamException e) {
            LOGGER.debug("erreur de fermeture du reader "
                  + file.getAbsolutePath());
         }
      }
   }

   private static void closeStream(InputStream stream, File file) {
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
