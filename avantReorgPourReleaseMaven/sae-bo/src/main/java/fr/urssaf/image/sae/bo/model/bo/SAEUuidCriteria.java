package fr.urssaf.image.sae.bo.model.bo;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import fr.urssaf.image.sae.bo.model.AbstractSAECriteria;

/**
 * Classe représentant le critère de recherche par UUID elle contient un UUID et
 * une liste de métadonnées souhaitées à retourner.<br/>
 * Elle contient les attributs :
 * <ul>
 * li>uuid :L'identifiant unique d’un document.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
public class SAEUuidCriteria extends AbstractSAECriteria {
	private UUID uuid;

	/**
	 * @return L'identifiant unique d’un document
	 */
	public final UUID getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *            : L'identifiant unique d’un document.
	 */
	public final void setUuid(final UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * Construit un objet de type {@link SAEUuidCriteria}
	 * 
	 * @param uuid
	 *            : L'identifiant unique d’un document.
	 */
	public SAEUuidCriteria(final UUID uuid) {
		super(null);
		this.uuid = uuid;
	}

	/**
	 * Construit un objet de type {@link SAEUuidCriteria}
	 * 
	 */
	public SAEUuidCriteria() {
		super();
	}

	/**
	 * Construit un objet de type {@link SAEUuidCriteria}
	 * 
	 * @param uuid
	 *            : L'identifiant unique d’un document.
	 * @param metadatas
	 *            : La liste des métadonnées désirées.
	 */
	public SAEUuidCriteria(final UUID uuid, final List<SAEMetadata> metadatas) {
		super(metadatas);
		this.uuid = uuid;
	}

	/**
	 * {@inheritDoc}
	 */
	public final  String toString() {
		final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
		toStrBuilder.append("uuid", uuid);
		if (getDesiredSAEMetadatas() != null) {
			for (SAEMetadata metadata : getDesiredSAEMetadatas()) {
				toStrBuilder.append(" code long", metadata.getLongCode());
			}
		}
		return toStrBuilder.toString();

	}
}
