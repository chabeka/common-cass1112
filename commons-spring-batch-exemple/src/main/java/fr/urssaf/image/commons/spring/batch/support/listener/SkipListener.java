package fr.urssaf.image.commons.spring.batch.support.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;

@Component
public class SkipListener {

   private static final Logger LOG = Logger.getLogger(SkipListener.class);

   @OnSkipInRead
   public void onSkipInRead(Throwable cause) {

      LOG.error("skip sur lecture", cause);
   }

   @Autowired
   @Qualifier("fileWriter")
   private ItemWriter<Livre> resultatWriter;

   @OnSkipInProcess
   public void onSkipInProcess(Livre item, Throwable cause) {

      LOG.error("skip sur processus " + item.getTitre(), cause);

      write(item);
   }

   @OnSkipInWrite
   public void onSkipInWrite(Livre item, Throwable cause) {

      LOG.error("skip sur écriture " + item.getTitre(), cause);

      write(item);
   }

   private void write(Livre item) {

      List<Livre> items = new ArrayList<Livre>();

      items.add(item);

      try {
         resultatWriter.write(items);
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }
   }
}
