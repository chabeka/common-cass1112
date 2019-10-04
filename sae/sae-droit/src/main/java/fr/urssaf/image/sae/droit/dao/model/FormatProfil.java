package fr.urssaf.image.sae.droit.dao.model;

import com.datastax.driver.mapping.annotations.UDT;

/**
 * Bean permettant de stocker le contenu d'une ligne pour le FormatProfilSerializer
 * Annotation UDT (User Defined Type) pour créer un nouveau type utilisé par la classe FormatControlProfil
 */
@UDT(name = "controlprofil")
public class FormatProfil {


  private String fileFormat;
  private boolean formatIdentification;
  private boolean formatValidation;
  private String formatValidationMode;


  /**
   * @return indicateur de contrôle d'identification
   */
  public final boolean isFormatIdentification() {
    return formatIdentification;
  }
  /**
   * @param formatIdentification indicateur de contrôle d'identification to set
   */
  public final void setFormatIdentification(final boolean formatIdentification) {
    this.formatIdentification = formatIdentification;
  }

  /**
   * @return indicateur de contrôle de validation
   */
  public final boolean isFormatValidation() {
    return formatValidation;
  }
  /**
   * @param formatValidation indicateur de contrôle de validation to set
   */
  public final void setFormatValidation(final boolean formatValidation) {
    this.formatValidation = formatValidation;
  }


  /**
   * @return Mode de validation (Strict/Monitor)
   */
  public final String getFormatValidationMode() {
    return formatValidationMode;
  }
  /**
   * @param formatValidationMode Mode de validation (Strict/Monitor) to set
   */
  public final void setFormatValidationMode(final String formatValidationMode) {
    this.formatValidationMode = formatValidationMode;
  }
  /**
   * @return the fileFormat
   */
  public final String getFileFormat() {
    return fileFormat;
  }
  /**
   * @param fileFormat the fileFormat to set
   */
  public final void setFileFormat(final String fileFormat) {
    this.fileFormat = fileFormat;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (fileFormat == null ? 0 : fileFormat.hashCode());
    result = prime * result + (formatIdentification ? 1231 : 1237);
    result = prime * result + (formatValidation ? 1231 : 1237);
    result = prime * result + (formatValidationMode == null ? 0 : formatValidationMode.hashCode());
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
    final FormatProfil other = (FormatProfil) obj;
    if (fileFormat == null) {
      if (other.fileFormat != null) {
        return false;
      }
    } else if (!fileFormat.equals(other.fileFormat)) {
      return false;
    }
    if (formatIdentification != other.formatIdentification) {
      return false;
    }
    if (formatValidation != other.formatValidation) {
      return false;
    }
    if (formatValidationMode == null) {
      if (other.formatValidationMode != null) {
        return false;
      }
    } else if (!formatValidationMode.equals(other.formatValidationMode)) {
      return false;
    }
    return true;
  }

}
