package fr.urssaf.image.sae.metadata.control.services.impl;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;

/**
 * 
 * @author akenore
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
public class AbstractDataProvider {
	@Autowired
	@Qualifier("metadataControlServices")
	private MetadataControlServices controlService;
	
	/**
	 * @param controlService
	 *            : Une instance de {@link MetadataControlServices}
	 */
	public final void setControlService(
			final MetadataControlServices controlService) {
		this.controlService = controlService;
	}

	/**
	 * @return Une instance de {@link MetadataControlServices}
	 */
	public final MetadataControlServices getControlService() {
		return controlService;
	}

}
