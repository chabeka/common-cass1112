package fr.urssaf.image.sae.ordonnanceur.service.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.service.DecisionService;
import fr.urssaf.image.sae.ordonnanceur.support.CaptureMasseSupport;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;
import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;

/**
 * Implémentation du sevice {@link DecisionService}
 * 
 * 
 */
@Service
public class DecisionServiceImpl implements DecisionService {

   private final CaptureMasseSupport captureMasseSupport;

   private final DFCESupport dfceSuppport;

   private static final Logger LOG = LoggerFactory
         .getLogger(DecisionServiceImpl.class);

   private static final String PREFIX_LOG = "ordonnanceur()";

   /**
    * 
    * @param captureMasseSupport
    *           support pour les traitements de capture en masse
    * @param dfceSuppport
    *           service de DFCE
    */
   @Autowired
   public DecisionServiceImpl(CaptureMasseSupport captureMasseSupport,
         DFCESupport dfceSuppport) {

      this.captureMasseSupport = captureMasseSupport;
      this.dfceSuppport = dfceSuppport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SimpleJobRequest trouverJobALancer(
         List<SimpleJobRequest> jobsEnAttente, List<SimpleJobRequest> jobsEnCours)
         throws AucunJobALancerException {

      // pour l'instant la partie décisionnelle ne prend actuellement en compte
      // que les traitements d'archivage de masse.
      // si un nouveau type de traitement est ajouté, l'algo sera modifié.

      // vérification que des traitements de capture en masse sont à lancer
      if (CollectionUtils.isEmpty(jobsEnAttente)) {
         throw new AucunJobALancerException();
      }

      // filtrage des capture en masse sur l'ECDE local
      List<SimpleJobRequest> jobInstances = captureMasseSupport
            .filtrerCaptureMasseLocal(jobsEnAttente);

      // vérification que des traitements de capture en masse sur l'ECDE local
      // sont à lancer
      if (CollectionUtils.isEmpty(jobInstances)) {
         throw new AucunJobALancerException();
      }

      // filtrage des traitements en cours sur les capture en masse
      if (!CollectionUtils.isEmpty(jobsEnCours)) {
         Collection<SimpleJobRequest> traitementsEnCours = captureMasseSupport
               .filtrerCaptureMasse(jobsEnCours);

         // si un traitement de capture en masse est en cours alors aucun
         // traitement n'est à lancer sur le serveur
         if (!traitementsEnCours.isEmpty()) {
            throw new AucunJobALancerException();
         }

      }

      // vérification que le serveur DFCE est Up!
      if (!dfceSuppport.isDfceUp()) {
         LOG.debug("{} - DFCE n'est pas accessible avec la configuration",
               PREFIX_LOG);
         throw new AucunJobALancerException();
      }

      // on renvoie la capture en masse en attente avec l'identifiant le plus
      // ancien
      // la liste est déjà triée

      return jobInstances.get(0);
   }

}
