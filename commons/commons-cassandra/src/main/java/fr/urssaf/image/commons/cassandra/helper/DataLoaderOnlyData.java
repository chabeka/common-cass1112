/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cassandraunit.LoadingOption;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.model.ColumnFamilyModel;
import org.cassandraunit.model.ColumnMetadataModel;
import org.cassandraunit.model.ColumnModel;
import org.cassandraunit.model.CompactionStrategyOptionModel;
import org.cassandraunit.model.KeyspaceModel;
import org.cassandraunit.model.RowModel;
import org.cassandraunit.model.SuperColumnModel;
import org.cassandraunit.serializer.GenericTypeSerializer;
import org.cassandraunit.type.GenericType;
import org.cassandraunit.type.GenericTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO (AC75095351) Description du type
 *
 */

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HCounterColumn;
import me.prettyprint.hector.api.beans.HCounterSuperColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * @author Jeremy Sevellec
 * @author Marc Carre (#27)
 */
public class DataLoaderOnlyData {
  Cluster cluster = null;

  private static final Logger log = LoggerFactory.getLogger(DataLoaderOnlyData.class);

  public DataLoaderOnlyData(final String clusterName, final String host) {
    super();
    cluster = HFactory.getOrCreateCluster(clusterName, host);
  }

  protected Cluster getCluster() {
    return cluster;
  }

  public void load(final DataSet dataSet) {
    load(dataSet, new LoadingOption());
  }

  public void load(final DataSet dataSet, final LoadingOption loadingOption) {
    load(dataSet, loadingOption, true);
  }

  public void load(final DataSet dataSet, final boolean dropAndCreateKeyspace) {
    load(dataSet, new LoadingOption(), dropAndCreateKeyspace);
  }

  public void loadOnlyData(final DataSet dataSet, final boolean dropAndCreateKeyspace, final boolean onlyData) {
    load(dataSet, new LoadingOption(), dropAndCreateKeyspace, onlyData);
  }

  public void load(final DataSet dataSet, final LoadingOption loadingOption, final boolean dropAndCreateKeyspace) {
    final KeyspaceModel dataSetKeyspace = dataSet.getKeyspace();

    if (dropAndCreateKeyspace) {
      dropKeyspaceIfExist(dataSetKeyspace.getName());
    }

    final KeyspaceDefinition keyspaceDefinition = createKeyspaceDefinition(dataSet, loadingOption);
    if (dropAndCreateKeyspace) {
      cluster.addKeyspace(keyspaceDefinition, dropAndCreateKeyspace);
    } else {
      for (final ColumnFamilyDefinition columnFamilyDefinition : keyspaceDefinition.getCfDefs()) {
        cluster.addColumnFamily(columnFamilyDefinition);
      }
    }

    log.info("creating keyspace : {}", keyspaceDefinition.getName());
    final Keyspace keyspace = HFactory.createKeyspace(dataSet.getKeyspace().getName(), cluster);

    if (!loadingOption.isOnlySchema()) {
      log.info("loading data into keyspace : {}", keyspaceDefinition.getName());
      loadData(dataSet, keyspace);
    }
  }

  public void load(final DataSet dataSet, final LoadingOption loadingOption, final boolean dropAndCreateKeyspace, final boolean onlyData) {
    final KeyspaceModel dataSetKeyspace = dataSet.getKeyspace();

    if (dropAndCreateKeyspace) {
      dropKeyspaceIfExist(dataSetKeyspace.getName());
    }

    final KeyspaceDefinition keyspaceDefinition = createKeyspaceDefinition(dataSet, loadingOption);
    if (dropAndCreateKeyspace) {
      cluster.addKeyspace(keyspaceDefinition, dropAndCreateKeyspace);
    } else if (!onlyData) {
      for (final ColumnFamilyDefinition columnFamilyDefinition : keyspaceDefinition.getCfDefs()) {
        cluster.addColumnFamily(columnFamilyDefinition);
      }
    }

    log.info("creating keyspace : {}", keyspaceDefinition.getName());
    final Keyspace keyspace = HFactory.createKeyspace(dataSet.getKeyspace().getName(), cluster);

    if (!loadingOption.isOnlySchema()) {
      log.info("loading data into keyspace : {}", keyspaceDefinition.getName());
      loadData(dataSet, keyspace);
    }
  }

  private KeyspaceModel overrideKeyspaceValueIfneeded(final KeyspaceModel keyspace, final LoadingOption loadingOption) {
    if (loadingOption.isOverrideReplicationFactor()) {
      keyspace.setReplicationFactor(loadingOption.getReplicationFactor());
    }

    if (loadingOption.isOverrideStrategy()) {
      keyspace.setStrategy(loadingOption.getStrategy());
    }

    return keyspace;
  }

  private KeyspaceDefinition createKeyspaceDefinition(final DataSet dataSet, final LoadingOption loadingOption) {
    final List<ColumnFamilyDefinition> columnFamilyDefinitions = createColumnFamilyDefinitions(dataSet);

    KeyspaceModel dataSetKeyspace = dataSet.getKeyspace();

    dataSetKeyspace = overrideKeyspaceValueIfneeded(dataSetKeyspace, loadingOption);

    final KeyspaceDefinition keyspaceDefinition = HFactory.createKeyspaceDefinition(dataSetKeyspace.getName(),
                                                                                    dataSetKeyspace.getStrategy().value(),
                                                                                    dataSetKeyspace.getReplicationFactor(),
                                                                                    columnFamilyDefinitions);
    return keyspaceDefinition;
  }

  private void dropKeyspaceIfExist(final String keyspaceName) {
    final KeyspaceDefinition existedKeyspace = cluster.describeKeyspace(keyspaceName);
    if (existedKeyspace != null) {
      log.info("dropping existing keyspace : {}", existedKeyspace.getName());
      cluster.dropKeyspace(keyspaceName, true);
    }
  }

  public void loadData(final DataSet dataSet, final Keyspace keyspace) {

    for (final ColumnFamilyModel columnFamily : dataSet.getColumnFamilies()) {
      loadColumnFamilyData(columnFamily, keyspace);
    }
  }

  /*
   * public void loadData(final DataSet dataSet, final String keyspaceName) {
   * for (final ColumnFamilyModel columnFamily : dataSet.getColumnFamilies()) {
   * loadColumnFamilyData(columnFamily, keyspace);
   * }
   * }
   */

  private void loadColumnFamilyData(final ColumnFamilyModel columnFamily, final Keyspace keyspace) {

    final Mutator<GenericType> mutator = HFactory.createMutator(keyspace, GenericTypeSerializer.get());
    for (final RowModel row : columnFamily.getRows()) {
      switch (columnFamily.getType()) {
      case STANDARD:
        loadStandardColumnFamilyData(columnFamily, mutator, row);
        break;
      case SUPER:
        loadSuperColumnFamilyData(columnFamily, mutator, row);
        break;
      default:
        break;
      }

    }
    mutator.execute();

  }

  private void loadSuperColumnFamilyData(final ColumnFamilyModel columnFamily, final Mutator<GenericType> mutator, final RowModel row) {
    if (columnFamily.isCounter()) {
      for (final SuperColumnModel superColumnModel : row.getSuperColumns()) {
        final HCounterSuperColumn<GenericType, GenericType> superCounterColumn = HFactory.createCounterSuperColumn(
                                                                                                                   superColumnModel.getName(),
                                                                                                                   createHCounterColumnList(superColumnModel.getColumns()),
                                                                                                                   GenericTypeSerializer.get(),
                                                                                                                   GenericTypeSerializer.get());
        mutator.addCounter(row.getKey(), columnFamily.getName(), superCounterColumn);
      }
    } else {
      for (final SuperColumnModel superColumnModel : row.getSuperColumns()) {
        final HSuperColumn<GenericType, GenericType, GenericType> superColumn = HFactory.createSuperColumn(
                                                                                                           superColumnModel.getName(),
                                                                                                           createHColumnList(superColumnModel.getColumns()),
                                                                                                           GenericTypeSerializer.get(),
                                                                                                           GenericTypeSerializer.get(),
                                                                                                           GenericTypeSerializer.get());
        mutator.addInsertion(row.getKey(), columnFamily.getName(), superColumn);
      }
    }
  }

  private void loadStandardColumnFamilyData(final ColumnFamilyModel columnFamily, final Mutator<GenericType> mutator, final RowModel row) {
    if (columnFamily.isCounter()) {
      for (final HCounterColumn<GenericType> hCounterColumn : createHCounterColumnList(row.getColumns())) {
        mutator.addCounter(row.getKey(), columnFamily.getName(), hCounterColumn);
      }
    } else {
      for (final HColumn<GenericType, GenericType> hColumn : createHColumnList(row.getColumns())) {
        mutator.addInsertion(row.getKey(), columnFamily.getName(), hColumn);
      }
    }
  }

  private List<HColumn<GenericType, GenericType>> createHColumnList(final List<ColumnModel> columnsModel) {
    final List<HColumn<GenericType, GenericType>> hColumns = new ArrayList<>();
    for (final ColumnModel columnModel : columnsModel) {
      GenericType columnValue = columnModel.getValue();
      if (columnValue == null) {
        columnValue = new GenericType("", GenericTypeEnum.BYTES_TYPE);
      }
      Long timestamp = columnModel.getTimestamp();
      if (timestamp == null) {
        timestamp = System.currentTimeMillis();
      }
      final HColumn<GenericType, GenericType> column = HFactory.createColumn(columnModel.getName(),
                                                                             columnValue,
                                                                             timestamp,
                                                                             GenericTypeSerializer.get(),
                                                                             GenericTypeSerializer.get());
      hColumns.add(column);
    }
    return hColumns;
  }

  private List<HCounterColumn<GenericType>> createHCounterColumnList(final List<ColumnModel> columnsModel) {
    final List<HCounterColumn<GenericType>> hColumns = new ArrayList<>();
    for (final ColumnModel columnModel : columnsModel) {
      final HCounterColumn<GenericType> column = HFactory.createCounterColumn(columnModel.getName(),
                                                                              LongSerializer
                                                                              .get()
                                                                              .fromByteBuffer(GenericTypeSerializer.get()
                                                                                              .toByteBuffer(columnModel.getValue())),
                                                                              GenericTypeSerializer.get());
      hColumns.add(column);
    }
    return hColumns;
  }

  private List<ColumnFamilyDefinition> createColumnFamilyDefinitions(final DataSet dataSet) {
    final KeyspaceModel dataSetKeyspace = dataSet.getKeyspace();
    final List<ColumnFamilyDefinition> columnFamilyDefinitions = new ArrayList<>();
    for (final ColumnFamilyModel columnFamily : dataSet.getColumnFamilies()) {
      final ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(dataSetKeyspace.getName(),
                                                                                 columnFamily.getName(),
                                                                                 ComparatorType.getByClassName(columnFamily.getComparatorType().getClassName()),
                                                                                 createColumnsDefinition(columnFamily.getColumnsMetadata()));
      cfDef.setColumnType(columnFamily.getType());
      cfDef.setComment(columnFamily.getComment());

      if (columnFamily.getCompactionStrategy() != null) {
        cfDef.setCompactionStrategy(columnFamily.getCompactionStrategy());
      }

      if (columnFamily.getCompactionStrategyOptions() != null && !columnFamily.getCompactionStrategyOptions().isEmpty()) {
        final Map<String, String> compactionStrategyOptions = new HashMap<>();
        for (final CompactionStrategyOptionModel compactionStrategyOption : columnFamily.getCompactionStrategyOptions()) {
          compactionStrategyOptions.put(compactionStrategyOption.getName(), compactionStrategyOption.getValue());
        }
        cfDef.setCompactionStrategyOptions(compactionStrategyOptions);
      }

      if (columnFamily.getGcGraceSeconds() != null) {
        cfDef.setGcGraceSeconds(columnFamily.getGcGraceSeconds());
      }

      if (columnFamily.getMaxCompactionThreshold() != null) {
        cfDef.setMaxCompactionThreshold(columnFamily.getMaxCompactionThreshold());
      }

      if (columnFamily.getMinCompactionThreshold() != null) {
        cfDef.setMinCompactionThreshold(columnFamily.getMinCompactionThreshold());
      }

      if (columnFamily.getReadRepairChance() != null) {
        cfDef.setReadRepairChance(columnFamily.getReadRepairChance());
      }

      if (columnFamily.getReplicationOnWrite() != null) {
        cfDef.setReplicateOnWrite(columnFamily.getReplicationOnWrite());
      }

      cfDef.setKeyValidationClass(columnFamily.getKeyType().getTypeName() + columnFamily.getKeyTypeAlias());

      if (columnFamily.getDefaultColumnValueType() != null) {
        cfDef.setDefaultValidationClass(columnFamily.getDefaultColumnValueType().getClassName());
      }

      if (columnFamily.getType().equals(ColumnType.SUPER) && columnFamily.getSubComparatorType() != null) {
        cfDef.setSubComparatorType(columnFamily.getSubComparatorType());
      }

      if (ComparatorType.COMPOSITETYPE.equals(columnFamily.getComparatorType())
          || StringUtils.containsIgnoreCase(columnFamily.getComparatorTypeAlias(), ColumnFamilyModel.REVERSED_QUALIFIER)) {
        cfDef.setComparatorTypeAlias(columnFamily.getComparatorTypeAlias());
      }

      columnFamilyDefinitions.add(cfDef);
    }
    return columnFamilyDefinitions;
  }

  private List<ColumnDefinition> createColumnsDefinition(final List<ColumnMetadataModel> columnsMetadata) {
    final List<ColumnDefinition> columnsDefinition = new ArrayList<>();
    for (final ColumnMetadataModel columnMetadata : columnsMetadata) {
      final BasicColumnDefinition columnDefinition = new BasicColumnDefinition();

      final GenericType columnName = columnMetadata.getColumnName();
      columnDefinition.setName(GenericTypeSerializer.get().toByteBuffer(columnName));

      if (columnMetadata.getColumnIndexType() != null) {
        final String indexName = columnMetadata.getIndexName();
        columnDefinition.setIndexName(indexName == null ? columnName.getValue() : indexName);
        columnDefinition.setIndexType(columnMetadata.getColumnIndexType());
      }

      if (columnMetadata.getValidationClass() != null) {
        columnDefinition.setValidationClass(columnMetadata.getValidationClass().getClassName());
      }
      columnsDefinition.add(columnDefinition);

    }
    return columnsDefinition;
  }
}
