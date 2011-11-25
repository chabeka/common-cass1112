package fr.urssaf.image.sae.storage.bouchon.services.messages;

/**
 * {@link Action} énumère les différentes possibilités d'action à faire
 * entreprendre à la suite d'un message prévues.
 * 
 * @author akenore
 * 
 */
public enum Action {

	/**
	 * Permet d'indiquer qu'elle action réalisée lorsque le chargement du fichier contenant les données
	 * ne se déroule pas bien
	 */
	DATA_FILE("DATA_FILE",
			"Vérifier que le fichier est bien présent et qu'il contient des données"),
	
	/**
	 * Permet d'indiquer qu'elle action réalisée lorsque l'utilisateur souhaite
	 * insérer un document non valorisé
	 */
	FILL_INSERTION_STORAGEDOCUMENT("FILL_STORAGEDOCUMENT",
			"Veuillez renseigner une valeur non nulle au document"),
	/**
	 * Permet d'indiquer qu'elle action réalisée lorsque l'utilisateur souhaite
	 * insérer un document dont les composants ne sont pas valorisés
	 */
	FILL_INSERTION_STORAGEDOCUMENT_COMPONENTS(
			"FILL_STORAGEDOCUMENT_COMPONENT",
			"Veuillez renseigner une valeur non nulle au moins à un des composantes du document"),
	/**
	 * Permet d'indiquer qu'elle action réalisée lorsque l'utilisateur souhaite
	 * insérer et que celle-ci n'aboutie pas suite à plusieurs raisons.
	 */
	INSERTION_STORAGEDOCUMENT_FAILED("FILL_STORAGEDOCUMENT_FAILED",
			"Vérifier les données du document sinon la connection à la base"),

	/**
	 * Permet d'indiquer qu'elle action à réaliser lorsque l'utilisateur
	 * souhaite insérer une liste de documents non valorisée
	 */
	FILL_BULK_INSERTION_STORAGEDOCUMENTS("BULK_INSERTION_STORAGEDOCUMENTS",
			"Veuillez renseigner une liste de document non nulle"),
	/**
	 * Permet d'indiquer qu'elle action à réaliser lorsque l'utilisateur
	 * souhaite effectuer une recherche avec des critères non valorisés.
	 */
	SEARCHING_BY_LUCENE_CRITERIA_NOT_NULL(
			"SEARCHING_BY_LUCENE_CRITERIA_NOT_NULL",
			"Veuillez renseigner les critères de recherche lucène"),
	/**
	 * Permet d'indiquer qu'elle action à réaliser lorsque l'utilisateur
	 * souhaite effectuer une recherche et que celle-ci n'aboutie pas suite à x
	 * raisons
	 */
	SEARCHING_FAILED("SEARCHING_FAILED",
			"Veuillez vérifier la connection à la base"),
	/**
	 * Permet d'indiquer qu'elle action à réaliser lorsque l'utilisateur
	 * souhaite effectuer une recherche par UUID avec des critères non valorisés
	 */
	SEARCHING_BY_UUID_CRITERIA_NOT_NULL("SEARCHING_BY_UUID_CRITERIA_NOT_NULL",
			"Veuillez renseigner les critères de recherche "),
	/**
	 * Permet d'indiquer qu'elle action à réaliser lorsque l'utilisateur
	 * souhaite effectuer une récupération et que celle-ci n'aboutie pas suite à x
	 * raisons
	 */
	RETRIEVE_BY_UUID_FAILED("RETRIEVE_BY_UUID_FAILED",
			"Vérifier la connection à la base"),
	/**
	 * Permet d'indiquer qu'elle action à réaliser lorsque l'utilisateur
	 * souhaite effectuer une récupération de document avec un critère UUID non
	 * valorisé
	 */
	RETRIEVE_BY_UUID("RETRIEVE_BY_UUID", "Veuillez renseigner le critère UUID ");
	/**
	 * Constructeur
	 * 
	 * @param shortDescription
	 *            : La description courte de l'Action
	 * @param longDescription
	 *            : La description longue de l'Action
	 */
	Action(final String shortDescription, final String longDescription) {
		this.setShortDescription(shortDescription);
		this.setLongDescription(longDescription);
	}

	/**
	 * @param longDescription
	 *            : La description longue de l'Action
	 */
	public void setLongDescription(final String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * @return La description longue de l'Action
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * @param shortDescription
	 *            : La description courte de l'Action
	 */
	public void setShortDescription(final String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return La description courte de l'Action
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/** Description courte de l'Action */
	private String shortDescription;
	/** Description longue de l'Action */
	private String longDescription;

}
