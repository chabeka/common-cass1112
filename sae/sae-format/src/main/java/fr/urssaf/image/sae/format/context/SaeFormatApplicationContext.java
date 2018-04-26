package fr.urssaf.image.sae.format.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Le context de l'application SAE-FORMAT
 * 
 */
@Component
public final class SaeFormatApplicationContext {

   @Autowired
   private ApplicationContext ctx;

   /**
    * Méthode d'accès à l'instance {@link ApplicationContext} de l'application
    * 
    * @return contexte de l'application
    */
   public ApplicationContext getApplicationContext() {
      return ctx;
   }
   
   /**
     * injection de l'instance {@link ApplicationContext}<br>
     * 
     * @param context
     *           contexte de l'application
     */
    @Autowired
    public void setStorageApplicationContext(final ApplicationContext context) {
       Assert.notNull(context, "'context' is required ");
       setContext(context);
    }

   private void setContext(final ApplicationContext context) {
       ctx = context;
   }

}
