package fr.urssaf.image.sae.rnd.service;

import fr.urssaf.image.sae.rnd.exception.MajCorrespondancesException;

/**
 * Service de mise à jour des correspondances
 * 
 *
 */
public interface MajCorrespondancesService {

   /**
    * Lance le traitement de mise à jour des correspondances
    * @throws MajCorrespondancesException Exception levée en cas d'erreur dans la mise à jour
    */
   void lancer() throws MajCorrespondancesException;
}
