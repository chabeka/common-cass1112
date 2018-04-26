package fr.urssaf.image.commons.xml.castor.modele;

import java.util.ArrayList;
import java.util.List;

public class Documents {
   
   private List<Document> items = new ArrayList<Document>();

   public void setDocuments(List<Document> items) {
      this.items = items;
   }

   public List<Document> getDocuments() {
      return items;
   }

}
