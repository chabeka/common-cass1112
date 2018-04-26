package fr.urssaf.image.sae.rnd.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.exception.MajCorrespondancesException;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.service.MajCorrespondancesService;

/**
 * Service de mise à jour des correspondances
 * 
 * 
 */
@Service
public class MajCorrespondancesServiceImpl implements MajCorrespondancesService {

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(MajCorrespondancesServiceImpl.class);

   @Autowired
   private SaeBddSupport saeBddSupport;

   @Override
   public final void lancer() throws MajCorrespondancesException {
      String trcPrefix = "lancer";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      LOGGER
            .info(
                  "{} - Début du traitement sur les associations codes RND temporaires / codes RND définitifs",
                  trcPrefix);

      try {
         // Récupération des correspondances
         List<Correspondance> listeCorrespondances = saeBddSupport
               .getAllCorrespondances();

         if (listeCorrespondances == null) {
            LOGGER.info("{} - Nombre d'associations trouvées : aucune",
                  trcPrefix);
         } else {
            LOGGER.info("{} - Nombre d'associations trouvées : {}", trcPrefix,
                  listeCorrespondances.size());
         }

         if (CollectionUtils.isEmpty(listeCorrespondances)) {
            LOGGER.info("{} - Aucun traitement à réaliser", trcPrefix);

         } else {

            for (Correspondance correspondance : listeCorrespondances) {

               LOGGER
                     .info(
                           "{} - Traitement de la correspondance {} (temporaire) => {} (définitif)",
                           new Object[] { trcPrefix,
                                 correspondance.getCodeTemporaire(),
                                 correspondance.getCodeDefinitif() });

               // On démarre la mise à jour des documents concernés par cette
               // correspondance
               saeBddSupport.startMajCorrespondance(correspondance);

               // TODO : Mise à jour des documents (En attente des dev pour la
               // modification de documents)

               // TODO : passer l'état à FAILURE ou SUCCES et mettre la date de
               // fin
            }
         }

      } catch (SaeBddRuntimeException e) {
         throw new MajCorrespondancesException(e);
      }

      LOGGER
            .info(
                  "{} - Fin du traitement sur les associations codes RND temporaires / codes RND définitifs",
                  trcPrefix);

      LOGGER.debug(FIN_LOG, trcPrefix);

   }

}
