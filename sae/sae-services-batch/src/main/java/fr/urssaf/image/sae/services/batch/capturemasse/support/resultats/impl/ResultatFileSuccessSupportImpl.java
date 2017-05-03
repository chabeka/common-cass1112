/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.commons.xml.StaxReadUtils;
import fr.urssaf.image.sae.commons.xml.StaxWriteUtils;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseVirtualDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.IntegratedDocumentType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ListeIntegratedDocumentsType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ListeMetadonneeType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ListeNonIntegratedDocumentsType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ListeNonIntegratedVirtualDocumentsType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.MetadonneeType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.resultats.ObjectFactory;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatFileSuccessSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.CaptureMasseIntegratedDocumentComparateur;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.CaptureMasseVirtualDocumentComparateur;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.JAXBUtils;

/**
 * Implémentation du support {@link ResultatFileSuccessSupport}
 * 
 */
@Component
public class ResultatFileSuccessSupportImpl implements
      ResultatFileSuccessSupport {

   private static final String ERREUR_VALEUR_VIDE = "Valeur vide non autorisée dans la valeur de la balise";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatFileSuccessSupportImpl.class);

   private static final String NS_RES = "http://www.cirtil.fr/sae/resultatsXml";
   private static final String PX_RES = "";
   private static final String NS_SOMRES = "http://www.cirtil.fr/sae/commun_sommaire_et_resultat";
   private static final String PX_SOMRES = "ns2";

   private static final String PREFIX_TRC = "writeResultatsFile()";

   @Autowired
   private ApplicationContext context;

   /**
    * @return Le context.
    */
   public final ApplicationContext getContext() {
      return context;
   }

   private static final String CHEMIN_FICHIER = "cheminEtNomDuFichier";
   private static final String UUID = "UUID";
   private static final String METADONNEE = "metadonnee";
   private static final String CODE = "code";
   private static final String VALEUR = "valeur";
   private static final String NUM_PAGE_DEBUT = "numeroPageDebut";
   private static final String NUM_PAGE = "nombreDePages";


   @Override
   public void writeResultatsFile(File ecdeDirectory,
         ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> integDocs,
         int initDocCount, boolean restitutionUuids, File sommaireFile,
         String modeBatch) {

      LOGGER.debug(
            "{} - Début de création du fichier (resultats.xml en réussite)",
            PREFIX_TRC);

      final ResultatsType resultatsType = creerResultat(initDocCount,
            integDocs, restitutionUuids, sommaireFile, modeBatch);

      final ObjectFactory factory = new ObjectFactory();
      final JAXBElement<ResultatsType> resultat = factory
            .createResultats(resultatsType);

      ecrireResultat(resultat, ecdeDirectory);

      LOGGER.debug(
            "{} - Fin de création du fichier (resultats.xml en réussite)",
            PREFIX_TRC);
   }

   /**
    * Création du résultat
    * 
    * @param documentsCount
    *           nombre de documents initial
    * @param modeBatch
    * @param intDocCount
    *           nombre de documents intégrés
    * @return le résultat sous forme d'objet
    */
   private ResultatsType creerResultat(final int documentsCount,
         ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> intDocuments,
         boolean restitutionsUuids, File sommaireFile, String modeBatch) {

      final ResultatsType resultatsType = new ResultatsType();
      resultatsType.setBatchMode(BatchModeType.fromValue(modeBatch));
      resultatsType.setInitialDocumentsCount(documentsCount);
      resultatsType.setInitialVirtualDocumentsCount(0);
      resultatsType.setIntegratedDocumentsCount(intDocuments.size());
      resultatsType.setNonIntegratedDocumentsCount(0);
      resultatsType.setIntegratedVirtualDocumentsCount(0);
      resultatsType.setNonIntegratedVirtualDocumentsCount(0);
      resultatsType
            .setNonIntegratedDocuments(new ListeNonIntegratedDocumentsType());
      resultatsType
            .setNonIntegratedVirtualDocuments(new ListeNonIntegratedVirtualDocumentsType());
      resultatsType.setErreurBloquanteTraitement(null);

      // Si on attend la liste des documents intégrés avec l'UUID associé
      if (restitutionsUuids) {
         // Tri des documents intégrés par index
         List<TraitementMasseIntegratedDocument> listeDocs = new ArrayList<TraitementMasseIntegratedDocument>(
               intDocuments);
         Collections.sort(listeDocs,
               new CaptureMasseIntegratedDocumentComparateur());

         // Ouverture et lecture du fichier sommaire.xml

         FileInputStream sommaireStream = null;
         XMLEventReader reader = null;

         try {
            sommaireStream = new FileInputStream(sommaireFile);
            reader = openSommaire(sommaireStream);

            XMLEvent xmlEvent = null;
            StartElement startElement;
            EndElement endElement;
            String name;
            // On parcourt le fichier sommaire.xml et pour chaque document on
            // récupère l'uuid associé dans la liste des documents intégrés
            // (Cette liste est triée par index et comme seul le mode "Tout ou
            // rien" est pris en charge actuellement, elle correspond bien à
            // l'ordre des documents du sommaire)

            int index = -1;
            IntegratedDocumentType integratedDocumentType = null;
            FichierType objetNumerique = null;
            ListeMetadonneeType metadonnees = null;
            MetadonneeType metadonnee = null;
            ListeIntegratedDocumentsType listeIntegratesDocuments = new ListeIntegratedDocumentsType();

            while (reader.hasNext()) {

               xmlEvent = reader.nextEvent();

               if (xmlEvent.isStartElement()) {
                  startElement = xmlEvent.asStartElement();
                  name = startElement.getName().getLocalPart();
                  if ("document".equals(name)) {
                     index++;
                     integratedDocumentType = new IntegratedDocumentType();
                     objetNumerique = new FichierType();
                     metadonnees = new ListeMetadonneeType();

                     // On ajoute l'UUID au document
                     if (index >= 0 && index < listeDocs.size()) {
                        integratedDocumentType.setUuid(listeDocs.get(index)
                              .getIdentifiant().toString());
                     } else {
                        throw new CaptureMasseRuntimeException(
                              "Le document n°"
                                    + index
                                    + " n'a pas été trouvé dans la liste des document intégrés");
                     }

                  } else if (CHEMIN_FICHIER.equals(name)) {
                     final XMLEvent xmlEventTmp = reader.peek();
                     if (!xmlEventTmp.isCharacters()) {
                        throw new CaptureMasseRuntimeException(
                              ERREUR_VALEUR_VIDE);
                     }
                     objetNumerique.setCheminEtNomDuFichier(xmlEventTmp
                           .asCharacters().getData());
                     integratedDocumentType.setObjetNumerique(objetNumerique);
                  } else if (UUID.equals(name)) {
                     final XMLEvent xmlEventTmp = reader.peek();
                     if (!xmlEventTmp.isCharacters()) {
                        throw new CaptureMasseRuntimeException(
                              ERREUR_VALEUR_VIDE);
                     }
                     objetNumerique.setUUID(xmlEventTmp
                           .asCharacters().getData());
                     integratedDocumentType.setObjetNumerique(objetNumerique);
                  } else if (METADONNEE.equals(name)) {
                     metadonnee = new MetadonneeType();
                  } else if (CODE.equals(name)) {
                     reader.peek();
                     final XMLEvent xmlEventTmp = reader.peek();
                     if (!xmlEventTmp.isCharacters()) {
                        throw new CaptureMasseRuntimeException(
                              ERREUR_VALEUR_VIDE);
                     }
                     metadonnee.setCode(xmlEventTmp.asCharacters().getData());
                  } else if (VALEUR.equals(name)) {
                     reader.peek();
                     final XMLEvent xmlEventTmp = reader.peek();
                     if (!xmlEventTmp.isCharacters()) {
                        throw new CaptureMasseRuntimeException(
                              ERREUR_VALEUR_VIDE);
                     }
                     metadonnee.setValeur(xmlEventTmp.asCharacters().getData());
                     metadonnees.getMetadonnee().add(metadonnee);
                  } else if (NUM_PAGE_DEBUT.equals(name)) {
                     reader.peek();
                     final XMLEvent xmlEventTmp = reader.peek();
                     if (!xmlEventTmp.isCharacters()) {
                        throw new CaptureMasseRuntimeException(
                              ERREUR_VALEUR_VIDE);
                     }
                     integratedDocumentType.setNumeroPageDebut(Integer
                           .parseInt(xmlEventTmp.asCharacters().getData()));
                  } else if (NUM_PAGE.equals(name)) {
                     reader.peek();
                     final XMLEvent xmlEventTmp = reader.peek();
                     if (!xmlEventTmp.isCharacters()) {
                        throw new CaptureMasseRuntimeException(
                              ERREUR_VALEUR_VIDE);
                     }
                     integratedDocumentType.setNombreDePages(Integer
                           .parseInt(xmlEventTmp.asCharacters().getData()));

                  }
               } else if (xmlEvent.isEndElement()) {
                  endElement = xmlEvent.asEndElement();
                  name = endElement.getName().getLocalPart();
                  if ("document".equals(name)) {
                     integratedDocumentType.setMetadonnees(metadonnees);
                     listeIntegratesDocuments.getIntegratedDocument().add(
                           integratedDocumentType);
                  }
               }

            }

            resultatsType.setIntegratedDocuments(listeIntegratesDocuments);
         } catch (FileNotFoundException e) {
            throw new CaptureMasseRuntimeException(e);
         } catch (XMLStreamException e) {
            throw new CaptureMasseRuntimeException(e);
         }

      }
      return resultatsType;
   }

   /**
    * Ecriture du fichier de resultat
    * 
    * @param resultat
    *           objet représentant le résultat
    * @param ecdeDirectory
    *           répertoire de traitement
    */
   private void ecrireResultat(final JAXBElement<ResultatsType> resultat,
         final File ecdeDirectory) {

      final String pathResultats = ecdeDirectory.getAbsolutePath()
            + File.separator + "resultats.xml";
      final File resultats = new File(pathResultats);

      FileOutputStream output = null;
      try {
         output = new FileOutputStream(resultats);

         final Resource classPath = getContext().getResource(
               "classpath:xsd_som_res/resultats.xsd");
         URL xsdSchema;

         xsdSchema = classPath.getURL();
         JAXBUtils.marshal(resultat, output, xsdSchema);

      } catch (FileNotFoundException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (JAXBException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (SAXException e) {
         throw new CaptureMasseRuntimeException(e);

      } finally {
         try {
            if (output != null) {
               output.close();
            }
         } catch (IOException e) {
            LOGGER.info("erreur de fermeture de flux", e);
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
    */
   @Override
   public final void writeVirtualResultatsFile(final File ecdeDirectory,
         final ConcurrentLinkedQueue<CaptureMasseVirtualDocument> intDocuments,
         final int documentsCount, boolean restitutionUuids, File sommaireFile) {

      String trcPrefix = "writeVirtualResultatsFile";
      LOGGER.debug("{} - début", trcPrefix);

      File resultats = new File(ecdeDirectory, "resultats.xml");

      OutputStream stream = null;
      XMLEventWriter writer = null;

      try {
         stream = new FileOutputStream(resultats);
         writer = StaxWriteUtils.loadWriter(stream);

         XMLEventFactory eventFactory = XMLEventFactory.newInstance();
         StaxWriteUtils staxUtils = new StaxWriteUtils(eventFactory, writer);
         ecrireEntete(staxUtils, documentsCount, intDocuments.size(), true);

         if (restitutionUuids) {
            writeVirtualBody(sommaireFile, staxUtils, intDocuments, true);
         }

         staxUtils.addEndElement("resultats", PX_RES, NS_RES);

      } catch (FileNotFoundException exception) {
         throw new CaptureMasseRuntimeException(exception);

      } catch (XMLStreamException exception) {
         throw new CaptureMasseRuntimeException(exception);

      } finally {

         if (writer != null) {
            try {
               writer.close();
            } catch (Exception exception) {
               LOGGER.info(
                     "{} - impossible de fermer l'écriture du fichier {}",
                     new Object[] { trcPrefix, resultats.getAbsolutePath() });
            }
         }

         if (stream != null) {
            try {
               stream.close();
            } catch (Exception exception) {
               LOGGER
                     .info(
                           "{} - impossible de fermer le flux de sortie du fichier {}",
                           new Object[] { trcPrefix,
                                 resultats.getAbsolutePath() });
            }
         }
      }

      LOGGER.debug("{} - fin", trcPrefix);
   }

   /**
    * @param reader
    * @param staxUtils
    * @param intDocuments
    * @param isVirtual
    * @throws XMLStreamException
    */
   private void writeVirtualBody(File sommaireFile, StaxWriteUtils staxUtils,
         ConcurrentLinkedQueue<CaptureMasseVirtualDocument> intDocuments,
         boolean isVirtual) throws XMLStreamException {
      String trcPrefix = "writeBody";
      LOGGER.debug("{} - début", trcPrefix);

      String tagName = "documentsVirtuels";

      InputStream somStream = null;
      XMLEventReader reader = null;

      try {
         somStream = new FileInputStream(sommaireFile);
         reader = StaxReadUtils.loadReader(somStream);

         XMLEvent event;
         skipToTag(reader, tagName);

         List<CaptureMasseVirtualDocument> list = new ArrayList<CaptureMasseVirtualDocument>(
               intDocuments);
         Collections.sort(list, new CaptureMasseVirtualDocumentComparateur());
         int index = 0;
         while (reader.hasNext()) {
            event = reader.nextEvent();
            index = gererVirtualEvent(event, staxUtils, list, index);
         }
      } catch (FileNotFoundException exception) {
         throw new CaptureMasseRuntimeException(exception);
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (Exception exception) {
               LOGGER
                     .info(
                           "{} - impossible de fermer l'écriture du fichier {}",
                           new Object[] { trcPrefix,
                                 sommaireFile.getAbsolutePath() });
            }
         }

         if (somStream != null) {
            try {
               somStream.close();
            } catch (Exception exception) {
               LOGGER
                     .info(
                           "{} - impossible de fermer le flux de sortie du fichier {}",
                           new Object[] { trcPrefix,
                                 sommaireFile.getAbsolutePath() });
            }
         }
      }

      LOGGER.debug("{} - fin", trcPrefix);
   }

   /**
    * @param event
    * @param isVirtual
    * @param staxUtils
    * @param intDocuments
    * @throws XMLStreamException
    */
   private int gererVirtualEvent(XMLEvent event, StaxWriteUtils staxUtils,
         List<CaptureMasseVirtualDocument> intDocuments, int index)
         throws XMLStreamException {

      int tempIndex = index;
      if (event.isStartElement()) {
         String tagName = event.asStartElement().getName().getLocalPart();
         gererStartTagVirtuel(staxUtils, tagName);
      } else if (event.isEndElement()) {
         String tagName = event.asEndElement().getName().getLocalPart();
         tempIndex = gererEndTagVirtuel(staxUtils, tagName, intDocuments, index);

      } else if (event.isCharacters()) {
         String value = event.asCharacters().getData();
         staxUtils.addValue(value);
      }

      return tempIndex;

   }

   /**
    * @param staxUtils
    * @param tagName
    * @param intDocuments
    * @throws XMLStreamException
    */
   private int gererEndTagVirtuel(StaxWriteUtils staxUtils, String tagName,
         List<CaptureMasseVirtualDocument> intDocuments, int index)
         throws XMLStreamException {

      int tempIndex = index;
      if ("documentsVirtuels".equals(tagName)) {
         staxUtils.addEndTag("integratedVirtualDocuments", PX_RES, NS_RES);
      } else if ("documentVirtuel".equals(tagName)) {
         staxUtils.addEndTag("integratedDocumentVirtuel", PX_SOMRES, NS_SOMRES);
      } else if ("nombreDePages".equals(tagName)) {
         staxUtils.addEndTag(tagName, PX_SOMRES, NS_SOMRES);
         staxUtils.addStartTag("uuid", PX_SOMRES, NS_SOMRES);
         staxUtils.addValue(intDocuments.get(index).getUuid().toString());
         staxUtils.addEndTag("uuid", PX_SOMRES, NS_SOMRES);
         tempIndex++;
      } else if (!"sommaire".equalsIgnoreCase(tagName)) {
         staxUtils.addEndTag(tagName, PX_RES, NS_RES);
      }

      return tempIndex;

   }

   /**
    * @param staxUtils
    * @param tagName
    * @throws XMLStreamException
    */
   private void gererStartTagVirtuel(StaxWriteUtils staxUtils, String tagName)
         throws XMLStreamException {
      if ("documentsVirtuels".equals(tagName)) {
         staxUtils.addStartTag("integratedVirtualDocuments", PX_RES, NS_RES);
      } else if ("documentVirtuel".equals(tagName)) {
         staxUtils.addStartTag("integratedDocumentVirtuel", PX_SOMRES,
               NS_SOMRES);
      } else {
         staxUtils.addStartTag(tagName, PX_SOMRES, NS_SOMRES);
      }

   }

   /**
    * @param reader
    * @param tag
    * @return
    * @throws XMLStreamException
    */
   private XMLEvent skipToTag(XMLEventReader reader, String tag)
         throws XMLStreamException {

      XMLEvent event = null;
      boolean stop = false;

      while (reader.hasNext() && !stop) {
         event = reader.peek();
         if (event.isStartElement()
               && tag.equals(event.asStartElement().getName().getLocalPart())) {
            stop = true;
         } else {
            event = reader.nextEvent();
         }

      }

      if (event == null) {
         throw new XMLStreamException("Impossible de trouver le flag demandé");
      }

      return event;
   }

   private void ecrireEntete(StaxWriteUtils staxUtils, int documentsCount,
         int integratedDocCount, boolean isVirtualDocument)
         throws XMLStreamException {

      String standardDocCount, standardIntDoc, virtualDocCount, virtualIntDoc;
      if (isVirtualDocument) {
         standardDocCount = "0";
         standardIntDoc = "0";
         virtualDocCount = String.valueOf(documentsCount);
         virtualIntDoc = String.valueOf(integratedDocCount);
      } else {
         standardDocCount = String.valueOf(documentsCount);
         standardIntDoc = String.valueOf(integratedDocCount);
         virtualDocCount = "0";
         virtualIntDoc = "0";
      }

      // entete XML
      staxUtils.startDocument();

      // debut de document
      staxUtils.addStartElement("resultats", PX_RES, NS_RES);
      staxUtils.addDefaultPrefix(NS_RES);
      staxUtils.addPrefix(PX_SOMRES, NS_SOMRES);

      // balises d'entete
      staxUtils.createTag("batchMode", "TOUT_OU_RIEN", PX_RES, NS_RES);
      staxUtils.createTag("initialDocumentsCount", standardDocCount, PX_RES,
            NS_RES);
      staxUtils.createTag("integratedDocumentsCount", standardIntDoc, PX_RES,
            NS_RES);
      staxUtils.createTag("nonIntegratedDocumentsCount", "0", PX_RES, NS_RES);
      staxUtils.createTag("initialVirtualDocumentsCount", virtualDocCount,
            PX_RES, NS_RES);
      staxUtils.createTag("integratedVirtualDocumentsCount", virtualIntDoc,
            PX_RES, NS_RES);
      staxUtils.createTag("nonIntegratedVirtualDocumentsCount", "0", PX_RES,
            NS_RES);
      staxUtils.addStartTag("nonIntegratedDocuments", PX_RES, NS_RES);
      staxUtils.addEndTag("nonIntegratedDocuments", PX_RES, NS_RES);
      staxUtils.addStartTag("nonIntegratedVirtualDocuments", PX_RES, NS_RES);
      staxUtils.addEndTag("nonIntegratedVirtualDocuments", PX_RES, NS_RES);
   }

}
