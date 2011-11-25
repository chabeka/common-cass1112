package fr.urssaf.image.sae.dfce.admin.services;

import net.docubase.toolkit.service.ServiceProvider;

/**
 * Cette classe contient les éléments communs des différentes services.
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractService {
	private ServiceProvider serviceProvider;

	/**
	 * Permet d'initialiser le service provider.
	 * 
	 * @param serviceProvider
	 *            : Le service provider.
	 */
	public final void setServiceProvider(final ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
/**
 * 
 * @return Le service provider
 */
	public final ServiceProvider getServiceProvider() {
		return serviceProvider;
	}
}
