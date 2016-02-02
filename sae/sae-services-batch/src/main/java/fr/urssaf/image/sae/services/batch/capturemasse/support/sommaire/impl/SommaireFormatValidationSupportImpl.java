package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlValidationUtils;

/**
 * Implémentation du support {@link SommaireFormatValidationSupport}
 * 
 */
@Component
public class SommaireFormatValidationSupportImpl implements
      SommaireFormatValidationSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SommaireFormatValidationSupportImpl.class);

   private static final String SOMMAIRE_XSD = "xsd_som_res/sommaire.xsd";

   @Autowired
   private ApplicationContext context;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void validationSommaire(final File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException {

      Resource sommaireXSD = context.getResource(SOMMAIRE_XSD);
      URL xsdSchema;
      try {
         xsdSchema = sommaireXSD.getURL();
      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);
      }

      try {
         XmlValidationUtils.parse(sommaireFile, xsdSchema);

      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (ParserConfigurationException e) {
         throw new CaptureMasseSommaireFormatValidationException(e);

      } catch (SAXException e) {
         throw new CaptureMasseSommaireFormatValidationException(e);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void validerModeBatch(File sommaireFile, String batchMode)
         throws CaptureMasseSommaireFormatValidationException {

      FileInputStream sommaireStream = null;
      XMLEventReader reader = null;

      try {
         sommaireStream = new FileInputStream(sommaireFile);
         reader = openSommaire(sommaireStream);
         String mode = null;
         XMLEvent event;
         while (reader.hasNext() && StringUtils.isBlank(mode)) {
            event = reader.nextEvent();

            if (event.isStartElement()
                  && "batchMode".equals(event.asStartElement().getName()
                        .getLocalPart())) {
               event = reader.nextEvent();
               mode = event.asCharacters().getData();
            }
         }

         if (!batchMode.equals(mode)) {
            throw new CaptureMasseSommaireFormatValidationException("mode "
                  + mode + " non accepté", new Exception("Mode non accepté : "
                  + mode));
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
                     + sommaireFile.getAbsolutePath());
            }
         }

         if (sommaireStream != null) {
            try {
               sommaireStream.close();
            } catch (IOException e) {
               LOGGER.debug("erreur de fermeture du flux "
                     + sommaireFile.getAbsolutePath());
            }
         }
      }

   }

   /**
    * Ouvre le fichier sommaire et renvoie le reader
    * 
    * @param stream
    * @return
    */
   private XMLEventReader openSommaire(final InputStream stream) {
      final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

      try {
         return xmlInputFactory.createXMLEventReader(stream);

      } catch (XMLStreamException e) {
         throw new CaptureMasseRuntimeException(e);
      }
   }

}
