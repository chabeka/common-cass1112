/**
 * 
 */
package fr.urssaf.image.sae.services.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;

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

}
