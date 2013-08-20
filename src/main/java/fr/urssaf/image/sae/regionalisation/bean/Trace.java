package fr.urssaf.image.sae.regionalisation.bean;

import java.util.UUID;

/**
 * Classe représentant un objet de trace de mise à jour
 * 
 * 
 */
public class Trace {

   private UUID idDocument;

   private String metaName;

   private String oldValue;

   private String newValue;

   private int lineNumber;

   /**
    * @return identifiant du document modifié
    */
   public final UUID getIdDocument() {
      return idDocument;
   }

   /**
    * @param idDocument
    *           identifiant du document modifié
    */
   public final void setIdDocument(UUID idDocument) {
      this.idDocument = idDocument;
   }

   /**
    * @return nom de la métadonnée modifié
    */
   public final String getMetaName() {
      return metaName;
   }

   /**
    * @param metaName
    *           nom de la métadonnée modifié
    */
   public final void setMetaName(String metaName) {
      this.metaName = metaName;
   }

   /**
    * @return ancienne valeur
    */
   public final String getOldValue() {
      return oldValue;
   }

   /**
    * @param oldValue
    *           ancienne valeur
    */
   public final void setOldValue(String oldValue) {
      this.oldValue = oldValue;
   }

   /**
    * @return nouvelle valeur
    */
   public final String getNewValue() {
      return newValue;
   }

   /**
    * @param newValue
    *           nouvelle valeur
    */
   public final void setNewValue(String newValue) {
      this.newValue = newValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Trace [idDocument=").append(idDocument).append(
            ", metaName=").append(metaName).append(", newValue=").append(
            newValue).append(", oldValue=").append(oldValue).append("]");
      return builder.toString();
   }

   /**
    * @return the lineNumber
    */
   public final int getLineNumber() {
      return lineNumber;
   }

   /**
    * @param lineNumber
    *           the lineNumber to set
    */
   public final void setLineNumber(int lineNumber) {
      this.lineNumber = lineNumber;
   }

}
