/**
 * 
 */
package fr.urssaf.image.sae.droit.service.validation;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Classe de validation des arguments entrée des implémentations du service
 * PrmdService
 */
@Aspect
@Component
public class PrmdServiceValidation {

  /**
   * 
   */
  private static final String ARGUMENT_REQUIRED = "argument.required";

  private static final String CHECK = "execution(boolean fr.urssaf.image.sae.droit.service.PrmdService.isPermitted(*,*))"
      + "&& args(metadatas, prmds)";

  private static final String CREATE = "execution(java.lang.String fr.urssaf.image.sae.droit.service.PrmdService.createLucene(*,*))"
      + "&& args(lucene, prmds)";

  private static final String ADD_DOMAINE = "execution(void fr.urssaf.image.sae.droit.service.PrmdService.addDomaine(*,*))"
      + "&& args(metadatas, prmds)";

  /**
   * méthode de validation des arguments de la méthode isPermitted
   * 
   * @param metadatas
   *          liste des métadonnées
   * @param prmds
   *          liste des prmd
   */
  @Before(CHECK)
  public final void checkIsPermitted(final List<UntypedMetadata> metadatas,
                                     final List<SaePrmd> prmds) {
    if (CollectionUtils.isEmpty(metadatas)) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           ARGUMENT_REQUIRED,
                                                                           "liste des métadonnées"));
    }

    if (CollectionUtils.isEmpty(prmds)) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           ARGUMENT_REQUIRED,
                                                                           "liste des prmd"));
    }
  }

  /**
   * méthode de validation des arguments de la méthode createLucene
   * 
   * @param lucene
   *          requete Lucene d'origine
   * @param prmds
   *          liste des prmds
   */
  @Before(CREATE)
  public final void checkCreate(final String lucene, final List<SaePrmd> prmds) {
    if (StringUtils.isEmpty(lucene)) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           ARGUMENT_REQUIRED,
                                                                           "requete"));
    }

    if (CollectionUtils.isEmpty(prmds)) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           ARGUMENT_REQUIRED,
                                                                           "liste des prmd"));
    }
  }

  /**
   * méthode de validation des arguments de la méthode addDomaine
   * 
   * @param metadatas
   *          listes des métas
   * @param prmds
   *          liste des prmds
   */
  @Before(ADD_DOMAINE)
  public final void checkAddDomaine(final List<UntypedMetadata> metadatas,
                                    final List<SaePrmd> prmds) {

    if (metadatas == null) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           ARGUMENT_REQUIRED,
                                                                           "liste des métadonnées"));
    }

    if (CollectionUtils.isEmpty(prmds)) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           ARGUMENT_REQUIRED,
                                                                           "liste des prmd"));
    }
  }

}
