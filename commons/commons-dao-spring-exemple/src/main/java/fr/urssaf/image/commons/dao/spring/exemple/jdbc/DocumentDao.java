package fr.urssaf.image.commons.dao.spring.exemple.jdbc;

import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;


public interface DocumentDao
{
   void save(Document document);

   int count();

   Document find(Integer identifiant);
   
   Document get(Integer identifiant);

}
