/**
 *
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.iterators.ReverseListIterator;
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

      final List<RMDocEvent> events = dfceServices.getDocumentEventLogsByDates(dateDebut, dateFin);

      List<DfceTraceDoc> values = null;

      if (CollectionUtils.isNotEmpty(events)) {
         Iterator<RMDocEvent> iterator;
         if (reversed) {
            iterator = new ReverseListIterator(events);
         } else {
            iterator = events.iterator();
         }

         int countLeft = limite;
         values = new ArrayList<DfceTraceDoc>(limite);
         while (countLeft > 0 && iterator.hasNext()) {
            final RMDocEvent event = iterator.next();
            final DfceTraceDoc trace = new DfceTraceDoc();
            trace.setAttributs(event.getAttributes());
            trace.setDateEvt(event.getEventDate());
            trace.setDocUuid(event.getDocUUID());
            trace.setLogin(event.getUsername());
            if (event.getEventType() != null) {
               trace.setTypeEvt(event.getEventType().toString());
            }
            trace.setIdJournal(event.getArchiveUUID());
            values.add(trace);
            countLeft--;
         }
      }

      return values;

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
      final List<DfceTraceDoc> listeTraces = new ArrayList<DfceTraceDoc>();
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
