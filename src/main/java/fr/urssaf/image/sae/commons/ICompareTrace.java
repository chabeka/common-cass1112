package fr.urssaf.image.sae.commons;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.trace.commons.TraceFieldsName;
import fr.urssaf.image.sae.trace.dao.model.Trace;
import fr.urssaf.image.sae.trace.dao.model.TraceIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.utils.RowUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;


public interface ICompareTrace<T extends Trace, TC extends Trace, I extends TraceIndex, IC extends TraceIndex> {

  /**
   * Créér une nouvelle instance de la {@link Trace}
   * 
   * @param idTrace
   * @param timestamp
   * @return
   */
  T createNewInstance(UUID idTrace, Date timestamp);

  /**
   * Ajoute les champs particulier à la trace {@link Trace} lors de l'extraction depuis
   * le resultat d'une requete venant de la base de données
   * 
   * @param trace
   * @param row
   *          la ligne issue de la requete
   */
  void completeTraceFromResult(T trace, final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> row);

  /**
   * Ajoute les principaux champs à la trace {@link Trace} lors de l'extraction de la {@link Trace} depuis
   * le resultat d'une requete venant de la base de données
   * 
   * @param trThrift
   *          le type de {@link Trace}
   * @return la trace de type cql
   */
  TC createTraceFromObjectThrift(T trThrift);

  /**
   * Extraction de la trace de type cql a partir d'une trace de type {@link TraceIndex}
   * 
   * @param index
   *          l'indexe de type thrift
   * @param key
   *          la clé
   * @return la trace cql
   */
  IC createIndexFromObjectThrift(I index, String key);

  /**
   * Créér une nouvelle instance de la trace de type {@link TraceIndex}
   * 
   * @return
   */
  I createNewInstanceIndex();

  /**
   * Le keyspace du cluster thrift
   * 
   * @return
   */
  Keyspace getKeySpace();

  JacksonSerializer<I> getIndexSerializer();
  Logger getLogger();

  /**
   * le dao en fonction du type de la {@link Trace}
   * 
   * @return
   */
  IGenericDAO<TC, UUID> getTraceDaoType();

  /**
   * le dao en fonction du type de la {@link TraceIndex}
   * 
   * @return
   */
  IGenericDAO<IC, String> getIndexDaoType();

  /**
   * renvoie le nom de la table associé a une table de type {@link Trace}
   * 
   * @return
   */
  String getTraceClasseName();

  /**
   * renvoie le nom de la table associé a une table de type {@link TraceIndex}
   * 
   * @return
   */
  String getIndexClasseName();


  /**
   * Comparer les Traces cql et Thrift de type {@link Trace}
   * 
   * @throws Exception
   */
  public default boolean traceComparator() throws Exception {
    // recuperer un iterateur sur la table cql
    // Parcourir les elements de l'iterateur et pour chaque element 
    // recuperer un ensemble de X elements dans la table thrift
    // chercher l'element cql dans les X elements
    // si trouvé, on passe à l'element suivant cql
    // sinon on recupère les X element suivant dans la table thrift puis on fait une nouvelle recherche
    // si on en recupère  moins de X et qu'on ne trouve pas l'element cql alors == > echec de comparaison
    final Map<String, Map<UUID, TC>> mapRow = new HashMap<>();

    final Iterator<TC> itJournal = getTraceDaoType().findAllWithMapper();
    boolean isEqBase = true;
    boolean isInMap = false;

    while (itJournal.hasNext()) {
      final TC tr = itJournal.next();

      // Pour chaque trace cql On cherche dans la map
      // La map sera construite au fur et à mesure
      if(mapRow != null && !mapRow.isEmpty()) {
        isInMap = false;
        for (final Map.Entry<String, Map<UUID, TC>> entry: mapRow.entrySet()) {
          final String key = entry.getKey();			   
          final Map<UUID, TC> ssRow = entry.getValue();
          final UUID id = tr.getIdentifiant();
          final TC trThToCql = ssRow.get(id);
          if(tr.equals(trThToCql)) {
            isInMap = true;
            ssRow.remove(id);
            if(ssRow.isEmpty()) {
              mapRow.remove(key);
            }
            break;
          }  

          if(isInMap) {
            break;
          }
        }
      }

      // on cherche l'objet courant dans la base thrift si elle n'est pas trouvé dans la map servant de cache
      if(!isInMap) {
        boolean isObj = false;
        isObj = checkTraceCql(tr, getTraceClasseName(), mapRow);

        if(!isObj) {
          getLogger().info(" Objet cql n'a pas été trouvé dans la base thrift");
        } else {
          isEqBase = isEqBase && isObj;
        }  
      }
    } 
    if(isEqBase) {
      getLogger().info(" MigrationTraceDestinataire - OK");
    }  else {
      getLogger().info(" MigrationTraceDestinataire - KO - quelques elements sont différents");
    }
    return isEqBase;
  }

