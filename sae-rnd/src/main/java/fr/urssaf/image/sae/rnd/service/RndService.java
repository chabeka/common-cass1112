package fr.urssaf.image.sae.rnd.service;

import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

/**
 * Service de récupération des propriété d'un type de document
 * 
 * 
 */
public interface RndService {

   /**
    * Récupère le code activité associé à un RND
    * 
    * @param codeRnd
    *           Le code RND
    * @return Le code activité
    * @throws CodeRndInexistantException
    *            Exception levée si le code RND n'existe pas
    */
   String getCodeActivite(String codeRnd) throws CodeRndInexistantException;

   /**
    * Récupère le code fonction associé à un RND
    * 
    * @param codeRnd
    *           Le code RND
    * @return Le code fonction
    * @throws CodeRndInexistantException
    *            Exception levée si le code RND n'existe pas
    */
   String getCodeFonction(String codeRnd) throws CodeRndInexistantException;

   /**
    * Récupère la durée de conservation associé à un RND
    * 
    * @param codeRnd
    *           Le code RND
    * @return La durée de conservation
    * @throws CodeRndInexistantException
    *            Exception levée si le code RND n'existe pas
    */
   int getDureeConservation(String codeRnd) throws CodeRndInexistantException;

   /**
    * Vérifie si un code est clôturé ou non
    * 
    * @param codeRnd
    *           le code RND a tester
    * @return true si le code est clôturé
    * @throws CodeRndInexistantException
    *            Exception levée si le code RND n'existe pas
    */
   boolean isCloture(String codeRnd) throws CodeRndInexistantException;

   /**
    * Récupère le type de document associé à un RND
    * 
    * @param codeRnd
    *           Le code RND
    * @return Le type de document
    * @throws CodeRndInexistantException
    *            Exception levée si le code RND n'existe pas
    */
   TypeDocument getTypeDocument(String codeRnd)
         throws CodeRndInexistantException;

}
