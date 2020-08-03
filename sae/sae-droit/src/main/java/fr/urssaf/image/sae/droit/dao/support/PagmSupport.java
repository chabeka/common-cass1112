/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.PagmDao;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.serializer.PagmSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * Classe de support de la classe {@link PagmDao}
 * 
 */
@Component
public class PagmSupport {

  private final PagmSerializer pagmSerializer = PagmSerializer.get();

  private final PagmDao dao;

  /**
   * constructeur
   * 
   * @param pagmDao
   *           DAO associée au pagm
   */
  @Autowired
  public PagmSupport(final PagmDao pagmDao) {
    dao = pagmDao;
  }

  /**
   * Méthode de création d'un ligne
   * 
   * @param idClient
   *           identifiant du client
   * @param pagm
   *           propriétés du PAGM à créer
   * @param clock
   *           horloge de la création
   */
  public final void create(final String idClient, final Pagm pagm, final long clock) {

    final ColumnFamilyUpdater<String, String> updater = dao.getCfTmpl()
        .createUpdater(idClient);

    dao.ecritPagm(updater, pagm, clock);

    dao.getCfTmpl().update(updater);

  }

  /**
   * Méthode de création d'un ligne avec utilisation d'un mutator
   * 
   * @param idClient
   *           identifiant du client
   * @param pagm
   *           propriétés du PAGM à créer
   * @param clock
   *           horloge de la création
   * @param mutator
   *           mutator de la ligne de pagm
   */
  public final void create(final String idClient, final Pagm pagm, final long clock,
                           final Mutator<String> mutator) {

    dao.mutatorEcritPagm(idClient, pagm, clock, mutator);
  }

  /**
   * Méthode de suppression d'une ligne
   * 
   * @param idContratService
   *           identifiant du contrat de service auquel est rattaché le PAGM
   * @param codePagm
   *           Identifiant du PAGM à supprimer
   * @param clock
   *           horloge de suppression
   */
  public final void delete(final String idContratService, final String codePagm, final long clock) {

    final Mutator<String> mutator = dao.createMutator();

    dao.mutatorSuppressionPagm(mutator, idContratService, codePagm, clock);

    mutator.execute();
  }

  /**
   * Méthode de suppression d'une ligne avec mutator en paramètre
   * 
   * @param idContratService
   *           Identifiant du contrat de service auquel le PAGM est rattaché
   * @param codePagm
   *           identifiant du Pagm à supprimer
   * @param clock
   *           horloge de suppression
   * @param mutator
   *           Mutator
   */
  public final void delete(final String idContratService, final String codePagm,
                           final long clock, final Mutator<String> mutator) {
    dao.mutatorSuppressionPagm(mutator, idContratService, codePagm, clock);
  }

  /**
   * Méthode de lecture d'une ligne
   * 
   * @param code
   *           identifiant du Contrat de service
   * @return la liste des PAGM correspondant à l'identifiant passé en paramètre
   */
  public final List<Pagm> find(final String code) {

    dao.getCfTmpl().setCount(AbstractDao.DEFAULT_MAX_COLS);
    final ColumnFamilyResult<String, String> result = dao.getCfTmpl().queryColumns(
                                                                                   code);

    final Collection<String> colNames = result.getColumnNames();

    List<Pagm> pagms = null;

    if (CollectionUtils.isNotEmpty(colNames)) {
      pagms = new ArrayList<>(colNames.size());
    }

    for (final String name : colNames) {

      final byte[] bResult = result.getByteArray(name);
      final Pagm pagm = pagmSerializer.fromBytes(bResult);

      pagms.add(pagm);
    }
    return pagms;
  }

}
