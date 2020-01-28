/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.serializer.PagmSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * Service DAO de la famille de colonnes "DroitPagm"
 * 
 */
@Repository
public class PagmDao extends AbstractDao<String, String> {

  public static final String PAGM_CFNAME = "DroitPagm";

  public static final String PAGM_CODE_PAGM = "code";

  public static final String PAGM_DESCRIPTION = "description";

  public static final String PAGM_PAGMA = "pagma";

  public static final String PAGM_PAGMP = "pagmp";

  public static final String PAGM_PAGMF = "pagmf";

  public static final String PAGM_PARAMETRES = "parametres";

  public static final String PAGM_COMPRESSION_PDF_ACTIVE = "compressionPdfActive";

  public static final String PAGM_SEUIL_COMPRESSION_PDF = "seuilCompressionPdf";

  /**
   * @param keyspace
   *          Keyspace utilisé par DroitPagm
   */
  @Autowired
  public PagmDao(final Keyspace keyspace) {
    super(keyspace);

  }

  /**
   * ajoute une colonne de PAGM
   * 
   * @param updater
   *          updater de <code>DroitPagm</code>
   * @param value
   *          valeur de la colonne
   * @param clock
   *          horloge de la colonne
   */
  public final void ecritPagm(final ColumnFamilyUpdater<String, String> updater,
                              final Pagm value, final long clock) {

    addColumn(updater, value.getCode(), value, PagmSerializer.get(), clock);

  }

  /**
   * ajoute une nouvelle ligne avec utilisation d'un mutator
   * 
   * @param idContratService
   *           Identifiant du contrat de service (clé de la ligne)
   * @param pagm
   *           pagm à créer
   * @param clock
   *           horloge de la colonne
   * @param mutator
   *           Mutator
   */
  public final void mutatorEcritPagm(final String idContratService, final Pagm pagm,
                                     final long clock, final Mutator<String> mutator) {
    addColumnWithMutator(idContratService, pagm.getCode(), pagm,
                         PagmSerializer.get(), clock, mutator);
  }

  /**
   * Suppression d'un PAGM
   * 
   * @param mutator
   *           Mutator de <code>Pagm</code>
   * @param idContratService
   *           Identifiant du contrat de service auquel le PAGM est rattaché
   * @param codePagm
   *           identifiant du Pagm à supprimer
   * @param clock
   *           horloge de la suppression
   */
  public final void mutatorSuppressionPagm(final Mutator<String> mutator,
                                           final String idContratService, final String codePagm, final long clock) {
    mutatorSuppressionColonne(mutator, idContratService, codePagm,
                              clock);
  }

  @Override
  public final String getColumnFamilyName() {
    return PAGM_CFNAME;
  }

  @Override
  public final Serializer<String> getColumnKeySerializer() {
    return StringSerializer.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Serializer<String> getRowKeySerializer() {
    return StringSerializer.get();
  }
}
