/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.service;

import java.util.List;

import com.datastax.driver.mapping.Result;

import fr.urssaf.image.sae.model.GenericType;
import fr.urssaf.image.sae.model.Metadata;

/**
 * TODO (AC75095028) Description du type
 */
public interface IMetadataReferenceService {

  List<Metadata> findMetadatasRecherchables();

  List<Metadata> findMetadatasConsultables();

  Result<Metadata> findAll();

  List<Metadata> saveAll(List<Metadata> metas);

  List<Metadata> findAllMetadata();

  List<Metadata> findAllByGType();

  void delete(Metadata metadata);

  void deleteById(String id);

  public void insertGType(GenericType gtype, Object daoType);
}
