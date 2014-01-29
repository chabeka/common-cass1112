package fr.urssaf.image.sae.metadata.rules;

/**
 * Classe abstraite représentant une unité de règle
 * 
 * @param <T>
 *            : Type générique
 * @param <E>
 *            : Type générique
 */
public abstract class AbstractLeafRule<T, E> {
/**
 * 
 * @param tCandidate : un objet générique.
 * @param eCandidate : un objet générique.
 * @return True si la règle est satisfaite
 */
	public abstract boolean isSatisfiedBy(T tCandidate, E eCandidate);

}
