package fr.urssaf.image.sae.services.util;

import java.util.Comparator;

import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;

/**
 * Permet de comparer 2 CaptureMasseIntegratedDocument par leur index (position
 * dans le fichier sommaire.xml)
 */
public class CaptureMasseIntegratedDocumentComparateur implements
      Comparator<CaptureMasseIntegratedDocument> {

   @Override
   public final int compare(CaptureMasseIntegratedDocument doc1,
         CaptureMasseIntegratedDocument doc2) {
      if (doc1.getIndex() < doc2.getIndex()) {
         return -1;
      } else if (doc1.getIndex() > doc2.getIndex()) {
         return 1;
      } else {
         return 0;
      }
   }

}
