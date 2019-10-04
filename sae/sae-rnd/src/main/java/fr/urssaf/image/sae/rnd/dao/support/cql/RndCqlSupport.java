package fr.urssaf.image.sae.rnd.dao.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.rnd.dao.cql.IRndDaoCql;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

/**
 * Support de manipulation de la table rndcql
 */
@Component
public class RndCqlSupport {



  private static final String FIN_LOG = "{} - fin";
  private static final String DEBUT_LOG = "{} - début";
  private static final Logger LOGGER = LoggerFactory
      .getLogger(RndCqlSupport.class);

  @Autowired
  IRndDaoCql rndDaoCql;

  /**
   * Création d'un RND dans la CF rndCql
   *
   * @param typeDoc
   *          le type de document à ajouter
   * @param clock
   *          Horloge de la création
   */
  public final void ajouterRnd(final TypeDocument typeDoc) {

    final String trcPrefix = "ajouterRnd";
    LOGGER.debug(DEBUT_LOG, trcPrefix);

    LOGGER.debug("{} - Code du type de doc : {}", new String[] { trcPrefix,
                                                                 typeDoc.getCode() });



    // Le code activité peut être null
    if (typeDoc.getCodeActivite() != null) {
      /*
       * rndDao.ecritCodeActivite(Integer.valueOf(typeDoc.getCodeActivite()),
       * updater, clock);
       */
    }
    // Si le code fonction est null, alors il s'agit d'un code temporaire et
    // on le met à 0
    String codeFonction = "0";
    if (typeDoc.getCodeFonction() == null
        && typeDoc.getType().equals(TypeCode.TEMPORAIRE)) {
      codeFonction = "0";
      typeDoc.setCodeFonction("0");
    } else {
      codeFonction = typeDoc.getCodeFonction();
    }
    rndDaoCql.saveWithMapper(typeDoc);

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


    final TypeDocument typeDoc = rndDaoCql.findWithMapperById(code).orElse(null);

    return typeDoc;
  }

  /**
   * Récupération de tous type documents présents dans le référentiel.
   * 
   * @return Une liste d’objet {@link FormatFichier} représentant les formats
   *         présents dans le référentiel
   */
  public final List<TypeDocument> findAll() {

    final Iterator<TypeDocument> it = rndDaoCql.findAllWithMapper();
    final List<TypeDocument> list = new ArrayList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }

  /**
   * Construction d'un objet {@link TypeDocument} à  partir du réultat de la
   * requête
   *
   * @param result
   *          {@link ColumnFamilyResult}
   * @return {@link TypeDocument}
   */
  /*
   * private TypeDocument getTypeDocFromResult(
   * final ColumnFamilyResult<String, String> result) {
   * TypeDocument typeDoc = null;
   * if (result != null && result.hasResults()) {
   * typeDoc = new TypeDocument();
   * typeDoc.setCode(result.getKey());
   * typeDoc.setCloture(result.getBoolean(RndDao.RND_CLOTURE));
   * // Code activité et code fonction peuvent être nul (ex code temporaire)
   * if (result.getInteger(RndDao.RND_CODE_ACTIVITE) != null) {
   * typeDoc.setCodeActivite(result.getInteger(RndDao.RND_CODE_ACTIVITE)
   * .toString());
   * }
   * if (result.getInteger(RndDao.RND_CODE_FONCTION) != null) {
   * typeDoc.setCodeFonction(result.getInteger(RndDao.RND_CODE_FONCTION)
   * .toString());
   * }
   * typeDoc.setDureeConservation(result
   * .getInteger(RndDao.RND_DUREE_CONSERVATION));
   * typeDoc.setLibelle(result.getString(RndDao.RND_LIBELLE));
   * typeDoc.setType(TypeCode.valueOf(result.getString(RndDao.RND_TYPE)).getValue());
   * }
   * return typeDoc;
   * }
   */

}
