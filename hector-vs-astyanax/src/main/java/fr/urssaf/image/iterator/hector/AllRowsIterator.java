package fr.urssaf.image.iterator.hector;

import java.util.Iterator;
import java.util.List;

import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
 
/**
 * Iterateur permettant d'iterer sur toutes les lignes d'une CF.
 *  
 * @param <K> Type de la clé d'une row
 * @param <N> Type pour le nom d'une colonne
 * @param <V> Type pour la valeur d'une colonne
 */
public class AllRowsIterator<K, N, V> implements Iterator<Row<K, N, V>> {
    private K start;
    private int count;
    Iterator<Row<K, N, V>> rowsIterator;
    RangeSlicesQuery<K, N, V> query;
    private boolean isLastIteration;
 
    /**
     * Constructeur de l'iterateur.
     * @param query RangeSlicesQuery
     */
    public AllRowsIterator(RangeSlicesQuery<K, N, V> query) {
        start = null;
        count = 100;
        rowsIterator = null;
        this.query = query;
        isLastIteration = false;
    }
 
    /**
     * Methode permettant de recuperer l'iterateur.
     * @return Iterator
     */
    public Iterator<Row<K, N, V>> iterator() {
        return this;
    }
 
    /**
     * Methode permettant de savoir s'il existe un element suivant.
     * @return boolean
     */
    public boolean hasNext() {
        if (rowsIterator == null || !rowsIterator.hasNext()) {
            if (isLastIteration)
                return false;
 
            if (!fetchMore())
                return false;
        }
        return true;
    }
 
    /**
     * Methode permettant d'avoir l'element suivant.
     * @return Row<K, N, V>
     */
    public Row<K, N, V> next() {
        return rowsIterator.next();
    }
 
    /**
     * Methode privee permettant d'avoir plus d'element.
     * @return boolean
     */
    private boolean fetchMore() {
        try {
            query.setKeys(start, null);
            query.setRowCount(count);
            OrderedRows<K, N, V> slice = query.execute().get();
            List<Row<K, N, V>> rows = slice.getList();
            int origSize = rows.size();
 
            if (origSize == 0) {
                return false;
            }
 
            if (origSize >= count)
                start = rows.remove(rows.size()-1).getKey();
 
            rowsIterator = rows.iterator();
 
            if (origSize < count)
                isLastIteration = true;
 
            return true;
        } catch (HectorException e) {
            return false;
        }
    }
 
    /**
     * Methode permettant de supprimer un element.
     * Cette methode renvoie une exception (operation non supportee)
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
