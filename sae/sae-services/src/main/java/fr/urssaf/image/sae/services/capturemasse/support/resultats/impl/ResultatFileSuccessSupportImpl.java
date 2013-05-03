/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLEventReader;
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

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.IntegratedDocumentType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ListeDocumentsVirtuelsType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ListeIntegratedDocumentsType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ListeMetadonneeType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ListeNonIntegratedDocumentsType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.MetadonneeType;
import fr.urssaf.image.sae.services.capturemasse.modele.resultats.ObjectFactory;
import fr.urssaf.image.sae.services.capturemasse.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatFileSuccessSupport;
import fr.urssaf.image.sae.services.util.CaptureMasseIntegratedDocumentComparateur;
import fr.urssaf.image.sae.services.util.JAXBUtils;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Implémentation du support {@link ResultatFileSuccessSupport}
 * 
 */
@Component
public class ResultatFileSuccessSupportImpl implements
      ResultatFileSuccessSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatFileSuccessSupportImpl.class);

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
   private static final String METADONNEE = "metadonnee";
   private static final String CODE = "code";
   private static final String VALEUR = "valeur";
   private static final String NUM_PAGE_DEBUT = "numeroPageDebut";
   private static final String NUM_PAGE = "nombreDePages";

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeResultatsFile(
         final File ecdeDirectory,
         final ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> intDocuments,
         final int documentsCount, boolean restitutionUuids, File sommaireFile) {

      LOGGER.debug(
            "{} - Début de création du fichier (resultats.xml en réussite)",
            PREFIX_TRC);

      final ResultatsType resultatsType = creerResultat(documentsCount,
            intDocuments, restitutionUuids, sommaireFile);

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
    * @param intDocCount
    *           nombre de documents intégrés
    * @return le résultat sous forme d'objet
    */
   private ResultatsType creerResultat(final int documentsCount,
         ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> intDocuments,
         boolean restitutionsUuids, File sommaireFile) {

      final ResultatsType resultatsType = new ResultatsType();
      resultatsType.setBatchMode(BatchModeType.TOUT_OU_RIEN);
      resultatsType.setInitialDocumentsCount(documentsCount);
      resultatsType.setInitialVirtualDocumentsCount(0);
      resultatsType.setIntegratedDocumentsCount(intDocuments.size());
      resultatsType.setNonIntegratedDocumentsCount(0);
      resultatsType.setIntegratedVirtualDocumentsCount(0);
      resultatsType.setNonIntegratedVirtualDocumentsCount(0);
      resultatsType
            .setNonIntegratedDocuments(new ListeNonIntegratedDocumentsType());
      resultatsType
            .setNonIntegratedVirtualDocuments(new ListeDocumentsVirtuelsType());
      resultatsType.setErreurBloquanteTraitement(null);

      // Si on attend la liste des documents intégrés avec l'UUID associé
      if (restitutionsUuids) {
         // Tri des documents intégrés par index
         List<CaptureMasseIntegratedDocument> listeDocs = new ArrayList<CaptureMasseIntegratedDocument>(
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
                              "Valeur vide non autorisée dans la valeur de la balise");
                     }
                     objetNumerique.setCheminEtNomDuFichier(xmlEventTmp
                           .asCharacters().getData());
                     integratedDocumentType.setObjetNumerique(objetNumerique);
                  } else if (METADONNEE.equals(name)) {
                     metadonnee = new MetadonneeType();
                  } else if (CODE.equals(name)) {
                     reader.peek();
                     final XMLEvent xmlEventTmp = reader.peek();
                     if (!xmlEventTmp.isCharacters()) {
                        throw new CaptureMasseRuntimeException(
                              "Valeur vide non autorisée dans la valeur de la balise");
                     }
                     metadonnee.setCode(xmlEventTmp.asCharacters().getData());
                  } else if (VALEUR.equals(name)) {
                     reader.peek();
                     final XMLEvent xmlEventTmp = reader.peek();
                     if (!xmlEventTmp.isCharacters()) {
                        throw new CaptureMasseRuntimeException(
                              "Valeur vide non autorisée dans la valeur de la balise");
                     }
                     metadonnee.setValeur(xmlEventTmp.asCharacters().getData());
                     metadonnees.getMetadonnee().add(metadonnee);
                  } else if (NUM_PAGE_DEBUT.equals(name)) {
                     reader.peek();
                     final XMLEvent xmlEventTmp = reader.peek();
                     if (!xmlEventTmp.isCharacters()) {
                        throw new CaptureMasseRuntimeException(
                              "Valeur vide non autorisée dans la valeur de la balise");
                     }
                     integratedDocumentType.setNumeroPageDebut(Integer
                           .parseInt(xmlEventTmp.asCharacters().getData()));
                  } else if (NUM_PAGE.equals(name)) {
                     reader.peek();
                     final XMLEvent xmlEventTmp = reader.peek();
                     if (!xmlEventTmp.isCharacters()) {
                        throw new CaptureMasseRuntimeException(
                              "Valeur vide non autorisée dans la valeur de la balise");
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

      try {
         FileOutputStream output = new FileOutputStream(resultats);

         final Resource classPath = getContext().getResource(
               "classpath:xsd_som_res/resultats.xsd");
         URL xsdSchema;

         try {
            xsdSchema = classPath.getURL();
            JAXBUtils.marshal(resultat, output, xsdSchema);
         } catch (IOException e) {
            throw new CaptureMasseRuntimeException(e);
         } catch (JAXBException e) {
            throw new CaptureMasseRuntimeException(e);
         } catch (SAXException e) {
            throw new CaptureMasseRuntimeException(e);
         } finally {
            try {
               output.close();
            } catch (IOException e) {
               LOGGER.info("erreur de fermeture de flux", e);
            }
         }

      } catch (FileNotFoundException e) {
         throw new CaptureMasseRuntimeException(e);

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
         final ConcurrentLinkedQueue<VirtualStorageDocument> intDocuments,
         final int documentsCount, boolean restitutionUuids, File sommaireFile) {
      String trcPrefix = "writeVirtualResultatsFile";
      LOGGER.debug("{} - début", trcPrefix);

      LOGGER.debug("{} - fin", trcPrefix);
      // TODO - FBON - Auto-generated method stub

   }
}
