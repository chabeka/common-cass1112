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

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.CaptureMasseErreur;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.util.StaxUtils;

/**
 * Implémentation du support {@link ResultatsFileEchecSupport}
 * 
 */
@Component
public class ResultatsFileEchecSupportImpl implements ResultatsFileEchecSupport {

   /**
    * 
    */
   private static final String ERREUR_FLUX = "erreur de fermeture du flux ";

   private static final String INDENTATION = "    ";

   private static final String CHEMIN_FICHIER = "cheminEtNomDuFichier";
   private static final String OBJET_NUMERIQUE = "objetNumerique";

   private static final String NS_RES = "http://www.cirtil.fr/sae/resultatsXml";
   private static final String PX_RES = "";
   private static final String NS_SOMRES = "http://www.cirtil.fr/sae/commun_sommaire_et_resultat";
   private static final String PX_SOMRES = "ns2";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileEchecSupportImpl.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeResultatsFile(final File ecdeDirectory,
         final File sommaireFile, final CaptureMasseErreur erreur,
         final int nombreDocsTotal) {

      FileInputStream sommaireStream = null;
      FileOutputStream resultatsStream = null;
      final String resultatPath = ecdeDirectory.getAbsolutePath()
            + File.separator + "resultats.xml";
      XMLEventReader reader = null;
      XMLEventWriter writer = null;

      try {
         sommaireStream = new FileInputStream(sommaireFile);
         resultatsStream = new FileOutputStream(resultatPath);
         reader = openSommaire(sommaireStream);
         writer = loadWriter(resultatsStream);

         ecrireFichierResultat(reader, writer, nombreDocsTotal, erreur);

      } catch (FileNotFoundException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (XMLStreamException e) {
         throw new CaptureMasseRuntimeException(e);

      } finally {
         if (writer != null) {
            try {
               writer.close();
            } catch (XMLStreamException e) {
               LOGGER.debug(ERREUR_FLUX + resultatPath);
            }
         }

         if (reader != null) {
            try {
               reader.close();
            } catch (XMLStreamException e) {
               LOGGER.debug(ERREUR_FLUX + sommaireFile.getAbsolutePath());
            }
         }

         if (resultatsStream != null) {
            try {
               resultatsStream.close();
            } catch (IOException e) {
               LOGGER.debug(ERREUR_FLUX + resultatPath);
            }
         }

         if (sommaireStream != null) {
            try {
               sommaireStream.close();
            } catch (IOException e) {
               LOGGER.debug(ERREUR_FLUX + sommaireFile.getAbsolutePath());
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
    * créé le writer pour le fichier résultats.xml
    * 
    * @param resultatsStream
    * @return
    */
   private XMLEventWriter loadWriter(final FileOutputStream resultatsStream) {

      final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

      try {
         final XMLEventWriter writer = outputFactory
               .createXMLEventWriter(resultatsStream);
         IndentingXMLEventWriter iWriter = new IndentingXMLEventWriter(writer);
         iWriter.setIndent(INDENTATION);
         return iWriter;

      } catch (XMLStreamException e) {
         throw new CaptureMasseRuntimeException(e);
      }
   }

   /**
    * Ecriture du fichier resultats.xml
    * 
    * @param rootSommaire
    * @param writer
    * @param erreur
    * @throws XMLStreamException
    */
   private void ecrireFichierResultat(final XMLEventReader reader,
         final XMLEventWriter writer, final int nombreDocs,
         final CaptureMasseErreur erreur) throws XMLStreamException {

      final XMLEventFactory eventFactory = XMLEventFactory.newInstance();

      StaxUtils staxUtils = new StaxUtils(eventFactory, writer);

      ecrireEntete(staxUtils, nombreDocs);

      XMLEvent xmlEvent = null;
      StartElement startElement;
      EndElement endElement;

      String name;
      int index = 0;
      while (reader.hasNext()) {

         xmlEvent = reader.nextEvent();

         if (xmlEvent.isStartElement()) {
            startElement = xmlEvent.asStartElement();
            name = startElement.getName().getLocalPart();

            index = startTag(name, staxUtils, reader, erreur, index);

         } else if (xmlEvent.isEndElement()) {
            endElement = xmlEvent.asEndElement();
            name = endElement.getName().getLocalPart();

            endElement(name, staxUtils);
         }

      }

      staxUtils.addStartTag("nonIntegratedVirtualDocuments", PX_RES, PX_SOMRES);
      staxUtils
            .addEndTag("nonIntegratedVirtualDocuments", PX_SOMRES, NS_SOMRES);
      staxUtils.addEndTag("resultats", PX_RES, NS_RES);

   }

   /**
    * 
    * @param name
    * @param staxUtils
    * @throws XMLStreamException
    */
   private void endElement(final String name, final StaxUtils staxUtils)
         throws XMLStreamException {
      if ("documents".equals(name)) {
         staxUtils.addEndTag("nonIntegratedDocuments", PX_RES, NS_RES);
      } else if ("document".equals(name)) {
         staxUtils.addEndTag("nonIntegratedDocument", PX_SOMRES, NS_SOMRES);
      }

   }

   /**
    * 
    * @param name
    * @param staxUtils
    * @param reader
    * @param erreur
    * @param index
    * @throws XMLStreamException
    */
   private int startTag(final String name, final StaxUtils staxUtils,
         final XMLEventReader reader, final CaptureMasseErreur erreur,
         final int index) throws XMLStreamException {

      int value = index;

      if ("documents".equals(name)) {
         staxUtils.addStartTag("nonIntegratedDocuments", PX_RES, NS_RES);

      } else if ("document".equals(name)) {
         staxUtils.addStartTag("nonIntegratedDocument", PX_SOMRES, NS_SOMRES);

      } else if (OBJET_NUMERIQUE.equals(name)) {
         staxUtils.addStartTag(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);

      } else if (CHEMIN_FICHIER.equals(name)) {
         staxUtils.addStartTag(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
         final XMLEvent xmlEvent = reader.peek();
         gestionValeur(xmlEvent, staxUtils);
         staxUtils.addEndElement(CHEMIN_FICHIER, PX_SOMRES, NS_SOMRES);
         staxUtils.addEndElement(OBJET_NUMERIQUE, PX_SOMRES, NS_SOMRES);
         addErreur(erreur, index, staxUtils, xmlEvent);

         value++;
      }

      return value;
   }

   /**
    * @param staxUtils
    * @throws XMLStreamException
    */
   private void ecrireEntete(StaxUtils staxUtils, int nombreDocs)
         throws XMLStreamException {

      // entete XML
      staxUtils.startDocument();

      // debut de document
      staxUtils.addStartElement("resultats", PX_RES, NS_RES);
      staxUtils.addDefaultPrefix(NS_RES);
      staxUtils.addPrefix(PX_SOMRES, NS_SOMRES);

      // balises d'entete
      staxUtils.createTag("batchMode", "TOUT_OU_RIEN", PX_RES, NS_RES);
      staxUtils.createTag("initialDocumentsCount", String.valueOf(nombreDocs),
            PX_RES, NS_RES);
      staxUtils.createTag("integratedDocumentsCount", "0", PX_RES, NS_RES);
      staxUtils.createTag("nonIntegratedDocumentsCount", String
            .valueOf(nombreDocs), PX_RES, NS_RES);
      staxUtils.createTag("initialVirtualDocumentsCount", "0", PX_RES, NS_RES);
      staxUtils.createTag("integratedVirtualDocumentsCount", "0", PX_RES,
            NS_RES);
      staxUtils.createTag("nonIntegratedVirtualDocumentsCount", "0", PX_RES,
            NS_RES);

   }

   private void gestionValeur(XMLEvent xmlEvent, StaxUtils staxUtils)
         throws XMLStreamException {

      if (!xmlEvent.isCharacters()) {
         throw new CaptureMasseRuntimeException(
               "Valeur vide non autorisée dans la valeur de la balise");
      }

      staxUtils.addValue(xmlEvent.asCharacters().getData());

   }

   /**
    * 
    * @param erreur
    * @param staxUtils
    * @param xmlEvent
    * @throws XMLStreamException
    */
   private void addErreur(final CaptureMasseErreur erreur, int index,
         final StaxUtils staxUtils, final XMLEvent xmlEvent)
         throws XMLStreamException {

      staxUtils.addStartTag("erreurs", PX_SOMRES, NS_SOMRES);

      if (erreur.getListIndex().contains(index)) {

         staxUtils.addStartTag("erreur", PX_SOMRES, NS_SOMRES);

         final String chemin = xmlEvent.asCharacters().getData();

         int indexList = erreur.getListIndex().indexOf(index);

         String code = erreur.getListCodes().get(indexList);
         String messageErreur = erreur.getListException().get(indexList)
               .getMessage();
         String message;

         if (Constantes.ERR_BUL002.equalsIgnoreCase(code)) {
            message = "Le document " + chemin
                  + " n'a pas été archivé. Détails : " + messageErreur;
         } else {
            message = "Une erreur interne à l'application est survenue lors de la capture du document "
                  + chemin + ". Détails : " + messageErreur;
         }

         staxUtils.createTag("code", code, PX_SOMRES, NS_SOMRES);
         staxUtils.createTag("libelle", message, PX_SOMRES, NS_SOMRES);
         staxUtils.addEndTag("erreur", PX_SOMRES, NS_SOMRES);
      }

      staxUtils.addEndTag("erreurs", PX_SOMRES, NS_SOMRES);
   }

}
