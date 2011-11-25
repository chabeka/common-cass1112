package fr.urssaf.image.sae.dfce.admin.services.validation;

import org.apache.commons.lang.Validate;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.dfce.admin.messages.MessageHandler;
import fr.urssaf.image.sae.dfce.admin.model.DataBaseModel;
import fr.urssaf.image.sae.dfce.admin.services.AbstractService;
import fr.urssaf.image.sae.dfce.admin.services.xml.XmlDataService;
/**
 * Contient les méthodes de validation des services d'admin de la base
 * @author akenore
 *
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Aspect
public class BaseAdministrationServiceValidation extends AbstractService{
	/**
	 * Valide le paramètre d'entrée de la méthode createBase
	 * @param dataBaseModel
	 *            : Le model de base de donnée
	 *            @param xmlDataService
	 *            : Le service de désérialisation xml.
	 */
	@Before(value = "execution( void fr.urssaf.image.sae.dfce.admin.services.base.BaseAdministrationService.createBase(..)) && args(dataBaseModel,xmlDataService)")
	public final void createBase(
			final DataBaseModel dataBaseModel,final XmlDataService xmlDataService) {
		Validate.notNull(
				dataBaseModel,
				MessageHandler.getMessage(
						"database.model.parameters.required", "database.impact",
						"database.action"));
		Validate.notNull(
				dataBaseModel.getBase(),
				MessageHandler.getMessage(
						"database.parameters.required", "database.impact",
						"database.action"));
		Validate.notNull(
				xmlDataService,
				MessageHandler.getMessage(
						"database.parameters.required", "database.impact",
						"database.action"));
		
	}
	/**
	 * Valide le paramètre d'entrée de la méthode deleteBase 
	 * 
	 * 
	 * @param dataBaseModel
	 *            : Le model de base de donnée
	 */
	@Before(value = "execution( void fr.urssaf.image.sae.dfce.admin.services.base.BaseAdministrationService.deleteBase(..)) && args(dataBaseModel)")
	public final void deleteBase(
			final DataBaseModel dataBaseModel){
		Validate.notNull(
				dataBaseModel,
				MessageHandler.getMessage(
						"database.model.parameters.required", "database.impact",
						"database.action"));
		Validate.notNull(
				dataBaseModel.getBase(),
				MessageHandler.getMessage(
						"database.parameters.required", "database.impact",
						"database.action"));
		
	}
}
