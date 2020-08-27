package fr.urssaf.image.sae.lotinstallmaj.dao.cql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.bo.cql.ParameterCql;
import fr.urssaf.image.sae.commons.dao.cql.IParametersDaoCql;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.SAEKeyspaceConnecter;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.SerializerUtils;

@Component
public class SAECassandraDaoCQL {

  @Autowired
  IParametersDaoCql parametersDaoImpl;

  @Autowired
  private SAEKeyspaceConnecter saeKeyspaceConnecter;

  /**
   * Mise à jour de la version de la base SAE CQL
   * 
   * @param version
   */
  public void updateDatabaseVersion(final int version) {
    saeKeyspaceConnecter.connectToKeyspace();
    final ParameterCql parameterCql = new ParameterCql();
    parameterCql.setName(ParameterType.versionBDD);
    parameterCql.setTypeParameters(ParameterRowType.PARAMETERS);
    parameterCql.setValue(version);
    parametersDaoImpl.saveWithMapper(parameterCql);
  }

  /**
   * Recupère la version actuelle de la base
   * 
   * @return
   */
  public long getDatabaseVersion() {
    saeKeyspaceConnecter.connectToKeyspace();
    long version = 0;
    try {
      final String query = "SELECT * FROM parameterscql WHERE typeparameters = 'PARAMETERS' AND name = 'versionBDD'";
      final ResultSet result = parametersDaoImpl.getSession().execute(query);

      final List<Row> rows = result.all();
      if (!rows.isEmpty()) {
        version = (Integer) SerializerUtils.getBytesAsObject(rows.get(0).getBytes("value"));
      }
    }
    catch (final Exception e) {
      throw new MajLotRuntimeException("Erreur survenue lors de la recupération de la version de la bdd SAE", e);
    }

    return version;
  }

  /**
   * Mise à jour de la version de la base SAE CQL
   * 
   * @param version
   */
  public void updateDatabaseVersionDFCE(final int version) {
    saeKeyspaceConnecter.connectToKeyspace();
    final ParameterCql parameterCql = new ParameterCql();
    parameterCql.setName(ParameterType.VERSION_BDD_DFCE);
    parameterCql.setTypeParameters(ParameterRowType.PARAMETERS);
    parameterCql.setValue(version);
    parametersDaoImpl.saveWithMapper(parameterCql);
  }


  /**
   * Recupère la version actuelle de la base
   * 
   * @return 0 si la version n'est pas setter
   */
  public int getDatabaseVersionDFCE() {
    saeKeyspaceConnecter.connectToKeyspace();
    int version = 0;
    final String query = "SELECT * FROM parameterscql WHERE typeParameters = 'PARAMETERS' AND name = 'VERSION_BDD_DFCE'";
    final ResultSet result = parametersDaoImpl.getSession().execute(query);

    final List<Row> rows = result.all();
    if (!rows.isEmpty()) {
      try {
        version = (Integer) SerializerUtils.getBytesAsObject(rows.get(0).getBytes("value"));
      }
      catch (final MajLotGeneralException e) {
        throw new MajLotRuntimeException("Erreur survenue lors de la recupération de la version de la bdd DFCE", e);
      }
    }

    return version;
  }

  public boolean isKeyspaceSAE() {
    saeKeyspaceConnecter.connectToKeyspace();
    final KeyspaceMetadata keyspaceMetadata = parametersDaoImpl.getSession().getCluster().getMetadata().getKeyspace("\"SAE\"");
    if (keyspaceMetadata == null) {
      return false;
    }
    return true;
  }
}
