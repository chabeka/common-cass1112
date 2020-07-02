package fr.urssaf.image.parser_opencsv.application.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.SSHException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.xfer.InMemorySourceFile;

//@Service
public class SenderOverSSH {

   // @Autowired
   private SSHClient sshClient;

   public void writeFileFromResource(final String targetPath, final String filePath)
         throws IOException {
      final File file = new File(filePath);
      final InputStream stream = new FileInputStream(file);
      final byte[] data = IOUtils.toByteArray(stream);
      final InMemorySourceFile source = new InMemorySourceFile() {
         @Override
         public String getName() {
            return file.getName();
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
      sshClient.close();
   }

   public String execute(final String command) throws IOException {
      try (final Session session = sshClient.startSession()) {
         final Command cmd = session.exec(command);
         cmd.join(15, TimeUnit.DAYS);
         final String out = IOUtils.toString(cmd.getInputStream(), StandardCharsets.UTF_8);
         if (cmd.getExitStatus() != 0) {
            throw new SSHException("Erreur lors de l'exécution de la commande " + command + " : "
                  + IOUtils.toString(cmd.getErrorStream(), StandardCharsets.UTF_8));
         }
         return out;
      }
   }
}
