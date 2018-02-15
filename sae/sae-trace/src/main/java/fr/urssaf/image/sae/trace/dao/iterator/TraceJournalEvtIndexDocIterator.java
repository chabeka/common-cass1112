/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.iterator;

import java.util.Iterator;
import java.util.UUID;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.lang.NotImplementedException;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.dao.serializer.TraceJournalEvtIndexDocSerializer;

/**
 * Itérateur sur les index des documents des traces du journal des événements SAE
 * 
 */
public class TraceJournalEvtIndexDocIterator implements
      Iterator<TraceJournalEvtIndexDoc> {

   private final ColumnSliceIterator<String, UUID, TraceJournalEvtIndexDoc> sliceIterator;

   /**
    * Constructeur
    * 
    * @param sliceQuery
    *           La requête cassandra permettant de faire l'itération
    */
   public TraceJournalEvtIndexDocIterator(
         SliceQuery<String, UUID, TraceJournalEvtIndexDoc> sliceQuery) {
      sliceIterator = new ColumnSliceIterator<String, UUID, TraceJournalEvtIndexDoc>(
            sliceQuery, (UUID) null, (UUID) null, false);
   }

   /**
    * Constructeur
    * 
    * @param sliceQuery
    *           La requête cassandra permettant de faire l'itération
    * @param start
    *           TimeUUID de la première colonne
    * @param end
    *           TimeUUID de la dernière colonne
    * @param reversed
    *           booleen indiquant si l'ordre décroissant doit etre appliqué<br>
    *           <ul>
    *           <li>true : ordre décroissant</li>
    *           <li>false : ordre croissant</li>
    *           </ul>
    */
   public TraceJournalEvtIndexDocIterator(
         SliceQuery<String, UUID, TraceJournalEvtIndexDoc> sliceQuery, UUID start,
         UUID end, boolean reversed) {
      if (reversed) {
         sliceIterator = new ColumnSliceIterator<String, UUID, TraceJournalEvtIndexDoc>(
               sliceQuery, end, start, reversed);
      } else {
         sliceIterator = new ColumnSliceIterator<String, UUID, TraceJournalEvtIndexDoc>(
               sliceQuery, start, end, reversed);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean hasNext() {
      return sliceIterator.hasNext();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final TraceJournalEvtIndexDoc next() {
      HColumn<UUID, TraceJournalEvtIndexDoc> column = sliceIterator.next();
      return TraceJournalEvtIndexDocSerializer.get().fromByteBuffer(
            column.getValueBytes());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void remove() {
      throw new NotImplementedException();
   }
}
