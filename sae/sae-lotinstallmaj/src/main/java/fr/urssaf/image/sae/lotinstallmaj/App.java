package fr.urssaf.image.sae.lotinstallmaj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.lotinstallmaj.component.Initializer;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;

public class App {

   private static final Logger LOG = LoggerFactory.getLogger(App.class);

   private static Initializer initializer;


   /**
    * @param args
    */
   public static void main(final String[] args) {

      final String cheminFicConfSae = args[0];
      final ApplicationContext context = startContextSpring(cheminFicConfSae);

      initializer = context.getBean(Initializer.class);
      try {
         initializer.majRnd();
      }
      catch (final MajLotGeneralException e) {
         e.printStackTrace();
      }
   }

   /**
    * Démarage du contexte Spring
    * 
    * @param cheminFicConfSae
    *           le chemin du fichier de configuration principal du sae
    *           (sae-config.properties)
    * @return le contexte Spring
    */
   protected static ApplicationContext startContextSpring(
                                                          final String cheminFicConfSae) {

      final String contextConfig = "/applicationContext-sae-lotinstallmaj.xml";

      return ContextFactory.createSAEApplicationContext(contextConfig,
                                                        cheminFicConfSae);
   }
}
