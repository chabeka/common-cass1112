package fr.urssaf.image.sae.dfce.admin.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Cette classe contient le code organisme suite à la
 * désérialisation du fichier xml [organisme.xml].<BR />.
 * <ul>
 * <li>code : Le code de l'organisme.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
@XStreamAlias("organisme")
public class Organisme {
	/**
	 * Le code de l'organisme
	 */
  private String code ;

	/**
	 * @param code : Le code de l'organisme
	 */
	public final void setCode(final String code) {
		this.code = code;
	}

	/**
	 * @return Le code de l'organisme
	 */
	public final String getCode() {
		return code;
	} 
}
