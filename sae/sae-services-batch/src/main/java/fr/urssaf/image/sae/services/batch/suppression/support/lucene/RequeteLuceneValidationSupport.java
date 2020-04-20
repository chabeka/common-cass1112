package fr.urssaf.image.sae.services.batch.suppression.support.lucene;

import fr.urssaf.image.sae.services.batch.suppression.exception.SuppressionMasseRequeteValidationException;

/**
 * Composant de validation de la requete lucene de traitement de
 * suppression de masse
 * 
 */
public interface RequeteLuceneValidationSupport {

   /**
    * validation du format de la requete lucene
    * 
    * @param requeteLucene
    *           requete lucene de suppression
    * @return String
    *       requete lucene issue du trim et validée
    * @throws SuppressionMasseRequeteValidationException
    *           La requete lucene est invalide
    */
   String validationRequeteLucene(String requeteLucene)
         throws SuppressionMasseRequeteValidationException;
   
   /**
    * verification des droits de la requete lucene
    * 
    * @param requeteLucene
    *           requete lucene de suppression
    * @return String
    *       requete lucene contenant la limitation en fonction des droits
    * @throws SuppressionMasseRequeteValidationException
    *           La requete lucene est invalide
    */
   String verificationDroitRequeteLucene(String requeteLucene)
         throws SuppressionMasseRequeteValidationException;
}
