package fr.urssaf.image.sae.format.aspect;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.Constantes;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * Classe de validation des paramètres obligatoires.
 */
@Aspect
@Component
public class ParamReferentiel {

  /********************************************************* SERVICE *********************************************************************************/

  private static final String REF_FORMAT_SERVICE_ADDFORMAT = "execution(* fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService.addFormat(*))"
      + "&& args(refFormat)";

  private static final String REF_FORMAT_SERVICE_DELETEFORMAT = "execution(* fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService.deleteFormat(*))"
      + "&& args(idFormat)";

  private static final String REF_FORMAT_SERVICE_GETFORMAT = "execution(* fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService.getFormat(*))"
      + "&& args(idFormat)";

  private static final String REF_FORMAT_SERVICE_EXISTS = "execution(* fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService.exists(*))"
      + "&& args(idFormat)";

  /********************************************************* DAO + SUPPORT *********************************************************************************/

  private static final String REF_FORMAT_SUPPORT_CREATE = "execution(* fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport.create(*,*))"
      + "&& args(referentielFormat,clock)";

  private static final String REF_FORMAT_SUPPORT_DELETE = "execution(* fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport.delete(*,*))"
      + "&& args(idFormat,clock)";

  private static final String REF_FORMAT_SUPPORT_FIND = "execution(* fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport.find(*))"
      + "&& args(idFormat)";

  /********************************************************* FACADE *********************************************************************************/

  private static final String REF_FORMAT_FACADE_CREATE = "execution(* fr.urssaf.image.sae.format.referentiel.dao.support.facade.ReferentielFormatSupportFacade.create(*))"
      + "&& args(referentielFormat)";

  private static final String REF_FORMAT_FACADE_DELETE = "execution(* fr.urssaf.image.sae.format.referentiel.dao.support.facade.ReferentielFormatSupportFacade.delete(*))"
      + "&& args(idFormat)";

  private static final String REF_FORMAT_FACADE_FIND = "execution(* fr.urssaf.image.sae.format.referentiel.dao.support.facade.ReferentielFormatSupportFacade.find(*))"
      + "&& args(idFormat)";

