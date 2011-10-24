package fr.urssaf.image.sae.dfce.admin.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Cette classe contient les différents types codes images  suite à la désérialisation du fichier xml [codefonctions.xml].<BR />
 * Elle contient :
 * <ul>
 * <li>codernd : Le code RND</li>
 * <li>codefonction : Le code fonction</li>
 * <li>codeactivite : Le code activité</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
@XStreamAlias("codeimage")
public class CodeImage {
	@XStreamAlias("codernd")
	private String codeRnd;
	@XStreamAlias("codefonction")
	private String codeFonction;
	@XStreamAlias("codeactivite")
	private String codeActivite;

	/**
	 * @param codeRnd
	 *            : Le code RND.
	 */
	public final void setCodeRnd(final String codeRnd) {
		this.codeRnd = codeRnd;
	}

	/**
	 * @return Le code RND.
	 */
	public final String getCodeRnd() {
		return codeRnd;
	}

	/**
	 * @param codeFonction
	 *            : Le code Fonction.
	 */
	public final void setCodeFonction(final String codeFonction) {
		this.codeFonction = codeFonction;
	}

	/**
	 * @return code Fonction.
	 */
	public final String getCodeFonction() {
		return codeFonction;
	}

	/**
	 * @param codeActivite
	 *            : Le code activité.
	 */
	public final void setCodeActivite(final String codeActivite) {
		this.codeActivite = codeActivite;
	}

	/**
	 * @return Le code activité.
	 */
	public final String getCodeActivite() {
		return codeActivite;
	}

}
