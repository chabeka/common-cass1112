/**
 * 
 */
package fr.urssaf.image.sae.droit.service.validation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Classe de validation des arguments entrée des implémentations du service
 * SaePagmaServiceValidation
 */
@Aspect
@Component
public class SaePagmaServiceValidation {

  private static final String CREATE_METHOD = "execution(void fr.urssaf.image.sae.droit.service.SaePagmaService.createPagma(*))"
      + "&& args(pagma)";

  /**
   * Méthode de validation de la méthode SaePagmaService#createPagma(Pagma)
   * 
   * @param pagma
   *          pagma à créer
   */
  @Before(CREATE_METHOD)
  public final void checkCreate(final Pagma pagma) {

    if (pagma == null) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           "argument.required",
                                                                           "pagma"));
    }

    if (StringUtils.isBlank(pagma.getCode())) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           "argument.required",
                                                                           "code pagma"));
    }

    if (CollectionUtils.isEmpty(pagma.getActionUnitaires())) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           "argument.required",
                                                                           "actions unitaires pagma"));
    }

  }

}
