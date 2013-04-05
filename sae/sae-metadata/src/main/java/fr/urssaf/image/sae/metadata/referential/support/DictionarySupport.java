package fr.urssaf.image.sae.metadata.referential.support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.dao.DictionaryDao;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;

@Component
public class DictionarySupport {
/**
 * classe permettant de réaliser les actions de manipulation des DAO pour la famille de colonne "Dictionary"
 */
   @Autowired
  private DictionaryDao dictionaryDao;
   
   public DictionarySupport(DictionaryDao dictionaryDao ){
      
   }
   
   /**
    * Ajout d'une entré au dictionnaire, le créé s'il n'existe pas
    * @param id identifiant du dictionnaire
    * @param value valeur de l'entée
    * @param clock horloge de la colonne
    */
   public void addElement(String id, String value, long clock){
      
   } 
   
   /**
    * Supprime une entré du dictionnaire
    * @param id identifiant du dictionnaire
    * @param value Valeur de l'entrée à supprimer
    * @param clock Horloge de la colonne
    */
   public void deleteElement(String id, String value, long clock){
      
   }
   
   
   /**
    * Retourne le dictionnaire avec l'identifiant passé en paramètre
    * @param id identifiant du dictionnaire.
    * @return l'objet dictionnaire
    * @throws DictionaryNotFoundException
    */
   public Dictionary find(String id) throws DictionaryNotFoundException{
      return null;
   }
   
   
   /**
    * Retourne l'ensemble des dictionnaires
    * @return Liste d'objet dictionnaire
    */
   public List<Dictionary> findAll(){
      return null;
   }
   
}
