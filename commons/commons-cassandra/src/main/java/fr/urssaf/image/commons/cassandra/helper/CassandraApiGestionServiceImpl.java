/**
 *
 */
package fr.urssaf.image.commons.cassandra.helper;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author AC75007648
 *         Cette classe permet de gérer les modes de fonctionnements des APIs
 *         Elle est prévue pour gérer la migration à chaud des API Hector vers
 *         Datastax
 *         C'est un thread qui scrute à intervalle régulier (5 min) le contenu d'une CF en BDD
 *         pour en déterminer le mode à adopter
 */
public class CassandraApiGestionServiceImpl {

  // Spring tire le client CQL factory pour effectuer la requête
  // @Autowired
  protected CassandraCQLClientFactory ccf;

  // Comme pour les services CQL le nom de la CF est présente ici
  private final String cfName = "modeapi";

  // Ajout du logger pour obtenir des traces si nécessaires
  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(CassandraApiGestionServiceImpl.class);

  private final LoadingCache<String, HashMap<String, String>> modeApisList;

  @Autowired
  public CassandraApiGestionServiceImpl(final CassandraCQLClientFactory cassandraCQLClientFactory, @Value("${sae.api.gestion.profil.cache}") final int value,
                                        @Value("${sae.api.gestion.profil.initCacheOnStartup}") final boolean initCacheOnStartup) {
    this.ccf = cassandraCQLClientFactory;
    modeApisList = CacheBuilder.newBuilder()
                               .refreshAfterWrite(value,
                                                  TimeUnit.MINUTES)
                               .build(
                                      new CacheLoader<String, HashMap<String, String>>() {

                                        @Override
                                        public HashMap<String, String> load(final String identifiant) throws FileNotFoundException {
                                          return getApiModeFromConfiguration();
                                        }
                                      });

    if (initCacheOnStartup) {
      populateCache();
    }
  }

  /**
   * Méthode de récupératio en BDD du mode de gestion API
   *
   * @return HashMap<String, String>
   * @throws FileNotFoundException
   */
  public HashMap<String, String> getApiModeFromConfiguration() throws FileNotFoundException {
    // Initialisation de la HashMap
    final HashMap<String, String> listeCfsModes = new HashMap<>();
    // Requête en CQL via API Datastax pour aller scruter le contenu de la CF "modeapi"
    final Select selectQuery = QueryBuilder.select().all().from(ccf.getKeyspace(), cfName);
    // Parcours du ResultSet
    final ResultSet results = ccf.getSession().execute(selectQuery);
    final Iterator<Row> iter = results.iterator();
    while (iter.hasNext()) {
      final Row row = iter.next();
      // Injection de la Row dans la HashMap
      listeCfsModes.put(row.getString(0), row.getString(1));
    }
    ModeGestionAPI.setListeCfsModes(listeCfsModes);
    // On retourne la HashMap
    return listeCfsModes;
  }

  /**
   * Méthode overridé du thread pour effectuer les appels régulier
   * 5 min d'intervalle pour laisser le temps à chaque serveur
   * de se mettre à jour
   */
  public void populateCache() {
    try {
      // On set la HashMap public avec le contenu retourné par la méthode du dessus
      modeApisList.put(cfName, getApiModeFromConfiguration());
    }
    catch (final FileNotFoundException e) {
      LOGGER.error(e.getMessage());
    }
  }

}
