/**
 * 
 */
package fr.urssaf.image.sae.services.modification;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;

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
    * @throws NotArchivableMetadataEx
    *            au moins une des métadonnées n'est pas archivable
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

}
