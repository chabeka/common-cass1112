/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.commons.utils.cql;

import java.util.List;

/**
 * (AC75095351) Classe columnFamilies pour la conversion xml du dataset
 */
// @XmlRootElement(name = "columnFamilies")
public class ColumnFamilies {

  /**
   * @return the rows
   */
  public List<ColumnFamily> getColumnFamily() {
    return columnFamilies;
  }

  /**
   * @param columnFamilies
   *          the columnFamilies to set
   */
  // @XmlElement(name = "columnFamily")
  public void setColumnFamily(final List<ColumnFamily> columnFamilies) {
    this.columnFamilies = columnFamilies;
  }

  private List<ColumnFamily> columnFamilies;

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "ColumnFamilies [columnFamilies=" + columnFamilies + "]";
  }

}
