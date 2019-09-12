
package sae.integration.manual;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.LoggerFactory;
import net.schmizz.sshj.common.StreamCopier;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;

/**
 * Tests de communication SSH avec les serveurs d'appli
 */
public class SSHTest {

   @Test
   public void test() throws Exception {
      try (final SSHClient ssh = getSSHClient()) {
         ssh.connect("hwi31devgnsv6boappli1.gidn.recouv");
         ssh.authPassword("root", "hwicnir");

         final String src = "c:/temp/13.pdf";
         ssh.newSCPFileTransfer().upload(new FileSystemFile(src), "/tmp/");
      }
   }

   @Test
   public void test2() throws Exception {
      try (final SSHClient ssh = getSSHClient()) {
         // ssh.connect("hwi31devgnsv6boappli1.gidn.recouv");
         ssh.connect("hwi31picgntboappli2.gidn.recouv");
         ssh.authPassword("root", "hwicnir");
         try (final Session session = ssh.startSession()) {
            final Command cmd = session.exec("echo 'toto' && sleep 1 && echo 'titi'");
            dumpOutput(cmd.getInputStream());
            // System.out.println("input : " + IOUtils.readFully(cmd.getInputStream()).toString());
            cmd.join(15, TimeUnit.SECONDS);
            System.out.println("exit status: " + cmd.getExitStatus());
         }
         try (final Session session = ssh.startSession()) {
            final Command cmd = session.exec("echo 'toto' && sleep 1 && echo 'titi'");
            dumpOutput(cmd.getInputStream());
            // System.out.println("input : " + IOUtils.readFully(cmd.getInputStream()).toString());
            cmd.join(5, TimeUnit.SECONDS);
            System.out.println("exit status: " + cmd.getExitStatus());
         }

      }
   }

   public SSHClient getSSHClient() {
      final SSHClient ssh = new SSHClient();
      ssh.addHostKeyVerifier(new PromiscuousVerifier());
      return ssh;
   }

   public void dumpOutput(final InputStream inputStream) throws IOException {
      new StreamCopier(inputStream, System.out, LoggerFactory.DEFAULT).copy();
   }

}