package fr.urssaf.image.sae.mapping.context;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Le context de l'application
 * @author akenore
 *
 */
@Component
public final class MappingApplicationContext {
	private static ApplicationContext ctx;

	  /**
	    * injection de l'instance {@link ApplicationContext}<br>
	    * 
	    * @param context
	    *           contexte de l'application
	    */
	   @Autowired
	   public void setMappingApplicationContext(final ApplicationContext context) {
	      Assert.notNull(context, "'context' is required ");
	      setContext(context);
	   }

	  private static  void setContext(final ApplicationContext context) {
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
