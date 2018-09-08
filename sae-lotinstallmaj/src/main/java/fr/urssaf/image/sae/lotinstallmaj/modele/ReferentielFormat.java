/**
 * 
 */
package fr.urssaf.image.sae.lotinstallmaj.modele;

/**
 * 
 * 
 */
public class ReferentielFormat {

   /**
    * Description
    */
   private String description;

   /**
    * Extension
    */
   private String extension;

   /**
    * Type mime
    */
   private String typeMime;

   /**
    * Visualisable
    */
   private Boolean visualisable;

   /**
    * Autorisé en GED
    */
   private Boolean autoriseEnGed;

   /**
    * Identifier
    */
   private String identifier;

   /**
    * Validateur
    */
   private String validator;

   /**
    * Convertisseur
    */
   private String convertisseur;

   /**
    * Constructeur
    * 
    * @param description Description
    * @param extension Extension
    * @param typeMime Type mime
    * @param visualisable Visualisable
    */
   public ReferentielFormat(String description, String extension,
         String typeMime, Boolean visualisable) {
      this(description, extension, typeMime, visualisable, null, null, null, null);
   }

   /**
    * Constructeur
    * 
    * @param description Description
    * @param extension Extension
    * @param typeMime Type mime
    * @param visualisable Visualisable
    * @param autoriseEnGed Autorisé en GED
    */
   public ReferentielFormat(String description, String extension,
         String typeMime, Boolean visualisable, Boolean autoriseEnGed) {
      this(description, extension, typeMime, visualisable, autoriseEnGed, null, null, null);
   }

   /**
    * Constructeur
    *
    * @param description Description
    * @param extension Extension
    * @param typeMime Type mime
    * @param visualisable Visualisable
    * @param autoriseEnGed Autorisé en GED
    * @param identifier Indentifier
    * @param validator Validateur
    * @param convertisseur Convertisseur
    */
   public ReferentielFormat(String description, String extension,
         String typeMime, Boolean visualisable, Boolean autoriseEnGed,
         String identifier, String validator, String convertisseur) {
      this.description = description;
      this.extension = extension;
      this.typeMime = typeMime;
      this.visualisable = visualisable;
      this.autoriseEnGed = autoriseEnGed;
      this.identifier = identifier;
      this.validator = validator;
      this.convertisseur = convertisseur;
   }

   /**
    * Getter pour description
    * 
    * @return the description
    */
   public String getDescription() {
      return description;
   }

   /**
    * Setter pour description
    * 
    * @param description
    *           the description to set
    */
   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Getter pour extension
    * 
    * @return the extension
    */
   public String getExtension() {
      return extension;
   }

   /**
    * Setter pour extension
    * 
    * @param extension
    *           the extension to set
    */
   public void setExtension(String extension) {
      this.extension = extension;
   }

   /**
    * Getter pour typeMime
    * 
    * @return the typeMime
    */
   public String getTypeMime() {
      return typeMime;
   }

   /**
    * Setter pour typeMime
    * 
    * @param typeMime
    *           the typeMime to set
    */
   public void setTypeMime(String typeMime) {
      this.typeMime = typeMime;
   }

   /**
    * Getter pour visualisable
    * 
    * @return the visualisable
    */
   public Boolean getVisualisable() {
      return visualisable;
   }

   /**
    * Setter pour visualisable
    * 
    * @param visualisable
    *           the visualisable to set
    */
   public void setVisualisable(Boolean visualisable) {
      this.visualisable = visualisable;
   }

   /**
    * Getter pour autoriseEnGed
    * 
    * @return the autoriseEnGed
    */
   public Boolean getAutoriseEnGed() {
      return autoriseEnGed;
   }

   /**
    * Setter pour autoriseEnGed
    * 
    * @param autoriseEnGed
    *           the autoriseEnGed to set
    */
   public void setAutoriseEnGed(Boolean autoriseEnGed) {
      this.autoriseEnGed = autoriseEnGed;
   }

   /**
    * Getter pour identifier
    * 
    * @return the identifier
    */
   public String getIdentifier() {
      return identifier;
   }

   /**
    * Setter pour identifier
    * 
    * @param identifier
    *           the identifier to set
    */
   public void setIdentifier(String identifier) {
      this.identifier = identifier;
   }

   /**
    * Getter pour validator
    * 
    * @return the validator
    */
   public String getValidator() {
      return validator;
   }

   /**
    * Setter pour validator
    * 
    * @param validator
    *           the validator to set
    */
   public void setValidator(String validator) {
      this.validator = validator;
   }

   /**
    * Getter pour convertisseur
    * 
    * @return the convertisseur
    */
   public String getConvertisseur() {
      return convertisseur;
   }

   /**
    * Setter pour convertisseur
    * 
    * @param convertisseur
    *           the convertisseur to set
    */
   public void setConvertisseur(String convertisseur) {
      this.convertisseur = convertisseur;
   }

}
