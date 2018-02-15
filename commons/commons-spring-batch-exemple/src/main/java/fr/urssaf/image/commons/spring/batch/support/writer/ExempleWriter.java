package fr.urssaf.image.commons.spring.batch.support.writer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;
import fr.urssaf.image.commons.spring.batch.service.WriterService;

@Component
public class ExempleWriter implements ItemWriter<Livre> {

   private static final Logger LOG = Logger.getLogger(ExempleWriter.class);

   @Autowired
   private WriterService writerService;

   @BeforeWrite
   public void beforeWrite(List<? extends Livre> items) {

      LOG.debug("before write: nombre de livres à persister --> " + items.size());
   }

   @Override
   public void write(List<? extends Livre> items) {

      for (Livre livre : items) {

         // écriture d'un item Livre

         LOG.debug("write " + livre.getTitre());
         writerService.traitement(livre);

      }

   }

}
