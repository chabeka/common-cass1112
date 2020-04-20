/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * Service DAO de la famille de colonnes "DroitPagmp"
 * 
 */
@Repository
public class PagmpDao extends AbstractDao<String, String> {

  public static final String PAGMP_DESCRIPTION = "description";

  public static final String PAGMP_PRMD = "prmd";

  public static final String PAGMP_CFNAME = "DroitPagmp";

   /**
   * @param keyspace
   *          Keyspace utilisé par la Pagmp
   */
  @Autowired
  public PagmpDao(final Keyspace keyspace) {
    super(keyspace);
  }

  /**
   * ajoute une colonne {@value #PAGMP_PRMD}
   * 
   * @param updater
   *           updater de <code>DroitActionUnitaire</code>
   * @param value
   *           valeur de la colonne
   * @param clock
   *           horloge de la colonne
   */
  public final void ecritPrmd(final ColumnFamilyUpdater<String, String> updater,
                              final String value, final long clock) {

    addColumn(updater, PAGMP_PRMD, value, StringSerializer.get(), clock);

  }

  /**
   * ajoute une colonne {@value #PAGMP_PRMD}
   * 
   * @param code
   *           code de la ligne
   * @param prmd
   *           PRMD
   * @param clock
   *           horloge de la création
   * @param mutator
   *           mutator de la colonne Pagmp
   */
  public final void ecritPrmd(final String code, final String prmd, final long clock,
                              final Mutator<String> mutator) {
    addColumnWithMutator(code, PAGMP_PRMD, prmd, StringSerializer.get(),
                         clock, mutator);
  }

  /**
   * ajoute une colonne {@value #PAGMP_DESCRIPTION}
   * 
   * @param updater
   *           updater de <code>DroitActionUnitaire</code>
   * @param value
   *           valeur de la colonne
   * @param clock
   *           horloge de la colonne
   */
  public final void ecritDescription(
                                     final ColumnFamilyUpdater<String, String> updater, final String value, final long clock) {
    addColumn(updater, PAGMP_DESCRIPTION, value, StringSerializer.get(),
              clock);
  }

  /**
   * ajoute une colonne {@value #PAGMP_DESCRIPTION} avec utilisation d'un
   * mutator
   * 
   * @param code
   *           le code du PAGMp
   * @param description
   *           la description du PAGMp
   * @param clock
   *           horloge de la colonne
   * @param mutator
   *           Mutator
   */
  public final void ecritDescription(final String code, final String description,
                                     final long clock, final Mutator<String> mutator) {
    addColumnWithMutator(code, PAGMP_DESCRIPTION, description,
                         StringSerializer.get(), clock, mutator);
  }

  @Override
  public final String getColumnFamilyName() {
    return PAGMP_CFNAME;
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
