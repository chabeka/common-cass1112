package fr.urssaf.image.sae.dfce.admin.model;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import fr.urssaf.image.sae.dfce.admin.utils.Utils;

/**
 * 
 * Cette classe contient les indexComposites suite à la désérialisation du
 * fichier xml [SaeBase.xml].<BR />
 * <ul>
 * <li>
 * indexComposites : les indexComposites</li>
 * </ul>
 * 
 * @author akenore
 * 
 */

@XStreamAlias("indexComposites")
public class IndexComposites {
	@XStreamImplicit(itemFieldName = "indexComposite")
	private List<String> indexComp;

	/**
	 * @param indexComposites
	 *            : les indexCompositest
	 */
	public final void setIndexComp(final List<String> indexComposites) {
		this.indexComp = indexComposites;
	}

	/**
	 * 
	 * @return La liste des indexComposites
	 */
	public final List<String> getIndexComp() {
		return Collections.unmodifiableList(indexComp);
	}

	@Override
	public final String toString() {
		final ToStringBuilder toStringBuilder = new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE);
		for (String index : Utils.nullSafeIterable(indexComp)) {
			toStringBuilder.append("indexComposite", index);
		}
		return toStringBuilder.toString();
	}

}
