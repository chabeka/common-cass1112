package fr.urssaf.image.sae.vi.spring;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import fr.urssaf.image.sae.droit.model.SaeDroits;

/**
 * Classe modèle du jeton d'authentification héritant de
 * {@link AnonymousAuthenticationToken}
 * 
 * 
 */
public class AuthenticationToken extends AnonymousAuthenticationToken {

   /**
    * instanciation de
    * {@link AnonymousAuthenticationToken#AnonymousAuthenticationToken(String, Object, List)}
    * 
    * @param key
    *           to identify if this object made by an authorised client
    * 
    * @param principal
    *           the principal
    * @param authorities
    *           the authorities granted to the principal
    * 
    * @param details
    *           information supplémentaires sur l'authentification courante
    */
   public AuthenticationToken(String key, Object principal,
         List<GrantedAuthority> authorities, SaeDroits details) {
      super(key, principal, authorities);
      this.setDetails(details);
   }

   private static final long serialVersionUID = 1L;

   /**
    * renvoie {@link ActionsUnitaires} de l'authenfication
    * 
    * {@inheritDoc}
    */
   @Override
   public final SaeDroits getDetails() {
      return (SaeDroits) super.getDetails();
   }

}
