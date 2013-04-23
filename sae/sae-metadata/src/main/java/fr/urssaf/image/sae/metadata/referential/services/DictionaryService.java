package fr.urssaf.image.sae.metadata.referential.services;

import java.util.List;

import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;

/**
 * Service permettant de réaliser les opérations sur les dicionnaires de données.
 */
public interface DictionaryService {


   
   /**
    * Créé ou modifie le dictionnaire
    * @param name nom du dictionnaire
    * @param values les des valeurs possibles
    */
   void addElements(String name, List<String> values);
   
   /**
    * supprime les éléments du dictionnaire
    * @param name nom du dictionnaire
    * @param values les des valeurs possibles 
    */
   void deleteElements(String name, List<String> values);
   
   
   /**
    * recherche le dictionnaire avec l'identifiant passé en paramètre
    * @param name nom du dictionnaire
    * @throws DictionaryNotFoundException 
    * @return {@link Dictionary} 
    */
   Dictionary find(String name)throws DictionaryNotFoundException;
   
   /**
    * recherche l'ensemble des dictionnaires existants
    * @return List{@link Dictionary} une liste de dictionnaires
    */
   List<Dictionary> findAll();
}
