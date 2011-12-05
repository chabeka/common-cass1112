/**
 * 
 */
package fr.urssaf.image.sae.dfce.admin.model;

import net.docubase.toolkit.model.base.CategoryDataType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Cette classe contient une catégorie suite à la désérialisation du fichier xml
 * [SaeBase.xml] une catégorie.
 * <ul>
 * <li>name : Le nom de catégorie</li>
 * <li>dataType : Le type de la catégorie</li>
 * <li>minimumValues : La valeur minimale [0: non obligatoire,1:obligatoire].</li>
 * <li>maximumValues : La valeur maximale</li>
 * <li>index : True si la catégorie est indexable False dans le cas contraire.</li>
 * <li>single : A définir</li>
 * <li>enableDictionary : True si la catégorie est prise en compte dans le
 * dictionnaire.</li>
 * <li>descriptif : Le descriptif de la catégorie.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
@XStreamAlias("category")
public class SaeCategory {

	private String name;
	private String dataType;
	private int minimumValues;
	private int maximumValues;
	private boolean index;
	private boolean single;
	private boolean enableDictionary;
	private String descriptif;

	/**
	 * @param descriptif
	 *            : Le descriptif
	 */
	public final void setDescriptif(final String descriptif) {
		this.descriptif = descriptif;
	}

	/**
	 * @return Le descriptif
	 */
	public final String getDescriptif() {
		return descriptif;
	}

	/**
	 * @param name
	 *            : Le nom de la catégorie
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Le nom de la catégorie
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param dataType
	 *            : Le type de la catégorie
	 */
	public final void setDataType(final String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return Le type de la catégorie
	 */
	public final String getDataType() {
		return dataType;
	}

	/**
	 * @param minimumValues
	 *            : La valeur minimal
	 */
	public final void setMinimumValues(final int minimumValues) {
		this.minimumValues = minimumValues;
	}

	/**
	 * @return La valeur minimal
	 **/
	public final int getMinimumValues() {
		return minimumValues;
	}

	/**
	 * @param maximumValues
	 *            : La valeur maximun
	 */
	public final void setMaximumValues(final int maximumValues) {
		this.maximumValues = maximumValues;
	}

	/**
	 * @return La valeur maximun
	 */
	public final int getMaximumValues() {
		return maximumValues;
	}

	/**
	 * @param index
	 *            : Indique si la catégorie doit être indexée.
	 * 
	 */
	public final void setIndex(final boolean index) {
		this.index = index;
	}

	/**
	 * @return L'indexaion de catégorie
	 */
	public final boolean isIndex() {
		return index;
	}

	/**
	 * @param single
	 *            : Indique la contrainte d'unicité sur la valeur de la
	 *            catégorie
	 */
	public final void setSingle(final boolean single) {
		this.single = single;
	}

	/**
	 * @return La contrainte d'unicité de la catégorie
	 */
	public final boolean isSingle() {
		return single;
	}

	/**
	 * @param enableDictionary
	 *            : ""
	 * 
	 */
	public final void setEnableDictionary(final boolean enableDictionary) {
		this.enableDictionary = enableDictionary;
	}

	/**
	 * @return Le type de catégorie
	 * 
	 */
	public final CategoryDataType categoryDataType() {
		return CategoryDataType.valueOf(dataType.trim().toUpperCase());
	}

	/**
	 * @return True s'il faut activé le dictionnaire interne
	 */
	public final boolean isEnableDictionary() {
		return enableDictionary;
	}

	/** {@inheritDoc} */
	@Override
	public final String toString() {
		final ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("Name", name);
		toStringBuilder.append("DataType", dataType);
		toStringBuilder.append("MinimumValues", minimumValues);
		toStringBuilder.append("MaximumValues", maximumValues);
		toStringBuilder.append("Index ", index);
		toStringBuilder.append("Single", single);
		return toStringBuilder.toString();
	}

}
