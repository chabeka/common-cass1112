package fr.urssaf.image.sae.commons.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import fr.urssaf.image.sae.commons.bo.Parameter;

/**
 * Classe permettant de réaliser des opérations sur la base CASSANDRA
 * 
 * 
 */
@Repository
public class ParametersDao extends AbstractDao<String, String> {

   /**
    * Constructeur
    * 
    * @param keyspace
    *           le keyspace à utiliser
    */
   @Autowired
   public ParametersDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getColumnFamilyName() {
      return "Parameters";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * Ajout d'une colonne de paramètre
    * 
    * @param updater
    *           updater de <b>Parameters</b>
    * @param parameter
    *           parametre a inserer
    * @param clock
    *           horloge de la creation
    */
   public final void writeColumnParameter(
         ColumnFamilyUpdater<String, String> updater, Parameter parameter,
         long clock) {
      addColumn(updater, parameter.getName().toString(), parameter.getValue(),
            ObjectSerializer.get(), clock);
   }

}
