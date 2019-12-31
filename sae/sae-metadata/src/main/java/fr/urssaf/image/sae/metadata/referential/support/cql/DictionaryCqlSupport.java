package fr.urssaf.image.sae.metadata.referential.support.cql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IDictionaryDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;

/**
 * classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "Dictionary"
 */

@Component
public class DictionaryCqlSupport {

  @Autowired
  IDictionaryDaoCql dictionaryDaoCql;

  public DictionaryCqlSupport(final IDictionaryDaoCql dictionaryDaoCql) {
    this.dictionaryDaoCql = dictionaryDaoCql;
  }

  /**
   * Ajout d'une entrée au dictionnaire, le créé s'il n'existe pas
   * 
   * @param identifiant
   *          identifiant du dictionnaire
   * @param value
   *          valeur de l'entée
   */

  public final void addElement(final String identifiant, final String value) {
    final String tab[] = {value};
    final Dictionary dictionary = new Dictionary();
    dictionary.setIdentifiant(identifiant);
    dictionary.setEntries(Arrays.asList(tab));
    saveOrUpdate(dictionary);
  }

  /**
   * Supprime une entrée du dictionnaire
   * 
   * @param identifiant
   *          identifiant du dictionnaire
   * @param value
   *          Valeur de l'entrée à supprimer
   */
  public final void deleteElement(final String identifiant, final String value, final long clock) {
    final Optional<Dictionary> dictionaryOpt = dictionaryDaoCql.findWithMapperById(identifiant);
    if (dictionaryOpt.isPresent()) {
      final Dictionary dictionaryFromBD = dictionaryOpt.get();
      if (dictionaryFromBD.getEntries().remove(value)) {
        dictionaryDaoCql.saveWithMapper(dictionaryFromBD, clock);
      }
    }
  }

  /**
   * Sauvegarde d'une action unitaire
   * 
   * @param actionUnitaire
   */
  private void saveOrUpdate(final Dictionary dictionary) {
    Assert.notNull(dictionary, "l'objet dictionary ne peut etre null");

    final boolean isValidCode = true;
    final String errorKey = "dictionary";
    if (isValidCode) {
      // recuperation de l'objet ayant le meme codeevt dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau
      final String id = dictionary.getIdentifiant();
      final Optional<Dictionary> dictionaryOpt = dictionaryDaoCql.findWithMapperById(id);
      if (dictionaryOpt.isPresent()) {
        final Dictionary dictionaryFromBD = dictionaryOpt.get();
        // On vérifie si un terme doit être ajouté, on supprime tous les éléments de la liste de dictionary qui sont dans dictionaryFromBD
        dictionary.getEntries().removeAll(dictionaryFromBD.getEntries());
        // On ajoute la liste ainsi modifiée qui ne contient plus les éléments communs
        dictionaryFromBD.getEntries().addAll(dictionary.getEntries());
        // on enregistre la modification
        dictionaryDaoCql.saveWithMapper(dictionaryFromBD);
      } else {
        dictionaryDaoCql.saveWithMapper(dictionary);
      }
    } else {
      throw new MetadataRuntimeException(
                                         "Impossible de créer l'enregistrement demandé. " + "La clé "
                                             + errorKey + " n'est pas supportée");
    }
  }

  /**
   * Retourne un dictionary
   */
  public Dictionary find(final String identifiant) {
    Assert.notNull(identifiant, "l'identifiant ne peut etre null");
    return dictionaryDaoCql.findWithMapperById(identifiant).orElse(null);

    /*
     * final Optional<Dictionary> dictionaryOpt = dictionaryDaoCql.findWithMapperById(identifiant);
     * if (dictionaryOpt.isPresent()) {
     * return dictionaryOpt.get();
     * } else {
     * throw new DictionaryNotFoundException(
     * "Le dictionnaire n'a pas été trouvé");
     * }
     */
  }

  /**
   * Retourne la liste de toutes les dictionary
   */
  public List<Dictionary> findAll() {
    final Iterator<Dictionary> it = dictionaryDaoCql.findAllWithMapper();
    final List<Dictionary> list = new ArrayList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }


}
