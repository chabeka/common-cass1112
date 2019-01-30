/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.service;

import java.util.List;

import com.datastax.driver.mapping.Result;

import fr.urssaf.image.sae.model.testpoc3;

/**
 * TODO (AC75095028) Description du type
 */
public interface ITestpoc3Service {
	List<testpoc3> findAll();
}
