/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.commons.utils.cql;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * (AC75095351) Classe columnFamilies pour la conversion xml du dataset
 */
@XmlRootElement(name = "columnFamilies")
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
  @XmlElement(name = "columnFamily")
  public void setColumnFamily(final List<ColumnFamily> columnFamilies) {
    this.columnFamilies = columnFamilies;
  }

  private List<ColumnFamily> columnFamilies;

}
