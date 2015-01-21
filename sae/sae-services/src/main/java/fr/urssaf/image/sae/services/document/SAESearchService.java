package fr.urssaf.image.sae.services.document;

import java.util.List;
import java.util.UUID;

import org.antlr.grammar.v3.ANTLRv3Parser.throwsSpec_return;
import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.bo.model.AbstractMetadata;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.PaginatedUntypedDocuments;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedRangeMetadata;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;

/**
 * Fournit l'ensemble des services pour la recherche.
 */
public interface SAESearchService {

   /**
    * Service pour l'opération <b>Recherche</b>
    * 
    * @param requete
    *           requete LuceneCriteria
    * @param listMetaDesired
    *           liste des metaDonnees desirée
    * @return Une liste de document de type {@link UntypedDocument}.
    * @throws SAESearchServiceEx
    *            Exception levée lorsqu'une erreur s'est produite lors de la
    *            recherche dans DFCE.
    * @throws MetaDataUnauthorizedToSearchEx
    *            Exception levée lorsqu'une erreur s'est produite sur des
    *            metadonnees<br>
    *            non autorisées pour la recherche dans DFCE.
    * @throws MetaDataUnauthorizedToConsultEx
    *            Exception levée lorsqu'une erreur s'est produite sur des
    *            metadonnees<br>
    *            non autorisées pour la consultation dans DFCE.
    * @throws UnknownDesiredMetadataEx
    *            {@link UnknownDesiredMetadataEx}
    * @throws UnknownLuceneMetadataEx
    *            {@link UnknownLuceneMetadataEx}
    * @throws SyntaxLuceneEx
    *            {@link SyntaxLuceneEx}
    */
   @PreAuthorize("hasRole('recherche')")
   List<UntypedDocument> search(String requete, List<String> listMetaDesired)
         throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
         UnknownLuceneMetadataEx, SyntaxLuceneEx, SAESearchServiceEx;

   /**
    * Service pour l'opération <b>Recherche</b>
    * 
    * @param requete
    *           requete LuceneCriteria
    * @param listMetaDesired
    *           liste des metaDonnees desirée
    * @param maxResult
    *           nombre maximum de résultat renvoyé.<i>attention : ne pas
    *           dépasser 5000 sous peine de saturation mémoire</i>
    * @return Une liste de document de type {@link UntypedDocument}.
    * @throws SAESearchServiceEx
    *            Exception levée lorsqu'une erreur s'est produite lors de la
    *            recherche dans DFCE.
    * @throws MetaDataUnauthorizedToSearchEx
    *            Exception levée lorsqu'une erreur s'est produite sur des
    *            metadonnees<br>
    *            non autorisées pour la recherche dans DFCE.
    * @throws MetaDataUnauthorizedToConsultEx
    *            Exception levée lorsqu'une erreur s'est produite sur des
    *            metadonnees<br>
    *            non autorisées pour la consultation dans DFCE.
    * @throws UnknownDesiredMetadataEx
    *            {@link UnknownDesiredMetadataEx}
    * @throws UnknownLuceneMetadataEx
    *            {@link UnknownLuceneMetadataEx}
    * @throws SyntaxLuceneEx
    *            {@link SyntaxLuceneEx}
    */
   @PreAuthorize("hasRole('recherche')")
   List<UntypedDocument> search(String requete, List<String> listMetaDesired,
         int maxResult) throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
         UnknownLuceneMetadataEx, SyntaxLuceneEx, SAESearchServiceEx;

   /**
    * Permet de faire une recherche paginée.
    * 
    * @param fixedMetadatas
    *           Liste des métadonnées fixes d'un index composite
    * @param varyingMetadata
    *           Liste des métadonnées variables d'un index composite
    * @param filters
    *           Liste des filtres après exécution de la requête principale
    * @param nbDocumentsParPage
    *           Nombre de document à récupérer
    * @param lastIdDoc
    *           Identifiant du dernier document renvoyé par la recherche par
    *           iterateur précédente
    * @param listeDesiredMetadata
    *           Liste des métadonnées souhaitées en retour de recherche
    * @return La liste des documents trouvés
    * @throws MetaDataUnauthorizedToSearchEx
    * @throws MetaDataUnauthorizedToConsultEx
    * @throws UnknownLuceneMetadataEx
    * @throws SyntaxLuceneEx
    * @throws SAESearchServiceEx
    * @throws UnknownDesiredMetadataEx
    * @throws UnknownFiltresMetadataEx 
    */
   @PreAuthorize("hasRole('recherche')")
   PaginatedUntypedDocuments searchPaginated(
         List<UntypedMetadata> fixedMetadatas,
         UntypedRangeMetadata varyingMetadata, List<AbstractMetadata> filters,
         int nbDocumentsParPage, UUID lastIdDoc,
         List<String>  listeDesiredMetadata)
         throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx,UnknownDesiredMetadataEx, UnknownFiltresMetadataEx;
}