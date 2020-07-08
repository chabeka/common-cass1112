/**
 *
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.trace.model.DfceTraceDoc;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;

/**
 * Classe de support pour les journaux du cycle de vie des archives
 *
 */
@Component
public class CycleVieSupport {

  private final DFCEServices dfceServices;

  /**
   * @param support
   *           Service de manipulation des objets DFCE
   */
  @Autowired
  public CycleVieSupport(final DFCEServices dfceServices) {
    super();
    this.dfceServices = dfceServices;
  }

  /**
   * Ajout d'une trace dans l'historique du cycle de vie des archives
   *
   * @param trace
   *           la trace à ajouter
   */
  public final void create(final TraceToCreate trace) {

    final RMDocEvent event = ToolkitFactory.getInstance().createRMDocEvent();
    event.setEventDescription(trace.toString());
    event.setUsername(trace.getLogin());
    event.setDocVersion(StringUtils.EMPTY);
    event.setDocUUID(UUID.randomUUID()); // TODO: à remplacer par l'UUID du
    // document. Pour l'instant, on ne
    // dispose pas directement de
    // l'information dans l'objet
    // TraceToCreate (elle est dans la partie infos supplémentaires)
    dfceServices.createCustomDocumentEventLog(event);
  }

  /**
   * Recherche des traces existantes dans le cycle de vie des archives
   *
   * @param dateDebut
   *           date de début de recherche
   * @param dateFin
   *           date de fin de recherche
   * @param limite
   *           nombre maximal d'enregistrements à retourner
   * @param reversed
   *           indicateur de retour de liste dans l'ordre décroissant de date
   *           de création
   * @return la liste des traces du cycle de vie trouvées dans l'intervalle de
   *         dates données
   */
  @SuppressWarnings("unchecked")
  public final List<DfceTraceDoc> findByDates(final Date dateDebut, final Date dateFin,
                                              final int limite, final boolean reversed) {

    /*
     * final List<RMDocEvent> events = dfceServices.getDocumentEventLogsByDates(dateDebut, dateFin);
     * List<DfceTraceDoc> values = null;
     * if (CollectionUtils.isNotEmpty(events)) {
     * Iterator<RMDocEvent> iterator;
     * if (reversed) {
     * iterator = new ReverseListIterator(events);
     * } else {
     * iterator = events.iterator();
     * }
     * int countLeft = limite;
     * values = new ArrayList<>(limite);
     * while (countLeft > 0 && iterator.hasNext()) {
     * final RMDocEvent event = iterator.next();
     * final DfceTraceDoc trace = createDfceTraceDoc(event);
     * values.add(trace);
     * countLeft--;
     * }
     * }
     */

    return getDocEventLogsByDatesAdaptatif(dateDebut, dateFin, limite, reversed);
  }

  /**
   * @param event
   * @return
   */
  private DfceTraceDoc createDfceTraceDoc(final RMDocEvent event) {
    final DfceTraceDoc trace = new DfceTraceDoc();
    trace.setAttributs(event.getAttributes());
    trace.setDateEvt(event.getEventDate());
    trace.setDocUuid(event.getDocUUID());
    trace.setLogin(event.getUsername());
    if (event.getEventType() != null) {
      trace.setTypeEvt(event.getEventType().toString());
    }
    trace.setIdJournal(event.getArchiveUUID());
    return trace;
  }

