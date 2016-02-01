/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.utils.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.EcdePermissionException;
import fr.urssaf.image.sae.services.capturemasse.utils.StaxUtils;

/**
 * Implémentation du service {@link StaxUtils}. Cette classe est une
 * implémentation qui est instanciée à chaque requête HTTP. Elle est accessible
 * par l'annotation @Autowired.
 * 
 */
@Component
public class StaxUtilsImpl implements StaxUtils {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StaxUtilsImpl.class);
   private static final String INDENTATION = "    ";
   private final XMLEventFactory eventFactory = XMLEventFactory.newInstance();
   private XMLEventWriter writer = null;
   private File file;
   private OutputStream outputStream = null;

   /**
    * {@inheritDoc}
    */
   @Override
   public void initStream(File file) {
      try {
         this.file = file;
         outputStream = new FileOutputStream(file);
         final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
         final XMLEventWriter eWriter = outputFactory
               .createXMLEventWriter(outputStream);
         IndentingXMLEventWriter iWriter = new IndentingXMLEventWriter(eWriter);
         iWriter.setIndent(INDENTATION);
         writer = iWriter;

      } catch (FileNotFoundException exception) {
         throw new EcdePermissionException(exception);

      } catch (FactoryConfigurationError exception) {
         throw new EcdePermissionException(exception);

      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void closeAll() {

      String trcPrefix = "closeAll()";

      if (writer != null) {
         try {
            writer.close();
         } catch (XMLStreamException exception) {
            LOGGER.debug(
                  "{} - Impossible de fermer l'objet d'écriture du fichier {}",
                  new Object[] { trcPrefix, file.getAbsolutePath() });
         }
      }

      if (outputStream != null) {
         try {
            outputStream.close();
         } catch (IOException exception) {
            LOGGER.debug("{} - Impossible de fermer le flux du fichier {}",
                  new Object[] { trcPrefix, file.getAbsolutePath() });
         }
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addPrefix(String prefix, String uri) {
      try {
         writer.add(eventFactory.createNamespace(prefix, uri));
      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addDefaultPrefix(String uri) {
      try {
         writer.add(eventFactory.createNamespace(uri));

      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addStartElement(String name, String prefix, String url) {
      try {
         writer.add(eventFactory.createStartElement(prefix, url, name));
      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addEndElement(String name, String prefix, String url) {
      try {
         writer.add(eventFactory.createEndElement(prefix, url, name));
      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void startDocument() {

      try {
         writer.add(eventFactory.createStartDocument("UTF-8", "1.0"));
      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addStartTag(String name, String prefix, String url) {
      try {
         writer.add(eventFactory.createStartElement(prefix,
               "http://www.cirtil.fr/sae/commun_sommaire_et_resultat", name));

      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addEndTag(String name, String prefix, String url) {
      try {
         writer.add(eventFactory.createEndElement(prefix, url, name));
      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addValue(String value) {
      try {
         writer.add(eventFactory.createCharacters(value));
      } catch (XMLStreamException exception) {
         throw new EcdePermissionException(exception);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void createTag(String name, String value, String prefix,
         String url) {
      // BatchMode
      addStartTag(name, prefix, url);
      addValue(value);
      addEndTag(name, prefix, url);
   }
}
