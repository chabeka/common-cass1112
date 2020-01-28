package fr.urssaf.image.sae.format.referentiel.model;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;


/**
 * Bean permettant de stocker le contenu d’une ligne de la Colonie Family
 * Referentielformat
 * 
 * */
@Table(name = "referentielformatcql")
public class FormatFichier implements Comparable<FormatFichier> {
  @PartitionKey
  @Column(name = "idFormat")
  private String idFormat;

  @Column(name = "typeMime")
  private String typeMime;

  @Column(name = "extension")
  private String extension;

  @Column(name = "description")
  private String description;

  @Column(name = "autoriseGED")
  private boolean autoriseGED;

  @Column(name = "visualisable")
  private boolean visualisable;

  @Column(name = "validator")
  private String validator;

  @Column(name = "identificateur")
  private String identificateur;

  @Column(name = "convertisseur")
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
  public final void setIdFormat(final String idFormat) {
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
  public final void setTypeMime(final String typeMime) {
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
  public final void setExtension(final String extension) {
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
  public final void setDescription(final String description) {
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
  public final void setVisualisable(final boolean visualisable) {
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
  public final void setValidator(final String validator) {
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
  public final void setIdentificateur(final String identificateur) {
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
  public final void setConvertisseur(final String convertisseur) {
    this.convertisseur = convertisseur;
  }

  /**
   * Getter pour autoriseGED
   * 
   * @return the autoriseGED
   */
  public boolean isAutoriseGED() {
    return autoriseGED;
  }

  /**
   * Setter pour autoriseGED
   * 
   * @param autoriseGED
   *           the autoriseGED to set
   */
  public void setAutoriseGED(final boolean autoriseGED) {
    this.autoriseGED = autoriseGED;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (autoriseGED ? 1231 : 1237);
    result = prime * result + (convertisseur == null ? 0 : convertisseur.hashCode());
    result = prime * result + (description == null ? 0 : description.hashCode());
    result = prime * result + (extension == null ? 0 : extension.hashCode());
    result = prime * result + (idFormat == null ? 0 : idFormat.hashCode());
    result = prime * result + (identificateur == null ? 0 : identificateur.hashCode());
    result = prime * result + (typeMime == null ? 0 : typeMime.hashCode());
    result = prime * result + (validator == null ? 0 : validator.hashCode());
    result = prime * result + (visualisable ? 1231 : 1237);
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
    final FormatFichier other = (FormatFichier) obj;
    if (autoriseGED != other.autoriseGED) {
      return false;
    }
    if (convertisseur == null) {
      if (other.convertisseur != null) {
        return false;
      }
    } else if (!convertisseur.equals(other.convertisseur)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (extension == null) {
      if (other.extension != null) {
        return false;
      }
    } else if (!extension.equals(other.extension)) {
      return false;
    }
    if (idFormat == null) {
      if (other.idFormat != null) {
        return false;
      }
    } else if (!idFormat.equals(other.idFormat)) {
      return false;
    }
    if (identificateur == null) {
      if (other.identificateur != null) {
        return false;
      }
    } else if (!identificateur.equals(other.identificateur)) {
      return false;
    }
    if (typeMime == null) {
      if (other.typeMime != null) {
        return false;
      }
    } else if (!typeMime.equals(other.typeMime)) {
      return false;
    }
    if (validator == null) {
      if (other.validator != null) {
        return false;
      }
    } else if (!validator.equals(other.validator)) {
      return false;
    }
    if (visualisable != other.visualisable) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final FormatFichier o) {

    return getIdFormat().compareTo(o.getIdFormat());
  }

}
