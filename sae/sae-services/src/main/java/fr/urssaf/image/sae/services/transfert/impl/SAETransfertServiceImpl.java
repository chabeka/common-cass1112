package fr.urssaf.image.sae.services.transfert.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageTransfertService;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.dao.support.ServiceProviderSupport;
import fr.urssaf.image.sae.trace.model.DfceTraceDoc;
import fr.urssaf.image.sae.trace.service.CycleVieService;
import fr.urssaf.image.sae.trace.service.impl.JournalEvtServiceImpl;

/**
 * Classe implémentant l'interface {@link SAETransfertService}. Cette classe
 * est un singleton et peut être accessible par le système d'injection IOC avec
 * l'annotation @Autowired
 * 
 */
@Service
public class SAETransfertServiceImpl extends AbstractSAEServices implements SAETransfertService{

   private static final Logger LOG = LoggerFactory
      .getLogger(SAETransfertServiceImpl.class);
   
   @Autowired
   private StorageDocumentService storageDocumentService;
   
   /**
    * Provider de service pour la connexion DFCE de la GNS
    */
   @Autowired
   private StorageTransfertService storageTransfertService;
   
   /**
    * Provider de service pour la connexion DFCE de la GNT
    */
   @Autowired
   private StorageServiceProvider storageServiceProvider;

   /**
    * Objet de manipulation des métadonnées du reférentiel des meta
    */
   @Autowired
   private MetadataReferenceDAO metadataReferenceDAO;
   
   /**
    * Liste des évents du cycle de vie des docs par id du doc
    */
   @Autowired
   private CycleVieService cycleVieService;
   
   @Autowired
   private ServiceProviderSupport traceServiceSupport;
   
   /**
    * Liste des events du SAE par id du doc
    */
   @Autowired
   private JournalEvtServiceImpl journalEvtService; 
   
