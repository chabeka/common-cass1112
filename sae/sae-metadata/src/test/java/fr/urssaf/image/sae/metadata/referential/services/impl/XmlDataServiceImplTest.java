package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.XmlDataService;

/**
 * Contient les tests de désérialisation du referentiel des métadonnées
 * 
 * @author akenore
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata.xml" })
public class XmlDataServiceImplTest {
	@Autowired
	@Qualifier("xmlDataService")
	private XmlDataService xmlService;

	@Autowired
	private ApplicationContext context;

	/**
	 * @return Le context.
	 */
	public final ApplicationContext getContext() {
		return context;
	}

	/**
	 * @param context
	 *            : Le context
	 */
	public final void setContext(final ApplicationContext context) {
		this.context = context;
	}

	/**
	 * @param xmlService
	 *            : Le service de lecture du flux Xml.
	 */
	public final void setXmlService(final XmlDataService xmlService) {
		this.xmlService = xmlService;
	}

	/**
	 * @return Le service de lecture du flux Xml.
	 */
	public XmlDataService getXmlService() {
		return xmlService;
	}

	@Test
	public void getReferentielMetaData() throws IOException {

		final InputStream xmlInputStream = context.getResource(
				"classpath:MetadataReferential.xml").getInputStream();
		final Map<String, MetadataReference> ref = xmlService
				.referentialReader(xmlInputStream);
		Assert.assertTrue("Le nombre de métadonnées doit être égal à 54",
				ref.size() == 55);
	}

}
