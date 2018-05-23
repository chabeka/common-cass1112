/**
 *
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.trace.dao.AbstractTraceDao;
import fr.urssaf.image.sae.trace.dao.AbstractTraceIndexDao;
import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueDao;
import fr.urssaf.image.sae.trace.dao.model.Trace;
import fr.urssaf.image.sae.trace.dao.model.TraceIndex;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.exceptions.HInvalidRequestException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 * Classe mère des classes de traitement des traces
 *
 * @param <T>
 *          Type de trace contenue dans le registre
 * @param <I>
 *          Index des traces
 */
public abstract class AbstractTraceSupport<T extends Trace, I extends TraceIndex> {

  private final SimpleDateFormat dateFormat = new SimpleDateFormat(
                                                                   "yyyy-MM-dd HH'h'mm ss's' SSS'ms'", Locale.FRENCH);

  /**
   * Création d'une trace dans le registre de sécurité.
   * les champs suivants sont renseignés :
   * <ul>
   * <li>code événement</li>
   * <li>timestamp</li>
   * <li>contrat de service</li>
   * <li>pagms</li>
   * <li>login</li>
   * <li>infos</li>
   * </ul>
   * Les informations complémentaires sont à saisir dans la méthode
   * {@link AbstractTraceSupport#completeCreateTrace(ColumnFamilyUpdater, Trace, long)}
   *
   * @param trace
   *          trace à créer
   * @param clock
   *          horloge de la création
   */
  public final void create(final T trace, final long clock) {

    // Trace applicative
    final String prefix = "create()";
    getLogger().debug("{} - Début", prefix);

    // création de la trace
    final ColumnFamilyTemplate<UUID, String> tmpl = getDao().getCfTmpl();
    final ColumnFamilyUpdater<UUID, String> updater = tmpl.createUpdater(trace
                                                                              .getIdentifiant());

    getDao().writeColumnCodeEvt(updater, trace.getCodeEvt(), clock);
    getDao().writeColumnTimestamp(updater, trace.getTimestamp(), clock);

    if (StringUtils.isNotBlank(trace.getContratService())) {
      getDao().writeColumnContratService(updater,
                                         trace.getContratService(),
                                         clock);
    }

    if (CollectionUtils.isNotEmpty(trace.getPagms())) {
      getDao().writeColumnPagms(updater, trace.getPagms(), clock);
    }

    if (StringUtils.isNotBlank(trace.getLogin())) {
      getDao().writeColumnLogin(updater, trace.getLogin(), clock);
    }

    // ajout des informations spécifiques à cette trace
    completeCreateTrace(updater, trace, clock);

    tmpl.update(updater);

    // création de l'index
    final I index = getIndexFromTrace(trace);
    final String journee = DateRegUtils.getJournee(index.getTimestamp());
    final ColumnFamilyUpdater<String, UUID> indexUpdater = getIndexDao()
                                                                        .createUpdater(journee);
    getIndexDao().writeColumn(indexUpdater,
                              index.getIdentifiant(),
                              index,
                              clock);
    getIndexDao().update(indexUpdater);

    // Trace applicative
    getLogger().debug(
                      "{} - Trace ajoutée dans le {} : Id={}. Timestamp={}",
                      new Object[] {prefix, getRegistreName(), trace.getIdentifiant(),
                                    dateFormat.format(trace.getTimestamp())});
    getLogger().debug("{} - Fin", prefix);

  }

  /**
   * Suppression de toutes les traces et index
   *
   * @param date
   *          date à laquelle supprimer les traces
   * @param clock
   *          horloge de la suppression
   * @return le nombre de traces purgées
   */
  public final long delete(final Date date, final long clock) {

    long nbTracesPurgees = 0;

    SliceQuery<String, UUID, I> sliceQuery;
    sliceQuery = getIndexDao().createSliceQuery();
    final String journee = DateRegUtils.getJournee(date);
    sliceQuery.setKey(journee);

    final Iterator<I> iterator = getIterator(sliceQuery);

    if (iterator.hasNext()) {

      // Suppression des traces de la CF TraceJournalEvt
      nbTracesPurgees = deleteRecords(iterator, clock);

      // suppression de l'index
      final Mutator<String> indexMutator = getIndexDao().createMutator();
      getIndexDao().mutatorSuppressionLigne(indexMutator, journee, clock);
      indexMutator.execute();

    }

    return nbTracesPurgees;

  }

