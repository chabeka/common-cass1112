/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe de modèle d'un PAGMp
 * Annotation pour Mapping avec la table cql
 */
@Table(name = "droitpagmpcql")
public class Pagmp implements Comparable<Pagmp> {

  /** identifiant unique du PAGMp */
  @PartitionKey
  @Column(name = "code")
  private String code;

  /** code du PRMD correspondant */
  @Column(name = "prmd")
  private String prmd;

  /** description du référentiel du PRMD */
  @Column(name = "description")
  private String description;

  /**
   * @return l'identifiant unique du PAGMp
   */
  public final String getCode() {
    return code;
  }

  /**
   * @param code
   *           identifiant unique du PAGMp
   */
  public final void setCode(final String code) {
    this.code = code;
  }

  /**
   * @return le code du PRMD
   */

  public final String getPrmd() {
    return prmd;
  }

  /**
   * @param prmd
   *           code du PRMD
   */
  public final void setPrmd(final String prmd) {
    this.prmd = prmd;
  }

  /**
   * @return the description
   */
  public final String getDescription() {
    return description;
  }

  /**
   * @param description
   *           the description to set
   */
  public final void setDescription(final String description) {
    this.description = description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean equals(final Object obj) {
    boolean areEquals = false;

    if (obj instanceof Pagmp) {
      final Pagmp pagmp = (Pagmp) obj;
      areEquals = code.equals(pagmp.getCode())
          && description.equals(pagmp.getDescription())
          && prmd.equals(pagmp.getPrmd());
    }

    return areEquals;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int hashCode() {
    return super.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String toString() {
    return "code : " + code + "\ndescription : " + description + "\nprmd : "
        + prmd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final Pagmp o) {
    return getCode().compareTo(o.getCode());
  }

}
