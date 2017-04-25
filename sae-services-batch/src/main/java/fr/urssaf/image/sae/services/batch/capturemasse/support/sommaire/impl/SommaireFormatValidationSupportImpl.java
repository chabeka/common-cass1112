package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
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
   public final void validerModeBatch(File sommaireFile, String... batchModes)
         throws CaptureMasseSommaireFormatValidationException,
         CaptureMasseSommaireFileNotFoundException {

      if (sommaireFile == null || batchModes == null
            || (batchModes != null && batchModes.length == 0)) {
         throw new IllegalArgumentException(
               "Le fichier sommaire ou le mode du batch es null. La validation du sommaire.xml à échouée.");
      }

      FileInputStream sommaireStream = null;
      XMLEventReader reader = null;
      boolean containValue = false;

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
         for (String batchMode : batchModes) {
            containValue = batchMode.equals(mode);
            if (containValue) {
               break;
            }
         }

         if (!containValue) {
            throw new CaptureMasseSommaireFormatValidationException("mode "
                  + mode + " non accepté", new Exception("Mode non accepté : "
                  + mode));
         }

      } catch (FileNotFoundException e) {
         throw new CaptureMasseSommaireFileNotFoundException(
               sommaireFile.getAbsolutePath());

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

   /**
    * {@inheritDoc}
    * 
    * @throws CaptureMasseSommaireFormatValidationException
    */
   @Override
   public void validerUniciteIdGed(File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException {
      FileInputStream sommaireStream = null;
      XMLEventReader reader = null;

      try {
         sommaireStream = new FileInputStream(sommaireFile);
         reader = openSommaire(sommaireStream);
         String nomMeta = null;
         String uuid = null;
         List<String> listUuid = new ArrayList<String>();
         XMLEvent event;

         while (reader.hasNext()) {

            // On parcourt le sommaire pour tomber sur un document
            event = reader.nextEvent();
            if (event.isStartElement()
                  && "document".equals(event.asStartElement().getName()
                        .getLocalPart())) {

               // On continue le parcourt pour trouver la métadonnée IdGed
               while (reader.hasNext()) {
                  event = reader.nextEvent();

                  if (event.isStartElement()
                        && "code".equals(event.asStartElement().getName()
                              .getLocalPart())) {
                     event = reader.nextEvent();
                     nomMeta = event.asCharacters().getData();

                     // Si on trouve la métadonnée IdGed, on regarde si la
                     // valeur
                     // de l'UUID a déjà été utilisée
                     if ("IdGed".equals(nomMeta)) {
                        while (reader.hasNext()) {
                           event = reader.nextEvent();
                           if (event.isStartElement()
                                 && "valeur".equals(event.asStartElement()
                                       .getName().getLocalPart())) {
                              event = reader.nextEvent();
                              uuid = event.asCharacters().getData();

                              if (listUuid.contains(uuid)) {
                                 // UUID déjà présent, on renvoie une exception
                                 throw new CaptureMasseSommaireFormatValidationException(
                                       "IdGed " + uuid
                                             + " présent plusieurs fois",
                                       new Exception(
                                             "IdGed présent plusieurs fois : "
                                                   + uuid));

                              } else {
                                 // UUID inconnu, on l'ajoute à la liste
                                 listUuid.add(uuid);
                                 break;
                              }
                           }
                        }
                     }
                  }
               }
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

   @Override
   public void validationDocumentBaliseRequisSommaire(File sommaireFile,
         String baliseRequired)
         throws CaptureMasseSommaireFormatValidationException {

      FileInputStream sommaireStream = null;
      XMLEventReader reader = null;

      try {
         sommaireStream = new FileInputStream(sommaireFile);
         reader = openSommaire(sommaireStream);
         String valeurBalise = null;
         XMLEvent event;

         while (reader.hasNext()) {

            // On parcourt le sommaire pour tomber sur un document
            event = reader.nextEvent();
            if (event.isStartElement()
                  && "document".equals(event.asStartElement().getName()
                        .getLocalPart())) {

               // On continue le parcourt pour trouver la métadonnée IdGed
               while (reader.hasNext()) {
                  event = reader.nextEvent();

                  if (event.isStartElement()
                        && event.asStartElement().getName().getLocalPart()
                              .equals(baliseRequired)) {
                     event = reader.nextEvent();
                     valeurBalise = event.asCharacters().getData();

                     if (valeurBalise.isEmpty()) {
                        throw new CaptureMasseSommaireFormatValidationException(
                              "La balise " + baliseRequired + " 'est vide",
                              new Exception("La balise " + baliseRequired
                                    + " est obligatoire"));
                     }
                  }
               }
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

   @Override
   public void validerUniciteUuid(File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException {
      FileInputStream sommaireStream = null;
      XMLEventReader reader = null;

      try {
         sommaireStream = new FileInputStream(sommaireFile);
         reader = openSommaire(sommaireStream);
         String uuid = null;
         List<String> listUuid = new ArrayList<String>();
         XMLEvent event;

         while (reader.hasNext()) {

            // On parcourt le sommaire pour tomber sur un document
            event = reader.nextEvent();
            if (event.isStartElement()
                  && "document".equals(event.asStartElement().getName()
                        .getLocalPart())) {

               // On continue le parcourt pour trouver la métadonnée IdGed
               while (reader.hasNext()) {
                  event = reader.nextEvent();

                  if (event.isStartElement()
                        && "UUID".equalsIgnoreCase(event.asStartElement()
                              .getName().getLocalPart())) {
                     event = reader.nextEvent();
                     uuid = event.asCharacters().getData();

                     if (listUuid.contains(uuid)) {
                        // UUID déjà présent, on renvoie une exception
                        throw new CaptureMasseSommaireFormatValidationException(
                              "UUID du document " + uuid
                                    + " présent plusieurs fois", new Exception(
                                    "UUID présent plusieurs fois : " + uuid));

                     } else {
                        // UUID inconnu, on l'ajoute à la liste
                        listUuid.add(uuid);
                        break;
                     }
                  }
               }
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

   @Override
   public void validationDocumentTypeMultiActionSommaire(File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException {

      FileInputStream sommaireStream = null;
      XMLEventReader reader = null;

      try {
         sommaireStream = new FileInputStream(sommaireFile);
         reader = openSommaire(sommaireStream);
         String baliseRequired = "documentsMultiAction";
         boolean isPresent = false;
         XMLEvent event;

         while (reader.hasNext()) {
            // On parcourt le sommaire pour tomber sur un document
            event = reader.nextEvent();
            if (event.isStartElement()
                  && "documentsMultiAction".equals(event.asStartElement()
                        .getName().getLocalPart())) {
               isPresent = true;
            }
         }
         if (!isPresent)
            throw new CaptureMasseSommaireFormatValidationException(
                  "La balise " + baliseRequired + " 'est vide", new Exception(
                        "La balise " + baliseRequired + " est obligatoire"));
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

}
