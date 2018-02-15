package fr.urssaf.image.sae.storage.dfce.data.model;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import fr.urssaf.image.sae.storage.dfce.utils.Utils;

/**
 * Classe permettant de désérialiser la liste des métadonnées souhaitées.<BR />
 * Elle contient l'attribut :
 * <ul>
 * <li>
 * codes : Les codes court des métadonnées souhaitées.</li>
 * </ul>
 */
@XStreamAlias("desiredMetaDatas")
public class DesiredMetaData {

	@XStreamImplicit(itemFieldName = "code")
	private List<String> codes;

	/**
	 * @return Les codes des métadonnées souhaitées
	 */
	public final List<String> getCodes() {
		return codes;
	}

	/**
	 * @param codes
	 *            : Les codes des métadonnées souhaitées
	 */
	public final void setCodes(final List<String> codes) {
		this.codes = codes;
	}

	/** {@inheritDoc} */
	@Override
	public final String toString() {
		final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
		for (String code : Utils.nullSafeIterable(codes)) {
			toStringBuilder.append("code", code);
		}

		return toStringBuilder.toString();
	}

}
