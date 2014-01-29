package fr.urssaf.image.sae.building.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Interface qui fournit des services de construction des objets métiers.
 */
public interface BuildService {

/**
	 * Service permettant de construire un objet de type
	 * {@link LuceneCriteria} à partir d’une requête lucène.
	 * 
	 * @param luceneQuery
	 *            : La requête lucene contenant les codes courts des
	 *            métadonnées.
	 *@param limit : Le nombre d'occurrence à retourner.
	 * @param metadatas
	 *            : La liste des objets de type {@link SAEMetadata}(chaque objet contient un code court et code long).
	 * @return un objet de type {@link LuceneCriteria}
	 */
	LuceneCriteria buildStorageLuceneCriteria(final String luceneQuery,int limit,
			final List<SAEMetadata> metadatas);

/**
	 * Service permettant de construire un objet de type {@link UUIDCriteria}
	 * à partir de l'identifiant unique d'un document UUID.
	 * 
	 * @param uuid
	 *            : identifiant unique du document UUID.
	 * 
	 * @param metadatas
	 *            : La liste des objets de type {@link SAEMetadata}(chaque objet contient un code court et code long).
	 * @return un objet de type {@link UUIDCriteria}
	 */
	UUIDCriteria buildStorageUuidCriteria(final UUID uuid,
			final List<SAEMetadata> metadatas);

	/**
	 * Service permettant de construire un objet de type {@link UntypedDocument}
	 * à partir d’un tableau de byte et une liste de paire clés/valeur.
	 * 
	 * @param content
	 *            : Le contenu du document
	 * @param metadatas
	 *            : Les métadonnées non typés du document contenant le code long
	 * @return un objet de type {@link UntypedDocument}
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	UntypedDocument buildUntypedDocument(final byte[] content,
			final Map<String, String> metadatas);

}
