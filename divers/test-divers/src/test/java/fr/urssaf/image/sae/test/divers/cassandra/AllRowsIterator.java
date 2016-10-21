package fr.urssaf.image.sae.test.divers.cassandra;

import java.util.Iterator;
import java.util.List;

import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
 
public class AllRowsIterator<K, N, V> implements Iterator<Row<K, N, V>> {
    private K start;
    private int count;
    Iterator<Row<K, N, V>> rowsIterator;
    RangeSlicesQuery<K, N, V> query;
    private boolean isLastIteration;
 
    public AllRowsIterator(RangeSlicesQuery<K, N, V> query) {
        start = null;
        count = 100;
        rowsIterator = null;
        this.query = query;
        isLastIteration = false;
    }
 
    public Iterator<Row<K, N, V>> iterator() {
        return this;
    }
 
    public boolean hasNext() {
        if (rowsIterator == null || !rowsIterator.hasNext()) {
            if (isLastIteration)
                return false;
 
            if (!fetchMore())
                return false;
        }
        return true;
    }
 
    public Row<K, N, V> next() {
        return rowsIterator.next();
    }
 
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
 
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
