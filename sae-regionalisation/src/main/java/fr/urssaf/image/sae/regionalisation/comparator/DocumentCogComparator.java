/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.comparator;

import java.util.Comparator;

import net.docubase.toolkit.model.document.Document;

/**
 * 
 * 
 */
public class DocumentCogComparator implements Comparator<Document> {

   /**
    * {@inheritDoc}
    */
   @Override
   public final int compare(Document original, Document other) {

      String cogOriginal = (String) original.getSingleCriterion("cog")
            .getWord();
      String cogOther = (String) other.getSingleCriterion("cog").getWord();

      return cogOriginal.compareTo(cogOther);
   }

}