  /**
   * Recherche et retourne la trace avec l'id donné
   *
   * @param identifiant
   *          identifiant de la trace
   * @return la trace de sécurité
   */
  public final T find(final UUID identifiant) {
    final ColumnFamilyTemplate<UUID, String> tmpl = getDao().getCfTmpl();
    final ColumnFamilyResult<UUID, String> result = tmpl.queryColumns(identifiant);

    return getTraceFromResult(result);
  }

  /**
   * Recherche et retourne la liste des traces à une date donnée
   *
   * @param date
   *          date à laquelle rechercher les traces
   * @return la liste des traces techniques
   */
  public final List<I> findByDate(final Date date) {
    final SliceQuery<String, UUID, I> sliceQuery = getIndexDao().createSliceQuery();
    sliceQuery.setKey(DateRegUtils.getJournee(date));

    List<I> list = null;
    final Iterator<I> iterator = getIterator(sliceQuery);

    if (iterator.hasNext()) {
      list = new ArrayList<I>();
    }

    while (iterator.hasNext()) {
      list.add(iterator.next());
    }

    return list;
  }

  /**
   * Retourne l'ensemble des destinataires des traces
   *
   * @return l'ensemble des destinataires des traces
   */
  public final List<T> findAll() {

    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
                                                                            .createRangeSlicesQuery(getDao().getKeyspace(),
                                                                                                    getDao().getRowKeySerializer(),
                                                                                                    getDao().getColumnKeySerializer(),
                                                                                                    bytesSerializer);
    rangeSlicesQuery.setColumnFamily(getDao().getColumnFamilyName());
    rangeSlicesQuery.setRange(
                              StringUtils.EMPTY,
                              StringUtils.EMPTY,
                              false,
                              AbstractDao.DEFAULT_MAX_COLS);
    rangeSlicesQuery.setRowCount(AbstractDao.DEFAULT_MAX_ROWS);
    QueryResult<OrderedRows<UUID, String, byte[]>> queryResult;
    queryResult = rangeSlicesQuery.execute();

    // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
    // son utilisation
    final QueryResultConverter<UUID, String, byte[]> converter = new QueryResultConverter<UUID, String, byte[]>();
    final ColumnFamilyResultWrapper<UUID, String> result = converter
                                                                    .getColumnFamilyResultWrapper(queryResult,
                                                                                                  getDao().getRowKeySerializer(),
                                                                                                  getDao().getColumnKeySerializer(),
                                                                                                  bytesSerializer);

    // On itère sur le résultat
    final HectorIterator<UUID, String> resultIterator = new HectorIterator<UUID, String>(
                                                                                         result);
    final List<T> list = new ArrayList<T>();
    for (final ColumnFamilyResult<UUID, String> row : resultIterator) {

      list.add(getTraceFromResult(row));

    }
    return list;

  }

  /**
   * recherche et retourne la liste des traces pour un intervalle de dates
   * données
   *
   * @param startDate
   *          date de début de recherche
   * @param endDate
   *          date de fin de recherche
   * @param maxCount
   *          nombre maximal d'enregistrements à retourner
   * @param reversed
   *          booleen indiquant si l'ordre décroissant doit etre appliqué<br>
   *          <ul>
   *          <li>true : ordre décroissant</li>
   *          <li>false : ordre croissant</li>
   *          </ul>
   * @return la liste des traces d'exploitation
   */
  public final List<I> findByDates(final Date startDate, final Date endDate, final int maxCount,
                                   final boolean reversed) {

    // Trace applicative
    final String prefix = "findByDates()";
    getLogger().debug("{} - Début", prefix);
    getLogger().debug("{} - Date de début : {}",
                      prefix,
                      dateFormat.format(startDate));
    getLogger().debug("{} - Date de fin : {}",
                      prefix,
                      dateFormat.format(endDate));
    getLogger().debug("{} - Ordre décroissant : {}", prefix, reversed);

    List<I> list = null;

    final SliceQuery<String, UUID, I> sliceQuery = getIndexDao().createSliceQuery();
    sliceQuery.setKey(DateRegUtils.getJournee(startDate));

    final UUID startUuid = getTimeUuidSupport().buildUUIDFromDate(startDate);
    final UUID endUuid = getTimeUuidSupport().buildUUIDFromDateBorneSup(endDate);

    final Iterator<I> iterator = getIterator(sliceQuery,
                                             startUuid,
                                             endUuid,
                                             reversed);

    try {
      if (iterator.hasNext()) {
        list = new ArrayList<I>();
      }
    }
    catch (final HInvalidRequestException ex) {
      getLogger()
                 .warn(
                       "{} - Echec de la requête Cassandra. Date de début : {}. UUID début : {}. Date de fin : {}. UUID fin : {}.",
                       new Object[] {prefix, dateFormat.format(startDate),
                                     startUuid, dateFormat.format(endDate), endUuid});
      throw ex;
    }

    int count = 0;
    while (iterator.hasNext() && count < maxCount) {
      list.add(iterator.next());
      count++;
    }

    return list;
  }

