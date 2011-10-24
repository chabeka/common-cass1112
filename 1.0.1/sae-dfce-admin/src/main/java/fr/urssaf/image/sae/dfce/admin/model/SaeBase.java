/**
 * 
 */
package fr.urssaf.image.sae.dfce.admin.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe permettant de désérialiser les données de la base documentaire.<BR />
 * elle contient les s :
 * <ul>
 * <li>
 * baseId : Le nom de la base</li>
 * <li>baseDescription : Le descriptif de la base</li>
 * <li>categories :Les catégories</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
@XStreamAlias("base")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class SaeBase {
	private String baseId;
	private String baseDescription;
	@XStreamAlias("categories")
	private SaeCategories saeCategories;
	/**
	 * @return Les categories
	 */
	public final SaeCategories getSaeCategories() {
		return saeCategories;
	}
	/**
	 * @param seaCategories
	 *            : Les categories
	 */
	public final void setSaeCategories(final SaeCategories seaCategories) {
		this.saeCategories = seaCategories;
	}
	/**
	 * @param baseId
	 *            : Le libellé de la base
	 */
	public final void setBaseId(final String baseId) {
		this.baseId = baseId;
	}

	/**
	 * @return Le libellé de la base
	 */
	public final String getBaseId() {
		return baseId;
	}

	/**
	 * @param baseDescription
	 *            : Le descriptif de la base
	 */
	public final void setBaseDescription(final String baseDescription) {
		this.baseDescription = baseDescription;
	}

	/**
	 * @return Le descriptif de la base
	 */
	public final String getBaseDescription() {
		return baseDescription;
	}

	/** {@inheritDoc} */
	@Override
	public final String toString() {
		final ToStringBuilder toStringBuilder = new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("BaseId", baseId);
		toStringBuilder.append("BaseDescription", baseDescription);
		
		if (saeCategories != null) {
			toStringBuilder.append("saeCategories", saeCategories.toString());
		}
		return toStringBuilder.toString();
	}

}
