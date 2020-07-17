package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.dao.cql.SAECassandraDaoCQL;
import fr.urssaf.image.sae.lotinstallmaj.service.DFCECassandraUpdaterV2;

@Component
public class DFCECassandraUpdaterCQL extends DFCECassandraUpdaterV2 {

   @Autowired
   private SAECassandraDaoCQL saeDao;

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
      saeDao.updateDatabaseVersion(version);
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
      saeDao.updateDatabaseVersionDFCE(version);
   }
}
