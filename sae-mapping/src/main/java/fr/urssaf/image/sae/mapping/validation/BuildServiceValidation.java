package fr.urssaf.image.sae.mapping.validation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.mapping.messages.MappingMessageHandler;

/**
 * Fournit des méthodes de validation des arguments des services de l'interface
 * {@link fr.urssaf.image.sae.building.services.BuildService BuildService}
 * 
 * @author akenore
 * 
 */
@Aspect
public class BuildServiceValidation {
	/**
	 * Valide l'argument de la méthode
	 * {@link fr.urssaf.image.sae.building.services.BuildService#buildStorageLuceneCriteria(String, int, List)
	 * buildSAELuceneCriteria}.<br>
	 * 
	 * @param luceneQuery
	 *            : La requête lucene contenant les codes courts des
	 *            métadonnées.
	 * @param limit
	 *            : Le nombre d'occurrence à retourner.
	 * @param metadatas
	 *            : La liste des objets de type {@link SAEMetadata)(chaque objet
	 *            contient un code court et code long).
	 */
	@Before(value = "execution(fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria  fr.urssaf.image.sae.building.services.BuildService.buildStorageLuceneCriteria(..)) && args(luceneQuery,limit,metadatas)")
	public final void buildStorageLuceneCriteriaValidation(
			final String luceneQuery, final int limit,
			final List<SAEMetadata> metadatas) {
		Validate.notNull(luceneQuery,
				MappingMessageHandler.getMessage("build.lucene.required"));
		if (limit <= 0) {
			throw new IllegalArgumentException(
					MappingMessageHandler.getMessage("build.limit.required"));
		}
		Validate.notNull(metadatas,
				MappingMessageHandler.getMessage("build.metadata.required"));
	}

	/**
	 * Valide l'argument de la méthode
	 * {@link fr.urssaf.image.sae.building.services.BuildService#buildStorageUuidCriteria(UUID, List)
	 * buildSAEUuidCriteria}.<br>
	 * 
	 * @param uuid
	 *            : identifiant unique du document UUID.
	 * 
	 * @param metadatas
	 *            : Les métadonnées non typés du document contenant le code
	 *            long.
	 */
	@Before(value = "execution(fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria  fr.urssaf.image.sae.building.services.BuildService.buildStorageUuidCriteria(..)) && args(uuid,metadatas)")
	public final void buildStorageUuidCriteriaValidation(final UUID uuid,
			final List<SAEMetadata> metadatas) {
		Validate.notNull(uuid, MappingMessageHandler.getMessage("build.uuid.required"));
		Validate.notNull(metadatas, MappingMessageHandler.getMessage("build.metadata.required"));
	}

	/**
	 * Valide l'argument de la méthode
	 * {@link fr.urssaf.image.sae.building.services.BuildService#buildUntypedDocument(byte[], Map)
	 * buildUntypedDocument}.<br>
	 * 
	 * @param content
	 *            : Le contenu du document.
	 * 
	 * @param metadatas
	 *            : Les métadonnées non typés du document contenant le code
	 *            long.
	 */
	@Before(value = "execution(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument  fr.urssaf.image.sae.building.services.BuildService.buildUntypedDocument(..)) && args(content,metadatas)")
	public final void buildUntypedDocumentValidation(final byte[] content,
			final Map<String, String> metadatas) {
		Validate.notNull(content, MappingMessageHandler.getMessage("build.content.required"));
		Validate.notNull(metadatas,MappingMessageHandler.getMessage("build.metadata.required"));
	}
}
