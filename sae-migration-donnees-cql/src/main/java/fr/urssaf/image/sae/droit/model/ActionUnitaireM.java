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


  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean equals(final Object obj) {
    boolean areEquals = false;

    if (obj instanceof ActionUnitaireM) {
      final ActionUnitaireM actionUnitaire = (ActionUnitaireM) obj;
      areEquals = code.equals(actionUnitaire.getCode())
          && description.equals(actionUnitaire.getDescription());
      if (!description.equals(actionUnitaire.getDescription())) {
        LOGGER.warn("DIFF ActionUnitaires/codes:" + code + "/" + actionUnitaire.getCode() + ", descriptions:" + description + "/"
            + actionUnitaire.getDescription());
      }
    }

    return areEquals;
  }

  public int compareTo(final ActionUnitaireM o) {

    return code.compareTo(o.getCode());
  }

}
