package fr.urssaf.image.sae.dfce.admin.services.base.impl;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import junit.framework.Assert;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Test;

import fr.urssaf.image.sae.dfce.admin.model.DataBaseModel;
import fr.urssaf.image.sae.dfce.admin.services.AbstractComponents;
import fr.urssaf.image.sae.dfce.admin.services.exceptions.BaseAdministrationServiceEx;
import fr.urssaf.image.sae.dfce.admin.services.exceptions.ConnectionServiceEx;
import fr.urssaf.image.sae.dfce.admin.utils.BaseUtils;


public class UpdateAllIndexesDFCETest extends AbstractComponents {
	/**
	 * Permet de supprimer une base
	 * @throws ConnectionServiceEx Exception levée lorsque la connection ne se passe pas bien.
	 * @throws BaseAdministrationServiceEx  Exception levée lorsque la le service de suppression ne se passe pas bien.
	 * @throws FileNotFoundException Exception levée lorsque le fichier n'existe pas.
	 * @throws MalformedURLException Exception levée
	 */
	@Test
	public void updateAllIndexesUsage() throws ConnectionServiceEx,
			BaseAdministrationServiceEx, FileNotFoundException, MalformedURLException {
		
		final DataBaseModel dataModel = getBaseAdmiService().getDataBaseModel(BaseUtils.BASE_XML_FILE, getXmlDBModelService());
		getBaseAdmiService().openConnection();
		getBaseAdmiService().updateAllIndexesUsage(dataModel);
		final String baseSae = dataModel.getDataBase().getBaseId();
		final ServiceProvider service = ServiceProvider.newServiceProvider();
		service.connect(getConnectionParameter().getUser().getLogin(),
				getConnectionParameter().getUser().getPassword(),BaseUtils.buildUrlForConnection(getConnectionParameter()));
		Assert.assertNotNull(service.getBaseAdministrationService().getBase(baseSae.trim()));
	}
	
	
}
