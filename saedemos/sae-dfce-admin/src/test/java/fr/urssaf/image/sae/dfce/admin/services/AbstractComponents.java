package fr.urssaf.image.sae.dfce.admin.services;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import fr.urssaf.image.sae.dfce.admin.model.ConnectionParameter;
import fr.urssaf.image.sae.dfce.admin.services.base.BaseAdministrationService;
import fr.urssaf.image.sae.dfce.admin.services.xml.XmlDataService;

/**
 * 
 * Contient les éléments commun des services de test
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-dfce-admin.xml" })
public abstract class AbstractComponents {

	@SuppressWarnings("PMD.LongVariable")
	@Autowired
	private ConnectionParameter connectionParameter;
	@Autowired
	private XmlDataService xmlDBModelService;

	@Autowired
	private BaseAdministrationService baseAdmiService;

	
	
	/**
	 * 
	 * @param xmlDBModelService
	 *            : Le service de gestion du fichier xml
	 */
	public final void setXmlDBModelService(
			final XmlDataService xmlDBModelService) {
		this.xmlDBModelService = xmlDBModelService;
	}

	/**
	 * 
	 * @return Le service de gestion du fichier xml
	 */
	public final XmlDataService getXmlDBModelService() {
		return xmlDBModelService;
	}

	/**
	 * 
	 * @param baseAdminService
	 *            : L'ensemble des services de gestion de la base
	 */
	public final void setBaseAdmiService(
			final BaseAdministrationService baseAdminService) {
		this.baseAdmiService = baseAdminService;
	}

	/**
	 * 
	 * @return L'ensemble des services de gestion de la base
	 */
	public final BaseAdministrationService getBaseAdmiService() {
		return baseAdmiService;
	}
	/**
	 * 
	 * @return Les paramètres de connections
	 */
	public final ConnectionParameter getConnectionParameter() {
		return connectionParameter;
	}

	/**
	 * 
	 * @param cnxParameter
	 *            : Les paramètres de connections.
	 */
	public final void setConnectionParameter(
			final ConnectionParameter cnxParameter) {
		this.connectionParameter = cnxParameter;
	}
}
