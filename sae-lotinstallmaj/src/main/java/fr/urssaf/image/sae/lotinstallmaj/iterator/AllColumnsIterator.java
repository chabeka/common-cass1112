package fr.urssaf.image.sae.lotinstallmaj.iterator;

import java.util.Iterator;
import java.util.List;
 
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
 
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 * Iterateur permettant d'iterer sur toutes les colonnes d'une ligne d'une CF.
 *  
 * @param <N> Type pour le nom d'une colonne
 * @param <V> Type pour la valeur d'une colonne
 */
public class AllColumnsIterator<N, V> implements Iterator<HColumn<N, V>> {
    private N start;
    private int count;
    Iterator<HColumn<N, V>> columnsIterator;
    SliceQuery<?, N, V> query;
    private boolean isLastIteration;
 
    /**
     * Constructeur.
     * @param query SliceQuery
     */
    public AllColumnsIterator(SliceQuery<?, N, V> query) {
        start = null;
        count = 100;
        columnsIterator = null;
        this.query = query;
        isLastIteration = false;
    }
 
    /**
     * Methode permettant de recuperer l'iterateur.
     * @return Iterator
     */
    public Iterator<HColumn<N, V>> iterator() {
        return this;
    }
 
    /**
     * Methode permettant de savoir s'il existe d'autres elements.
     * @return boolean
     */
    public boolean hasNext() {
        if (columnsIterator == null || !columnsIterator.hasNext()) {
            if (isLastIteration)
                return false;
 
            if (!fetchMore())
                return false;
        }
        return true;
    }
 
    /**
     * Methode permettant de recuperer la colonne suivante.
     * @return HColumn<N, V>
     */
    public HColumn<N, V> next() {
        return columnsIterator.next();
    }
 
    /**
     * Methode permettant d'avoir plus d'elements.
     * @return boolean
     */
    private boolean fetchMore() {
        try {
            query.setRange(start, null, false, count);
            ColumnSlice<N, V> slice = query.execute().get();
            List<HColumn<N, V>> columns = slice.getColumns();
            int origSize = columns.size();
 
            if (origSize == 0) {
                return false;
            }
 
            if (origSize >= count)
                start = columns.remove(columns.size()-1).getName();
 
            columnsIterator = columns.iterator();
 
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
