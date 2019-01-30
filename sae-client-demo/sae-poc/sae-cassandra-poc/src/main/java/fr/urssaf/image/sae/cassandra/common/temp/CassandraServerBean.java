package fr.urssaf.image.sae.cassandra.common.temp;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Classe utilitaire facilitant la création d'un serveur cassandra local par un
 * bean spring.
 */
public class CassandraServerBean implements InitializingBean, DisposableBean {

  private static final Logger LOG = LoggerFactory
                                                 .getLogger(CassandraServerBean.class);

  private static final int WAIT_MAX_TRY = 12;

  private static final long WAIT_MS = 1000;

  private static final String TEST_CLUSTER_NAME = "SAE-MIG";

  private String[] dataSets;

  private boolean startLocal = false;

  private String hosts = null;

  /**
   * Indique quel jeu de données cassandraUnit doit être utilisé lors de
   * l'initialisation du serveur cassandra
   *
   * @param dataSet
   *          Jeu de données
   */
  public final void setDataSet(final String dataSet) {

    // Il peut y avoir plusieurs dataSets séparés par des ;
    this.dataSets = StringUtils.split(dataSet, ';');

  }

  /**
   * Indique s'il faut lancer un serveur cassandra local
   *
   * @param startLocal
   *          vrai s'il faut lancer un serveur local
   */
  public final void setStartLocal(final boolean startLocal) {
    this.startLocal = startLocal;
  }

  /**
   * @return vrai si le serveur cassandra est lancé localement
   */
  public final boolean getStartLocal() {
    return this.startLocal;
  }

  public final void destroy() throws Exception {
    // Pas besoin d'arrêter le serveur
  }

  public final void afterPropertiesSet() throws Exception {
    LOG.debug("CassandraServerBean : startLocal={} - dataSet={}",
              startLocal,
              dataSets);
    resetData(dataSets);
  }

  /**
   * Réinitialise les données de la base cassandra locale, avec le jeu de
   * données utilisé initialement lors de la création du serveur
   *
   * @throws Exception
   *           Une erreur est survenue
   */
  public final void resetData() throws Exception {
    resetData(dataSets);
  }

  /**
   * Réinitialise les données de la base cassandra locale
   *
   * @param newDataSets
   *          Jeu(x) de données à utiliser
   * @throws Exception
   *           Une erreur est survenue
   */
  public final void resetData(final String... newDataSets) throws Exception {
  }

  /**
   * Il arrive que le serveur cassandra local mette du temps avant d'être
   * opérationnel. Cette méthode fait en sorte d'attendre jusqu'à ce qu'il soit
   * opérationnel
   *
   * @throws InterruptedException
   *           : on a été interrompu
   */
  private void waitForServer() throws InterruptedException {
  }

  /**
   * Dans le cas d'un cassandra zookeeper non local, il s'agit de la chaîne de
   * connexion
   *
   * @param hosts
   *          Chaîne de connexion (ex :
   *          "toto.toto.com:9160,titi.titi.com:9160")
   */
  public final void setHosts(final String hosts) {
    this.hosts = hosts;
  }

  /**
   * Renvoie la chaîne de connexion au serveur cassandra
   *
   * @return chaîne de connexion
   */
  public final String getHosts() {
    if (startLocal) {
      // Petite bidouille : on met le serveur localhost 3 fois : ça permet de
      // tenter 3 fois
      // l'opération si elle échoue la 1ere fois (ça arrive lorsque le
      // serveur cassandra local
      // ne se lance pas assez rapidement)
      return "localhost:9042,localhost:9042,localhost:9042";
    } else {
      return hosts;
    }
  }

}
