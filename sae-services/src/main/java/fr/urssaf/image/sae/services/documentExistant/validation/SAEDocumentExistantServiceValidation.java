package fr.urssaf.image.sae.services.documentExistant.validation;

import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

@Aspect
@Component
public class SAEDocumentExistantServiceValidation {
  private static final String DOCUMENTEXISTANT_METHOD = "execution(boolean fr.urssaf.image.sae.services.documentExistant.SAEDocumentExistantService.documentExistant(*))"
      + "&& args(idGed)";

  @Before(DOCUMENTEXISTANT_METHOD)
  public final void copie(final UUID idGed) {
    if (idGed == null) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
                                                                           "argument.required",
                                                                           "idCopie"));
    }
  }
}
