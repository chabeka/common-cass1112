/**
 * 
 */
package fr.urssaf.image.sae.storage.dfce.data.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * Classe Correspondant à occurrence de la base SAE.
 */
@XStreamAlias("docuBase")
public class SaeDocument {
	@XStreamAlias("base")
	private SaeBase base;


	/**
	 * @return the base
	 */
	public final SaeBase getBase() {
		return base;
	}
	/**
	 * @param base : une instance de base
	 */
	public final void setBase(final SaeBase base) {
		this.base = base;
	}
	/**
	 * 
	 * @return une instance de base
	 */
	public final SaeBase getDataBase() {

		return base;
	}

	@Override
	public final String toString() {
		final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
		if (base != null) {
			toStringBuilder.append("base", base.toString());
		}
		
		return toStringBuilder.toString();
	}

	
}
