package fr.urssaf.image.commons.spring.batch.support.callback;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.stereotype.Component;

@Component
public class FileCallback implements FlatFileHeaderCallback,
      FlatFileFooterCallback {

   @Override
   public void writeHeader(Writer writer) throws IOException {

      //écriture d'un en-tête
      writer.write("Liste des livres");

   }

   @Override
   public void writeFooter(Writer writer) throws IOException {
      
      //écriture d'un pied de page
      writer.write("Copyright Arkham Library");

   }

}
