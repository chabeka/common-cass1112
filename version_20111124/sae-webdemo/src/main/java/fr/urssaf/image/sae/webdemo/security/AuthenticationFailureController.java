package fr.urssaf.image.sae.webdemo.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

/**
 * Contrôleur appelé quand l'utilisateur n'est pas authentifié
 * <code>/authenticationFailure.html</code><br>
 * <br>
 * Ce contrôleur est appelé quand le contrôleur d'identification
 * {@link SecurityFilter} renvoie une exception
 * {@link AuthentificationException}
 * 
 * @see SecurityFilter#setAuthenticationFailureHandler
 * 
 */
@Controller
@RequestMapping(value = "/authenticationFailure")
public class AuthenticationFailureController {

   /**
    * requête HTTP en GET<br>
    * L'exception {@link AuthenticationException} est récupéré à partir de la
    * session HTTP sous le nom {@link WebAttributes.AUTHENTICATION_EXCEPTION}<br>
    * <br>
    * Si {@link AuthenticationException} est de type {@link SecurityException}
    * alors on retourne la vue {@link SecurityException#getView()} et la réponse
    * HTTP a le statut contenu dans {@link SecurityException#getStatus()}<br>
    * <br>
    * Sinon on renvoie par défaut la vue
    * <code>error/erreur403_pasauthentifie</code> avec le status
    * {@link HttpServletResponse.SC_FORBIDDEN}
    * 
    * @param session
    *           session HTTP contenant l'exception d'authentification v
    * @param response
    *           réponse HTTP contenant l'exception d'authentification
    * @param request
    *           requête HTTP contenant l'exception d'authentification
    * @return vue d'erreur (CF répertoire view/errror}
    */
   @RequestMapping(method = RequestMethod.GET)
   protected final String exception(HttpSession session,
         HttpServletRequest request, HttpServletResponse response) {

      AuthenticationException exception = (AuthenticationException) WebUtils
            .getSessionAttribute(request,
                  WebAttributes.AUTHENTICATION_EXCEPTION);

      String view;
      if (exception instanceof SecurityException) {

         SecurityException authException = (SecurityException) exception;

         view = authException.getView();
         response.setStatus(authException.getStatus());

         for (String information : authException.getExtraInformation().keySet()) {

            request.setAttribute(information, authException
                  .getExtraInformation().get(information));
         }

      }

      else {

         response.setStatus(HttpServletResponse.SC_FORBIDDEN);
         view = "error/erreur403_pasauthentifie";
      }

      return view;
   }

}
