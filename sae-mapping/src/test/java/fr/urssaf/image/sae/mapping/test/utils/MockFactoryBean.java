/**
 *
 */
package fr.urssaf.image.sae.mapping.test.utils;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Classe de factory pour créer les mocks
 */
public class MockFactoryBean {

  /**
   * Création d'un mock de Base
   *
   * @return un mock Base
   */
  public final StorageServiceProvider createStorageServiceProvider() {
    return EasyMock.createMock(StorageServiceProvider.class);
  }

}
