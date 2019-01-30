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

import fr.urssaf.image.sae.dao.IPOCDAO;
import fr.urssaf.image.sae.model.POC;
import fr.urssaf.image.sae.service.IPOCService;

/**
 * TODO (AC75095028) Description du type
 */
@Service
public class POCServiceImpl implements IPOCService {

  @Autowired
  IPOCDAO pocdao;

  /**
   * {@inheritDoc}
   */
  @Override
  public Result<POC> getAllPOC() {
    return pocdao.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<POC> findById(final UUID id) {
    return pocdao.findById(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Result<POC> findAll() {
    return pocdao.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<POC> saveAll(final List<POC> entities) {
    return pocdao.saveAll(entities);
  }

}
