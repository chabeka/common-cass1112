package fr.urssaf.image.sae.documents.executable.support;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.documents.executable.utils.ConstantesTracabilite;
import fr.urssaf.image.sae.documents.executable.utils.HostnameUtil;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;

/**
 * Classe de support pour écrire les traces via la brique de traçabilité.
 */
@Component
public class TracesDfceSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TracesDfceSupport.class);

   private DispatcheurService dispatcheurService;
   
   /**
    * Informations pour l'authentification utilisée dans les traces
    */
   public static final String CS = "SAE";
   public static final String LOGIN = "DOCUMENTS EXECUTABLE";

   /**
    * Constructeur
    * 
    * @param dispatcheurService
    *           Service du dispatcheur de la trace
    */
   @Autowired
   public TracesDfceSupport(DispatcheurService dispatcheurService) {
      this.dispatcheurService = dispatcheurService;
   }

   /**
    * Trace l'événement "Dépôt d'un document dans DFCE"
    * 
    * @param idDoc
    *           l'identifiant unique DFCE du document archivé
    * @param hash
    *           le hash du document
    * @param typeHash
    *           le type de hash ayant servi à calculer le hash fourni en
    *           paramètre
    * @param dateArchivageDfce
    *           la date d'archivage DFCE
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final void traceDepotDocumentDansDFCE(UUID idDoc, String hash,
         String typeHash, Date dateArchivageDfce) {

      // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
      // planter cette méthode
      try {

         // Traces
         String prefix = "traceDepotDocumentDansDFCE()";
         LOGGER.debug("{} - Début", prefix);

         // Instantiation de l'objet TraceToCreate
         TraceToCreate traceToCreate = new TraceToCreate();

         // Code de l'événement
         traceToCreate.setCodeEvt(ConstantesTracabilite.TRACE_CODE_EVT_DEPOT_DOC_DFCE);

         // Contexte
         traceToCreate.setContexte("DepotDocumentDansDFCE");

         // Contrat de service et login
         setInfosAuth(traceToCreate);

         // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
         // ce code
         traceToCreate.getInfos().put("saeServeurHostname",
               HostnameUtil.getHostname());
         traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

         // Info supplémentaire : identifiant d'archivage
         traceToCreate.getInfos().put("idDoc", idDoc.toString());

         // Info supplémentaire : hash du document
         traceToCreate.getInfos().put("hash", hash);

         // Info supplémentaire : type de hash
         traceToCreate.getInfos().put("typeHash", typeHash);

         // Info supplémentaire : date d'archivage DFCE
         traceToCreate.getInfos().put("dateArchivageDfce", dateArchivageDfce);

         // Appel du dispatcheur
         dispatcheurService.ajouterTrace(traceToCreate);

         // Traces
         LOGGER.debug("{} - Fin", prefix);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de l'écriture de la trace de dépôt de document dans DFCE",
                     ex);
      }

   }
   
   
   /**
    * Trace l'événement "Dépôt d'un document attaché dans DFCE"
    * 
    * @param idDoc
    *           l'identifiant unique DFCE du document parent archivé
    * @param hash
    *           le hash du document
    * @param typeHash
    *           le type de hash ayant servi à calculer le hash fourni en
    *           paramètre
    * @param dateArchivageDfce
    *           la date d'archivage DFCE
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final void traceDepotAttachmentDansDFCE(UUID idDoc, String hash,
         String typeHash, Date dateArchivageDfce) {

      // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
      // planter cette méthode
      try {

         // Traces
         String prefix = "traceDepotAttachmentDansDFCE()";
         LOGGER.debug("{} - Début", prefix);

         // Instantiation de l'objet TraceToCreate
         TraceToCreate traceToCreate = new TraceToCreate();

         // Code de l'événement
         traceToCreate.setCodeEvt(ConstantesTracabilite.TRACE_CODE_EVT_DEPOT_ATTACH_DFCE);

         // Contexte
         traceToCreate.setContexte("DepotAttachmentDansDFCE");

         // Contrat de service et login
         setInfosAuth(traceToCreate);

         // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
         // ce code
         traceToCreate.getInfos().put("saeServeurHostname",
               HostnameUtil.getHostname());
         traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

         // Info supplémentaire : identifiant d'archivage
         traceToCreate.getInfos().put("idDoc", idDoc.toString());

         // Info supplémentaire : hash du document
         traceToCreate.getInfos().put("hash", hash);

         // Info supplémentaire : type de hash
         traceToCreate.getInfos().put("typeHash", typeHash);

         // Info supplémentaire : date d'archivage DFCE
         traceToCreate.getInfos().put("dateArchivageDfce", dateArchivageDfce);

         // Appel du dispatcheur
         dispatcheurService.ajouterTrace(traceToCreate);

         // Traces
         LOGGER.debug("{} - Fin", prefix);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de l'écriture de la trace de dépôt de document attaché dans DFCE",
                     ex);
      }

   }
   
   
   
   /**
    * Trace l'événement "Mise à la corbeille d'un document dans DFCE"
    * 
    * @param idDoc
    *           l'identifiant unique DFCE du document archivé
    * @param hash
    *           le hash du document
    * @param typeHash
    *           le type de hash ayant servi à calculer le hash fourni en
    *           paramètre
    * @param dateArchivageDfce
    *           la date d'archivage DFCE
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final void traceCorbeilleDocDansDFCE(UUID idDoc, String hash,
         String typeHash, Date dateArchivageDfce) {

      // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
      // planter cette méthode
      try {

         // Traces
         String prefix = "traceCorbeilleDocDansDFCE()";
         LOGGER.debug("{} - Début", prefix);

         // Instantiation de l'objet TraceToCreate
         TraceToCreate traceToCreate = new TraceToCreate();

         // Code de l'événement
         traceToCreate.setCodeEvt(ConstantesTracabilite.TRACE_CODE_EVT_CORBEILLE_DOC_DFCE);

         // Contexte
         traceToCreate.setContexte("CorbeilleDocumentDansDFCE");

         // Contrat de service et login
         setInfosAuth(traceToCreate);

         // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
         // ce code
         traceToCreate.getInfos().put("saeServeurHostname",
               HostnameUtil.getHostname());
         traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

         // Info supplémentaire : identifiant d'archivage
         traceToCreate.getInfos().put("idDoc", idDoc.toString());

         // Info supplémentaire : hash du document
         traceToCreate.getInfos().put("hash", hash);

         // Info supplémentaire : type de hash
         traceToCreate.getInfos().put("typeHash", typeHash);

         // Info supplémentaire : date d'archivage DFCE
         traceToCreate.getInfos().put("dateArchivageDfce", dateArchivageDfce);

         // Appel du dispatcheur
         dispatcheurService.ajouterTrace(traceToCreate);

         // Traces
         LOGGER.debug("{} - Fin", prefix);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de l'écriture de la trace de mise à la corbeille de document dans DFCE",
                     ex);
      }

   }
   

   /**
    * Trace l'événement "Restore d'un document dans DFCE"
    * 
    * @param idDoc
    *           l'identifiant unique DFCE du document archivé
    * @param hash
    *           le hash du document
    * @param typeHash
    *           le type de hash ayant servi à calculer le hash fourni en
    *           paramètre
    * @param dateArchivageDfce
    *           la date d'archivage DFCE
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final void traceRestoreDocDansDFCE(UUID idDoc, String hash,
         String typeHash, Date dateArchivageDfce) {

      // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
      // planter cette méthode
      try {

         // Traces
         String prefix = "traceRestoreDocDansDFCE()";
         LOGGER.debug("{} - Début", prefix);

         // Instantiation de l'objet TraceToCreate
         TraceToCreate traceToCreate = new TraceToCreate();

         // Code de l'événement
         traceToCreate.setCodeEvt(ConstantesTracabilite.TRACE_CODE_EVT_RESTORE_DOC_DFCE);

         // Contexte
         traceToCreate.setContexte("RestoreDocumentDansDFCE");

         // Contrat de service et login
         setInfosAuth(traceToCreate);

         // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
         // ce code
         traceToCreate.getInfos().put("saeServeurHostname",
               HostnameUtil.getHostname());
         traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

         // Info supplémentaire : identifiant d'archivage
         traceToCreate.getInfos().put("idDoc", idDoc.toString());

         // Info supplémentaire : hash du document
         traceToCreate.getInfos().put("hash", hash);

         // Info supplémentaire : type de hash
         traceToCreate.getInfos().put("typeHash", typeHash);

         // Info supplémentaire : date d'archivage DFCE
         traceToCreate.getInfos().put("dateArchivageDfce", dateArchivageDfce);

         // Appel du dispatcheur
         dispatcheurService.ajouterTrace(traceToCreate);

         // Traces
         LOGGER.debug("{} - Fin", prefix);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de l'écriture de la trace de restore de document dans DFCE",
                     ex);
      }

   }
   
   
   /**
    * Trace l'événement "Modification d'un document dans DFCE"
    * 
    * @param idDoc
    *           l'identifiant unique DFCE du document archivé
    * @param modifiedMetas
    *           les metas modifiées
    * @param deletedMetas
    *           les métas supprimées
    * @param dateModificationDfce
    *           la date de modification DFCE
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final void traceModifDocumentDansDFCE(UUID idDoc,
         List<String> modifiedMetas, List<String> deletedMetas,
         Date dateModificationDfce) {

      // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
      // planter cette méthode
      try {

         // Traces
         String prefix = "traceModifDocumentDansDFCE()";
         LOGGER.debug("{} - Début", prefix);

         // Instantiation de l'objet TraceToCreate
         TraceToCreate traceToCreate = new TraceToCreate();

         // Code de l'événement
         traceToCreate.setCodeEvt(ConstantesTracabilite.TRACE_CODE_EVT_MODIF_DOC_DFCE);

         // Contexte
         traceToCreate.setContexte("ModificationDocumentDansDFCE");

         // Contrat de service et login
         setInfosAuth(traceToCreate);

         // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
         // ce code
         traceToCreate.getInfos().put("saeServeurHostname",
               HostnameUtil.getHostname());
         traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

         // Info supplémentaire : identifiant d'archivage
         traceToCreate.getInfos().put("idDoc", idDoc.toString());

         // Info supplémentaire : hash du document
         traceToCreate.getInfos().put("modifiedMetadatas",
               StringUtils.join(modifiedMetas, ","));

         // Info supplémentaire : type de hash
         traceToCreate.getInfos().put("deletedMetadatas",
               StringUtils.join(deletedMetas, ","));

         // Info supplémentaire : date d'archivage DFCE
         traceToCreate.getInfos().put("dateModificationDfce",
               dateModificationDfce);

         // Appel du dispatcheur
         dispatcheurService.ajouterTrace(traceToCreate);

         // Traces
         LOGGER.debug("{} - Fin", prefix);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de l'écriture de la trace de dépôt de document dans DFCE",
                     ex);
      }

   }

   private void setInfosAuth(TraceToCreate traceToCreate) {
      
      // Le code du contrat de service
      traceToCreate.setContrat(CS);
      // Le login utilisateur
      traceToCreate.setLogin(LOGIN);
   }

   /**
    * Trace l'événement "Suppression d'un document de DFCE"
    * 
    * @param idDoc
    *           l'identifiant unique DFCE du document supprimé
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final void traceSuppressionDocumentDeDFCE(UUID idDoc) {

      // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
      // planter cette méthode
      try {

         // Traces
         String prefix = "traceSuppressionDocumentDeDFCE()";
         LOGGER.debug("{} - Début", prefix);

         // Instantiation de l'objet TraceToCreate
         TraceToCreate traceToCreate = new TraceToCreate();

         // Code de l'événement
         traceToCreate
               .setCodeEvt(ConstantesTracabilite.TRACE_CODE_EVT_SUPPRESSION_DOC_DFCE);

         // Contexte
         traceToCreate.setContexte("SuppressionDocumentDeDFCE");

         // Contrat de service et login
         setInfosAuth(traceToCreate);

         // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
         // ce code
         traceToCreate.getInfos().put("saeServeurHostname",
               HostnameUtil.getHostname());
         traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

         // Info supplémentaire : identifiant d'archivage
         traceToCreate.getInfos().put("idDoc", idDoc.toString());

         // Appel du dispatcheur
         dispatcheurService.ajouterTrace(traceToCreate);

         // Traces
         LOGGER.debug("{} - Fin", prefix);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de l'écriture de la trace de suppression d'un document de DFCE",
                     ex);
      }

   }

   /**
    * Trace l'événement "Transfert de document vers la GNS"
    * 
    * @param idDoc
    *           l'identifiant unique DFCE du document transféré
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final void traceTransfertDocumentDeDFCE(UUID idDoc) {

      // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
      // planter cette méthode
      try {

         // Traces
         String prefix = "traceTransfertDocumentDeDFCE()";
         LOGGER.debug("{} - Début", prefix);

         // Instantiation de l'objet TraceToCreate
         TraceToCreate traceToCreate = new TraceToCreate();

         // Code de l'événement
         traceToCreate.setCodeEvt(ConstantesTracabilite.TRACE_CODE_EVT_TRANSFERT_DOC_DFCE);

         // Contexte
         traceToCreate.setContexte("TransfertDocumentDeDFCE");

         // Contrat de service et login
         setInfosAuth(traceToCreate);

         // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
         // ce code
         traceToCreate.getInfos().put("saeServeurHostname",
               HostnameUtil.getHostname());
         traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

         // Info supplémentaire : identifiant d'archivage
         traceToCreate.getInfos().put("idDoc", idDoc.toString());

         // Appel du dispatcheur
         dispatcheurService.ajouterTrace(traceToCreate);

         // Traces
         LOGGER.debug("{} - Fin", prefix);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de l'écriture de la trace de transfert d'un document vers la GNS",
                     ex);
      }

   }
}
