package fr.urssaf.image.commons.cassandra.modeapi;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;


/**
 * Service de récupération des propriétés d'un type de document
 * 
 * 
 */
@Service
public class ModeAPIServiceImpl implements ModeAPIService {

  /**
   * Cache ModeApi
   */
  private final LoadingCache<String, ModeAPI> cacheModeAPI;


  private ModeApiCqlSupport modeApiSupport;

  private static final int LIFE_DURATION = 45;

  @Autowired
  public ModeAPIServiceImpl(final ModeApiCqlSupport modeApiSupport) {
    this.modeApiSupport = modeApiSupport;
    cacheModeAPI = CacheBuilder.newBuilder()
        .refreshAfterWrite(LIFE_DURATION,
                           TimeUnit.SECONDS)
        .build(new CacheLoader<String, ModeAPI>() {

          @Override
          public ModeAPI load(final String cfName) {
            return modeApiSupport.findById(cfName);
          }

        });
  }

  /**
   * @param dureeCache
   *          la durée du cache définie dans le fichier sae-config Construit
   *          un objet de type {@link ModeAPIServiceImpl}
   */

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("PMD.PreserveStackTrace")
  public String getModeAPI(final String cfName) throws ModeGestionAPIUnkownException {
    try {
      final ModeAPI modeAPI = cacheModeAPI.getUnchecked(cfName);
      return modeAPI.getMode();
    } catch (final InvalidCacheLoadException e) {
      throw new ModeGestionAPIUnkownException("Le modeAPI pour " + cfName
                                              + " n'existe pas.");
    }
  }

}
