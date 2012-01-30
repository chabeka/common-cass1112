package fr.urssaf.image.commons.spring.batch.support.processor;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;
import fr.urssaf.image.commons.spring.batch.service.ProcessorService;

@Component
public class ExempleProcessor implements ItemProcessor<Livre, Livre> {

   private static final Logger LOG = Logger.getLogger(ExempleProcessor.class);

   @Autowired
   private ProcessorService processorService;

   @Override
   public Livre process(Livre item) {

      LOG.debug("processor " + item.getTitre());
      processorService.traitement(item);

      return item;
   }

}
