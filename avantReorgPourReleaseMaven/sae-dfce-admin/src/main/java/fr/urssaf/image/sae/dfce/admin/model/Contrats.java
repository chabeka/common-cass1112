package fr.urssaf.image.sae.dfce.admin.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Cette classe contient la liste des contrats de services suite à la
 * désérialisation du fichier xml [contrats.xml]
 * <ul>
 * <li>contrats : La liste des contrats de services.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
@XStreamAlias("contrats")
public class Contrats {
	@XStreamImplicit(itemFieldName = "contrat")
	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
	private List<String> contrats;

	/**
	 * @param contrats
	 *            : Les contrats de services.
	 */
	public final void setContrats(final List<String> contrats) {
		this.contrats = contrats;
	}

	/**
	 * @return Les contrats de services
	 */
	public final List<String> getContrats() {
		return contrats;
	}
}
