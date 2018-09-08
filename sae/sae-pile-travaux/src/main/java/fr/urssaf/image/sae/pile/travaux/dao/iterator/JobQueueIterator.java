package fr.urssaf.image.sae.pile.travaux.dao.iterator;

import java.util.Iterator;
import java.util.UUID;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.lang.NotImplementedException;

import fr.urssaf.image.sae.pile.travaux.dao.serializer.JobQueueSerializer;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;

/**
 * Itérateur permettant d'itérer sur les jobs non réservés en attente de traitement
 *
 */
public class JobQueueIterator implements Iterator<JobQueue> {

   private final ColumnSliceIterator<String, UUID, String> sliceIterator;
   
   /**
    * Constructeur
    * @param sliceQuery  La requête cassandra permettant de faire l'itération
    */
   public JobQueueIterator(SliceQuery<String, UUID, String> sliceQuery) {
      sliceIterator = new ColumnSliceIterator<String, UUID, String>(sliceQuery, (UUID)null, (UUID)null, false);
   }
   
   @Override
   public final boolean hasNext() {
      return sliceIterator.hasNext();
   }

   @Override
   public final JobQueue next() {
      HColumn<UUID, String> column = sliceIterator.next();
      return JobQueueSerializer.get().fromBytes(column.getValue().getBytes());
   }

   @Override
   public final void remove() {
      throw new NotImplementedException();      
   }

}
