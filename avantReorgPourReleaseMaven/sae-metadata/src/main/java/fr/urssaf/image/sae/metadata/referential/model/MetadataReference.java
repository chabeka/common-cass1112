package fr.urssaf.image.sae.metadata.referential.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Cette classe représente une métadonnée du référentiel des métadonnées. <BR />
 * Elle contient les attributs :
 * <ul>
 * <li>shortCode : Le code court.</li>
 * <li>longCode : Le code long.</li>
 * <li>type : Le type de la métadonnée.</li>
 * <li>requiredForArchival : True si la métadonnée est obligatoire à l'archivage
 * sinon False.</li>
 * <li>requiredForStorage : True si la métadonnée est obligatoire au stockage
 * sinon False.</li>
 * <li>length : La longueur maximal de la valeur de la métadonnée.</li>
 * <li>pattern : Le motif que la valeur de la métadonnée doit respecter.</li>
 * <li>consultable : True si la métadonnée est consultable par l'utisateur sinon
 * False.</li>
 * <li>searchable : True si la métadonnée est interrogeable par l'utisateur
 * sinon False.</li>
 * <li>defaultConsultable : True si la métadonnée est consultable par
 * l'utisateur par défaut sinon False.</li>
 * <li>archivable : True si la métadonnée est archivable par l'utisateur sinon
 * False.</li>
 * <li>internal : True si la métadonnée est une métadonnée métier sinon False.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
@XStreamAlias("metaDataReference")
@SuppressWarnings("PMD.LongVariable")
public class MetadataReference implements Serializable {

	/**
	 * Version de la serialisation
	 */
	private static final long serialVersionUID = 1L;
	private String shortCode;
	private String longCode;
	private String type;
	private boolean requiredForArchival;
	private boolean requiredForStorage;
	private int length;
	private String pattern;
	private boolean consultable;
	private boolean defaultConsultable;
	private boolean searchable;
	private boolean internal;
	private boolean archivable;
	private String label;
	private String description;

	/**
	 * @return Le code court
	 */
	public final String getShortCode() {
		return shortCode;
	}

	/**
	 * @param shortCode
	 *            : Le code court
	 */
	public final void setShortCode(final String shortCode) {
		this.shortCode = shortCode;
	}

	/**
	 * @return Le code long
	 */
	public final String getLongCode() {
		return longCode;
	}

	/**
	 * @param longCode
	 *            : Le code long
	 */
	public final void setLongCode(final String longCode) {
		this.longCode = longCode;
	}

	/**
	 * @return Le type de la métadonnée
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @param type
	 *            Le type de la métadonnée
	 */
	public final void setType(final String type) {
		this.type = type;
	}

	/**
	 * @return La longueur maximal de la valeur de la métadonnée.
	 */
	public final int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            : La longueur maximal de la valeur de la métadonnée.
	 * 
	 */
	public final void setLength(final int length) {
		this.length = length;
	}

	/**
	 * @return Le motif que la valeur de la métadonnée doit respecter.
	 */
	public final String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern
	 *            : Le motif que la valeur de la métadonnée doit respecter.
	 */
	public final void setPattern(final String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return True si la métadonnée doit être visible par l'utilisateur sinon
	 *         False.
	 */
	public final boolean isConsultable() {
		return consultable;
	}

	/**
	 * @param consultable
	 *            : True si la métadonnée est consultable par l'utilisateur
	 *            sinon False.
	 */
	public final void setConsultable(final boolean consultable) {
		this.consultable = consultable;
	}

	/**
	 * @return True si la métadonnée est interrogeable par l'utilisateur sinon
	 *         False.
	 */
	public final boolean isSearchable() {
		return searchable;
	}

	/**
	 * @param isSearchable
	 *            : True si la métadonnée est interrogeable par l'utilisateur
	 *            sinon False.
	 * 
	 */
	public final void setSearchable(final boolean isSearchable) {
		this.searchable = isSearchable;
	}

	/**
	 * @return True si la métadonnée est une métadonnée métier sinon False.
	 */
	public final boolean isInternal() {
		return internal;
	}

	/**
	 * @param isInternal
	 *            : True si la métadonnée est une métadonnée métier sinon False.
	 */
	public final void setInternal(final boolean isInternal) {
		this.internal = isInternal;
	}

	/**
	 * @param isArchivable
	 *            : True si la métadonnée est interrogeable par l'utilisateur
	 *            sinon False.
	 */
	public final void setArchivable(final boolean isArchivable) {
		this.archivable = isArchivable;
	}

	/**
	 * @return True si la métadonnée est interrogeable par l'utilisateur sinon
	 *         False.
	 */
	public final boolean isArchivable() {
		return archivable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("shortCode", shortCode)
				.append("longCode", longCode)
				.append("label", label)
				.append("pattern", pattern)
				.append("type", type)
				.append("required", "")
				.append("length", length)
				.append("pattern", pattern)
				.append("consultable", consultable)
				.append("archivable", archivable)
				.append("requiredForStorage", requiredForStorage)
				.append("requiredForArchival", requiredForArchival)
				.append("defaultConsultable", defaultConsultable)
				.append("searchable", searchable)
				.append("internal", internal).toString();
	}

	/**
	 * 
	 * @return True si la métadonnée est requise pour l'archivage.
	 */
	public final boolean isRequiredForArchival() {
		return requiredForArchival;
	}

	/**
	 * 
	 * @param requiredForArchival
	 *            : le booleen qui indique si la métadonnée est requise pour
	 *            l'archivage.
	 */
	public final void setRequiredForArchival(final boolean requiredForArchival) {
		this.requiredForArchival = requiredForArchival;
	}

	/**
	 * 
	 * @return True si la métadonnée est requise pour le stockage.
	 */
	public final boolean isRequiredForStorage() {
		return requiredForStorage;
	}

	/**
	 * 
	 * @param requiredForStorage
	 *            : le booleen qui indique si la métadonnée est requise pour le
	 *            stockage.
	 */
	public final void setRequiredForStorage(final boolean requiredForStorage) {
		this.requiredForStorage = requiredForStorage;
	}

	/**
	 * 
	 * @return True si la métadonnée est consultable par défaut.
	 */
	public final boolean isDefaultConsultable() {
		return defaultConsultable;
	}

	/**
	 * 
	 * @param defaultConsultable
	 *            : le booleen qui indique si la métadonnée consultable par
	 *            défaut.
	 */
	public final void setDefaultConsultable(final boolean defaultConsultable) {
		this.defaultConsultable = defaultConsultable;
	}

	/**
	 * @param label
	 *            : Le libellé.
	 */
	public final void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @return Le libellé
	 */
	public final String getLabel() {
		return label;
	}

	/**
	 * @param description
	 *            : Le descriptif
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return Le descriptif
	 */
	public final String getDescription() {
		return description;
	}
}
