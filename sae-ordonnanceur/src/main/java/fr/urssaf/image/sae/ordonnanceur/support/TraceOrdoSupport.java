package fr.urssaf.image.sae.ordonnanceur.support;

import java.net.URI;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.trace.utils.HostnameUtil;

/**
 * Classe chargée de la traçabilité
 */
@Component
public class TraceOrdoSupport {

   protected static final String TRC_CODE_EVT_ECDE_INDISPO = "ORDO_ECDE_DISPO|KO";

   protected static final String TRC_CONTEXTE = "ordonnanceur";

   protected static final String TRC_ACTION = "Vérifier le montage NFS de l'ECDE. Vérifier la disponibilité de l'ECDE. Vérifier l'existence du répertoire ECDE, et ses droits";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceOrdoSupport.class);

   private final DispatcheurService dispatcheurService;

   /**
    * Constructeur
    * 
    * @param dispatcheurService
    *           le serveur dispatcheur des traces
    */
   @Autowired
   public TraceOrdoSupport(DispatcheurService dispatcheurService) {
      this.dispatcheurService = dispatcheurService;
   }

   /**
    * Ecrit une trace d'indisponibilité d'une URL ECDE
    * 
    * @param idJob
    *           l'identifiant du job dont l'URL ECDE est indisponible
    * @param urlEcde
    *           l'URL ECDE indisponible
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final TraceToCreate ecritTraceUrlEcdeNonDispo(UUID idJob, URI urlEcde) {

      TraceToCreate traceToCreate = null;

      // On fait un try catch Throwable pour empêcher qu'un plantage de la
      // traçabilité fasse planter la suite des opérations
      try {

         // Instantiation de l'objet TraceToCreate
         traceToCreate = new TraceToCreate();

         // Code de l'événement
         traceToCreate.setCodeEvt(TRC_CODE_EVT_ECDE_INDISPO);

         // Contexte
         traceToCreate.setContexte(TRC_CONTEXTE);

         // Action (obligatoire pour le registre d'exploitation)
         traceToCreate.setAction(TRC_ACTION);

         // Info supplémentaire : Nom du serveur et adresse IP du serveur
         // sur lequel tourne l'ordonnanceur
         traceToCreate.getInfos().put("saeServeurHostname",
               HostnameUtil.getHostname());
         traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

         // Info supplémentaire : Identifiant du job de capture de masse
         traceToCreate.getInfos().put("idJob", idJob.toString());

         // Info supplémentaire : URL ECDE indisponible
         traceToCreate.getInfos().put("urlEcde", urlEcde.toString());

         // Appel du dispatcheur
         dispatcheurService.ajouterTrace(traceToCreate);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur est survenue lors de la traçabilité de l'indisponibilité d'une URL ECDE",
                     ex);
      }

      // Renvoie de la trace
      return traceToCreate;

   }

}
