package fr.urssaf.image.sae.bo.model;

import java.util.List;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;

/**
 * Classe abstraite contenant les éléments communs des critères de recherches
 * des documents métiers.<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>desiredSAEMetadatas : La liste des métadonnées métiers.</li>
 * </ul>
 */
@SuppressWarnings("PMD.LongVariable")
public class AbstractSAECriteria {

	private List<SAEMetadata> desiredSAEMetadatas;

	/**
	 * @return La liste des métadonnées métiers.
	 */
	public final List<SAEMetadata> getDesiredSAEMetadatas() {
		return desiredSAEMetadatas;
	}

	/**
	 * @param metadatas
	 *            : La liste des métadonnées métiers souhaitées.
	 */
	public final void setDesiredSAEMetadatas(final List<SAEMetadata> metadatas) {
		this.desiredSAEMetadatas = metadatas;
	}

	/**
	 * Construit un objet de type {@link AbstractSAECriteria}
	 */
	public AbstractSAECriteria() {
		// Ici on fait rien
	}

	/**
	 * Construit un objet de type {@link AbstractSAECriteria}.
	 * 
	 * @param metadatas
	 *            : La liste des métadonnées métiers souhaitées.
	 */
	public AbstractSAECriteria(final List<SAEMetadata> metadatas) {
		this.desiredSAEMetadatas = metadatas;
	}

}
