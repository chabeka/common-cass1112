package fr.urssaf.image.sae.services.document;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.bo.model.AbstractMetadata;
import fr.urssaf.image.sae.bo.model.untyped.PaginatedUntypedDocuments;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedRangeMetadata;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.DoublonFiltresMetadataEx;
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
    * @param listeFiltreEgal
    *           Liste des filtres de type "égal" après exécution de la requête
    *           principale
    * @param listeFiltreDifferent
    *           Liste des filtres de type "différent" après exécution de la
    *           requête principale
    * @param nbDocumentsParPage
    *           Nombre de document à récupérer
    * @param lastIdDoc
    *           Identifiant du dernier document renvoyé par la recherche par
    *           iterateur précédente
    * @param listeDesiredMetadata
    *           Liste des métadonnées souhaitées en retour de recherche
    *           du résultat final
    * @return La liste des documents trouvés
    * @throws MetaDataUnauthorizedToSearchEx
    *            Erreur si une méta n'est pas autorisée à la recherche
    * @throws MetaDataUnauthorizedToConsultEx
    *            Erreur si une méta n'est pas autorisée à la consultation
    * @throws UnknownLuceneMetadataEx
    *            Erreur si métadonnée inexistante
    * @throws SAESearchServiceEx
    *            Erreur lors de la recherche paginée
    * @throws SyntaxLuceneEx
    *            Erreur de syntaxe de la recherche lucène
    * @throws UnknownDesiredMetadataEx
    *            Erreur si métas inconnues dans la liste des métas souhaitées en
    *            résultat
    * @throws UnknownFiltresMetadataEx
    *            Erreur si métas inconnues dans les filtres
    * @throws DoublonFiltresMetadataEx
    *            Erreur si métas en double dans les filtres
    */
   @PreAuthorize("hasRole('recherche_iterateur')")
   PaginatedUntypedDocuments searchPaginated(
         List<UntypedMetadata> fixedMetadatas,
         UntypedRangeMetadata varyingMetadata,
         List<AbstractMetadata> listeFiltreEgal,
         List<AbstractMetadata> listeFiltreDifferent, int nbDocumentsParPage,
         UUID lastIdDoc, List<String> listeDesiredMetadata)
         throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx;
}