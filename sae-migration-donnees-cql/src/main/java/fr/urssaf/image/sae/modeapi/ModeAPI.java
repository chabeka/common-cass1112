/**
 * 
 */
package fr.urssaf.image.sae.modeapi;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe de modèle du modeAPI pour la sélection du mode de migration
 * Annotation pour Mapping avec la table cql
 */
@Table(name = "modeapi")
public class ModeAPI  {

  /** identifiant unique du cfname */
  @PartitionKey
  @Column(name = "cfname")
  private String cfname;

  /** mode associé à la table cfname */
  @Column(name = "mode")
  private String mode;

  /**
   * @return the cfname
   */
  public String getCfname() {
    return cfname;
  }

  /**
   * @param cfname
   *          the cfname to set
   */
  public void setCfname(final String cfname) {
    this.cfname = cfname;
  }

  /**
   * @return the mode
   */
  public String getMode() {
    return mode;
  }

  /**
   * @param mode
   *          the mode to set
   */
  public void setMode(final String mode) {
    this.mode = mode;
  }

}
