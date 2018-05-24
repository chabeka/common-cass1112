/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao.supportcql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.model.Trace;
import fr.urssaf.image.sae.trace.dao.model.TraceIndexCql;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import me.prettyprint.hector.api.exceptions.HInvalidRequestException;

/**
 * TODO (AC75095028) Description du type
 */
public abstract class GenericAbstractTraceCqlSupport<T extends Trace, I extends TraceIndexCql> {

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
   *
   * @param trace
   *          trace à créer
   * @param clock
   *          horloge de la création
   */
  public void create(final T trace, final long clock) {

    getDao().saveWithMapper(trace);

    // création de l'index
    final I index = getIndexFromTrace(trace);

    final DateFormat dateFormat = new SimpleDateFormat(getDateFormat());

    // date en string sous la forme de YYYYMMJJ sans les heures et les secondes
    final String journee = DateRegUtils.getJournee(index.getTimestamp());
    index.setIdentifiant(journee);
    getIndexDao().saveWithMapper(index);

    /*
     * try {
     * final Date date = dateFormat.parse(journee);
     * final Date date1 = DateRegUtils.getDateWithoutTime();
     * index.setIdentifiant(journee);
     * getIndexDao().saveWithMapper(index);
     * }
     * catch (final ParseException e) {
     * e.printStackTrace();
     * }
     */

    // Trace applicative

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
  public long delete(final Date date, final long clock) {
    long nbTracesPurgees = 0;

    final Iterator<I> iterator = getIterator(date);

    if (iterator.hasNext()) {

      // Suppression des traces de la CF TraceJournalEvt
      nbTracesPurgees = deleteRecords(iterator, clock);

      // suppression de l'index
      final Iterator<I> indexToDelete = getIterator(date);
      while (indexToDelete.hasNext()) {
        getIndexDao().delete(indexToDelete.next());
      }
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
  public Optional<T> find(final UUID identifiant) {
    return getDao().findWithMapperById(identifiant);
  }

  // TODO A Supprimer
  public Iterator<T> findAll() {
    return getDao().findAllWithMapper();
  }

  /**
   * Recherche et retourne la liste des tracesIndex à une date donnée
   *
   * @param date
   *          date à laquelle rechercher les traces
   * @return la liste des traces techniques
   */
  public List<I> findByDate(final Date date) {

    List<I> list = null;
    final Iterator<I> iterator = getIterator(date);

    if (iterator.hasNext()) {
      list = new ArrayList<I>();
    }

    while (iterator.hasNext()) {
      list.add(iterator.next());
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
  public List<I> findByDates(final Date startDate, final Date endDate, final int maxCount,
                             final boolean reversed) {

    final Iterator<I> iterator = getIterator(startDate, endDate, reversed);

    List<I> list = null;

    try {
      if (iterator.hasNext()) {
        list = new ArrayList<I>();
      }
    }
    catch (final HInvalidRequestException ex) {
      getLogger()
                 .warn(
                       " - Echec de la requête Cassandra. Date de début : {}. UUID début : {}. Date de fin : {}. UUID fin : {}.",
                       new Object[] {dateFormat.format(startDate), dateFormat.format(endDate),});
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
  long deleteRecords(final Iterator<I> iterator, final long clock) {
    long result = 0;
    while (iterator.hasNext()) {
      getDao().deleteById(iterator.next().getTraceId());
      result++;
    }
    return result;
  }

  /**
   * @param sliceQuery
   *          la requête concernée
   * @return l'iterateur
   */
  abstract Iterator<I> getIterator(Date id);

  /**
   * @param dateStar
   *          identifiant unique de départ
   * @param dateEnd
   *          identifiant unique de fin
   * @param reversed
   *          booleen indiquant si l'ordre décroissant doit etre appliqué<br>
   *          <ul>
   *          <li>true : ordre décroissant</li>
   *          <li>false : ordre croissant</li>
   *          </ul>
   * @return l'iterateur
   */
  abstract Iterator<I> getIterator(Date dateStar, Date dateEnd, boolean reversed);

  /**
   * @return le dao utilisé pour les traces
   */
  abstract IGenericDAO getDao();

  /**
   * @return le dao utilisé pour les index des traces
   */
  abstract IGenericDAO getIndexDao();

  /**
   * retourne l'objet Index créé à partir de la trace
   *
   * @param trace
   *          la trace
   * @return l'index associé à la trace
   */
  abstract I getIndexFromTrace(T trace);

  /**
   * Retourne le nom du registre pour des fins de logs
   *
   * @return le nom du registre
   */
  abstract String getRegistreName();

  /**
   * @param timestamp
   *          le timestamp
   * @param idTrace
   *          l'identifiant de la trace
   * @return une nouvelle instance de la classe
   */
  abstract T createNewInstance(UUID idTrace, Date timestamp);

  /**
   * @return le support de time uuid
   */
  abstract TimeUUIDEtTimestampSupport getTimeUuidSupport();

  /**
   * @return le logger concerné
   */
  abstract Logger getLogger();

  /**
   * @return
   */
  abstract String getDateFormat();

}
