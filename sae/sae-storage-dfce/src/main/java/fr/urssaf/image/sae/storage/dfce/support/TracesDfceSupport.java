package fr.urssaf.image.sae.storage.dfce.support;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.utils.HostnameUtil;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * Classe de support pour écrire les traces via la brique de traçabilité.
 */
@Component
public class TracesDfceSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TracesDfceSupport.class);

   @Autowired
   private DispatcheurService dispatcheurService;

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
         traceToCreate.setCodeEvt(Constants.TRACE_CODE_EVT_DEPOT_DOC_DFCE);

         // Timestamp
         Date timestamp = new Date();
         traceToCreate.setTimestamp(timestamp);

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
                     "Une erreur s'est produite lors de l'écriture de la trace de dépôt de fichier dans DFCE",
                     ex);
      }

   }

   private void setInfosAuth(TraceToCreate traceToCreate) {

      if ((SecurityContextHolder.getContext() != null)
            && (SecurityContextHolder.getContext().getAuthentication() != null)
            && (SecurityContextHolder.getContext().getAuthentication()
                  .getPrincipal() != null)) {

         VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
               .getContext().getAuthentication().getPrincipal();

         // Le code du Contrat de Service
         traceToCreate.setContrat(extrait.getCodeAppli());

         // Le ou les PAGM
         if (CollectionUtils.isNotEmpty(extrait.getPagms())) {
            traceToCreate.getPagms().addAll(extrait.getPagms());
         }

         // Le login utilisateur
         traceToCreate.setLogin(extrait.getIdUtilisateur());

      }

   }

}
