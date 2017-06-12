package fr.urssaf.image.sae.webservices.context;

import me.prettyprint.cassandra.utils.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Cette classe permet d'accéder à l'instance {@link ApplicationContext} de
 * l'application.<br>
 * <br>
 * La classe est de type {@link Component}<br>
 * L'instance {@link ApplicationContext} est accessible par une méthode statique
 * {@link #getApplicationContext()}<br>
 * Cette méthode est appelée par des classes quand le contexte de l'application
 * n'est pas accessible par injection.<br>
 * C'est en particulier le cas dans les classe de type <code>@Aspect</code> ou
 * dans les méthodes statiques
 * 
 * 
 */
@Component
public class WebServicesApplicationContext {
      
      private static ApplicationContext ctx;

      /**
       * injection de l'instance {@link ApplicationContext}<br>
       * 
       * @param context
       *           contexte de l'application, ne doit pas être null
       */
      @Autowired
      public final void setServicesApplicationContext(ApplicationContext context) {
         Assert.notNull(context, "'context' is required ");
         setContext(context);
      }

      private static void setContext(ApplicationContext context) {
         ctx = context;
      }

      /**
       * Méthode d'accès à l'instance {@link ApplicationContext} de l'application
       * 
       * @return contexte de l'application
       */
      public static ApplicationContext getApplicationContext() {
         Assert.notNull(ctx, "ApplicationContext not initialized ");
         return ctx;
      }

}