/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.model;

import java.util.UUID;



/**
 * TODO (AC75095028) Description du type
 */
public class POC {

  private UUID id;

  private String value;

  /**
   * @return the id
   */
  public UUID getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(final UUID id) {
    this.id = id;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(final String value) {
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "POC [id=" + id + ", value=" + value + "]";
  }

}
