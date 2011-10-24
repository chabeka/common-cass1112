package fr.urssaf.image.sae.dfce.admin.services.base.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.reference.LifeCycleLengthUnit;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.dfce.admin.messages.MessageHandler;
import fr.urssaf.image.sae.dfce.admin.model.ConnectionParameter;
import fr.urssaf.image.sae.dfce.admin.model.DataBaseModel;
import fr.urssaf.image.sae.dfce.admin.model.LifeCycleRule;
import fr.urssaf.image.sae.dfce.admin.model.Rule;
import fr.urssaf.image.sae.dfce.admin.services.AbstractService;
import fr.urssaf.image.sae.dfce.admin.services.base.BaseAdministrationService;
import fr.urssaf.image.sae.dfce.admin.services.exceptions.BaseAdministrationServiceEx;
import fr.urssaf.image.sae.dfce.admin.services.exceptions.ConnectionServiceEx;
import fr.urssaf.image.sae.dfce.admin.services.xml.XmlDataService;
import fr.urssaf.image.sae.dfce.admin.utils.BaseUtils;
import fr.urssaf.image.sae.dfce.admin.utils.Utils;

/**
 * Implémente l'interface : {@link BaseAdministrationService}
 * <ul>
 * <li>
 * createBase : Service de création de la base.</li>
 * <li>
 * deleteBase : Service de suppression de la base.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
@Service
@Qualifier("baseAdministrationService")
public class BaseAdministrationServiceImpl extends AbstractService implements
		BaseAdministrationService {
	private static final Logger LOGGER = Logger
			.getLogger("BaseAdministrationService");

	@SuppressWarnings("PMD.LongVariable")
	@Autowired
	private ConnectionParameter connectionParameter;

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @throws FileNotFoundException
	 *             Exception levée lorsque le fichier n'existe pas.
	 * 
	 * 
	 */
	@SuppressWarnings({ "PMD.LongVariable", "DataflowAnomalyAnalysis" })
	public final void createBase(final DataBaseModel dataBaseModel,
			final XmlDataService xmlDataService)
			throws BaseAdministrationServiceEx, FileNotFoundException {
		try {
			final ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();
			LOGGER.info(MessageHandler.getMessage("database.initialization",
					dataBaseModel.toString()));
			// Instantiation d'une base
			Base base = getServiceProvider().getBaseAdministrationService()
					.getBase(dataBaseModel.getBase().getBaseId());
			if (base == null) {
				base = toolkitFactory.createBase(dataBaseModel.getBase()
						.getBaseId());
			}
			LOGGER.info(MessageHandler
					.getMessage("database.initialization.technical.metadatas"));
			// Définition des propriétes de la base
			BaseUtils.initBaseProperties(base, dataBaseModel);
			LOGGER.info(MessageHandler
					.getMessage("database.initialization.base.categories"));
			// Création des baseCatégories
			final List<BaseCategory> baseCategories = BaseUtils
					.initBaseCategories(dataBaseModel, getServiceProvider());
			LOGGER.info(MessageHandler
					.getMessage("database.base.categories.creation"));
			for (BaseCategory baseCategory : Utils
					.nullSafeIterable(baseCategories)) {
				base.addBaseCategory(baseCategory);
			}
			LOGGER.info(MessageHandler.getMessage("database.creation",
					dataBaseModel.getBase().getBaseId()));
			// Création de la base
			getServiceProvider().getBaseAdministrationService()
					.createBase(base);
			LOGGER.info(MessageHandler
					.getMessage("database.index.composite.creation"));
			// Création des indexes composites
			BaseUtils.createIndexComposite(dataBaseModel, getServiceProvider());
			// on démarre la base
			getServiceProvider().getBaseAdministrationService().startBase(base);
			// Alimentation de la colonne famille LifeCycleRule
			createNewLifeCycleRule(xmlDataService);

		} catch (ObjectAlreadyExistsException objAlreadyExistsEx) {
			throw new BaseAdministrationServiceEx(
					MessageHandler
							.getMessage(
									MessageHandler
											.getMessage(
													"database.already.exists",
													dataBaseModel.getBase()
															.getBaseId()),
									MessageHandler
											.getMessage("database.already.exist.impact"),
									MessageHandler
											.getMessage("database.already.exist.action")),
					objAlreadyExistsEx);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public final void deleteBase(final DataBaseModel dataBaseModel) {
		LOGGER.info(MessageHandler.getMessage("database.retrieve"));
		// Instantiation d'une base
		final Base base = getServiceProvider().getBaseAdministrationService()
				.getBase(dataBaseModel.getBase().getBaseId());
		if (base != null) {
			LOGGER.info(MessageHandler.getMessage("database.stop"));
			getServiceProvider().getBaseAdministrationService().stopBase(base);

			getServiceProvider().getBaseAdministrationService()
					.deleteBase(base);
			LOGGER.info(MessageHandler.getMessage("database.deleted",
					dataBaseModel.getBase().getBaseId()));
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public final DataBaseModel getDataBaseModel(final File xmlBaseModel,
			final XmlDataService xmlDataService) throws FileNotFoundException {

		return xmlDataService.baseModelReader(xmlBaseModel);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void openConnection() throws ConnectionServiceEx {
		setServiceProvider(ServiceProvider.newServiceProvider());
		try {
			getServiceProvider().connect(
					connectionParameter.getUser().getLogin(),
					connectionParameter.getUser().getPassword(),
					BaseUtils.buildUrlForConnection(connectionParameter));

		} catch (MalformedURLException malURLException) {
			throw new ConnectionServiceEx(MessageHandler.getMessage(
					"url.connection.malformed", "connection.impact",
					"connection.action"), malURLException);

		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final void closeConnection() {
		getServiceProvider().disconnect();
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

	/**
	 * Service permettant de créer le cycle de vie d'un document.
	 * 
	 * @param xmlDataService
	 *            : Le service de désérialisation des flux xml
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 * @throws ConnectionServiceEx
	 *             Lorsqu'un problème survient lors de la connexion.
	 */
	private void createNewLifeCycleRule(final XmlDataService xmlDataService)
			throws FileNotFoundException {
		LOGGER.info(MessageHandler.getMessage("lifeCycleRule.initialization"));
		final LifeCycleRule lifeCycles = xmlDataService
				.lifeCycleRuleReader(BaseUtils.CYCLE_XML_FILE);
		LOGGER.info(MessageHandler.getMessage("lifeCycleRule.create"));
		for (Rule documentRule : lifeCycles.getRules()) {
			try {
				LOGGER.info(MessageHandler.getMessage("lifeCycleRule.document",
						documentRule.getRndCode()));
				getServiceProvider().getStorageAdministrationService()
						.createNewLifeCycleRule(documentRule.getRndCode(),
								documentRule.getStorageDuration(),
								LifeCycleLengthUnit.DAY);
			} catch (ObjectAlreadyExistsException objectExist) {
				LOGGER.info(MessageHandler.getMessage(
						"lifeCycleRule.already.exist",
						documentRule.getRndCode()));
			}
		}
	}
}
