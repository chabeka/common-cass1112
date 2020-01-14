/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.TypeName;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;



/**
 * Classe de modèle d'une action unitaire
 * Annotation pour Mapping avec la table cql
 */
@TypeName("ActionUnitaire")
@Table(name = "droitactionunitairecql")
public class ActionUnitaire implements Comparable<ActionUnitaire> {


  /** identifiant unique de l'action unitaire. */
  @PartitionKey
  @Column(name = "code")
  @Id
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
  public final String toString() {
    return "code : " + code + "\ndescription : " + description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int hashCode() {
    return super.hashCode();
  }

  @Override
  public int compareTo(final ActionUnitaire o) {

    return code.compareTo(o.getCode());
  }


}