  /**
   * Comparer les Traces cql et Thrift de type {@link TraceIndex}
   * 
   * @throws Exception
   */
  public default boolean indexComparator() throws Exception {
    // recuperer un iterateur sur la table cql
    // Parcourir les elements de l'iterateur et pour chaque element 
    // recuperer un ensemble de X elements dans la table thrift
    // chercher l'element cql dans les X elements
    // si trouvé, on passe à l'element suivant cql
    // sinon on recupère les X element suivant dans la table thrift puis on fait une nouvelle recherche
    // si on en recupère  moins de X et qu'on ne trouve pas l'element cql alors == > echec de comparaison
    final Map<String, Map<UUID, IC>> mapRow = new HashMap<>();

    final Iterator<IC> itJournal = getIndexDaoType().findAllWithMapper();
    boolean isEqBase = true;
    boolean isInMap = false;
    while (itJournal.hasNext()) {
      final IC tr = itJournal.next();

      // Pour chaque trace cql On cherche dans la map
      // La map sera construite au fur et à mesure
      if(mapRow != null && !mapRow.isEmpty()) {
        isInMap = false;
        for (final Map.Entry<String, Map<UUID, IC>> entry: mapRow.entrySet()) {
          final String key = entry.getKey();				   
          final Map<UUID, IC> ssRow = entry.getValue();
          final UUID id = tr.getIdentifiant();
          final IC trThToCql = ssRow.get(id);
          if(tr.equals(trThToCql)) {
            isInMap = true;
            ssRow.remove(id);
            if(ssRow.isEmpty()) {
              mapRow.remove(key);
            }
            break;
          } 
        }
      }
      // on cherche l'objet courant dans la base thrift si elle n'est pas trouvé dans la map servant de cache
      if(!isInMap) {
        boolean isObj = false;
        isObj = checkIndexCql(tr, getIndexClasseName(), mapRow);

        if(!isObj) {
          getLogger().info("l' objet d'identifiant " + tr.getIdentifiant() + " n'a pas été trouvé dans la table cql" );
        } else {
          isEqBase = isEqBase && isObj;
        }  
      }

    } 
    if(isEqBase) {
      getLogger().info(" MigrationTraceDestinataire - OK");
    }  else {
      getLogger().info(" MigrationTraceDestinataire - KO - quelques elements sont différents");
    }
    return isEqBase;
  }

