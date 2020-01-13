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
  private boolean result;

  private Diff diff;

  /**
   * @return the result
   */
  public boolean isResult() {
    return result;
  }

  /**
   * @param result
   *          the result to set
   */
  public void setResult(final boolean result) {
    this.result = result;
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
}
