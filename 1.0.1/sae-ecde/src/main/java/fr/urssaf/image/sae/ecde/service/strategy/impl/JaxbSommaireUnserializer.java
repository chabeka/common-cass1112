package fr.urssaf.image.sae.ecde.service.strategy.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.ecde.exception.EcdeBadResultException;
import fr.urssaf.image.sae.ecde.exception.EcdeGeneralException;
import fr.urssaf.image.sae.ecde.exception.EcdeInvalidBatchModeException;
import fr.urssaf.image.sae.ecde.exception.EcdeInvalidDocumentException;
import fr.urssaf.image.sae.ecde.exception.EcdeRuntimeException;
import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.DocumentType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.ListeDocumentsVirtuelsType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.ListeErreurType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.ListeNonIntegratedDocumentsType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.MetadonneeType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.NonIntegratedDocumentType;
import fr.urssaf.image.sae.ecde.modele.resultats.ObjectFactory;
import fr.urssaf.image.sae.ecde.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;
import fr.urssaf.image.sae.ecde.modele.sommaire.SommaireType;
import fr.urssaf.image.sae.ecde.service.ResultatsXmlService;
import fr.urssaf.image.sae.ecde.service.SommaireXmlService;
import fr.urssaf.image.sae.ecde.service.strategy.SommaireUnserializerStrategy;
import fr.urssaf.image.sae.ecde.util.MessageRessourcesUtils;

/**
 * 
 * Classe permettant de convertir les attributs d'un fichier XML en
 * BusinessObjcet
 * 
 */
@Service
@Qualifier("sommaireUnserializerStrategy")
@SuppressWarnings({ "PMD.ExcessiveImports" })
public class JaxbSommaireUnserializer implements SommaireUnserializerStrategy {

   @Autowired
   private SommaireXmlService somXmlService;

   @Autowired
   private ResultatsXmlService resultXmlService;

   private static final Logger LOG = Logger.getLogger(JaxbSommaireUnserializer.class);

   private static final String DOCUMENTS = "documents";
   private static final String SEPARATOR_FILE = System.getProperty("file.separator");
   
   private final ObjectFactory objFactory = new ObjectFactory();

   /**
    * {@inheritDoc}
    * 
    * @throws EcdeGeneralException
    */
   @Override
   public final Sommaire unserializeSommaire(File fileXml)
         throws EcdeGeneralException {
      try {
         SommaireType sommaireType = somXmlService.readSommaireXml(fileXml);

         return mapToUntypedDoc(sommaireType, fileXml.getParent());

      } catch (EcdeXsdException except) {
         
         if ( MessageRessourcesUtils.recupererMessage("invalid.batchmode.error", null)
              .equals
              (except.getMessage())
            ) {
            
            // creation du fichier resultats avec les erreurs.
            ResultatsType resultats = affectResultatsOnError(fileXml);
            File outputFile = new File(FilenameUtils.concat(fileXml.getParent(), "resultats.xml"));
            resultXmlService.writeResultatsXml(resultats, outputFile);
            try {
               createFlag(fileXml.getParent());
            } catch (IOException e) {
               LOG.error("Impossible de creer le fichier témoin fin_traitement.flag.", e);
            }
            throw new EcdeInvalidBatchModeException(MessageRessourcesUtils.recupererMessage("invalid.batchmode.error", null));
         }
         else {
            // creation du fichier resultats avec les erreurs.
            ResultatsType resultats = affectResultatsOnError(fileXml);
            File outputFile = new File(FilenameUtils.concat(
                  fileXml.getParent(), "resultats.xml"));
            resultXmlService.writeResultatsXml(resultats, outputFile);
            try {
               createFlag(fileXml.getParent());
            } catch (IOException e) {
               LOG.error("Impossible de creer le fichier témoin fin_traitement.flag.", e);
            }
            throw new EcdeBadResultException(
                  MessageRessourcesUtils.recupererMessage("badresult.error",null), 
                  except);
         }  
      } catch (IOException except) {
         throw new EcdeBadResultException(
               "Erreur de création du fichier flag.", except);
      }

   }

