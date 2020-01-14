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
