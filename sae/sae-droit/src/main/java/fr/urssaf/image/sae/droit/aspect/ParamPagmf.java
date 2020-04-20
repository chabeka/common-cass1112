package fr.urssaf.image.sae.droit.aspect;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.utils.Constantes;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Classe de validation des paramètres obligatoires pour les classes à PAGMF.
 */
@Aspect
@Component
public class ParamPagmf {

  /********************************************************* SERVICE *********************************************************************************/

  private static final String PAGMF_SERVICE_ADDFORMAT = "execution(* fr.urssaf.image.sae.droit.service.SaePagmfService.addPagmf(*))"
      + "&& args(pagmf)";

  private static final String PAGMF_SERVICE_DELETEFORMAT = "execution(* fr.urssaf.image.sae.droit.service.SaePagmfService.deletePagmf(*))"
      + "&& args(codePagmf)";

  private static final String PAGMF_SERVICE_GETFORMAT = "execution(* fr.urssaf.image.sae.droit.service.SaePagmfService.getPagmf(*))"
      + "&& args(codePagmf)";

  /********************************************************* SUPPORT *********************************************************************************/

  private static final String PAGMF_SUPPORT_CREATE = "execution(* fr.urssaf.image.sae.droit.dao.support.PagmfSupport.create(*,*))"
      + "&& args(pagmf,clock)";

  private static final String PAGMF_SUPPORT_DELETE = "execution(* fr.urssaf.image.sae.droit.dao.support.PagmfSupport.delete(*,*))"
      + "&& args(code,clock)";

  private static final String PAGMF_SUPPORT_FIND = "execution(* fr.urssaf.image.sae.droit.dao.support.PagmfSupport.find(*))"
      + "&& args(code)";

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.dao.support.PagmfSupport#create(Pagmf, long)}
   * <br>
   * Vérification de l'objet obligatoire Pagmf donné en paramètre ainsi que de
   * l'heure
   * 
   * @param pagmf
   *          : le pagmf à ajouter
   * @param clock
   *          : horloge de la création
   */
  @Before(PAGMF_SUPPORT_CREATE)
  public final void validCreate(final Pagmf pagmf, final Long clock) {

    final List<String> param = getAttributsNull(pagmf, clock);

    if (!param.isEmpty()) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           Constantes.PARAM_OBLIGATOIRE,
                                                                           param.toString()));
    }
  }

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.dao.support.PagmfSupport#delete(String, Long)}
   * <br>
   * Vérification du String codePagmf donné en paramètre ainsi que de l'heure
   * 
   * @param code
   *          le code du pagmf à supprimer
   * @param clock
   *          horloge de la création
   */
  @Before(PAGMF_SUPPORT_DELETE)
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
   * {@link fr.urssaf.image.sae.droit.dao.support.PagmfSupport#find(String)}<br>
   * Vérification du String codePagmf donné en paramètre
   * 
   * @param code
   *          le code du pagmf à recuperer
   */
  @Before(PAGMF_SUPPORT_FIND)
  public final void validFind(final String code) {

    if (StringUtils.isBlank(code)) {
      final List<String> param = new ArrayList<>();
      param.add(Constantes.COL_CODEPAGMF);
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           Constantes.PARAM_OBLIGATOIRE,
                                                                           param.toString()));
    }
  }

  /********************************************** SERVICE ******************************************/

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.service.SaePagmfService#addPagmf(Pagmf)}<br>
   * Vérification des attributs obligatoires de l'objet Pagmf donné en
   * paramètre
   * 
   * @param pagmf
   *          le Pagmf à ajouter
   */
  @Before(PAGMF_SERVICE_ADDFORMAT)
  public final void addPagmf(final Pagmf pagmf) {

    if (pagmf == null) {
      throw new DroitRuntimeException(ResourceMessagesUtils.loadMessage(
                                                                        Constantes.PARAM_OBLIGATOIRE,
                                                                        "pagmf"));
    } else {
      final List<String> variable = getAttributsNull(pagmf, Long.valueOf(1));

      if (!variable.isEmpty()) {
        throw new DroitRuntimeException(ResourceMessagesUtils.loadMessage(
                                                                          Constantes.PARAM_OBLIGATOIRE,
                                                                          variable.toString()));
      }
    }

  }

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.service.SaePagmfService#deletePagmf(String)}
   * <br>
   * Le codePagmf ne doit n'y être vide ni null.
   * 
   * @param codePagmf
   *          le Pagmf à supprimer
   */
  @Before(PAGMF_SERVICE_DELETEFORMAT)
  public final void deletePagmf(final String codePagmf) {

    if (StringUtils.isBlank(codePagmf)) {
      throw new DroitRuntimeException(ResourceMessagesUtils.loadMessage(
                                                                        Constantes.PARAM_OBLIGATOIRE,
                                                                        "codePagmf"));
    }

  }

  /**
   * Vérification des paramètres de la méthode
   * {@link fr.urssaf.image.sae.droit.service.SaePagmfService#getPagmf(String)}<br>
   * Vérification du String codePagmf donné en paramètre
   * 
   * @param codePagmf
   *          le Pagmf à recuperer
   */
  @Before(PAGMF_SERVICE_GETFORMAT)
  public final void getPagmf(final String codePagmf) {

    if (StringUtils.isBlank(codePagmf)) {
      throw new DroitRuntimeException(ResourceMessagesUtils.loadMessage(
                                                                        Constantes.PARAM_OBLIGATOIRE,
                                                                        "codePagmf"));
    }
  }

  private List<String> getAttributsNull(final Pagmf pagmf, final Long clock) {

    final List<String> variable = new ArrayList<>();

    if (pagmf == null) {
      variable.add("pagmf");
    }
    if (clock == null || clock <= 0) {
      variable.add(Constantes.CLOCK);
    }
    if (pagmf != null) {
      final String description = pagmf.getDescription();
      final String codeFormatControlProfil = pagmf.getCodeFormatControlProfil();
      final String codePagmf = pagmf.getCodePagmf();

      if (StringUtils.isBlank(codePagmf)) {
        variable.add(Constantes.COL_CODEPAGMF);
      }
      if (StringUtils.isBlank(description)) {
        variable.add(Constantes.COL_DESCRIPTION);
      }
      if (StringUtils.isBlank(codeFormatControlProfil)) {
        variable.add(Constantes.COL_CODEFORMATCONTROLPROFIL);
      }
    }
    return variable;
  }

  private List<String> getParamNull(final String codePagmf, final Long clock) {
    final List<String> variable = new ArrayList<>();

    if (StringUtils.isBlank(codePagmf)) {
      variable.add(Constantes.COL_CODEPAGMF);
    }
    if (clock == null || clock <= 0) {
      variable.add(Constantes.CLOCK);
    }
    return variable;
  }

}
