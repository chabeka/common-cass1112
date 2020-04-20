package fr.urssaf.image.sae.format.aspect;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.utils.Constantes;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * Classe de validation des paramètres obligatoires.
 */
@Aspect
@Component
public class ParamIdentification {

  private static final String IDENTIFIER_SERVICE_IDENTIFYFILE = "execution(* fr.urssaf.image.sae.format.identification.service.IdentificationService.identifyFile(*,*))"
      + "&& args(idFormat,fichier)";

  private static final String IDENTIFIER_SERVICE_IDENTIFYSTREAM = "execution(* fr.urssaf.image.sae.format.identification.service.IdentificationService.identifyStream(*,*,*))"
      + "&& args(idFormat,stream,nomFichier)";

  private static final String IDENTIFIER_IDENTIFYFILE = "execution(* fr.urssaf.image.sae.format.identification.identifiers.Identifier.identifyFile(*,*))"
      + "&& args(idFormat,fichier)";

  private static final String IDENTIFIER_IDENTIFYSTREAM = "execution(* fr.urssaf.image.sae.format.identification.identifiers.Identifier.identifyStream(*,*,*))"
      + "&& args(idFormat,stream,nomFichier)";


  /**
   * Vérification des paramètres de la méthode "identifyFile" de la classe
   * IdentifierService Vérification du String idFormat donné en paramètre<br>
   * 
   * @param idFormat
   *          identifiant du format souhaité
   * @param fichier
   *          le fichier à identifier
   */
  @Before(IDENTIFIER_SERVICE_IDENTIFYFILE)
  public final void validIdentifyFileFromIdentifierService(final String idFormat,
                                                           final File fichier) {

    genererExceptionFile(idFormat, fichier);
  }

  /**
   * Vérification des paramètres de la méthode "identifyStream" de la classe
   * IdentifierService Vérification du String idFormat donné en paramètre<br>
   * 
   * @param idFormat
   *          identifiant du format souhaité
   * @param stream
   *          le flux à identifier
   * @param nomFichier
   *          le nom du fichier correspondant au stream
   */
  @Before(IDENTIFIER_SERVICE_IDENTIFYSTREAM)
  public final void validIdentifyStreamFromIdentifierService(final String idFormat,
                                                             final InputStream stream, final String nomFichier) {

    genererExceptionStream(idFormat, stream, nomFichier);
  }

  /**
   * Vérification des paramètres de la méthode "identifyFile" de la classe
   * Identifier Vérification du String idFormat donné en paramètre<br>
   * 
   * @param idFormat
   *          identifiant du format souhaité
   * @param fichier
   *          le fichier à identifier
   */
  @Before(IDENTIFIER_IDENTIFYFILE)
  public final void identifyFile(final String idFormat, final File fichier) {

    genererExceptionFile(idFormat, fichier);
  }

  /**
   * Vérification des paramètres de la méthode "identifyStream" de la classe
   * Identifier Vérification du String idFormat donné en paramètre<br>
   * 
   * @param idFormat
   *          identifiant du format souhaité
   * @param stream
   *          le flux à identifier
   * @param nomFichier
   *          le nom du fichier correspondant au stream
   */
  @Before(IDENTIFIER_IDENTIFYSTREAM)
  public final void identifyStream(final String idFormat, final InputStream stream,
                                   final String nomFichier) {
    genererExceptionStream(idFormat, stream, nomFichier);
  }

  private void genererExceptionFile(final String idFormat, final File file) {

    final List<String> param = new ArrayList<>();

    if (StringUtils.isBlank(idFormat)) {
      param.add(Constantes.IDFORMAT);
    }
    if (file == null || !file.exists()) {
      param.add(Constantes.FICHIER);
    }
    if (!param.isEmpty()) {
      throw new ReferentielRuntimeException(SaeFormatMessageHandler
                                            .getMessage(Constantes.PARAM_OBLIGATOIRE, param.toString()));
    }
  }

  private void genererExceptionStream(final String idFormat, final InputStream stream,
                                      final String nomFichier) {

    final List<String> param = new ArrayList<>();

    if (StringUtils.isBlank(idFormat)) {
      param.add(Constantes.IDFORMAT);
    }
    if (stream == null) {
      param.add(Constantes.STREAM);
    }
    if (nomFichier == null) {
      param.add(Constantes.NOMFICHIER);
    }
    if (!param.isEmpty()) {
      throw new ReferentielRuntimeException(SaeFormatMessageHandler
                                            .getMessage(Constantes.PARAM_OBLIGATOIRE, param.toString()));
    }
  }

}
