package fr.urssaf.image.commons.dao.spring.exemple.dao;

import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;

public interface DocumentDao {

   /**
    * Renvoie un objet document persisté.<br>
    * L'identifiant du document est défini dans le mapping Hibernate.<br>  
    * Cherche en session puis en base.
    * 
    * @param identifiant identifiant Hibernate du document
    * @return document persisté
    */
   Document get(Integer identifiant);

   
   /**
    * Renvoie un objet document persisté.<br>
    * L'identifiant du document est défini dans le mapping Hibernate.<br> 
    * Cherche en base.
    * 
    * @param identifiant identifiant Hibernate du document
    * @return document persisté
    */
   Document find(Integer identifiant);
   
   /**
    * Renvoie un objet document persisté.<br>
    * L'identifiant du document correspond à la clé primaire en base de données .<br> 
    * Cherche en base.
    * 
    * @param identifiant clé primaire du document
    * @return document persisté
    */
   Document findSQL(Integer identifiant);
   
   /**
    * Compte le nombre de documents persistés
    * 
    * @return nombre de document persistés
    */
   int count();
   
}
