package fr.urssaf.image.sae.service.indexComposite;

import java.util.List;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.SaeIndexComposite;

public interface IndexCompositeService {

  /**
   * Méthode de récupération de la liste des indexes composites
   * 
   * @return : La liste de {@link SaeIndexComposite}
   */
  List<SaeIndexComposite> getAllComputedIndexComposite();

  /**
   * Retourne true si l'indexComposite est utilisable pour les criteres passes en parametre
   * des critères passées en paramètre
   * 
   * @param indexComposite
   * @param listSaeMetadatas
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
      throws InvalidSAETypeException,
      MappingFromReferentialException;

  /**
   * Retourne l'indexComposite le plus pertinent de la liste passée en paramètre
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
  boolean isIndexedMetadata(UntypedMetadata metadata) throws MappingFromReferentialException;

  /**
   * Retourne true si l'indexComposite est utilisable pour les criteres passes en parametre
   * 
   * @param indexComposite
   * @param listShortCodeMetadatas
   * @return
   */
  boolean checkIndexCompositeValid(SaeIndexComposite indexComposite, List<String> listShortCodeMetadatas);

  /**
   * Retourne true si la metadata avec le shortCode passée en parametre est indexee
   * 
   * @param shortCodeMetadata
   * @return
   * @throws MappingFromReferentialException
   */
  boolean isIndexedMetadataByShortCode(String shortCodeMetadata) throws MappingFromReferentialException;

}