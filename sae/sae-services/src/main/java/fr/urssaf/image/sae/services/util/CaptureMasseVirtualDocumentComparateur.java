package fr.urssaf.image.sae.services.util;

import java.util.Comparator;

import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseVirtualDocument;

/**
 * Permet de comparer 2 CaptureMasseVirtualDocument par leur index (position
 * dans le fichier sommaire.xml)
 */
public class CaptureMasseVirtualDocumentComparateur implements
      Comparator<CaptureMasseVirtualDocument> {

   @Override
   public final int compare(CaptureMasseVirtualDocument doc1,
         CaptureMasseVirtualDocument doc2) {
      if (doc1.getIndex() < doc2.getIndex()) {
         return -1;
      } else if (doc1.getIndex() > doc2.getIndex()) {
         return 1;
      } else {
         return 0;
      }
   }

}
