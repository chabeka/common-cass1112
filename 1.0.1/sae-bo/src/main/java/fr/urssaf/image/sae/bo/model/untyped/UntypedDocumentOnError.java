package fr.urssaf.image.sae.bo.model.untyped;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import fr.urssaf.image.sae.bo.model.MetadataError;

/**
 * Classe qui réprésente un document non typé en erreur.<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>errors : La liste des erreurs .</li>
 * </ul>
 * 
 * @author akenore
 */
public class UntypedDocumentOnError extends UntypedDocument {

	/** La liste des erreurs. */
	private List<MetadataError> errors;

	/**
	 * @param err
	 *            : La liste des erreurs.
	 */
	public final void setErrors(final List<MetadataError> err) {
		this.errors = err;
	}

	/**
	 * @return La liste des erreurs.
	 */
	public final List<MetadataError> getErrors() {
		return errors;
	}

	/**
	 * Construit un objet de type {@link UntypedDocumentOnError }.
	 * 
	 * @param content
	 *            : Le contenu du document métier.
	 * @param metadatas
	 *            : La liste des métadonnées non typés.
	 * @param err
	 *            : La liste des erreurs.
	 */
	public UntypedDocumentOnError(final byte[] content,
			final List<UntypedMetadata> metadatas, final List<MetadataError> err) {
		super(content, metadatas);
		this.errors = err;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		if (getUMetadatas() != null) {
			for (UntypedMetadata uMetadata : getUMetadatas()) {
				toStrBuilder.append(uMetadata.toString());
			}
		}
		if (errors != null) {
			for (MetadataError error : errors) {
				toStrBuilder.append(error.toString());
			}
		}
		return toStrBuilder.toString();
	}
}
