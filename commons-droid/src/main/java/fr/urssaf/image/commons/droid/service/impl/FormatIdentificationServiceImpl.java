package fr.urssaf.image.commons.droid.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import uk.gov.nationalarchives.droid.command.container.Ole2ContainerContentIdentifier;
import uk.gov.nationalarchives.droid.command.container.ZipContainerContentIdentifier;
import uk.gov.nationalarchives.droid.container.ContainerFileIdentificationRequestFactory;
import uk.gov.nationalarchives.droid.container.ContainerSignatureDefinitions;
import uk.gov.nationalarchives.droid.container.ContainerSignatureSaxParser;
import uk.gov.nationalarchives.droid.container.TriggerPuid;
import uk.gov.nationalarchives.droid.container.ole2.Ole2IdentifierEngine;
import uk.gov.nationalarchives.droid.container.zip.ZipIdentifierEngine;
import uk.gov.nationalarchives.droid.core.BinarySignatureIdentifier;
import uk.gov.nationalarchives.droid.core.SignatureParseException;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationResult;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationResultCollection;
import uk.gov.nationalarchives.droid.core.interfaces.RequestIdentifier;
import uk.gov.nationalarchives.droid.core.interfaces.archive.IdentificationRequestFactory;
import uk.gov.nationalarchives.droid.core.interfaces.resource.FileSystemIdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.resource.RequestMetaData;
import fr.urssaf.image.commons.droid.exception.FormatIdentificationRuntimeException;

// TODO : Faire une interface une fois les méthodes publiques bien définies
public class FormatIdentificationServiceImpl {

   private static Logger LOGGER = LoggerFactory.getLogger(FormatIdentificationServiceImpl.class);
   
   private BinarySignatureIdentifier binarySignatureIdentifier;
   
   private ContainerSignatureDefinitions containerSignatureDefinitions;
   private List<TriggerPuid> triggerPuids;
   
   private final String OLE2_CONTAINER = "OLE2";
   private final String ZIP_CONTAINER = "ZIP";
   private final String ZIP_ARCHIVE = "x-fmt/263";
   private final String JIP_ARCHIVE = "x-fmt/412";
   private final String TAR_ARCHIVE = "x-fmt/265";
   private final String GZIP_ARCHIVE = "x-fmt/266";
      
   public FormatIdentificationServiceImpl() {
      
      binarySignatureIdentifier = chargeFichierSignaturePronom();
      containerSignatureDefinitions = chargeFichierSignatureContainerPronom();
      
      triggerPuids = containerSignatureDefinitions.getTiggerPuids();
      
   }
   
   
   private BinarySignatureIdentifier chargeFichierSignaturePronom() {
      
      // TODO : Fichier en paramètre du constructeur
      // TODO : Chemin complet du fichier à revoir pour fonctionnement en dépendance Maven
      String fileSignaturesFileName = getRessourceFilePath("/DROID_SignatureFile_V65.xml");
      Long maxBytesToScan=new Long(65536);
      
      BinarySignatureIdentifier binarySignatureIdentifier = new BinarySignatureIdentifier();
      
      binarySignatureIdentifier.setSignatureFile(fileSignaturesFileName);
      
      try {
         binarySignatureIdentifier.init();
      } catch (SignatureParseException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      }
      
      binarySignatureIdentifier.setMaxBytesToScan(maxBytesToScan);
      
      return binarySignatureIdentifier;
      
   }
   
   
   private ContainerSignatureDefinitions chargeFichierSignatureContainerPronom() {
    
      // TODO : Fichier en paramètre du constructeur
      // TODO : Chemin complet du fichier à revoir pour fonctionnement en dépendance Maven
      String containerSignaturesFileName = getRessourceFilePath("/container-signature.xml");
      
      ContainerSignatureDefinitions containerSignatureDefinitions = null;
      
      InputStream in = null;
      try {
         
         in = new FileInputStream(containerSignaturesFileName);
         
         ContainerSignatureSaxParser parser = new ContainerSignatureSaxParser();
         
         containerSignatureDefinitions = parser.parse(in);
         
      } catch (FileNotFoundException e) {
         throw new FormatIdentificationRuntimeException(e); 
      } catch (JAXBException e) {
         throw new FormatIdentificationRuntimeException(e);
      } catch (SignatureParseException e) {
         throw new FormatIdentificationRuntimeException(e);
      } finally {
         if (in!=null) {
            try {
               in.close();
            } catch (IOException e) {
               throw new FormatIdentificationRuntimeException(e);
            }
         }
      }
      
      return containerSignatureDefinitions;
      
   }
   
   
   public String identifie(File file, boolean analyserContenuArchives) {
      
      // TODO : Voir si on peut travailler sur un InputStream au lieu d'un File
      
      // Préparation des données nécessaires au moteur d'identification
      
      String fileName;
      try {
         fileName = file.getCanonicalPath();
      } catch (IOException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      }
      
      LOGGER.debug("{}, Identification du format par Droid : Préparation", fileName);
      
      RequestMetaData metaData =
         new RequestMetaData(file.length(), file.lastModified(), fileName);
      
      RequestIdentifier identifier = new RequestIdentifier(file.toURI());
      identifier.setParentId(1L);
      
      IdentificationRequest request = new FileSystemIdentificationRequest(metaData, identifier);
      
      InputStream in;
      try {
         in = new FileInputStream(file);
      } catch (FileNotFoundException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      }
      try {
         request.open(in);
      } catch (IOException ex) {
         throw new FormatIdentificationRuntimeException(ex);
      }
      
      // L'identification du format d'un fichier avec Droid se fait en 3 étapes
      // 1) A l'aide des signatures binaires
      // 2) Si le format identifié en 1) est un format conteneur, on descend en 
      //    profondeur en rentrant dans le conteneur
      // 3) Extension ???
      
      return identifieAvecSignature(request, analyserContenuArchives);
      
   }
   
   
   
