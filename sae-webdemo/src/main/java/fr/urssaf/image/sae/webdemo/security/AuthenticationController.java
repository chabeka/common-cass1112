package fr.urssaf.image.sae.webdemo.security;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.commons.util.base64.Base64Decode;
import static fr.urssaf.image.sae.webdemo.security.AuthenticationConfiguration.SERVICE_FIELD;
import static fr.urssaf.image.sae.webdemo.security.AuthenticationConfiguration.TOKEN_FIELD;
import static fr.urssaf.image.sae.webdemo.security.AuthenticationConfiguration.SECURITY_URL;

/**
 * Classe de manipulation de la servlet <code>/authentication.html</code><br>
 * ce contrôleur permet de récupérer en particulier le VI de la requête HTTP
 * envoyée à l'application pour l'identification<br>
 * Ce contrôleur correspond à la position <code>FORM_LOGIN_FILTER</code> de
 * Spring Security<br>
 * C'est ici que l'utilisateur est envoyé si il n'est pas connecté après être
 * passé par le point d'entré {@link SecurityEntryPoint}
 * 
 */
@Controller
@RequestMapping(value = "/authentication")
public class AuthenticationController {

   /**
    * La méthode GET renvoie vers l'url d'identification sans réupérer le
    * moindre paramètre<br>
    * C'est au contrôleur d'identification {@link SecurityFilter} qui se charge
    * de retrourner une page d'erreur
    * 
    * @see SECURITY_URL
    * @return url d'identification de spring security
    * @throws IOException
    */
   @RequestMapping(method = RequestMethod.GET)
   protected final String authenticate() throws IOException {

      return redirect();
   }

   /**
    * La méthode POSt récupère les paramètres d'identification
    * {@link TOKEN_FIELD} et {@link SERVICE_FIELD}<br>
    * Au passage la valeur du VI est décodé de la base 64<br>
    * Les paramètres sont placés en session HTTP pour être récupérés par le
    * contrôleur d'identification {@link SecurityFilter}
    * 
    * @param authForm
    *           formulaire de la requête HTTP en POST
    * @param session
    *           session HTTP où seront stockés les paramètres d'identification
    * @return url d'identification de spring security
    * @throws IOException
    */
   @RequestMapping(method = RequestMethod.POST)
   protected final String authenticate(AuthentificationForm authForm,
         HttpSession session) throws IOException {

      session.setAttribute(SERVICE_FIELD, authForm.getRelayState());
      session.setAttribute(TOKEN_FIELD, authForm.getSAMLResponse());

      return redirect();
   }

   private String redirect() throws IOException {
      return "redirect:" + SECURITY_URL;
   }

   /**
    * Décodage de la base 64 pour le paramètre {@link TOKEN_FIELD}
    * 
    * @param binder
    * @param request
    */
   @InitBinder
   protected final void decodageBase64(WebDataBinder binder,
         HttpServletRequest request) {

      binder.registerCustomEditor(String.class, TOKEN_FIELD,
            new DecodageBase64());
   }

   private static class DecodageBase64 extends PropertyEditorSupport {

      @Override
      public void setAsText(String text) {
         if (StringUtils.hasText(text)) {
            this.setValue(Base64Decode.decode(text));

         }
      }

   }

}
