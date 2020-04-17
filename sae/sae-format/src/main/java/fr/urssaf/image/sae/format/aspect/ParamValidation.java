package fr.urssaf.image.sae.format.aspect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.utils.Constantes;

/**
 * Classe de validation des paramètres obligatoires.
 */
@Aspect
@Component
public class ParamValidation {

  /********************************************************* SERVICE *********************************************************************************/
  private static final String VALIDATIONSERVICE_VALIDATE_FILE = "execution(* fr.urssaf.image.sae.format.validation.service.ValidationService.validateFile(*,*))"
      + "&& args(idFormat,file)";

  private static final String VALIDATIONSERVICE_VALIDATE_STREAM = "execution(* fr.urssaf.image.sae.format.validation.service.ValidationService.validateStream(*,*))"
      + "&& args(idFormat,stream)";

  /********************************************************* DAO + SUPPORT *********************************************************************************/

  private static final String VALIDATOR_VALIDATE_FILE = "execution(* fr.urssaf.image.sae.format.validation.validators.Validator.validateFile(*))"
      + "&& args(file)";

  private static final String VALIDATOR_VALIDATE_STREAM = "execution(* fr.urssaf.image.sae.format.validation.validators.Validator.validateStream(*))"
      + "&& args(stream)";



  /**
   * Vérification des paramètres de la méthode "validateFile" de la classe
   * Validator Vérification du fichier donné en paramètre<br>
   * 
   * @param file
   *          à vérifier
   */
  @Before(VALIDATOR_VALIDATE_FILE)
  public final void validateFileFromValidator(final File file) {

    final List<String> param = new ArrayList<>();
    if (file == null) {
      param.add(Constantes.FICHIER);
    }
    if (!param.isEmpty()) {
      throw new IllegalArgumentException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler.getMessage(
                                                                                                                     Constantes.PARAM_OBLIGATOIRE,
                                                                                                                     param.toString()));
    }
    if (!file.exists()) {
      throw new IllegalArgumentException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler
                                         .getMessage(Constantes.FILE_NOT_FOUND));
    }
  }

  /**
   * Vérification des paramètres de la méthode "validateStream" de la classe
   * Validator Vérification du flux donné en paramètre<br>
   * 
   * @param stream
   *          à vérifier
   */
  @Before(VALIDATOR_VALIDATE_STREAM)
  public final void validateStreamFromValidator(final InputStream stream) {

    final List<String> param = new ArrayList<>();
    if (stream == null) {
      param.add(Constantes.STREAM);
    }
    if (stream == null) {
      throw new IllegalArgumentException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler.getMessage(
                                                                                                                     Constantes.PARAM_OBLIGATOIRE,
                                                                                                                     param.toString()));
    }
  }

  /**
   * Vérification des paramètres de la méthode "validateFile" de la classe
   * ValidationService Vérification du fichier donné en paramètre<br>
   * 
   * @param idFormat
   *          identifiant du format à valider
   * @param file
   *          à vérifier
   * @throws FileNotFoundException
   *           le fichier est introuvable
   */
  @Before(VALIDATIONSERVICE_VALIDATE_FILE)
  public final void validateFileFromValidationService(final String idFormat,
                                                      final File file)
                                                          throws FileNotFoundException {

    if (StringUtils.isBlank(idFormat)) {
      throw new IllegalArgumentException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler.getMessage(
                                                                                                                     Constantes.PARAM_OBLIGATOIRE,
                                                                                                                     Constantes.IDFORMAT));
    }
    if (file == null) {
      throw new IllegalArgumentException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler.getMessage(
                                                                                                                     Constantes.PARAM_OBLIGATOIRE,
                                                                                                                     Constantes.FICHIER));
    }
    if (!file.exists()) {
      throw new IllegalArgumentException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler
                                         .getMessage(Constantes.FILE_NOT_FOUND));
    }
  }

  /**
   * Vérification des paramètres de la méthode "validateStream" de la classe
   * ValidationService Vérification du flux donné en paramètre<br>
   * 
   * @param idFormat
   *          identifiant du format à valider
   * @param stream
   *          à vérifier
   */
  @Before(VALIDATIONSERVICE_VALIDATE_STREAM)
  public final void validateStreamFromValidationService(final String idFormat,
                                                        final InputStream stream) {

    if (StringUtils.isBlank(idFormat)) {
      throw new ReferentielRuntimeException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler
                                            .getMessage(Constantes.PARAM_OBLIGATOIRE, Constantes.IDFORMAT));
    }
    if (stream == null) {
      throw new ReferentielRuntimeException(fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler
                                            .getMessage(Constantes.PARAM_OBLIGATOIRE, Constantes.STREAM));
    }
  }

}
