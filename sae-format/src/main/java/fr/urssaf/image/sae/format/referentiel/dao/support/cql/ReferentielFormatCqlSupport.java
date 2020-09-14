package fr.urssaf.image.sae.format.referentiel.dao.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.ReferentielFormatDao;
import fr.urssaf.image.sae.format.referentiel.dao.cql.IReferentielFormatDaoCql;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;


/***
 * 
 * Classe support permettant d’exploiter le bean {@link FormatFichier} <br>
 * et la DAO {@link ReferentielFormatDao} du référentiel des formats.<br>
 * 
 * Classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "ReferentielFormat" *
 */
@Component
public class ReferentielFormatCqlSupport {


  private static final String FIN_LOG = "{} - fin";
  private static final String DEBUT_LOG = "{} - début";
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ReferentielFormatCqlSupport.class);

  /**
   * Constructeur de la classe support
   * 
   * @param referentielFormatDao
   *           la dao
   */
  @Autowired
  IReferentielFormatDaoCql referentielFormatDaoCql;

  public ReferentielFormatCqlSupport(final IReferentielFormatDaoCql referentielFormatDaoCql) {
    this.referentielFormatDaoCql = referentielFormatDaoCql;
  }
  /**
   * Ajoute un nouveau format de fichier {@link FormatFichier}.
   * 
   * @param referentielFormat
   *           objet contenant les informations sur le nouveau format de
   *           fichier - paramètre obligatoire
   */
  public final void create(final FormatFichier referentielFormat) {

    if (referentielFormat == null) {
      throw new IllegalArgumentException(SaeFormatMessageHandler
                                         .getMessage("erreur.param"));
    }


    final String opeRefFormatPrefix = "ajouterRefFormat";
    LOGGER.debug(DEBUT_LOG, opeRefFormatPrefix);
    LOGGER.debug("{} - Identifiant du referentielFormat : {}", new String[] {
                                                                             opeRefFormatPrefix, referentielFormat.getIdFormat()});
    saveOrUpdate(referentielFormat);

    LOGGER.info("{} - Ajout du format de fichier : {}", new String[] {
                                                                      opeRefFormatPrefix, referentielFormat.getIdFormat()});
    LOGGER.debug(FIN_LOG, opeRefFormatPrefix);

  }

  /**
   * Méthode de suppression d’un format de fichier {@link FormatFichier}
   * 
   * @param idFormat
   *           Identifiant du format de fichier {@link FormatFichier} à
   *           supprimer - paramètre obligatoire
   * @throws UnknownFormatException
   *            : le format renseigné n'existe pas en base
   */
  public final void delete(final String idFormat)
      throws UnknownFormatException {

    if (idFormat == null) {
      throw new IllegalArgumentException(SaeFormatMessageHandler
                                         .getMessage("erreur.referentielformat.notnull"));
    }


    final FormatFichier refFormatExistant = find(idFormat);

    if (refFormatExistant == null) {
      throw new UnknownFormatException(SaeFormatMessageHandler.getMessage(
                                                                          "erreur.format.delete", idFormat));
    }

    try {
      referentielFormatDaoCql.delete(refFormatExistant);
    } catch (final Exception except) {
      throw new ReferentielRuntimeException(SaeFormatMessageHandler
                                            .getMessage("erreur.impossible.delete.format"), except);
    }
  }

  /**
   * Récupération des informations associées à un format de fichier
   * {@link FormatFichier}.
   * 
   * Si le format n'est pas trouvé en base, on renvoie null et pas une
   * exception typée pour des contraintes techniques de liées au cache Guava
   * réalisées dans la couche service
   * 
   * @param idFormat
   *           Identifiant du format de fichier à rechercher - paramètre
   *           obligatoire
   * @return Un objet {@link FormatFichier} qui représente le format de fichier
   *         trouvé
   * */
  public final FormatFichier find(final String idFormat) {

    if (StringUtils.isBlank(idFormat)) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            "erreur.param.obligatoire.null", idFormat));
    }

    final FormatFichier refFormat = referentielFormatDaoCql.findWithMapperById(idFormat).orElse(null);

    return refFormat;

  }



  /**
   * Récupération de tous les formats de fichier présents dans le référentiel.
   * 
   * 
   * @return Une liste d’objet {@link FormatFichier} représentant les formats
   *         présents dans le référentiel
   */
  public final List<FormatFichier> findAll() {

    final Iterator<FormatFichier> it = referentielFormatDaoCql.findAllWithMapper();
    final List<FormatFichier> list = new ArrayList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }

  /**
   * Sauvegarde d'un formatFichier
   * 
   * @param formatFichier
   */
  private void saveOrUpdate(final FormatFichier formatFichier) {
    Assert.notNull(formatFichier, "l'objet formatFichier ne peut etre null");
    Assert.notNull(formatFichier.getIdFormat(), "L'identifiant ne peut être null");
    referentielFormatDaoCql.saveWithMapper(formatFichier);

  }
}
