package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    final Resource sommaireXSD = context.getResource(SOMMAIRE_XSD);
    URL xsdSchema;
    try {
      xsdSchema = sommaireXSD.getURL();
    }
    catch (final IOException e) {
      throw new CaptureMasseRuntimeException(e);
    }

    try {
      XmlValidationUtils.parse(sommaireFile, xsdSchema);

    }
    catch (final IOException e) {
      throw new CaptureMasseRuntimeException(e);

    }
    catch (final ParserConfigurationException e) {
      throw new CaptureMasseSommaireFormatValidationException(e);

    }
    catch (final SAXException e) {
      throw new CaptureMasseSommaireFormatValidationException(e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void validerModeBatch(final File sommaireFile, final String... batchModes)
      throws CaptureMasseSommaireFormatValidationException,
      CaptureMasseSommaireFileNotFoundException {

    if (sommaireFile == null || batchModes == null
        || batchModes != null && batchModes.length == 0) {
      throw new IllegalArgumentException(
                                         "Le fichier sommaire ou le mode du batch est null. La validation du sommaire.xml à échouée.");
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
            && "batchMode".equals(event.asStartElement()
                                       .getName()
                                       .getLocalPart())) {
          event = reader.nextEvent();
          if (event.isCharacters()) {
            mode = event.asCharacters().getData();
          }
        }
      }
      for (final String batchMode : batchModes) {
        containValue = batchMode.equals(mode);
        if (containValue) {
          break;
        }
      }

      if (!containValue) {
        throw new CaptureMasseSommaireFormatValidationException("mode "
            + mode + " non accepté",
                                                                new Exception("Mode non accepté : "
                                                                    + mode));
      }

    }
    catch (final FileNotFoundException e) {
      throw new CaptureMasseSommaireFileNotFoundException(
                                                          sommaireFile.getAbsolutePath());

    }
    catch (final XMLStreamException e) {
      throw new CaptureMasseRuntimeException(e);

    }
    finally {

      if (reader != null) {
        try {
          reader.close();
        }
        catch (final XMLStreamException e) {
          LOGGER.debug("erreur de fermeture du reader "
              + sommaireFile.getAbsolutePath());
        }
      }

      if (sommaireStream != null) {
        try {
          sommaireStream.close();
        }
        catch (final IOException e) {
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

    }
    catch (final XMLStreamException e) {
      throw new CaptureMasseRuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws CaptureMasseSommaireFormatValidationException
   */
  @Override
  public void validerUniciteIdGed(final File sommaireFile)
      throws CaptureMasseSommaireFormatValidationException {
    FileInputStream sommaireStream = null;
    XMLEventReader reader = null;

    try {
      sommaireStream = new FileInputStream(sommaireFile);
      reader = openSommaire(sommaireStream);
      String nomMeta = null;
      String uuid = null;
      final List<String> listUuid = new ArrayList<>();
      XMLEvent event;

      while (reader.hasNext()) {

        // On parcourt le sommaire pour tomber sur un document
        event = reader.nextEvent();
        if (event.isStartElement()
            && "document".equals(event.asStartElement()
                                      .getName()
                                      .getLocalPart())) {

          // On continue le parcourt pour trouver la métadonnée IdGed
          while (reader.hasNext()) {
            event = reader.nextEvent();

            if (event.isStartElement()
                && "code".equals(event.asStartElement()
                                      .getName()
                                      .getLocalPart())) {
              event = reader.nextEvent();
              if (event.isCharacters()) {
                nomMeta = event.asCharacters().getData();

                // Si on trouve la métadonnée IdGed, on regarde si la
                // valeur
                // de l'UUID a déjà été utilisée
                if ("IdGed".equals(nomMeta)) {
                  while (reader.hasNext()) {
                    event = reader.nextEvent();
                    if (event.isStartElement()
                        && "valeur".equals(event.asStartElement()
                                                .getName()
                                                .getLocalPart())) {
                      event = reader.nextEvent();
                      if (event.isCharacters()) {
                        uuid = event.asCharacters().getData();

                        if (listUuid.contains(uuid)) {
                          // UUID déjà présent, on renvoie une
                          // exception
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
                      } else {
                        // Une valeur est obligatoire pour la métadonnée IdGed
                        throw new CaptureMasseSommaireFormatValidationException(
                                                                                "Valeur manquante pour la métadonnée IdGed",
                                                                                new Exception(
                                                                                              "Valeur manquante pour la métadonnée IdGed"));
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

    }
    catch (final FileNotFoundException e) {
      throw new CaptureMasseRuntimeException(e);

    }
    catch (final XMLStreamException e) {
      throw new CaptureMasseRuntimeException(e);

    }
    finally {

      if (reader != null) {
        try {
          reader.close();
        }
        catch (final XMLStreamException e) {
          LOGGER.debug("erreur de fermeture du reader "
              + sommaireFile.getAbsolutePath());
        }
      }

      if (sommaireStream != null) {
        try {
          sommaireStream.close();
        }
        catch (final IOException e) {
          LOGGER.debug("erreur de fermeture du flux "
              + sommaireFile.getAbsolutePath());
        }
      }
    }
  }

  @Override
  public void validationDocumentBaliseRequisSommaire(final File sommaireFile,
                                                     final String baliseRequired)
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
            && ("document".equals(event.asStartElement()
                                       .getName()
                                       .getLocalPart())
                || "documentMultiAction".equals(event
                                                     .asStartElement()
                                                     .getName()
                                                     .getLocalPart()))) {

          // On continue le parcourt pour trouver la métadonnée IdGed
          while (reader.hasNext()) {
            event = reader.nextEvent();

            if (event.isStartElement()
                && event.asStartElement()
                        .getName()
                        .getLocalPart()
                        .equals(baliseRequired)) {
              event = reader.nextEvent();
              if (event.isCharacters()) {
                valeurBalise = event.asCharacters().getData();

                if (StringUtils.isEmpty(valeurBalise) || StringUtils.isBlank(valeurBalise)) {
                  throw new CaptureMasseSommaireFormatValidationException(
                                                                          "La balise " + baliseRequired + " 'est vide",
                                                                          new Exception("La balise " + baliseRequired
                                                                              + " est obligatoire"));
                }
              }
            }
          }
        }
      }

    }
    catch (final FileNotFoundException e) {
      throw new CaptureMasseRuntimeException(e);

    }
    catch (final XMLStreamException e) {
      throw new CaptureMasseRuntimeException(e);

    }
    finally {

      if (reader != null) {
        try {
          reader.close();
        }
        catch (final XMLStreamException e) {
          LOGGER.debug("erreur de fermeture du reader "
              + sommaireFile.getAbsolutePath());
        }
      }

      if (sommaireStream != null) {
        try {
          sommaireStream.close();
        }
        catch (final IOException e) {
          LOGGER.debug("erreur de fermeture du flux "
              + sommaireFile.getAbsolutePath());
        }
      }
    }

  }

  @Override
  public void validationDocumentValeurBaliseRequisSommaire(final File sommaireFile,
                                                           final String baliseRequired, final String valeurRequired, final boolean verifyValue)
      throws CaptureMasseSommaireFormatValidationException {
    boolean baliseValuefind = false;
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
            && ("document".equals(event.asStartElement()
                                       .getName()
                                       .getLocalPart())
                || "documentMultiAction".equals(event
                                                     .asStartElement()
                                                     .getName()
                                                     .getLocalPart()))) {

          // On continue le parcourt pour trouver la métadonnée IdGed
          while (reader.hasNext()) {
            event = reader.nextEvent();

            if (event.isStartElement()
                && event.asStartElement()
                        .getName()
                        .getLocalPart()
                        .equals(baliseRequired)) {
              event = reader.nextEvent();
              if (event.isCharacters()) {
                valeurBalise = event.asCharacters().getData();

                if (valeurBalise.equals(valeurRequired)) {
                  // Si on trouve la balise et la valeur requise, on
                  // passe au document suivant.
                  if (verifyValue) {
                    while (reader.hasNext()) {
                      event = reader.nextEvent();
                      if (event.isStartElement()
                          && "valeur".equals(event.asStartElement()
                                                  .getName()
                                                  .getLocalPart())) {
                        event = reader.nextEvent();
                        if (event.isCharacters()) {
                          final String value = event.asCharacters().getData();

                          if (StringUtils.isEmpty(value) || StringUtils.isBlank(value.trim())) {
                            // Le valeur est vide ou contient uniquement des espaces
                            throw new CaptureMasseSommaireFormatValidationException(
                                                                                    "La balise " + valeurBalise
                                                                                        + " ne contient pas de valeur.",
                                                                                    new Exception(
                                                                                                  valeurBalise + " ne contient pas de valeur."));

                          }
                        } else {
                          // Le valeur est vide ou contient uniquement des espaces
                          throw new CaptureMasseSommaireFormatValidationException(
                                                                                  "La balise " + valeurBalise
                                                                                      + " ne contient pas de valeur.",
                                                                                  new Exception(
                                                                                                valeurBalise + " ne contient pas de valeur."));

                        }
                      }
                    }
                  }

                  baliseValuefind = true;
                  break;
                }
              }
            }
          }
          if (!baliseValuefind) {
            // Si on ne trouve pas la balise et la valeur requise, on
            // arrete
            // le traitement pour lancer une erreur de validation.
            break;
          }
        }
      }

      if (!baliseValuefind) {
        throw new CaptureMasseSommaireFormatValidationException(
                                                                "Au moins un '" + valeurRequired + "' n'est pas présent dans le sommaire. '" + valeurRequired
                                                                    + "'  est obligatoire pour tous les documents",
                                                                new Exception(
                                                                              "'" + valeurRequired + "' est obligatoire pour tous les documents."));
      }

    }
    catch (final FileNotFoundException e) {
      throw new CaptureMasseRuntimeException(e);

    }
    catch (final XMLStreamException e) {
      throw new CaptureMasseRuntimeException(e);

    }
    finally {

      if (reader != null) {
        try {
          reader.close();
        }
        catch (final XMLStreamException e) {
          LOGGER.debug("erreur de fermeture du reader "
              + sommaireFile.getAbsolutePath());
        }
      }

      if (sommaireStream != null) {
        try {
          sommaireStream.close();
        }
        catch (final IOException e) {
          LOGGER.debug("erreur de fermeture du flux "
              + sommaireFile.getAbsolutePath());
        }
      }
    }

  }

  @Override
  public void validationDocumentTypeMultiActionSommaire(final File sommaireFile)
      throws CaptureMasseSommaireFormatValidationException {

    FileInputStream sommaireStream = null;
    XMLEventReader reader = null;

    try {
      sommaireStream = new FileInputStream(sommaireFile);
      reader = openSommaire(sommaireStream);
      final String baliseRequired = "documentsMultiAction";
      boolean isPresent = false;
      XMLEvent event;

      while (reader.hasNext()) {
        // On parcourt le sommaire pour tomber sur un document
        event = reader.nextEvent();
        if (event.isStartElement()
            && "documentsMultiAction".equals(event.asStartElement()
                                                  .getName()
                                                  .getLocalPart())) {
          isPresent = true;
        }
      }
      if (!isPresent) {
        throw new CaptureMasseSommaireFormatValidationException(
                                                                "La balise " + baliseRequired + " 'est vide",
                                                                new Exception(
                                                                              "La balise " + baliseRequired + " est obligatoire"));
      }
    }
    catch (final FileNotFoundException e) {
      throw new CaptureMasseRuntimeException(e);

    }
    catch (final XMLStreamException e) {
      throw new CaptureMasseRuntimeException(e);

    }
    finally {

      if (reader != null) {
        try {
          reader.close();
        }
        catch (final XMLStreamException e) {
          LOGGER.debug("erreur de fermeture du reader "
              + sommaireFile.getAbsolutePath());
        }
      }

      if (sommaireStream != null) {
        try {
          sommaireStream.close();
        }
        catch (final IOException e) {
          LOGGER.debug("erreur de fermeture du flux "
              + sommaireFile.getAbsolutePath());
        }
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Integer> validerUniciteMeta(final File sommaireFile, final String nomMeta) throws IOException {
    XMLEventReader reader = null;
    final Map<Integer, String> listIdValeurMetaDoublons = new HashMap<>();
    int indexDocument = -1;

    try (FileInputStream sommaireStream = new FileInputStream(sommaireFile)) {
      reader = openSommaire(sommaireStream);
      String codeValue = null;
      final Map<Integer, String> listValeurMeta = new HashMap<>();
      XMLEvent event;
      boolean valeurMetaTrouve = false;

      while (reader.hasNext()) {

        // On parcourt le sommaire pour tomber sur un document
        event = reader.nextEvent();
        if (event.isStartElement()
            && ("document".equals(event.asStartElement()
                                       .getName()
                                       .getLocalPart())
                || "documentMultiAction".equals(event.asStartElement()
                                                     .getName()
                                                     .getLocalPart()))) {
          indexDocument++;
          valeurMetaTrouve = false;
          // On continue le parcourt pour trouver la métadonnée IdGed
          while (reader.hasNext()) {
            if (valeurMetaTrouve) {
              // Si on a trouvé la valeur de la métadonné, on sort de la boucle. pour incrémenter le compteur de document.
              break;
            }
            event = reader.nextEvent();

            if (event.isStartElement()
                && "code".equals(event.asStartElement()
                                      .getName()
                                      .getLocalPart())) {
              event = reader.nextEvent();
              if (event.isCharacters()) {
                codeValue = event.asCharacters().getData();

                // Si on trouve la métadonnée IdGed, on regarde si la
                // valeur
                // de l'UUID a déjà été utilisée
                if (nomMeta.equals(codeValue)) {
                  while (reader.hasNext()) {
                    if (valeurMetaTrouve) {
                      // Si on a trouvé la valeur de la métadonné, on sort de la boucle. pour incrémenter le compteur de document.
                      break;
                    }
                    event = reader.nextEvent();
                    if (event.isStartElement()
                        && "valeur".equals(event.asStartElement()
                                                .getName()
                                                .getLocalPart())) {
                      identifierValeurDoublons(reader, listValeurMeta, listIdValeurMetaDoublons, indexDocument, nomMeta);
                      valeurMetaTrouve = true;
                      break;
                    }
                  }
                }
              }
            }
          }
        }
      }

    }
    catch (final FileNotFoundException | XMLStreamException e) {
      throw new CaptureMasseRuntimeException(e);
    }
    finally {
      if (reader != null) {
        try {
          reader.close();
        }
        catch (final XMLStreamException e) {
          LOGGER.debug("erreur de fermeture du reader "
              + sommaireFile.getAbsolutePath());
        }
      }
    }
    return new ArrayList<>(listIdValeurMetaDoublons.keySet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Integer> validerUniciteTag(final File sommaireFile, final String nomTag) throws IOException {
    XMLEventReader reader = null;
    final Map<Integer, String> listValeurTagDoublons = new HashMap<>();
    int indexDocument = -1;
    try (FileInputStream sommaireStream = new FileInputStream(sommaireFile)) {
      reader = openSommaire(sommaireStream);
      final Map<Integer, String> listValeurTag = new HashMap<>();
      XMLEvent event;

      while (reader.hasNext()) {
        // On parcourt le sommaire pour tomber sur un document
        event = reader.nextEvent();
        if (event.isStartElement()
            && ("document".equals(event.asStartElement()
                                       .getName()
                                       .getLocalPart())
                || "documentMultiAction".equals(event
                                                     .asStartElement()
                                                     .getName()
                                                     .getLocalPart()))) {
          indexDocument++;
          // On continue le parcourt pour trouver la métadonnée IdGed
          while (reader.hasNext()) {
            event = reader.nextEvent();

            if (event.isStartElement()
                && StringUtils.isNotEmpty(nomTag) && nomTag.equalsIgnoreCase(event.asStartElement()
                                                                                  .getName()
                                                                                  .getLocalPart())) {
              identifierValeurDoublons(reader, listValeurTag, listValeurTagDoublons, indexDocument, nomTag);
              break;
            }
          }
        }
      }

    }
    catch (final FileNotFoundException | XMLStreamException e) {
      throw new CaptureMasseRuntimeException(e);
    }
    finally {
      if (reader != null) {
        try {
          reader.close();
        }
        catch (final XMLStreamException e) {
          LOGGER.debug("erreur de fermeture du reader "
              + sommaireFile.getAbsolutePath());
        }
      }
    }
    return new ArrayList<>(listValeurTagDoublons.keySet());
  }

  /**
   * Methode permettant d'identifier une valeur en doublons et de positionner ce doublons ainsi que la valeur d'origine dans une Map.
   * 
   * @param reader
   *          Lecteur de fichier XML
   * @param listValeurTag
   *          Map de données déjà traité
   * @param listValeurTagDoublons
   *          Map de données en doublons
   * @param indexDocument
   *          Index du document en cours de traitement
   * @param nomTag
   *          Nom de la balise XML
   * @throws XMLStreamException
   * @{@link XMLStreamException}
   * @throws CaptureMasseSommaireFormatValidationException
   * @{@link CaptureMasseSommaireFormatValidationException}
   */
  private void identifierValeurDoublons(final XMLEventReader reader, final Map<Integer, String> listValeurTag,
                                        final Map<Integer, String> listValeurTagDoublons, final int indexDocument, final String nomTag)
      throws XMLStreamException {
    String tagValue = null;
    final XMLEvent event = reader.nextEvent();
    if (event.isCharacters()) {
      tagValue = event.asCharacters().getData();
      // Premier passage
      if (listValeurTag.isEmpty()) {
        listValeurTag.put(indexDocument, tagValue);
        return;
      }
      // A partir du deuxieme passage
      for (final int indexDoc : listValeurTag.keySet()) {
        if (StringUtils.isNotEmpty(tagValue) && tagValue.equals(listValeurTag.get(indexDoc))) {
          // On ajoute dans la liste des doublons le document intégré à la liste des documents de référence.
          if (!listValeurTagDoublons.containsKey(indexDoc)) {
            listValeurTagDoublons.put(indexDoc, listValeurTag.get(indexDoc));
          }

          // On ajoute dans la liste des doublons le document en doublons.
          listValeurTagDoublons.put(indexDocument, tagValue);
          return;
        }
      }
      // on l'ajoute à la liste de référence
      listValeurTag.put(indexDocument, tagValue);
    } else {
      throw new CaptureMasseRuntimeException(
                                             "La balise " + nomTag
                                                 + " ne contient pas une chaine de caractére. Echec de validation de doublons");
    }
  }

}
