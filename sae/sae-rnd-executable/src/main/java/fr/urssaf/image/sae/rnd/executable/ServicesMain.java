package fr.urssaf.image.sae.rnd.executable;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

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

   private ServicesMain() {

   }

   /**
    * Méthode appelée lors de l'exécution du traitement
    * 
    * @param args
    *           arguments de l'exécutable
    */
   public static void main(String[] args) {

      if (ArrayUtils.getLength(args) <= 0 || !StringUtils.isNotBlank(args[0])) {
         throw new IllegalArgumentException(
               "L'opération du traitement doit être renseignée.");
      }

      String[] newArgs = (String[]) ArrayUtils.subarray(args, 1, args.length);

      if ("MAJ_RND".equals(args[0])) {

         MajRndMain.main(newArgs);

      } else if ("MAJ_CORRESPONDANCE_RND".equals(args[0])) {

         MajCorrespondancesMain.main(newArgs);

      } else {
         throw new IllegalArgumentException("L'opération du traitement '"
               + args[0] + "' est inconnu.");
      }

   }
}
