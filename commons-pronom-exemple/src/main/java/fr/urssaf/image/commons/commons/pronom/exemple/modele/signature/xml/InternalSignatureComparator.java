
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

import java.util.Comparator;

public class InternalSignatureComparator implements Comparator<InternalSignature> {

    @Override
    public final int compare(InternalSignature o1, InternalSignature o2) {
        final int o1SortOrder = o1.getSortOrder();
        final int o2SortOrder = o2.getSortOrder();
        return o1SortOrder < o2SortOrder ? -1 : o1SortOrder > o2SortOrder ? 1 : 0;
    }

}
