package fr.urssaf.image.commons.util.collection;

import java.util.Collection;


/**
 * Méthodes utilitaires pour les collections
 *
 */
@SuppressWarnings("PMD.MissingStaticMethodInNonInstantiatableClass")
public final class CollectionUtils {

   
   private CollectionUtils() {
      
   }
   
   
   /**
    * Classe de comparaison entre deux collections.<br>
    * <br>
    * Regarde si deux collections contiennent les mêmes éléments.<br>
    * <br>
    * La comparaison entre les éléments s'appuient sur leur méthode equals.<br>
    * <br>
    * L'ordre des collections n'est pas pris en considération.
    *
    * @param <O> le type d'objet contenu dans la collection
    */
	public static class CollectionComparator<O> {

	   /**
	    * Regarde si deux collections contiennent les mêmes éléments.<br>
	    * <br>
	    * La comparaison entre les éléments s'appuient sur leur méthode equals.<br>
	    * <br>
	    * L'ordre des collections n'est pas pris en considération.<br>
	    * <br>
	    * Les résultats de la comparaison sont les suivants :<br>
	    * <ul>
	    *   <li>si les deux collections sont null : la méthode renvoie TRUE</li>
	    *   <li>si l'un des deux collections est null et pas l'autre : la méthode renvoie FALSE</li>
	    *   <li>si la taille des collections n'est pas identique : la méthode renvoie FALSE</li>
	    *   <li>si les collections contiennent les mêmes éléments : la méthode renvoie TRUE</li>
	    *   <li>sinon : la méthode renvoie FALSE</li>
	    * </ul>
	    * 
	    * @param arg1 la première collection
	    * @param arg2 la deuxième collection
	    * @return true si les collections contiennent les mêmes éléments, false 
	    * dans le cas contraire
	    */
		public final boolean equals(Collection<O> arg1, Collection<O> arg2) {

			boolean isEquals;

			if (arg1 == null && arg2 == null) {
				isEquals = true;
			}

			else if (arg1 == null || arg2 == null) {
				isEquals = false;
			}

			else if (arg1.size() == arg2.size()) {

				isEquals = equalsCollection(arg1, arg2);

			}

			else {
				isEquals = false;
			}

			return isEquals;
		}

		
		private boolean equalsCollection(Collection<O> arg1, Collection<O> arg2) {

			boolean isEquals = true;

			for (O obj1 : arg1) {

				if (!arg2.contains(obj1)) {
					isEquals = false;
					break;
				}

			}

			if (isEquals) {
				for (O obj2 : arg2) {

					if (!arg1.contains(obj2)) {
						isEquals = false;
						break;
					}

				}
			}

			return isEquals;
		}

	}
}
