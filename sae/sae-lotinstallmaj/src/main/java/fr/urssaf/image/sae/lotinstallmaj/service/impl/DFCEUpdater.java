package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;
import fr.urssaf.image.sae.lotinstallmaj.iterator.AllRowsIterator;
import fr.urssaf.image.sae.lotinstallmaj.iterator.AllColumnsIterator;

/**
 * Classe permettant la mise à jour des donnees du keyspace Docubase dans
 * cassandra
 * 
 */
public class DFCEUpdater {

   /**
    * Nom du keyspace
    */
   private static final String DFCE_KEYSPACE_NAME = "Docubase";
   
   /**
    * Column family TermInfo.
    */
   private static final String CF_TERM_INFO = "TermInfo";
   
   /**
    * Column family BasesReference.
    */
   private static final String CF_BASES_REFERENCE = "BasesReference";
   
   /**
    * Colum family IndexReference.
    */
   private static final String CF_INDEX_REFERENCE = "IndexReference";
   
   /**
    * Caractere separateur pour IndexReference.
    */
   private static final char CARACTERE_SEPARATEUR = 65535;
   
   /**
    * Column family TermInfoRangeString.
    */
   private static final String CF_TERM_INFO_RANGE_STRING = "TermInfoRangeString";
   
   /**
    * Column family CompositeIndexesReference.
    */
   private static final String CF_COMPOSITE_INDEXES_REFERENCE = "CompositeIndexesReference";

   private final Cluster cluster;
   private Keyspace keyspace;
   private final Map<String, String> credentials;

   private static final Logger LOG = LoggerFactory.getLogger(DFCEUpdater.class);

   /**
    * Constructeur
    * 
    * @param config
    *           : configuration d'accès au cluster cassandra
    */
   public DFCEUpdater(CassandraConfig config) {
      credentials = new HashMap<String, String>();
      credentials.put("username", config.getLogin());
      credentials.put("password", config.getPassword());
      CassandraHostConfigurator chc = new CassandraHostConfigurator(
            config.getHosts());
      cluster = HFactory.getOrCreateCluster("SAECluster", chc, credentials);
   }

   private void connectToKeyspace() {
      if (keyspace != null)
         return;
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      keyspace = HFactory.createKeyspace(DFCE_KEYSPACE_NAME, cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);
   }

   /**
    * Permet de supprimer le contenu d'un index composite.
    * 
    * @param indexes
    *           Liste des index composites à supprimer
    */
   public final void disableCompositeIndex(Map<String[], String> indexes) {

      // On se connecte au keyspace
      connectToKeyspace();
      
      // calcul les nom d'index
      List<String> indexNames = new ArrayList<String>();
      for (Entry<String[], String> entry : indexes.entrySet()) {
         String[] index = entry.getKey();

         // calcul le nom de la cle
         StringBuffer nomCle = new StringBuffer();
         for (String meta : index) {
            nomCle.append(meta);
            nomCle.append('&');
         }
         indexNames.add(nomCle.toString());
      }
      
      /*---------------------------------------------------------
       *- 1 - Suppression des differentes valeurs dans TermInfo -
       *---------------------------------------------------------
       * Le parcours de TermInfo etant long, on le fait pour
       * tous les index a supprimer (en une fois)
       * Pour rappel, la cle de TermInfo comporte : 
       *   - l'espace de stockage de l'index (chaine vide ou RB)
       *   - le nom de l'index
       *   - la valeur de l'index
       *---------------------------------------------------------*/
      deleteIndexInTermInfo(indexNames);
      
      /*---------------------------------------------------------
       *- 2 - Traitement de la CF TermInfoRangeString -
       *---------------------------------------------------------
       * Pour rappel, la cle de TermInfoRangeString comporte :
       *   - l'espace de stockage de l'index (chaine vide ou RB)
       *   - le nom de l'index
       *   - l'uuid de la base dfce
       *   - le numero du range
       *---------------------------------------------------------*/
      Map<UUID, String> bases = getAllBases();
      for (UUID idBase : bases.keySet()) {
         LOG.debug("Traitement de la base {} ({})",
               new String[] { bases.get(idBase), idBase.toString() });

         for (String nomIndexComposite : indexNames) {
            List<Long> ranges = getAllRangesInIndexReferenceByNameAndBase(
                  nomIndexComposite, idBase.toString());
            for (Long numeroRange : ranges) {
               LOG.debug(
                     "Traitement du range {} de l'index {} pour la base {}",
                     new String[] { numeroRange.toString(), nomIndexComposite,
                           bases.get(idBase) });

               // suppression de l'index dans la CF TermInfoRangeString
               deleteIndexInTermInfoRangeString(nomIndexComposite, idBase,
                     numeroRange);
            }
            LOG.info(
                  "{} - {} ranges supprimés pour l'index {} et pour la base {}",
                  new String[] { CF_TERM_INFO_RANGE_STRING,
                        Integer.toString(ranges.size()), nomIndexComposite,
                        bases.get(idBase) });

            /*---------------------------------------------------------
             * Suppression dans la CF IndexReference
             *---------------------------------------------------------*/
            deleteIndexInIndexReference(nomIndexComposite, idBase.toString());
         }
      }
      
      /*---------------------------------------------------------
       *- 3 - Suppression dans la CF CompositeIndexesReference  -
       *---------------------------------------------------------*/
      for (String nomIndexComposite : indexNames) {

         // suppression de l'index dans la CF CompositeIndexesReference
         deleteIndexInCompositeIndexesReference(nomIndexComposite);
      }
   }
   
