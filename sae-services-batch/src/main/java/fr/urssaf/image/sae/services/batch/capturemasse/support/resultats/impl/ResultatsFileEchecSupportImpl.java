/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.impl;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.CaptureMasseErreur;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.EcdePermissionException;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.EcdeConnexionConfiguration;
import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.StaxUtils;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlReader;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Implémentation du support {@link ResultatsFileEchecSupport}
 * 
 */
@Component
public class ResultatsFileEchecSupportImpl implements ResultatsFileEchecSupport {

   private static final String CHEMIN_FICHIER = "cheminEtNomDuFichier";
   private static final String OBJET_NUMERIQUE = "objetNumerique";
   private static final String NUMERO_PAGE_DEBUT = "numeroPageDebut";
   private static final String NOMBRE_PAGES = "nombreDePages";
   private static final String METADONNEES = "metadonnees";
   private static final String UUID = "uuid";
   private static final String NON_INTEGRATED_DOCUMENTS = "nonIntegratedDocuments";
   private static final String NON_INTEGRATED_DOCUMENT = "nonIntegratedDocument";
   private static final String NON_INTEGRATED_VIRT_DOCS = "nonIntegratedVirtualDocuments";
   private static final String NON_INTEGRATED_VIRT_DOC = "nonIntegratedVirtualDocument";
   private static final String INTEGRATED_DOCUMENTS = "integratedDocuments";
   private static final String INTEGRATED_DOCUMENT = "integratedDocument";
   private static final String INTEGRATED_VIRT_DOCS = "integratedVirtualDocuments";
   private static final String INTEGRATED_VIRT_DOC = "integratedVirtualDocument";
   private static final String ERREURS = "erreurs";
   private static final String ERREUR = "erreur";

