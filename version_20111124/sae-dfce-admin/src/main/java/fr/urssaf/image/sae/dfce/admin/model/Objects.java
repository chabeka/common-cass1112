package fr.urssaf.image.sae.dfce.admin.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Cette classe contient les différents types d'objets suite à la
 * désérialisation du fichier xml [objetTypes.xml].<BR />
 * . Elle contient :
 * <ul>
 * <li>objects : Les différents types d'objets.</li>
 * </ul>
 * @author akenore
 * 
 */
@XStreamAlias("objects")
public class Objects {
	@XStreamImplicit(itemFieldName = "object")
	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
	private List<String> objects;

	/**
	 * @param objects
	 *            : Les objets types
	 */
	public final void setObjects(final List<String> objects) {
		this.objects = objects;
	}

	/**
	 * @return Les objets types
	 */
	public final List<String> getObjects() {
		return objects;
	}
}
