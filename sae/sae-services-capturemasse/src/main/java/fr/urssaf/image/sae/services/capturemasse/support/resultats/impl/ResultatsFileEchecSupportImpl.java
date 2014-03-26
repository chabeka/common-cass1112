/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.impl;

import java.io.File;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.CaptureMasseErreur;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.EcdePermissionException;
import fr.urssaf.image.sae.services.capturemasse.listener.EcdeConnexionConfiguration;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.capturemasse.utils.StaxUtils;
import fr.urssaf.image.sae.services.capturemasse.utils.XmlReader;

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
   private static final String NON_INTEGRATED_DOCUMENTS = "nonIntegratedDocuments";
   private static final String NON_INTEGRATED_VIRT_DOCS = "nonIntegratedVirtualDocuments";
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
         final int nombreDocsTotal) {

      writeResultatsFile(sommaireFile, erreur, nombreDocsTotal, false);

   }

   /**
    * @param ecdeDirectory
    * @param sommaireFile
    * @param erreur
    * @param nombreDocsTotal
    * @param isVirtual
    */
   private void writeResultatsFile(File sommaireFile,
         CaptureMasseErreur erreur, int nombreDocsTotal, boolean isVirtual) {

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

            ecrireFichierResultat(reader, nombreDocsTotal, erreur, isVirtual);

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
    * @param nombreDocs
    * @param isVirtual
    */
   private void ecrireFichierResultat(final XmlReader reader,
         final int nombreDocs, final CaptureMasseErreur erreur,
         boolean isVirtual) {

      ecrireEntete(nombreDocs, isVirtual);

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
               indexReference = startTag(name, reader, erreur, indexReference);
            }

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

      staxUtils.addEndTag("resultats", PX_RES, NS_RES);
   }

   /**
    * 
    * @param name
    * @throws XMLStreamException
    */
   private void endElement(final String name) {
      if ("documents".equals(name)) {
         staxUtils.addEndTag(NON_INTEGRATED_DOCUMENTS, PX_RES, NS_RES);
      } else if ("document".equals(name)) {
         staxUtils.addEndTag("nonIntegratedDocument", PX_SOMRES, NS_SOMRES);
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
         staxUtils.addEndTag("nonIntegratedVirtualDocument", PX_SOMRES,
               NS_SOMRES);
      } else if ("composants".equals(name) || "composant".equals(name)) {
         staxUtils.addEndTag(name, PX_SOMRES, NS_SOMRES);
      }

   }

   /**
    * 
    * @param name
    * @param reader
    * @param erreur
    * @param index
    * @throws XMLStreamException
    */
   private IndexReference startTag(final String name, final XmlReader reader,
         final CaptureMasseErreur erreur, final IndexReference indexReference) {

      IndexReference reference = indexReference;

      if ("documents".equals(name)) {
         staxUtils.addStartTag(NON_INTEGRATED_DOCUMENTS, PX_RES, NS_RES);

      } else if ("document".equals(name)) {
         staxUtils.addStartTag("nonIntegratedDocument", PX_SOMRES, NS_SOMRES);

      } else if (OBJET_NUMERIQUE.equals(name)) {
         staxUtils.addStartTag(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);

      } else if (CHEMIN_FICHIER.equals(name)) {
         staxUtils.addStartTag(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
         final XMLEvent xmlEvent = reader.peek();
         gestionValeur(xmlEvent);
         staxUtils.addEndElement(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
         staxUtils.addEndElement(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);
         addErreur(erreur, indexReference.getDocIndex(), xmlEvent);

         int value = indexReference.getDocIndex() + 1;
         reference = new IndexReference(value, indexReference.getRefIndex());

      }

      return reference;
   }

   /**
    * 
    * @param name
    * @param reader
    * @param erreur
    * @param index
    * @throws XMLStreamException
    */
   private IndexReference startTagVirtuel(final String name,
         final XmlReader reader, final CaptureMasseErreur erreur,
         final IndexReference indexReference) {

      IndexReference reference = indexReference;

      if ("documentsVirtuels".equals(name)) {
         staxUtils.addStartTag(NON_INTEGRATED_VIRT_DOCS, PX_RES, PX_SOMRES);

      } else if ("documentVirtuel".equals(name)) {
         staxUtils.addStartElement("nonIntegratedVirtualDocument", PX_SOMRES,
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
    * @throws XMLStreamException
    */
   private void ecrireEntete(int nombreDocs, boolean isVirtual) {

      String countVirtual, countStandard;
      if (isVirtual) {
         countStandard = "0";
         countVirtual = String.valueOf(nombreDocs);
      } else {
         countStandard = String.valueOf(nombreDocs);
         countVirtual = "0";
      }

      // entete XML
      staxUtils.startDocument();

      // debut de document
      staxUtils.addStartElement("resultats", PX_RES, NS_RES);
      staxUtils.addDefaultPrefix(NS_RES);
      staxUtils.addPrefix(PX_SOMRES, NS_SOMRES);

      // balises d'entete
      staxUtils.createTag("batchMode", "TOUT_OU_RIEN", PX_RES, NS_RES);
      staxUtils.createTag("initialDocumentsCount", countStandard, PX_RES,
            NS_RES);
      staxUtils.createTag("integratedDocumentsCount", "0", PX_RES, NS_RES);
      staxUtils.createTag("nonIntegratedDocumentsCount", countStandard, PX_RES,
            NS_RES);
      staxUtils.createTag("initialVirtualDocumentsCount", countVirtual, PX_RES,
            NS_RES);
      staxUtils.createTag("integratedVirtualDocumentsCount", "0", PX_RES,
            NS_RES);
      staxUtils.createTag("nonIntegratedVirtualDocumentsCount", countVirtual,
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
         File sommaireFile, CaptureMasseErreur erreur, int totalDocuments) {

      writeResultatsFile(sommaireFile, erreur, totalDocuments, true);

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
