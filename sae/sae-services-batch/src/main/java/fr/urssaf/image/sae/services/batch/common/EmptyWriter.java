/**
 * 
 */
package fr.urssaf.image.sae.services.batch.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * Cette classe est employée dans le cas où l'on a un chunck sans avoir à
 * ecrire. la méthode write ne réalise rien, mise à part logger le passage
 * 
 */
@Component
public class EmptyWriter implements ItemWriter<Object> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(EmptyWriter.class);

   private static final String PREFIXE_TRC = "EmptyWriter.write()";

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final void write(final List<? extends Object> items) throws Exception {

      LOGGER.debug("{} - écriture non specifiée", PREFIXE_TRC);
   }

}
