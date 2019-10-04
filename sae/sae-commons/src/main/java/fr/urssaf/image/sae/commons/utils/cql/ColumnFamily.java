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

  private List<Row> rows;

  private String name;

}
