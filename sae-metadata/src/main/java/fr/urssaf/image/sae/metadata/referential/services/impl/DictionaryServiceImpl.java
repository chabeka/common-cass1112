package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.commons.utils.ZookeeperUtils;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataReferenceException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.services.DictionaryService;
import fr.urssaf.image.sae.metadata.referential.support.facade.DictionarySupportFacade;

/**
 * Classe d'implémentation du serviceDictionaryService. Cette classe est un
 * singleton et peut être accessible via le mécanisme d'injection IOC avec
 * l'annotation @Autowired
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Service
public class DictionaryServiceImpl implements DictionaryService {

  private final DictionarySupportFacade dictionarySupport;
  private final CuratorFramework curator;
  private final LoadingCache<String, Dictionary> dictionaries;
  private static final String PREFIXE_DICT = "/Dictionary/";
  private static final Logger LOGGER = LoggerFactory
      .getLogger(DictionaryServiceImpl.class);
  private static final String TRC_CREATE = "addElements()";
  private static final String TRC_DELETE = "deleteElements()";

  /**
   * Constructeur du service
   * 
   * @param dictSupport
   *           la classe support
   * @param jobClockSupport
   *           {@link JobClockSupport}
   * @param curatorFramework
   *           {@link CuratorFramework}
   * @param value
   *           durée du cache
   * @param initCacheOnStartup
   *           flag indiquant si initialise le cache au demarrage du serveur d'application
   */
  @Autowired
  public DictionaryServiceImpl(final DictionarySupportFacade dictSupport,
                               final CuratorFramework curatorFramework,
                               @Value("${sae.metadata.cache}") final int value,
                               @Value("${sae.metadata.initCacheOnStartup}") final boolean initCacheOnStartup) {
    dictionarySupport = dictSupport;
    curator = curatorFramework;
    dictionaries = CacheBuilder.newBuilder().refreshAfterWrite(value,
                                                               TimeUnit.MINUTES).build(new CacheLoader<String, Dictionary>() {

                                                                 @Override
                                                                 public Dictionary load(final String identifiant) {
                                                                   return dictionarySupport.find(identifiant);
                                                                 }

                                                               });

    if (initCacheOnStartup) {
      populateCache();
    }
  }

  private void populateCache() {
    // initialisation du cache des dictionnaires
    final List<Dictionary> allDictionnaries = dictionarySupport.findAll();
    for (final Dictionary dictionnaire : allDictionnaries) {
      dictionaries.put(dictionnaire.getIdentifiant(), dictionnaire);
    }
  }

  /**
   * Ajout d'un nouveau dictionnaire
   * 
   * @param name
   *           nom du dictionnaire
   * @param values
   *           les des valeurs possibles
   */
  @Override
  public final void addElements(final String name, final List<String> values) {

    final String resourceName = PREFIXE_DICT + name;

    final ZookeeperMutex mutex = ZookeeperUtils.createMutex(curator, resourceName);
    try {

      LOGGER.debug("{} - Lock Zookeeper", TRC_CREATE);
      ZookeeperUtils.acquire(mutex, resourceName);
      LOGGER.debug("{} - Création du Dictionnaire", TRC_CREATE);
      for (final String value : values) {
        dictionarySupport.addElement(name, value);
      }

      checkLock(mutex, name);

    } finally {
      mutex.release();
    }

  }

  /**
   * Supression d'un dictionnaire. Attention les données supprimées sont
   * toujours présentes en cache jusqu'a leur expiration
   * 
   * @param name
   *           nom du dictionnaire
   * @param values
   *           les des valeurs possibles
   */

  @Override
  public final void deleteElements(final String name, final List<String> values) {
    LOGGER.debug("{} - Supression des valeurs du dictionnaire", TRC_DELETE);
    for (final String value : values) {
      LOGGER.debug("{} - Supression de la valeur", value);
      dictionarySupport.deleteElement(name, value);
    }
  }

  /**
   * Méthode permettant de récupérer un dictionnaire donnée. Si le dictionnaire
   * n'est pas en cache on appel la méthode find sinon on renvoit la valeur
   * contenu dans le cache.
   * 
   * @param name
   *           nom du dictionnaire
   * @return {@link Dictionary}
   */
  @Override
  public final Dictionary find(final String name) {

    return dictionaries.getUnchecked(name);
  }

  /**
   * Méthode permettant de récupérer tous les dictionnaires sans passer par le
   * cache.
   * 
   * @return List{@link Dictionary}
   */
  @Override
  public final List<Dictionary> findAll() {
    return dictionarySupport.findAll();
  }

  private void checkLock(final ZookeeperMutex mutex, final String dictionary) {

    if (!ZookeeperUtils.isLock(mutex)) {

      Dictionary storedDict = null;
      try {
        storedDict = dictionarySupport.find(dictionary);
      } catch (final DictionaryNotFoundException e) {
        throw new MetadataReferenceException("Le dictionnaire "
            + dictionary + " n'a pas été créé");
      }

      if (!storedDict.getIdentifiant().equals(dictionary)) {
        throw new MetadataRuntimeException("Le dictionnaire " + dictionary
                                           + " a déjà été créé");
      }

    }

  }

}
