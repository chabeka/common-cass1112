package fr.urssaf.image.sae;

/**
 * (AC75095028) Interface générique de migration
 */
public interface IMigrationR {

  /**
   * Migration des données de la table thrift vers la table cql
   */
  public boolean migrationFromThriftToCql();

  /**
   * Migration des données de la table cql vers la table thrift
   */
  public boolean migrationFromCqlTothrift();

}
