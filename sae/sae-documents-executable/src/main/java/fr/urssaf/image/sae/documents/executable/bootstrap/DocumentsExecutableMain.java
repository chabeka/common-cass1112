package fr.urssaf.image.sae.documents.executable.bootstrap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de lancement des traitements.
 */
public class DocumentsExecutableMain {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(DocumentsExecutableMain.class);

   public static final String VERIFICATION_FORMAT = "VERIFICATION_FORMAT";
   public static final String HELP = "help";

   public static final String[] AVAIBLE_SERVICES = new String[] { VERIFICATION_FORMAT };

   /**
    * Méthode appelée lors du lancement.
    * 
    * @param args
    *           arguments passés en paramètres
    */
   public static void main(String[] args) {

      LOGGER.info("Arguments de la ligne de commande : {}", StringUtils.join(
            args, ' '));

      DocumentsExecutableMain documentsExecutableMain = new DocumentsExecutableMain();
      documentsExecutableMain.execute(args);
   }

   /**
    * Methode permettant d'exécuter le traitement.
    * 
    * @param args
    *           arguments passés en paramètres
    */
   protected void execute(String[] args) {

      if (args.length > 0 && VERIFICATION_FORMAT.equals(args[0])) {
         LOGGER.warn("L'opération du traitement doit être renseigné.");

         return;
      }
   }
}
