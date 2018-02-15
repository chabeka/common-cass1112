package fr.urssaf.image.commons.dao.spring.exemple.dao.impl;

import java.util.Date;

import fr.urssaf.image.commons.dao.spring.exemple.modele.Auteur;
import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;

/**
 * 
 */
public final class DocumentUtil {
   
   private DocumentUtil(){
      
   }

   /**
    * 
    * @param obj
    * @return
    */
   protected static Document getDocument(Object[] obj) {

      Document document = new Document();
      document.setId((Integer) obj[0]);
      document.setTitre((String) obj[1]);
      document.setDate((Date) obj[2]);

      if (obj[4] != null) {

         Auteur auteur = new Auteur();
         auteur.setId((Integer) obj[3]);
         auteur.setNom((String) obj[4]);

         document.setAuteur(auteur);

      }

      return document;
   }
}
