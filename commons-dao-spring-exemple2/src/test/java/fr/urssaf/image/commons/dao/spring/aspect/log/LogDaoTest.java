package fr.urssaf.image.commons.dao.spring.aspect.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;
import org.junit.Test;


/**
 * Tests unitaires de la classe {@link LogDao} 
 *
 */
@SuppressWarnings("PMD")
public class LogDaoTest {

   /**
    * Classe Mock pour utiliser ProceedingJoinPoint
    */
   private class MockPjp implements ProceedingJoinPoint {

      @Override
      public Object proceed() throws Throwable {
         
         // Attend quelques milli-secondes pour un message de log complet
         Thread.sleep(153);
         
         // Renvoie du résultat
         return "Toto" ;
         
      }

      @Override
      public Object proceed(Object[] args) throws Throwable {
         return null;
      }

      @Override
      public void set$AroundClosure(AroundClosure arc) {
      }

      @Override
      public Object[] getArgs() {
         return null;
      }

      @Override
      public String getKind() {
         return null;
      }

      @Override
      public Signature getSignature() {
         return null;
      }

      @Override
      public SourceLocation getSourceLocation() {
         return null;
      }

      @Override
      public StaticPart getStaticPart() {
         return null;
      }

      @Override
      public Object getTarget() {
         return null;
      }

      @Override
      public Object getThis() {
         return this;
      }

      @Override
      public String toLongString() {
         return null;
      }

      @Override
      public String toShortString() {
         return null;
      }
      
   }
   
   
   /**
    * Classe permettant de vérifier le message écrit dans le log 
    */
   private class VerifyAppender implements Appender {

      public String lastMessage;
      
      @Override
      public void addFilter(Filter newFilter) {
      }

      @Override
      public void clearFilters() {
      }

      @Override
      public void close() {
      }

      @Override
      public void doAppend(LoggingEvent event) {
         lastMessage = (String)event.getMessage();
      }

      @Override
      public ErrorHandler getErrorHandler() {
         return null;
      }

      @Override
      public Filter getFilter() {
         return null;
      }

      @Override
      public Layout getLayout() {
         return null;
      }

      @Override
      public String getName() {
         return null;
      }

      @Override
      public boolean requiresLayout() {
         return false;
      }

      @Override
      public void setErrorHandler(ErrorHandler errorHandler) {
      }

      @Override
      public void setLayout(Layout layout) {
      }

      @Override
      public void setName(String name) {
      }
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link LogDao#logControllerTime(ProceedingJoinPoint)}
    */
   @Test
   public void logControllerTime() throws Throwable {
      
      MockPjp pjp = new MockPjp();
      LogDao logDao = new LogDao();
      VerifyAppender verifyAppender = new VerifyAppender();
      
      LogDao.LOGGER.addAppender(verifyAppender);
      try {
      
         Object result = logDao.logControllerTime(pjp);
         
         String lastMessage = verifyAppender.lastMessage;
                  
         assertEquals(String.class,result.getClass());
         assertEquals("Toto",(String)result);
      
         if (!lastMessage.startsWith("MockPjp time:")) {
            fail("Le message est incorrect");
         }
         
      }
      finally {
         LogDao.LOGGER.removeAppender(verifyAppender);
      }
      
   }
   
}
