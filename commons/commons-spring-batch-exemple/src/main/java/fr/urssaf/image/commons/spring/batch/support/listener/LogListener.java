package fr.urssaf.image.commons.spring.batch.support.listener;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;
import fr.urssaf.image.commons.spring.batch.model.xml.livre.LivreType;

@Component
public class LogListener {

   private static final Logger LOG = Logger.getLogger(LogListener.class);

   private StepExecution stepExecution;

   @BeforeStep
   public void beforeStep(StepExecution stepExecution) {

      this.stepExecution = stepExecution;

      LOG.trace("before step: " + stepExecution.getStepName());

   }

   @AfterStep
   public ExitStatus afterStep(StepExecution stepExecution) {

      ExitStatus exitStatus = stepExecution.getExitStatus();

      LOG.trace("after step: " + stepExecution.getStepName());

      return exitStatus;

   }

   @BeforeRead
   public void beforeRead() {

      LOG.trace("before read: " + stepExecution.getStepName());
   }

   @AfterRead
   public void afterRead(JAXBElement<LivreType> item) {

      LOG.trace("after read: " + stepExecution.getStepName());
   }

   @OnReadError
   public void onReadError(Exception exception) {

      LOG.error("read error: " + stepExecution.getStepName(), exception);
   }

   @BeforeWrite
   public void beforeWrite(List<? extends Livre> items) {

      LOG.trace("before write: " + stepExecution.getStepName());
   }

   @AfterWrite
   public void afterWrite(List<? extends Livre> items) {

      LOG.trace("after write: " + stepExecution.getStepName());
   }

   @OnWriteError
   public void onWriteError(Exception exception, List<LivreType> items) {

      LOG.error("write error: " + stepExecution.getStepName(), exception);
   }

   @BeforeProcess
   public void beforeProcess(JAXBElement<LivreType> item) {

      LOG.trace("before process: " + stepExecution.getStepName());
   }

   @AfterProcess
   public void afterProcess(JAXBElement<LivreType> item, Livre result) {

      LOG.trace("after process: " + stepExecution.getStepName());
   }

   @OnProcessError
   public void onProcessError(JAXBElement<LivreType> item, Exception exception) {

      LOG.error("write error: " + stepExecution.getStepName(), exception);
   }

   @AfterChunk
   public void afterChunk() {

      LOG.trace("after chunck: " + stepExecution.getStepName());
   }

   @BeforeChunk
   public void beforeChunk() {

      LOG.trace("before chunck: " + stepExecution.getStepName());
   }
}
