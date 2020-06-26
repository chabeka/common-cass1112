/**
 * 
 */
package fr.urssaf.image.sae.commons.bo;

/**
 * Objet contenant un paramètre
 * 
 */
public class Parameter {

  /** Nom du paramètre */
  private final ParameterType name;



  /** valeur du paramètre */
  private final Object value;

  /**
   * Constructeur
   * 
   * @param name
   *          nom du paramètre
   * @param value
   *          valeur du paramètre
   */
  public Parameter(final ParameterType name, final Object value) {
    this.name = name;
    this.value = value;
  }



  /**
   * @return le nom du paramètre
   */
  public final ParameterType getName() {
    return name;
  }

  /**
   * @return la valeur du paramètre
   */
  public final Object getValue() {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (name == null ? 0 : name.hashCode());
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
    final Parameter other = (Parameter) obj;
    if (name != other.name) {
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
