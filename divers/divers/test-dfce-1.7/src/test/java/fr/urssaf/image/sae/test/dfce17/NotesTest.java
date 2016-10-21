package fr.urssaf.image.sae.test.dfce17;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.note.Note;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobExecutionException;

@RunWith(BlockJUnit4ClassRunner.class)
public class NotesTest {

   private static final Logger LOGGER = LoggerFactory
   .getLogger(NotesTest.class);
   
   // Developpement 
   //private String urlDfce = "http://cer69-ds4int.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cer69imageint9.cer69.recouv";
   //private String hosts = "cer69imageint10.cer69.recouv";
   
   // Recette interne GNT
   //private String urlDfce = "http://hwi69devgntappli1.gidn.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69devgntcas1.gidn.recouv:9160,cnp69devgntcas2.gidn.recouv:9160";
   
   // Recette interne GNS
   //private String urlDfce = "http://hwi69devsaeapp1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
   
   // Integration cliente GNT
   //private String urlDfce = "http://hwi69intgntappli1.gidn.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
   
   // Integration cliente GNS
   private String urlDfce = "http://hwi69intgnsapp1.gidn.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   
   // Integration nationale GNT
   //private String urlDfce = "http://hwi69gingntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69gingntcas1.cer69.recouv:9160,cnp69gingntcas2.cer69.recouv:9160";
   
   // Integration nationale GNS
   //private String urlDfce = "http://hwi69ginsaeappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "hwi69ginsaecas1.cer69.recouv:9160,hwi69ginsaecas2.cer69.recouv:9160";
   
   // Validation nationale GNT
   //private String urlDfce = "http://hwi69givngntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69givngntcas1.cer69.recouv:9160,cnp69givngntcas2.cer69.recouv:9160,cnp69givngntcas3.cer69.recouv:9160";
   
   // Validation nationale GNS
   //private String urlDfce = "http://hwi69givnsaeappli.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
   
   // Pre-prod nationale GNT
   //private String urlDfce = "http://hwi69pregntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69pregntcas1.cer69.recouv:9160,cnp69pregntcas2.cer69.recouv:9160,cnp69pregntcas3.cer69.recouv:9160";
   
   // Pre-prod nationale GNS
   //private String urlDfce = "http://hwi69pregnsapp.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69pregnscas1.cer69.recouv,cnp69pregnscas2.cer69.recouv,cnp69pregnscas3.cer69.recouv,cnp69pregnscas4.cer69.recouv,cnp69pregnscas5.cer69.recouv,cnp69pregnscas6.cer69.recouv";
   
   // Prod nationale GNT
   //private String urlDfce = "http://hwi69gntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69gntcas1.cer69.recouv:9160,cnp69gntcas2.cer69.recouv:9160,cnp69gntcas3.cer69.recouv:9160";
   
   // Prod nationale GNS
   //private String urlDfce = "http://hwi69saeappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
   
   @Test
   public void getNotes() throws NoSuchDfceJobExecutionException {
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      provider.connect("_ADMIN", "DOCUBASE", urlDfce, 3 * 60 * 1000);
      
      UUID idDoc = UUID.fromString("1D3C98E1-2407-401D-9BB3-912738AE1657");
      
      List<Note> notes = provider.getNoteService().getNotes(idDoc);
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      for (Note note : notes) {
         LOGGER.debug("Note {} de {} à {} : {}", new String[]{ note.getUuid().toString(), note.getAuthor(), formatter.format(note.getCreationDate()), note.getContent()});
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
}
