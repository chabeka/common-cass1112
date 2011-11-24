package fr.urssaf.image.sae.webdemo.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import fr.urssaf.image.sae.vi.schema.DroitsType;
import fr.urssaf.image.sae.vi.schema.IdentiteUtilisateurType;
import fr.urssaf.image.sae.vi.schema.SaeJetonAuthentificationType;

/**
 * Classe du jeton d'identification de l'application<br>
 * La classe hérite de {@link AbstractAuthenticationToken}<br>
 * <br>
 * Le jeton d'identification est construit à partir du jeton VI
 * {@link SaeJetonAuthentificationType} instancié dans {@link SecurityFilter}<br>
 * 
 * 
 */
public class SecurityAuthentication extends AbstractAuthenticationToken {

   private static final long serialVersionUID = 1L;

   private final SaeJetonAuthentificationType jeton;

   /**
    * initialise les valeurs du jeton d'identification<br>
    * <br>
    * <ul>
    * <li>authorities : vide</li>
    * <li>{@link AbstractAuthenticationToken#setDetails} : jeton VI</li>
    * </ul>
    * 
    * @param jeton
    *           jeton VI
    */
   public SecurityAuthentication(SaeJetonAuthentificationType jeton) {
      super(null);
      this.setDetails(jeton);
      this.jeton = jeton;
   }

   /**
    * Retourne {@link SaeJetonAuthentificationType#getDroits()}<br>
    * <br> {@inheritDoc}
    */
   @Override
   public final DroitsType getCredentials() {
      return jeton.getDroits();
   }

   /**
    * Retourne {@link SaeJetonAuthentificationType#getIdentiteUtilisateur()}<br>
    * <br> {@inheritDoc}
    * 
    */
   @Override
   public final IdentiteUtilisateurType getPrincipal() {
      return jeton.getIdentiteUtilisateur();
   }

}
