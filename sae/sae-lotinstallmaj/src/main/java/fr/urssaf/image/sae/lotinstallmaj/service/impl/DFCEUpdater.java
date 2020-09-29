package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.lotinstallmaj.iterator.AllColumnsIterator;
import fr.urssaf.image.sae.lotinstallmaj.iterator.AllRowsIterator;
import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.DFCEKeyspaceConnecter;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
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

/**
 * Classe permettant la mise à jour des donnees du keyspace Docubase dans
 * cassandra
 * 
 */
public class DFCEUpdater {

  /**
   * Nom du keyspace
   */
  private static final String DFCE_KEYSPACE_NAME = "dfce";

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
  private static final String CF_COMPOSITE_INDEXES_REFERENCE = "composite_index";

  private final Cluster cluster;
  private Keyspace keyspace;
  private final Map<String, String> credentials;

  private static final Logger LOG = LoggerFactory.getLogger(DFCEUpdater.class);


  private DFCEKeyspaceConnecter dfcecf;
  /**
   * Constructeur
   * 
   * @param config
   *           : configuration d'accès au cluster cassandra
   */
  public DFCEUpdater(final CassandraConfig config) {
    credentials = new HashMap<>();
    credentials.put("username", config.getLogin());
    credentials.put("password", config.getPassword());
    final CassandraHostConfigurator chc = new CassandraHostConfigurator(
                                                                        config.getHosts());
    cluster = HFactory.getOrCreateCluster("SAECluster", chc, credentials);
  }

  /**
   * Constructeur
   * 
   * @param config
   *          : configuration d'accès au cluster cassandra
   */
  public DFCEUpdater(final CassandraConfig config, final DFCEKeyspaceConnecter dfcecf) {
    credentials = new HashMap<>();
    credentials.put("username", config.getLogin());
    credentials.put("password", config.getPassword());
    final CassandraHostConfigurator chc = new CassandraHostConfigurator(
                                                                        config.getHosts());
    cluster = HFactory.getOrCreateCluster("SAECluster", chc, credentials);
    this.dfcecf = dfcecf;
  }

