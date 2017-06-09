/**
 * 
 */
package fr.urssaf.image.sae.droit.controle;

import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;

/**
 * Service de vérification qu'une liste de métadonnées appartient bien à un PRMD
 * 
 */
public interface PrmdControle {

   /**
    * Vérification que les métadonnées appartiennent au PRMD
    * 
    * @param metadatas
    *           liste des métadonnées
    * @param values
    *           valeurs des paramètres dynamiques
    * @return vrai si les métadonnées appartiennent bien au PRMD de cette
    *         fonction
    */
   boolean isPermitted(List<UntypedMetadata> metadatas,
         Map<String, String> values);

   /**
    * Construit une requête lucène
    * 
    * @param parametres
    *           valeurs des paramètres dynamiques
    * @return la requête lucène
    */
   String createLucene(Map<String, String> parametres);
   
   
   /**
    * Permet d’ajouter le domaine métier dans la liste des métadonnées 
    * si celui-ci n’est pas déjà présent
    * 
    * @param metadatas : Liste des métadonnées
    * 
    * @param values : Map des valeurs des métadonnées du périmètre de données
    */
   public void addDomaine(List<UntypedMetadata> metadatas, Map<String, String> values);
   
}