package fr.urssaf.image.sae.dfce.admin.services.base;

import java.io.File;
import java.io.FileNotFoundException;

import fr.urssaf.image.sae.dfce.admin.model.DataBaseModel;
import fr.urssaf.image.sae.dfce.admin.services.exceptions.BaseAdministrationServiceEx;
import fr.urssaf.image.sae.dfce.admin.services.exceptions.ConnectionServiceEx;
import fr.urssaf.image.sae.dfce.admin.services.xml.XmlDataService;

/**
 * Fournit l'interface des services :
 * <ul>
 * <li>
 * createBase : Service de création de la base.</li>
 * <li>
 * deleteBase : Service de suppression de la base.</li>
 * </ul>
 * 
 * 
 * @author akenore
 * 
 */
public interface BaseAdministrationService {

	/**
	 * Ouvre la connection
	 * 
	 * @throws ConnectionServiceEx
	 *             Lorsqu'un problème survient lors de la connexion
	 */
	void openConnection() throws ConnectionServiceEx;

	/**
	 * Ferme la connection
	 */
	void closeConnection();

	/**
	 * Service de création de la base.
	 * 
	 * @param dataBaseModel
	 *            : un modèle de base de donnée
	 * @param xmlDataService
	 *            : Le service de désérialisation des flux xml
	 * @throws BaseAdministrationServiceEx
	 *             lorsqu'il y'a une problème lors de l'appel d'un service
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */
	void createBase(final DataBaseModel dataBaseModel,
			final XmlDataService xmlDataService)
			throws BaseAdministrationServiceEx, FileNotFoundException;

	/**
	 * Service de mise à jour des indexes de la base.
	 * 
	 * @param dataBaseModel
	 *            : un modèle de base de donnée
	 */
	void updateAllIndexesUsage(final DataBaseModel dataBaseModel);

	  /**
    * Service de suppression de la base.
    * 
    * @param dataBaseModel
    *            : un modèle de base de donnée
    */
   void deleteBase(final DataBaseModel dataBaseModel);

	/**
	 * 
	 * @param xmlBaseModel
	 *            : Le fichier xml contenant le mode de base
	 * @param xmlDataService
	 *            : Le service de désérialisation des flux xml
	 * @return La base de donnée désérialisée.
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */

	DataBaseModel getDataBaseModel(final File xmlBaseModel,
			final XmlDataService xmlDataService) throws FileNotFoundException;

}
