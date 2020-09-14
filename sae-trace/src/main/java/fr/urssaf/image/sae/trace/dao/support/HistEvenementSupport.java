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
    // Initialisation
    List<DfceTraceSyst> values = null;
    ListIterator<RMSystemEvent> iterator;
    int tailleTemp = 0;
    long deltaSecondeTemp = 1;
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
      final int size = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).size();
      iterator = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).listIterator(size);
    } else {
      iterator = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).listIterator();
    }

    // Itération de recherche sur les intervalles construits dynamiquement
    // On sort de la boucle si on est arrivé à la fin de l'intervalle ou si on a atteint la limite demandée
    while (tailleTemp < limit && deltaSecondeTemp <= deltaSeconde) {
      // On ajoute la trace si on a un suivant ou un précédent (cas reversed)
      if (reversed && iterator.hasPrevious() || !reversed && iterator.hasNext()) {
        if (values == null) {
          values = new ArrayList<>();
        }
        if (reversed && iterator.hasPrevious()) {
          values.add(createDfceTraceSyst(iterator.previous()));
        }
        if (!reversed && iterator.hasNext()) {
          values.add(createDfceTraceSyst(iterator.next()));
        }
        tailleTemp += 1;
      } else {
        if (deltaSecondeTemp != deltaSeconde) {
          deltaSecondeTemp += increment * 60;
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
            deltaSecondeTemp = deltaSeconde;
          }
          date2 = c2.getTime();
          if (date2.after(dateFin)) {
            c2.setTime(dateFin);
            deltaSecondeTemp = deltaSeconde;
          }
          final int size = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).size();
          incrementPrecedent = increment;
          int facteur = calculFacteur(limit, size);

          increment = updateIncrement(limit, increment, size, facteur);
          etape += 1;
          // System.out.println("ETAPE" + etape + "/INC=" + increment + "/FACT=" + facteur);

          if (reversed) {
            iterator = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).listIterator(size);
          } else {
            iterator = dfceServices.getSystemEventLogsByDates(c1.getTime(), c2.getTime()).listIterator();
          }
        } else {
          deltaSecondeTemp += 60;
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
  private int updateIncrement(final int limit, int increment, final int size, int facteur) {
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
  private int calculFacteur(final int limit, final int size) {
    int facteur = 2;
    if (size == 0 || limit / size > 500) {
      facteur = 10;
    } else {
      facteur = 2;
    }
    return facteur;
  }

}
