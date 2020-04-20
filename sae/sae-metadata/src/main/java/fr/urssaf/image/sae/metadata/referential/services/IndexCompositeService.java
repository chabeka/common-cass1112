package fr.urssaf.image.sae.metadata.referential.services;

import java.util.Collection;
import java.util.List;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.exceptions.IndexCompositeException;
import fr.urssaf.image.sae.metadata.referential.model.SaeIndexComposite;

public interface IndexCompositeService {

   /**
    * Méthode de récupération de la liste des indexes composites
    * 
    * @return : La liste de {@link SaeIndexComposite}
    */
   List<SaeIndexComposite> getAllComputedIndexComposite();

   /**
    * Retourne true si l'indexComposite est utilisable pour les critères passés en paramètre,
    * c'est à dire si l'ensemble des métadonnées composant l'index sont présentes dans la liste
    * listSaeMetadatas
    * 
    * @param indexComposite
    *           l'index composite testé
    * @param listSaeMetadatas
    *           la liste des métadonnées, correspondant aux critères de recherche
    * @return
    */
   boolean isIndexCompositeValid(SaeIndexComposite indexComposite, List<SAEMetadata> listSaeMetadatas);

   /**
    * Service de conversion d’une liste d'objets de type UntypedMetadata
    * vers une liste de codes d'objets de typeSAEMetadata.
    * 
    * @param metadatas
    * @return
    * @throws InvalidSAETypeException
    * @throws MappingFromReferentialException
    */
   List<SAEMetadata> untypedMetadatasToCodeSaeMetadatas(
                                                        final List<UntypedMetadata> metadatas)
         throws IndexCompositeException;

   /**
    * Retourne l'indexComposite le plus pertinent de la liste passée en paramètre.
    * Le plus pertinent est celui qui est composé du plus grand nombre de métadonnées
    * 
    * @param indexCandidats
    * @return
    */
   SaeIndexComposite getBestIndexComposite(List<SaeIndexComposite> indexCandidats);

   /**
    * Retourne true si la metadata passée en paramètre est indexée
    * 
    * @param metadata
    * @return
    * @throws MappingFromReferentialException
    */
   boolean isIndexedMetadata(UntypedMetadata metadata) throws IndexCompositeException;

   /**
    * Retourne true si l'indexComposite est utilisable pour les critères passés en paramètre,
    * c'est à dire si l'ensemble des métadonnées composant l'index sont présentes dans la liste
    * listShortCodeMetadatas
    * 
    * @param indexComposite
    *           l'index composite testé
    * @param listShortCodeMetadatas
    *           la liste des codes courts des métadonnées, correspondant aux critères de recherche
    * @return
    */
   boolean checkIndexCompositeValid(SaeIndexComposite indexComposite, Collection<String> listShortCodeMetadatas);

   /**
    * Retourne true si la metadata avec le shortCode passée en paramètre est indexée
    * 
    * @param shortCodeMetadata
    * @return
    * @throws MappingFromReferentialException
    */
   boolean isIndexedMetadataByShortCode(String shortCodeMetadata) throws IndexCompositeException;

   /**
    * Trouve le meilleur index (composite ou non) possible pour une requête de recherche donnée
    * 
    * @param shortCodeRequiredMetadatas
    *           La liste des code courts des métadonnées valorisées dans la recherche. Il convient de trouver un
    *           index entièrement composé avec ces métadonnées
    * @return
    *         L'index, (avec des codes courts de métadonnées)
    */
   String getBestIndexForQuery(Collection<String> shortCodeRequiredMetadatas) throws IndexCompositeException;

   /**
    * Service de conversion d’une liste d'objets de type UntypedMetadata
    * vers une liste de codes courts de métadonnées
    * 
    * @param metadatas
    * @return
    * @throws InvalidSAETypeException
    * @throws MappingFromReferentialException
    */
   List<String> untypedMetadatasToShortCodeMetadatas(List<UntypedMetadata> metadatas) throws IndexCompositeException;

   /**
    * Service de conversion d’une liste de code long de métadonnées
    * vers une liste de codes courts de métadonnées
    * 
    * @param metadatas
    * @return
    * @throws IndexCompositeException
    */
   List<String> longCodeMetadatasToShortCodeMetadatas(Collection<String> metadatas) throws IndexCompositeException;

}