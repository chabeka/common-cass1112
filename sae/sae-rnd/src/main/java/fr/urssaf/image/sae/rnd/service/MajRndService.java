package fr.urssaf.image.sae.rnd.service;

import fr.urssaf.image.sae.rnd.exception.MajRndException;

/**
 * Service de mise à jour du RND
 * 
 *
 */
public interface MajRndService {

   /**
    * Lance le traitement de mise à jour du RND
    * @throws MajRndException Exception levée en cas d'erreur lors de la mise à jour
    */
   void lancer() throws MajRndException;

}
