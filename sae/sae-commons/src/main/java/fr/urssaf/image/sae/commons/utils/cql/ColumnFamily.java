/**
 *   (AC75095351) 
 *    */
package fr.urssaf.image.sae.commons.utils.cql;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import fr.urssaf.image.sae.commons.utils.Row;

/**
 * (AC75095351) Classe columnFamily pour la conversion xml du dataset
 */
@XmlRootElement(name = "columnFamily")
public class ColumnFamily {
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
  @XmlElement(name = "name")
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @return the rows
   */
  public List<Row> getRows() {
    return rows;
  }

  /**
   * @param rows
   *          the rows to set
   */
  @XmlElement(name = "row")
  public void setRows(final List<Row> rows) {
    this.rows = rows;
  }

  public String getType() {
    return type;
  }

  @XmlElement(name = "type")
  public void setType(final String type) {
    this.type = type;
  }

  public String getKeyType() {
    return keyType;
  }

  @XmlElement(name = "keyType")
  public void setKeyType(final String keyType) {
    this.keyType = keyType;
  }

  public String getComparatorType() {
    return comparatorType;
  }

  @XmlElement(name = "comparatorType")
  public void setComparatorType(final String comparatorType) {
    this.comparatorType = comparatorType;
  }

  public String getDefaultColumnValueType() {
    return defaultColumnValueType;
  }

  @XmlElement(name = "defaultColumnValueType")
  public void setDefaultColumnValueType(final String defaultColumnValueType) {
    this.defaultColumnValueType = defaultColumnValueType;
  }
  private List<Row> rows;

  private String name;

  private String type;

  private String keyType;

  private String comparatorType;

  private String defaultColumnValueType;

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "ColumnFamily [rows=" + rows + ", name=" + name + ", type=" + type + ", keyType=" + keyType + ", comparatorType=" + comparatorType
        + ", defaultColumnValueType=" + defaultColumnValueType + "]";
  }

}
