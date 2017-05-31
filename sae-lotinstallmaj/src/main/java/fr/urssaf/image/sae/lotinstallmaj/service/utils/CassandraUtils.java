package fr.urssaf.image.sae.lotinstallmaj.service.utils;

import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;

/**
 * Utilitaire pour les besoins de mise à jour de Cassandra.
 */
public class CassandraUtils {

   /**
    * Ajout de colonne dans Cassandra.
    * 
    * @param colName
    *           Nom colonne
    * @param value
    *           Valeur de la colonne
    * @param nameSerializer
    *           Nom du serializer
    * @param valueSerializer
    *           Valeur du serializer
    * @param updater
    *           L'Updater
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public static void addColumn(Object colName, Object value,
         Serializer nameSerializer, Serializer valueSerializer,
         ColumnFamilyUpdater<String, String> updater) {
      HColumn<String, String> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);
      updater.setColumn(column);

   }

}
