package fr.urssaf.image.sae.services.batch.transfert.support.resultats.batch;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.xsd.XsdValidationSupport;
import fr.urssaf.image.sae.services.batch.common.support.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.transfert.support.resultats.ResultatsFileEchecTransfertSupport;

/**
 * Tasklet pour l'écriture du fichier resultats.xml lors d'un échec de
 * traitement de transfert de masse
 * 
 */
@Component
public class ResultatsFileFailureTransfertTasklet extends AbstractResultatsFileFailureTransfertTasklet{

   @Autowired
   private ResultatsFileEchecTransfertSupport support;

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
   protected final ConcurrentLinkedQueue<?> getIntegratedDocuments() {
      return executor.getIntegratedDocuments();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ResultatsFileEchecTransfertSupport getResultatsFileEchecSupport() {
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
