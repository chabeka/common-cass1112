/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae;

/**
 * TODO (AC75095028) Description du type
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
