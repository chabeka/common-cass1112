package fr.urssaf.image.sae.dfce.admin.services.validation;

import java.io.FileNotFoundException;

import org.junit.Test;

import fr.urssaf.image.sae.dfce.admin.services.AbstractComponents;
import fr.urssaf.image.sae.dfce.admin.services.exceptions.BaseAdministrationServiceEx;
import fr.urssaf.image.sae.dfce.admin.services.exceptions.ConnectionServiceEx;

/**
 * Classe permettant de tester la validation par aspect du service de
 * suppression.
 * 
 * @author akenore
 * 
 */
public class DeletionBaseValidationTest extends AbstractComponents {
	/**
	 * Test unitaire de validation de la suppression d'une base
	 * 
	 * @throws ConnectionServiceEx
	 *             Exception levée lorsque la connection ne se passe pas bien.
	 * @throws BaseAdministrationServiceEx
	 *             Exception levée lorsque la le service de suppression ne se
	 *             passe pas bien.
	 * @throws FileNotFoundException
	 *             Exception levée lorsque le fichier n'existe pas.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteBase() throws ConnectionServiceEx,
			BaseAdministrationServiceEx, FileNotFoundException {
		getBaseAdmiService().openConnection();
		getBaseAdmiService().deleteBase(null);
	}

}