   /**
    * mapping de l'objet sommaireType en untypedDocument
    * 
    * @throws EcdeBadResultException
    * @throws EcdeGeneralException
    * @throws IOException
    */
   private Sommaire mapToUntypedDoc(SommaireType sommaireType,
         String ecdeDirectory) throws EcdeGeneralException, IOException {

      // Objet Sommaire à retourner
      Sommaire sommaire = new Sommaire();
      sommaire.setBatchMode(sommaireType.getBatchMode().toString());
      sommaire.setDescription(sommaireType.getDescription());
      sommaire.setDateCreation(convertXMLGregorianCalendarToDate(sommaireType
            .getDateCreation()));
      sommaire.setEcdeDirectory(ecdeDirectory);

      
      List<UntypedDocument> listUDocSom = new ArrayList<UntypedDocument>();

      List<DocumentType> listDocsType = sommaireType.getDocuments().getDocument();
      String repDocs = ecdeDirectory.concat(SEPARATOR_FILE).concat(DOCUMENTS);
      File outputFile = new File(ecdeDirectory, "resultats.xml");
      for (DocumentType docType : listDocsType) {
         UntypedDocument untypedDoc = new UntypedDocument();
         String chemin = "";
         ResultatsType resultats = objFactory.createResultatsType();
         String chemEtNomFile = StringUtils.trim(docType.getObjetNumerique().getCheminEtNomDuFichier());
         try {
            chemin = repDocs.concat(SEPARATOR_FILE).concat(chemEtNomFile);

            // test pour vérifier si le cheminEtNomDuFichier de l'objet
            // numérique existe
            exist(untypedDoc, chemin);
            List<MetadonneeType> metaDataType = docType.getMetadonnees().getMetadonnee();
            List<UntypedMetadata> listUM = new ArrayList<UntypedMetadata>();
            for (MetadonneeType metadonneeType : metaDataType) {
               UntypedMetadata untypedMetadata = new UntypedMetadata();
               untypedMetadata.setLongCode(metadonneeType.getCode());
               untypedMetadata.setValue(metadonneeType.getValeur());
               listUM.add(untypedMetadata);
            }
            untypedDoc.setUMetadatas(listUM);
            listUDocSom.add(untypedDoc);
         } catch (Exception except) {
            ErreurType erreurType = new ErreurType();
            erreurType.setCode("SAE-EC-SOM002");
            erreurType.setLibelle(MessageRessourcesUtils.recupererMessage("ecde.invaliddoc.error", chemEtNomFile)
                                                        .concat("\n").concat(" Détails : ").concat("\n")
                                                        .concat(except.getMessage()));
            resultats = affectResultatsInvalidDoc(sommaireType, chemin,erreurType, ecdeDirectory);
            resultXmlService.writeResultatsXml(resultats, outputFile);
            // creation du fichier drapeau.
            createFlag(ecdeDirectory);
            throw new EcdeInvalidDocumentException(MessageRessourcesUtils
                  .recupererMessage("ecde.invaliddoc.error", chemin)
                  .concat("Détails : ").concat(except.getMessage()),
                  except);
         }
      }
      sommaire.setDocuments(listUDocSom);
      return sommaire;
   }

   /**
    * Verification de l'existance d'un fichier
    * 
    * @param chemin
    * @return
    * @throws IOException
    * @throws FileNotFoundException
    */
   private File exists(String chemin) throws IOException {
      File fichier = new File(chemin);
      // verification si objetNumerique du sommaire represente un document
      if (!fichier.isFile()) {
         throw new IOException(MessageRessourcesUtils.recupererMessage(
               "objetnum.notexist.error", chemin));
      }
      return fichier;
   }

   /**
    * Verification de l'existance d'un fichier
    * 
    * @throws IOException
    * 
    */
   private void exist(UntypedDocument untypedDoc, String chemin)
         throws IOException {

      File fichier = new File(chemin);

      // verification si objetNumerique du sommaire represente un document
      // si oui alors remplissement du untypedDocument
      if (fichier.isFile()) {
         untypedDoc.setFilePath(chemin);
         untypedDoc
               .setContent(FileUtils.readFileToByteArray(exists(chemin)));
      }// sinon rejeté une exception
      else {
         throw new IOException(MessageRessourcesUtils.recupererMessage(
               "objetnum.notexist.error", chemin));
      }
   }

   /**
    * Affectation resultatsOnError
    * 
    * @param sommaireType
    *            representant le fichier sommaire.xml
    * @param sommaireType
    *            sommaire.xml
    * 
    * @return ResultatsType correspondant au resultats.xml
    */
   private ResultatsType affectResultatsOnError(File fileXml) {

      ErreurType erreurType = new ErreurType();
      erreurType.setCode("SAE-EC-SOM001");
      erreurType.setLibelle(MessageRessourcesUtils.recupererMessage("som.invalid.error", null)
                            .concat(" Détails : ")
                            .concat(MessageRessourcesUtils.recupererMessage("nodoc.insert.ecde", null)));
      
      ResultatsType resultats = objFactory.createResultatsType();

      resultats.setErreurBloquanteTraitement(erreurType);

      return resultats;

   }

