package fr.urssaf.image.sae.lotinstallmaj.iterator;

import java.util.Iterator;
import java.util.List;

import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * Itérateur permettant d'itérer sur toutes les lignes d'une CF.
 * 
 * @param <K>
 *           Type de la clé d'une row
 * @param <N>
 *           Type pour le nom d'une colonne
 * @param <V>
 *           Type pour la valeur d'une colonne
 */
public class AllRowsIterator<K, N, V> implements Iterator<Row<K, N, V>> {
   private K start;
   private final int count;
   Iterator<Row<K, N, V>> rowsIterator;
   RangeSlicesQuery<K, N, V> query;
   private boolean isLastIteration;

   /**
    * Constructeur de l'iterateur.
    * @param query RangeSlicesQuery
    */
   public AllRowsIterator(final RangeSlicesQuery<K, N, V> query) {
      start = null;
      count = 100;
      rowsIterator = null;
      this.query = query;
      isLastIteration = false;
   }

    /**
    * Méthode permettant de récupérer l'itérateur.
    * 
    * @return Iterator
    */
   public Iterator<Row<K, N, V>> iterator() {
      return this;
   }

    /**
    * Méthode permettant de savoir s'il existe un element suivant.
    * 
    * @return boolean
    */
   @Override
   public boolean hasNext() {
      if (rowsIterator == null || !rowsIterator.hasNext()) {
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
    * Méthode permettant d'avoir l'élément suivant.
    * 
    * @return Row<K, N, V>
    */
   @Override
   public Row<K, N, V> next() {
      return rowsIterator.next();
   }

    /**
    * Méthode privée permettant d'avoir plus d'éléments.
    * 
    * @return boolean
    */
   private boolean fetchMore() {
      try {
         query.setKeys(start, null);
         query.setRowCount(count);
         final OrderedRows<K, N, V> slice = query.execute().get();
         final List<Row<K, N, V>> rows = slice.getList();
         final int origSize = rows.size();

         if (origSize == 0) {
            return false;
         }

         if (origSize >= count) {
            start = rows.remove(rows.size()-1).getKey();
         }

         rowsIterator = rows.iterator();

         if (origSize < count) {
            isLastIteration = true;
         }

         return true;
      } catch (final HectorException e) {
         return false;
      }
   }

    /**
    * Méthode permettant de supprimer un élément.
    * Cette méthode renvoie une exception (operation non supportée)
    */
   @Override
   public void remove() {
      throw new UnsupportedOperationException();
   }
}
