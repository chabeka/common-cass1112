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

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.serializer.TraceJournalEvtIndexSerializer;

/**
 * Itérateur sur les index des traces du journal des événements SAE
 * 
 */
public class TraceJournalEvtIndexIterator implements
      Iterator<TraceJournalEvtIndex> {

   private final ColumnSliceIterator<String, UUID, TraceJournalEvtIndex> sliceIterator;

   /**
    * Constructeur
    * 
    * @param sliceQuery
    *           La requête cassandra permettant de faire l'itération
    */
   public TraceJournalEvtIndexIterator(
         SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery) {
      sliceIterator = new ColumnSliceIterator<String, UUID, TraceJournalEvtIndex>(
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
   public TraceJournalEvtIndexIterator(
         SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery, UUID start,
         UUID end, boolean reversed) {
      if (reversed) {
         sliceIterator = new ColumnSliceIterator<String, UUID, TraceJournalEvtIndex>(
               sliceQuery, end, start, reversed);
      } else {
         sliceIterator = new ColumnSliceIterator<String, UUID, TraceJournalEvtIndex>(
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
   public final TraceJournalEvtIndex next() {
      HColumn<UUID, TraceJournalEvtIndex> column = sliceIterator.next();
      return TraceJournalEvtIndexSerializer.get().fromByteBuffer(
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
