package fr.urssaf.image.sae.webdemo.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Classe d'entrée lorsque l'utilisateur n'est pas authentifié<br>
 * La classe hérite de {@link LoginUrlAuthenticationEntryPoint}<br>
 * Cette classe est paramétré dans le fichier
 * <code>ApplicationContext-security.xml</code>
 * 
 * <pre>
 * &lt;http use-expressions="true" entry-point-ref="authenticationEntryPoint">
 *       ...
 * &lt;/http>
 * ... 
 * &lt;beans:bean id="authenticationEntryPoint"
 *       class="fr.urssaf.image.sae.webdemo.security.SecurityEntryPoint">
 *    &lt;beans:property name="loginFormUrl" value="/authentication.html"/>
 *    &lt;beans:property name="useForward" value="true"/>
 *    &lt;beans:property name="failureURL" value="/authenticationFailure.html"/>
 *    &lt;beans:property name="authenticateURL" value="/applicationDemo.html"/>
 * &lt;/beans:bean>
 * 
 * </pre>
 * Les propriétés suivantes doit être complétées obligatoirement
 * <ul>
 * <li><code>loginFormUrl</code> : URl de redirection vers le contrôleur
 * d'authentification de type{@link AuthenticateController}</li>
 * <li><code>useForward</code> : true</li>
 * <li><code>failureURL</code> : URL de redirection</li>
 * <li><code>authenticateURL</code> : URL d'authentification de l'application</li>
 * </ul>
 * Le mécanisme d'authentification s'applique si
 * <ul>
 * <li>la requête HTTP est en POST</li>
 * <li>l'url de la requête HTTP correspond à la valeur de la propriété
 * <code>authenticateURL</code></li>
 * </ul>
 * sinon l'utilisateur est renvoyé vers la vue <code>failureURL</code>
 * 
 */
public class SecurityEntryPoint extends LoginUrlAuthenticationEntryPoint {

   private static final String METHOD = "POST";

   private String authenticateURL;

   /**
    * 
    * @param authenticateURL
    *           URL d'authentification de l'application
    */
   public final void setAuthenticateURL(String authenticateURL) {
      this.authenticateURL = authenticateURL;
   }

   private String failureURL;

   /**
    * 
    * @param failureURL
    *           URL de redirection vers le contrôleur de d'échec de
    *           l'authentification
    */
   public final void setFailureURL(String failureURL) {
      this.failureURL = failureURL;
   }

   @Override
   public final void afterPropertiesSet() {

      try {
         super.afterPropertiesSet();
         Assert.isTrue(StringUtils.hasText(failureURL)
               && UrlUtils.isValidRedirectUrl(failureURL),
               "failureURL must be specified and must be a valid redirect URL");
         Assert
               .isTrue(StringUtils.hasText(authenticateURL)
                     && UrlUtils.isValidRedirectUrl(authenticateURL),
                     "authenticateURL must be specified and must be a valid redirect URL");

      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public final void commence(HttpServletRequest request,
         HttpServletResponse response, AuthenticationException authException)
         throws IOException, ServletException {

      if (METHOD.equals(request.getMethod())
            && authenticateURL.equals(request.getServletPath())) {

         super.commence(request, response, authException);

      }

      else {
         RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
         redirectStrategy.sendRedirect(request, response, failureURL);

      }

   }

}
