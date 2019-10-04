/**
 *   (AC75095351) Description du fichier
 */

package fr.urssaf.image.sae.commons.utils.cql;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * (AC75095351) Classe keyspace pour la conversion xml du dataset
 * ATTENTION bien préciser le namespace sinon erreur
 */
@XmlRootElement(name = "keyspace", namespace = "http://xml.dataset.cassandraunit.org")
public class Keyspace {
  /**
   * @return the columnFamilies
   */
  public ColumnFamilies getColumnFamilies() {
    return columnFamilies;
  }

  /**
   * @param columnFamilies
   *          the columnFamilies to set
   */
  @XmlElement(name = "columnFamilies")
  public void setColumnFamilies(final ColumnFamilies columnFamilies) {
    this.columnFamilies = columnFamilies;
  }

  ColumnFamilies columnFamilies;



}