   private static final String NS_RES = "http://www.cirtil.fr/sae/resultatsXml";
   private static final String PX_RES = "";
   private static final String NS_SOMRES = "http://www.cirtil.fr/sae/commun_sommaire_et_resultat";
   private static final String PX_SOMRES = "ns2";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileEchecSupportImpl.class);

   @Autowired
   private EcdeConnexionConfiguration configuration;

   @Autowired
   private StaxUtils staxUtils;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeResultatsFile(final File ecdeDirectory,
         final File sommaireFile, final CaptureMasseErreur erreur,
         final int nombreDocsTotal, final int nbDocumentsIntegres,
         final String batchModeTraitement, ConcurrentLinkedQueue<?> listIntDocs) {

      writeResultatsFile(sommaireFile, erreur, nombreDocsTotal,
            nbDocumentsIntegres, batchModeTraitement, listIntDocs, false);

   }

   /**
    * @param ecdeDirectory
    * @param sommaireFile
    * @param erreur
    * @param nombreDocsTotal
    * @param isVirtual
    */
   private void writeResultatsFile(File sommaireFile,
         CaptureMasseErreur erreur, int nombreDocsTotal,
         final int nbDocumentsIntegres, final String batchModeTraitement,
         ConcurrentLinkedQueue<?> listIntDocs, boolean isVirtual) {

      String trcPrefix = "writeResultatsFile()";

      LOGGER.debug(
            "{} - Début de création du fichier (resultats.xml en erreur)",
            trcPrefix);
      File resultats = new File(sommaireFile.getParentFile(), "resultats.xml");

      XmlReader reader = new XmlReader(sommaireFile);
      Throwable storedEx = null;
      int index = 0;
      boolean writeFinished = false;

      while (index < configuration.getNbreEssaiMax() && !writeFinished) {

         try {
            reader.initStream();
            staxUtils.initStream(resultats);

            ecrireFichierResultat(reader, nombreDocsTotal, nbDocumentsIntegres,
                  batchModeTraitement, erreur, isVirtual, listIntDocs);

            writeFinished = true;

         } catch (EcdePermissionException exception) {
            storedEx = exception.getCause();

         } finally {

            staxUtils.closeAll();
            reader.closeStream();
         }

         index++;
      }

      if (!writeFinished) {
         throw new CaptureMasseRuntimeException(storedEx);
      }

      LOGGER.debug("{} - Fin de création du fichier (resultats.xml en erreur)",
            trcPrefix);

   }

   /**
    * Ecriture du fichier resultats.xml
    * 
    * @param reader
    * @param nombreDocsTot
    * @param isVirtual
    */
   private void ecrireFichierResultat(final XmlReader reader,
         final int nombreDocsTot, final int nombreDocsIntegres,
         final String batchModeTraitement, final CaptureMasseErreur erreur,
         boolean isVirtual, ConcurrentLinkedQueue<?> listIntDocs) {

      ecrireEntete(nombreDocsTot, nombreDocsIntegres, batchModeTraitement,
            isVirtual);

      if (Constantes.BATCH_MODE.TOUT_OU_RIEN.getModeNom().equals(
            batchModeTraitement)) {

         this.ecrireFichierResultatDocumentsNonIntegres(reader, isVirtual,
               erreur);

      } else if (Constantes.BATCH_MODE.PARTIEL.getModeNom().equals(
            batchModeTraitement)) {
         // Lecture du sommaire pour la gestion des documents non intégrés.
         this.ecrireFichierResultatDocumentsNonIntegres(reader, isVirtual,
               erreur, listIntDocs);

         if (listIntDocs != null && !listIntDocs.isEmpty()) {
            // Réinitialisation du reader
            reader.initStream();

            // Lecture du sommaire pour la gestion des documents intégrés.
            this.ecrireFichierResultatDocumentsIntegres(reader, isVirtual,
                  erreur, listIntDocs);
         } else {
            staxUtils.addStartTag(INTEGRATED_DOCUMENTS, PX_RES, PX_SOMRES);
            staxUtils.addEndTag(INTEGRATED_DOCUMENTS, PX_SOMRES, NS_SOMRES);

            staxUtils.addStartTag(INTEGRATED_VIRT_DOCS, PX_RES, PX_SOMRES);
            staxUtils.addEndTag(INTEGRATED_VIRT_DOCS, PX_SOMRES, NS_SOMRES);
         }

      }

      staxUtils.addEndTag("resultats", PX_RES, NS_RES);
   }

   /**
    * Methode permettant d'ecrire le fichier resultat pour les documents
    * 
    * @param reader
    * @param isVirtual
    * @param listIntDocs
    * @param erreur2
    */
   @SuppressWarnings("unchecked")
   private void ecrireFichierResultatDocumentsIntegres(XmlReader reader,
         boolean isVirtual, CaptureMasseErreur erreur,
         ConcurrentLinkedQueue<?> listIntDocs) {
      XMLEvent xmlEvent = null;
      StartElement startElement;
      EndElement endElement;
      String name;
      IndexReference indexReference = new IndexReference(0, 0);
      ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> listIntDocTemp = new ConcurrentLinkedQueue<CaptureMasseIntegratedDocument>();
      listIntDocTemp
            .addAll((Collection<? extends CaptureMasseIntegratedDocument>) listIntDocs);

      if (isVirtual) {
         staxUtils.addStartTag(INTEGRATED_DOCUMENTS, PX_RES, PX_SOMRES);
         staxUtils.addEndTag(INTEGRATED_DOCUMENTS, PX_SOMRES, NS_SOMRES);
      }

      while (reader.hasNext()) {

         xmlEvent = reader.nextEvent();

         if (xmlEvent.isStartElement()) {
            startElement = xmlEvent.asStartElement();
            name = startElement.getName().getLocalPart();

            if (isVirtual) {
               indexReference = startTagVirtuelPartiel(name, reader, erreur,
                     indexReference, listIntDocTemp);
            } else {
               if ("documents".equals(name)) {
                  staxUtils.addStartTag(INTEGRATED_DOCUMENTS, PX_RES, NS_RES);
               } else {
                  startTagDocsIntegres(name, reader, listIntDocTemp,
                        indexReference);
               }
            }
         } else if (xmlEvent.isEndElement()) {
            endElement = xmlEvent.asEndElement();
            name = endElement.getName().getLocalPart();

            if (isVirtual) {
               endElementVirtuelIntegrated(name);
            } else {
               endElementIntegrated(name);
            }

         }
      }
      if (!isVirtual) {
         staxUtils.addStartTag(INTEGRATED_VIRT_DOCS, PX_RES, PX_SOMRES);
         staxUtils.addEndTag(INTEGRATED_VIRT_DOCS, PX_SOMRES, NS_SOMRES);
      }
   }

   /**
    * Methode permettant de
    * 
    * @param reader
    * @param isVirtual
    * @param erreur2
    * @param listIntDocs
    */
   private void ecrireFichierResultatDocumentsNonIntegres(XmlReader reader,
         boolean isVirtual, CaptureMasseErreur erreur,
         ConcurrentLinkedQueue<?> listIntDocs) {
      XMLEvent xmlEvent = null;
      StartElement startElement;
      EndElement endElement;
      String name;
      IndexReference indexReference = new IndexReference(0, 0);

      if (isVirtual) {
         staxUtils.addStartTag(NON_INTEGRATED_DOCUMENTS, PX_RES, PX_SOMRES);
         staxUtils.addEndTag(NON_INTEGRATED_DOCUMENTS, PX_SOMRES, NS_SOMRES);
      }
      while (reader.hasNext()) {

         xmlEvent = reader.nextEvent();

         if (xmlEvent.isStartElement()) {
            startElement = xmlEvent.asStartElement();
            name = startElement.getName().getLocalPart();

            if (isVirtual) {
               indexReference = startTagVirtuel(name, reader, erreur,
                     indexReference);
            } else {
               if ("documents".equals(name)) {
                  staxUtils.addStartTag(NON_INTEGRATED_DOCUMENTS, PX_RES,
                        NS_RES);
               } else {
                  indexReference = startTagDocsNonIntegres(name, reader,
                        erreur, indexReference, listIntDocs);
               }
            }
            // s'il n'y a pas de nouvelle index de reference, c'est que l'on a
            // pas
         } else if (xmlEvent.isEndElement()) {
            endElement = xmlEvent.asEndElement();
            name = endElement.getName().getLocalPart();

            if (isVirtual) {
               endElementVirtuel(name);
            } else {
               endElement(name);
            }
         }
      }

      if (!isVirtual) {
         staxUtils.addStartTag(NON_INTEGRATED_VIRT_DOCS, PX_RES, PX_SOMRES);
         staxUtils.addEndTag(NON_INTEGRATED_VIRT_DOCS, PX_SOMRES, NS_SOMRES);
      }

   }

   /**
    * Methode permettant de
    * 
    * @param reader
    * @param erreur2
    * @param isVirtual
    */
   private void ecrireFichierResultatDocumentsNonIntegres(XmlReader reader,
         boolean isVirtual, CaptureMasseErreur erreur) {
      ecrireFichierResultatDocumentsNonIntegres(reader, isVirtual, erreur, null);
   }

   /**
    * 
    * @param name
    * @throws XMLStreamException
    */
   private void endElement(final String name) {
      if ("documents".equals(name)) {
         staxUtils.addEndTag(NON_INTEGRATED_DOCUMENTS, PX_RES, NS_RES);
      }
   }

   /**
    * 
    * @param name
    * @throws XMLStreamException
    */
   private void endElementVirtuel(final String name) {
      if ("documentsVirtuels".equals(name)) {
         staxUtils.addEndTag(NON_INTEGRATED_VIRT_DOCS, PX_RES, NS_RES);
      } else if ("documentVirtuel".equals(name)) {
         staxUtils.addEndTag(NON_INTEGRATED_VIRT_DOC, PX_SOMRES,
               NS_SOMRES);
      } else if ("composants".equals(name) || "composant".equals(name)) {
         staxUtils.addEndTag(name, PX_SOMRES, NS_SOMRES);
      }

   }

   /**
    * 
    * @param name
    * @throws XMLStreamException
    */
   private void endElementIntegrated(final String name) {
      if ("documents".equals(name)) {
         staxUtils.addEndTag(INTEGRATED_DOCUMENTS, PX_RES, NS_RES);
      }

   }

   /**
    * 
    * @param name
    * @throws XMLStreamException
    */
   private void endElementVirtuelIntegrated(final String name) {
      if ("documentsVirtuels".equals(name)) {
         staxUtils.addEndTag(INTEGRATED_VIRT_DOCS, PX_RES, NS_RES);
      } else if ("documentVirtuel".equals(name)) {
         staxUtils.addEndTag(INTEGRATED_VIRT_DOC, PX_SOMRES, NS_SOMRES);
      } else if ("composants".equals(name) || "composant".equals(name)) {
         staxUtils.addEndTag(name, PX_SOMRES, NS_SOMRES);
      }

   }

   /**
    * 
    * Methode permettant de générer le corps du fichier resultat.xml.
    * 
    * @param name
    *           Nom balise lue
    * @param reader
    *           Reader de lecture du fichier reference
    * @param erreur
    *           Erreur
    * @param indexReference
    *           L'index de référence.
    * @param listIntDocs
    * @return L'index de reference ajouté au fichier resultat.xml.
    */
   private IndexReference startTagDocsNonIntegres(final String name,
         final XmlReader reader, final CaptureMasseErreur erreur,
         final IndexReference indexReference,
         ConcurrentLinkedQueue<?> listIntDocs) {

      IndexReference reference = indexReference;

      if (CHEMIN_FICHIER.equals(name)) {
         final XMLEvent xmlEvent = reader.peek();
         boolean isDocumentDansListe = false;
         int value = indexReference.getDocIndex() + 1;
         if (listIntDocs != null && !listIntDocs.isEmpty()) {
            CaptureMasseIntegratedDocument document = this
                  .getDocumentInListByXmlEvent(xmlEvent, listIntDocs,
                        indexReference);
            isDocumentDansListe = (document != null);
         }
         if (!isDocumentDansListe) {
            staxUtils
                  .addStartTag(NON_INTEGRATED_DOCUMENT, PX_SOMRES, NS_SOMRES);
            staxUtils.addStartTag(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);
            staxUtils.addStartTag(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
            gestionValeur(xmlEvent);
            staxUtils.addEndElement(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
            staxUtils.addEndElement(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);
            addErreur(erreur, indexReference.getDocIndex(), xmlEvent);
            staxUtils.addEndTag(NON_INTEGRATED_DOCUMENT, PX_SOMRES, NS_SOMRES);
         }
         reference = new IndexReference(value, indexReference.getRefIndex());
      }

      return reference;
   }

   /**
    * 
    * Methode permettant de générer le corps du fichier resultat.xml.
    * 
    * @param name
    *           Nom balise lue
    * @param reader
    *           Reader de lecture du fichier reference
    * @param erreur
    *           Erreur
    * @param indexReference
    *           L'index de référence.
    * @param listIntDocs
    * @param indexReference
    * @return L'index de reference ajouté au fichier resultat.xml.
    */
   private void startTagDocsIntegres(final String name, final XmlReader reader,
         final ConcurrentLinkedQueue<?> listIntDocs,
         IndexReference indexReference) {

      // Gestion des documents non intégrés
      if (CHEMIN_FICHIER.equals(name)) {
         final XMLEvent xmlEvent = reader.peek();
         CaptureMasseIntegratedDocument document = this
               .getDocumentInListByXmlEvent(xmlEvent, listIntDocs,
                     indexReference);
         boolean isDocumentDansListe = document != null;
         if (isDocumentDansListe) {
            staxUtils.addStartTag(INTEGRATED_DOCUMENT, PX_SOMRES, NS_SOMRES);
            staxUtils.addStartTag(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);
            staxUtils.addStartTag(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
            gestionValeur(xmlEvent);
            staxUtils.addEndElement(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
            staxUtils.addEndElement(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);

            addMetadatas(reader);
            addUUID(document);
            addNumeroPageDebut(reader);
            addNombrePages(reader);
            staxUtils.addEndTag(INTEGRATED_DOCUMENT, PX_SOMRES, NS_SOMRES);

         }
      }

   }

   /**
    * 
    * Methode permettant de générer le corps du fichier resultat.xml pour les
    * documents virtuels.
    * 
    * @param name
    *           Nom balise lue
    * @param reader
    *           Reader de lecture du fichier reference
    * @param erreur
    *           Erreur
    * @param indexReference
    *           L'index de référence.
    * @return L'index de reference ajouté au fichier resultat.xml.
    */
   private IndexReference startTagVirtuel(final String name,
         final XmlReader reader, final CaptureMasseErreur erreur,
         final IndexReference indexReference) {

      IndexReference reference = indexReference;

      if ("documentsVirtuels".equals(name)) {
         staxUtils.addStartTag(NON_INTEGRATED_VIRT_DOCS, PX_RES, PX_SOMRES);

      } else if ("documentVirtuel".equals(name)) {
         staxUtils.addStartElement(NON_INTEGRATED_VIRT_DOC, PX_SOMRES,
               NS_SOMRES);

      } else if (CHEMIN_FICHIER.equals(name)) {
         staxUtils.addStartTag(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);
         staxUtils.addStartTag(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
         final XMLEvent xmlEvent = reader.peek();
         gestionValeur(xmlEvent);
         staxUtils.addEndTag(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
         staxUtils.addEndTag(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);

         int value = indexReference.getDocIndex() + 1;
         reference = new IndexReference(value, indexReference.getRefIndex());
      } else if (NUMERO_PAGE_DEBUT.equals(name)) {
         addErreurVirtuelle(erreur, indexReference.getDocIndex());

         staxUtils.addStartTag(NUMERO_PAGE_DEBUT, PX_SOMRES, NS_SOMRES);
         XMLEvent xmlEvent = reader.peek();
         gestionValeur(xmlEvent);
         staxUtils.addEndTag(NUMERO_PAGE_DEBUT, PX_SOMRES, NS_SOMRES);

         int value = indexReference.getDocIndex() + 1;
         reference = new IndexReference(value, indexReference.getRefIndex());

      } else if (NOMBRE_PAGES.equals(name)) {
         staxUtils.addStartTag(NOMBRE_PAGES, PX_SOMRES, NS_SOMRES);
         XMLEvent xmlEvent = reader.peek();
         gestionValeur(xmlEvent);
         staxUtils.addEndTag(NOMBRE_PAGES, PX_SOMRES, NS_SOMRES);

      } else if (METADONNEES.equals(name)) {
         addMetadatas(erreur, indexReference.getDocIndex(), reader);

      } else if ("composant".equals(name)) {
         staxUtils.addStartTag(name, PX_SOMRES, NS_SOMRES);

      } else if ("composants".equals(name)) {
         addErreurReference(erreur, indexReference.getRefIndex());
         staxUtils.addStartTag(name, PX_SOMRES, NS_SOMRES);
         reference = new IndexReference(indexReference.getDocIndex(),
               indexReference.getRefIndex() + 1);
      }

      return reference;
   }

   /**
    * 
    * Methode permettant de générer le corps du fichier resultat.xml pour les
    * documents virtuels.
    * 
    * @param name
    *           Nom balise lue
    * @param reader
    *           Reader de lecture du fichier reference
    * @param erreur
    *           Erreur
    * @param indexReference
    *           L'index de référence.
    * @param listIntDocs
    * @return L'index de reference ajouté au fichier resultat.xml.
    */
   private IndexReference startTagVirtuelPartiel(final String name,
         final XmlReader reader, final CaptureMasseErreur erreur,
         final IndexReference indexReference,
         ConcurrentLinkedQueue<?> listIntDocs) {

      IndexReference reference = indexReference;

      if (CHEMIN_FICHIER.equals(name)) {
         final XMLEvent xmlEvent = reader.peek();
         CaptureMasseIntegratedDocument document = this
               .getDocumentInListByXmlEvent(xmlEvent, listIntDocs,
                     indexReference);
         boolean isDocumentIntegrated = document != null;

         if (isDocumentIntegrated) {
            staxUtils.addStartTag(NON_INTEGRATED_VIRT_DOCS, PX_RES, PX_SOMRES);
            staxUtils.addStartElement(NON_INTEGRATED_VIRT_DOC, PX_SOMRES,
                  NS_SOMRES);
         }
         staxUtils.addStartTag(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);
         staxUtils.addStartTag(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
         gestionValeur(xmlEvent);
         staxUtils.addEndTag(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
         staxUtils.addEndTag(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);

         int value = indexReference.getDocIndex() + 1;
         reference = new IndexReference(value, indexReference.getRefIndex());
      } else if (NUMERO_PAGE_DEBUT.equals(name)) {
         addErreurVirtuelle(erreur, indexReference.getDocIndex());

         staxUtils.addStartTag(NUMERO_PAGE_DEBUT, PX_SOMRES, NS_SOMRES);
         XMLEvent xmlEvent = reader.peek();
         gestionValeur(xmlEvent);
         staxUtils.addEndTag(NUMERO_PAGE_DEBUT, PX_SOMRES, NS_SOMRES);

         int value = indexReference.getDocIndex() + 1;
         reference = new IndexReference(value, indexReference.getRefIndex());

      } else if (NOMBRE_PAGES.equals(name)) {
         staxUtils.addStartTag(NOMBRE_PAGES, PX_SOMRES, NS_SOMRES);
         XMLEvent xmlEvent = reader.peek();
         gestionValeur(xmlEvent);
         staxUtils.addEndTag(NOMBRE_PAGES, PX_SOMRES, NS_SOMRES);

      } else if (METADONNEES.equals(name)) {
         addMetadatas(erreur, indexReference.getDocIndex(), reader);

      } else if ("composant".equals(name)) {
         staxUtils.addStartTag(name, PX_SOMRES, NS_SOMRES);

      } else if ("composants".equals(name)) {
         addErreurReference(erreur, indexReference.getRefIndex());
         staxUtils.addStartTag(name, PX_SOMRES, NS_SOMRES);
         reference = new IndexReference(indexReference.getDocIndex(),
               indexReference.getRefIndex() + 1);
      }

      return reference;
   }

   /**
    * @param erreur
    * @param index
    * @param reader
    * @throws XMLStreamException
    */
   private void addMetadatas(CaptureMasseErreur erreur, int index,
         XmlReader reader) {

      if (erreur.getListIndex().contains(index)) {
         staxUtils.addStartTag(METADONNEES, PX_SOMRES, NS_SOMRES);
      }
      XMLEvent xmlEvent = reader.peek();
      boolean end = xmlEvent.isEndElement()
            && METADONNEES.equals(xmlEvent.asEndElement().getName()
                  .getLocalPart());

      while (!end) {
         reader.nextEvent();

         if (erreur.getListIndex().contains(index)) {
            gestionElement(xmlEvent);
         }

         xmlEvent = reader.peek();
         end = xmlEvent.isEndElement()
               && METADONNEES.equals(xmlEvent.asEndElement().getName()
                     .getLocalPart());
      }
      if (erreur.getListIndex().contains(index)) {
         staxUtils.addEndTag(METADONNEES, PX_SOMRES, NS_SOMRES);
      }

   }

   /**
    * 
    * Methode permettant d'ajouter les balises metadonnées lus dans le fichier
    * référence.
    * 
    * @param reader
    *           Reader du fichier référence.
    */
   private void addMetadatas(XmlReader reader) {
      this.copieXmlEventReaderParNom(
            this.trouverXmlEventReaderParNom(reader, METADONNEES), METADONNEES);
   }

   /**
    * Methode permettant d'ajouter la balise nombre de pages dans le fichier
    * resultat.
    * 
    * @param reader
    *           Reader du fichier référence.
    */
   private void addNombrePages(XmlReader reader) {
      this.copieXmlEventReaderParNom(
            this.trouverXmlEventReaderParNom(reader, NOMBRE_PAGES),
            NOMBRE_PAGES);
   }

   /**
    * Methode permettant d'ajouter la balise numéro de page début dans le
    * fichier resultat.
    * 
    * @param reader
    *           Reader du fichier référence.
    */
   private void addNumeroPageDebut(XmlReader reader) {
      this.copieXmlEventReaderParNom(
            this.trouverXmlEventReaderParNom(reader, NUMERO_PAGE_DEBUT),
            NUMERO_PAGE_DEBUT);
   }

   /**
    * Methode permettant d'ajouter la balise UUID dans le fichier resultat.
    * 
    * @param document
    *           Document integré
    */
   private void addUUID(CaptureMasseIntegratedDocument document) {
      staxUtils.addStartTag(UUID, PX_SOMRES, NS_SOMRES);
      staxUtils.addValue(document.getIdentifiant().toString());
      staxUtils.addEndTag(UUID, PX_SOMRES, NS_SOMRES);
   }

   /**
    * Methode permettant de copier l'ensemble des balise contenu entre un
    * evenement defini par son nom. L'evenement est également copié.
    * 
    * @param reader
    *           {@link XmlReader}
    * @param nomEvent
    *           Nom de l'evenement
    */
   private void copieXmlEventReaderParNom(XmlReader reader, String nomXmlEvent) {
      // On copie toutes les données de entre l'evenement définit par son nom.
      XMLEvent xmlEvent = reader.peek();
      boolean end = xmlEvent.isEndElement()
            && nomXmlEvent.equals(xmlEvent.asEndElement().getName()
                  .getLocalPart());

      while (!end) {
         reader.nextEvent();

         gestionElement(xmlEvent);

         xmlEvent = reader.peek();
         end = xmlEvent.isEndElement()
               && nomXmlEvent.equals(xmlEvent.asEndElement().getName()
                     .getLocalPart());

      }

      staxUtils.addEndTag(nomXmlEvent, PX_SOMRES, NS_SOMRES);
   }

   /**
    * Methode permettant de trouver dans le reader {@link XmlReader}, l'element
    * {@link XMLEvent} défini par son nom.
    * 
    * @param reader
    *           {@link XmlReader}
    * @param nomEvent
    *           Nom de l'evenement
    * @return Le reader positionné sur l'evenement.
    */
   private XmlReader trouverXmlEventReaderParNom(XmlReader reader,
         String nomXmlEvent) {
      XMLEvent xmlEvent = reader.peek();
      // On se positionne sur la balise Metadonnées
      boolean baliseMetadonneesFind = xmlEvent.isStartElement()
            && nomXmlEvent.equals(xmlEvent.asStartElement().getName()
                  .getLocalPart());
      while (!baliseMetadonneesFind) {
         reader.nextEvent();
         if (xmlEvent.isStartElement()) {
            String name = xmlEvent.asStartElement().getName().getLocalPart();
            if (nomXmlEvent.equals(name)) {
               gestionElement(xmlEvent);
            }
         }

         xmlEvent = reader.peek();
         baliseMetadonneesFind = xmlEvent.isStartElement()
               && nomXmlEvent.equals(xmlEvent.asStartElement().getName()
                     .getLocalPart());

      }
      return reader;
   }

   /**
    * @param xmlEvent
    * @throws XMLStreamException
    */
   private void gestionElement(XMLEvent xmlEvent) {

      if (xmlEvent.isStartElement()) {
         String name = xmlEvent.asStartElement().getName().getLocalPart();
         staxUtils.addStartTag(name, PX_SOMRES, NS_SOMRES);

      } else if (xmlEvent.isEndElement()) {
         String name = xmlEvent.asEndElement().getName().getLocalPart();
         staxUtils.addEndTag(name, PX_SOMRES, NS_SOMRES);

      } else {
         String value = xmlEvent.asCharacters().getData();
         staxUtils.addValue(value);
      }

   }

   /**
    * Methode permettant de savoir si un document à été intégré dans un liste
    * passée en paramétre. Si c'est le cas, on renvoie le document trouvé et on
    * le supprime de la liste.
    * 
    * @param xmlEvent
    *           Event XML
    * @param listDocs
    *           Liste de document
    * @param indexReference
    * @return Le document qui a été trouvé dans la liste de documents.
    */
   private CaptureMasseIntegratedDocument getDocumentInListByXmlEvent(
         XMLEvent xmlEvent, ConcurrentLinkedQueue<?> listDocs,
         IndexReference indexReference) {
      CaptureMasseIntegratedDocument document = null;
      Object objFind = null;
      // Valeur de la balise chemin
      String value = xmlEvent.asCharacters().getData();
      if (value != null) {
         // On recherche si le document existe dans les documents
         for (Object obj : listDocs) {
            document = (CaptureMasseIntegratedDocument) obj;
            if (document.getIndex() == indexReference.getDocIndex()
                  && value.equals(document.getDocumentFile().getName())) {
               // Document trouvé dans la liste des documents
               objFind = obj;
               break;
            }
         }
      }
      return objFind != null ? document : null;
   }

   /**
    * 
    * Methode permettant d'ecrirer l'entete du fichier resultat.xml.
    * 
    * @param nombreDocs
    *           Nombre de documents total
    * @param nombreDocsIntegres
    *           Nombre de documents intégrés
    * @param batchModeTraitement
    *           Mode de traitement du batch
    * @param isVirtual
    *           True = traitement virtuel, false sinon
    */
   private void ecrireEntete(int nombreDocs, int nombreDocsIntegres,
         String batchModeTraitement, boolean isVirtual) {

      String countVirtual, countStandard, countIntegrated, countIntegratedVirtual, countNonIntegrated, countNonIntegratedVirtual;

      int nombreNonIntegrated = 0;
      if (Constantes.BATCH_MODE.PARTIEL.getModeNom()
            .equals(batchModeTraitement)) {
         // Dans le mode PARTIEL, on compte le nombre de document non intégré.
         // Dans le mode TOUT_OU_RIEN, le rollback permet de ne plus avoir de
         // documents intégrés normalement.
         nombreNonIntegrated = nombreDocs - nombreDocsIntegres;
      }

      if (isVirtual) {
         countStandard = "0";
         countIntegrated = "0";
         countNonIntegrated = "0";
         countVirtual = String.valueOf(nombreDocs);
         countIntegratedVirtual = String.valueOf(nombreDocsIntegres);
         countNonIntegratedVirtual = String.valueOf(nombreNonIntegrated);
      } else {
         countVirtual = "0";
         countIntegratedVirtual = "0";
         countNonIntegratedVirtual = "0";
         countStandard = String.valueOf(nombreDocs);
         countIntegrated = String.valueOf(nombreDocsIntegres);
         countNonIntegrated = String.valueOf(nombreNonIntegrated);
      }

      // entete XML
      staxUtils.startDocument();

      // debut de document
      staxUtils.addStartElement("resultats", PX_RES, NS_RES);
      staxUtils.addDefaultPrefix(NS_RES);
      staxUtils.addPrefix(PX_SOMRES, NS_SOMRES);

      // balises d'entete
      staxUtils.createTag("batchMode", batchModeTraitement, PX_RES, NS_RES);
      staxUtils.createTag("initialDocumentsCount", countStandard, PX_RES,
            NS_RES);
      staxUtils.createTag("integratedDocumentsCount", countIntegrated, PX_RES,
            NS_RES);
      staxUtils.createTag("nonIntegratedDocumentsCount", countNonIntegrated,
            PX_RES,
            NS_RES);
      staxUtils.createTag("initialVirtualDocumentsCount", countVirtual, PX_RES,
            NS_RES);
      staxUtils.createTag("integratedVirtualDocumentsCount",
            countIntegratedVirtual, PX_RES,
            NS_RES);
      staxUtils.createTag("nonIntegratedVirtualDocumentsCount",
            countNonIntegratedVirtual,
            PX_RES, NS_RES);

   }

   private void gestionValeur(XMLEvent xmlEvent) {

      if (!xmlEvent.isCharacters()) {
         throw new CaptureMasseRuntimeException(
               "Valeur vide non autorisée dans la valeur de la balise");
      }

      staxUtils.addValue(xmlEvent.asCharacters().getData());

   }

   /**
    * 
    * @param erreur
    * @param xmlEvent
    */
   private void addErreur(final CaptureMasseErreur erreur, int index,
         final XMLEvent xmlEvent) {

      staxUtils.addStartTag(ERREURS, PX_SOMRES, NS_SOMRES);

      if (erreur.getListIndex().contains(index)) {

         final String chemin = xmlEvent.asCharacters().getData();

         Integer currIndex;
         for (int i = 0; i < erreur.getListIndex().size(); i++) {
            currIndex = erreur.getListIndex().get(i);
            if (index == currIndex.intValue()) {
               staxUtils.addStartTag(ERREUR, PX_SOMRES, NS_SOMRES);

               String code = erreur.getListCodes().get(i);
               String messageErreur = erreur.getListException().get(i)
                     .getMessage();
               String message;

               if (Constantes.ERR_BUL002.equalsIgnoreCase(code)) {
                  message = "Le document " + chemin
                        + " n'a pas été archivé. Détails : " + messageErreur;
               } else if (Constantes.ERR_BUL001.equalsIgnoreCase(code)) {
                  message = "Une erreur interne à l'application est survenue lors de la capture du document "
                        + chemin + ". Détails : " + messageErreur;
               } else {
                  message = messageErreur;
               }

               staxUtils.createTag("code", code, PX_SOMRES, NS_SOMRES);
               staxUtils.createTag("libelle", message, PX_SOMRES, NS_SOMRES);
               staxUtils.addEndTag(ERREUR, PX_SOMRES, NS_SOMRES);
            }
         }
      }

      staxUtils.addEndTag(ERREURS, PX_SOMRES, NS_SOMRES);
   }

   /**
    * 
    * @param erreur
    * @throws XMLStreamException
    */
   private void addErreurVirtuelle(final CaptureMasseErreur erreur, int index) {

      staxUtils.addStartTag(ERREURS, PX_SOMRES, NS_SOMRES);

      if (erreur.getListIndex().contains(index - 1)) {

         Integer currIndex;
         for (int i = 0; i < erreur.getListIndex().size(); i++) {
            currIndex = erreur.getListIndex().get(i);
            if ((index - 1) == currIndex.intValue()) {
               staxUtils.addStartTag(ERREUR, PX_SOMRES, NS_SOMRES);

               String code = erreur.getListCodes().get(i);
               String messageErreur = erreur.getListException().get(i)
                     .getMessage();
               String message;

               if (Constantes.ERR_BUL002.equalsIgnoreCase(code)) {
                  message = "Le document virtuel n'a pas été archivé. Détails : "
                        + messageErreur;
               } else if (Constantes.ERR_BUL001.equalsIgnoreCase(code)) {
                  message = "Une erreur interne à l'application est survenue lors de la capture "
                        + "du document virtuel. Détails : " + messageErreur;
               } else {
                  message = messageErreur;
               }

               staxUtils.createTag("code", code, PX_SOMRES, NS_SOMRES);
               staxUtils.createTag("libelle", message, PX_SOMRES, NS_SOMRES);
               staxUtils.addEndTag(ERREUR, PX_SOMRES, NS_SOMRES);
            }
         }
      }

      staxUtils.addEndTag(ERREURS, PX_SOMRES, NS_SOMRES);
   }

   /**
    * 
    * @param erreur
    * @throws XMLStreamException
    */
   private void addErreurReference(final CaptureMasseErreur erreur, int index) {

      staxUtils.addStartTag(ERREURS, PX_SOMRES, NS_SOMRES);

      if (erreur.getListRefIndex().contains(index)) {

         Integer currIndex;
         for (int i = 0; i < erreur.getListRefIndex().size(); i++) {
            currIndex = erreur.getListRefIndex().get(i);
            if ((index) == currIndex.intValue()) {
               staxUtils.addStartTag(ERREUR, PX_SOMRES, NS_SOMRES);

               String code = erreur.getListCodes().get(i);
               String messageErreur = erreur.getListException().get(i)
                     .getMessage();
               String message;

               if (Constantes.ERR_BUL002.equalsIgnoreCase(code)) {
                  message = "Le document virtuel n'a pas été archivé. Détails : "
                        + messageErreur;
               } else if (Constantes.ERR_BUL001.equalsIgnoreCase(code)) {
                  message = "Une erreur interne à l'application est survenue lors de la capture "
                        + "du document virtuel. Détails : " + messageErreur;
               } else {
                  message = messageErreur;
               }

               staxUtils.createTag("code", code, PX_SOMRES, NS_SOMRES);
               staxUtils.createTag("libelle", message, PX_SOMRES, NS_SOMRES);
               staxUtils.addEndTag(ERREUR, PX_SOMRES, NS_SOMRES);
            }
         }
      }

      staxUtils.addEndTag(ERREURS, PX_SOMRES, NS_SOMRES);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeVirtualResultatsFile(File ecdeDirectory,
         File sommaireFile, CaptureMasseErreur erreur, int totalDocuments,
         final int nbDocumentsIntegres, final String batchModeTraitement,
         final ConcurrentLinkedQueue<?> listIntDocs) {

      writeResultatsFile(sommaireFile, erreur, totalDocuments,
            nbDocumentsIntegres, batchModeTraitement, listIntDocs, true);

   }

   private class IndexReference {
      private final int docIndex;
      private final int refIndex;

      /**
       * Constructeur
       * 
       * @param docIndex
       *           index du document
       * @param refIndex
       *           index de la référence
       */
      public IndexReference(int docIndex, int refIndex) {
         this.docIndex = docIndex;
         this.refIndex = refIndex;
      }

      /**
       * @return the docIndex
       */
      public final int getDocIndex() {
         return docIndex;
      }

      /**
       * @return the refIndex
       */
      public final int getRefIndex() {
         return refIndex;
      }

   }

}
