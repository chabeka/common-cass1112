/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.driver.mapping.Result;

import fr.urssaf.image.sae.dao.IDictionaryDAO;
import fr.urssaf.image.sae.model.Dictionary;
import fr.urssaf.image.sae.service.IDictionaryService;

/**
 * TODO (AC75095028) Description du type
 */

@Service
public class DictionaryServiceImpl implements IDictionaryService {

  @Autowired
  IDictionaryDAO dicdao;

  /**
   * {@inheritDoc}
   */
  @Override
  public Result<Dictionary> getDictionary() {
    return dicdao.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Dictionary> findById(final UUID id) {
    return dicdao.findById(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Result<Dictionary> findAll() {
    return dicdao.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Dictionary> saveAll(final List<Dictionary> entities) {
    return dicdao.saveAll(entities);
  }

}
