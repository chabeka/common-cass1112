/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.commons.utils.cql;

/**
 * (AC75095351) Classe column pour la conversion xml du dataset
 */
// @XmlRootElement(name = "column")
public class Column {
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  // @XmlElement(name = "name")
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value
   *          the value to set
   */
  // @XmlElement(name = "value")
  public void setValue(final String value) {
    this.value = value;
  }


  String name;


  String value;
}
