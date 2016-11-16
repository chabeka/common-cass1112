package fr.urssaf.image.sae.services.copie.validation;

import java.util.List;
import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

@Aspect
public class SAECopieServiceValidation {

   private static final String COPIE_METHOD = "execution(UUID fr.urssaf.image.sae.services.copie.SAECopieService.copie(*,*))"
         + "&& args(idCopie, metadata)";

   @Before(COPIE_METHOD)
   public final void copie(UUID idCopie, List<UntypedMetadata> metadata) {
      if (idCopie == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "idCopie"));
      }

      if (CollectionUtils.isEmpty(metadata)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "metadata"));
      }
   }
}