   public void transfertDoc(UUID idArchive) throws TransfertException,
      ArchiveAlreadyTransferedException, ArchiveInexistanteEx {

      //-- On trace le début du transfert
      String trcPrefix = "transfertDoc";
      LOG.debug("{} - début", trcPrefix);
      LOG.debug("{} - Début de transfert du document {}", new Object[] {
            trcPrefix, idArchive.toString() });
     
      try {
         //-- Ouverture des connections DFCE
         storageServiceProvider.openConnexion();
         storageTransfertService.openConnexion();
         traceServiceSupport.connect();
         
         LOG.debug("{} - recherche du document", trcPrefix);

         List<StorageMetadata> desiredMetas = getTransferableStorageMeta();
         UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);

         StorageDocument document = storageDocumentService
            .searchStorageDocumentByUUIDCriteria(uuidCriteria);

         //-- Le document n'existe pas sur la GNT
         if (document == null) {
            //-- On recherche le document sur la GNS
            document = storageTransfertService
               .searchStorageDocumentByUUIDCriteria(uuidCriteria);
            
            String uuid = idArchive.toString();

            //-- Le document n'existe pas sur la GNS (et sur la GNT)
            if (document == null) {
               String message = "Le document {0} n'existe pas. Transfert impossible.";
               throw new ArchiveInexistanteEx(StringUtils.replace(message,"{0}", uuid));
            } else {
               //-- Le document existe sur la GNS
               String message = "Le document {0} a déjà été transféré.";
               throw new ArchiveAlreadyTransferedException(StringUtils.replace(message,"{0}", uuid));
            }
         } else {
            
            String mssgErreur = "Une erreur s'est produite lors du transfert. " +
                                    "Transfert impossible.";
            
            //-- On recherche le document sur la GNS
            StorageDocument documentGNS = storageTransfertService
               .searchStorageDocumentByUUIDCriteria(uuidCriteria);
            
            //-- On s'assure que le document qu'on va transférer n'existe pas sur la GNS
            if(documentGNS == null){

               //-- Modification des métadonnées du document pour le transfert
               updateMetaDocumentForTransfert(document);
               
               //-- Archivage du document
               try {
                  documentGNS = storageTransfertService.insertBinaryStorageDocument(document);
               } catch (InsertionServiceEx ex) {
                  throw new TransfertException(mssgErreur, ex);
               }
            }
            
            //-- Suppression du document transféré de la GNT
            try {
               storageDocumentService.deleteStorageDocumentTraceTransfert(idArchive);
            } catch (DeletionServiceEx erreurSupprGNT) {
               StorageDocument documentGNT = storageDocumentService
                  .searchStorageDocumentByUUIDCriteria(uuidCriteria);
               if (documentGNT != null) {
                  //-- Le document existe toujours dans la GNT
                  try {
                     storageTransfertService.deleteStorageDocument(idArchive);
                  } catch (DeletionServiceEx erreurSupprGNS) {
                     throw new TransfertException(erreurSupprGNS);
                  }
               }
               throw new TransfertException(mssgErreur, erreurSupprGNT);
            }
            
            //-- Fermeture des connections DFCE
            storageServiceProvider.closeConnexion();
            storageTransfertService.closeConnexion();
            traceServiceSupport.disconnect();
            
            LOG.debug("{} - Fin de transfert du document {}", new Object[] {
                  trcPrefix, idArchive.toString() });
         }
      } catch (ConnectionServiceEx ex) {
         throw new TransfertException(ex);
      } catch (SearchingServiceEx ex) {
         throw new TransfertException(ex);
      } catch (ReferentialException ex) {
         throw new TransfertException(ex);
      }
   }
   
   /**
    * Récupération de la liste des métadonnées transférables,
    * depuis le référentiel des métadonnées.
    * 
    * @return Liste des métadonnées
    * @throws ReferentialException 
    * 
    */
   private List<StorageMetadata> getTransferableStorageMeta() throws ReferentialException{
      //-- Récupération de la liste des méta transférables du référentiel
      Map<String, MetadataReference> transferables = metadataReferenceDAO.getTransferableMetadataReference();
      
      Set<Entry<String, MetadataReference>> data = transferables.entrySet();
      List<StorageMetadata> metas = new ArrayList<StorageMetadata>();
      
      for (Map.Entry<String, MetadataReference> entry : data){
         metas.add(new StorageMetadata(entry.getValue().getShortCode()));
      }
      
      //-- La métadonnée DateArchivage ne fait pas partie des métadonnées transférable, mais nous en avons besoin pour
      // calculer la date d'archivage en GNT
      // on l'ajoute donc a la liste
      metas.add(new StorageMetadata(StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode()));
      
      return metas;
   }
   
   /**
    * Mise à jour des métadonnées du documents à transférer :
    * <li>DateArchivageGNT</li>
    * <li>Supp. meta non transférables</li>
    * <li>TracePreArchivage</li>
    * 
    * @param document Document à transférer
    * @throws ReferentialException En cas d'erreur de récupérarion de la liste des méta transférables
    * @throws TransfertException En cas d'erreur de création des traces json
    */
   private void updateMetaDocumentForTransfert(StorageDocument document) throws ReferentialException, TransfertException {

      //-- Ajout métadonnée "DateArchivageGNT"
      Object dateArchivage = StorageMetadataUtils.valueObjectMetadataFinder(document.getMetadatas(), StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode());
      StorageMetadata dateArchivageGNT = new StorageMetadata(StorageTechnicalMetadatas.DATE_ARCHIVE_GNT.getShortCode(), dateArchivage);
      document.getMetadatas().add(dateArchivageGNT);
      
      //-- Suppression des métadonnées vides (impératif api dfce)
      // Supprime aussi la métadonnée DateArchivage qui est non transférable
      List<StorageMetadata> metadata = document.getMetadatas();
      for (int i =0; i<metadata.size(); i++) {
         if(metadata.get(i).getValue() == null || metadata.get(i).getValue().equals("") 
               || metadata.get(i).getShortCode().equals(StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode())) {
            metadata.remove(i);
            i--;
         }
      }
      
      //-- Ajout tarces (preachivage) au document
      String traces = getTracePreArchivageAsJson(document);
      String shortCode = StorageTechnicalMetadatas.TRACABILITE_PRE_ARCHIVAGE.getShortCode();
      document.getMetadatas().add(new StorageMetadata(shortCode, traces));
   }
   
   private String getTracePreArchivageAsJson(StorageDocument document) throws TransfertException{
      Map<String, String> mapTraces = new HashMap<String, String>();
      
      //-- Get json de la liste des events du cycle de vie DFCE
      String eventsDfce = getCycleVieDfceEventsAsJson(document.getUuid());
      mapTraces.put("traceDfce", eventsDfce);
      
      //-- Get json de la liste des events du jounal des events SAE
      String eventsSae = getJournalSaeEventsAsJson(document.getUuid());
      mapTraces.put("traceSae", eventsSae);
      
      //-- Get json de la traçabilité pré-archivage si présente
      for(int i=0; i<document.getMetadatas().size(); i++){
         StorageMetadata meta = document.getMetadatas().get(i);
         String codeCourt = StorageTechnicalMetadatas.TRACABILITE_PRE_ARCHIVAGE.getShortCode();
         if(meta.getShortCode() == codeCourt){
            mapTraces.put("tracePreArchivage", meta.getValue().toString());
            break;
         }
      }
      
      try {
         return new ObjectMapper().writeValueAsString(mapTraces);
      } catch (JsonGenerationException e) {
         throw new TransfertException(e);
      } catch (JsonMappingException e) {
         throw new TransfertException(e);
      } catch (IOException e) {
         throw new TransfertException(e);
      }
   }
   
   /**
    * Méthode conversion en json de la liste des évènements du cycle de vie DFCE
    * par identifiant de document. La liste est renvoyée au format Json.
    * 
    * @param idArchive
    * @return
    * @throws TransfertException
    */
   private String getCycleVieDfceEventsAsJson(final UUID idArchive) throws TransfertException {
      List<DfceTraceDoc> evtCycleVie = cycleVieService.lectureParDocument(idArchive);
      ObjectMapper mapper = new ObjectMapper();
      try {
         return mapper.writeValueAsString(evtCycleVie);
      } catch (JsonGenerationException e) {
         throw new TransfertException(e);
      } catch (JsonMappingException e) {
         throw new TransfertException(e);
      } catch (IOException e) {
         throw new TransfertException(e);
      }
   }
   
   /**
    * Récupératrion de la liste des évènements du jounal SAE des évents
    * par identifiant de document. La liste est renvoyée au format Json.
    * 
    * @param idArchive
    * @return
    * @throws TransfertException
    */
   private String getJournalSaeEventsAsJson(final UUID idArchive) throws TransfertException {
      List<TraceJournalEvtIndexDoc>  evtSae = 
         journalEvtService.getTraceJournalEvtByIdDoc(idArchive);
      ObjectMapper mapper = new ObjectMapper();
      try {
         return mapper.writeValueAsString(evtSae);
      } catch (JsonGenerationException e) {
         throw new TransfertException(e);
      } catch (JsonMappingException e) {
         throw new TransfertException(e);
      } catch (IOException e) {
         throw new TransfertException(e);
      }
   }
}
