package fr.urssaf.image.sae.format.referentiel.model;

/**
 * Bean permettant de stocker le contenu d’une ligne de la Colonie Family
 * Referentielformat
 * 
 * */
public class FormatFichier {

   private String idFormat;
   private String typeMime;
   private String extension;
   private String description;
   private boolean visualisable;
   private String validator;
   private String identificateur;
   private String convertisseur;

   /**
    * @return Identifiant du format de fichier défini par le CIRTIL
    */
   public final String getIdFormat() {
      return idFormat;
   }

   /**
    * @param idFormat
    *           Identifiant du format de fichier défini par le CIRTIL
    */
   public final void setIdFormat(String idFormat) {
      this.idFormat = idFormat;
   }

   /**
    * @return Le type-mime du format de fichier
    */
   public final String getTypeMime() {
      return typeMime;
   }

   /**
    * @param typeMime
    *           : Le type-mime du format de fichier
    */
   public final void setTypeMime(String typeMime) {
      this.typeMime = typeMime;
   }

   /**
    * @return L’extension du fichier
    */
   public final String getExtension() {
      return extension;
   }

   /**
    * @param extension
    *           L’extension du fichier
    */
   public final void setExtension(String extension) {
      this.extension = extension;
   }

   /**
    * @return Une description sur le format de fichier
    */
   public final String getDescription() {
      return description;
   }

   /**
    * @param description
    *           Une description sur le format de fichier
    */
   public final void setDescription(String description) {
      this.description = description;
   }

   /**
    * @return Un flag pour indiquer si le format de fichier permet une
    *         visualisation à l’écran ou pas.
    */
   public final boolean isVisualisable() {
      return visualisable;
   }

   /**
    * @param visualisable
    *           : Un flag pour indiquer si le format de fichier permet une
    *           visualisation à l’écran ou pas.
    */
   public final void setVisualisable(boolean visualisable) {
      this.visualisable = visualisable;
   }

   /**
    * @return Le nom du bean à utiliser pour effectuer la validation du fichier
    */
   public final String getValidator() {
      return validator;
   }

   /**
    * @param validator
    *           : Le nom du bean à utiliser pour effectuer la validation du
    *           fichier
    */
   public final void setValidator(String validator) {
      this.validator = validator;
   }

   /**
    * @return Le nom du bean à utiliser pour effectuer l’identification d’un
    *         format de fichier
    */
   public final String getIdentificateur() {
      return identificateur;
   }

   /**
    * @param identificateur
    *           : Le nom du bean à utiliser pour effectuer l’identification d’un
    *           format de fichier
    */
   public final void setIdentificateur(String identificateur) {
      this.identificateur = identificateur;
   }

   /**
    * Getter sur le convertisseur.
    * 
    * @return Le nom du bean à utiliser pour effectuer une conversion dans un
    *         format affichable
    */
   public final String getConvertisseur() {
      return convertisseur;
   }

   /**
    * Setter sur le convertisseur.
    * 
    * @param convertisseur
    *           nom du bean à utiliser pour effectuer une conversion dans un
    *           format affichable
    */
   public final void setConvertisseur(String convertisseur) {
      this.convertisseur = convertisseur;
   }

}