   private String identifieAvecSignature(
         IdentificationRequest request, 
         boolean analyserContenuArchives) {
      
      // Lance l'analyse par les signatures binaires
      IdentificationResultCollection results = binarySignatureIdentifier.matchBinarySignatures(request);
      
      // Analyse des résultats
      String idPronom;
      if ((results==null) || (CollectionUtils.isEmpty(results.getResults()))) {
         
         // L'étape d'identification par le fichier de signature binaire n'a
         // rien donné. On arrête là.
         
         // Trace
         LOGGER.debug("{}, Identification du format par Droid : Identification avec les signatures binaires : Aucun résultat", request.getFileName());
         
         // Résultat de l'analyse
         idPronom = null;
         
      } else {
         
         // L'étape d'identification par le fichier de signature binaire a abouti.
         // Il faut peut-être creuser si on a identifié un format conteneur.
         
         // Trace
         List<String> listeIds = new ArrayList<String>();
         for(IdentificationResult result: results.getResults()) {
            listeIds.add(result.getPuid());
         }
         LOGGER.debug("{}, Identification du format par Droid : Identification avec les signatures binaires : Format(s) identifié(s) : {}", request.getFileName(), listeIds.toString());
         
         // On poursuit l'analyse
         idPronom = identifieConteneur(request, results, analyserContenuArchives);
         
      }
      
      // Renvoie du résultat
      return idPronom;
      
   }
   
   
   private String identifieConteneur(
         IdentificationRequest request,
         IdentificationResultCollection results,
         boolean analyserContenuArchives) {
      
      // Le résultat de la méthode
      String idPronom;
      
      // Approfondit l'analyse en cherchant dans les container
      IdentificationResultCollection containerResults = getContainerResults(results, request);
      
      // Trace
      if (CollectionUtils.isEmpty(containerResults.getResults())) {
         LOGGER.debug("{}, Identification du format par Droid : Identification dans un conteneur : Aucun résultat", request.getFileName());
      } else {
         List<String> listeIds = new ArrayList<String>();
         for(IdentificationResult result: containerResults.getResults()) {
            listeIds.add(result.getPuid());
         }
         LOGGER.debug("{}, Identification du format par Droid : Identification dans un conteneur : Format(s) identifié(s) : {}", request.getFileName(), listeIds.toString());
      }
      
      // Choix des résultats finaux, selon si on a trouvé quelque chose dans le conteneur
      IdentificationResultCollection finalResults = new IdentificationResultCollection(request);
      boolean container;
      if (CollectionUtils.isEmpty(containerResults.getResults())) {
         // Aucun résultat dans un conteneur
         // On choisit comme résultats finaux ceux hors conteneur
         container = false;
         finalResults = results;
      } else {
         // On a obtenu des résultats dans le conteneur
         // On choisit comme résultats finaux ceux du conteneur
         container = true;
         finalResults = containerResults;
      }
      
      // Si au moins 1 format trouvé
      if (!CollectionUtils.isEmpty(finalResults.getResults())) {
         
         // On ne conserve que le format le plus fin
         binarySignatureIdentifier.removeLowerPriorityHits(finalResults);
         
         // Récupération du format identifié
         // On ne traite que le 1er de la liste
         IdentificationResult result = finalResults.getResults().get(0);
         String puid = result.getPuid();
         
         // Bricolage
         if (!container && JIP_ARCHIVE.equals(puid)) {
             puid = ZIP_ARCHIVE;
         }
         
         // Selon si on doit analyser le contenu d'une archive
         if (analyserContenuArchives && !container) {
            
            if (GZIP_ARCHIVE.equals(puid)) {
            
               // Trace
               LOGGER.debug("{}, Identification du format par Droid : Identification dans une archive : Le format est une archive", request.getFileName());
               
               // TODO : Traiter le cas des archives GZip
               // throw new FormatIdentificationRuntimeException("L'analyse des fichies gzip n'est pas implémentée");
               idPronom = puid;
               
            } else if (TAR_ARCHIVE.equals(puid)) {
            
               // Trace
               LOGGER.debug("{}, Identification du format par Droid : Identification dans une archive : Le format est une archive", request.getFileName());
               
               // TODO : Traiter le cas des archives Tar
               // throw new FormatIdentificationRuntimeException("L'analyse des fichies tar n'est pas implémentée");
               idPronom = puid;
               
            } else if (ZIP_ARCHIVE.equals(puid) || JIP_ARCHIVE.equals(puid)) {
               
               // Trace
               LOGGER.debug("{}, Identification du format par Droid : Identification dans une archive : Le format est une archive", request.getFileName());
               
               // TODO : Traiter le cas des archives Zip
               // throw new FormatIdentificationRuntimeException("L'analyse des fichies zip n'est pas implémentée");
               idPronom = puid;
               
            } else {
               
               // Le format identifié n'est pas un format d'archives
               
               // Trace
               LOGGER.debug("{}, Identification du format par Droid : Identification dans une archive : Le format n'est pas une archive", request.getFileName());
               
               // On prend l'identifiant Pronom déterminé plus haut
               idPronom = puid;
            }
            
         } else {
            
            idPronom = puid;
            
         }
         
      } else {
         
         // Aucun format identifié
         idPronom = null;
         
      }
         
      // Renvoie le résultat
      return idPronom;
      
   }
   
   
   
   
   
   
   
