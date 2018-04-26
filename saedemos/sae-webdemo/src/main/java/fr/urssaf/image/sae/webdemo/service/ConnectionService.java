package fr.urssaf.image.sae.webdemo.service;

import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;

/**
 * Service de connexion de l'application demo
 * 
 */
@Service
public class ConnectionService {

   private final ApplicationContext context;

   /**
    * Initialisation de la variable <code>context</code><br>
    * <br>
    * <code>context</code> ne peut être null
    * 
    * @see ApplicationContex
    * @param context
    *           contexte de l'application
    */
   @Autowired
   public ConnectionService(ApplicationContext context) {

      if (context == null) {
         throw new IllegalStateException("'applicationContext' is required");
      }

      this.context = context;
   }

   /**
    * Retourne si la servlet est valide dans l'application<br>
    * <br>
    * La méthode récupère les beans de type {@link AbstractUrlHandlerMapping} à
    * partir de {@link ApplicationContext} et vérifie si l'un d'entre contient
    * bien la servlet<br>
    * <br>
    * ex : /, /connection, /accueil.html sont valides<br>
    * /service.html ne l'est pas
    * 
    * @see AbstractUrlHandlerMapping#getHandlerMap()
    * 
    * @param servlet
    *           url de la servlet
    * @return true si la servlet est valide faut sinon
    */
   public final boolean isValidateService(String servlet) {

      Map<String, AbstractUrlHandlerMapping> matchingBeans = BeanFactoryUtils
            .beansOfTypeIncludingAncestors(context,
                  AbstractUrlHandlerMapping.class);

      boolean inContext = false;

      if ("/".equals(servlet)) {
         inContext = true;
      } else {
         for (AbstractUrlHandlerMapping handlerMapping : matchingBeans.values()) {

            inContext = handlerMapping.getHandlerMap().containsKey(servlet);
            if (inContext) {
               break;
            }
         }
      }

      return inContext;
   }

}
