/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe de modèle d'une action unitaire
 * Annotation pour Mapping avec la table cql
 */
@Table(name = "droitactionunitairecql")
public class ActionUnitaire implements Comparable<ActionUnitaire> {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ActionUnitaire.class);

  /** identifiant unique de l'action unitaire. */
  @PartitionKey
  @Column(name = "code")
  private String code;

  /** description de l'action unitaire */
  @Column(name = "description")
  private String description;

  /**
   * @return l'identifiant unique de l'action unitaire
   */
  public final String getCode() {
    return code;
  }

  /**
   * @param code
   *           identifiant unique de l'action unitaire
   */
  public final void setCode(final String code) {
    this.code = code;
  }

  /**
   * @return la description de l'action unitaire
   */
  public final String getDescription() {
    return description;
  }

  /**
   * @param description
   *           description de l'action unitaire
   */
  public final void setDescription(final String description) {
    this.description = description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean equals(final Object obj) {
    boolean areEquals = false;

    if (obj instanceof ActionUnitaire) {
      final ActionUnitaire actionUnitaire = (ActionUnitaire) obj;
      areEquals = code.equals(actionUnitaire.getCode())
          && description.equals(actionUnitaire.getDescription());
      if (!description.equals(actionUnitaire.getDescription())) {
        LOGGER.warn("codes:" + code + "/" + actionUnitaire.getCode() + ", descriptions:" + description + "/" + actionUnitaire.getDescription());
      }
    }

    return areEquals;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int hashCode() {
    return super.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String toString() {
    return "code : " + code + "\ndescription : " + description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final ActionUnitaire o) {

    return getCode().compareTo(o.getCode());
  }

}
