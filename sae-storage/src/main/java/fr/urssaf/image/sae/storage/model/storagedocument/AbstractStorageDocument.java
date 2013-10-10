package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.Date;
import java.util.List;

/**
 * Classe abstraite contenant les attributs communs des différents types de
 * documents destinés au stockage.</BR> Elle contient les attributs :
 * <ul>
 * <li>
 * metadatas : Liste des métadatas</li>
 * <li>
 * content : Le contenu du document</li>
 * <li>
 * typeDoc : type de document</li>
 * <li>
 * filePath : Le chemin du document</li>
 * <li>
 * processId : L'identifiant du traitement</li>
 * </ul>
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractStorageDocument {
	// Les attributs
	private List<StorageMetadata> metadatas;
	private byte[] content;
	private String filePath;
	private String typeDoc;
	private Date creationDate;
	private String title;
	private String processId;
	private String fileName;

	/**
	 * @return L'identifiant du traitement.
	 */
	public final String getProcessId() {
		return processId;
	}

	/**
	 * @param processId : L'identifiant du traitement.
	 */
	public final void setProcessId(final String processId) {
		this.processId = processId;
	}

	/**
	 * Retourne la liste des métadonnées.
	 * 
	 * @return Liste des métadonnées
	 */
	public final List<StorageMetadata> getMetadatas() {
		return metadatas;
	}

	/**
	 * Initialise la liste des métadonnées.
	 * 
	 * @param metadatas
	 *            : La liste des métadonnées
	 */
	public final void setMetadatas(final List<StorageMetadata> metadatas) {
		this.metadatas = metadatas;
	}

	/**
	 * Retourne le contenu du document
	 * 
	 * @return Le contenu du document
	 */
	@SuppressWarnings("PMD.MethodReturnsInternalArray")
	public final byte[] getContent() {
		return content;
	}

	/**
	 * Initialise le contenu du document
	 * 
	 * @param content
	 *            : Le contenu du document
	 */
	@SuppressWarnings("PMD.ArrayIsStoredDirectly")
	public final void setContent(final byte[] content) {
		this.content = content;
	}

	/**
	 * Retourne le chemin du fichier
	 * 
	 * @return Le chemin du fichier
	 */
	public final String getFilePath() {
		return filePath;
	}

	/**
	 * Initialise le chemin du fichier
	 * 
	 * @param filePath
	 *            : Le chemin du document
	 */
	public final void setFilePath(final String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return le type de document.
	 */
	public final String getTypeDoc() {
		return typeDoc;
	}

	/**
	 * @param typeDoc
	 *            :le type de document.
	 */
	public final void setTypeDoc(final String typeDoc) {
		this.typeDoc = typeDoc;
	}

	/**
	 * @return : date de creation.
	 */
	public final Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            : date de creation.
	 */
	public final void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return le titre du document.
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            :le titre du document.
	 */
	public final void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Construit un nouveau {@link AbstractStorageDocument }.
	 * 
	 * @param metadatas
	 *            : Les metadatas du document
	 * @param content
	 *            : Le contenu du document
	 * @param filePath
	 *            : Le chemin du fichier
	 */
	@SuppressWarnings("PMD.ArrayIsStoredDirectly")
	public AbstractStorageDocument(final List<StorageMetadata> metadatas,
			final byte[] content, final String filePath) {
		this.metadatas = metadatas;
		this.content = content;
		this.filePath = filePath;
	}

	/**
	 * Construit un nouveau {@link AbstractStorageDocument } par défaut.
	 */
	public AbstractStorageDocument() {
		// Ici on ne fait rien.
	}

   /**
    * @return le nom du fichier
    */
   public final String getFileName() {
      return fileName;
   }

   /**
    * Initialise le chemin du fichier
    * 
    * @param fileName
    *            : Le chemin du document
    */
   public final void setFileName(String fileName) {
      this.fileName = fileName;
   }

}
