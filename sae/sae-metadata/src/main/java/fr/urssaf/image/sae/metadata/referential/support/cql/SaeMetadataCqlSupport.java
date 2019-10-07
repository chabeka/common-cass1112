package fr.urssaf.image.sae.metadata.referential.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IMetadataDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;;

/**
 * classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "Metadata"
 */

@Component
public class SaeMetadataCqlSupport {

  @Autowired
  IMetadataDaoCql metadataDaoCql;



  /**
   * Ajout d'une entrée au metadata, le créé s'il n'existe pas
   * 
   * @param metadata
   */

  public final void create(final MetadataReference metadata) {
    checkCodeCourtInexistant(metadata.getShortCode());
    saveOrUpdate(metadata);
  }


  /**
   * Modification d'une entrée au metadata
   * 
   * @param metadata
   */
  public final void modify(final MetadataReference metadata) {
    checkCodeCourtInexistant(metadata.getShortCode());
    saveOrUpdate(metadata);
  }


  /**
   * Sauvegarde d'une action unitaire
   * 
   * @param actionUnitaire
   */
  private void saveOrUpdate(final MetadataReference metadata) {
    Assert.notNull(metadata, "l'objet metadata ne peut etre null");

    final boolean isValidCode = true;
    final String errorKey = "metadata";
    if (isValidCode) {
      // recuperation de l'objet ayant le meme code long dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau
      final String id = metadata.getLongCode();
      final Optional<MetadataReference> metadataOpt = metadataDaoCql.findWithMapperById(id);
      if (metadataOpt.isPresent()) {
        final MetadataReference metadataFromBD = metadataOpt.get();
        metadataDaoCql.saveWithMapper(metadataFromBD);
      } else {
        metadataDaoCql.saveWithMapper(metadata);
      }
    } else {
      throw new MetadataRuntimeException(
                                         "Impossible de créer l'enregistrement demandé. " + "La clé "
                                             + errorKey + " n'est pas supportée");
    }
  }

  /**
   * Retourne un metadata
   */
  public MetadataReference find(final String codeLong) {
    Assert.notNull(codeLong, "l'identifiant ne peut etre null");
    return metadataDaoCql.findWithMapperById(codeLong).orElse(null);
  }

  /**
   * Retourne la liste de toutes les metadata
   */
  public List<MetadataReference> findAll() {
    final Iterator<MetadataReference> it = metadataDaoCql.findAllWithMapper();
    final List<MetadataReference> list = new ArrayList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }

  /**
   * Récupère toutes les métadonnées recherchables
   * 
   * @return la liste des métadonnées recherchables
   */
  public final List<MetadataReference> findMetadatasRecherchables() {

    // On itère sur le résultat et on ne récupère que les métadonnées
    // recherchable
    final List<MetadataReference> listRecherchables = new ArrayList<>();
    final List<MetadataReference> list = findAll();
    for (final MetadataReference metadata : list) {
      if (metadata.isSearchable()) {
        listRecherchables.add(metadata);
      }
    }
    return listRecherchables;
  }

  /**
   * Récupère toutes les métadonnées Consultables
   * 
   * @return la liste des métadonnées Consultables
   */
  public final List<MetadataReference> findMetadatasConsultables() {

    // On itère sur le résultat et on ne récupère que les métadonnées
    // Consultables
    final List<MetadataReference> listConsultables = new ArrayList<>();
    final List<MetadataReference> list = findAll();
    for (final MetadataReference metadata : list) {
      if (metadata.isConsultable()) {
        listConsultables.add(metadata);
      }
    }
    return listConsultables;
  }

  private void checkCodeCourtInexistant(final String codeCourt) {
    final List<MetadataReference> metadatas = findAll();
    boolean found = false;
    int index = 0;

    while (!found && index < metadatas.size()) {
      if (codeCourt.equalsIgnoreCase(metadatas.get(index).getShortCode())) {
        found = true;
      }

      index++;
    }

    if (found) {
      throw new MetadataRuntimeException("Code court déjà existant : '" + codeCourt + "'");
    }
  }

}
