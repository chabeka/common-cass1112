/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.model.columnKey;


import org.springframework.context.annotation.Primary;


import com.datastax.driver.mapping.annotations.PartitionKey;

import java.io.Serializable;


/**
 * TODO (AC75095028) Description du type
 */

public class Testpoc3Id implements Serializable{
	
  @PartitionKey
  private final String nom;

  @PartitionKey
  private final String prenom;

  /**
   * @param nom
   * @param prenom
   */
  public Testpoc3Id(final String nom, final String prenom) {
    super();
    this.nom = nom;
    this.prenom = prenom;
  }

  /**
   * @return the nom
   */
  public String getNom() {
    return nom;
  }

  /**
   * @return the prenom
   */
  public String getPrenom() {
    return prenom;
  }

}
