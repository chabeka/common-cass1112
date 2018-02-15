package fr.urssaf.image.sae.droit.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
public final class DroitsApplicationContext {

   @Autowired
   private ApplicationContext ctx;

   /**
    * Méthode d'accès à l'instance {@link ApplicationContext} de l'application
    * 
    * @return contexte de l'application
    */
   public ApplicationContext getApplicationContext() {
      Assert.notNull(ctx, "ApplicationContext not initialized ");
      return ctx;
   }

   /**
    * injection de l'instance {@link ApplicationContext}<br>
    * 
    * @param context
    *           contexte de l'application, ne doit pas être null
    */
   @Autowired
   public void setServicesApplicationContext(final ApplicationContext context) {
      Assert.notNull(context, "'context' is required ");
      setContext(context);
   }

   private void setContext(final ApplicationContext context) {
      ctx = context;
   }

}
