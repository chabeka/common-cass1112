package fr.urssaf.image.sae.rnd.dao.cql.impl;


import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.datastax.driver.extras.codecs.enums.EnumNameCodec;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOCompositeImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.rnd.dao.cql.ICorrespondancesDaoCql;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.EtatCorrespondance;

/**
 * (AC75095351)Implémentation du dao cql CorrespondancesRnd
 */
@Repository
public class CorrespondancesCqlDaoImpl extends GenericDAOCompositeImpl<Correspondance, String, String> implements ICorrespondancesDaoCql {
  /**
   * Cette methode est appelé après l'instanciation de la classe par spring.
   * Grace à l'annotation {@link PostConstruct} on est sur que les dependances
   * son bien injectés ({@link CassandraClientFactory}) et cela nous permet d'enregistrer tous les <b>codec</b> nécessaires
   * aux opérations sur la table (CF) de ce DAO
   * Ici le codec nous permet d'utiliser les enum avec Datastax
   */
  @PostConstruct
  public void setRegister() {
    if(ccf != null) {
      ccf.getCluster().getConfiguration().getCodecRegistry().register(new EnumNameCodec<>(EtatCorrespondance.class));
    }
  }

}
