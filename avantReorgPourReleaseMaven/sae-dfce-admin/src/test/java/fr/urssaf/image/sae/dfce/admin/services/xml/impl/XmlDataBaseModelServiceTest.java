package fr.urssaf.image.sae.dfce.admin.services.xml.impl;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.dfce.admin.model.DataBaseModel;
import fr.urssaf.image.sae.dfce.admin.services.xml.XmlDataService;

/**
 * Contient les tests des service de gestion du fichier xml contenant les
 * données sur la base.
 * 
 * @author akenore
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-dfce-admin.xml" })
public class XmlDataBaseModelServiceTest {
	private DataBaseModel dataBaseModel;
	@SuppressWarnings("PMD.LongVariable")
	@Autowired
	private XmlDataService xmlDataBaseModelService;

	/**
	 * 
	 * @param xmlDataBaseModelService
	 *            : Le service de gestion du fichier xml
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setXmlDataBaseModelService(
			final XmlDataService xmlDataBaseModelService) {
		this.xmlDataBaseModelService = xmlDataBaseModelService;
	}

	/**
	 * 
	 * @return Le service de gestion du fichier xml
	 */
	public final XmlDataService getXmlDataBaseModelService() {
		return xmlDataBaseModelService;
	}

	/**
	 * 
	 * @param dataBaseModel
	 *            : le modèle de base
	 */
	public final void setDataBaseModel(final DataBaseModel dataBaseModel) {
		this.dataBaseModel = dataBaseModel;
	}

	/**
	 * 
	 * @return le modèle de base
	 */
	public final DataBaseModel getDataBaseModel() {
		return dataBaseModel;
	}

	@Before
	public void getDataBaseModelFormXml() throws FileNotFoundException {
		final DataBaseModel dbModel = xmlDataBaseModelService
				.baseModelReader(new File("src/main/resources/saeBase/saeBase.xml"));
		setDataBaseModel(dbModel);

	}

	// On vérifie qu'on bien récupérer un identifiant de la base.
	@Test
	public void getBase() {
		Assert.assertNotNull(getDataBaseModel().getBase());
	}

	// On vérifie l'identifiant de la base est conforme à celle attendu.
	@Test
	public void getBaseID() {
		Assert.assertEquals("SAE-TEST", getDataBaseModel()
				.getBase().getBaseId().trim());
	}
}
