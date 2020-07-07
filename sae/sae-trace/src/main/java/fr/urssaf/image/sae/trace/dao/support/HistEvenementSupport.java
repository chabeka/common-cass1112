/**
 *
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.trace.model.DfceTraceSyst;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;

/**
 * Classe de support pour les historiques des événements
 */
@Component
public class HistEvenementSupport {

  private final DFCEServices dfceServices;

  /**
   * @param support
   *          Service de manipulation des objets DFCE
   */
  @Autowired
  public HistEvenementSupport(final DFCEServices dfceServices) {
    super();
    this.dfceServices = dfceServices;
  }

  /**
   * Ajout d'une trace dans l'historique des événements
   *
   * @param trace
   *          la trace à ajouter
   */
  public final void create(final TraceToCreate trace) {

    final RMSystemEvent event = ToolkitFactory.getInstance().createRMSystemEvent();
    event.setEventDescription(trace.toString());
    event.setUsername(trace.getLogin());
    dfceServices.createCustomSystemEventLog(event);
  }

  /**
   * Recherche des traces existantes dans l'historique des événements
   *
   * @param dateDebut
   *          date de début de recherche
   * @param dateFin
   *          date de fin de recherche
   * @param limite
   *          nombre maximal d'enregistrements à retourner
   * @param reversed
   *          indicateur de retour de liste dans l'ordre décroissant de date
   *          de création
   * @return la liste des traces
   */
  @SuppressWarnings("unchecked")
  public final List<DfceTraceSyst> findByDates(final Date dateDebut, final Date dateFin,
                                               final int limite, final boolean reversed) {

    // final List<RMSystemEvent> events = getSystemEventLogsByDatesAdaptatif(dateDebut, dateFin, limite, reversed);

    // List<DfceTraceSyst> values = null;

    /*
     * if (CollectionUtils.isNotEmpty(events)) {
     * final Iterator<RMSystemEvent> iterator;
     * iterator = events.iterator();
     * if (reversed) {
     * iterator = new ReverseListIterator(events);
     * } else {
     * iterator = events.iterator();
     * }
     * int countLeft = limite;
     * values = new ArrayList<>(limite);
     * while (countLeft > 0 && iterator.hasNext()) {
     * final RMSystemEvent event = iterator.next();
     * final DfceTraceSyst trace = createDfceTraceSyst(event);
     * values.add(trace);
     * countLeft--;
     * }
     * }
     */

    return getSystemEventLogsByDatesAdaptatif(dateDebut, dateFin, limite, reversed);
  }

  /**
   * @param event
   * @return
   */
  private DfceTraceSyst createDfceTraceSyst(final RMSystemEvent event) {
    final DfceTraceSyst trace = new DfceTraceSyst();

    trace.setAttributs(event.getAttributes());
    trace.setDateEvt(event.getEventDate());
    trace.setDocUuid(event.getArchiveUUID());
    trace.setLogin(event.getUsername());
    trace.setTypeEvt(event.getEventDescription());
    return trace;
  }

  private List<DfceTraceSyst> getSystemEventLogsByDatesAdaptatif(Date dateDebut, Date dateFin, final int limit, final boolean reversed) {
    // Recherche adaptative

    final List<DfceTraceSyst> values = new ArrayList<>();
    ListIterator<RMSystemEvent> iterator;
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
    // Gestion des secondes

    if (reversed) {
      c1.setTime(dateFin);
      c1.set(Calendar.SECOND, 0);
      c2.setTime(dateFin);
      c2.set(Calendar.SECOND, 59);
      // c2.set(Calendar.MILLISECOND, 999);
    } else {
      c1.setTime(dateDebut);
      c1.set(Calendar.SECOND, 0);
      c2.setTime(dateDebut);
      c2.set(Calendar.SECOND, 59);
      // c2.set(Calendar.MILLISECOND, 999);
    }
    date1 = c1.getTime();
    date2 = c2.getTime();
    final long deltaMinute = 59 + (dateFin.getTime() - dateDebut.getTime()) / 1000;
    if (reversed) {
      final int size = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).size();
      iterator = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).listIterator(size);
    } else {
      iterator = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).listIterator();
    }


    while (tailleTemp < limit && deltaMinuteTemp <= deltaMinute) {
      if (reversed && iterator.hasPrevious() || !reversed && iterator.hasNext()) {
        if (reversed && iterator.hasPrevious()) {
          values.add(createDfceTraceSyst(iterator.previous()));
        }
        if (!reversed && iterator.hasNext()) {
          values.add(createDfceTraceSyst(iterator.next()));
        }
        tailleTemp += 1;
      } else {
        if (deltaMinuteTemp != deltaMinute) {
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
            deltaMinuteTemp = deltaMinute;
          }
          date2 = c2.getTime();
          if (date2.after(dateFin)) {
            c2.setTime(dateFin);
            deltaMinuteTemp = deltaMinute;
          }
          final int size = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).size();
          incrementPrecedent = increment;
          int facteur = 2;
          if (size == 0 || limit / size > 500) {
            facteur = 10;
          } else {
            facteur = 2;
          }

          if (size < limit) {
            increment = increment * facteur;
          } else {
            if (increment >= facteur) {
              increment = increment / facteur;
            }
          }
          etape += 1;
          System.out.println("ETAPE" + etape + "/INC=" + increment + "/FACT=" + facteur);

          if (reversed) {
            iterator = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).listIterator(size);
          } else {
            iterator = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).listIterator();
          }
        } else {
          deltaMinuteTemp += 60;
        }
      }

    }
    return values;
  }

}