  /**
   * Chercher un objet {@link TC} dans la table thrift avec 
   * Requetage hector par tranche sur la table {@link T}
   * @param trCql
   * @return si l'objet {@link TC} a été trouvé dans la base thrift
   * @throws Exception
   */
  public default boolean  checkTraceCql(final TC trCql, final String tableName, 
                                        final Map<String, Map<UUID, TC>> mapRow) throws Exception {


    final StringSerializer stringSerializer = StringSerializer.get();
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final UUIDSerializer uSl = UUIDSerializer.get();

    final RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(getKeySpace(),
                                uSl,
                                stringSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(tableName);
    final int blockSize = RowUtils.BLOCK_SIZE_DEFAULT;
    UUID startKey = null;
    int count;
    boolean isEqObj = false;
    Integer ordre = 0;

    // Pour chaque tranche de blockSize, on recherche l'objet cql
    do {
      rangeSlicesQuery.setRange("", "", false, blockSize);
      rangeSlicesQuery.setKeys(startKey, null);
      rangeSlicesQuery.setRowCount(blockSize);
      final QueryResult<OrderedRows<UUID, String, byte[]>> result = rangeSlicesQuery
          .execute();

      final OrderedRows<UUID, String, byte[]> orderedRows = result.get();
      count = orderedRows.getCount();

      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> lastRow = orderedRows.peekLast();
      startKey = lastRow.getKey();

      final Map<UUID, TC> ssMap = new HashMap<>();
      for (final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> row : orderedRows) {

        // on recupère la trace thrifh
        final T trThrift = getTraceFromResult(row);
        // On le transforme en cql
        final TC trThToCql = createTraceFromObjectThrift(trThrift);
        // on compare les deux object cql
        if(trThToCql.equals(trCql)) {
          isEqObj = true;
        } else {
          ssMap.put(trThToCql.getIdentifiant(), trThToCql);
        }

      }
      if (!mapRow.containsKey(ordre.toString())) {
        mapRow.put(ordre.toString(), ssMap);
      }
      ordre++;
      // on sort de la boucle si l'elemet est trouvé dans l'itération precedante
      if (isEqObj) {
        break;
      }

    } while (count == blockSize);

    if(!isEqObj) {
      getLogger().info("l' objet d'identifiant " + trCql.getIdentifiant() + " n'a pas été trouvé dans la table cql" );
    }

    return isEqObj;

  }	

  /**
   * Chercher un objet {@link IC} dans la table thrift avec 
   * Requetage hector par tranche sur la table {@link I}
   * @param trCql
   * @return si l'objet {@link IC} a été trouvé dans la base thrift
   * @throws Exception
   */

  public default boolean checkIndexCql(final IC indexCql, final String tableName, final Map<String, Map<UUID, IC>> map) throws Exception {


    final StringSerializer stringSerializer = StringSerializer.get();
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final UUIDSerializer uSl = UUIDSerializer.get();

    final RangeSlicesQuery<String, UUID, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(getKeySpace(),
                                stringSerializer,
                                uSl,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(tableName);
    final int blockSize = RowUtils.BLOCK_SIZE_DEFAULT;
    String startKey = StringUtils.EMPTY;
    int count = 0;
    boolean isEqObj = false;
    Map<UUID, IC> mapRow = null;
    Integer mapKey = 0;

    // Pour chaque tranche de blockSize, on recherche l'objet cql
    do {
      rangeSlicesQuery.setRange(null, null, false, 10000);
      rangeSlicesQuery.setKeys(startKey, null);
      rangeSlicesQuery.setRowCount(blockSize);
      QueryResult<OrderedRows<String, UUID, byte[]>> result = null;
      try {
        result = rangeSlicesQuery.execute();
      } catch (final Exception e) {
        throw new RuntimeException(e.getMessage(), e);
      }



      final OrderedRows<String, UUID, byte[]> orderedRows = result.get();

      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<String, UUID, byte[]> lastRow = orderedRows.peekLast();
      startKey = lastRow.getKey();

      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
      // son utilisation
      final QueryResultConverter<String, UUID, byte[]> converter = new QueryResultConverter<>();
      final ColumnFamilyResultWrapper<String, UUID> result0 = converter
          .getColumnFamilyResultWrapper(result, stringSerializer, uSl, bytesSerializer);

      // map cache
      mapRow = new HashMap<>();

      // On itère sur le résultat
      final HectorIterator<String, UUID> resultIterator = new HectorIterator<>(result0);  
      for (final ColumnFamilyResult<String, UUID> row : resultIterator) {

        final String key = row.getKey();
        final Iterator<UUID> it = row.getColumnNames().iterator();
        while (it.hasNext()) {
          count++;
          final UUID uuid = it.next();
          final HColumn<UUID, ByteBuffer> tHl = row.getColumn(uuid);
          final I indexThrift = getIndexSerializer().fromByteBuffer(tHl.getValue()); 
          final IC indexThToCql = createIndexFromObjectThrift(indexThrift, key);
          if(indexThToCql.equals(indexCql)) {
            isEqObj = true;
          } else { 
            // construire une map qui nous servira de cache
            mapRow.put(indexThToCql.getIdentifiant(), indexThToCql);	 
          }
        } 
      }

      // on ajoute que si la plage n'a pas encore été ajouté
      if (!map.containsKey(mapKey.toString())) {
        map.put(mapKey.toString(), mapRow);
      }
      mapKey++;
      // on sort de la boucle si l'element est trouvé dans l'itération precedante
      if (isEqObj) {
        break;
      }

    } while (count == blockSize);

    if(!isEqObj) {
      getLogger().info("l' objet d'identifiant " +indexCql.getIdentifiant() + " n'a pas été trouvé dans la table cql" );
    }

    return isEqObj;

  }

  /**
   * Extraction de la {@link TraceJournalEvtCql} depuis le resultat de la requete hector
   * @param row
   * @return
   */
  public default T getTraceFromResult(final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> row) {

    T trace = null;
    Date timestamp = null;
    String login = null;
    String codeEvt = null;
    String contrat = null;
    List<String> pagms = null;

    if (row != null) {

      final UUID key = row.getKey();
      final HColumn<String, byte[]> tHl = row.getColumnSlice().getColumnByName(TraceFieldsName.COL_TIMESTAMP.getName());
      if(tHl != null) {
        timestamp = DateSerializer.get().fromBytes(tHl.getValue());	  	
      }

      trace = createNewInstance(key, timestamp);

      // codeEvt
      final HColumn<String, byte[]> hl = row.getColumnSlice().getColumnByName(TraceFieldsName.COL_CODE_EVT.getName());
      if(hl != null) {
        codeEvt = StringSerializer.get().fromBytes(hl.getValue());
      }

      /** code du contrat de service */
      final HColumn<String, byte[]> csHl = row.getColumnSlice().getColumnByName(TraceFieldsName.COL_CONTRAT_SERVICE.getName());
      if(csHl != null) {
        contrat = StringSerializer.get().fromBytes(csHl.getValue());
      }

      /** identifiant utilisateur */
      final HColumn<String, byte[]> loHl = row.getColumnSlice().getColumnByName(TraceFieldsName.COL_LOGIN.getName());
      if(loHl != null) {
        login = StringSerializer.get().fromBytes(loHl.getValue());
      }

      /** Le ou les PAGMS */
      // COL_PAGMS("pagms"),
      final HColumn<String, byte[]> pHl = row.getColumnSlice().getColumnByName(TraceFieldsName.COL_PAGMS.getName());
      if(pHl != null) {
        pagms = ListSerializer.get().fromBytes(pHl.getValue());
      }

      completeTraceFromResult(trace, row);

    }
    trace.setPagms(pagms);
    trace.setLogin(login);
    trace.setContratService(contrat);
    trace.setCodeEvt(codeEvt);

    return trace;
  }

}
