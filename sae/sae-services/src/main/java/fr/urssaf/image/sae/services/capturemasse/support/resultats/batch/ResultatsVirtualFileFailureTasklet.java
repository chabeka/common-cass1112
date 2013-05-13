/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadVirtualExecutor;
import fr.urssaf.image.sae.services.capturemasse.support.xsd.XsdValidationSupport;

/**
 * Tasklet pour l'écriture du fichier resultats.xml dans le cas d'un échec de
 * traitement pour les documents virtuels
 * 
 */
@Component
public class ResultatsVirtualFileFailureTasklet extends
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
   private InsertionPoolThreadVirtualExecutor executor;

   /**
    * {@inheritDoc}
    */
   @Override
   ConcurrentLinkedQueue<?> getIntegratedDocuments() {
      return executor.getIntegratedDocuments();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   ResultatsFileEchecSupport getResultatsFileEchecSupport() {
      return support;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   SommaireFormatValidationSupport getSommaireFormatValidationSupport() {
      return validationSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   XsdValidationSupport getXsdValidationSupport() {
      return xsdValidationSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   boolean isVirtual() {
      return true;
   }

}
