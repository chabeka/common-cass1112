/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.commons.dao.cql.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.CodecNotFoundException;
import com.datastax.driver.extras.codecs.enums.EnumNameCodec;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.bo.cql.ParameterCql;
import fr.urssaf.image.sae.commons.dao.cql.IParametersDaoCql;
import fr.urssaf.image.sae.commons.utils.ObjectCodec;

/**
 * (AC75095351) Implémentation du dao cql Parameters
 */
@Repository
public class ParametersCqlDaoImpl extends GenericDAOImpl<ParameterCql, String> implements IParametersDaoCql {
  /**
   * Constructeur pour injection manuelle
   * 
   * @param ccf
   */
  @Autowired
  public ParametersCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

  @PostConstruct
  public void setRegister() {
    if (ccf != null) {
      /**
       * On initialise les codec pour la gestion des Object et des enums
       */
      /**
       * On initialise les codec pour la gestion des Object et des enums
       */
      final CodecRegistry registry = ccf.getCluster().getConfiguration().getCodecRegistry();
      registerCodecIfNotFound(registry, ObjectCodec.instance);
      registerCodecIfNotFound(registry, new EnumNameCodec<>(ParameterType.class));
      registerCodecIfNotFound(registry, new EnumNameCodec<>(ParameterRowType.class));

      /*
       * ccf.getCluster().getConfiguration().getCodecRegistry().register(ObjectCodec.instance);
       * ccf.getCluster().getConfiguration().getCodecRegistry().register(new EnumNameCodec<>(ParameterType.class));
       * ccf.getCluster().getConfiguration().getCodecRegistry().register(new EnumNameCodec<>(ParameterRowType.class));
       */

    }
  }

  private void registerCodecIfNotFound(final CodecRegistry registry, final TypeCodec<?> codec) {
    try {
      registry.codecFor(codec.getCqlType(), codec.getJavaType());
    }
    catch (final CodecNotFoundException e) {
      registry.register(codec);
    }
  }
}