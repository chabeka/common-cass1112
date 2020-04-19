package fr.urssaf.image.sae.format.referentiel.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.format.referentiel.dao.ReferentielFormatDao;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.exceptions.UnknownParameterException;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * 
 * Classe utilisée pour générer les RuntimeExceptions.
 * 
 */
@Component
public class ReferentielFormatSupportBouchon {

  private final ReferentielFormatDao referentielFormatDao;

  /**
   * Constructeur
   * 
   * @param referentielFormatDao
   *           : DAO
   */
  @Autowired
  public ReferentielFormatSupportBouchon(
                                         final ReferentielFormatDao referentielFormatDao) {
    this.referentielFormatDao = referentielFormatDao;
  }

  /**
   * Utilisé seulement pour les tests
   * 
   * @param idFormat
   *           identifiant du format
   * @return formatFichier
   */
  public final FormatFichier find(final String idFormat) {

    if (StringUtils.isBlank(idFormat)) {
      throw new IllegalArgumentException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler.getMessage(
                                                                                                                     "erreur.param.obligatoire.null", idFormat));
    }

    final FormatFichier refFormat = new FormatFichier();
    refFormat.setIdFormat("test");
    refFormat.setTypeMime("application/test");
    refFormat.setExtension("Test");
    refFormat.setDescription("Test, simplement pour les tests");
    refFormat.setVisualisable(true);
    refFormat.setValidator("TestValidatorImpl");
    refFormat.setIdentificateur("TestIdentifierImpl");

    return refFormat;

  }

  /**
   * Méthode de suppression d’un format de fichier {@link FormatFichier}
   * 
   * @param idFormat
   *           Identifiant du format de fichier {@link FormatFichier} à
   *           supprimer - paramètre obligatoire
   * @param clock
   *           horloge de suppression - paramètre obligatoire
   */
  public final void delete(final String idFormat, final Long clock) {

    if (idFormat == null) {
      throw new IllegalArgumentException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler
                                         .getMessage("erreur.referentielformat.notnull"));
    }

    if (clock == null || clock <= 0) {
      throw new IllegalArgumentException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler
                                         .getMessage("erreur.param"));
    }

    final FormatFichier refFormatExistant = find(idFormat);

    if (refFormatExistant == null) {
      throw new UnknownParameterException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler
                                          .getMessage("erreur.format.delete", idFormat));
    }

    try {
      final Mutator<String> mutator = referentielFormatDao.createMutator();
      referentielFormatDao.mutatorSuppressionRefFormat(mutator, idFormat,
                                                       clock);
      mutator.execute();
    } catch (final Exception except) {
      throw new ReferentielRuntimeException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler
                                            .getMessage("erreur.impossible.delete.format"), except);
    }
  }

}
