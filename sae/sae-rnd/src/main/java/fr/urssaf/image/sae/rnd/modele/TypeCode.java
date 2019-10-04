/**
 *  TODO (AC75095351) Description du fichier
 */

/**
 * TODO (AC75095351) Description du type
 *
 */

package fr.urssaf.image.sae.rnd.modele;

/**
 * Type possible des types de document
 */
public enum TypeCode {
                          ARCHIVABLE_AED("Type de document archivable AED"),
                          NON_ARCHIVABLE_AED("Type de document non archivable AED"),
                          TEMPORAIRE("Type de document temporaire");

  private String description;

  TypeCode(final String description) {
    this.description = description;
  }

  /**
   * @return le nom
   */
  public String getValue() {
    return name();
  }

  /**
   * @return le libellé
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description
   *          Le libellé
   */
  public void setDescription(final String description) {
    this.description = description;
  }
}
