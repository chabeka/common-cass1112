/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.commons;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.utils.Bytes;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.commons.bo.Parameter;
import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.bo.cql.ParameterCql;
import fr.urssaf.image.sae.commons.dao.IGenericParametersTypeDao;
import fr.urssaf.image.sae.commons.dao.cql.IParametersDaoCql;
import fr.urssaf.image.sae.commons.model.GenericParametersType;
import fr.urssaf.image.sae.commons.support.ParametersSupport;
import fr.urssaf.image.sae.commons.utils.ParametersUtils;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;

/**
 * (AC75095351) Classe de migration des parameters
 */
@Component
public class MigrationParameters {

  @Autowired
  private IParametersDaoCql parameterDaoCql;

  @Autowired
  private ParametersSupport parameterSupport;

  @Autowired
  IGenericParametersTypeDao genericdao;

  @Autowired
  private CassandraClientFactory ccf;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationParameters.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationParameter - migrationFromThriftToCql- start ");

    final Iterator<GenericParametersType> it = genericdao.findAllByCFName("Parameters", ccf.getKeyspace().getKeyspaceName());

    ParameterCql parameterCql;
    int nb = 0;
    String key = null;

    while (it.hasNext()) {
      // Extraction de la clé
      final Row row = (Row) it.next();
      key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));

      // extraction de la colonne
      parameterCql = new ParameterCql();
      parameterCql.setTypeParameters(ParameterRowType.getLabel(key));
      // extraction de la value
      final String name = StringSerializer.get().fromByteBuffer(row.getBytes("column1"));
      parameterCql.setName(ParameterType.valueOfLabel(name));
      final ByteBuffer byteArray = row.getBytes("value");
      // final byte[] bytes = new byte[byteArray.remaining()];
      try {
        final Object obj = ObjectSerializer.get().fromByteBuffer(byteArray);
        // final Object obj = ObjectSerializer.get().fromBytes(bytes);
        parameterCql.setValue(obj);
        if (parameterCql.getName() != null && parameterCql.getTypeParameters() != null) {
          // enregistrement
          parameterDaoCql.saveWithMapper(parameterCql);
        } else {
          LOGGER.error("Les énumérations sont inconnues");
        }
      }
      catch (final Exception e) {
        LOGGER.error(e.getMessage());
        // Pb de conversion on teste une conversion en long
        final String value = Bytes.toRawHexString(byteArray);
        final BigInteger value2 = new BigInteger(value, 16);
        parameterCql.setValue(value2.longValue());
        if (parameterCql.getName() != null && parameterCql.getTypeParameters() != null) {
          parameterDaoCql.saveWithMapper(parameterCql);
        } else {
          LOGGER.error("Les énumérations sont inconnues");
        }
      }
      nb++;
    }
    LOGGER.info(" MigrationParameter - migrationFromThriftToCql- end:nb= " + nb);
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationParameter - migrationFromCqlTothrift- start ");

    final Iterator<ParameterCql> parametersCql = parameterDaoCql.findAllWithMapper();
    while (parametersCql.hasNext()) {
      final ParameterCql parameterCql = parametersCql.next();
      final Parameter parameter = ParametersUtils.convertParameterCqlToParameter(parameterCql);
      // final Parameter parameter = new Parameter(parameterCql.getName(), parameterCql.getValue());
      // final ParameterRowType parameterRowType = ParameterRowType.valueOf(parameterCql.getTypeParameters());
      // ATTENTION POUR TEST !!!!
      // final ParameterRowType parameterRowType = ParameterRowType.CORBEILLE;
      parameterSupport.create(parameter, parameterCql.getTypeParameters(), new Date().getTime());
    }

    LOGGER.info(" MigrationParameter - migrationFromCqlTothrift- end ");
  }

  /**
   * Méthode qui créé une liste de ParameterCql à partir des données thrift
   * Cette méthode est utilisée pour comparer les données thrift et cql
   * 
   * @return liste de ParameterCql
   */

  public List<ParameterCql> getListParametersCqlFromThrift() {
    final List<ParameterCql> listAllFromThrift=new ArrayList<>();

    for (final ParameterRowType parameterRowType : ParameterRowType.values()) {
      final List<Parameter> listThrift = parameterSupport.findAllByRowType(parameterRowType);

      final List<ParameterCql> listParameterFromThrift = ParametersUtils.convertListParametersToListParametersCql(listThrift,
                                                                                                                  parameterRowType);
      if (listParameterFromThrift != null && !listParameterFromThrift.isEmpty()) {
        listAllFromThrift.addAll(listParameterFromThrift);
      }

    }

    /*
     * // CORBEILLE
     * final List<Parameter> listThriftCorbeille = parameterSupport.findAllByRowType(ParameterRowType.CORBEILLE);
     * final List<ParameterCql> listParameterFromThriftCorbeille = ParametersUtils.convertListParametersToListParametersCql(listThriftCorbeille,
     * ParameterRowType.CORBEILLE);
     * listAllFromThrift.addAll(listParameterFromThriftCorbeille);
     * // TRACABILITE
     * final List<Parameter> listThriftTracabilite = parameterSupport.findAllByRowType(ParameterRowType.TRACABILITE);
     * final List<ParameterCql> listParameterFromThriftTracabilite = ParametersUtils.convertListParametersToListParametersCql(listThriftTracabilite,
     * ParameterRowType.TRACABILITE);
     * listAllFromThrift.addAll(listParameterFromThriftTracabilite);
     * // RND
     * final List<Parameter> listThriftRnd = parameterSupport.findAllByRowType(ParameterRowType.RND);
     * final List<ParameterCql> listParameterFromThriftRnd = ParametersUtils.convertListParametersToListParametersCql(listThriftRnd,
     * ParameterRowType.RND);
     * listAllFromThrift.addAll(listParameterFromThriftRnd);
     */

    return listAllFromThrift;
  }
}