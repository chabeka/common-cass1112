package fr.urssaf.image.sae.lotinstallmaj.iterator;

import java.util.Iterator;
import java.util.List;

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 * Itérateur permettant d'itérer sur toutes les colonnes d'une ligne d'une CF.
 * 
 * @param <N>
 *           Type pour le nom d'une colonne
 * @param <V>
 *           Type pour la valeur d'une colonne
 */
public class AllColumnsIterator<N, V> implements Iterator<HColumn<N, V>> {
   private N start;
   private final int count;
   Iterator<HColumn<N, V>> columnsIterator;
   SliceQuery<?, N, V> query;
   private boolean isLastIteration;

   /**
    * Constructeur.
    * @param query SliceQuery
    */
   public AllColumnsIterator(final SliceQuery<?, N, V> query) {
      start = null;
      count = 100;
      columnsIterator = null;
      this.query = query;
      isLastIteration = false;
   }

    /**
    * Méthode permettant de récuperer l'iterateur.
    * 
    * @return Iterator
    */
   public Iterator<HColumn<N, V>> iterator() {
      return this;
   }

    /**
    * Méthode permettant de savoir s'il existe d'autres éléments.
    * 
    * @return boolean
    */
   @Override
   public boolean hasNext() {
      if (columnsIterator == null || !columnsIterator.hasNext()) {
         if (isLastIteration) {
            return false;
         }

         if (!fetchMore()) {
            return false;
         }
      }
      return true;
   }

    /**
    * Méthode permettant de récupérer la colonne suivante.
    * 
    * @return HColumn<N, V>
    */
   @Override
   public HColumn<N, V> next() {
      return columnsIterator.next();
   }

    /**
    * Méthode permettant d'avoir plus d'élements.
    * 
    * @return boolean
    */
   private boolean fetchMore() {
      try {
         query.setRange(start, null, false, count);
         final ColumnSlice<N, V> slice = query.execute().get();
         final List<HColumn<N, V>> columns = slice.getColumns();
         final int origSize = columns.size();

         if (origSize == 0) {
            return false;
         }

         if (origSize >= count) {
            start = columns.remove(columns.size()-1).getName();
         }

         columnsIterator = columns.iterator();

         if (origSize < count) {
            isLastIteration = true;
         }

         return true;
      } catch (final HectorException e) {
         return false;
      }
   }

    /**
    * Méthode permettant de supprimer un element.
    * Cette méthode renvoie une exception (operation non supportée)
    */
   @Override
   public void remove() {
      throw new UnsupportedOperationException();
   }
}
