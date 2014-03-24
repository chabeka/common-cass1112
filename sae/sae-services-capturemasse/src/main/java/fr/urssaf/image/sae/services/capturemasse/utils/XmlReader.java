/**
 * 
 */
package fr.urssaf.image.sae.services.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.services.capturemasse.exception.EcdePermissionException;

/**
 * Classe de lecture d'un fichier XML
 * 
 */
public class XmlReader {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(XmlReader.class);

   private final File file;
   private InputStream inputStream;
   private XMLEventReader reader = null;

   /**
    * Constructeur
    * 
    * @param file
    *           fichier à lire
    */
   public XmlReader(File file) {
      this.file = file;
   }

   /**
    * Initialisation du reader
    */
   public void initStream() {

      try {
         inputStream = new FileInputStream(file);
         final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
         reader = xmlInputFactory.createXMLEventReader(inputStream);

      } catch (FileNotFoundException exception) {
         throw new EcdePermissionException(exception);

      } catch (FactoryConfigurationError exception) {
         throw new EcdePermissionException(exception);

      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }
   }

   /**
    * @return le prochain noeud du fichier
    */
   public XMLEvent nextEvent() {

      XMLEvent event = null;

      try {
         event = reader.nextEvent();

      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }

      return event;
   }

   /**
    * @return un indicateur de présence d'un noeud suivant
    */
   public boolean hasNext() {
      return reader.hasNext();
   }

   /**
    * Fermeture des streams
    */
   public void closeStream() {
      String trcPrefix = "closeStream()";

      if (reader != null) {
         try {
            reader.close();
         } catch (XMLStreamException exception) {
            LOGGER.debug(
                  "{} - Impossible de fermer l'objet de lecture du fichier {}",
                  new Object[] { trcPrefix, file.getAbsolutePath() });
         }
      }

      if (inputStream != null) {
         try {
            inputStream.close();
         } catch (IOException exception) {
            LOGGER.debug("{} - Impossible de fermer le flux du fichier {}",
                  new Object[] { trcPrefix, file.getAbsolutePath() });
         }
      }

   }

   /**
    * @return le noeud suivant sans y aller
    */
   public XMLEvent peek() {

      XMLEvent event = null;

      try {
         event = reader.peek();

      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }

      return event;
   }

}
