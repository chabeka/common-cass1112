package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;
import fr.urssaf.image.sae.lotinstallmaj.service.DFCECassandraUpdaterV2;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;

@Component
public class DFCECassandraUpdaterThriftImpl extends DFCECassandraUpdaterV2 {

  @Autowired
  private SAECassandraDao saeDao;

  private static final Logger LOG = LoggerFactory.getLogger(DFCECassandraUpdaterThriftImpl.class);

  /**
   * {@inheritDoc}
   */
  @Override
  protected long getDatabaseVersion() {
    return saeDao.getDatabaseVersion();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setDatabaseVersion(final int version) {
    saeDao.setDatabaseVersion(version);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected long getDatabaseVersionDFCE() {
    return saeDao.getDatabaseVersionDFCE();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setDatabaseVersionDFCE(final int version) {
    saeDao.setDatabaseVersionDFCE(version);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isKeyspaceSAE() {
    final KeyspaceDefinition keyspace = saeDao.describeKeyspace();
    if (keyspace == null) {
      return false;
    }
    return true;
  }

}
