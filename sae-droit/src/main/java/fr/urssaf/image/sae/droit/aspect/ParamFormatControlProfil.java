package fr.urssaf.image.sae.droit.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.utils.Constantes;
import fr.urssaf.image.sae.droit.utils.EnumValidationMode;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Classe de validation des paramètres obligatoires pour les classes à PAGMF.
 */
@Aspect
@Component
public class ParamFormatControlProfil {

  /********************************************************* SERVICE *********************************************************************************/

  private static final String FORMAT_SERVICE_ADDFORMATCONTROL = "execution(* fr.urssaf.image.sae.droit.service.FormatControlProfilService.addFormatControlProfil(*))"
      + "&& args(formatControlProfil)";

  private static final String FORMAT_SERVICE_DELETEFORMATCONTROL = "execution(* fr.urssaf.image.sae.droit.service.FormatControlProfilService.deleteFormatControlProfil(*))"
      + "&& args(codeFormatControlProfil)";

  private static final String FORMAT_SERVICE_GETFORMATCONTROL = "execution(* fr.urssaf.image.sae.droit.service.FormatControlProfilService.getFormatControlProfil(*))"
      + "&& args(codeFormatControlProfil)";

  /********************************************************* SUPPORT *********************************************************************************/

  private static final String FORMAT_SERVICE_CREATE = "execution(* fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport.create(*,*))"
      + "&& args(profil,clock)";

  private static final String FORMAT_SERVICE_DELETE = "execution(* fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport.delete(*,*))"
      + "&& args(code,clock)";

