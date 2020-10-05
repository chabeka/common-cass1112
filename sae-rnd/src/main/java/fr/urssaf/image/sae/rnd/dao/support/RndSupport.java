package fr.urssaf.image.sae.rnd.dao.support;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.rnd.dao.RndDao;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * Support de manipulation de la CF Rnd
 *
 *
 */
@Component
public class RndSupport {

  private final RndDao rndDao;

  private static final String FIN_LOG = "{} - fin";
  private static final String DEBUT_LOG = "{} - début";
  private static final Logger LOGGER = LoggerFactory
      .getLogger(RndSupport.class);

  /**
   * Constructeur
   *
   * @param rndDao
   *           DAO d'accès à la CF Rnd
   */
  @Autowired
  public RndSupport(final RndDao rndDao) {
    this.rndDao = rndDao;
  }

  /**
   * Création d'un RND dans la CF Rnd
   *
   * @param typeDoc
   *           le type de document à ajouter
   * @param clock
   *           Horloge de la création
   */
  public final void ajouterRnd(final TypeDocument typeDoc, final long clock) {

    final String trcPrefix = "ajouterRnd";
    LOGGER.debug(DEBUT_LOG, trcPrefix);

    LOGGER.debug("{} - Code du type de doc : {}", new String[] { trcPrefix,
                                                                 typeDoc.getCode() });

    final ColumnFamilyUpdater<String, String> updater = rndDao.getCfTmpl()
        .createUpdater(typeDoc.getCode());

    rndDao.ecritCloture(typeDoc.isCloture(), updater, clock);

    // Le code activité peut être null
    if (typeDoc.getCodeActivite() != null) {
      rndDao.ecritCodeActivite(Integer.valueOf(typeDoc.getCodeActivite()),
                               updater, clock);
    }
    // Si le code fonction est null, alors il s'agit d'un code temporaire et
    // on le met à 0
    String codeFonction = "0";
    if (typeDoc.getCodeFonction() == null
        && typeDoc.getType().equals(TypeCode.TEMPORAIRE)) {
      codeFonction = "0";
    } else {
      codeFonction = typeDoc.getCodeFonction();
    }
    rndDao.ecritCodeFonction(Integer.valueOf(codeFonction), updater, clock);

    rndDao.ecritDureeConservation(Integer.valueOf(typeDoc
                                                  .getDureeConservation()), updater, clock);
    rndDao.ecritLibelle(typeDoc.getLibelle(), updater, clock);
    rndDao.ecritType(typeDoc.getType().toString(), updater, clock);

    rndDao.getCfTmpl().update(updater);

    LOGGER.info("{} - Ajout du code : {}", new String[] { trcPrefix,
                                                          typeDoc.getCode() });

    LOGGER.debug(FIN_LOG, trcPrefix);
  }

  /**
   * Récupère le type de document correspondant au code passé en paramètre
   *
   * @param code
   *           le code RND dont on veut le type de document
   * @return le type de document recherché
   */
  public final TypeDocument getRnd(final String code) {
    final ColumnFamilyResult<String, String> result = rndDao.getCfTmpl()
        .queryColumns(code);

    final TypeDocument typeDoc = getTypeDocFromResult(result);

    return typeDoc;
  }

  /**
   * Construction d'un objet {@link TypeDocument} à  partir du réultat de la
   * requête
   *
   * @param result
   *           {@link ColumnFamilyResult}
   * @return {@link TypeDocument}
   */
  private TypeDocument getTypeDocFromResult(
                                            final ColumnFamilyResult<String, String> result) {
    TypeDocument typeDoc = null;
    if (result != null && result.hasResults()) {
      typeDoc = new TypeDocument();

      typeDoc.setCode(result.getKey());
      typeDoc.setCloture(result.getBoolean(RndDao.RND_CLOTURE));

      // Code activité et code fonction peuvent être nul (ex code temporaire)
      if (result.getInteger(RndDao.RND_CODE_ACTIVITE) != null) {
        typeDoc.setCodeActivite(result.getInteger(RndDao.RND_CODE_ACTIVITE)
                                .toString());
      }
      if (result.getInteger(RndDao.RND_CODE_FONCTION) != null) {
        typeDoc.setCodeFonction(result.getInteger(RndDao.RND_CODE_FONCTION)
                                .toString());
      }
      typeDoc.setDureeConservation(result
                                   .getInteger(RndDao.RND_DUREE_CONSERVATION));
      typeDoc.setLibelle(result.getString(RndDao.RND_LIBELLE));
      typeDoc.setType(TypeCode.valueOf(result.getString(RndDao.RND_TYPE)));
    }
    return typeDoc;
  }

  /**
   * Récupération de tous les rnd.
   * 
   * @return Une liste d’objet {@link TypeDocument} représentant les rnd
   */
  public final List<TypeDocument> findAll() {

    try {
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();

      final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
          .createRangeSlicesQuery(rndDao.getKeyspace(),
                                  StringSerializer.get(),
                                  StringSerializer.get(),
                                  bytesSerializer);

      rangeSlicesQuery.setColumnFamily(rndDao.getColumnFamilyName());
      rangeSlicesQuery.setRange("", "", false, AbstractDao.DEFAULT_MAX_COLS);
      rangeSlicesQuery.setRowCount(AbstractDao.DEFAULT_MAX_ROWS);
      final QueryResult<OrderedRows<String, String, byte[]>> queryResult = rangeSlicesQuery
          .execute();

      // Convertion du résultat en ColumnFamilyResultWrapper pour mieux
      // l'utiliser
      final QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<>();
      final ColumnFamilyResultWrapper<String, String> result = converter
          .getColumnFamilyResultWrapper(queryResult,
                                        StringSerializer
                                        .get(),
                                        StringSerializer.get(),
                                        bytesSerializer);

      final HectorIterator<String, String> resultIterator = new HectorIterator<>(
          result);

      final List<TypeDocument> list = new ArrayList<>();
      for (final ColumnFamilyResult<String, String> row : resultIterator) {
        if (row != null && row.hasResults()) {

          final TypeDocument typeDocument = getTypeDocFromResult(row);
          list.add(typeDocument);
        }
      }

      return list;
    }
    catch (final Exception except) {

      throw except;
      /*
       * new RndRuntimeException(SaeFormatMessageHandler
       * .getMessage("erreur.impossible.recup.info"),
       * except);
       */
    }
  }
}
