package fr.urssaf.image.sae.metadata.referential.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.javers.core.metamodel.annotation.Id;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

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
 * <li>clientAvailable : True si la métadonnée est mise à disposition des
 * clients. False sinon</li>
 * </ul>
 */
@SuppressWarnings( { "PMD.LongVariable", "PMD.TooManyFields" })
@Table(name = "metadatacql")
public class MetadataReference implements Serializable, Comparable<MetadataReference> {



  /**
   * Version de la serialisation
   */
  private static final long serialVersionUID = 1L;

  @PartitionKey
  @Column(name = "longCode")
  @Id // Comparaison Javers
  private String longCode;

  @Column(name = "sCode")
  private String shortCode;

  @Column(name = "type")
  private String type;

  @Column(name = "reqArch")
  private Boolean requiredForArchival;

  @Column(name = "reqStor")
  private Boolean requiredForStorage;

  @Column(name = "length")
  private Integer length;

  @Column(name = "pattern")
  private String pattern;

  @Column(name = "cons")
  private Boolean consultable;

  @Column(name = "defCons")
  private Boolean defaultConsultable;

  @Column(name = "search")
  private Boolean searchable;

  @Column(name = "int")
  private Boolean internal;

  @Column(name = "arch")
  private Boolean archivable;

  @Column(name = "label")
  private String label;

  @Column(name = "descr")
  private String description;

  @Column(name = "hasDict")
  private Boolean hasDictionary;

  @Column(name = "dictName")
  private String dictionaryName;

  @Column(name = "indexed")
  private Boolean isIndexed;

  @Column(name = "modif")
  private Boolean modifiable;

  @Column(name = "dispo")
  private Boolean clientAvailable;

  @Column(name = "leftTrim")
  private Boolean leftTrimable;

  @Column(name = "rightTrim")
  private Boolean rightTrimable;

  @Column(name = "transf")
  private Boolean transferable;

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
  public final void setLongCode(final String longCode) {
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
    searchable = isSearchable;
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
    internal = isInternal;
  }

