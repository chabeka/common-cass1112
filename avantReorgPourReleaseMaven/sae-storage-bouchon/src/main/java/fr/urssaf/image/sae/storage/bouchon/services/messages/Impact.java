package fr.urssaf.image.sae.storage.bouchon.services.messages;

/**
 * {@link Impact} énumère les différentes possibilités d'impact de la cause d'un
 * message.
 * 
 * @author akenore
 */
public enum Impact {

	/**
	 * A utiliser quand l'insertion du document n'est pas possible
	 * 
	 */
	INSERTION_REQ_HS("INSERTION_REQ_KO",
			"Ce document ne sera pas insérer dans la base"),
	/**
	 * A utiliser quand la restitution de donnée n'est pas possible
	 * 
	 */
	DATA_RES_HS("DATA_RES_HS", "Pas de données à restituer "),
	
	/**
	 * A utiliser quand la chargement du fichier des données n'est pas possible
	 * 
	 */
	DATA_LOAD_HS("DATA_LOAD_HS", "Pas de données chargées "),
	
	/**
	 * A utiliser quand l'insertion d'une liste de documents n'est pas possible.
	 * 
	 */
	BULK_INSERTION_REQ_HS("BULK_INSERTION_REQ_HS",
			"Cette liste de documents ne sera pas insérer dans la base"),
	/**
	 * A utiliser quand la requête de récupération n'est pas possible
	 * 
	 */
	RETRIEVE_REQ_HS("RETRIEVE_REQ_KO", "Aucune récupération ne sera effectuée"),
	/**
	 * A utiliser quand auncune recherche ne sera effectuée
	 * 
	 */
	SEARCH_REQ_HS("SEARCH_REQ_KO", "Aucune recherche ne sera effectuée");
	/**
	 * Constructeur
	 * 
	 * @param shortDescription
	 *            : La description courte de l'Impact
	 * @param longDescription
	 *            : La description longue de l'Impact
	 */
	Impact(final String shortDescription, final String longDescription) {
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	/**
	 * Initialise la description longue de l'Impact
	 * 
	 * @param longDescription
	 *            : La description longue de l'Impact
	 */
	public void setLongDescription(final String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * Retourne la description longue de l'Impact
	 * 
	 * @return La description longue de l'Impact
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * Initialise la description courte de l'Impact
	 * 
	 * @param shortDescription
	 *            : La description courte de l'Impact
	 */
	public void setShortDescription(final String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * Retourne la description courte de l'Impact
	 * 
	 * @return La description courte de l'Impact
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/** Description courte de l'impact */
	private String shortDescription;
	/** Description longue de l'impact */
	private String longDescription;
}
