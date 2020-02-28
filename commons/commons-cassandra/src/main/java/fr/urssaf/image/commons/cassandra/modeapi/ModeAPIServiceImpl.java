package fr.urssaf.image.commons.cassandra.modeapi;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;


/**
 * Service de récupération du mode (thrift ou CQL) des column families
 */
@Service
public class ModeAPIServiceImpl implements ModeAPIService {

   /**
    * Cache ModeApi
    */
   private final LoadingCache<String, ModeAPI> cacheModeAPI;


   @Autowired
   public ModeAPIServiceImpl(final ModeApiCqlSupport modeApiSupport, @Value("${sae.modeapi.cache.refresh:30000}") final int valueRefresh
         ) {

      cacheModeAPI = CacheBuilder.newBuilder()

            .refreshAfterWrite(valueRefresh,
                  TimeUnit.MILLISECONDS)
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