  private static final String FORMAT_SERVICE_FIND = "execution(* fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport.find(*))"
      + "&& args(code)";

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport#create(FormatControlProfil, long)}
   * <br>
   * Vérification de l'objet obligatoire Profil donné en paramètre ainsi que de
   * l'heure
   * 
   * @param profil
   *          : le profil à ajouter
   * @param clock
   *          : horloge de la création
   */
  @Before(FORMAT_SERVICE_CREATE)
  public final void validCreate(final FormatControlProfil profil, final Long clock) {

    final List<String> param = getAttributsNull(profil, clock);

    if (!param.isEmpty()) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           Constantes.PARAM_OBLIGATOIRE,
                                                                           param.toString()));
    }
  }

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport#delete(String, Long)}
   * <br>
   * Vérification du String code donné en paramètre ainsi que de l'heure
   * 
   * @param code
   *          le code du controle profil à supprimer
   * @param clock
   *          horloge de la création
   */
  @Before(FORMAT_SERVICE_DELETE)
  public final void validDelete(final String code, final Long clock) {

    final List<String> variable = getParamNull(code, clock);

    if (!variable.isEmpty()) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           Constantes.PARAM_OBLIGATOIRE,
                                                                           variable.toString()));
    }
  }

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport#find(String)}
   * <br>
   * Vérification du String code donné en paramètre
   * 
   * @param code
   *          le code du controle profil à recuperer
   */
  @Before(FORMAT_SERVICE_FIND)
  public final void validFind(final String code) {

    if (StringUtils.isBlank(code)) {
      final List<String> param = new ArrayList<>();
      param.add(Constantes.COL_CODEFORMATCONTROLPROFIL);
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           Constantes.PARAM_OBLIGATOIRE,
                                                                           param.toString()));
    }
  }

  /********************************************** SERVICE ******************************************/

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.service.FormatControlProfilService#addFormatControlProfil(FormatControlProfil)}
   * <br>
   * Vérification des attributs obligatoires de l'objet
   * {@link FormatControlProfil} donné en paramètre
   * 
   * @param formatControlProfil
   *          le formatControlProfil à ajouter
   */
  @Before(FORMAT_SERVICE_ADDFORMATCONTROL)
  public final void addFormatControlProfil(
                                           final FormatControlProfil formatControlProfil) {

    if (formatControlProfil == null) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           Constantes.PARAM_OBLIGATOIRE,
                                                                           "formatControlProfil"));
    } else {
      final List<String> variable = getAttributsNull(formatControlProfil,
                                                     Long
                                                         .valueOf(1));

      if (!variable.isEmpty()) {
        throw new IllegalArgumentException(ResourceMessagesUtils
                                                                .loadMessage(Constantes.PARAM_OBLIGATOIRE,
                                                                             variable
                                                                                     .toString()));
      }
    }

  }

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.service.FormatControlProfilService#deleteFormatControlProfil(String)}
   * <br>
   * Le codeFormatControlProfil ne doit n'y être vide ni null.
   * 
   * @param codeFormatControlProfil
   *          le code du formatControlProfil à supprimer
   */
  @Before(FORMAT_SERVICE_DELETEFORMATCONTROL)
  public final void deleteFormatControlProfil(final String codeFormatControlProfil) {

    if (StringUtils.isBlank(codeFormatControlProfil)) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           Constantes.PARAM_OBLIGATOIRE,
                                                                           "codeFormatControlProfil"));
    }

  }

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.service.FormatControlProfilService#getFormatControlProfil(String)}
   * <br>
   * Vérification du String codeFormatControlProfil donné en paramètre
   * 
   * @param codeFormatControlProfil
   *          le FormatControlProfil à recuperer
   */
  @Before(FORMAT_SERVICE_GETFORMATCONTROL)
  public final void getFormatControlProfil(final String codeFormatControlProfil) {

    if (StringUtils.isBlank(codeFormatControlProfil)) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           Constantes.PARAM_OBLIGATOIRE,
                                                                           "codeFormatControlProfil"));
    }
  }

  private List<String> getAttributsNull(final FormatControlProfil profil, final Long clock) {

    final List<String> variable = new ArrayList<>();
    final List<String> doNotValidateList = Arrays.asList(Constantes.AUCUN.toUpperCase(),
                                                         Constantes.NONE.toUpperCase());

    if (profil == null) {
      variable.add("profil");
    }
    if (clock == null || clock <= 0) {
      variable.add(Constantes.CLOCK);
    }
    if (profil != null) {
      final String description = profil.getDescription();
      final String code = profil.getFormatCode();
      final FormatProfil formatProfil = profil.getControlProfil();

      if (StringUtils.isBlank(code)) {
        variable.add(Constantes.COL_CODEPROFIL);
      }
      if (StringUtils.isBlank(description)) {
        variable.add(Constantes.COL_DESCRIPTION);
      }
      if (formatProfil == null) {
        variable.add(Constantes.COL_CONTROLPROFIL);
      }

      if (formatProfil != null) {
        final boolean validation = formatProfil.isFormatValidation();
        final boolean identification = formatProfil.isFormatIdentification();
        final String validationMode = formatProfil.getFormatValidationMode();

        if (validation || identification) {
          if (!EnumValidationMode.contains(validationMode)) {

            throw new IllegalArgumentException(
                                               ResourceMessagesUtils
                                                                    .loadMessage("erreur.param.format.valid.mode.obligatoire"));
          }
        } else {
          if (!StringUtils.isBlank(validationMode)
              && !doNotValidateList.contains(StringUtils
                                                        .upperCase(validationMode))) {
            throw new IllegalArgumentException(
                                               ResourceMessagesUtils
                                                                    .loadMessage("erreur.param.format.valid.mode.renseigne"));
          }
        }
      }
    }
    return variable;
  }

  private List<String> getParamNull(final String codeFormatProfil, final Long clock) {
    final List<String> variable = new ArrayList<>();

    if (StringUtils.isBlank(codeFormatProfil)) {
      variable.add(Constantes.COL_CODEFORMATCONTROLPROFIL);
    }
    if (clock == null || clock <= 0) {
      variable.add(Constantes.CLOCK);
    }
    return variable;
  }

}