   /**
    * Methode privee permettant de supprimer des index composite dans la CF TermInfo.
    * @param indexesComposite liste des index composite a supprimer
    */
   private void deleteIndexInTermInfo(List<String> indexesComposite) {
      
      long compteur = 0;
      Map<String, Long> compteursSuppression = new HashMap<String, Long>();
      
      RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspace, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily(CF_TERM_INFO).setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(5000);
      
      AllRowsIterator<Composite, byte[], byte[]> iterateur = new AllRowsIterator<Composite, byte[], byte[]>(rangeQueryDocubase);
      
      while (iterateur.hasNext()) {
         Row<Composite, byte[], byte[]> row = iterateur.next();
         
         // la key est compose de :
         // - l'espace de stockage de l'index ("" pour l'index par defaut, RB pour la corbeille)
         // - le nom de l'index
         // - la valeur de l'index
         
         //String typeIndex = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
         String termField = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
         String valeurIndex = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(2));
         //LOGGER.debug("Row key {}:{}:{}", new String[] { typeIndex, termField, valeurIndex });
         
         if (indexesComposite.contains(termField)) {
            LOG.debug("{} - Suppression de la valeur '{}' indexée sur l'index {}", new String[] { CF_TERM_INFO, valeurIndex, termField });
            Mutator<Composite> mutator = HFactory.createMutator(
                  keyspace, CompositeSerializer.get());
            mutator.addDeletion(row.getKey(), CF_TERM_INFO);
            mutator.execute();
            if (!compteursSuppression.containsKey(termField)) {
               compteursSuppression.put(termField, Long.valueOf(1));
            } else {
               compteursSuppression.put(termField, compteursSuppression.get(termField) + 1);
            }
         }
         
         compteur++;
         if (compteur % 100000 == 0) {
            LOG.debug("{} - {} index analysés", CF_TERM_INFO, compteur);
         }
      }
      