  private void connectToKeyspace() {
    if (keyspace != null) {
      return;
    }
    final ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
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
  public final void disableCompositeIndex(final Map<String[], String> indexes) {

    // On se connecte au keyspace
    connectToKeyspace();

    // calcul les nom d'index
    final List<String> indexNames = new ArrayList<>();
    for (final Entry<String[], String> entry : indexes.entrySet()) {
      final String[] index = entry.getKey();

      // calcul le nom de la cle
      final StringBuffer nomCle = new StringBuffer();
      for (final String meta : index) {
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
    final Map<UUID, String> bases = getAllBases();
    for (final Map.Entry<UUID, String> entry : bases.entrySet()) {
      final UUID idBase = entry.getKey();
      LOG.debug("Traitement de la base {} ({})",
                new String[] {entry.getValue(), idBase.toString()});

      for (final String nomIndexComposite : indexNames) {
        final List<Long> ranges = getAllRangesInIndexReferenceByNameAndBase(
                                                                            nomIndexComposite, idBase.toString());
        for (final Long numeroRange : ranges) {
          LOG.debug(
                    "Traitement du range {} de l'index {} pour la base {}",
                    new String[] { numeroRange.toString(), nomIndexComposite,
                                   entry.getValue()});

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
    for (final String nomIndexComposite : indexNames) {

      // suppression de l'index dans la CF CompositeIndexesReference
      deleteIndexInCompositeIndexesReference(nomIndexComposite);
    }
  }

  /**
   * Methode privee permettant de supprimer des index composite dans la CF TermInfo.
   * @param indexesComposite liste des index composite a supprimer
   */
  private void deleteIndexInTermInfo(final List<String> indexesComposite) {

    long compteur = 0;
    final Map<String, Long> compteursSuppression = new HashMap<>();

    final RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspace, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
    rangeQueryDocubase.setColumnFamily(CF_TERM_INFO).setKeys(null, null);
    rangeQueryDocubase.setReturnKeysOnly();
    rangeQueryDocubase.setRowCount(5000);

    final AllRowsIterator<Composite, byte[], byte[]> iterateur = new AllRowsIterator<>(rangeQueryDocubase);

    while (iterateur.hasNext()) {
      final Row<Composite, byte[], byte[]> row = iterateur.next();

      // la key est compose de :
      // - l'espace de stockage de l'index ("" pour l'index par defaut, RB pour la corbeille)
      // - le nom de l'index
      // - la valeur de l'index

      //String typeIndex = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
      final String termField = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
      final String valeurIndex = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(2));
      //LOGGER.debug("Row key {}:{}:{}", new String[] { typeIndex, termField, valeurIndex });

      if (indexesComposite.contains(termField)) {
        LOG.debug("{} - Suppression de la valeur '{}' indexée sur l'index {}", new String[] { CF_TERM_INFO, valeurIndex, termField });
        final Mutator<Composite> mutator = HFactory.createMutator(
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
    for (final Map.Entry<String, Long> entry : compteursSuppression.entrySet()) {
      LOG.info("{} - {} valeurs d'index supprimés pour l'index {}", new String[] {CF_TERM_INFO, entry.getValue().toString(), entry.getKey()});
    }
  }

  /**
   * Methode permettant de recuperer toutes les bases dfce.
   * @return Map<UUID, String>
   */
  private Map<UUID, String> getAllBases() {
    final Map<UUID, String> resultat = new HashMap<>();

    final RangeSlicesQuery<String, String, byte[]> queryDocubase = HFactory.createRangeSlicesQuery(keyspace, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
    queryDocubase.setColumnFamily(CF_BASES_REFERENCE);
    queryDocubase.setKeys(null, null);
    queryDocubase.setRowCount(5000);
    queryDocubase.setColumnNames("uuid");

    final QueryResult<OrderedRows<String, String, byte[]>> resultatQuery = queryDocubase.execute();
    if (resultatQuery.get() != null) {
      for (final Row<String, String, byte[]> row : resultatQuery.get().getList()) {
        final HColumn<String, byte[]> colonne = row.getColumnSlice().getColumnByName("uuid");
        if (colonne != null) {
          final UUID idBase = UUIDSerializer.get().fromBytes(colonne.getValue());
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
  private List<Long> getAllRangesInIndexReferenceByNameAndBase(final String nomIndexComposite, final String idBase) {
    final List<Long> ranges = new ArrayList<>();

    final StringBuffer rowKey = new StringBuffer();
    rowKey.append(nomIndexComposite);
    rowKey.append(CARACTERE_SEPARATEUR);
    rowKey.append(idBase);

    final SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspace, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
    queryDocubase.setColumnFamily(CF_INDEX_REFERENCE);
    queryDocubase.setKey(StringSerializer.get().toBytes(rowKey.toString()));

    final AllColumnsIterator<String,byte[]> iterateur = new AllColumnsIterator<>(queryDocubase);
    while (iterateur.hasNext()) {
      final HColumn<String, byte[]> colonne = iterateur.next();
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
  private Long convertByteToLong(final byte[] bytes) {
    long value = 0;
    for (final byte b : bytes) {
      value = (value << 8) + (b & 0xff);
    }
    return Long.valueOf(value);
  }

  private void deleteIndexInTermInfoRangeString(final String nomIndexComposite, 
                                                final UUID idBase, final Long numeroRange) {

    // la cle de l'index est compose de :
    // - l'espace de stockage de l'index ("" pour l'index par defaut, RB pour la corbeille)
    // - le nom de l'index
    // - l'uuid de la base
    // - le numero du range de l'index
    final Composite compositeKey = new Composite();
    compositeKey.add(0, "");
    compositeKey.add(1, nomIndexComposite);
    compositeKey.add(2, idBase);
    compositeKey.add(3, toByteArrayNumRow(numeroRange));

    final SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspace, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
    queryDocubaseTerm.setColumnFamily(CF_TERM_INFO_RANGE_STRING);
    queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
    queryDocubaseTerm.setRange(null, null, false, 10);

    final QueryResult<ColumnSlice<Composite, byte[]>> resultatQuery = queryDocubaseTerm.execute();
    if (resultatQuery.get() != null && !resultatQuery.get().getColumns().isEmpty()) {
      LOG.debug("{} - Suppression du range {} de l'index {} pour la base {}", new String[] { CF_TERM_INFO_RANGE_STRING, numeroRange.toString(), nomIndexComposite, idBase.toString() });
      final Mutator<byte[]> mutator = HFactory.createMutator(
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
    final byte[] result = new byte[nbOctet];
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
  private void deleteIndexInIndexReference(final String nomIndexComposite, 
                                           final String idBase) {

    final StringBuffer rowKey = new StringBuffer();
    rowKey.append(nomIndexComposite);
    rowKey.append(CARACTERE_SEPARATEUR);
    rowKey.append(idBase);

    final SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspace, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
    queryDocubase.setColumnFamily(CF_INDEX_REFERENCE);
    queryDocubase.setKey(StringSerializer.get().toBytes(rowKey.toString()));
    queryDocubase.setRange(null, null, false, 10); // recupere 10 colonnes
    final QueryResult<ColumnSlice<String,byte[]>> resultIndexComposite = queryDocubase
        .execute();

    if (resultIndexComposite.get() != null && !resultIndexComposite.get().getColumns().isEmpty()) {
      LOG.info("{} - Suppression de l'index de reference de l'index {} pour la base {}", new String[] { CF_INDEX_REFERENCE, nomIndexComposite, idBase });
      final Mutator<byte[]> mutator = HFactory.createMutator(
                                                             keyspace, BytesArraySerializer.get());
      mutator.addDeletion(StringSerializer.get().toBytes(rowKey.toString()), CF_INDEX_REFERENCE);
      mutator.execute();
    }
  }

  /**
   * Methode permettant de supprimer l'index composite dans la CF CompositeIndexesReference.
   * @param nomIndexComposite nom de l'index composite
   */
  private void deleteIndexInCompositeIndexesReference(final String nomIndexComposite) {

    final SliceQuery<String, String, String> queryIndexComposite = HFactory
        .createSliceQuery(keyspace,
                          StringSerializer.get(), StringSerializer.get(),
                          StringSerializer.get());
    queryIndexComposite.setColumnFamily(CF_COMPOSITE_INDEXES_REFERENCE);
    queryIndexComposite.setKey(nomIndexComposite);
    queryIndexComposite.setRange(null, null, false, 10); // recupere 10 colonnes
    final QueryResult<ColumnSlice<String,String>> resultIndexComposite = queryIndexComposite
        .execute();

    if (resultIndexComposite.get() != null && !resultIndexComposite.get().getColumns().isEmpty()) {
      LOG.info("{} - Suppression de l'index composite {}",
               CF_COMPOSITE_INDEXES_REFERENCE, nomIndexComposite);
      final Mutator<String> mutator = HFactory.createMutator(
                                                             keyspace, StringSerializer.get());
      mutator.addDeletion(nomIndexComposite, CF_COMPOSITE_INDEXES_REFERENCE);
      mutator.execute();
    }      
  }




  /**
   * Vérifie si l'index est déjà indexée dans DFCE (computed à true)
   * 
   * @param indexName
   *          le nom de l'index
   * @return vrai si l'index est indexée
   * @deprecated Il faut migrer vers la librairie CQL pour pouvoir faire des requetes dans la base DFCE.
   */
  @Deprecated
  private boolean isCompositeIndexComputed(final String indexName) {

    boolean isIndexee = false;

    final SliceQuery<String, String, byte[]> queryDocubase = HFactory
        .createSliceQuery(keyspace, StringSerializer.get(),
                          StringSerializer.get(), BytesArraySerializer.get());
    queryDocubase.setColumnFamily(CF_COMPOSITE_INDEXES_REFERENCE);
    queryDocubase.setKey(indexName);
    queryDocubase.setColumnNames("computed");

    final QueryResult<ColumnSlice<String, byte[]>> resultat = queryDocubase
        .execute();
    if (resultat.get() != null && !resultat.get().getColumns().isEmpty()) {
      final HColumn<String, byte[]> isComputed = resultat.get().getColumnByName(
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
  public boolean isMetaIndexedAndComputed(final String rowKey) {

    final SliceQuery<byte[], String, byte[]> queryDocubase = HFactory
        .createSliceQuery(keyspace, BytesArraySerializer.get(),
                          StringSerializer.get(), BytesArraySerializer.get());
    queryDocubase.setColumnFamily("BaseCategoriesReference");
    queryDocubase.setKey(StringSerializer.get().toBytes(rowKey));
    queryDocubase.setColumnNames("indexed", "computed");

    boolean valeurRetour = false;

    final QueryResult<ColumnSlice<String, byte[]>> resultat = queryDocubase
        .execute();
    if (resultat.get() != null && !resultat.get().getColumns().isEmpty()) {
      final HColumn<String, byte[]> isIndexed = resultat.get().getColumnByName(
          "indexed");
      final HColumn<String, byte[]> isComputed = resultat.get().getColumnByName(
          "computed");


      if (isIndexed != null && isComputed != null) {
        final boolean valeurIndexed = BooleanSerializer.get().fromBytes(
                                                                        isIndexed.getValue());
        final boolean valeurComputed = BooleanSerializer.get().fromBytes(
                                                                         isComputed.getValue());

        if (valeurIndexed && valeurComputed) {
          valeurRetour = true;
        }
      }
    }
    return valeurRetour;
  }


  /**
   * Methode de création du fichier CQL d'update des indexes composites.
   * 
   * @param cFName
   *          Nom de CF
   * @param rowName
   *          Nom de la ligne
   * @param columnName
   *          Nom de la colonne
   * @param value
   *          Valeur
   * @param pathFichierUpdateCql
   * @throws IOException
   * @{@link IOException}
   */
  private void filesCQLWrite(final String cFName, final String rowName, final String columnName, final Object value, final String pathFichierUpdateCql) throws IOException {

    final StringBuffer sbf = prepareCQLUpdateTrueQuery(cFName, rowName, columnName);

    final String cqlReq = sbf.toString();
    LOG.info("Requete CQL = " + cqlReq);

    final Path fileCQL = Paths.get(pathFichierUpdateCql);
    if (Files.notExists(fileCQL, LinkOption.NOFOLLOW_LINKS)) {
      Files.createFile(fileCQL);
    }

    Files.write(fileCQL, cqlReq.getBytes(), StandardOpenOption.APPEND);

  }

  /**
   * Methode de création de la requete d'update de la base de données.
   * 
   * @param cFName
   * @param rowName
   * @param columnName
   * @param value
   * @return
   */
  private StringBuffer prepareCQLUpdateTrueQuery(final String cFName, final String rowName, final String columnName) {
    final String spaceString = " ";
    final StringBuffer sbf = new StringBuffer();

    sbf.append("UPDATE " + DFCE_KEYSPACE_NAME + "." + cFName + spaceString);
    sbf.append("SET " + columnName + "=" + Boolean.TRUE + spaceString);
    sbf.append("WHERE id='" + rowName + "'");
    sbf.append(" IF " + columnName + "=" + Boolean.FALSE);
    sbf.append(";");

    return sbf;
  }
}
