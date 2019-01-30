/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.model;



import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.model.columnKey.Testpoc3Id;

/**
 * TODO (AC75095028) Description du type
 */
@Table(name = "testpoc3")
public class testpoc3 {
	
	  @PartitionKey
	  private  String nom;
	  private  String prenom;

	public String getNom() {
return nom;
} 


	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

}
