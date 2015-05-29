package fr.urssaf.image.sae.storage.dfce.data.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe permettant de désérialiser les codes :
 * <ul>
 * <li>
 * codeRnd : Le code RND</li>
 * <li>
 * codefonction : Le code fonction</li>
 * <li>
 * codeActivite : Le code activité</li>
 * </ul>
 * 
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
	 *            : code RND.
	 */
	public final void setCodeRnd(final String codeRnd) {
		this.codeRnd = codeRnd;
	}

	/**
	 * @return code RND.
	 */
	public final String getCodeRnd() {
		return codeRnd;
	}

	/**
	 * @param codeFonction
	 *            : code Fonction.
	 */
	public final void setCodeFonction(final String codeFonction) {
		this.codeFonction = codeFonction;
	}

	/**
	 * @return code Fonction.
	 */
	public  final String getCodeFonction() {
		return codeFonction;
	}

	/**
	 * @param codeActivite
	 *            : code activité.
	 */
	public  final void setCodeActivite( final String codeActivite) {
		this.codeActivite = codeActivite;
	}

	/**
	 * @return Le code activité.
	 */
	public  final String getCodeActivite() {
		return codeActivite;
	}

}
