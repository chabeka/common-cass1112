package fr.urssaf.image.commons.dao.spring.exemple.dao;

import java.util.List;

import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;

public interface DocumentFindDao {

   /**
    * Renvoie une collection de documents avec leur auteur
    * <u>méthode Criteria</u> 
    * 
    * @return liste de documents
    */
   List<Document> findByCriteria();

   /**
    * Renvoie une collection de documents avec leur auteur
    * <u>méthode SQL natif</u> 
    * 
    * @return liste de documents
    */
   List<Document> findBySQL();

   /**
    * Renvoie une collection de documents avec leur auteur
    * <u>méthode HQL</u> 
    * 
    * @return liste de documents
    */
   List<Document> findByHQL();

   /**
    * Renvoie une collection de documents triée et paginée.
    * <u>méthode Criteria</u> 
    * 
    * @param firstResult premier element
    * @param maxResult nombre de documents au maximum
    * @param order attribut à trier
    * @param inverse sens du tri
    * @return liste de documents
    */
   List<Document> findByCriteria(int firstResult, int maxResult, String order,
         boolean inverse);

   /**
    * Renvoie une collection de documents triée et paginée.
    * <u>méthode HQL</u> 
    * 
    * @param firstResult premier element
    * @param maxResult nombre de documents au maximum
    * @param order attribut à trier
    * @param inverse sens du tri
    * @return liste de documents
    */
   List<Document> findBySQL(int firstResult, int maxResult, String order,
         boolean inverse);

   /**
    * Renvoie une collection de documents avec leurs états.<br>
    * <u>méthode HQL</u>
    * @return liste de documents
    */
   List<Document> loadEtatsByHQL();
   
   /**
    * Renvoie une collection de documents avec leurs états.<br>
    * <u>méthode Criteria</u>
    * @return liste de documents
    */
   List<Document> loadEtatsByCriteria();
   
   /**
    * Renvoie une collection de documents avec leurs états.<br>
    * <u>méthode avec Hibernate.initialize</u>
    * @return liste de documents
    */
   List<Document> loadEtatsByInitialize();

   /**
    * Utilisation du curseur en Hibernate
    */
   void scroll();

}
