package fr.urssaf.image.sae.services.executable;

import org.apache.commons.lang.ArrayUtils;

import fr.urssaf.image.sae.services.executable.capturemasse.CaptureMasseMain;
import fr.urssaf.image.sae.services.executable.traitementmasse.TraitementMasseMain;
import fr.urssaf.image.sae.services.executable.util.ValidateUtils;

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

      if (!ValidateUtils.isNotBlank(args, 0)) {
         throw new IllegalArgumentException(
               "L'opération du traitement doit être renseigné.");
      }

      String[] newArgs = (String[]) ArrayUtils.subarray(args, 1, args.length);

      if ("captureMasse".equals(args[0])) {

         CaptureMasseMain.main(newArgs);

      }
      else if ("traitementMasse".equals(args[0])) {

         TraitementMasseMain.main(newArgs);

      }

      else {
         throw new IllegalArgumentException("L'opération du traitement '"
               + args[0] + "' est inconnu.");
      }

   }
}
