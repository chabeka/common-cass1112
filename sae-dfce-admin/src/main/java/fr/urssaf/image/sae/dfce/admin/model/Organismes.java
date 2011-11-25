package fr.urssaf.image.sae.dfce.admin.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Cette classe contient la structure du
 * CodeOrgaProprietaire,CodeOrgaGestionnaire, SiteAcquisition suite à la
 * désérialisation du fichier xml [organisme.xml].<BR />
 * Elle contient :
 * <ul>
 * <li>organismes : La liste des différents organismes.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
@XStreamAlias("organismes")
public class Organismes {
	@XStreamImplicit(itemFieldName = "organisme")
	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
	private List<Organisme> organismes;

	/**
	 * @param organismes
	 *            : La liste des organismes
	 */
	public final void setOrganismes(final List<Organisme> organismes) {
		this.organismes = organismes;
	}

	/**
	 * @return La liste des organismes
	 */
	public final List<Organisme> getOrganismes() {
		return organismes;
	}
}
