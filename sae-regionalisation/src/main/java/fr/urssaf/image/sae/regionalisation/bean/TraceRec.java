package fr.urssaf.image.sae.regionalisation.bean;

import java.math.BigDecimal;

/**
 * Classe représentant un objet de trace de recherches
 * 
 * 
 */
public class TraceRec {

   private BigDecimal idSearch;

   private int nbreDoc;

   private boolean maj;

   /**
    * @return identifiant du critère de recherche
    */
   public final BigDecimal getIdSearch() {
      return idSearch;
   }

   /**
    * @param idSearch
    *           identifiant du critère de recherche
    */
   public final void setIdSearch(BigDecimal idSearch) {
      this.idSearch = idSearch;
   }

   /**
    * @return nombre de documents associés à la recherche
    */
   public final int getNbreDoc() {
      return nbreDoc;
   }

   /**
    * @param nbreDoc
    *           nombre de documents associés à la recherche
    */
   public final void setNbreDoc(int nbreDoc) {
      this.nbreDoc = nbreDoc;
   }

   /**
    * 
    * @param maj
    *           <code>true</code> si le mode est MISE_A_JOUR, <code>false</code>
    *           si TIR_A_BLANC
    */
   public final void setMaj(boolean maj) {
      this.maj = maj;
   }

   /**
    * 
    * @return <code>true</code> si le mode est MISE_A_JOUR, <code>false</code>
    *         si TIR_A_BLANC
    */
   public final boolean isMaj() {
      return maj;
   }

}
