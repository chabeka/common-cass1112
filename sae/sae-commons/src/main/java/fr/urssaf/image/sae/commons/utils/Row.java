/**
 *   (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.commons.utils;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.commons.utils.cql.Column;

/**
 * (AC75095351) Ligne thrift pour extraction de données à partir de dataset
 */
// @XmlRootElement(name = "row")
public class Row {
  /**
   * @return the key
   */

  public String getKey() {
    return key;
  }

  /**
   * @param key
   *          the key to set
   */
  public void setKey(final String key) {
    this.key = key;
  }


  /**
   * @return the columns
   */
  public List<Column> getColumns() {
    return columns;
  }

  /**
   * @param columns the columns to set
   */
  // @XmlElement(name = "column")
  public void setColumns(final List<Column> columns) {
    this.columns = columns;
  }


  private String key;

  private List<Column> columns = new ArrayList<>();
}
