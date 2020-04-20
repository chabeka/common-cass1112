package fr.urssaf.image.sae.lotinstallmaj;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotService;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.OperationCQL;

/**
 * Classe Main du JAR Executable
 * 
 */
public final class Main {

  /**
   * LOGGER
   */
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

   private Main() {

   }

   /**
    * Méthode main du JAR Executable
    * 
    * @param args
    *           arguments de la ligne de commande du JAR Executable
    */
  public static void main(final String[] args) {

      // Extrait les infos de la ligne de commandes
      // La vérification du tableau args est faite par la validation AOP
    final String cheminFicConfSae = args[0];
    final String nomOperation = args[1];

      // Démarrage du contexte spring
    final ApplicationContext context = startContextSpring(cheminFicConfSae);

      // Récupération du contexte Spring du bean permettant de lancer
      // l'opération
    final MajLotService majLotService = context.getBean("majLotServiceImpl",MajLotService.class);

      // Retire des arguments de la ligne de commande ceux que l'on a déjà
      // traités.
      // On ne laisse que les arguments spécifiques à l'opération
    final String[] argsSpecifiques = (String[]) ArrayUtils.remove(args, 0);

      try {
	     
	      // Récupération du contexte Spring du bean permettant de lancer
	      // l'opération
      final MajLotService majLotServicecql = context.getBean("majLotServiceCQLImpl",
	            MajLotService.class);
	      
	      // Démarre l'opération sur les nouvelles tables cql
	      if(OperationCQL.get(nomOperation) != null) {
	    	  majLotServicecql.demarre(nomOperation, argsSpecifiques);
	      } 
	      else {
	    	  // Démarre l'opération sur les anciennes commandes
	    	  majLotService.demarre(nomOperation, argsSpecifiques);
	      }
    } catch (final Exception e){
      LOG.error(e.getMessage());
      } finally {
    	  System.exit(0);
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
