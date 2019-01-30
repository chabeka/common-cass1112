/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.datastax.driver.mapping.Result;

import fr.urssaf.image.sae.model.POC;

/**
 * TODO (AC75095028) Description du type
 */
public interface IPOCService {
	Result<POC> getAllPOC();

  Optional<POC> findById(UUID id);

  Result<POC> findAll();

  List<POC> saveAll(List<POC> entities);
}
