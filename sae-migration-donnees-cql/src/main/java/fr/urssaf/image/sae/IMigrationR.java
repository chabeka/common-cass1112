package fr.urssaf.image.sae;

import org.javers.core.Javers;
import org.javers.core.diff.Diff;

/**
 * (AC75095028) Interface générique de migration
 */
public interface IMigrationR {

  /**
   * Migration des données de la table thrift vers la table cql
   */
  public Diff migrationFromThriftToCql(Javers javers);

  /**
   * Migration des données de la table cql vers la table thrift
   */
  public Diff migrationFromCqlTothrift(Javers javers);

}
