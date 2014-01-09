package fr.urssaf.image.sae.vi.spring;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Factory d'objet de type {@link AuthenticationToken}
 * 
 * 
 */
public final class AuthenticationFactory {

   private AuthenticationFactory() {

   }

   /**
    * Instanciation de {@link AuthenticationToken}
    * 
    * @param key
    *           {@link AuthenticationToken#getKeyHash()}
    * @param principal
    *           {@link AuthenticationToken#getPrincipal()}
    * @param roles
    *           liste des roles applicatifs
    * @param actionsUnitaires
    *           liste des actions unitaires
    * @return instance de {@link AuthenticationToken}
    */
   public static AuthenticationToken createAuthentication(String key,
         Object principal, String[] roles) {

      List<GrantedAuthority> authorities = AuthorityUtils
            .createAuthorityList(roles);

      return new AuthenticationToken(key, principal, authorities);
   }
}