      for (String index : compteursSuppression.keySet()) {
         LOG.info("{} - {} valeurs d'index supprimés pour l'index {}", new String[] { CF_TERM_INFO, compteursSuppression.get(index).toString(), index });
      }
   }
   
   /**
    * Methode permettant de recuperer toutes les bases dfce.
    * @return Map<UUID, String>
    */
   private Map<UUID, String> getAllBases() {
      Map<UUID, String> resultat = new HashMap<UUID, String>();
      
      RangeSlicesQuery<String, String, byte[]> queryDocubase = HFactory.createRangeSlicesQuery(keyspace, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily(CF_BASES_REFERENCE);
      queryDocubase.setKeys(null, null);
      queryDocubase.setRowCount(5000);
      queryDocubase.setColumnNames("uuid");
      
      QueryResult<OrderedRows<String, String, byte[]>> resultatQuery = queryDocubase.execute();
      if (resultatQuery.get() != null) {
         for (Row<String, String, byte[]> row : resultatQuery.get().getList()) {
            HColumn<String, byte[]> colonne = row.getColumnSlice().getColumnByName("uuid");
            if (colonne != null) {
               UUID idBase = UUIDSerializer.get().fromBytes(colonne.getValue());
               resultat.put(idBase, row.getKey());
            }
         }
      }
      return resultat;
   }
   
   /**
    * Methode permettant de recuperer l'ensemble de range de l'index pour la base demandee.
    * @param nomIndexComposite nom de l'index
    * @param idBase uuid de la base
    * @return List<Long>
    */
   private List<Long> getAllRangesInIndexReferenceByNameAndBase(String nomIndexComposite, String idBase) {
      List<Long> ranges = new ArrayList<Long>();
      
      StringBuffer rowKey = new StringBuffer();
      rowKey.append(nomIndexComposite);
      rowKey.append(CARACTERE_SEPARATEUR);
      rowKey.append(idBase);
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspace, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily(CF_INDEX_REFERENCE);
      queryDocubase.setKey(StringSerializer.get().toBytes(rowKey.toString()));
      
      AllColumnsIterator<String,byte[]> iterateur = new AllColumnsIterator<String, byte[]>(queryDocubase);
      while (iterateur.hasNext()) {
         HColumn<String, byte[]> colonne = iterateur.next();
         if (colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
            ranges.add(convertByteToLong(colonne.getValue()));
         } 
      }
      
      Collections.sort(ranges);
      return ranges;
   }
   
   /**
    * Methode permettant de convertir le bytes array en long.
    * @param bytes array de byte
    * @return Long
    */
   private Long convertByteToLong(byte[] bytes) {
      long value = 0;
      for (int i = 0; i < bytes.length; i++) {
         value = (value << 8) + (bytes[i] & 0xff);
      }
      return Long.valueOf(value);
   }
   
   private void deleteIndexInTermInfoRangeString(String nomIndexComposite, 
         UUID idBase, Long numeroRange) {
      
      // la cle de l'index est compose de :
      // - l'espace de stockage de l'index ("" pour l'index par defaut, RB pour la corbeille)
      // - le nom de l'index
      // - l'uuid de la base
      // - le numero du range de l'index
      Composite compositeKey = new Composite();
      compositeKey.add(0, "");
      compositeKey.add(1, nomIndexComposite);
      compositeKey.add(2, idBase);
      compositeKey.add(3, toByteArrayNumRow(numeroRange));
      
      SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspace, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
      queryDocubaseTerm.setColumnFamily(CF_TERM_INFO_RANGE_STRING);
      queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
      queryDocubaseTerm.setRange(null, null, false, 10);
      
      QueryResult<ColumnSlice<Composite, byte[]>> resultatQuery = queryDocubaseTerm.execute();
      if (resultatQuery.get() != null && !resultatQuery.get().getColumns().isEmpty()) {
         LOG.debug("{} - Suppression du range {} de l'index {} pour la base {}", new String[] { CF_TERM_INFO_RANGE_STRING, numeroRange.toString(), nomIndexComposite, idBase.toString() });
         Mutator<byte[]> mutator = HFactory.createMutator(
               keyspace, BytesArraySerializer.get());
         mutator.addDeletion(CompositeSerializer.get().toBytes(compositeKey), CF_TERM_INFO_RANGE_STRING);
         mutator.execute();
      }
   }
   
   /**
    * Methode permettant de convertir un long en byte[] sans depassement de capacite.
    * Cela signifie que l'on se base sur la valeur pour savoir sur combien d'octet sera
    * le byte.
    * @param valeur
    * @return byte[]
    */
   private byte[] toByteArrayNumRow(long valeur) {
      int nbOctet;
      if (valeur <= Byte.MAX_VALUE) {
         nbOctet = 1;
      } else if (valeur > Byte.MAX_VALUE && valeur <= Short.MAX_VALUE) {
         nbOctet = 2;
      } else if (valeur > Short.MAX_VALUE && valeur <= Integer.MAX_VALUE) {
         nbOctet = 4;
      } else if (valeur > Integer.MAX_VALUE && valeur <= Long.MAX_VALUE) {
         nbOctet = 8;
      } else {
         throw new IllegalArgumentException("valeur trop grande");
      }
      byte[] result = new byte[nbOctet];
      for (int i = nbOctet - 1; i >= 0; i--) {
        result[i] = (byte) (valeur & 0xffL);
        valeur >>= 8;
      }
      return result;
   }
   
   /**
    * Methode permettant de supprimer l'index dans la CF IndexReference.
    * @param nomIndexComposite nom de l'index
    * @param idBase uuid de la base
    */
   private void deleteIndexInIndexReference(String nomIndexComposite, 
         String idBase) {
      
      StringBuffer rowKey = new StringBuffer();
      rowKey.append(nomIndexComposite);
      rowKey.append(CARACTERE_SEPARATEUR);
      rowKey.append(idBase);
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspace, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily(CF_INDEX_REFERENCE);
      queryDocubase.setKey(StringSerializer.get().toBytes(rowKey.toString()));
      queryDocubase.setRange(null, null, false, 10); // recupere 10 colonnes
      QueryResult<ColumnSlice<String,byte[]>> resultIndexComposite = queryDocubase
            .execute();
      
      if (resultIndexComposite.get() != null && !resultIndexComposite.get().getColumns().isEmpty()) {
         LOG.info("{} - Suppression de l'index de reference de l'index {} pour la base {}", new String[] { CF_INDEX_REFERENCE, nomIndexComposite, idBase });
         Mutator<byte[]> mutator = HFactory.createMutator(
               keyspace, BytesArraySerializer.get());
         mutator.addDeletion(StringSerializer.get().toBytes(rowKey.toString()), CF_INDEX_REFERENCE);
         mutator.execute();
      }
   }
   
   /**
    * Methode permettant de supprimer l'index composite dans la CF CompositeIndexesReference.
    * @param nomIndexComposite nom de l'index composite
    */
   private void deleteIndexInCompositeIndexesReference(String nomIndexComposite) {

      SliceQuery<String, String, String> queryIndexComposite = HFactory
            .createSliceQuery(keyspace,
                  StringSerializer.get(), StringSerializer.get(),
                  StringSerializer.get());
      queryIndexComposite.setColumnFamily(CF_COMPOSITE_INDEXES_REFERENCE);
      queryIndexComposite.setKey(nomIndexComposite);
      queryIndexComposite.setRange(null, null, false, 10); // recupere 10 colonnes
      QueryResult<ColumnSlice<String,String>> resultIndexComposite = queryIndexComposite
            .execute();
      
      if (resultIndexComposite.get() != null && !resultIndexComposite.get().getColumns().isEmpty()) {
         LOG.info("{} - Suppression de l'index composite {}",
               CF_COMPOSITE_INDEXES_REFERENCE, nomIndexComposite);
         Mutator<String> mutator = HFactory.createMutator(
               keyspace, StringSerializer.get());
         mutator.addDeletion(nomIndexComposite, CF_COMPOSITE_INDEXES_REFERENCE);
         mutator.execute();
      }      
   }

   /**
    * Indexe à vide les index composite si nécessaire (met les colonnes indexed
    * et computed à true dans la colonne de famille CompositeIndexesReference
    * 
    * @param indexes
    *           Liste des index composites à créer
    */
   public final void indexeAVideCompositeIndex(String indexName) {

      // On se connecte au keyspace
      connectToKeyspace();

      // On indexe l'index composite si ce n'est pas déjà le cas dans DFCE
      if (!isCompositeIndexComputed(indexName)) {

         updateColumn(CF_COMPOSITE_INDEXES_REFERENCE, indexName, "computed", true);

      }
   }

   
   /**
    * Indexe à vide un index simple si nécessaire (met les colonnes indexed
    * et computed à true dans la colonne de famille BaseCategoriesReference
    * 
    * @param indexes
    *           Liste des index composites à créer
    */
   public final void indexeAVideIndexSimple(String indexName, String baseName) {

      // On se connecte au keyspace
      connectToKeyspace();
      
      // creation de la rowKey
      StringBuffer buffer = new StringBuffer();
      buffer.append(baseName);
      buffer.append((char) 65535);
      buffer.append(indexName);
      String rowKey = buffer.toString();

      // On indexe l'index composite si ce n'est pas déjà le cas dans DFCE
      if (!isMetaIndexedAndComputed(rowKey)) {
         updateColumn("BaseCategoriesReference", rowKey, "indexed", true);
         updateColumn("BaseCategoriesReference", rowKey, "computed", true);
      }
   }
   
   /**
    * Vérifie si l'index est déjà indexée dans DFCE (computed à true)
    * 
    * @param indexName
    *           le nom de l'index
    * @return vrai si l'index est indexée
    */
   private boolean isCompositeIndexComputed(String indexName) {

      boolean isIndexee = false;

      SliceQuery<String, String, byte[]> queryDocubase = HFactory
            .createSliceQuery(keyspace, StringSerializer.get(),
                  StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily(CF_COMPOSITE_INDEXES_REFERENCE);
      queryDocubase.setKey(indexName);
      queryDocubase.setColumnNames("computed");

      QueryResult<ColumnSlice<String, byte[]>> resultat = queryDocubase
            .execute();
      if (resultat.get() != null && !resultat.get().getColumns().isEmpty()) {
         HColumn<String, byte[]> isComputed = resultat.get().getColumnByName(
               "computed");
         if (isComputed != null) {
            isIndexee = BooleanSerializer.get()
                  .fromBytes(isComputed.getValue());
         }
      }
      return isIndexee;
   }

   /**
    * Methode permettant de verifier qu'une métadonnée est indexée
    * et que l'index est 'actif'.
    * @param rowKey nom de la métadonnées
    * @return boolean indiquant s'il y a quelquechose a faire
    */
   public boolean isMetaIndexedAndComputed(String rowKey) {

      SliceQuery<byte[], String, byte[]> queryDocubase = HFactory
            .createSliceQuery(keyspace, BytesArraySerializer.get(),
                  StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("BaseCategoriesReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(rowKey));
      queryDocubase.setColumnNames("indexed", "computed");

      boolean valeurRetour = false;
      
      QueryResult<ColumnSlice<String, byte[]>> resultat = queryDocubase
            .execute();
      if (resultat.get() != null && !resultat.get().getColumns().isEmpty()) {
         HColumn<String, byte[]> isIndexed = resultat.get().getColumnByName(
               "indexed");
         HColumn<String, byte[]> isComputed = resultat.get().getColumnByName(
               "computed");

         
         if (isIndexed != null && isComputed != null) {
            boolean valeurIndexed = BooleanSerializer.get().fromBytes(
                  isIndexed.getValue());
            boolean valeurComputed = BooleanSerializer.get().fromBytes(
                  isComputed.getValue());

            if (valeurIndexed && valeurComputed) {
               valeurRetour = true;
            }
         }
      }
      return valeurRetour;
   }

   private void updateColumn(String CFName, String rowName, String columnName,
         Object value) {

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, CFName, StringSerializer.get(), StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater(rowName);

      Collection<String> columnNames = cfTmpl.queryColumns(rowName)
            .getColumnNames();

      if (columnNames.contains(columnName)) {

         if (value != null && value instanceof String) {
            HColumn<String, String> column = HFactory.createColumn(columnName,
                  (String) value, StringSerializer.get(),
                  StringSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Long) {
            HColumn<String, Long> column = HFactory.createColumn(columnName,
                  (Long) value, StringSerializer.get(), LongSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Date) {
            HColumn<String, Date> column = HFactory.createColumn(columnName,
                  (Date) value, StringSerializer.get(), DateSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Boolean) {
            HColumn<String, Boolean> column = HFactory.createColumn(columnName,
                  (Boolean) value, StringSerializer.get(),
                  BooleanSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null) {
            LOG.info("Type de valeur non prise en charge : "
                  + value.getClass().getName());
         }

      } else {
         LOG.info("Column " + columnName + " inexistante pour la key "
               + rowName + " dans la CF " + CFName);
      }
   }
}
