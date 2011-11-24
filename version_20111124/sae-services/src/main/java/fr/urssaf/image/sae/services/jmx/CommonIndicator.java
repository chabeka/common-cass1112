package fr.urssaf.image.sae.services.jmx;

import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;

/**
 * 
 * @author akenore
 * 
 */
public class CommonIndicator {
	private JmxIndicator indicator;

	/**
	 * @param indicator
	 *            : Les indicateurs de l'archivage de masse.
	 */
	public final void setIndicator(final JmxIndicator indicator) {
		this.indicator = indicator;
	}

	/**
	 * @return Les indicateurs de l'archivage de masse.
	 */
	public final JmxIndicator getIndicator() {
		return indicator;
	}
}
