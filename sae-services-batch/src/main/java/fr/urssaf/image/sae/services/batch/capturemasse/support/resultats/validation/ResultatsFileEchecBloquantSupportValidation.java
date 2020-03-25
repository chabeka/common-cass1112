/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Validation des arguments passés en entrée de l'implémentation du service
 * {@link fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecBloquantSupport}
 * . La validation est basée sur la programmation Aspect
 */
@Aspect
@Component
public class ResultatsFileEchecBloquantSupportValidation {

  private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

  private static final String CHECK_METHOD = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecBloquantSupport.writeResultatsFile(*,*))"
      + " && args(ecdeDirectory,erreur)";

  /**
   * Vérification de la présence de tous les arguments de la méthode
   * writeResultatsFile
   * 
   * @param ecdeDirectory
   *          chemin absolu du répertoire de traitement du traitement de masse
   * @param erreur
   *          erreur bloquante mère
   */
  @Before(CHECK_METHOD)
  public final void checkWriteResultatsFile(final File ecdeDirectory,
                                            final Exception erreur) {

    if (ecdeDirectory == null) {
      throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
                                                       "ecdeDirectory"));
    }

    if (erreur == null) {
      throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
                                                       "erreur"));
    }
  }
}
