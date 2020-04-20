package fr.urssaf.image.sae.droit.dao.model;

import org.javers.core.metamodel.annotation.Id;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Bean permettant de stocker le contenu d'une ligne de la CF DroitFormatControlProfil
 * Annotation pour Mapping avec la table cql
 */
@Table(name = "droitformatcontrolprofilcql")
public class FormatControlProfil implements Comparable<FormatControlProfil> {

  @PartitionKey
  @Column(name = "formatCode")
  @Id
  private String formatCode;

  @Column(name = "description")
  private String description;

  @Column(name = "controlProfil")
  @Frozen
  private FormatProfil controlProfil;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (controlProfil == null ? 0 : controlProfil.hashCode());
    result = prime * result + (description == null ? 0 : description.hashCode());
    result = prime * result + (formatCode == null ? 0 : formatCode.hashCode());
    return result;
  }
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
    final FormatControlProfil other = (FormatControlProfil) obj;
    if (controlProfil == null) {
      if (other.controlProfil != null) {
        return false;
      }
    } else if (!controlProfil.equals(other.controlProfil)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (formatCode == null) {
      if (other.formatCode != null) {
        return false;
      }
    } else if (!formatCode.equals(other.formatCode)) {
      return false;
    }
    return true;
  }




  /**
   * @return Code pronom correspondant au format de fichier
   */
  public final String getFormatCode() {
    return formatCode;
  }
  /**
   * @param formatCode Code pronom correspondant au format de fichier to set
   */
  public final void setFormatCode(final String formatCode) {
    this.formatCode = formatCode;
  }


  /**
   * @return the description
   */
  public final String getDescription() {
    return description;
  }
  /**
   * @param description the description to set
   */
  public final void setDescription(final String description) {
    this.description = description;
  }
  /**
   * @return the formatProfil
   */
  public final FormatProfil getControlProfil() {
    return controlProfil;
  }
  /**
   * @param controlProfil the formatProfil to set
   */
  public final void setControlProfil(final FormatProfil controlProfil) {
    this.controlProfil = controlProfil;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final FormatControlProfil o) {
    return formatCode.compareTo(o.getFormatCode());
  }

}
