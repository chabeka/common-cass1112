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


  @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionUnitaire other = (ActionUnitaire) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}

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

  @Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((code == null) ? 0 : code.hashCode());
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	return result;
}

  @Override
  public int compareTo(final ActionUnitaire o) {

    return code.compareTo(o.getCode());
  }


}
