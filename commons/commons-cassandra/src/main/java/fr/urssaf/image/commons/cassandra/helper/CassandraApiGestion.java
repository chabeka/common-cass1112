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

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

/**
 * @author AC75007648
 *         Cette classe permet de gérer les modes de fonctionnements des APIs
 *         Elle est prévue pour gérer la migration à chaud des API Hector vers
 *         Datastax
 *         C'est un thread qui scrute à intervalle régulier (5 min) le contenu d'une CF en BDD
 *         pour en déterminer le mode à adopter
 */
public class CassandraApiGestion extends Thread {

  @Autowired
  protected CassandraCQLClientFactory ccf;

  private final String cfName = "modeapi";

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(CassandraApiGestion.class);

  /**
   * Méthode de récupératio en BDD du mode de gestion API
   *
   * @return
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
    // On retourne la HashMap
    return listeCfsModes;
  }

  /**
   * Méthode overridé du thread pour effectuer les appels régulier
   * 5 min d'intervalle pour laisser le temps à chaque serveur
   * de se mettre à jour
   */
  @Override
  public void run() {
    try {
      // On set la HashMap public avec le contenu retourné par le méthode du dessus
      ModeGestionAPI.setListeCfsModes(getApiModeFromConfiguration());
      // On impose une latence de 5 min pour que tous les serveurs d'application
      // se mettent à jour selon le contenu de la base
      Thread.sleep(TimeUnit.MINUTES.toMillis(5));
    }
    catch (final InterruptedException e) {
      LOGGER.error(e.getMessage());
    }
    catch (final FileNotFoundException e) {
      LOGGER.error(e.getMessage());
    }
  }

}
