package fr.urssaf.image.sae.dfce.admin.model;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import fr.urssaf.image.sae.dfce.admin.utils.Utils;

/**
 * Cette classe contient les catégories suite à la désérialisation du fichier
 * xml [SaeBase.xml]
 * <ul>
 * <li>categories : Les catégories</li>
 * </ul>
 * 
 * @author akenore
 * 
 */

@XStreamAlias("categories")
public class SaeCategories {
	@XStreamImplicit(itemFieldName = "category")
	private List<SaeCategory> categories;

	/**
	 * @param categories
	 *            : Les catégories
	 */
	public final void setCategories(final List<SaeCategory> categories) {
		this.categories = categories;
	}

	/**
	 * @return la liste des catégories.
	 */
	public final List<SaeCategory> getCategories() {
		return categories;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
		for (SaeCategory saeCategory : Utils.nullSafeIterable(categories)) {
			toStringBuilder.append("SaeCategory", saeCategory.toString());
		}
		return toStringBuilder.toString();
	}

}
