package fr.urssaf.image.sae.services.controles.traces;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.HostnameUtil;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * Classe de support pour écrire les traces via la brique de traçabilité.
 */
@Component
public class TracesControlesSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TracesControlesSupport.class);

   /**
    * Traçabilité : le code de l'événement pour l'erreur d'identification du
    * format du fichier
    */
   public static final String TRACE_CODE_EVT_ERREUR_IDENT_FICHIER = "ERREUR_IDENT_FORMAT_FICHIER|INFO";

   /**
    * Traçabilité : le code de l'événement pour l'erreur de validation du format
    * du fichier
    */
   public static final String TRACE_CODE_EVT_ERREUR_VALID_FICHIER = "ERREUR_VALID_FORMAT_FICHIER|INFO";

   /**
    * Dispatcheur pour la traçabilité.
    */
   @Autowired
   private DispatcheurService dispatcheurService;

   /**
    * Méthode permettant de récupérer les informations d'authentification pour
    * les mettre dans l'objet du domaine de la traçabilité.
    * 
    * @param traceToCreate
    *           trace à créer
    */
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

   /**
    * Ecrit une trace d'erreur d'identification du format du fichier.
    * 
    * @param contexte
    *           contexte
    * @param formatFichierDeclare
    *           format du fichier déclaré
    * @param formatFichierReconnu
    *           format du fichier reconnu
    * @param idTraitement
    *           identifiant du traitement de masse ou du traitement unitaire
    */
   public final void traceErreurIdentFormatFichier(String contexte,
         String formatFichierDeclare, String formatFichierReconnu,
         String idTraitement) {
      try {

         traceErreurIdent(contexte, formatFichierDeclare, formatFichierReconnu,
               "", idTraitement);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur est survenue lors de la traçabilité de l'erreur d'identification d'un format de fichier",
                     ex);
      }
   }

   /**
    * Ecrit une trace d'erreur d'identification du format du fichier.
    * 
    * @param contexte
    *           contexte
    * @param formatFichierDeclare
    *           format du fichier déclaré
    * @param formatFichierReconnu
    *           format du fichier reconnu
    * @param idDoc
    *           identifiant du document archivé
    * @param idTraitement
    *           identifiant du traitement de masse ou du traitement unitaire
    */
   public final void traceErreurIdentFormatFichier(String contexte,
         String formatFichierDeclare, String formatFichierReconnu,
         String idDoc, String idTraitement) {
      try {

         traceErreurIdent(contexte, formatFichierDeclare, formatFichierReconnu,
               idDoc, idTraitement);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur est survenue lors de la traçabilité de l'erreur d'identification d'un format de fichier",
                     ex);
      }
   }

   /**
    * Ecrit une trace d'erreur d'identification du format du fichier.
    * 
    * @param contexte
    *           contexte
    * @param formatFichierDeclare
    *           format du fichier déclaré
    * @param formatFichierReconnu
    *           format du fichier reconnu
    * @param idDoc
    *           identifiant du document archivé
    * @param idTraitement
    *           identifiant du traitement de masse ou du traitement unitaire
    */
   private void traceErreurIdent(String contexte, String formatFichierDeclare,
         String formatFichierReconnu, String idDoc, String idTraitement) {
      // Instantiation de l'objet TraceToCreate
      TraceToCreate traceToCreate = new TraceToCreate();

      // Code de l'événement
      traceToCreate.setCodeEvt(TRACE_CODE_EVT_ERREUR_IDENT_FICHIER);

      // Contexte
      traceToCreate.setContexte(contexte);

      // Contrat de service et login
      setInfosAuth(traceToCreate);

      // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
      // ce code
      traceToCreate.getInfos().put("saeServeurHostname",
            HostnameUtil.getHostname());
      traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

      // Info supplémentaire : formatFichierDeclare
      traceToCreate.getInfos()
            .put("formatFichierDeclare", formatFichierDeclare);

      // Info supplémentaire : formatFichierReconnu
      traceToCreate.getInfos()
            .put("formatFichierReconnu", formatFichierReconnu);

      // Info supplémentaire : idTraitement
      traceToCreate.getInfos().put("idTraitement", idTraitement);

      // Info supplémentaire : idDoc
      traceToCreate.getInfos().put("idDoc", idDoc);
      LOGGER.debug("infos: {}", traceToCreate.getInfos());

      // Appel du dispatcheur
      dispatcheurService.ajouterTrace(traceToCreate);
   }

   /**
    * Ecrit une trace d'erreur de validation du format du fichier.
    * 
    * @param contexte
    *           contexte
    * @param formatFichier
    *           format du fichier déclaré
    * @param details
    *           détails des erreurs de validation
    * @param idTraitement
    *           identifiant du traitement de masse ou du traitement unitaire
    */
   public final void traceErreurValidFormatFichier(String contexte,
         String formatFichier, String details, String idTraitement) {
      try {

         traceErreurValid(contexte, formatFichier, details, "", idTraitement);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur est survenue lors de la traçabilité de l'erreur de validation d'un format de fichier",
                     ex);
      }
   }

   /**
    * Ecrit une trace d'erreur de validation du format du fichier.
    * 
    * @param contexte
    *           contexte
    * @param formatFichier
    *           format du fichier déclaré
    * @param details
    *           détails des erreurs de validation
    * @param idDoc
    *           identifiant du document archivé
    * @param idTraitement
    *           identifiant du traitement de masse ou du traitement unitaire
    */
   public final void traceErreurValidFormatFichier(String contexte,
         String formatFichier, String details, String idDoc, String idTraitement) {
      try {

         traceErreurValid(contexte, formatFichier, details, idDoc, idTraitement);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur est survenue lors de la traçabilité de l'erreur de validation d'un format de fichier",
                     ex);
      }
   }

   /**
    * Ecrit une trace d'erreur d'identification du format du fichier.
    * 
    * @param contexte
    *           contexte
    * @param formatFichier
    *           format du fichier déclaré
    * @param details
    *           détails des erreurs de validation
    * @param idDoc
    *           identifiant du document archivé
    * @param idTraitement
    *           identifiant du traitement de masse ou du traitement unitaire
    */
   private void traceErreurValid(String contexte, String formatFichier,
         String details, String idDoc, String idTraitement) {
      // Instantiation de l'objet TraceToCreate
      TraceToCreate traceToCreate = new TraceToCreate();

      // Code de l'événement
      traceToCreate.setCodeEvt(TRACE_CODE_EVT_ERREUR_VALID_FICHIER);

      // Contexte
      traceToCreate.setContexte(contexte);

      // Contrat de service et login
      setInfosAuth(traceToCreate);

      // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
      // ce code
      traceToCreate.getInfos().put("saeServeurHostname",
            HostnameUtil.getHostname());
      traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

      // Info supplémentaire : formatFichier
      traceToCreate.getInfos().put("formatFichier", formatFichier);

      // Info supplémentaire : details
      traceToCreate.getInfos().put("details", details);

      // Info supplémentaire : idTraitement
      traceToCreate.getInfos().put("idTraitement", idTraitement);

      // Info supplémentaire : idDoc
      traceToCreate.getInfos().put("idDoc", idDoc);
      LOGGER.debug("infos: {}", traceToCreate.getInfos());

      // Appel du dispatcheur
      dispatcheurService.ajouterTrace(traceToCreate);
   }
}