  private List<DfceTraceDoc> getDocEventLogsByDatesAdaptatif(Date dateDebut, Date dateFin, final int limit, final boolean reversed) {
    // Initialisation
    List<DfceTraceDoc> values = null;
    ListIterator<RMDocEvent> iterator;
    int tailleTemp = 0;
    long deltaMinuteTemp = 1;
    final Calendar c1 = Calendar.getInstance();
    final Calendar c2 = Calendar.getInstance();
    final Calendar c = Calendar.getInstance();
    c.setTime(dateDebut);
    c.set(Calendar.SECOND, 00);
    dateDebut = c.getTime();
    c.setTime(dateFin);
    c.set(Calendar.SECOND, 59);
    dateFin = c.getTime();
    Date date1;
    Date date2;
    int increment = 2;
    int incrementPrecedent = 1;
    int etape = 0;
    // Création du premier intervalle d'une minute
    if (reversed) {
      c1.setTime(dateFin);
      c1.set(Calendar.SECOND, 0);
      c2.setTime(dateFin);
      c2.set(Calendar.SECOND, 59);

    } else {
      c1.setTime(dateDebut);
      c1.set(Calendar.SECOND, 0);
      c2.setTime(dateDebut);
      c2.set(Calendar.SECOND, 59);

    }
    date1 = c1.getTime();
    date2 = c2.getTime();
    // On calcule l'intervalle total en secondes
    final long deltaSeconde = 59 + (dateFin.getTime() - dateDebut.getTime()) / 1000;
    // Suivant le sens (reversed ou non) on démarre l'iterator du début ou de la fin
    if (reversed) {
      final int size = dfceServices.getDocumentEventLogsByDates(c1.getTime(), c2.getTime()).size();
      iterator = dfceServices.getDocumentEventLogsByDates(c1.getTime(), c2.getTime()).listIterator(size);
    } else {
      iterator = dfceServices.getDocumentEventLogsByDates(c1.getTime(), c2.getTime()).listIterator();
    }
    // Itération de recherche sur les intervalles construits dynamiquement
    // On sort de la boucle si on est arrivé à la fin de l'intervalle ou si on a atteint la limite demandée
    while (tailleTemp < limit && deltaMinuteTemp <= deltaSeconde) {
      // On ajoute la trace si on a un suivant ou un précédent (cas reversed)
      if (reversed && iterator.hasPrevious() || !reversed && iterator.hasNext()) {
        if (values == null) {
          values = new ArrayList<>();
        }
        if (reversed && iterator.hasPrevious()) {
          values.add(createDfceTraceDoc(iterator.previous()));
        }
        if (!reversed && iterator.hasNext()) {
          values.add(createDfceTraceDoc(iterator.next()));
        }
        tailleTemp += 1;
      } else {
        if (deltaMinuteTemp != deltaSeconde) {
          deltaMinuteTemp += increment * 60;
          date1 = c1.getTime();
          date2 = c2.getTime();
          if (reversed) {
            c1.add(Calendar.MINUTE, -increment);
            c2.add(Calendar.MINUTE, -incrementPrecedent);
          } else {
            c1.add(Calendar.MINUTE, incrementPrecedent);
            c2.add(Calendar.MINUTE, increment);
          }
          date1 = c1.getTime();
          if (date1.before(dateDebut)) {
            c1.setTime(dateDebut);
            deltaMinuteTemp = deltaSeconde;
          }
          date2 = c2.getTime();
          if (date2.after(dateFin)) {
            c2.setTime(dateFin);
            deltaMinuteTemp = deltaSeconde;
          }
          final int size = dfceServices.getDocumentEventLogsByDates(c1.getTime(), c2.getTime()).size();
          incrementPrecedent = increment;
          final int facteur = calculeFacteur(limit, size);
          increment = updateIncrement(limit, increment, size, facteur);
          etape += 1;
          if (reversed) {
            iterator = dfceServices.getDocumentEventLogsByDates(c1.getTime(), c2.getTime()).listIterator(size);
          } else {
            iterator = dfceServices.getDocumentEventLogsByDates(c1.getTime(), c2.getTime()).listIterator();
          }
        } else {
          deltaMinuteTemp += 60;
        }
      }

    }
    return values;
  }

  /**
   * @param limit
   * @param increment
   * @param size
   * @param facteur
   * @return
   */
  private int updateIncrement(final int limit, int increment, final int size, final int facteur) {
    if (size < limit) {
      increment = increment * facteur;
    } else {
      if (increment >= facteur) {
        increment = increment / facteur;
      }
    }
    return increment;
  }

  /**
   * @param limit
   * @param size
   * @return
   */
  private int calculeFacteur(final int limit, final int size) {
    int facteur = 2;
    if (size == 0 || limit / size > 500) {
      facteur = 10;
    } else {
      facteur = 2;
    }
    return facteur;
  }



  /**
   * Recherche de toutes les traces du cycle de vie des archives pour un
   * document avec l'uuid donnée
   *
   * @param docUuid
   *           Identifiant unique du document
   * @return La liste eds traces du cycle de vie des archives pour le document
   *         donné
   */
  public final List<DfceTraceDoc> findByDocUuid(final UUID docUuid) {

    final List<RMDocEvent> liste = dfceServices.getDocumentEventLogsByUUID(docUuid);
    final List<DfceTraceDoc> listeTraces = new ArrayList<>();
    if (liste != null) {
      for (final RMDocEvent rmDocEvent : liste) {
        final DfceTraceDoc trace = new DfceTraceDoc();
        trace.setLogin(rmDocEvent.getUsername());
        trace.setAttributs(rmDocEvent.getAttributes());
        trace.setDateEvt(rmDocEvent.getEventDate());
        trace.setDocUuid(rmDocEvent.getDocUUID());
        trace.setTypeEvt(rmDocEvent.getEventType().toString());
        trace.setIdJournal(rmDocEvent.getArchiveUUID());

        listeTraces.add(trace);
      }
      return listeTraces;
    }
    return null;

  }

}