  /**
   * @param isArchivable
   *           : True si la métadonnée est interrogeable par l'utilisateur
   *           sinon False.
   */
  public final void setArchivable(final Boolean isArchivable) {
    archivable = isArchivable;
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
                                                                                                                                                                                                                                                                       "internal", internal).append("clientAvailable",
                                                                                                                                                                                                                                                                                                    clientAvailable).append("leftTrim", leftTrimable).append(
                                                                                                                                                                                                                                                                                                                                                             "rightTrim", rightTrimable).toString();
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
  public final void setHasDictionary(final Boolean hasDictionary) {
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
  public final void setDictionaryName(final String dictionaryName) {
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
  public final void setIsIndexed(final Boolean isIndexed) {
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
  public final void setModifiable(final Boolean modifiable) {
    this.modifiable = modifiable;
  }

  /**
   * Permet de récuperer l'indicateur de mise à disposition client.
   * 
   * @return indicateur de mise à disposition client
   */
  public final Boolean isClientAvailable() {
    return clientAvailable;
  }

  /**
   * Permet de modifier l'indicateur de mise à disposition client.
   * 
   * @param clientAvailable
   *           indicateur de mise à disposition client
   */
  public final void setClientAvailable(final Boolean clientAvailable) {
    this.clientAvailable = clientAvailable;
  }

  /**
   * @return the leftTrimable
   */
  public final Boolean isLeftTrimable() {
    return leftTrimable;
  }

  /**
   * @param leftTrimable
   *           the leftTrimable to set
   */
  public final void setLeftTrimable(final Boolean leftTrimable) {
    this.leftTrimable = leftTrimable;
  }

  /**
   * @return the rightTrimable
   */
  public final Boolean isRightTrimable() {
    return rightTrimable;
  }

  /**
   * @param rightTrimable
   *           the rightTrimable to set
   */
  public final void setRightTrimable(final Boolean rightTrimable) {
    this.rightTrimable = rightTrimable;
  }

  /**
   * @param transferable the transferable to set
   */
  public final void setTransferable(final Boolean transferable) {
    this.transferable = transferable;
  }

  /**
   * @return the transferable
   */
  public final Boolean getTransferable() {
    return transferable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final MetadataReference o) {

    return getLongCode().compareTo(o.getLongCode());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (archivable == null ? 0 : archivable.hashCode());
    result = prime * result + (clientAvailable == null ? 0 : clientAvailable.hashCode());
    result = prime * result + (consultable == null ? 0 : consultable.hashCode());
    result = prime * result + (defaultConsultable == null ? 0 : defaultConsultable.hashCode());
    result = prime * result + (description == null ? 0 : description.hashCode());
    result = prime * result + (dictionaryName == null ? 0 : dictionaryName.hashCode());
    result = prime * result + (hasDictionary == null ? 0 : hasDictionary.hashCode());
    result = prime * result + (internal == null ? 0 : internal.hashCode());
    result = prime * result + (isIndexed == null ? 0 : isIndexed.hashCode());
    result = prime * result + (label == null ? 0 : label.hashCode());
    result = prime * result + (leftTrimable == null ? 0 : leftTrimable.hashCode());
    result = prime * result + (length == null ? 0 : length.hashCode());
    result = prime * result + (longCode == null ? 0 : longCode.hashCode());
    result = prime * result + (modifiable == null ? 0 : modifiable.hashCode());
    result = prime * result + (pattern == null ? 0 : pattern.hashCode());
    result = prime * result + (requiredForArchival == null ? 0 : requiredForArchival.hashCode());
    result = prime * result + (requiredForStorage == null ? 0 : requiredForStorage.hashCode());
    result = prime * result + (rightTrimable == null ? 0 : rightTrimable.hashCode());
    result = prime * result + (searchable == null ? 0 : searchable.hashCode());
    result = prime * result + (shortCode == null ? 0 : shortCode.hashCode());
    result = prime * result + (transferable == null ? 0 : transferable.hashCode());
    result = prime * result + (type == null ? 0 : type.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MetadataReference other = (MetadataReference) obj;
    if (archivable == null) {
      if (other.archivable != null) {
        return false;
      }
    } else if (!archivable.equals(other.archivable)) {
      return false;
    }
    if (clientAvailable == null) {
      if (other.clientAvailable != null) {
        return false;
      }
    } else if (!clientAvailable.equals(other.clientAvailable)) {
      return false;
    }
    if (consultable == null) {
      if (other.consultable != null) {
        return false;
      }
    } else if (!consultable.equals(other.consultable)) {
      return false;
    }
    if (defaultConsultable == null) {
      if (other.defaultConsultable != null) {
        return false;
      }
    } else if (!defaultConsultable.equals(other.defaultConsultable)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (dictionaryName == null) {
      if (other.dictionaryName != null) {
        return false;
      }
    } else if (!dictionaryName.equals(other.dictionaryName)) {
      return false;
    }
    if (hasDictionary == null) {
      if (other.hasDictionary != null) {
        return false;
      }
    } else if (!hasDictionary.equals(other.hasDictionary)) {
      return false;
    }
    if (internal == null) {
      if (other.internal != null) {
        return false;
      }
    } else if (!internal.equals(other.internal)) {
      return false;
    }
    if (isIndexed == null) {
      if (other.isIndexed != null) {
        return false;
      }
    } else if (!isIndexed.equals(other.isIndexed)) {
      return false;
    }
    if (label == null) {
      if (other.label != null) {
        return false;
      }
    } else if (!label.equals(other.label)) {
      return false;
    }
    if (leftTrimable == null) {
      if (other.leftTrimable != null) {
        return false;
      }
    } else if (!leftTrimable.equals(other.leftTrimable)) {
      return false;
    }
    if (length == null) {
      if (other.length != null) {
        return false;
      }
    } else if (!length.equals(other.length)) {
      return false;
    }
    if (longCode == null) {
      if (other.longCode != null) {
        return false;
      }
    } else if (!longCode.equals(other.longCode)) {
      return false;
    }
    if (modifiable == null) {
      if (other.modifiable != null) {
        return false;
      }
    } else if (!modifiable.equals(other.modifiable)) {
      return false;
    }
    if (pattern == null) {
      if (other.pattern != null) {
        return false;
      }
    } else if (!pattern.equals(other.pattern)) {

      return false;
    }
    if (requiredForArchival == null) {
      if (other.requiredForArchival != null) {
        return false;
      }
    } else if (!requiredForArchival.equals(other.requiredForArchival)) {
      return false;
    }
    if (requiredForStorage == null) {
      if (other.requiredForStorage != null) {
        return false;
      }
    } else if (!requiredForStorage.equals(other.requiredForStorage)) {
      return false;
    }
    if (rightTrimable == null) {
      if (other.rightTrimable != null) {
        return false;
      }
    } else if (!rightTrimable.equals(other.rightTrimable)) {
      return false;
    }
    if (searchable == null) {
      if (other.searchable != null) {
        return false;
      }
    } else if (!searchable.equals(other.searchable)) {
      return false;
    }
    if (shortCode == null) {
      if (other.shortCode != null) {
        return false;
      }
    } else if (!shortCode.equals(other.shortCode)) {
      return false;
    }
    if (transferable == null) {
      if (other.transferable != null) {
        return false;
      }
    } else if (!transferable.equals(other.transferable)) {
      return false;
    }
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      return false;
    }
    return true;
  }
}
