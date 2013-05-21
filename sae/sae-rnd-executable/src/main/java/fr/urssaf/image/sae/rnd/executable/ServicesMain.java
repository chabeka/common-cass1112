package fr.urssaf.image.sae.rnd.executable;

import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exécutable pour les différents services contenus dans ce module :
 * <ul>
 * <li><code>{0} : le premier argument indique le nom du traitement</code></li>
 * <li>
 * <code>{1}...{n} : les autres arguments sont spécifiques au traitement désigné par {0}</code>
 * </li>
 * </ul>
 * Le premier argument est obligatoire et doit être reconnu comme une opération
 * de traitement.<br>
 * <br>
 * Liste des opérations de traitement <br>
 * <ul>
 * <li>traitementMasse</li>
 * </ul>
 * 
 */
public final class ServicesMain {

   private static final String MAJ_RND = "MAJ_RND";
   private static final String MAJ_CORRESPONDANCES_RND = "MAJ_CORRESPONDANCES_RND";
   private static final int CONVERSION_MINUTES = 60000;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ServicesMain.class);

   private ServicesMain() {

   }

   /**
    * Méthode appelée lors de l'exécution du traitement
    * 
    * @param args
    *           arguments de l'exécutable
    * @throws Throwable 
    */
   public static void main(String[] args) throws Throwable {

      String prefix = "main()";
      LOGGER.debug("{} - début", prefix);
      LOGGER.info("{} - Arguments de la ligne de commande : {}", new Object[] {
            prefix, args });

      try {
         long startDate = new Date().getTime();

         if (ArrayUtils.getLength(args) <= 0
               || !StringUtils.isNotBlank(args[0])) {
            throw new IllegalArgumentException(
                  "L'opération du traitement doit être renseignée.");
         }

         String[] newArgs = (String[]) ArrayUtils
               .subarray(args, 1, args.length);

         if (MAJ_RND.equals(args[0])) {

            MajRndMain.main(newArgs);

         } else if (MAJ_CORRESPONDANCES_RND.equals(args[0])) {

            MajCorrespondancesMain.main(newArgs);

         } else {
            throw new IllegalArgumentException("L'opération du traitement '"
                  + args[0] + "' est inconnu.");
         }

         long endDate = new Date().getTime();
         long duree = (endDate - startDate) / CONVERSION_MINUTES;
         LOGGER.debug("{} - fin. Traitement réalisé en {} min", new Object[] {
               prefix, duree });

      } catch (Throwable ex) {

         LOGGER.error("Une erreur a eu lieu dans l'execution du jar sae-rnd-executable", ex);
         throw ex;

      } 
   }
}
