/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae;

import org.javers.core.diff.Diff;

/**
 * TODO (AC75095351) Description du type
 *
 */
public class DiffM {




  private Diff diff;

  private String message;

  private boolean resultCompare;

  private boolean resultMigration;





  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  /**
   * @return the diff
   */
  public Diff getDiff() {
    return diff;
  }

  /**
   * @param diff
   *          the diff to set
   */
  public void setDiff(final Diff diff) {
    this.diff = diff;
  }

  public boolean isResultMigration() {
    return resultMigration;
  }

  public void setResultMigration(final boolean resultMigration) {
    this.resultMigration = resultMigration;
  }

  public boolean isResultCompare() {
    return resultCompare;
  }

  public void setResultCompare(final boolean resultCompare) {
    this.resultCompare = resultCompare;
  }
}
