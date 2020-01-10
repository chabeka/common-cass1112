/**
 * 
 */
package fr.urssaf.image.sae.droit.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;

public class ActionUnitaireM extends ActionUnitaire {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ActionUnitaireM.class);

  public ActionUnitaireM(final ActionUnitaire actionUnitaire) {
    super.setCode(actionUnitaire.getCode());
    super.setDescription(actionUnitaire.getDescription());
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean equals(final Object obj) {
    boolean areEquals = false;

    if (obj instanceof ActionUnitaireM) {
      final ActionUnitaireM actionUnitaire = (ActionUnitaireM) obj;
      areEquals = getCode().equals(actionUnitaire.getCode())
          && getDescription().equals(actionUnitaire.getDescription());
      if (!getDescription().equals(actionUnitaire.getDescription())) {
        LOGGER.warn("DIFF ActionUnitaires/codes:" + getCode() + "/" + actionUnitaire.getCode() + ", descriptions:" + getDescription() + "/"
            + actionUnitaire.getDescription());
      }
    }

    return areEquals;
  }

  /* *//**
   * {@inheritDoc}
   *//*
   * @Override
   * public final int hashCode() {
   * return super.hashCode();
   * }
   * @Override
   * public int compareTo(final ActionUnitaireM o) {
   * return code.compareTo(o.getCode());
   * }
   */

}
