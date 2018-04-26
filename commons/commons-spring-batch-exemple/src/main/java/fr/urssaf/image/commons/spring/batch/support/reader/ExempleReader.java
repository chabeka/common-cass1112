package fr.urssaf.image.commons.spring.batch.support.reader;

import org.apache.log4j.Logger;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;
import fr.urssaf.image.commons.spring.batch.service.ReaderService;

@Component
public class ExempleReader extends
      AbstractItemCountingItemStreamItemReader<Livre> implements
      ItemReader<Livre> {

   private static final Logger LOG = Logger.getLogger(ExempleReader.class);

   @Autowired
   private ReaderService readerService;

   public ExempleReader() {
      super();
      super.setName("exempleReader");
      this.setMaxItemCount(10);

   }

   @Override
   protected void doClose() {
      // pas d'implémentation

   }

   @Override
   protected void doOpen() {
      // pas d'implémentation

   }

   @Override
   protected Livre doRead() {

      Livre livre = new Livre();
      livre.setIdentifiant(this.getCurrentItemCount());
      livre.setTitre("titre_" + this.getCurrentItemCount());
      livre.setAuteur("auteur_" + this.getCurrentItemCount());

      readerService.traitement(livre);

      return livre;
   }

   @AfterRead
   public void afterRead(Livre item) {

      LOG.debug("after read: " + item);
   }

}
