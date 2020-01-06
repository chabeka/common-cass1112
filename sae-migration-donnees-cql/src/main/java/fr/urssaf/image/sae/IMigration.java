package fr.urssaf.image.sae;

/**
 * (AC75095028) Interface générique de migration
 */
public interface IMigration {

  /**
   * Migration des données de la table thrift vers la table cql
   */
  public void migrationFromThriftToCql();

  /**
   * Migration des données de la table cql vers la table thrift
   */
  public void migrationFromCqlTothrift();

}
