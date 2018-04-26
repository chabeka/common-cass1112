package fr.urssaf.image.sae.rnd.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.exception.DfceRuntimeException;
import fr.urssaf.image.sae.rnd.exception.MajRndException;
import fr.urssaf.image.sae.rnd.exception.RndRecuperationException;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;
import fr.urssaf.image.sae.rnd.service.MajRndService;
import fr.urssaf.image.sae.rnd.support.DfceSupport;
import fr.urssaf.image.sae.rnd.util.HostnameUtil;
import fr.urssaf.image.sae.rnd.ws.adrn.service.RndRecuperationService;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;

/**
 * Service de mise à jour du RND
 * 
 * 
 */
@Service
public class MajRndServiceImpl implements MajRndService {

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(MajRndServiceImpl.class);

   @Autowired
   private SaeBddSupport saeBddSupport;

   @Autowired
   private DfceSupport dfceSupport;

   @Autowired
   private RndRecuperationService rndRecuperationService;

   @Autowired
   private DispatcheurService dispatcheurService;

   /**
    * Traçabilité : le code de l'événement pour la réussite de la MAJ du RND
    */
   private static final String TRACE_CODE_EVT_REUSSITE_MAJ_RND = "MAJ_VERSION_RND|OK";

   @Override
   public final void lancer() throws MajRndException {
      String trcPrefix = "lancer";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      LOGGER.info("{} - Début de la synchronisation avec l'ADRN", trcPrefix);

      // Récupération de la version en cours dans le SAE
      // -----------------------------------------------
      VersionRnd versionRndSae;
      try {
         versionRndSae = saeBddSupport.getVersionRnd();
      } catch (SaeBddRuntimeException e) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de la récupération de la version en cours dans le SAE",
                     e);
         throw new MajRndException(e);
      }
      LOGGER.info("{} - Version du RND en cours dans le SAE : {}", trcPrefix,
            versionRndSae.getVersionEnCours());

      // Récupération de la version en cours dans l'ADRN
      // -----------------------------------------------
      String versionAdrn;
      try {
         versionAdrn = rndRecuperationService.getVersionCourante();
      } catch (RndRecuperationException e) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de la récupération de la version en cours dans l'ADRN",
                     e);
         throw new MajRndException(e);
      }
      LOGGER.info("{} - Version du RND en cours dans l'ADRN : {}", trcPrefix,
            versionAdrn);

      if (StringUtils.isBlank(versionAdrn)) {
         LOGGER
               .error("Une erreur s'est produite lors de la récupération de la version en cours dans l'ADRN : Version ADRN nulle");
         throw new MajRndException(
               "Une erreur s'est produite lors de la récupération de la version en cours dans l'ADRN : Version ADRN nulle");
      }

      // Mise à jour du RND dans le SAE
      // ==============================
      // Si le RND n'a jamais été mis à jour dans le SAE ou si la version
      // n'est pas la même que dans l'ADRN
      if (StringUtils.isBlank(versionRndSae.getVersionEnCours())
            || !versionRndSae.getVersionEnCours().equals(versionAdrn)) {

         // Récupération du RND à partir de l'ADRN
         // --------------------------------------
         LOGGER.info("{} - Récupération du RND à partir de l'ADRN", trcPrefix);
         List<TypeDocument> listeTypeDocs;
         try {
            listeTypeDocs = rndRecuperationService.getListeRnd(versionAdrn);
         } catch (RndRecuperationException e) {
            LOGGER
                  .error(
                        "Une erreur s'est produite lors de la récupération du RND à partir de l'ADRN",
                        e);
            throw new MajRndException(e);
         }

         // Mise à jour de la BDD du SAE
         // ----------------------------
         LOGGER.info("{} - Mise à jour de la BDD du SAE", trcPrefix);
         try {
            saeBddSupport.updateRnd(listeTypeDocs);
         } catch (SaeBddRuntimeException e) {
            LOGGER
                  .error(
                        "Une erreur s'est produite lors de la MAJ du RND dans la BDD du SAE",
                        e);
            throw new MajRndException(e);
         }

         // Mise à jour de la BDD DFCE
         // --------------------------
         LOGGER.info("{} - Mise à jour de la BDD DFCE", trcPrefix);
         try {
            dfceSupport.updateLifeCycleRule(listeTypeDocs);
         } catch (DfceRuntimeException e) {
            LOGGER
                  .error(
                        "Une erreur s'est produite lors de la MAJ du RND dans la BDD DFCE",
                        e);
            throw new MajRndException(e);
         }

         // Récupération de la liste des correspondances
         // --------------------------------------------
         LOGGER.info("{} - Récupération de la liste des correspondances",
               trcPrefix);
         Map<String, String> listeCorrespondances;
         try {
            listeCorrespondances = rndRecuperationService
                  .getListeCorrespondances(versionAdrn);
         } catch (RndRecuperationException e) {
            LOGGER
                  .error(
                        "Une erreur s'est produite lors de récupération de la liste des correspondances",
                        e);
            throw new MajRndException(e);
         }

         // Mise à jour des correspondances dans la BDD du SAE
         // --------------------------------------------------
         LOGGER.info("{} - Mise à jour des correspondances dans la BDD du SAE",
               trcPrefix);
         try {
            saeBddSupport.updateCorrespondances(listeCorrespondances, versionRndSae.getVersionEnCours());
         } catch (SaeBddRuntimeException e) {
            LOGGER
                  .error(
                        "Une erreur s'est produite lors de la MAJ des correspondances dans la BDD du SAE",
                        e);
            throw new MajRndException(e);
         }

         // Mise à jour des informations sur la version en cours dans le SAE
         // ----------------------------------------------------------------
         LOGGER
               .info(
                     "{} - Mise à jour des informations sur la version en cours dans le SAE",
                     trcPrefix);
         versionRndSae.setVersionEnCours(versionAdrn);
         versionRndSae.setDateMiseAJour(new Date());
         try {
            saeBddSupport.updateVersionRnd(versionRndSae);
         } catch (SaeBddRuntimeException e) {
            LOGGER
                  .error(
                        "Une erreur s'est produite lors de la MAJ des informations sur la version en cours dans le SAE",
                        e);
            throw new MajRndException(e);
         }

         // Mise à jour des traces de la MAJ de la version du RND OK
         // --------------------------------------------------------
         LOGGER.info("{} - Ecriture de la traçabilité", trcPrefix);
         ecrireTraces(TRACE_CODE_EVT_REUSSITE_MAJ_RND);

      } else {
         LOGGER.info("{} - Aucune mise à jour à effectuer", trcPrefix);
      }

      // GESTION DES CODES TEMPORAIRES
      // =============================
      // Récupération de la liste des codes temporaires
      LOGGER.info("{} - Récupération de la liste des codes temporaires",
            trcPrefix);
      List<TypeDocument> listeCodesTemporaires;
      try {
         listeCodesTemporaires = rndRecuperationService
               .getListeCodesTemporaires();
      } catch (RndRecuperationException e) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de la récupération de la liste des codes temporaires",
                     e);
         throw new MajRndException(e);
      }

      if (listeCodesTemporaires == null) {
         LOGGER.info("{} - Nombre de codes temporaires trouvés : aucun",
               trcPrefix);
      } else {
         LOGGER.info("{} - Nombre de codes temporaires trouvés : {}",
               trcPrefix, listeCodesTemporaires.size());
      }

      // On traite les codes temporaires s'il y en a au moins 1
      if (CollectionUtils.isNotEmpty(listeCodesTemporaires)) {

         // Mise à jour des types de documents dans la BDD du SAE en ajoutant
         // les temporaires
         LOGGER
               .info(
                     "{} - Mise à jour des types de documents temporaires dans la BDD du SAE",
                     trcPrefix);
         try {
            saeBddSupport.updateRnd(listeCodesTemporaires);
         } catch (SaeBddRuntimeException e) {
            LOGGER
                  .error(
                        "Une erreur s'est produite lors de la MAJ des types de documents temporaires dans la BDD du SAE",
                        e);
            throw new MajRndException(e);
         }

         // Mise à jour des types de documents dans DFCE en ajoutant les
         // temporaires
         LOGGER
               .info(
                     "{} - Mise à jour des types de documents dans DFCE en ajoutant les temporaires",
                     trcPrefix);
         try {
            dfceSupport.updateLifeCycleRule(listeCodesTemporaires);
         } catch (DfceRuntimeException e) {
            LOGGER
                  .error(
                        "Une erreur s'est produite lors de la MAJ des types de documents temporaires dans la BDD DFCE",
                        e);
            throw new MajRndException(e);
         }

         // TODO : Traitement de mise à jour des correspondances

      }

      LOGGER.info("{} - Fin de la synchronisation avec l'ADRN", trcPrefix);

      LOGGER.debug(FIN_LOG, trcPrefix);

   }

   private void ecrireTraces(String codeEvenement) {
      try {
         // Instantiation de l'objet TraceToCreate
         TraceToCreate traceToCreate = new TraceToCreate();

         // Code de l'événement
         traceToCreate.setCodeEvt(codeEvenement);

         // Contexte
         traceToCreate.setContexte("majVersionRnd");

         // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
         // ce code
         traceToCreate.getInfos().put("saeServeurHostname",
               HostnameUtil.getHostname());
         traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

         // Appel du dispatcheur
         dispatcheurService.ajouterTrace(traceToCreate);
      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de l'écriture de la trace d'erreur de maj du RND",
                     ex);
      }
   }

}
