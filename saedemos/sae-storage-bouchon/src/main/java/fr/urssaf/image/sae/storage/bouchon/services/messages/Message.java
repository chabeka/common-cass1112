package fr.urssaf.image.sae.storage.bouchon.services.messages;

/**
 * {@link Message} est utilisé pour émettre des informations.
 * 
 * @author akenore
 * 
 */
public enum Message {
	
	/**
	 * A utiliser quand le document est null
	 */
	INSERTION_STORAGEDOCUMENT_NOT_NULL(
			"Erreur :  Le document que vous chercher à insérer est null",
			Impact.INSERTION_REQ_HS, Action.FILL_INSERTION_STORAGEDOCUMENT),

	/**
	 * A utiliser quand le fichier à charger est null
	 */
	DATA_FILE_LOADER(
			"Erreur :  Le fichier que vous chercher à charger est null",
			Impact.DATA_LOAD_HS, Action.DATA_FILE),
	/**
	 * A utiliser quand il n'ya pas de données pour l'insertion
	 */
	INSERTION_DATA("Erreur :  Pas de données pour l'insertion",
			Impact.DATA_LOAD_HS, Action.DATA_FILE),

	/**
	 * A utiliser quand il n'ya pas de données pour l'insertion en masse
	 */
	BULK_INSERTION_DATA("Erreur :  Pas de données pour l'insertion en masse",
			Impact.DATA_LOAD_HS, Action.DATA_FILE),

	/**
	 * A utiliser quand il n'ya pas de données pour la recherche par requête
	 * lucene
	 */
	SEARCH_BY_LUCENE_DATA(
			"Erreur :  Pas de données pour la recherche par requête lucene",
			Impact.DATA_LOAD_HS, Action.DATA_FILE),

	/**
	 * A utiliser quand il n'ya pas de données pour la recherche par requête
	 * UUID
	 */
	SEARCH_BY_UUID_DATA(
			"Erreur :  Pas de données pour la recherche par requête UUID",
			Impact.DATA_LOAD_HS, Action.DATA_FILE),
	/**
	 * A utiliser quand il n'ya pas de données pour la récupération des
	 * métadonnées et du contenu du document UUID
	 */
	RETRIEVE_BY_UUID_DATA(
			"Erreur :  Pas de données pour la récupération des métadonnées et du contenu du document",
			Impact.DATA_LOAD_HS, Action.DATA_FILE),

	/**
	 * A utiliser quand il n'ya pas de données pour la récupération des
	 * métadonnées.
	 */
	RETRIEVE_METADATA_BY_UUID_DATA(
			"Erreur :  Pas de données pour la récupération des métadonnées du document",
			Impact.DATA_LOAD_HS, Action.DATA_FILE),
	/**
	 * A utiliser quand il n'ya pas de données pour et du contenu du document
	 * UUID
	 */
	RETRIEVE_CONTENT_BY_UUID_DATA(
			"Erreur :  Pas de données pour la récupération du contenu du document",
			Impact.DATA_LOAD_HS, Action.DATA_FILE),
	/**
	 * A utiliser quand les composants du document(métadonnées et contenu du
	 * document) sont tous null
	 */
	INSERTION_STORAGEDOCUMENT_COMPONENTS_NOT_NULL(
			"Erreur : Les composants du document que vous chercher à insérer sont tous null",
			Impact.INSERTION_REQ_HS,
			Action.FILL_INSERTION_STORAGEDOCUMENT_COMPONENTS),
	/**
	 * A utiliser quand une exception est léver lors de l'insertion d'un
	 * document
	 * 
	 */
	INSERTION_STORAGEDOCUMENT_FAILED(
			"Erreur : L'insertion ne s'est pas bien passée",
			Impact.INSERTION_REQ_HS, Action.INSERTION_STORAGEDOCUMENT_FAILED),
	/**
	 * A utiliser quand la liste des document est nulle
	 */
	BULK_INSERTION_STORAGEDOCUMENTS_NOT_NULL(
			"Erreur :  La liste des documents que vous chercher à insérer est Null",
			Impact.BULK_INSERTION_REQ_HS,
			Action.FILL_BULK_INSERTION_STORAGEDOCUMENTS),
	/**
	 * A utiliser quand les critères lucène sont nuls
	 */
	SEARCHING_BY_LUCENE_CRITERIA_NOT_NULL(
			"Erreur : les critères de recherche lucène sont null",
			Impact.SEARCH_REQ_HS, Action.SEARCHING_BY_UUID_CRITERIA_NOT_NULL),
	/**
	 * A utiliser quand les critères UUID sont nuls
	 */
	SEARCHING_BY_UUID_NOT_NULL(
			"Erreur : les critères de recherche par UUID sont nuls",
			Impact.SEARCH_REQ_HS, Action.SEARCHING_BY_UUID_CRITERIA_NOT_NULL),
	/**
	 * A utiliser quand les critères UUID sont nuls
	 */
	RETRIEVE_BY_UUID_NOT_NULL(
			"Erreur : les critères de récupération d'un document par uuid sont null",
			Impact.RETRIEVE_REQ_HS, Action.RETRIEVE_BY_UUID),
	/**
	 * A utiliser quand une exception est léver lors de la recherche
	 * 
	 */
	SEARCHING_FAILED("Erreur : La recherche n'a pas aboutie",
			Impact.SEARCH_REQ_HS, Action.SEARCHING_FAILED),

	/**
	 * A utiliser quand une exception est léver lors de la recherche
	 * 
	 */
	RETRIEVE_FAILED("Erreur : La récuperation du document n'a pas aboutie",
			Impact.RETRIEVE_REQ_HS, Action.RETRIEVE_BY_UUID_FAILED);

	/** Buffer */
	private StringBuilder stringBuilde = new StringBuilder();

	/**
	 * Constructeur
	 * 
	 * @param label
	 *            : Le libellé
	 * @param impact
	 *            : L'impact
	 * @param action
	 *            : L'action
	 */
	Message(final String label, final Impact impact, final Action action) {
		this.setLabel(label);
		this.setImpact(impact);
		this.setAction(action);
		stringBuilde.setLength(0);
		stringBuilde.append(label);
		stringBuilde.append(" | ").append(impact.getLongDescription()).append(" | ")
				.append(action.getLongDescription());
		textFormated = stringBuilde.toString();
	}
	
	/**
	 * 
	 * @return Le message
	 */
	public String getMessage() {
		return textFormated;
	}

	/**
	 * Initialise le libellé
	 * 
	 * @param label
	 *            : Le libellé
	 * 
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * Retourne le libellé
	 * 
	 * @return Le libellé
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Initialise l'impact
	 * 
	 * @param impact
	 *            : L'impact
	 * 
	 */
	public void setImpact(final Impact impact) {
		this.impact = impact;
	}

	/**
	 * Retourne l'impact
	 * 
	 * @return L'impact
	 */
	public Impact getImpact() {
		return impact;
	}

	/**
	 * Initialise l'action
	 * 
	 * @param action
	 *            : L'action
	 * 
	 */
	public void setAction(final Action action) {
		this.action = action;
	}

	/**
	 * @return L'action
	 */
	public Action getAction() {
		return action;
	}

	private String label;
	private Impact impact;
	private Action action;
	private String textFormated;
}
