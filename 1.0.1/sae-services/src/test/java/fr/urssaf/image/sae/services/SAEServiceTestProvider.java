package fr.urssaf.image.sae.services;

import java.util.List;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.model.document.Document;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Service pour fournir des méthodes communes pour les tests de l'artefact<br>
 * <br>
 * La classe peut-être injecté par {@link Autowired}
 * 
 * 
 */
@Component
public class SAEServiceTestProvider {

	private final StorageServiceProvider serviceProvider;
	@Autowired
	@Qualifier("mappingDocumentService")
	private MappingDocumentService mappingService;

	/**
	 * initialise la façade des services de sae-storage
	 * 
	 * @param serviceProvider
	 *            façade des services de sae-storage
	 * @param connection
	 *            connection à DFCE
	 */
	@Autowired
	public SAEServiceTestProvider(
			@Qualifier("storageServiceProvider") StorageServiceProvider serviceProvider,
			@Qualifier("mappingDocumentService") MappingDocumentService mappingService) {

		Assert.assertNotNull(serviceProvider);
		Assert.assertNotNull(mappingService);

		this.serviceProvider = serviceProvider;
		this.mappingService= mappingService;
	}

	/**
	 * Permet de retrouver un document dans le SAE à partir de son identifiant
	 * unique d'archivage<br>
	 * <br>
	 * Cette méthode peut s'avérer utile pour les tests unitaires simplement
	 * pour vérifier qu'un document a bien été inséré dans le SAE
	 * 
	 * @param uuid
	 *            identifiant unique du document à retrouver dans le SAE
	 * @return instance du {@link Document} correspond au paramètre
	 *         <code>uuid</code>
	 * @throws ConnectionServiceEx
	 *             une exception est levée lors de l'ouverture de la connexion
	 * @throws SearchingServiceEx
	 *             une exception est levée lors de la recherche
	 */
	public final StorageDocument searchDocument(UUID uuid)
			throws ConnectionServiceEx, SearchingServiceEx {

		try {
			UUIDCriteria criteria = new UUIDCriteria(uuid, null);
			serviceProvider.openConnexion();
			return serviceProvider.getStorageDocumentService()
					.searchStorageDocumentByUUIDCriteria(criteria);

		} finally {
			serviceProvider.closeConnexion();
		}

	}



	/**
	 * Permet de supprimer un document dans le SAE à partir de son identifiant
	 * unique d'archivage<br>
	 * <br>
	 * Cette méthode peut s'avérer utile pour les tests unitaires simplement
	 * pour supprimer un document du SAE qui vient d'être inséré et n'est plus
	 * utile
	 * 
	 * @param uuid
	 *            identifiant unique du document à supprimer SAE
	 * @throws ConnectionServiceEx
	 *             une exception est levée lors de l'ouverture de la connexion
	 * @throws DeletionServiceEx
	 *             une exception est levée lors de la suppression.
	 */
	public final void deleteDocument(UUID uuid) throws ConnectionServiceEx,
			DeletionServiceEx {

		try {
			UUIDCriteria criteria = new UUIDCriteria(uuid, null);
			serviceProvider.openConnexion();
			serviceProvider.getStorageDocumentService().deleteStorageDocument(
					criteria);

		} finally {

			serviceProvider.closeConnexion();

		}

	}

	/**
	 * 
	 * Permet d'insérer un document dans le SAE<br>
	 * <br>
	 * Cette méthode peut s'avérer utile pour les tests unitaires pour consulter
	 * ou recherche un document du SAE
	 * 
	 * @param content
	 *            contenu du document à archiver
	 * @param metadatas
	 *            liste des métadonnées
	 * @param title
	 *            titre du document
	 * @param type
	 *            type du document
	 * @param creationDate
	 *            date de création du document
	 * @return UUID du document dans le SAE
	 * @throws ConnectionServiceEx
	 *             une exception est levée lors de l'ouverture de la connexion
	 */
	public final UUID captureDocument(byte[] content,
			List<StorageMetadata> metadatas) throws ConnectionServiceEx {

		try {

			serviceProvider.openConnexion();
			StorageDocument doc = new StorageDocument(metadatas, content);

			return serviceProvider.getStorageDocumentService().insertStorageDocument(doc).getUuid();
		} catch (InsertionServiceEx e) {
			throw new NestableRuntimeException(e);
		} finally {
			serviceProvider.closeConnexion();

		}

	}

	/**
	 * @param mappingService
	 *            the mappingService to set
	 */
	public void setMappingService(MappingDocumentService mappingService) {
		this.mappingService = mappingService;
	}

	/**
	 * @return the mappingService
	 */
	public MappingDocumentService getMappingService() {
		return mappingService;
	}
}
