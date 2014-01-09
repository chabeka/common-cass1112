package fr.urssaf.image.sae.vi.spring;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

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
         List<GrantedAuthority> authorities) {
      super(key, principal, authorities);
   }

   private static final long serialVersionUID = 1L;

   /**
    * @return les droits {@link SaeDroits} de l'authentification
    */
   public SaeDroits getSaeDroits() {
      return getViContenuExtrait().getSaeDroits();
   }

   /**
    * @return la liste des controles de profil pour les formats {@link List
    *         <FormatControlProfil>}
    */
   public List<FormatControlProfil> getListFormatControlProfil() {
      return getViContenuExtrait().getListControlProfil();
   }

   private VIContenuExtrait getViContenuExtrait() {
      return (VIContenuExtrait) getPrincipal();
   }

}
