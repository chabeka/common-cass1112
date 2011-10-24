package fr.urssaf.image.sae.bo.model.bo;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import fr.urssaf.image.sae.bo.model.SAEError;

/**
 * Classe qui réprésente un document metier en erreur.<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>errors : La liste des erreurs .</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
public class SAEDocumentOnError extends SAEDocument {
	private List<SAEError> errors;

	/**
	 * 
	 * @param errors
	 *            : La liste des erreurs.
	 */
	public final void setErrors(final List<SAEError> errors) {
		this.errors = errors;
	}

	/**
	 * 
	 * @return La liste des erreurs.
	 */
	public final List<SAEError> getErrors() {
		return errors;
	}

	/**
	 * {@inheritDoc}
	 */
	public  final String toString() {
		final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		if (getMetadatas() != null) {
			for (SAEMetadata metadata : getMetadatas()) {
				toStrBuilder.append(metadata.toString());
			}
		}
		if (errors != null) {
			for (SAEError error : errors) {
				toStrBuilder.append(error.toString());
			}
		}
		return toStrBuilder.toString();
	}
}
