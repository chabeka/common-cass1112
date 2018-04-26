package fr.urssaf.image.commons.dao.spring.exemple.dao;

import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;

public interface DocumentModifyDao {
   
   /**
    * Persiste d'un objet document<br>
    * <u>méthode Hibernate</u>
    * @param document entité à persister
    */
   void save(Document document);
   
   /**
    * Persiste d'un objet document<br>
    * <u>méthode SQL natif</u>
    * @param document entité à persister
    */
   void saveSQL(Document document);  

   /**
    * Supprime un objet document de la persistance<br>
    * <u>méthode Hibernate</u>
    * @param document entité à supprimer
    */
   void delete(Document document);
      
   /**
    * Supprime un objet document de la persistance<br>
    * <u>méthode SQL natif</u>
    * @param document entité à supprimer
    */
   void deleteSQL(Document document);
   
   /**
    * Met à jour un objet document dans la persistance<br>
    * <u>méthode Hibernate</u>
    * @param document entité à mettre à jour
    */
   void update(Document document);
   
   /**
    * Met à jour un objet document dans la persistance<br>
    * <u>méthode SQL natif</u>
    * @param document entité à mettre à jour
    */  
   void updateSQL(Document document);

}
