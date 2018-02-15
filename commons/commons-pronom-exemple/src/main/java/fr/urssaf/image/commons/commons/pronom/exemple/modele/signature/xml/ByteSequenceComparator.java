
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

import java.util.Comparator;

/**
 * Compare deux sequences de bytes.
 */
public class ByteSequenceComparator implements Comparator<ByteSequence> {

    @Override
    public final int compare(ByteSequence o1, ByteSequence o2) {
        final int o1SortOrder = o1.getSortOrder();
        final int o2SortOrder = o2.getSortOrder();
        // use safe method of comparing sort orders (no possibility of number overflow
        // which may be caused if sortOrder numbers are large)
        return o1SortOrder < o2SortOrder ? -1 : o1SortOrder > o2SortOrder ? 1 : 0;
    }

}
