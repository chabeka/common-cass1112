/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.capturemasse.support.xsd.XsdValidationSupport;

/**
 * Tasklet pour l'écriture du fichier resultats.xml lors d'un échec de
 * traitement
 * 
 */
@Component
public class ResultatsFileFailureTasklet extends
      AbstractResultatsFileFailureTasklet {

   @Autowired
   private ResultatsFileEchecSupport support;

   @Autowired
   private SommaireFormatValidationSupport validationSupport;

   @Autowired
   private XsdValidationSupport xsdValidationSupport;

   /**
    * Pool d'execution des insertions de documents
    */
   @Autowired
   private InsertionPoolThreadExecutor executor;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ConcurrentLinkedQueue<?> getIntegratedDocuments() {
      return executor.getIntegratedDocuments();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ResultatsFileEchecSupport getResultatsFileEchecSupport() {
      return support;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SommaireFormatValidationSupport getSommaireFormatValidationSupport() {
      return validationSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final XsdValidationSupport getXsdValidationSupport() {
      return xsdValidationSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isVirtual() {
      return false;
   }

}
