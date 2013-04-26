package fr.urssaf.image.sae.rnd.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.exception.DfceRuntimeException;
import fr.urssaf.image.sae.rnd.exception.MajRndException;
import fr.urssaf.image.sae.rnd.exception.RndRecuperationException;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;
import fr.urssaf.image.sae.rnd.service.MajRndService;
import fr.urssaf.image.sae.rnd.support.DfceSupport;
import fr.urssaf.image.sae.rnd.ws.adrn.service.RndRecuperationService;

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

   @Override
   public final void lancer() throws MajRndException {
      String trcPrefix = "lancer";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      try {
         // Récupération de la version en cours dans le SAE
         VersionRnd versionRndSae = saeBddSupport.getVersionRnd();

         // Récupération de la version en cours dans l'ADRN
         String versionAdrn = rndRecuperationService.getVersionCourante();

         // Si le RND n'a jamais été mis à jour dans le SAE ou si la version
         // n'est pas la même que dans l'ADRN
         if (StringUtils.isBlank(versionRndSae.getVersionEnCours())
               || !versionRndSae.getVersionEnCours().equals(versionAdrn)) {

            // Récupération du RND à partir de l'ADRN
            List<TypeDocument> listeTypeDocs = rndRecuperationService
                  .getListeRnd(versionAdrn);

            // Mise à jour de la BDD du SAE
            saeBddSupport.updateRnd(listeTypeDocs);

            // Mise à jour de la BDD DFCE
            dfceSupport.updateLifeCycleRule(listeTypeDocs);

            // Récupération de la liste des correspondances
            Map<String, String> listeCorrespondances = rndRecuperationService
                  .getListeCorrespondances(versionAdrn);

            // Mise à jour des correspondances dans la BDD du SAE
            saeBddSupport.updateCorrespondances(listeCorrespondances);

            // Récupération de la liste des codes temporaires
            List<TypeDocument> listeCodesTemporaires = rndRecuperationService
                  .getListeCodesTemporaires();

            // Mise à jour des types de documents dans la BDD du SAE en ajoutant
            // les temporaires
            saeBddSupport.updateRnd(listeCodesTemporaires);

            // Mise à jour des types de documents dans DFCE en ajoutant les
            // temporaires
            dfceSupport.updateLifeCycleRule(listeCodesTemporaires);

            versionRndSae.setVersionEnCours(versionAdrn);
            versionRndSae.setDateMiseAJour(new Date());
            saeBddSupport.updateVersionRnd(versionRndSae);

            // TODO : Mise à jour des traces

            // TODO : Traitement de mise à jour des correspondances

         }
         

         LOGGER.debug(FIN_LOG, trcPrefix);

      } catch (ParameterNotFoundException e) {
         throw new MajRndException(e.getMessage(), e.getCause());
      } catch (SaeBddRuntimeException e) {
         throw new MajRndException(e.getMessage(), e.getCause());
      } catch (RndRecuperationException e) {
         throw new MajRndException(e.getMessage(), e.getCause());
      } catch (DfceRuntimeException e) {
         throw new MajRndException(e.getMessage(), e.getCause());
      }

   }

}
