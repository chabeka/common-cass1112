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

import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.serializer.TraceRegTechniqueIndexSerializer;

/**
 * Itérateur sur les index des traces de surveillance technique
 * 
 */
public class TraceRegTechniqueIndexIterator implements
      Iterator<TraceRegTechniqueIndex> {

   private final ColumnSliceIterator<Date, UUID, TraceRegTechniqueIndex> sliceIterator;

   /**
    * Constructeur
    * 
    * @param sliceQuery
    *           La requête cassandra permettant de faire l'itération
    */
   public TraceRegTechniqueIndexIterator(
         SliceQuery<Date, UUID, TraceRegTechniqueIndex> sliceQuery) {
      sliceIterator = new ColumnSliceIterator<Date, UUID, TraceRegTechniqueIndex>(
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
    */
   public TraceRegTechniqueIndexIterator(
         SliceQuery<Date, UUID, TraceRegTechniqueIndex> sliceQuery, UUID start,
         UUID end) {
      sliceIterator = new ColumnSliceIterator<Date, UUID, TraceRegTechniqueIndex>(
            sliceQuery, start, end, false);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean hasNext() {
      return sliceIterator.hasNext();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TraceRegTechniqueIndex next() {
      HColumn<UUID, TraceRegTechniqueIndex> column = sliceIterator.next();
      return TraceRegTechniqueIndexSerializer.get().fromByteBuffer(
            column.getValueBytes());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void remove() {
      throw new NotImplementedException();

   }

}
