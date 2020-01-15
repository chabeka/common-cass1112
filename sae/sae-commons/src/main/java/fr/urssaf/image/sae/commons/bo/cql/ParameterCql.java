/**
 * 
 */
package fr.urssaf.image.sae.commons.bo.cql;

import org.javers.core.metamodel.annotation.Id;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.collect.ComparisonChain;

import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;

/**
 * Objet contenant un paramètre enversion Cql
 */
@Table(name = "parameterscql")
public class ParameterCql implements Comparable<ParameterCql> {

  /**
   * @return the typeParameters
   */
  public ParameterRowType getTypeParameters() {
    return typeParameters;
  }

  /**
   * @param typeParameters
   *          the typeParameters to set
   */
  public void setTypeParameters(final ParameterRowType typeParameters) {
    this.typeParameters = typeParameters;
  }

  /**
   * @return the name
   */
  public ParameterType getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(final ParameterType name) {
    this.name = name;
  }

  /**
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(final Object value) {
    this.value = value;
  }

  /** Type paramètre */
  @PartitionKey
  @Column(name = "typeParameters")
  @Id
  private ParameterRowType typeParameters;
  /** Nom du paramètre */
  @ClusteringColumn
  @Column(name = "name")
  @Id
  private ParameterType name;

  /** valeur du paramètre */
  @Column(name = "value")
  private Object value;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final ParameterCql o) {
    return ComparisonChain.start()
        .compare(getTypeParameters(), o.getTypeParameters())
        .compare(getName(), o.getName())
        .result();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (name == null ? 0 : name.hashCode());
    result = prime * result + (typeParameters == null ? 0 : typeParameters.hashCode());
    result = prime * result + (value == null ? 0 : value.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ParameterCql other = (ParameterCql) obj;
    if (name != other.name) {
      return false;
    }
    if (typeParameters != other.typeParameters) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

}
