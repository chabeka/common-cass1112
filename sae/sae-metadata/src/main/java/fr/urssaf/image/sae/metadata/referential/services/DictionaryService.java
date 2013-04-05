package fr.urssaf.image.sae.metadata.referential.services;

import java.util.List;

import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;

public interface DictionaryService {

   /**
    * Service permettant de réaliser les opérations sur les dicionnaires de données.
    */
   
   /**
    * Créé ou modifie le dictionnaire
    * 
    */
   void addElements(String name, List<String> values);
   
   /**
    * supprime les éléments du dictionnaire
    * 
    */
   void deleteElement(String name, List<String> values);
   
   
   /**
    * recherche le dictionnaire avec l'identifiant passé en paramètre
    * 
    */
   Dictionary find(String name)throws DictionaryNotFoundException;
   
   /**
    * recherche l'ensemble des dictionnaires existants
    * 
    */
   List<Dictionary> findAll();
}
