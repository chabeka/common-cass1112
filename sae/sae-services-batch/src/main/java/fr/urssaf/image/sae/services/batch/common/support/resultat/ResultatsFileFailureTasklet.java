/**
 * 
 */
package fr.urssaf.image.sae.services.batch.common.support.resultat;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.batch.AbstractResultatsFileFailureTasklet;
import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.xsd.XsdValidationSupport;
import fr.urssaf.image.sae.services.batch.modification.support.stockage.multithreading.ModificationPoolThreadExecutor;

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
   private ModificationPoolThreadExecutor executor;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ConcurrentLinkedQueue<?> getIntegratedDocuments() {
      return executor.getIntegratedDocuments();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ResultatsFileEchecSupport getResultatsFileEchecSupport() {
      return support;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final SommaireFormatValidationSupport getSommaireFormatValidationSupport() {
      return validationSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final XsdValidationSupport getXsdValidationSupport() {
      return xsdValidationSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final boolean isVirtual() {
      return false;
   }

}