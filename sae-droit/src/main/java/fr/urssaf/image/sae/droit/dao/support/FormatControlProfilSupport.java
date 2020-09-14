/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.FormatControlProfilDao;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.serializer.FormatProfilSerializer;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.utils.Constantes;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * Classe de support permettant d'exploiter le bean et la DAO du profil de
 * contrôle {@link FormatControlProfilDao}.
 * 
 */
@Component
public class FormatControlProfilSupport {

  private static final String FIN_LOG = "{} - fin";
  private static final String DEBUT_LOG = "{} - début";
  private static final Logger LOGGER = LoggerFactory
      .getLogger(FormatControlProfilSupport.class);

  private final FormatControlProfilDao formatControlDao;

  // recherches

  /**
   * constructeur
   * 
   * @param formatControlDao
   *           DAO associée au pagmf
   */
  @Autowired
  public FormatControlProfilSupport(final FormatControlProfilDao formatControlDao) {
    this.formatControlDao = formatControlDao;
  }

  /**
   * Création d'un nouveau profil de contrôle
   * 
   * @param profil
   *           {@link FormatControlProfil} à créer
   * @param clock
   *           horloge de la création
   */
  public final void create(final FormatControlProfil profil, final long clock) {
    try {
      final String opeFormatControlPrefix = "ajouterFormatControl";

      LOGGER.debug(DEBUT_LOG, opeFormatControlPrefix);
      LOGGER.debug("{} - Identifiant du FormatControlProfil : {}",
                   new String[] { opeFormatControlPrefix, profil.getFormatCode() });

      final ColumnFamilyUpdater<String, String> updater = formatControlDao
          .getCfTmpl().createUpdater(profil.getFormatCode());

      formatControlDao.addFormatControlProfil(updater, profil, clock);

      formatControlDao.getCfTmpl().update(updater);

      LOGGER.info("{} - Ajout du FormatControlProfil : {}", new String[] {
                                                                          opeFormatControlPrefix, profil.getFormatCode() });
      LOGGER.debug(FIN_LOG, opeFormatControlPrefix);
    } catch (final Exception except) {
      throw new DroitRuntimeException(except);
    }

  }

  /**
   * Méthode de suppression d'un {@link FormatControlProfil}
   * 
   * @param code
   *           identifiant du FormatControlProfil à supprimer - paramètre
   *           obligatoire
   * @param clock
   *           horloge de suppression - paramètre obligatoire
   * @throws FormatControlProfilNotFoundException : formatControlProfil inexistant          
   */
  public final void delete(final String code, final Long clock) throws FormatControlProfilNotFoundException {

    if (StringUtils.isBlank(code)) {
      throw new IllegalArgumentException(ResourceMessagesUtils
                                         .loadMessage("erreur.format.profil.notnull"));
    }

    if (clock == null || clock <= 0) {
      throw new IllegalArgumentException(ResourceMessagesUtils
                                         .loadMessage("erreur.param"));
    }

    final FormatControlProfil formatControl = find(code);

    if (formatControl == null) {
      // pas en base
      throw new FormatControlProfilNotFoundException(ResourceMessagesUtils
                                                     .loadMessage("erreur.format.control.delete", code));
    }

    try {
      final Mutator<String> mutator = formatControlDao.createMutator();
      formatControlDao.deleteFormatControlProfil(mutator, code, clock);
      mutator.execute();
    } catch (final Exception except) {
      throw new DroitRuntimeException(ResourceMessagesUtils
                                      .loadMessage("erreur.impossible.delete.pagmf"), except);
    }
  }

  /**
   * Méthode de lecture d'une ligne
   * 
   * @param code
   *           identifiant du FormatControlProfil
   * @return un FormatControlProfil correspondant à l'identifiant passé en
   *         paramètre
   */
  public final FormatControlProfil find(final String code) {

    final ColumnFamilyResult<String, String> result = formatControlDao.getCfTmpl()
        .queryColumns(code);

    final FormatControlProfil formatControl = getFormatControlProfilFromResult(result);

    return formatControl;

  }

  private FormatControlProfil getFormatControlProfilFromResult(
                                                               final ColumnFamilyResult<String, String> result) {

    FormatControlProfil formatControl = null;

    if (result != null && result.hasResults()) {
      formatControl = new FormatControlProfil();

      formatControl.setFormatCode(result.getKey());

      formatControl.setDescription(result
                                   .getString(Constantes.COL_DESCRIPTION));

      if (result.getByteArray(Constantes.COL_CONTROLPROFIL) != null) {
        final FormatProfil formatProfil = FormatProfilSerializer.get().fromBytes(
                                                                                 result.getByteArray(Constantes.COL_CONTROLPROFIL));
        formatControl.setControlProfil(formatProfil);
      }
    }

    return formatControl;
  } // Erreur levée quand le FormatControlProfil demandé n’existe pas

  /**
   * Récupération de tous les FormatControlProfil de la famille de colonne.
   * 
   * 
   * @return Une liste d’objet {@link FormatControlProfil} représentant le
   *         contenu de CF DroitFormatControlProfil
   */
  public final List<FormatControlProfil> findAll() {

    try {
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();

      final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
          .createRangeSlicesQuery(formatControlDao.getKeyspace(),
                                  StringSerializer.get(), StringSerializer.get(),
                                  bytesSerializer);

      rangeSlicesQuery.setColumnFamily(formatControlDao
                                       .getColumnFamilyName());
      rangeSlicesQuery.setRange("", "", false, AbstractDao.DEFAULT_MAX_COLS);
      rangeSlicesQuery.setRowCount(AbstractDao.DEFAULT_MAX_ROWS);
      final QueryResult<OrderedRows<String, String, byte[]>> queryResult = rangeSlicesQuery
          .execute();

      // Convertion du résultat en ColumnFamilyResultWrapper pour mieux
      // l'utiliser
      final QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<>();
      final ColumnFamilyResultWrapper<String, String> result = converter
          .getColumnFamilyResultWrapper(queryResult, StringSerializer
                                        .get(), StringSerializer.get(), bytesSerializer);

      final HectorIterator<String, String> resultIterator = new HectorIterator<>(
          result);

      final List<FormatControlProfil> list = new ArrayList<>();
      for (final ColumnFamilyResult<String, String> row : resultIterator) {
        if (row != null && row.hasResults()) {

          final FormatControlProfil formatControlProfil = getFormatControlProfilFromResult(row);
          list.add(formatControlProfil);
        }
      }

      return list;
    } catch (final Exception except) {
      throw new DroitRuntimeException(ResourceMessagesUtils
                                      .loadMessage("erreur.impossible.recup.info"), except);
    }
  }

}
