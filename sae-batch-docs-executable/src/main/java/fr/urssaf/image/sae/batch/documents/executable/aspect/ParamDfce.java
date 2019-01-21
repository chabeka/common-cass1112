package fr.urssaf.image.sae.batch.documents.executable.aspect;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.batch.documents.executable.exception.ParametreRuntimeException;
import fr.urssaf.image.sae.batch.documents.executable.messages.ExecutableMessageHandler;
import fr.urssaf.image.sae.batch.documents.executable.utils.Constantes;
import net.docubase.toolkit.model.document.Document;

/**
 * Classe de validation des paramètres obligatoires.
 */
@Aspect
@Component
public class ParamDfce {

  /********************************************************* SERVICE *********************************************************************************/
  private static final String DFCE_SERVICE_EXECUTERREQUETE = "execution(* fr.urssaf.image.sae.batch.documents.executable.service.DfceService.executerRequete(*))"
      + "&& args(requeteLucene)";

  private static final String DFCE_SERVICE_RECUPERERCONTENU = "execution(* fr.urssaf.image.sae.batch.documents.executable.service.DfceService.recupererContenu(*))"
      + "&& args(document)";

  /**
   * Vérification des paramètres de la méthode "executerRequete" de la classe
   * DfceService. Vérification du String requeteLucene donné en paramètre<br>
   * 
   * @param requeteLucene
   *          requête lucène
   */
  @Before(DFCE_SERVICE_EXECUTERREQUETE)
  public final void validExecuterRequeteFromDfceService(final String requeteLucene) {
    final List<String> param = new ArrayList<>();

    if (StringUtils.isBlank(requeteLucene)) {
      param.add(Constantes.REQUETELUCENE);
    }

    if (!param.isEmpty()) {
      throw new ParametreRuntimeException(
                                          ExecutableMessageHandler.getMessage(
                                                                              Constantes.PARAM_OBLIGATOIRE,
                                                                              param.toString()));
    }
  }

  /**
   * Vérification des paramètres de la méthode "recupererContenu" de la classe
   * DfceService. Vérification du Document document donné en paramètre<br>
   * 
   * @param document
   *          Document Dfce
   */
  @Before(DFCE_SERVICE_RECUPERERCONTENU)
  public final void validRecupererContenuFromDfceService(final Document document) {
    final List<String> param = new ArrayList<>();

    if (document == null) {
      param.add(Constantes.DOCUMENT);
    }

    if (!param.isEmpty()) {
      throw new ParametreRuntimeException(
                                          ExecutableMessageHandler.getMessage(
                                                                              Constantes.PARAM_OBLIGATOIRE,
                                                                              param.toString()));
    }
  }
}
