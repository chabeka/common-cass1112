package fr.urssaf.image.sae.bo.model.untyped;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import fr.urssaf.image.sae.bo.model.AbstractMetadata;

/**
 * Classe représentant un document c'est-à-dire un tableau de byte correspondant
 * au contenu du document et la liste des métadonnées(liste de paire(code,
 * valeur)dont les valeurs sont non typées).<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>longCode : Le code long de la métadonnée.</li>
 * <li>value : La valeur de la métadonnée.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
public class UntypedMetadata extends AbstractMetadata {

	private String value;

	/**
	 * @param value
	 *            : La valeur de la métadonnée.
	 */
	public final void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @return La valeur de la métadonnée.
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * Construit un objet de type {@link UntypedMetadata}
	 */
	public UntypedMetadata() {
		super();
	}

	/**
	 * Construit un objet de type {@link UntypedMetadata}
	 * 
	 * @param longCode
	 *            : Le long court de la métadonnée métier.
	 * @param value
	 *            : La valeur de la métadonnée.
	 */
	public UntypedMetadata(final String longCode, final String value) {
		super(longCode);
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
				ToStringStyle.SHORT_PREFIX_STYLE);  
		toStrBuilder.append("code long:", getLongCode());
		toStrBuilder.append("value", getValue());
		return toStrBuilder.toString();

	}
}
