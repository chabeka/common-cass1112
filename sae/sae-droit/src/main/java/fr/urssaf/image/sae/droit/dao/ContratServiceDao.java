/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import java.util.List;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.droit.dao.serializer.ListSerializer;

/**
 * Service DAO de la famille de colonnes "DroitContratService"
 * 
 */
@Repository
public class ContratServiceDao {

   public static final String CS_CFNAME = "DroitContratService";

   public static final int MAX_CS_ATTIBUTS = 100;

   private final ColumnFamilyTemplate<String, String> cfTmpl;

   private final Keyspace keyspace;

   /** code de l'organisme client lié au contrat de service */
   public static final String CS_LIBELLE = "libelle";

   /** durée maximum de l'habilitation exprimée en secondes */
   public static final String CS_VI_DUREE = "viDuree";

   /** description du contrat de service */
   public static final String CS_DESCRIPTION = "description";

   /** CN de la pki attendue */
   public static final String CS_PKI = "pki";

   /** liste des CN des pki attendues */
   public static final String CS_LISTE_PKI = "listPki";

   /** CN du certificat client attendu */
   public static final String CS_CERT = "cert";

   /** liste des CN des certificats clients attendus */
   public static final String CS_LISTE_CERT = "listeCert";

   /** Vérification controle de nommage */
   public static final String CS_VERIF_NOMMAGE = "verifNommage";

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public ContratServiceDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      cfTmpl = new ThriftColumnFamilyTemplate<String, String>(keyspace,
            CS_CFNAME, StringSerializer.get(), StringSerializer.get());

      cfTmpl.setCount(MAX_CS_ATTIBUTS);

   }

   /**
    * 
    * @return CassandraTemplate de <code>DroitContratService</code>
    */
   public final ColumnFamilyTemplate<String, String> getContratServiceTmpl() {

      return this.cfTmpl;
   }

   /**
    * 
    * @return Mutator de <code>DroitContratService</code>
    */
   public final Mutator<String> createMutator() {

      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());

      return mutator;

   }

   private void addColumn(ColumnFamilyUpdater<String, String> updater,
         String colName, String value, Serializer<String> nameSerializer,
         Serializer<String> valueSerializer, long clock) {

      HColumn<String, String> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   private void addListColumn(ColumnFamilyUpdater<String, String> updater,
         String colName, List<String> value, Serializer<String> nameSerializer,
         Serializer<List<String>> valueSerializer, long clock) {

      HColumn<String, List<String>> column = HFactory.createColumn(colName,
            value, nameSerializer, valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   private void addColumnBoolean(ColumnFamilyUpdater<String, String> updater,
         String colName, boolean value, Serializer<String> nameSerializer,
         Serializer<Boolean> valueSerializer, long clock) {

      HColumn<String, Boolean> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   private void addLongColumn(ColumnFamilyUpdater<String, String> updater,
         String colName, Long value, Serializer<String> nameSerializer,
         Serializer<Long> valueSerializer, long clock) {

      HColumn<String, Long> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   /**
    * ajoute une colonne {@value #CS_CODE_CLIENT}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritLibelle(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, CS_LIBELLE, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_VI_DUREE}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritViDuree(ColumnFamilyUpdater<String, String> updater,
         Long value, long clock) {

      addLongColumn(updater, CS_VI_DUREE, value, StringSerializer.get(),
            LongSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_DESCRIPTION}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritDescription(
         ColumnFamilyUpdater<String, String> updater, String value, long clock) {

      addColumn(updater, CS_DESCRIPTION, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_PKI}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritIdPki(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, CS_PKI, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_LISTE_PKI}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritListePki(ColumnFamilyUpdater<String, String> updater,
         List<String> value, long clock) {

      addListColumn(updater, CS_LISTE_PKI, value, StringSerializer.get(),
            ListSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_CERT}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritCert(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, CS_CERT, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_LISTE_CERT}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritListeCert(
         ColumnFamilyUpdater<String, String> updater, List<String> value,
         long clock) {

      addListColumn(updater, CS_LISTE_CERT, value, StringSerializer.get(),
            ListSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_CERT}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritFlagControlNommage(
         ColumnFamilyUpdater<String, String> updater, boolean value, long clock) {

      addColumnBoolean(updater, CS_VERIF_NOMMAGE, value,
            StringSerializer.get(), BooleanSerializer.get(), clock);

   }

   /**
    * Suppression d'un Contrat de service
    * 
    * @param mutator
    *           Mutator de <code>ContratService</code>
    * @param code
    *           identifiant du contrat de service
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionContratService(Mutator<String> mutator,
         String code, long clock) {

      mutator.addDeletion(code, CS_CFNAME, clock);

   }

   /**
    * @return the keyspace
    */
   public final Keyspace getKeyspace() {
      return keyspace;
   }
}
