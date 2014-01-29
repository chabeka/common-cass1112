package fr.urssaf.image.sae.metadata.referential.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Cette classe représente une métadonnée du référentiel des métadonnées. <BR />
 * Elle contient les attributs :
 * <ul>
 * <li>shortCode : Le code court.</li>
 * <li>longCode : Le code long.</li>
 * <li>type : Le type de la métadonnée.</li>
 * <li>requiredForArchival : True si la métadonnée est obligatoire à l'archivage
 * sinon False.</li>
 * <li>requiredForStorage : True si la métadonnée est obligatoire au stockage
 * sinon False.</li>
 * <li>length : La longueur maximal de la valeur de la métadonnée.</li>
 * <li>pattern : Le motif que la valeur de la métadonnée doit respecter.</li>
 * <li>consultable : True si la métadonnée est consultable par l'utisateur sinon
 * False.</li>
 * <li>searchable : True si la métadonnée est interrogeable par l'utisateur
 * sinon False.</li>
 * <li>defaultConsultable : True si la métadonnée est consultable par
 * l'utisateur par défaut sinon False.</li>
 * <li>archivable : True si la métadonnée est archivable par l'utisateur sinon
 * False.</li>
 * <li>internal : True si la métadonnée est une métadonnée métier sinon False.</li>
 * <li>archivable : True si la métadonnée est une métadonnée archivable sinon
 * False.</li>
 * <li>label : libellé de la métadonnée.</li>
 * <li>description : description de la métadonnée</li>
 * <li>hasDictionary : True si la métadonnée est rattachée à un dictionnaire.
 * False sinon</li>
 * <li>dictionaryName : nom du dictionnaire associé</li>
 * <li>isIndexed : True si la métadonnée est indexée. False sinon</li>
 * <li>modifiable : True si la métadonnée est modifiable. False sinon</li>
 * </ul>
 */
@SuppressWarnings( { "PMD.LongVariable", "PMD.TooManyFields" })
public class MetadataReference implements Serializable {

   /**
    * Version de la serialisation
    */
   private static final long serialVersionUID = 1L;
   private String shortCode;
   private String longCode;
   private String type;
   private Boolean requiredForArchival;
   private Boolean requiredForStorage;
   private Integer length;
   private String pattern;
   private Boolean consultable;
   private Boolean defaultConsultable;
   private Boolean searchable;
   private Boolean internal;
   private Boolean archivable;
   private String label;
   private String description;
   private Boolean hasDictionary;
   private String dictionaryName;
   private Boolean isIndexed;
   private Boolean modifiable;

   /**
    * @return Le code court
    */
   public final String getShortCode() {
      return shortCode;
   }

   /**
    * @param shortCode
    *           : Le code court
    */
   public final void setShortCode(final String shortCode) {
      this.shortCode = shortCode;
   }

   /**
    * @return Le code long
    */
   public final String getLongCode() {
      return longCode;
   }

   /**
    * @param longCode
    *           : Le code long
    */
   public final void setLongCode(String longCode) {
      this.longCode = longCode;
   }

   /**
    * @return Le type de la métadonnée
    */
   public final String getType() {
      return type;
   }

   /**
    * @param type
    *           Le type de la métadonnée
    */
   public final void setType(final String type) {
      this.type = type;
   }

   /**
    * @return La longueur maximal de la valeur de la métadonnée.
    */
   public final int getLength() {
      return length;
   }

   /**
    * @param length
    *           : La longueur maximal de la valeur de la métadonnée.
    * 
    */
   public final void setLength(final int length) {
      this.length = length;
   }

   /**
    * @return Le motif que la valeur de la métadonnée doit respecter.
    */
   public final String getPattern() {
      return pattern;
   }

   /**
    * @param pattern
    *           : Le motif que la valeur de la métadonnée doit respecter.
    */
   public final void setPattern(final String pattern) {
      this.pattern = pattern;
   }

   /**
    * @return True si la métadonnée doit être visible par l'utilisateur sinon
    *         False.
    */
   public final boolean isConsultable() {
      return consultable;
   }

   /**
    * @param consultable
    *           : True si la métadonnée est consultable par l'utilisateur sinon
    *           False.
    */
   public final void setConsultable(final boolean consultable) {
      this.consultable = consultable;
   }

   /**
    * @return True si la métadonnée est interrogeable par l'utilisateur sinon
    *         False.
    */
   public final Boolean isSearchable() {
      return searchable;
   }

