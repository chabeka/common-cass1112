/**
 * 
 */
package fr.urssaf.image.sae.services.security.authorization;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.services.security.exception.PermissionEvaluatorDomainException;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe d'implémentation de {@link PermissionEvaluator}. Cette classe permet
 * de sécuriser l'accès aux méthodes en fonction du contexte de sécurité et d'un
 * modèle de données. Ici, le modèle de données est une liste de métadonnées
 * pour évaluer si oui ou non elles appartiennent bien à un PRMD du contexte de
 * sécurité.
 * 
 */
public class SAEDocumentPermissionEvaluator implements PermissionEvaluator {

   private PrmdService prmdService;

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean hasPermission(Authentication authentication, Object domain,
         Object permission) {

      boolean hasPermission = false;

      if (domain == null) {
         hasPermission = false;
      } else if (AuthorityUtils.authorityListToSet(
            authentication.getAuthorities()).contains(permission)) {

         AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
               .getContext().getAuthentication();

         if (!(domain instanceof List<?>)) {
            String message = "le type " + domain.getClass()
                  + " est inattendu là où on attend un modèle de métadonnées";
            throw new PermissionEvaluatorDomainException(message);
         }

         @SuppressWarnings("unchecked")
         List<UntypedMetadata> metadatas = (List<UntypedMetadata>) domain;

         hasPermission = prmdService.isPermitted(metadatas, token.getDetails()
               .get(permission));
      }

      return hasPermission;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean hasPermission(Authentication authentication,
         Serializable targetId, String targetType, Object permission) {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * @param prmdService the prmdService to set
    */
   public final void setPrmdService(PrmdService prmdService) {
      this.prmdService = prmdService;
   }

}
