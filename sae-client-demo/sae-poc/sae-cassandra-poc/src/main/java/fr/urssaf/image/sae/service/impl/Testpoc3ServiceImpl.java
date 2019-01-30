/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.driver.mapping.Result;

import fr.urssaf.image.sae.dao.ITestpoc3DAO;
import fr.urssaf.image.sae.model.testpoc3;
import fr.urssaf.image.sae.service.ITestpoc3Service;

/**
 * TODO (AC75095028) Description du type
 */
@Service
public class Testpoc3ServiceImpl implements ITestpoc3Service {

  @Autowired
  ITestpoc3DAO testpocdao;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<testpoc3> findAll() {
    return testpocdao.findAll().all();
  }

}
