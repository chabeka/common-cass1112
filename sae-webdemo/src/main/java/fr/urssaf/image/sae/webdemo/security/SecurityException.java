package fr.urssaf.image.sae.webdemo.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;

/**
 * Classe d'exception survenue lors de l'authentification<br>
 * cette exception contient
 * <ul>
 * <li>vue renvoyée pour l'excption levé</li>
 * <li>status de la réponse HTTP</li>
 * </ul>
 * 
 * 
 */
public class SecurityException extends AuthenticationException {

   private static final long serialVersionUID = 1L;

   public static final String EXCEPTION_STATUS = "status";

   public static final String EXCEPTION_VIEW = "view";

   /**
    * initialise les paramètres
    * 
    * @param msg
    *           message de l'exception
    * @param view
    *           vue de l'exception
    * @param status
    *           status de la réponse HTTP
    */
   public SecurityException(String msg, String view, int status) {
      super(msg, new HashMap<String, Object>());

      this.getExtraInformation().put(EXCEPTION_VIEW, view);
      this.getExtraInformation().put(EXCEPTION_STATUS, status);

   }

   /**
    * @see SecurityException#SecurityException(String, String, int)
    * 
    * @param throwable
    *           exception ajouté dans le
    *           {@link #setStackTrace(StackTraceElement[])}
    */
   protected SecurityException(String msg, String view, int status,
         Throwable throwable) {
      this(msg, view, status);
      this.setStackTrace(throwable.getStackTrace());
   }

   /**
    * Retourne des paramètres complémentaires pour l'exception<br>
    * Structure du tableau associatif<br>
    * <ul>
    * <li> <code>key</code> : nom du paramètre</li>
    * <li> <code>value</code> : valeur du paramètre</li>
    * </ul>
    * exemples:
    * <ul>
    * <li><code>{@link #EXCEPTION_STATUS}</code> : status de la réponse HTTP
    * {@link #getStatus()}</li>
    * <li><code>{@link #EXCEPTION_VIEW}</code> : vue de l'exception HTTP
    * {@link #getView()}</li>
    * </ul>
    * 
    * <br> {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   @Override
   public final Map<String, Object> getExtraInformation() {
      return (Map<String, Object>) super.getExtraInformation();
   }

   /**
    * 
    * @return vue de l'exception d'authentification
    */
   public final String getView() {

      return (String) this.getExtraInformation().get(EXCEPTION_VIEW);
   }

   /**
    * 
    * @return status de la réponse HTTP de l'exception d'authentification
    */
   public final int getStatus() {

      return (Integer) this.getExtraInformation().get(EXCEPTION_STATUS);
   }

}
