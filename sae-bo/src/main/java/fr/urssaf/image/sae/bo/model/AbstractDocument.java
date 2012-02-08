package fr.urssaf.image.sae.bo.model;

import java.util.UUID;

/**
 * Classe abstraite contenant les éléments communs des objets métiers et objets
 * conteneurs.<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>content : Le contenu d’un document.</li>
 * <li>uuid :L'identifiant unique d’un document.</li>
 * <li>filePath :Le chemin absolu du fichier.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractDocument {
	// Les attributs
	private byte[] content;
	private UUID uuid;
	private String filePath;

	/**
	 * @return Le contenu d’un document
	 */
	@SuppressWarnings("PMD.MethodReturnsInternalArray")
	public final byte[] getContent() {
		// Pas de clone pour des raisons de performance.
		return content;
	}

	/**
	 * @param fileContent
	 *            : Le contenu d’un document.
	 */
	@SuppressWarnings("PMD.ArrayIsStoredDirectly")
	public final void setContent(final byte[] fileContent) {
		// Pas de clone pour des raisons de performance.
		this.content = fileContent;
	}

	/**
	 * @return L'identifiant unique d’un document
	 */
	public final UUID getUuid() {
		return uuid;
	}

	/**
	 * @param uuidDoc
	 *            : L'identifiant unique d’un document.
	 */
	public final void setUuid(final UUID uuidDoc) {
		this.uuid = uuidDoc;
	}

	/**
	 * Construit un objet de type {@link AbstractDocument}.
	 */
	public AbstractDocument() {
		// Ici on fait rien
	}

	/**
	 * Construit un objet de type {@link AbstractDocument}.
	 * 
	 * @param fileContent
	 *            : Le contenu d’un document.
	 */
	@SuppressWarnings("PMD.ArrayIsStoredDirectly")
	public AbstractDocument(final byte[] fileContent) {
		// Pas de clone pour des raisons de performance.
		this.content = fileContent;

	}

	/**
	 * Construit un objet de type {@link AbstractDocument}.
	 * 
	 * @param fileContent
	 *            : Le contenu d’un document.
	 * @param filePath
	 *            : Le chemin absolu du fichier.
	 */
	@SuppressWarnings("PMD.ArrayIsStoredDirectly")
	public AbstractDocument(final byte[] fileContent, final String filePath) {
		// Pas de clone pour des raisons de performance.
		this.content = fileContent;
		this.filePath = filePath;

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
}
