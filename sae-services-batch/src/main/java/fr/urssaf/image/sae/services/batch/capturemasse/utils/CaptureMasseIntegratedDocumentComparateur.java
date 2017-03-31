package fr.urssaf.image.sae.services.batch.capturemasse.utils;

import java.util.Comparator;

import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;

/**
 * Permet de comparer 2 CaptureMasseIntegratedDocument par leur index (position
 * dans le fichier sommaire.xml)
 */
public class CaptureMasseIntegratedDocumentComparateur implements
      Comparator<TraitementMasseIntegratedDocument> {

   @Override
   public final int compare(TraitementMasseIntegratedDocument doc1,
         TraitementMasseIntegratedDocument doc2) {
      if (doc1.getIndex() < doc2.getIndex()) {
         return -1;
      } else if (doc1.getIndex() > doc2.getIndex()) {
         return 1;
      } else {
         return 0;
      }
   }

}
