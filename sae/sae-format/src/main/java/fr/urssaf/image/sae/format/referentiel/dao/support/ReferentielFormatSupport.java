package fr.urssaf.image.sae.format.referentiel.dao.support;

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
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.ReferentielFormatDao;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.Constantes;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;
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

/***
 * Classe support permettant d’exploiter le bean {@link FormatFichier} <br>
 * et la DAO {@link ReferentielFormatDao} du référentiel des formats.<br>
 * Classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "ReferentielFormat" *
 */
@Component
public class ReferentielFormatSupport {

  private final ReferentielFormatDao referentielFormatDao;


  private static final String FIN_LOG = "{} - fin";

  private static final String DEBUT_LOG = "{} - début";

  private static final Logger LOGGER = LoggerFactory.getLogger(ReferentielFormatSupport.class);


  /**
   * Constructeur de la classe support
   * 
   * @param referentielFormatDao
   *          la dao
   */
  @Autowired
  public ReferentielFormatSupport(final ReferentielFormatDao referentielFormatDao) {
    this.referentielFormatDao = referentielFormatDao;
    // messageHandler = messageHandler;
  }

  /**
   * Ajoute un nouveau format de fichier {@link FormatFichier}.
   * 
   * @param referentielFormat
   *          objet contenant les informations sur le nouveau format de
   *          fichier - paramètre obligatoire
   * @param clock
   *          horloge de la création - paramètre obligatoire
   */
  public final void create(final FormatFichier referentielFormat, final Long clock) {

    if (referentielFormat == null) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage("erreur.param"));
    }

    final String description = referentielFormat.getDescription();
    final String extension = referentielFormat.getExtension();
    final String identification = referentielFormat.getIdentificateur();
    final String idFormat = referentielFormat.getIdFormat();
    final String typeMime = referentielFormat.getTypeMime();
    final String validator = referentielFormat.getValidator();
    final Boolean autoriseGED = referentielFormat.isAutoriseGED();
    final Boolean visualisable = referentielFormat.isVisualisable();
    final String convertisseur = referentielFormat.getConvertisseur();

    final String opeRefFormatPrefix = "ajouterRefFormat";
    LOGGER.debug(DEBUT_LOG, opeRefFormatPrefix);
    LOGGER.debug("{} - Identifiant du referentielFormat : {}", new String[] {opeRefFormatPrefix, idFormat});

    final ColumnFamilyUpdater<String, String> updater = referentielFormatDao.getCfTmpl().createUpdater(idFormat);

    referentielFormatDao.addNewFormat(updater,
                                      idFormat,
                                      typeMime,
                                      extension,
                                      description,
                                      autoriseGED,
                                      visualisable,
                                      validator,
                                      identification,
                                      convertisseur,
                                      clock);

    referentielFormatDao.getCfTmpl().update(updater);

    LOGGER.info("{} - Ajout du format de fichier : {}", new String[] {opeRefFormatPrefix, idFormat});
    LOGGER.debug(FIN_LOG, opeRefFormatPrefix);

  }

  /**
   * Méthode de suppression d’un format de fichier {@link FormatFichier}
   * 
   * @param idFormat
   *          Identifiant du format de fichier {@link FormatFichier} à
   *          supprimer - paramètre obligatoire
   * @param clock
   *          horloge de suppression - paramètre obligatoire
   * @throws UnknownFormatException
   *           : le format renseigné n'existe pas en base
   */
  @SuppressWarnings("static-access")
  public final void delete(final String idFormat, final Long clock) throws UnknownFormatException {

    if (idFormat == null) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage("erreur.referentielformat.notnull"));
    }

    if (clock == null || clock <= 0) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage("erreur.param"));
    }

    final FormatFichier refFormatExistant = find(idFormat);

    if (refFormatExistant == null) {
      throw new UnknownFormatException(SaeFormatMessageHandler.getMessage("erreur.format.delete", idFormat));
    }

    try {
      final Mutator<String> mutator = referentielFormatDao.createMutator();
      referentielFormatDao.mutatorSuppressionRefFormat(mutator, idFormat, clock);
      mutator.execute();
    } catch (final Exception except) {
      throw new ReferentielRuntimeException(SaeFormatMessageHandler.getMessage("erreur.impossible.delete.format"), except);
    }
  }

  /**
   * Récupération des informations associées à un format de fichier
   * {@link FormatFichier}.
   * Si le format n'est pas trouvé en base, on renvoie null et pas une
   * exception typée pour des contraintes techniques de liées au cache Guava
   * réalisées dans la couche service
   * 
   * @param idFormat
   *          Identifiant du format de fichier à rechercher - paramètre
   *          obligatoire
   * @return Un objet {@link FormatFichier} qui représente le format de fichier
   *         trouvé
   */
  @SuppressWarnings("static-access")
  public final FormatFichier find(final String idFormat) {

    if (StringUtils.isBlank(idFormat)) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage("erreur.param.obligatoire.null", idFormat));
    }

    final ColumnFamilyResult<String, String> result = referentielFormatDao.getCfTmpl().queryColumns(idFormat);

    final FormatFichier refFormat = getRefFormatFromResult(result, idFormat);

    return refFormat;

  }

  /**
   * Construction d'un objet {@link FormatFichier} à  partir du résultat de la
   * requête
   * 
   * @param result
   *          {@link ColumnFamilyResult}
   * @param idFormat
   *          Identifiant du format de fichier à supprimer - paramètre
   *          obligatoire
   * @return {@link FormatFichier}
   * @throws UnknownFormatException
   *           : Erreur levée quand le format demandé n’existe pas au sein du
   *           référentiel
   */
  private FormatFichier getRefFormatFromResult(final ColumnFamilyResult<String, String> result, final String idFormat) {

    // pour un besoin lié au cache GUAVA
    FormatFichier refFormat = null;

    if (result != null && result.hasResults()) {

      refFormat = new FormatFichier();

      // refFormat.setIdFormat(result.getKey());
      refFormat.setIdFormat(idFormat);

      // typeMime et extension peut être null
      final String typeMime = result.getString(Constantes.COL_TYPEMIME);
      if (!StringUtils.isBlank(typeMime)) {
        refFormat.setTypeMime(typeMime);
      }

      final String extension = result.getString(Constantes.COL_EXTENSION);
      if (!StringUtils.isBlank(extension)) {
        refFormat.setExtension(extension);
      }

      refFormat.setDescription(result.getString(Constantes.COL_DESCRIPTION));

      refFormat.setAutoriseGED(result.getBoolean(Constantes.COL_AUTORISE_GED));

      refFormat.setVisualisable(result.getBoolean(Constantes.COL_VISUALISABLE));

      refFormat.setValidator(result.getString(Constantes.COL_VALIDATOR));

      refFormat.setIdentificateur(result.getString(Constantes.COL_IDENTIFIEUR));

      refFormat.setConvertisseur(result.getString(Constantes.COL_CONVERTISSEUR));

    }
    // Erreur levée quand le format demandé n’existe pas au sein du
    // référentiel

    return refFormat;

  }

  /**
   * Récupération de tous les formats de fichier présents dans le référentiel.
   * 
   * @return Une liste d’objet {@link FormatFichier} représentant les formats
   *         présents dans le référentiel
   */
  @SuppressWarnings("static-access")
  public final List<FormatFichier> findAll() {

    try {
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();

      final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory.createRangeSlicesQuery(referentielFormatDao.getKeyspace(),
                                                                                                        StringSerializer.get(),
                                                                                                        StringSerializer.get(),
                                                                                                        bytesSerializer);

      rangeSlicesQuery.setColumnFamily(referentielFormatDao.getColumnFamilyName());
      rangeSlicesQuery.setRange("", "", false, AbstractDao.DEFAULT_MAX_COLS);
      rangeSlicesQuery.setRowCount(AbstractDao.DEFAULT_MAX_ROWS);
      final QueryResult<OrderedRows<String, String, byte[]>> queryResult = rangeSlicesQuery.execute();

      // Convertion du résultat en ColumnFamilyResultWrapper pour mieux
      // l'utiliser
      final QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<>();
      final ColumnFamilyResultWrapper<String, String> result = converter.getColumnFamilyResultWrapper(queryResult,
                                                                                                      StringSerializer.get(),
                                                                                                      StringSerializer.get(),
                                                                                                      bytesSerializer);

      final HectorIterator<String, String> resultIterator = new HectorIterator<>(result);

      final List<FormatFichier> list = new ArrayList<>();
      for (final ColumnFamilyResult<String, String> row : resultIterator) {
        if (row != null && row.hasResults()) {

          final FormatFichier referentielFormat = getRefFormatFromResult(row, row.getKey());
          list.add(referentielFormat);
        }
      }

      return list;
    } catch (final Exception except) {
      throw new ReferentielRuntimeException(SaeFormatMessageHandler.getMessage("erreur.impossible.recup.info"), except);
    }
  }

}