   private IdentificationResultCollection getContainerResults(
         final IdentificationResultCollection results,
         final IdentificationRequest request) {
     
         IdentificationResultCollection containerResults =
                 new IdentificationResultCollection(request);
                 
         if (results.getResults().size() > 0 && containerSignatureDefinitions != null) {
             for (IdentificationResult identResult : results.getResults()) {
                 String filePuid = identResult.getPuid();
                 if (filePuid != null) {
                     TriggerPuid containerPuid = getTriggerPuidByPuid(filePuid);
                     if (containerPuid != null) {
                        IdentificationRequestFactory requestFactory = new ContainerFileIdentificationRequestFactory();
                         String containerType = containerPuid.getContainerType();

                         if (OLE2_CONTAINER.equals(containerType)) {
                             try {
                                 Ole2ContainerContentIdentifier ole2Identifier =
                                         new Ole2ContainerContentIdentifier();
                                 ole2Identifier.init(containerSignatureDefinitions, containerType);
                                 Ole2IdentifierEngine ole2IdentifierEngine = new Ole2IdentifierEngine();
                                 ole2IdentifierEngine.setRequestFactory(requestFactory);
                                 ole2Identifier.setIdentifierEngine(ole2IdentifierEngine);
                                 containerResults = ole2Identifier.process(
                                     request.getSourceInputStream(), containerResults);
                             } catch (IOException ex) {   // carry on after container i/o problems
                                 throw new FormatIdentificationRuntimeException(ex);
                             }
                         } else if (ZIP_CONTAINER.equals(containerType)) {
                             try {
                                 ZipContainerContentIdentifier zipIdentifier =
                                             new ZipContainerContentIdentifier();
                                 zipIdentifier.init(containerSignatureDefinitions, containerType);
                                 ZipIdentifierEngine zipIdentifierEngine = new ZipIdentifierEngine();
                                 zipIdentifierEngine.setRequestFactory(requestFactory);
                                 zipIdentifier.setIdentifierEngine(zipIdentifierEngine);
                                 containerResults = zipIdentifier.process(
                                     request.getSourceInputStream(), containerResults);
                             } catch (IOException ex) {   // carry on after container i/o problems
                                throw new FormatIdentificationRuntimeException(ex);
                             }
                         } else {
                             throw new FormatIdentificationRuntimeException("Unknown container type: " + containerPuid);
                         }
                     }
                 }
             }
         }
         return containerResults;
     }
   
   
   private TriggerPuid getTriggerPuidByPuid(final String puid) {
      for (final TriggerPuid tp : triggerPuids) {
          if (tp.getPuid().equals(puid)) {
              return tp;
          }
      }
      return null;
  }
   
   private String getRessourceFilePath(String cheminRessource) {
      
      try {
         return this.getClass().getResource(cheminRessource).toURI().getPath();
      } catch (URISyntaxException e) {
         throw new RuntimeException(e);
      }
      
   }
   
}
