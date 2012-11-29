/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.iterator;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.lang.NotImplementedException;

import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.serializer.TraceRegSecuriteIndexSerializer;

/**
 * Itérateur sur les index des traces de sécurité
 * 
 */
public class TraceRegSecuriteIndexIterator implements
      Iterator<TraceRegSecuriteIndex> {

   private final ColumnSliceIterator<Date, UUID, TraceRegSecuriteIndex> sliceIterator;

   /**
    * Constructeur
    * 
    * @param sliceQuery
    *           La requête cassandra permettant de faire l'itération
    */
   public TraceRegSecuriteIndexIterator(
         SliceQuery<Date, UUID, TraceRegSecuriteIndex> sliceQuery) {
      sliceIterator = new ColumnSliceIterator<Date, UUID, TraceRegSecuriteIndex>(
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
   public TraceRegSecuriteIndexIterator(
         SliceQuery<Date, UUID, TraceRegSecuriteIndex> sliceQuery, UUID start,
         UUID end, boolean reversed) {
      if (reversed) {
         sliceIterator = new ColumnSliceIterator<Date, UUID, TraceRegSecuriteIndex>(
               sliceQuery, end, start, reversed);
      } else {
         sliceIterator = new ColumnSliceIterator<Date, UUID, TraceRegSecuriteIndex>(
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
   public final TraceRegSecuriteIndex next() {
      HColumn<UUID, TraceRegSecuriteIndex> column = sliceIterator.next();
      return TraceRegSecuriteIndexSerializer.get().fromByteBuffer(
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
