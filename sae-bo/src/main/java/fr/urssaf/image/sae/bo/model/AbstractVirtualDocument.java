/**
 * 
 */
package fr.urssaf.image.sae.bo.model;

import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;

/**
 * Classe abstraite contenant les informations de base d'un document virtuel
 * 
 */
public abstract class AbstractVirtualDocument {

   private VirtualReferenceFile reference;

   private int startPage;

   private int endPage;

   private int index;

   /**
    * Constructeur
    */
   public AbstractVirtualDocument() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param reference
    *           le fichier de référence
    * @param startPage
    *           le n° de la page de début
    * @param endPage
    *           le n° de la page de fin
    */
   public AbstractVirtualDocument(VirtualReferenceFile reference,
         int startPage, int endPage) {
      this.reference = reference;
      this.startPage = startPage;
      this.endPage = endPage;
   }

   /**
    * @return le fichier de référence
    */
   public final VirtualReferenceFile getReference() {
      return reference;
   }

   /**
    * @param reference
    *           le fichier de référence
    */
   public final void setReference(VirtualReferenceFile reference) {
      this.reference = reference;
   }

   /**
    * @return le n° de la page de début
    */
   public final int getStartPage() {
      return startPage;
   }

   /**
    * @param startPage
    *           le n° de la page de début
    */
   public final void setStartPage(int startPage) {
      this.startPage = startPage;
   }

   /**
    * @return le n° de la page de fin
    */
   public final int getEndPage() {
      return endPage;
   }

   /**
    * @param endPage
    *           le n° de la page de fin
    */
   public final void setEndPage(int endPage) {
      this.endPage = endPage;
   }

   /**
    * @return index du document de référence
    */
   public final int getIndex() {
      return index;
   }

   /**
    * @param index
    *           index du document de référence
    */
   public final void setIndex(int index) {
      this.index = index;
   }

}
