package fr.urssaf.image.sae.rnd.modele;

import org.javers.core.metamodel.annotation.Id;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;


/**
 * Classe représentant un type de document
 * 
 * 
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
@Table(name = "rndcql")
public class TypeDocument implements Comparable<TypeDocument> {

  /**
   * Code du type de document (unique, ex : 1.C.X.X.X)
   */
  @PartitionKey
  @Column(name = "code")
  @Id
  private String code;

  /**
   * Fonction correspondante
   */
  @Column(name = "codeFonction")
  private String codeFonction;

  /**
   * Activité correspondante (peut être nulle)
   */
  @Column(name = "codeActivite")
  private String codeActivite;

  /**
   * Libellé du type de document (ex : AR DES DONNEES ADMINISTRATIVES)
   */
  @Column(name = "libelleEnd")
  private String libelle;

  /**
   * Nombre de jours pendant lequel les documents doivent être conservés
   */
  @Column(name = "dureeConservation")
  private int dureeConservation;

  /**
   * Clôturé ou non (un document dont le type de document est cloturé ne peut
   * plus entrer dans le SAE)
   */
  @Column(name = "cloture")
  private boolean cloture;

  /**
   * Type du code RND (ARCHIVABLE_AED, NON_ARCHIVABLE_AED, TEMPORAIRE)
   */

  private TypeCode type;

  /**
   * @return the code
   */
  public final String getCode() {
    return code;
  }

  /**
   * @param code
   *           the code to set
   */
  public final void setCode(final String code) {
    this.code = code;
  }

  /**
   * @return the codeFonction
   */
  public final String getCodeFonction() {
    return codeFonction;
  }

  /**
   * @param codeFonction
   *           the codeFonction to set
   */
  public final void setCodeFonction(final String codeFonction) {
    this.codeFonction = codeFonction;
  }

  /**
   * @return the codeActivite
   */
  public final String getCodeActivite() {
    return codeActivite;
  }

  /**
   * @param codeActivite
   *           the codeActivite to set
   */
  public final void setCodeActivite(final String codeActivite) {
    this.codeActivite = codeActivite;
  }

  /**
   * @return the libelle
   */
  public final String getLibelle() {
    return libelle;
  }

  /**
   * @param libelle
   *           the libelle to set
   */
  public final void setLibelle(final String libelle) {
    this.libelle = libelle;
  }

  /**
   * @return the dureeConservation
   */
  public final int getDureeConservation() {
    return dureeConservation;
  }

  /**
   * @param dureeConservation
   *           the dureeConservation to set
   */
  public final void setDureeConservation(final int dureeConservation) {
    this.dureeConservation = dureeConservation;
  }

  /**
   * @return the cloture
   */
  public final boolean isCloture() {
    return cloture;
  }

  /**
   * @param cloture
   *           the cloture to set
   */
  public final void setCloture(final boolean cloture) {
    this.cloture = cloture;
  }

  /**
   * @return the type
   */
  public final TypeCode getType() {
    return type;
  }

  /**
   * @param type
   *           the type to set
   */
  public final void setType(final TypeCode type) {
    this.type = type;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (cloture ? 1231 : 1237);
    result = prime * result + (code == null ? 0 : code.hashCode());
    result = prime * result
        + (codeActivite == null ? 0 : codeActivite.hashCode());
    result = prime * result
        + (codeFonction == null ? 0 : codeFonction.hashCode());
    result = prime * result + dureeConservation;
    result = prime * result + (libelle == null ? 0 : libelle.hashCode());
    result = prime * result + (type == null ? 0 : type.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public final boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TypeDocument other = (TypeDocument) obj;
    if (cloture != other.cloture) {
      return false;
    }
    if (code == null) {
      if (other.code != null) {
        return false;
      }
    } else if (!code.equals(other.code)) {
      return false;
    }
    if (codeActivite == null) {
      if (other.codeActivite != null) {
        return false;
      }
    } else if (!codeActivite.equals(other.codeActivite)) {
      return false;
    }
    if (codeFonction == null) {
      if (other.codeFonction != null) {
        return false;
      }
    } else if (!codeFonction.equals(other.codeFonction)) {
      return false;
    }
    if (dureeConservation != other.dureeConservation) {
      return false;
    }
    if (libelle == null) {
      if (other.libelle != null) {
        return false;
      }
    } else if (!libelle.equals(other.libelle)) {
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

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final TypeDocument o) {
    return getCode().compareTo(o.getCode());
  }


}