  /**
   * Suppression des traces dans les registres ou journaux
   *
   * @param iterator
   *          Iterateur contenant les traces
   * @param clock
   *          l'horloge de la suppression
   * @return le nombre d'enregistrements supprimés
   */
  private long deleteRecords(final Iterator<I> iterator, final long clock) {
    long result = 0;

    // suppression de toutes les traces
    final Mutator<UUID> mutator = getDao().createMutator();
    while (iterator.hasNext()) {
      getDao().mutatorSuppressionLigne(mutator,
                                       iterator.next().getIdentifiant(),
                                       clock);
      result++;
    }
    mutator.execute();

    return result;

  }

  private T getTraceFromResult(final ColumnFamilyResult<UUID, String> result) {

    T trace = null;

    if (result != null && result.hasResults()) {

      final UUID idTrace = result.getKey();
      final Date timestamp = result.getDate(TraceRegTechniqueDao.COL_TIMESTAMP);

      trace = createNewInstance(idTrace, timestamp);

      trace.setCodeEvt(result.getString(TraceRegTechniqueDao.COL_CODE_EVT));
      trace.setContratService(result
                                    .getString(TraceRegTechniqueDao.COL_CONTRAT_SERVICE));
      trace.setLogin(result.getString(TraceRegTechniqueDao.COL_LOGIN));

      final byte[] bValue = result.getByteArray(TraceRegTechniqueDao.COL_PAGMS);
      if (bValue != null) {
        trace.setPagms(ListSerializer.get().fromBytes(bValue));
      }

      completeTraceFromResult(trace, result);

    }

    return trace;
  }

  /**
   * Complétion de l'objet créé à partir du résultat de la recherche
   *
   * @param trace
   *          la trace à compléter
   * @param result
   *          le résultat
   */
  protected abstract void completeTraceFromResult(T trace,
                                                  ColumnFamilyResult<UUID, String> result);

  /**
   * @param sliceQuery
   *          la requête concernée
   * @return l'iterateur
   */
  protected abstract Iterator<I> getIterator(
                                             SliceQuery<String, UUID, I> sliceQuery);

  /**
   * @param sliceQuery
   *          la requête concernée
   * @param startUuid
   *          identifiant unique de départ
   * @param endUuid
   *          identifiant unique de fin
   * @param reversed
   *          booleen indiquant si l'ordre décroissant doit etre appliqué<br>
   *          <ul>
   *          <li>true : ordre décroissant</li>
   *          <li>false : ordre croissant</li>
   *          </ul>
   * @return l'iterateur
   */
  protected abstract Iterator<I> getIterator(
                                             SliceQuery<String, UUID, I> sliceQuery, UUID startUuid, UUID endUuid,
                                             boolean reversed);

  /**
   * @return le dao utilisé pour les traces
   */
  protected abstract AbstractTraceDao getDao();

  /**
   * @return le dao utilisé pour les index des traces
   */
  protected abstract AbstractTraceIndexDao<I> getIndexDao();

  /**
   * ajout des colonnes spécifiques
   *
   * @param updater
   *          updater de la trace
   * @param trace
   *          trace à insérer
   * @param clock
   *          time de l'insertion
   */
  protected abstract void completeCreateTrace(
                                              ColumnFamilyUpdater<UUID, String> updater, T trace, long clock);

  /**
   * retourne l'objet Index créé à partir de la trace
   *
   * @param trace
   *          la trace
   * @return l'index associé à la trace
   */
  protected abstract I getIndexFromTrace(T trace);

  /**
   * Retourne le nom du registre pour des fins de logs
   *
   * @return le nom du registre
   */
  protected abstract String getRegistreName();

  /**
   * @param timestamp
   *          le timestamp
   * @param idTrace
   *          l'identifiant de la trace
   * @return une nouvelle instance de la classe
   */
  protected abstract T createNewInstance(UUID idTrace, Date timestamp);

  /**
   * @return le support de time uuid
   */
  protected abstract TimeUUIDEtTimestampSupport getTimeUuidSupport();

  /**
   * @return le logger concerné
   */
  protected abstract Logger getLogger();
}
