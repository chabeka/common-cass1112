/**
 * 
 */
package fr.urssaf.image.sae.services.modification;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Service permettant de réaliser des modifications sur les documents
 * 
 */
public interface SAEModificationService {

   /**
    * Modifie le document donné
    * 
    * @param idArchive
    *           identifiant unique du document à modifier
    * @param metadonnees
    *           Liste des métadonnées :
    *           <ul>
    *           <li>Les métadonnées ayant une valeur vide sont celles qui
    *           doivent être supprimées</li>
    *           <li>Les métadonnées ayant une valeur renseignée sont les
    *           métadonnées à créer ou modifier</li>
    *           </ul>
    * @throws InvalidValueTypeAndFormatMetadataEx
    *            Au moins une des métadonnées fournies n'est pas du bon type ou
    *            du bon format
    * @throws UnknownMetadataEx
    *            au moins une des métadonnées n'existe pas
    * @throws DuplicatedMetadataEx
    *            au moins une des métadonnées est en double dans la liste
    *            fournie
    * @throws NotSpecifiableMetadataEx
    *            au moins une des métadonnées n'est pas spécifiable à
    *            l'archivage
    * @throws RequiredArchivableMetadataEx
    *            Au moins une des métadonnées requises à l'archivage n'est pas
    *            présente
    * @throws ReferentialRndException
    *            une erreur a eu lieu lors de la récupération des RND
    * @throws UnknownCodeRndEx
    *            le code RND est inconnu
    * @throws UnknownHashCodeEx
    *            Une erreur a été soulevée lors de la vérification du hash
    * @throws NotModifiableMetadataEx
    *            au moins une des métadonnées n'est pas modifiable
    * @throws ModificationException
    *            une erreur a été soulevée lors de la modification du document
    * @throws ArchiveInexistanteEx
    *            Le document à modifier n'a pas été trouvé
    * @throws MetadataValueNotInDictionaryEx
    *            La valeur d'au moins une des métadannées n'appartient pas au
    *            dictionnaire rattaché
    */
   @PreAuthorize("hasRole('modification')")
   void modification(UUID idArchive, List<UntypedMetadata> metadonnees)
         throws InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, ReferentialRndException,
         UnknownCodeRndEx, UnknownHashCodeEx, NotModifiableMetadataEx,
         ModificationException, ArchiveInexistanteEx,
         MetadataValueNotInDictionaryEx;

   /**
    * Controle des metadatas à modifier pour un identifiant de document donné.
    * @param idArchive Identifiant du document
    * @param metadonnees Metadonnées à controler
    * @param trcPrefix Trace prefixe
    * @return La liste des metadonnées devant être modifiées.
    * @throws ArchiveInexistanteEx @{@link ArchiveInexistanteEx}
    * @throws ModificationException @{@link ModificationException}
    * @throws DuplicatedMetadataEx @{@link DuplicatedMetadataEx}
    */
   public List<StorageMetadata> controlerMetaDocumentModifie(UUID idArchive,
         List<UntypedMetadata> metadonnees, String trcPrefix) throws ArchiveInexistanteEx, ModificationException, DuplicatedMetadataEx;
   
   /**
    * Modification des metadonnées d'un document.
    * @param document Le documents à modifier (Metas modifiées et metas supprimées)
    * @param metadonnees Metadonnées à modifier
    * @param trcPrefix Trace prefixe
    * @throws ModificationException @{@link ModificationException}
    * @throws UpdateServiceEx @{@link UpdateServiceEx}
    */
   public void modificationMetaDocument(StorageDocument document, String trcPrefix) throws ModificationException, UpdateServiceEx;
   
   /**
    * Permet la séparation des metadonnées en 2 listes de metadonnées (metas à supprimer et metas à modifier)
    * @param idArchive Identifiant du document
    * @param listeStorageMetaDocument Liste des metadonnées du document
    * @param metadonnees Liste de metadonnées à modifier ou supprimer
    * @param trcPrefix Prefix de trace
    * @return Le storage document à modifier
    * @throws UnknownCodeRndEx @{@link UnknownCodeRndEx}
    * @throws ReferentialRndException @{@link ReferentialRndException}
    * @throws InvalidValueTypeAndFormatMetadataEx @{@link InvalidValueTypeAndFormatMetadataEx}
    * @throws UnknownMetadataEx @{@link UnknownMetadataEx}
    * @throws DuplicatedMetadataEx @{@link DuplicatedMetadataEx}
    * @throws NotSpecifiableMetadataEx @{@link NotSpecifiableMetadataEx}
    * @throws RequiredArchivableMetadataEx @{@link RequiredArchivableMetadataEx}
    * @throws UnknownHashCodeEx @{@link UnknownHashCodeEx}
    * @throws NotModifiableMetadataEx @{@link NotModifiableMetadataEx}
    * @throws MetadataValueNotInDictionaryEx @{@link MetadataValueNotInDictionaryEx}
    * @throws ModificationException @{@link ModificationException}
    */
   public StorageDocument separationMetaDocumentModifie(UUID idArchive,
         List<StorageMetadata> listeStorageMetaDocument, List<UntypedMetadata> metadonnees, String trcPrefix) throws UnknownCodeRndEx, 
         ReferentialRndException, InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx, 
         RequiredArchivableMetadataEx, UnknownHashCodeEx, NotModifiableMetadataEx, MetadataValueNotInDictionaryEx, ModificationException;

   /**
    * Methode permettant de générer la liste des metadonnées storage.
    * 
    * @param idArchive
    *           Identifiant document
    * @return la liste des metadonnées storage
    * @throws ReferentialException
    * @{@link ReferentialException}
    * @throws RetrievalServiceEx
    * @{@link RetrievalServiceEx}
    */
   public List<StorageMetadata> getListeStorageMetadatas(UUID idArchive)
         throws ReferentialException, RetrievalServiceEx;
}
