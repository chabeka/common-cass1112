package fr.urssaf.image.sae.documents.executable.aspect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.documents.executable.exception.ParametreRuntimeException;
import fr.urssaf.image.sae.documents.executable.utils.Constantes;
import fr.urssaf.image.sae.documents.executable.utils.MetadataUtils;
import fr.urssaf.image.sae.documents.executable.utils.messages.SaeDocumentsExecutableMessageHandler;
import net.docubase.toolkit.model.document.Document;

/**
 * Classe de validation des paramètres obligatoires.
 */
@Aspect
@Component
public class ParamFormatFichier {

  /********************************************************* SERVICE *********************************************************************************/
  private static final String FORMAT_SERVICE_IDENTIFIERFICHIER = "execution(* fr.urssaf.image.sae.documents.executable.service.FormatFichierService.identifierFichier(*,*,*,*))"
      + "&& args(idFormat,file,document,metadonnees)";

  private static final String FORMAT_SERVICE_VALIDERFICHIER = "execution(* fr.urssaf.image.sae.documents.executable.service.FormatFichierService.validerFichier(*,*))"
      + "&& args(idFormat,file)";

  /**
   * Vérification des paramètres de la méthode "identifierFichier" de la classe
   * FormatFichierService. Vérification du String idFormat donné en paramètre<br>
   * Vérification du File file donné en paramètre<br>
   * Vérification du Document document donné en paramètre<br>
   * 
   * @param idFormat
   *          identifiant du format
   * @param file
   *          fichier
   * @param document
   *          document DFCE
   * @param metadonnees
   *          metadonnées
   */
  @Before(FORMAT_SERVICE_IDENTIFIERFICHIER)
  public final void validIdentifierFichierFromFormatFichierService(
                                                                   final String idFormat, final File file, final Document document,
                                                                   final List<String> metadonnees) {
    final List<String> param = new ArrayList<>();

    if (StringUtils.isBlank(idFormat)) {
      param.add(Constantes.IDFORMAT);
    }
    if (file == null) {
      param.add(Constantes.FILE);
    }
    if (document == null) {
      param.add(Constantes.DOCUMENT);
    }
    if (metadonnees == null) {
      param.add(Constantes.METADONNEES);
    }

    if (param.isEmpty()) {
      // verifie la liste des metadonnees
      final List<String> metasNonAutorisees = MetadataUtils
                                                           .checkMetadonneesNonAutorisees(metadonnees);
      if (!metasNonAutorisees.isEmpty()) {
        throw new ParametreRuntimeException(
                                            SaeDocumentsExecutableMessageHandler.getMessage(
                                                                                            Constantes.PARAM_METADONNEES_NON_AUTORISEES,
                                                                                            metasNonAutorisees.toString()));
      }
    } else {
      throw new ParametreRuntimeException(
                                          SaeDocumentsExecutableMessageHandler.getMessage(
                                                                                          Constantes.PARAM_OBLIGATOIRE,
                                                                                          param.toString()));
    }
  }

  /**
   * Vérification des paramètres de la méthode "validerFichier" de la classe
   * FormatFichierService. Vérification du String idFormat donné en paramètre<br>
   * Vérification du File file donné en paramètre<br>
   * 
   * @param idFormat
   *          identifiant du format
   * @param file
   *          fichier
   */
  @Before(FORMAT_SERVICE_VALIDERFICHIER)
  public final void validValiderFichierFromFormatFichierService(
                                                                final String idFormat, final File file) {
    final List<String> param = new ArrayList<>();

    if (StringUtils.isBlank(idFormat)) {
      param.add(Constantes.IDFORMAT);
    }
    if (file == null) {
      param.add(Constantes.FILE);
    }

    if (!param.isEmpty()) {
      throw new ParametreRuntimeException(
                                          SaeDocumentsExecutableMessageHandler.getMessage(
                                                                                          Constantes.PARAM_OBLIGATOIRE,
                                                                                          param.toString()));
    }
  }
}
