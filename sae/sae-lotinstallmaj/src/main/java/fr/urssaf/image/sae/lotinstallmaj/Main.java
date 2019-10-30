package fr.urssaf.image.sae.lotinstallmaj;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotService;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.OperationCQL;

/**
 * Classe Main du JAR Executable
 * 
 */
public final class Main {

   private Main() {

   }

   /**
    * Méthode main du JAR Executable
    * 
    * @param args
    *           arguments de la ligne de commande du JAR Executable
    */
   public static void main(String[] args) {

      // Extrait les infos de la ligne de commandes
      // La vérification du tableau args est faite par la validation AOP
      String cheminFicConfSae = args[0];
      String nomOperation = args[1];

      // Démarrage du contexte spring
      ApplicationContext context = startContextSpring(cheminFicConfSae);

      // Récupération du contexte Spring du bean permettant de lancer
      // l'opération
      MajLotService majLotService = context.getBean("majLotServiceImpl",MajLotService.class);

      // Retire des arguments de la ligne de commande ceux que l'on a déjà
      // traités.
      // On ne laisse que les arguments spécifiques à l'opération
      String[] argsSpecifiques = (String[]) ArrayUtils.remove(args, 0);

      try {
	     
	      // Récupération du contexte Spring du bean permettant de lancer
	      // l'opération
	      MajLotService majLotServicecql = context.getBean("majLotServiceCQLImpl",
	            MajLotService.class);
	      
	      // Démarre l'opération sur les nouvelles tables cql
	      if(OperationCQL.get(nomOperation) != null) {
	    	  majLotServicecql.demarre(nomOperation, argsSpecifiques);
	      } 
	      else {
	    	  // Démarre l'opération sur les anciennes commandes
	    	  majLotService.demarre(nomOperation, argsSpecifiques);
	      }
      } catch (Exception e){
    	 
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
         String cheminFicConfSae) {

      String contextConfig = "/applicationContext-sae-lotinstallmaj.xml";

      return ContextFactory.createSAEApplicationContext(contextConfig,
            cheminFicConfSae);

   }

}
