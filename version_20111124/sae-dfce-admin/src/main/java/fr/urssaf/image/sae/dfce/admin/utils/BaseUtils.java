package fr.urssaf.image.sae.dfce.admin.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.ServiceProvider;
import fr.urssaf.image.sae.dfce.admin.model.ConnectionParameter;
import fr.urssaf.image.sae.dfce.admin.model.DataBaseModel;
import fr.urssaf.image.sae.dfce.admin.model.SaeCategory;

/**
 * Contient des services de gestion de bean
 * 
 * @author akenore
 * 
 */
public final class BaseUtils {
	/** le répertoire de base */
	private static final String BASE_DIR = "src/main/resources/";

	/** le fichier xml de la base */
	public static final File BASE_XML_FILE = new File(BASE_DIR + "saeBase/"
			+ "saeBase.xml");

	/** le fichier xml des durée de conservation */
	public static final File CYCLE_XML_FILE = new File(BASE_DIR
			+ "LifeCycleRule/" + "lifeCycleRule.xml");

	/**
	 * @param base
	 *            : La base DFCE.
	 * @param dataBaseModel
	 *            : Le modèle de base de donnée.
	 */
	public static void initBaseProperties(final Base base,
			final DataBaseModel dataBaseModel) {
		// on définit les caractéristiques de la base
		base.setDescription(dataBaseModel.getBase().getBaseDescription());
	}

	/**
	 * 
	 * @param dataBaseModel
	 *            : Le model de base
	 * @param serviceProvider
	 *            : Le service provider.
	 * @return une liste de baseCategories
	 */
	@SuppressWarnings({ "PMD.AvoidUsingShortType",
			"PMD.DataflowAnomalyAnalysis" })
	public static List<BaseCategory> initBaseCategories(
			final DataBaseModel dataBaseModel,
			final ServiceProvider serviceProvider) {
		final List<BaseCategory> baseCategories = new ArrayList<BaseCategory>();
		final ToolkitFactory toolkit = ToolkitFactory.getInstance();
		for (SaeCategory category : Utils.nullSafeIterable(dataBaseModel
				.getDataBase().getSaeCategories().getCategories())) {
			final Category categoryDfce = serviceProvider
					.getStorageAdministrationService().findOrCreateCategory(
							category.getName(), category.categoryDataType());
			final BaseCategory baseCategory = toolkit.createBaseCategory(
					categoryDfce, category.isIndex());
			baseCategory.setEnableDictionary(category.isEnableDictionary());
			baseCategory.setMaximumValues((short) category.getMaximumValues());
			baseCategory.setMinimumValues((short) category.getMinimumValues());
			baseCategory.setSingle(category.isSingle());
			baseCategories.add(baseCategory);
		}
		return baseCategories;
	}

	/**
	 * Contrôle la cohérence entre les catégories et les index composites
	 * 
	 * @param serviceProvider
	 *            : Le service provider.
	 * @param dataBaseModel
	 *            : Le model de base
	 * @return True si tous les index existent bien en temps que catégorie.
	 *         sinon false
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public static List<List<Category>> buildIndexComposite(
			final DataBaseModel dataBaseModel,
			final ServiceProvider serviceProvider) {
		final List<List<Category>> indexComposites = new ArrayList<List<Category>>();
		for (String indexComp : Utils.nullSafeIterable(dataBaseModel
				.getIndexComposites())) {
			final List<Category> indexComposite = new ArrayList<Category>();
			final List<String> index = Arrays.asList(indexComp.split(","));
			if (index != null && index.size() > 1) {
				boolean find = true;
				for (String indexC : Utils.nullSafeIterable(index)) {
					find &= indexCompositeFinder(indexC, dataBaseModel);
				}
				if (find) {
					for (String indexC : Utils.nullSafeIterable(index)) {
						final Category category = serviceProvider
								.getStorageAdministrationService().getCategory(
										indexC);
						indexComposite.add(category);
					}
				}
			}
			indexComposites.add(indexComposite);
		}
		return indexComposites;
	}

	/**
	 * 
	 * @param index
	 *            : l'indexComposite
	 * @param dataBaseModel
	 *            : Le model de base
	 * @return True si l'index définie par l'utilisateur existe dans les
	 *         catégories
	 */
	@SuppressWarnings("DataflowAnomalyAnalysis")
	private static boolean indexCompositeFinder(final String index,
			final DataBaseModel dataBaseModel) {
		boolean find = false;
		for (SaeCategory category : Utils.nullSafeIterable(dataBaseModel
				.getDataBase().getSaeCategories().getCategories())) {
			if (category.getName().contains(index)) {
				find = true;
				break;
			}
		}
		return find;
	}

	/**
	 * Cette classe ne doit pas avoir de constructeur
	 */
	private BaseUtils() {
		assert false;
	}

	/**
	 * Crée les indexComposites d'une base
	 * 
	 * @param serviceProvider
	 *            : Le service provider.
	 * @param dataBaseModel
	 *            : Le model de base
	 */
	@SuppressWarnings({ "PMD.AvoidUsingShortType",
			"PMD.DataflowAnomalyAnalysis",
			"PMD.AvoidInstantiatingObjectsInLoops" })
	public static void createIndexComposite(final DataBaseModel dataBaseModel,
			final ServiceProvider serviceProvider) {
		final List<List<Category>> indexComposites = BaseUtils
				.buildIndexComposite(dataBaseModel, serviceProvider);
		for (List<Category> indexComposite : Utils
				.nullSafeIterable(indexComposites)) {
			final Category[] categories = new Category[indexComposite.size()];
			int position = 0;
			for (Category category : indexComposite) {
				categories[position] = category;
				position++;
			}
			if (categories.length > 0) {
				serviceProvider.getStorageAdministrationService()
						.findOrCreateCompositeIndex(categories);
			}
		}
	}

	/**
	 * @param cnxParameter
	 *            : Les paramètres de connexion.
	 * @return L'url de connection
	 * @throws MalformedURLException
	 *             : L'exception lorsque la construction de l'url ne s'est pas
	 *             bien construite
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static String buildUrlForConnection(
			final ConnectionParameter cnxParameter)
			throws MalformedURLException {

		URL urlConnection = new URL("http", cnxParameter.getHost()
				.getHostName(), cnxParameter.getHost().getHostPort(),
				cnxParameter.getHost().getContextRoot());
		if (cnxParameter.getHost().isSecure()) {
			urlConnection = new URL("https", cnxParameter.getHost()
					.getHostName(), cnxParameter.getHost().getHostPort(),
					cnxParameter.getHost().getContextRoot());
		}
		return urlConnection.toString();
	}

}
