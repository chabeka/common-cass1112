package fr.urssaf.image.sae.services.batch.common.model;

public class XMLEventReference {

   /**
    * Numéro colonne
    */
   private int columnNumber;

   /**
    * Numéro ligne
    */
   private int lineNumber;

   /**
    * Offset du premier charactère
    */
   private int characterOffset;

   /**
    * Identifiant publique
    */
   private String publicId;

   /**
    * Identifiant système
    */
   private String systemId;

   /**
    * Constructeur.
    * 
    * @param columnNumber
    *           Numéro colonne
    * @param lineNumber
    *           Numéro ligne
    * @param characterOffset
    *           Offset du premier charactère
    * @param publicId
    *           Identifiant publique
    * @param systemId
    *           Identifiant système
    */
   public XMLEventReference(int columnNumber, int lineNumber,
         int characterOffset, String publicId, String systemId) {
      super();
      this.columnNumber = columnNumber;
      this.lineNumber = lineNumber;
      this.characterOffset = characterOffset;
      this.publicId = publicId;
      this.systemId = systemId;
   }

   /**
    * Getter
    * 
    * @return the columnNumber
    */
   public int getColumnNumber() {
      return columnNumber;
   }

   /**
    * Setter
    * 
    * @param columnNumber
    *           the columnNumber to set
    */
   public void setColumnNumber(int columnNumber) {
      this.columnNumber = columnNumber;
   }

   /**
    * Getter
    * 
    * @return the lineNumber
    */
   public int getLineNumber() {
      return lineNumber;
   }

   /**
    * Setter
    * 
    * @param lineNumber
    *           the lineNumber to set
    */
   public void setLineNumber(int lineNumber) {
      this.lineNumber = lineNumber;
   }

   /**
    * Getter
    * 
    * @return the publicId
    */
   public String getPublicId() {
      return publicId;
   }

   /**
    * Setter
    * 
    * @param publicId
    *           the publicId to set
    */
   public void setPublicId(String publicId) {
      this.publicId = publicId;
   }

   /**
    * Getter
    * 
    * @return the systemId
    */
   public String getSystemId() {
      return systemId;
   }

   /**
    * Setter
    * 
    * @param systemId
    *           the systemId to set
    */
   public void setSystemId(String systemId) {
      this.systemId = systemId;
   }

   /**
    * Getter
    * 
    * @return the characterOffset
    */
   public int getCharacterOffset() {
      return characterOffset;
   }

   /**
    * Setter
    * 
    * @param characterOffset
    *           the characterOffset to set
    */
   public void setCharacterOffset(int characterOffset) {
      this.characterOffset = characterOffset;
   }

}
