/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * Service DAO de la famille de colonnes "DroitPagma"
 * 
 */
@Repository
public class PagmaDao extends AbstractDao<String, String> {

  public static final String PAGMA_CFNAME = "DroitPagma";

  public static final int MAX_ATTRIBUTS = 100;

   /**
   * Constructeur
   * 
   * @param keyspace
   *          Keyspace utilisé par DroitPagma
   */
  @Autowired
  public PagmaDao(final Keyspace keyspace) {
    super(keyspace);

  }

  /**
   * Ajoute une colonne Action unitaire
   * 
   * @param updater
   *           updater de <code>DroitPagma</code>
   * @param colName
   *           identifiant de la colonne
   * @param clock
   *           horloge de la colonne
   */
  public final void ecritActionUnitaire(
                                        final ColumnFamilyUpdater<String, String> updater, final String colName, final long clock) {

    addColumn(updater, colName, StringUtils.EMPTY, StringSerializer.get(),
              clock);

  }

  /**
   * Ajoute une colonne Action unitaire avec utilisation mutator
   * 
   * @param code
   *           le code du PAGMa
   * @param action
   *           l'action unitaire
   * @param clock
   *           horloge de la colonne
   * @param mutator
   *           le mutator
   */
  public final void ecritActionUnitaire(final String code, final String action,
                                        final long clock, final Mutator<String> mutator) {
    addColumnWithMutator(code, action, "", StringSerializer.get(), clock,
                         mutator);
  }

  @Override
  public final String getColumnFamilyName() {
    return PAGMA_CFNAME;
  }

  /**
   * {@inheritDoc}
   */
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