   /**
    * Affectation resultatsInvalidDoc
    * 
    * @param sommaireType
    *            representant le fichier sommaire.xml
    * @param sommaireType
    *            sommaire.xml
    * 
    * @return ResultatsType correspondant au resultats.xml
    * 
    */
   private ResultatsType affectResultatsInvalidDoc(SommaireType sommaireType,
         String chemin, ErreurType erreurType, String ecdeDirectory) {

      ResultatsType resultats = objFactory.createResultatsType();

      // ResultatsType resultats = new ResultatsType();
      resultats.setBatchMode(sommaireType.getBatchMode());
      resultats.setInitialDocumentsCount(sommaireType.getDocuments().getDocument().size());
      resultats.setInitialVirtualDocumentsCount(sommaireType.getDocumentsVirtuels().getDocumentVirtuel().size());
      resultats.setIntegratedDocumentsCount(0);
      resultats.setIntegratedVirtualDocumentsCount(0);

      ListeNonIntegratedDocumentsType nonDocsIntegrated = new ListeNonIntegratedDocumentsType();
      // etant donné erreur alors recup tous les documents du sommaire.xml et
      // les integrer à
      // la liste nonIntegratedDocument.
      List<DocumentType> docsType = sommaireType.getDocuments().getDocument();
      ListeErreurType erreursType = new ListeErreurType();
      erreursType.getErreur().add(erreurType);

      ListeErreurType listeErreurType = new ListeErreurType();
      for (DocumentType documentType : docsType) {
         NonIntegratedDocumentType nonInteg = new NonIntegratedDocumentType();
         nonInteg.setObjetNumerique(documentType.getObjetNumerique());
         nonInteg.setNombreDePages(documentType.getNombreDePages());
         nonInteg.setNumeroPageDebut(documentType.getNumeroPageDebut());
         // si le doc en question est celui en erreur alors ajout
         if (chemin.equals(ecdeDirectory
               .concat(SEPARATOR_FILE)
               .concat(DOCUMENTS)
               .concat(SEPARATOR_FILE)
               .concat(documentType.getObjetNumerique().getCheminEtNomDuFichier()))) {
            nonInteg.setErreurs(erreursType);
         } else {
            nonInteg.setErreurs(listeErreurType);
         }
         nonDocsIntegrated.getNonIntegratedDocument().add(nonInteg);
      }
      resultats.setNonIntegratedDocuments(nonDocsIntegrated);
      resultats.setNonIntegratedDocumentsCount(nonDocsIntegrated
            .getNonIntegratedDocument().size());
      resultats
            .setNonIntegratedVirtualDocuments(new ListeDocumentsVirtuelsType());
      resultats.setNonIntegratedVirtualDocumentsCount(0);
      

      return resultats;
   }

   /*
    * Permet la convertion d'un XML Gregorian Calendar en java.util.Date
    */
   private Date convertXMLGregorianCalendarToDate(XMLGregorianCalendar xgc) {
      Date date = null;
      if (xgc != null) {
         date = xgc.toGregorianCalendar().getTime();
      }
      return date;
   }

   /**
    * Création du fichier flag pour signaler la fin du traitement
    * 
    * @throws IOException
    */
   private void createFlag(String ecdeDirectory) throws IOException {
      try {
         File flag = new File(FilenameUtils.concat(ecdeDirectory,"fin_traitement.flag"));
         if (flag.exists()) {
            boolean delete = flag.delete();
            if (!delete) {
               throw new EcdeRuntimeException("Erreur lors de la suppression du fichier flag!");
            }
         }
         boolean create = flag.createNewFile();
         if (!create) {
            throw new EcdeRuntimeException("Erreur lors de l'ecriture du fichier flag!");
         }
      } catch (IOException except) {
         throw new IOException("Erreur de création du fichier flag.", except);
      }
   }

   /**
    * 
    * @return le service SommaireXml
    */
   public final SommaireXmlService getSommaireXmlService() {
      return somXmlService;
   }

   /**
    * 
    * @param somXmlService
    *            set le service SommaireXml
    */
   public final void setSommaireXmlService(SommaireXmlService somXmlService) {
      this.somXmlService = somXmlService;
   }

   /**
    * 
    * @return le resultatXmlService
    */
   public final ResultatsXmlService getResultXmlService() {
      return resultXmlService;
   }

   /**
    * 
    * @param resultXmlService
    *            set le service ResultatsXml
    */
   public final void setResultXmlService(ResultatsXmlService resultXmlService) {
      this.resultXmlService = resultXmlService;
   }
}
