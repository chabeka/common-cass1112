package fr.urssaf.image.commons.util.comparable;

import java.io.Serializable;


/**
 * Classe de comparaison entre deux objets
 * 
 * @param <O> le type des objects à comparer
 */
public class ObjectComparator<O extends Comparable<O>>
   extends
		AbstractComparator<O>
   implements
      Serializable {

	
   private static final long serialVersionUID = 4896805714992237597L;

   
   @Override
	protected final int compareImpl(O arg1, O arg2) {
		return arg1.compareTo(arg2);
	}
}
