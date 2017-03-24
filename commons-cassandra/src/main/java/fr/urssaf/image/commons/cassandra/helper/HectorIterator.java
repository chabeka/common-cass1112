package fr.urssaf.image.commons.cassandra.helper;

import java.util.Iterator;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;


/**
 * Crée un vrai itérateur à partir d'un ColumnFamilyResult
 * En effet, ColumnFamilyResultWrapper se positionne automatiquement sur le 1er élément,
 * ce qui empêche de parcourir l'itérateur de manière standard.
 * 
 * @param <K>  type de la clé
 * @param <N>  type de la valeur de la colonne
 */
public class HectorIterator<K,N> implements Iterator<ColumnFamilyResult<K,N>>, Iterable<ColumnFamilyResult<K,N>> {

   private final ColumnFamilyResult<K,N> result;
   private boolean firstIteration = true;

   /**
    * Crée un itérateur itérable de manière standard
    *    <K>  type de la clé
    *    <N>  type de la valeur de la colonne
    * @param result  résultat obtenu par une requête hector via un template
    */
   public HectorIterator(ColumnFamilyResult<K,N> result) {
      this.result = result;
   }
   
   @Override
   public final boolean hasNext() {
      if (firstIteration) {
         return result.hasResults();
      }
      else {
         return result.hasNext();
      }
   }

   @Override
   public final ColumnFamilyResult<K, N> next() {
      if (firstIteration) {
         firstIteration = false;
         return result;
      }
      else {
         return result.next();
      }
   }

   @Override
   public final void remove() {
      result.remove();
   }

   @Override
   public final Iterator<ColumnFamilyResult<K, N>> iterator() {
      return this;
   }

}
