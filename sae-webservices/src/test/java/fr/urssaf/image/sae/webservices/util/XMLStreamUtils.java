package fr.urssaf.image.sae.webservices.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * Classe utilitaire pour la classe {@link XMLStreamReader}
 * 
 * 
 */
public final class XMLStreamUtils {

   private XMLStreamUtils() {

   }

   /**
    * instanciation d'un objet de type {@link XMLStreamReader}
    * 
    * @param filePath
    *           chemin du fichier
    * @return instance de {@link XMLStreamReader} correspondant au fichier
    */
   public static XMLStreamReader createXMLStreamReader(String filePath) {

      try {

         File file = new File(filePath);
         FileInputStream fis = new FileInputStream(file);
         XMLInputFactory xif = XMLInputFactory.newInstance();
         return xif.createXMLStreamReader(fis);

      } catch (FileNotFoundException e) {

         throw new NestableRuntimeException(e);

      } catch (XMLStreamException e) {
         throw new NestableRuntimeException(e);
      }
   }
}
