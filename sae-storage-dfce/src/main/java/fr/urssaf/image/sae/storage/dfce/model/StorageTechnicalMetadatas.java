package fr.urssaf.image.sae.storage.dfce.model;

import com.docubase.dfce.commons.indexation.SystemFieldName;

/**
 * Énumération contenant la listes des couples (code long ,code court) des
 * métadonnées gérées par DFCE.
 */
public enum StorageTechnicalMetadatas {
	// Titre du document
	TITRE("Titre", SystemFieldName.SM_TITLE.toString()),
	// Date de création du document
	DATE_CREATION("DateCreation", SystemFieldName.SM_CREATION_DATE.toString()),
	// Le type du document c'est à dire le code RND
	TYPE("CodeRND", SystemFieldName.SM_DOCUMENT_TYPE.toString()),
	// Durée de conservation
	DUREE_CONSERVATION("DureeConservation","dco"),
	// Date de début de conservation 
	DATE_DEBUT_CONSERVATION("DateDebutConservation",
	      SystemFieldName.SM_LIFE_CYCLE_REFERENCE_DATE.toString()),
	// Gel du document
	GEL("Gel", "gel"),
	// DocFormatOrigine
   DOC_FORMAT_ORIGINE("DocFormatOrigine", "dfo"),
	// TracabilitePostArchivage
	TRACABILITE_POST_ARCHIVAGE("TracabilitePostArchivage", "toa"),
	// Le hash
	HASH("Hash", SystemFieldName.SM_DIGEST.toString()),
	// Le type hash
	TYPE_HASH("TypeHash", SystemFieldName.SM_DIGEST_ALGORITHM.toString()),
	// NomFichier
	NOM_FICHIER("NomFichier", "nfi"),
	// TailleFichier
	TAILLE_FICHIER("TailleFichier", SystemFieldName.SM_SIZE.toString()),
	// L'extension du fichier.
	EXTENSION_FICHIER("ExtensionFichierDFCE", SystemFieldName.SM_EXTENSION.toString()),
	// ObjectType
	DOCUMENT_VIRTUEL("DocumentVirtuel", SystemFieldName.SM_VIRTUAL.toString()),
	// startPage
	START_PAGE("StartPage", SystemFieldName.SM_START_PAGE.toString()),
	// endPage
	END_PAGE("EndPage", SystemFieldName.SM_END_PAGE.toString()),
	// DateArchivage
	DATE_ARCHIVE("DateArchivage", SystemFieldName.SM_ARCHIVAGE_DATE.toString()),
	// DateArchivageGNT
	DATE_ARCHIVE_GNT("DateArchivageGNT", "dag"),
	// TracabilitePreArchivage
	TRACABILITE_PRE_ARCHIVAGE("TracabilitePreArchivage", "tpa"),
	// version number
	VERSION_NUMBER("VersionNumber", SystemFieldName.SM_VERSION.toString()),
	// DateModification
	DATE_MODIFICATION("DateModification", SystemFieldName.SM_MODIFICATION_DATE
			.toString()),
	// Pas de valeur
	NOVALUE("", ""),
	// Note sur le document
	NOTE("Note","not"),
	// Identifiant unique du document
   IDGED("IdGed", SystemFieldName.SM_UUID.toString());
	
	// Le code court de la métadonnée.
	private String shortCode;

	/**
	 * 
	 * @param shortCode
	 *            . Le code court
	 */
	StorageTechnicalMetadatas(final String longCode, final String shortCode) {
		this.shortCode = shortCode;
		this.longCode = longCode;
	}

	/**
	 * @param shortCode
	 *            : Le code court de la métadonnée.
	 */
	public final void setShortCode(final String shortCode) {
		this.shortCode = shortCode;
	}

	/**
	 * @return Le code court de la métadonnée.
	 */
	public String getShortCode() {
		return shortCode;
	}

	// Le code long de la métadonnée.
	private String longCode;

	/**
	 * @param longCode
	 *            : Le code long de la métadonnée.
	 */
	public void setLongCode(final String longCode) {
		this.longCode = longCode;
	}

	/**
	 * @return : Le code long de la métadonnée.
	 */
	public String getLongCode() {
		return longCode;
	}

}
