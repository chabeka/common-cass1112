package fr.urssaf.image.commons.controller.spring3.jndi.exemple.dao;

import java.util.List;

import fr.urssaf.image.commons.controller.spring3.jndi.exemple.modele.Document;

public interface DocumentDao {

   List<Document> allDocuments();
   
   void save(Document document);
   
   @SuppressWarnings("PMD.ShortVariable") 
   Document find(int id);
}
