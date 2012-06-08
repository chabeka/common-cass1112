package fr.urssaf.image.sae.webdemo.security;

import static fr.urssaf.image.sae.webdemo.security.AuthenticationConfiguration.SECURITY_URL;
import static fr.urssaf.image.sae.webdemo.security.AuthenticationConfiguration.SERVICE_FIELD;
import static fr.urssaf.image.sae.webdemo.security.AuthenticationConfiguration.TOKEN_FIELD;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import fr.urssaf.image.sae.vi.exception.VIException;
import fr.urssaf.image.sae.vi.modele.VIPortailContenuExtrait;
import fr.urssaf.image.sae.vi.service.PortailVIService;
import fr.urssaf.image.sae.webdemo.service.ConnectionService;

/**
 * Contrôleur d'authentification appelé à chaque fois que l'utilisateur n'est
 * pas identifié<br>
 * Ce contrôleur est appelé à la suite du contrôleur
 * {@link AuthenticateController}<br>
 * La classe hérite de {@link AbstractAuthenticationProcessingFilter}<br>
 * le filtre d'authentification est configuré dans
 * <code>ApplicationContext-security.xml</code>
 * 
 * <pre>
 * 
 * &lt;http use-expressions="true" entry-point-ref="authenticationEntryPoint">
 *       &lt;custom-filter ref="authenticationFilter" position="FORM_LOGIN_FILTER" />
 * &lt;/http>
 * 
 * &lt;authentication-manager alias="authenticationManager">
 *    &lt;authentication-provider ref="authenticationProvider" />
 * &lt/authentication-manager>
 * 
 * &lt;beans:bean id="authenticationFilter"
 *       class="fr.urssaf.image.sae.webdemo.security.SecurityFilter">
 *       ...
 *    &lt;beans:property name="authenticationManager" ref="authenticationManager" />
 * &lt;/beans:bean>
 * 
 * </pre>
 * 
 * le filtre d'authentification remplace celui par défault à la position
 * <code>FORM_LOGIN_FILTER</code>
 * 
 */
public class SecurityFilter extends AbstractAuthenticationProcessingFilter {

   private final ConnectionService connection;

   private final PortailVIService saePortailViService;

   protected SecurityFilter(ConnectionService connection) {
      super("/" + SECURITY_URL);

      this.saePortailViService = new PortailVIService();

      this.connection = connection;

      this.setAuthenticationSuccessHandler(new SecuritySuccess());
   }

   /**
    * Instanciation de {@link Authentification} de type
    * {@link SecurityAuthentification} <br>
    * <br>
    * La méthode récupère en session HTTP les paramètres {@link TOKEN_FIELD}
    * pour instancier le jeton d'authentification<br>
    * Le paramètre {@link SERVICE_FIELD} permet de rediriger l'utilisateur vers
    * le service demandé <br>
    * <br>
    * Les valeurs permettent de vérifier si l'authentification est correcte
    * auquelle on redirige vers le service demandé sinon on affiche une vue
    * d'échec d'authentification avec un status d'erreur <br>
    * 
    * <br>
    * Vues d'échec pour l'authentification:
    * <ul>
    * <li>{@link TOKEN_FIELD} & {@link SERVICE_FIELD} non renseignés :
    * <code>error/erreur403_viko<code> et {@link HttpServletResponse#SC_FORBIDDEN}
    * </li>
    * <li>{@link SERVICE_FIELD} n'existe pas en tant que service :
    * <code>error/erreur404_serviceinexistant<code> et {@link HttpServletResponse#SC_NOT_FOUND}
    * </li>
    * <li>{@link TOKEN_FIELD} incorrecte :
    * <code>error/erreur403_viformatko</code> et
    * {@link HttpServletResponse#SC_FORBIDDEN}</li>
    * </ul>
    * <br> {@link ConnectionService#isValidateService(String)} permet de valider
    * {@link SERVICE_FIELD}<br>
    * <br>{@link VIService#readVI(String)} permet de valider {@link TOKEN_FIELD}<br>
    * 
    * 
    * <br>{@inheritDoc}
    */
   @Override
   public final Authentication attemptAuthentication(
         HttpServletRequest request, HttpServletResponse response)
         throws IOException, ServletException {

      Authentication authentification = null;

      String relayState = (String) WebUtils.getSessionAttribute(request,
            SERVICE_FIELD);
      String samlResponse = (String) WebUtils.getSessionAttribute(request,
            TOKEN_FIELD);

      if (StringUtils.hasText(relayState) && StringUtils.hasText(samlResponse)) {

         if (connection.isValidateService(relayState)) {

            try {

               System.out.println(samlResponse);
               
               VIPortailContenuExtrait jeton = saePortailViService
                     .lireVI(samlResponse);

               authentification = this.getAuthenticationManager().authenticate(
                     new SecurityAuthentication(jeton));

               request.setAttribute(SERVICE_FIELD, relayState);

            } catch (VIException e) {

               throw new SecurityException(e.getMessage(),
                     "error/erreur403_viformatko",
                     HttpServletResponse.SC_FORBIDDEN, e);
            }

         } else {

            SecurityException authException = new SecurityException(
                  "erreur404_serviceinexistant",
                  "error/erreur404_serviceinexistant",
                  HttpServletResponse.SC_NOT_FOUND);

            authException.getExtraInformation().put("service", relayState);

            throw authException;

         }

      }

      else {

         throw new SecurityException("erreur403_viko", "error/erreur403_viko",
               HttpServletResponse.SC_FORBIDDEN);

      }

      return authentification;
   }

   protected static class SecuritySuccess extends
         SimpleUrlAuthenticationSuccessHandler {

      @Override
      public final void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

         String service = (String) request.getAttribute(SERVICE_FIELD);
         this.setDefaultTargetUrl(service);

         super.onAuthenticationSuccess(request, response, authentication);

      }

   }

}
