/**
 * 
 */
package fr.urssaf.image.tests.dfcetest.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import fr.urssaf.image.tests.dfcetest.Categories;

/**
 * Générateur de document Docubase. 
 * Chaque document généré est automatiquement inséré dans Docubase.
 */
public class DocGen {
   public static List<Document> storedDocuments;
   public Map<String, Object> metadata;
   private String title;
   private Base base;

   {
      storedDocuments = new ArrayList<Document>();
   }
   
   public DocGen(Base base) {
      this.base = base;
      this.metadata = new HashMap<String, Object>();
   }
   
   /**
    * Affecte un titre aléatoire tel que : fixedPart + System.nanoTime();
    * @param fixedPart
    */
   public DocGen setRandomTitle(String fixedPart) {
      this.title = fixedPart + System.nanoTime();
      return this;
   }
   
   public DocGen setTitle(String title) {
      this.title = title;
      return this;
   }
   
   public String getTitle() {
      return title;
   }
   
   public void put(Categories category, Object value) {
      metadata.put(category.toString(), value);
   }
   
   /**
    * Insère un document dans Docubase avec des metadonnées aléatoires sauf
    * celles spécifiées via {@link DocGen#put(Categories, Object)}
    * @return
    */
   public Document store() {
      Map<String, Object> randomMetadata = DocubaseHelper.getCategoriesRandomValues(title);
      randomMetadata.putAll(metadata);
      Document doc = DocubaseHelper.storeOneDoc(base, randomMetadata);
      storedDocuments.add(doc);
      return doc;      
   }
   
   /**
    * Insère plusieurs documents. Les documents ont le même titre. 
    * @param nbDocs Nombre de documents à insérer
    * @return Liste des documents insérés dans Docubase
    */
   public List<Document> storeMany(int nbDocs) {
      List<Document> docs = new ArrayList<Document>(); 
      
      for (int i = 0; i < nbDocs; i++) {
         Document doc = store();
         docs.add(doc);
      }
      return docs;
   }   
}