   /**
    * @param isSearchable
    *           : True si la métadonnée est interrogeable par l'utilisateur
    *           sinon False.
    * 
    */
   public final void setSearchable(final Boolean isSearchable) {
      this.searchable = isSearchable;
   }

   /**
    * @return True si la métadonnée est une métadonnée métier sinon False.
    */
   public final Boolean isInternal() {
      return internal;
   }

   /**
    * @param isInternal
    *           : True si la métadonnée est une métadonnée métier sinon False.
    */
   public final void setInternal(final Boolean isInternal) {
      this.internal = isInternal;
   }

   /**
    * @param isArchivable
    *           : True si la métadonnée est interrogeable par l'utilisateur
    *           sinon False.
    */
   public final void setArchivable(final Boolean isArchivable) {
      this.archivable = isArchivable;
   }

   /**
    * @return True si la métadonnée est interrogeable par l'utilisateur sinon
    *         False.
    */
   public final Boolean isArchivable() {
      return archivable;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {
      return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(
            "shortCode", shortCode).append("longCode", longCode).append(
            "label", label).append("pattern", pattern).append("type", type)
            .append("required", "").append("length", length).append("pattern",
                  pattern).append("consultable", consultable).append(
                  "archivable", archivable).append("requiredForStorage",
                  requiredForStorage).append("requiredForArchival",
                  requiredForArchival).append("defaultConsultable",
                  defaultConsultable).append("searchable", searchable).append(
                  "internal", internal).toString();
   }

   /**
    * 
    * @return True si la métadonnée est requise pour l'archivage.
    */
   public final boolean isRequiredForArchival() {
      return requiredForArchival;
   }

   /**
    * 
    * @param requiredForArchival
    *           : le booleen qui indique si la métadonnée est requise pour
    *           l'archivage.
    */
   public final void setRequiredForArchival(final boolean requiredForArchival) {
      this.requiredForArchival = requiredForArchival;
   }

   /**
    * 
    * @return True si la métadonnée est requise pour le stockage.
    */
   public final boolean isRequiredForStorage() {
      return requiredForStorage;
   }

   /**
    * 
    * @param requiredForStorage
    *           : le booleen qui indique si la métadonnée est requise pour le
    *           stockage.
    */
   public final void setRequiredForStorage(final boolean requiredForStorage) {
      this.requiredForStorage = requiredForStorage;
   }

   /**
    * 
    * @return True si la métadonnée est consultable par défaut.
    */
   public final boolean isDefaultConsultable() {
      return defaultConsultable;
   }

   /**
    * 
    * @param defaultConsultable
    *           : le booleen qui indique si la métadonnée consultable par
    *           défaut.
    */
   public final void setDefaultConsultable(final boolean defaultConsultable) {
      this.defaultConsultable = defaultConsultable;
   }

   /**
    * @param label
    *           : Le libellé.
    */
   public final void setLabel(final String label) {
      this.label = label;
   }

   /**
    * @return Le libellé
    */
   public final String getLabel() {
      return label;
   }

   /**
    * @param description
    *           : Le descriptif
    */
   public final void setDescription(final String description) {
      this.description = description;
   }

   /**
    * @return Le descriptif
    */
   public final String getDescription() {
      return description;
   }

   /**
    * @return renvoie vrai/faux suivant si la métadonnée est soumise à un
    *         dictionnaire
    */
   public final Boolean getHasDictionary() {
      return hasDictionary;
   }

   /**
    * @param hasDictionary
    *           indique si la métadonnée est soumise à un dictionnaire
    */
   public final void setHasDictionary(Boolean hasDictionary) {
      this.hasDictionary = hasDictionary;
   }

   /**
    * @return nom du dictionnaire associé
    */
   public final String getDictionaryName() {
      return dictionaryName;
   }

   /**
    * @param dictionaryName
    *           nom du dictionnaire
    */
   public final void setDictionaryName(String dictionaryName) {
      this.dictionaryName = dictionaryName;
   }

   /**
    * @return renvoie si la métadonnée est indexée
    */
   public final Boolean getIsIndexed() {
      return isIndexed;
   }

   /**
    * @param isIndexed
    *           indique que la métadonnée est indexée
    */
   public final void setIsIndexed(Boolean isIndexed) {
      this.isIndexed = isIndexed;
   }

   /**
    * @return indicateur désignant la métadonnée comme modifiable
    */
   public final Boolean isModifiable() {
      return modifiable;
   }

   /**
    * @param modifiable
    *           indicateur désignant la métadonnée comme modifiable
    */
   public final void setModifiable(Boolean modifiable) {
      this.modifiable = modifiable;
   }
}
