package sae.integration.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.SSHException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.InMemorySourceFile;
import sae.integration.environment.Environment;

/**
 * Méthodes utilitaires concernant les transferts SSH
 */
public class SSHHelper {

   private SSHHelper() {
      // Classe statique
   }

   /**
    * Renvoie un client SSH permettant d'accéder au serveur d'appli de l'environnement
    * 
    * @param environment
    * @return
    * @throws Exception
    */
   public static SSHClient getSSHClient(final Environment environment) throws Exception {
      final SSHClient sshClient = new SSHClient();
      sshClient.addHostKeyVerifier(new PromiscuousVerifier());
      sshClient.connect(environment.getAppliServer());
      sshClient.authPassword("root", "hwicnir");
      return sshClient;
   }

   /**
    * Creates over overwrites a text file with given content, in utf-8 encoding
    * 
    * @param targetPath
    *           the full path to the file on the host
    * @param contents
    *           the desired contents of the file
    */
   public static void writeTextFile(final SSHClient sshClient, final String targetPath, final String contents) throws IOException {
      final byte[] data = contents.getBytes(StandardCharsets.UTF_8);

      final InMemorySourceFile source = new InMemorySourceFile() {
         @Override
         public String getName() {
            return "file";
         }

         @Override
         public long getLength() {
            return data.length;
         }

         @Override
         public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
         }
      };
      sshClient.newSCPFileTransfer().upload(source, targetPath);
   }

   public static void writeFileFromResource(final SSHClient sshClient, final Object object, final String targetPath, final String resourcePath)
         throws IOException {
      final InputStream stream = ResourceUtils.loadResource(object, resourcePath);
      final byte[] data = IOUtils.toByteArray(stream);
      final InMemorySourceFile source = new InMemorySourceFile() {
         @Override
         public String getName() {
            return "file";
         }

         @Override
         public long getLength() {
            return data.length;
         }

         @Override
         public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
         }
      };
      sshClient.newSCPFileTransfer().upload(source, targetPath);
   }

   public static String execute(final SSHClient sshClient, final String command) throws Exception {
      try (final Session session = sshClient.startSession()) {
         final Command cmd = session.exec(command);
         cmd.join(15, TimeUnit.DAYS);
         final String out = IOUtils.toString(cmd.getInputStream());
         if (cmd.getExitStatus() != 0) {
            throw new SSHException("Erreur lors de l'exécution de la commande " + command + " : " + IOUtils.toString(cmd.getErrorStream()));
         }
         return out;
      }

   }
}
