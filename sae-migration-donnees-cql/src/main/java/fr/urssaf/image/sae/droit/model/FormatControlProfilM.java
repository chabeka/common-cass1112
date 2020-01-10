/**
 * 
 */
package fr.urssaf.image.sae.droit.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;

public class FormatControlProfilM extends FormatControlProfil implements Comparable<FormatControlProfilM> {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(FormatControlProfilM.class);

  public FormatControlProfilM(final FormatControlProfil formatControlProfil) {
    super.setFormatCode(formatControlProfil.getFormatCode());
    super.setDescription(formatControlProfil.getDescription());
    super.setControlProfil(formatControlProfil.getControlProfil());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (getControlProfil() == null ? 0 : getControlProfil().hashCode());
    result = prime * result + (getDescription() == null ? 0 : getDescription().hashCode());
    result = prime * result + (getFormatCode() == null ? 0 : getFormatCode().hashCode());
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
    final FormatControlProfilM other = (FormatControlProfilM) obj;
    if (getControlProfil() == null) {
      if (other.getControlProfil() != null) {
        return false;
      }
    } else if (!getControlProfil().equals(other.getControlProfil())) {

      LOGGER.warn("DIFF/code:" + getFormatCode() + "/" + other.getFormatCode() + ", controlProfil:" + getControlProfil() + "/" + other.getControlProfil());

      return false;
    }
    if (getDescription() == null) {
      if (other.getDescription() != null) {
        return false;
      }
    } else if (!getDescription().equals(other.getDescription())) {
      LOGGER.warn("DIFF/code:" + getFormatCode() + "/" + other.getFormatCode() + ", description:" + getDescription() + "/" + other.getDescription());
      return false;
    }
    if (getFormatCode() == null) {
      if (other.getFormatCode() != null) {
        return false;
      }
    } else if (!getFormatCode().equals(other.getFormatCode())) {
      return false;
    }
    return true;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final FormatControlProfilM o) {

    return getFormatCode().compareTo(o.getFormatCode());
  }

}
