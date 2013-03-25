/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.iterators.ReverseListIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * Classe de support pour les historiques des événements
 * 
 */
@Component
public class HistEvenementSupport {

   @Autowired
   private ServiceProviderSupport support;

   /**
    * Ajout d'une trace dans l'historique des événéments
    * 
    * @param trace
    *           la trace à ajouter
    */
   public final void create(TraceToCreate trace) {

      RMSystemEvent event = ToolkitFactory.getInstance().createRMSystemEvent();
      event.setEventDescription(trace.toString());
      support.getRecordManagerService().createCustomSystemEventLog(event);
   }

   /**
    * Recherche des traces existantes dans l'historique des événements
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
    * @return la liste des traces
    */
   @SuppressWarnings("unchecked")
   public final List<RMSystemEvent> findByDates(Date dateDebut, Date dateFin,
         int limite, boolean reversed) {

      List<RMSystemEvent> events = support.getRecordManagerService()
            .getSystemEventLogsByDates(dateDebut, dateFin);

      List<RMSystemEvent> values = null;

      if (CollectionUtils.isNotEmpty(events)) {
         Iterator<RMSystemEvent> iterator;
         if (reversed) {
            iterator = new ReverseListIterator(events);
         } else {
            iterator = events.iterator();
         }

         int countLeft = limite;
         values = new ArrayList<RMSystemEvent>(limite);
         while (countLeft > 0 && iterator.hasNext()) {
            values.add(iterator.next());
            countLeft--;
         }
      }

      return values;
   }
}
