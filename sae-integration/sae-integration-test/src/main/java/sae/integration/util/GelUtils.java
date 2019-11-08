/**
 *
 */
package sae.integration.util;

import net.schmizz.sshj.SSHClient;
import sae.integration.environment.Environment;

/**
 * Classe utilitaire facilitant le gel/dégel de documents
 */
public final class GelUtils {

   private static final String COMMAND = "java -jar /hawai/data/ged/ged_batch_docs_executable/sae-batch-docs-executable.jar";
   private GelUtils() {
      // Classe statique
   }

   /**
    * Lance le gel d'un document
    * 
    * @throws Exception
    */
   public static void gelDocument(final Environment environment, final String uuidDocument) throws Exception {
      final String command = COMMAND + " GEL_DOCUMENT " + environment.getEnvCode() + " " + uuidDocument;
      final SSHClient sshClient = SSHHelper.getSSHClient(environment);
      SSHHelper.execute(sshClient, command);
   }

   /**
    * Lance le dégel d'un document
    * 
    * @throws Exception
    */
   public static void degelDocument(final Environment environment, final String uuidDocument) throws Exception {
      final String command = COMMAND + " DEGEL_DOCUMENT " + environment.getEnvCode() + " " + uuidDocument;
      final SSHClient sshClient = SSHHelper.getSSHClient(environment);
      SSHHelper.execute(sshClient, command);
   }

}
