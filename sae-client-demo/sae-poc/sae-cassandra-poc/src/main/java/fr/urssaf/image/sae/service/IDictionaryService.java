/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.datastax.driver.mapping.Result;

import fr.urssaf.image.sae.model.Dictionary;

/**
 * TODO (AC75095028) Description du type
 */
public interface IDictionaryService {
  public Result<Dictionary> getDictionary();

  public Optional<Dictionary> findById(UUID id);

  Result<Dictionary> findAll();

  List<Dictionary> saveAll(List<Dictionary> entities);
}