  /**
   * Vérification des paramètres de la méthode "addNewFormat" de la classe
   * ReferentielFormatService Vérification des attributs obligatoires de
   * l'objet ReferentielFormat donné en paramètre
   * 
   * @param refFormat
   *          le referentielFormat à ajouter
   */
  @Before(REF_FORMAT_SERVICE_ADDFORMAT)
  public final void validAddNewFormat(final FormatFichier refFormat) {

    if (refFormat == null) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            Constantes.PARAM_OBLIGATOIRE,
                                                                            Constantes.REFERENTIEL_FORMAT));
    } else {
      final List<String> variable = getAttributsNull(refFormat, Long.valueOf(1));

      if (!variable.isEmpty()) {
        throw new IllegalArgumentException(
                                           SaeFormatMessageHandler.getMessage(
                                                                              Constantes.PARAM_OBLIGATOIRE,
                                                                              variable.toString()));
      }
    }

  }

  /**
   * Vérification des paramètres de la méthode "deleteFormat" de la classe
   * ReferentielFormatService L'identifiant format ne doit n'y être vide ni
   * null.
   * 
   * @param idFormat
   *          l'identifiant du format à supprimer
   */
  @Before(REF_FORMAT_SERVICE_DELETEFORMAT)
  public final void validDeleteFormat(final String idFormat) {

    if (StringUtils.isBlank(idFormat)) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            Constantes.PARAM_OBLIGATOIRE,
                                                                            Constantes.IDFORMAT));
    }

  }

  /**
   * Vérification des paramètres de la méthode "getFormat" de la classe
   * ReferentielFormatService Vérification du String idFormat donné en
   * paramètre<br>
   * 
   * @param idFormat
   *          le referentielFormat à recuperer
   */
  @Before(REF_FORMAT_SERVICE_GETFORMAT)
  public final void validGetFormat(final String idFormat) {

    if (StringUtils.isBlank(idFormat)) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            Constantes.PARAM_OBLIGATOIRE,
                                                                            Constantes.IDFORMAT));
    }
  }

  /**
   * Vérification des paramètres de la méthode "getFormat" de la classe
   * ReferentielFormatService Vérification du String idFormat donné en
   * paramètre<br>
   * 
   * @param idFormat
   *          le referentielFormat à recuperer
   */
  @Before(REF_FORMAT_SERVICE_EXISTS)
  public final void validExists(final String idFormat) {

    if (StringUtils.isBlank(idFormat)) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            Constantes.PARAM_OBLIGATOIRE,
                                                                            Constantes.IDFORMAT));
    }
  }

  /**
   * Vérification des paramètres de la méthode "create" de la classe
   * ReferentielFormatSupport Vérification de l'objet obligatoire
   * ReferentielFormat donné en paramètre<br>
   * ainsi que de l'heure
   * 
   * @param referentielFormat
   *          : le referentielFormat à ajouter
   * @param clock
   *          : horloge de la création
   */
  @Before(REF_FORMAT_SUPPORT_CREATE)
  public final void validCreate(final FormatFichier referentielFormat, final Long clock) {

    final List<String> param = getAttributsNull(referentielFormat, clock);

    if (!param.isEmpty()) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            Constantes.PARAM_OBLIGATOIRE,
                                                                            param.toString()));
    }
  }

  /**
   * Vérification des paramètres de la méthode "delete" de la classe
   * ReferentielFormatSupport Vérification du String idFormat donné en
   * paramètre<br>
   * ainsi que de l'heure
   * 
   * @param idFormat
   *          le referentielFormat à supprimer
   * @param clock
   *          horloge de la création
   */
  @Before(REF_FORMAT_SUPPORT_DELETE)
  public final void validDelete(final String idFormat, final Long clock) {

    final List<String> variable = getParamNull(idFormat, clock);

    if (!variable.isEmpty()) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            Constantes.PARAM_OBLIGATOIRE,
                                                                            variable.toString()));
    }
  }

  /**
   * Vérification des paramètres de la méthode "find" de la classe
   * ReferentielFormatSupport Vérification du String idFormat donné en
   * paramètre<br>
   * 
   * @param idFormat
   *          le referentielFormat à recuperer
   */
  @Before(REF_FORMAT_SUPPORT_FIND)
  public final void validFind(final String idFormat) {
    if (StringUtils.isBlank(idFormat)) {
      final List<String> param = new ArrayList<>();
      param.add(Constantes.IDFORMAT);
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            Constantes.PARAM_OBLIGATOIRE,
                                                                            param.toString()));
    }
  }

  private List<String> getAttributsNull(final FormatFichier referentielFormat,
                                        final Long clock) {

    final List<String> variable = new ArrayList<>();

    if (referentielFormat == null) {
      variable.add(Constantes.REFERENTIEL_FORMAT);
    }
    if (clock == null || clock <= 0) {
      variable.add(Constantes.CLOCK);
    }
    if (referentielFormat != null) {
      final String description = referentielFormat.getDescription();
      // String identification = referentielFormat.getIdentificateur();
      final String idFormat = referentielFormat.getIdFormat();
      // String validator = referentielFormat.getValidator();
      final Boolean visualisable = referentielFormat.isVisualisable();

      if (StringUtils.isBlank(idFormat)) {
        variable.add(Constantes.IDFORMAT);
      }
      if (StringUtils.isBlank(description)) {
        variable.add(Constantes.DESCRIPTION);
      }
      if (visualisable == null) {
        variable.add(Constantes.VISUALISABLE);
      }
      // if (StringUtils.isBlank(validator)) {
      // variable.add(Constantes.VALIDATOR);
      // }
      // if (StringUtils.isBlank(identification)) {
      // variable.add(Constantes.IDENTIFICATION);
      // }
    }
    return variable;
  }

  private List<String> getParamNull(final String idFormat, final Long clock) {
    final List<String> variable = new ArrayList<>();

    if (StringUtils.isBlank(idFormat)) {
      variable.add(Constantes.IDFORMAT);
    }
    if (clock == null || clock <= 0) {
      variable.add(Constantes.CLOCK);
    }
    return variable;
  }

  /**
   * Vérification des paramètres de la méthode "create" de la classe
   * ReferentielFormatSupportFacade Vérification de l'objet obligatoire
   * ReferentielFormat donné en paramètre<br>
   * ainsi que de l'heure
   * 
   * @param referentielFormat
   *          : le referentielFormat à ajouter
   */
  @Before(REF_FORMAT_FACADE_CREATE)
  public final void validFacadeCreate(final FormatFichier referentielFormat) {
    if (referentielFormat == null || referentielFormat.getIdFormat() == null) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            Constantes.PARAM_OBLIGATOIRE,
                                                                            Constantes.REFERENTIEL_FORMAT));
    }
  }

  /**
   * Vérification des paramètres de la méthode "delete" de la classe
   * ReferentielFormatSupportFacade Vérification du String idFormat donné en
   * paramètre<br>
   * ainsi que de l'heure
   * 
   * @param idFormat
   *          le referentielFormat à supprimer
   */
  @Before(REF_FORMAT_FACADE_DELETE)
  public final void validFacadeDelete(final String idFormat) {

    if (StringUtils.isBlank(idFormat)) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            Constantes.PARAM_OBLIGATOIRE,
                                                                            Constantes.IDFORMAT.toString()));
    }
  }

  /**
   * Vérification des paramètres de la méthode "find" de la classe
   * ReferentielFormatSupportFacade Vérification du String idFormat donné en
   * paramètre<br>
   * 
   * @param idFormat
   *          le referentielFormat à recuperer
   */
  @Before(REF_FORMAT_FACADE_FIND)
  public final void validFacadeFind(final String idFormat) {
    if (StringUtils.isBlank(idFormat)) {
      throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
                                                                            Constantes.PARAM_OBLIGATOIRE,
                                                                            Constantes.IDFORMAT.toString()));
    }
  }
}
