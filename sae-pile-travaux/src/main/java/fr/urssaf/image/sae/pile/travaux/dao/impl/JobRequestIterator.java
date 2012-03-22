package fr.urssaf.image.sae.pile.travaux.dao.impl;

import java.util.Iterator;
import java.util.UUID;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.lang.NotImplementedException;

import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;

/**
 * Itérateur permettant d'itérer sur les jobs non réservés en attente de traitement
 *
 */
public class JobRequestIterator implements Iterator<SimpleJobRequest> {

   private final ColumnSliceIterator<String, UUID, String> sliceIterator;
   
   /**
    * Constructeur
    * @param sliceQuery  La requête cassandra permettant de faire l'itération
    */
   public JobRequestIterator(SliceQuery<String, UUID, String> sliceQuery) {
      sliceIterator = new ColumnSliceIterator<String, UUID, String>(sliceQuery, (UUID)null, (UUID)null, false);
   }
   
   @Override
   public final boolean hasNext() {
      return sliceIterator.hasNext();
   }

   @Override
   public final SimpleJobRequest next() {
      HColumn<UUID, String> column = sliceIterator.next();
      return SimpleJobRequestSerializer.get().fromBytes(column.getValue().getBytes());
   }

   @Override
   public final void remove() {
      throw new NotImplementedException();      
   }

}
