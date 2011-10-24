package fr.urssaf.image.sae.dfce.admin.services.base.impl;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.dfce.admin.model.DataBaseModel;
import fr.urssaf.image.sae.dfce.admin.services.AbstractComponents;
import fr.urssaf.image.sae.dfce.admin.services.exceptions.BaseAdministrationServiceEx;
import fr.urssaf.image.sae.dfce.admin.services.exceptions.ConnectionServiceEx;
import fr.urssaf.image.sae.dfce.admin.utils.BaseUtils;

public class CreationBaseDFCETest extends AbstractComponents {
	/**
	 * Test unitaire sur la création d'une base
	 * 
	 * @throws ConnectionServiceEx
	 *             Lorsque la connexion ne s'est pas bien déroulée
	 * @throws BaseAdministrationServiceEx
	 *             Lorsque le service de création ne s'est pas bien déroulée
	 * @throws FileNotFoundException Exception levée
	 * @throws MalformedURLException Exception levée
	 */
	@Test
	@Ignore
	public void createBase() throws ConnectionServiceEx,
			BaseAdministrationServiceEx, FileNotFoundException,
			MalformedURLException {
		final DataBaseModel dataModel = getBaseAdmiService().getDataBaseModel(
				BaseUtils.BASE_XML_FILE, getXmlDBModelService());
		getBaseAdmiService().openConnection();
		getBaseAdmiService().createBase(dataModel, getXmlDBModelService());
		final String baseSae = dataModel.getDataBase().getBaseId();
		final ServiceProvider service = ServiceProvider.newServiceProvider();
		service.connect(getConnectionParameter().getUser().getLogin(),
				getConnectionParameter().getUser().getPassword(),
				BaseUtils.buildUrlForConnection(getConnectionParameter()));
		Assert.assertEquals(baseSae.trim(), service
				.getBaseAdministrationService().getBase(baseSae.trim())
				.getBaseId());
		service.disconnect();
		
	}
}
