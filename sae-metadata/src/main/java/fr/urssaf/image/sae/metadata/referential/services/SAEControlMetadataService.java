/**
 * 
 */
package fr.urssaf.image.sae.metadata.referential.services;

import java.util.List;

import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;

/**
 * Fournit les contrôles sur les metadatas
 * 
 */
public interface SAEControlMetadataService {

   /**
    * Vérifie que la liste des codes long passés en paramètre existent dans le
    * référentiel des metadata
    * 
    * @param listLongCode
    *           liste des codes longs dont il faut vérifier l'existence
    * @throws LongCodeNotFoundException exception levée lorsqu'au moins
    *         un code n'existe pas
    * @throws ReferentialException
    *            exception levée lors de l'accès aux metadonnées
    */
   void controlLongCodeExist(List<String> listLongCode)
         throws LongCodeNotFoundException, ReferentialException;

   /**
    * Vérifie que la liste des codes long passés en paramètre sont consultables
    * 
    * @param listLongCode
    *           liste des codes longs dont il faut vérifier qu'ils sont
    *           consultables
    * @throws LongCodeNotFoundException
    *            exception levée lorsqu'au moins un code n'est pas consultable
    * @throws ReferentialException
    *            exception levée lors de l'accès aux
    */
   void controlLongCodeIsAFConsultation(List<String> listLongCode)
         throws LongCodeNotFoundException, ReferentialException;
}
