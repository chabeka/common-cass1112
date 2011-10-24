package fr.urssaf.image.sae.storage.dfce.model;

import com.docubase.dfce.core.utils.FieldName;

/**
 * Énumération contenant la listes des couples (code long ,code court) des
 * métadonnées gérées par DFCE.
 * 
 * 
 * @author akenore
 * 
 */
public enum StorageTechnicalMetadatas {
	// Titre du document
	TITRE("Titre", FieldName.SM_TITLE.toString()),
	// Date de création du document
	DATE_CREATION("DateCreation", FieldName.SM_CREATION_DATE.toString()),
	// Le type du document c'est à dire le code RND
	TYPE("CodeRND", FieldName.SM_DOCUMENT_TYPE.toString()),
	// Durée de conservation
	DUREE_CONSERVATION("DureeConservation","dco"),
	// Date de début de conservation 
	DATE_DEBUT_CONSERVATION("DateDebutConservation",
			FieldName.SM_LIFE_CYCLE_REFERENCE_DATE.toString()),
	// Gel du document
	GEL("Gel", "gel"),
	// TracabilitePostArchivage
	TRACABILITE_POST_ARCHIVAGE("TracabilitePostArchivage", "toa"),
	// Le hash
	HASH("Hash", FieldName.SM_DIGEST.toString()),
	// Le type hash
	TYPE_HASH("TypeHash", FieldName.SM_DIGEST_ALGORITHM.toString()),
	// NomFichier
	NOM_FICHIER("NomFichier", "nfi"),
	// TailleFichier
	TAILLE_FICHIER("TailleFichier", FieldName.SM_SIZE.toString()),
	// L'extension du fichier.
	EXTENSION_FICHIER("ExtensionFichierDFCE", FieldName.SM_EXTENSION
			.toString()),
	// ObjectType
	DOCUMENT_VIRTUEL("DocumentVirtuel", FieldName.SM_VIRTUAL.toString()),
	// startPage
	START_PAGE("StartPage", FieldName.SM_START_PAGE.toString()),
	// endPage
	END_PAGE("EndPage", FieldName.SM_END_PAGE.toString()),
	// DateArchivage
	DATE_ARCHIVE("DateArchivage", FieldName.SM_ARCHIVAGE_DATE.toString()),
	// version number
	VERSION_NUMBER("VersionNumber", FieldName.SM_VERSION.toString()),
	// DateModification
	DATE_MODIFICATION("DateModification", FieldName.SM_MODIFICATION_DATE
			.toString()),
	// Pas de valeur
	NOVALUE("", "");
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
