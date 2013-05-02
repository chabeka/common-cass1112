package fr.urssaf.image.sae.metadata.referential.dao;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;

/**
 * Dao permettant d'éffectuer les opérations d'écriture sur la CF MetaData
 * 
 * 
 */
@SuppressWarnings("PMD.TooManyMethods")
@Repository
public class SaeMetadataDao extends AbstractDao<String, String> {

   public static final String META_SHORT_CODE = "sCode";
   public static final String META_TYPE = "type";
   public static final String META_REQ_ARCH = "reqArch";
   public static final String META_REQ_STOR = "reqStor";
   public static final String META_LENGTH = "length";
   public static final String META_PATTERN = "pattern";
   public static final String META_CONSUL = "cons";
   public static final String META_DEF_CONSUL = "defCons";
   public static final String META_SEARCH = "search";
   public static final String META_INTERNAL = "int";
   public static final String META_ARCH = "arch";
   public static final String META_LABEL = "label";
   public static final String META_DESCR = "descr";
   public static final String META_HAS_DICT = "hasDict";
   public static final String META_DICT_NAME = "dictName";
   public static final String META_INDEXED = "index";

   public static final String METADATA_CFNAME = "Metadata";

   /**
    * Contructeur
    * 
    * @param keyspace
    *           {@link Keyspace}
    */
   @Autowired
   public SaeMetadataDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * Renvoie le nom de la CF
    */
   @Override
   public String getColumnFamilyName() {
      return METADATA_CFNAME;
   }

   /**
    * Renvoie le sérializer d'une colonne
    */
   @Override
   public Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * renvoie le sérializer de la clé d'une ligne
    */
   @Override
   public Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * Ecrit le code court
    * 
    * @param sCode
    *           le code à écrire
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritShortCode(String sCode,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_SHORT_CODE, sCode, StringSerializer.get(), clock);
   }

   /**
    * Ecrit le type de la métadonnée
    * 
    * @param type
    *           la valeur du type
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritType(String type,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_TYPE, type, StringSerializer.get(), clock);
   }

   /**
    * Ecrit la valeur de la colonne reqArch
    * 
    * @param reqArch
    *           obligatoire à l'archivage
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritRequiredArchival(Boolean reqArch,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_REQ_ARCH, reqArch, BooleanSerializer.get(), clock);
   }

   /**
    * Ecrit la valeur de la colonne reqStor
    * 
    * @param reqStor
    *           obligatoire au stockage
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritRequiredStorage(Boolean reqStor,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_REQ_STOR, reqStor, BooleanSerializer.get(), clock);
   }

   /**
    * Ecrit longeur de la métadonnée
    * 
    * @param length
    *           la valeur de la longueure
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritLength(Integer length,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_LENGTH, length, IntegerSerializer.get(), clock);
   }

   /**
    * Ecrit le pattern à respecter par la métadonnée
    * 
    * @param pattern
    *           le pattern à respecter
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritPattern(String pattern,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_PATTERN, pattern, StringSerializer.get(), clock);
   }

   /**
    * Ecrit la valeur de la métadonnée cons
    * 
    * @param cons
    *           indique si la métadonnée est consultable
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritConsultable(Boolean cons,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_CONSUL, cons, BooleanSerializer.get(), clock);
   }

   /**
    * Ecrit la valeur de la métadonnée defCons
    * 
    * @param defCons
    *           indique si la métadonnée est consultable par défaut
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritDefaultConsultable(Boolean defCons,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_DEF_CONSUL, defCons, BooleanSerializer.get(),
            clock);
   }

   /**
    * Ecrit la valeur de la propriété search
    * 
    * @param search
    *           indique si la métadonnée est recherchable
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritSearchable(Boolean search,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_SEARCH, search, BooleanSerializer.get(), clock);
   }

   /**
    * Ecrit la valeur de la propriété int
    * 
    * @param internal
    *           Indique si la métadonnée est interne
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritInternal(Boolean internal,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_INTERNAL, internal, BooleanSerializer.get(),
            clock);
   }

   /**
    * Ecrit la valeur de la propriété arch
    * 
    * @param arch
    *           Indique si la métadonnée est archivable
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritArchivable(Boolean arch,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_ARCH, arch, BooleanSerializer.get(), clock);
   }

   /**
    * Ecrit la valeur de la propriété label
    * 
    * @param label
    *           la valeur du type
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritLabel(String label,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_LABEL, label, StringSerializer.get(), clock);
   }

   /**
    * Ecrit la description de la métadonnée
    * 
    * @param descr
    *           la valeur du type
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritDescription(String descr,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_DESCR, descr, StringSerializer.get(), clock);
   }

   /**
    * Ecrit la valeur de la propriété hasName
    * 
    * @param hasDict
    *           précise l'association à un dictionnaire
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritHasDictionary(Boolean hasDict,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_HAS_DICT, hasDict, BooleanSerializer.get(), clock);
   }

   /**
    * Ecrit le nom du dictionnaire associé à la métadonnée
    * 
    * @param dictName
    *           le nom du dictionnaire
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritDictionaryName(String dictName,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_DICT_NAME, dictName, StringSerializer.get(),
            clock);
   }

   /**
    * Ecrit la valeur de la propriété index
    * 
    * @param index
    *           indique si la métadonnée est indexée
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public void ecritIndexed(Boolean index,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_INDEXED, index, BooleanSerializer.get(), clock);
   }

}
