package fr.urssaf.image.sae.droit.dao.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Bean permettant de stocker le contenu d'une ligne de la CF DroitPagmf.
 * Annotation pour Mapping avec la table cql
 */
@Table(name = "droitpagmfcql")
public class Pagmf implements Comparable<Pagmf> {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(Pagmf.class);
  @PartitionKey
  @Column(name = "codePagmf")
  private String codePagmf;

  @Column(name = "description")
  private String description;

  @Column(name = "codeFormatControlProfil")
  private String codeFormatControlProfil;
  /**
   * @return identifiant du Pagmf
   */
  public final String getCodePagmf() {
    return codePagmf;
  }
  /**
   * @param codePagmf identifiant du Pagmf à setter
   */
  public final void setCodePagmf(final String codePagmf) {
    this.codePagmf = codePagmf;
  }
  /**
   * @return la description du contrôle
   */
  public final String getDescription() {
    return description;
  }
  /**
   * @param description la description du contrôle to set
   */
  public final void setDescription(final String description) {
    this.description = description;
  }
  /**
   * @return code du profil de contrôle contenant les paramètres à sa mise en oeuvre.
   */
  public final String getCodeFormatControlProfil() {
    return codeFormatControlProfil;
  }
  /**
   * @param codeFormatControlProfil du profil de contrôle contenant les paramètres à sa mise en oeuvre. to set
   */
  public final void setCodeFormatControlProfil(final String codeFormatControlProfil) {
    this.codeFormatControlProfil = codeFormatControlProfil;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (codeFormatControlProfil == null ? 0 : codeFormatControlProfil.hashCode());
    result = prime * result + (codePagmf == null ? 0 : codePagmf.hashCode());
    result = prime * result + (description == null ? 0 : description.hashCode());
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
    final Pagmf other = (Pagmf) obj;
    if (codeFormatControlProfil == null) {
      if (other.codeFormatControlProfil != null) {
        return false;
      }
    } else if (!codeFormatControlProfil.equals(other.codeFormatControlProfil)) {
      return false;
    }
    if (codePagmf == null) {
      if (other.codePagmf != null) {
        return false;
      }
    } else if (!codePagmf.equals(other.codePagmf)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final Pagmf o) {

    return getCodePagmf().compareTo(o.getCodePagmf());
  }

}
