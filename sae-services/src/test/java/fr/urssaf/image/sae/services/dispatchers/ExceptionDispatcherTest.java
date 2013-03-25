/**
 * 
 */
package fr.urssaf.image.sae.services.dispatchers;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.urssaf.image.sae.services.dispatchers.handlers.SpyHandler;
import fr.urssaf.image.sae.services.dispatchers.handlers.ThrowerExceptionHandler;


/**
 * Teste le dispatcher d'exceptions et sa chaine de responsabilité.
 * 
 */
public class ExceptionDispatcherTest {

   ExceptionDispatcher dispatcher = new ExceptionDispatcher();
   Exception exception = new ExceptionDispatcherTestException();

   /**
    * Vérifie qu'il n'y a pas de plantage si aucun handler n'est défini.
    * 
    * @throws Exception
    */
   @Test
   public void noHandler() throws Exception {
      dispatcher.setHandler(null);
      dispatcher.dispatch(exception);
   }

   /**
    * Vérifie le handler dont le but est simplement de lever l'exception.
    * 
    * @throws Exception
    */
   @Test(expected=ExceptionDispatcherTestException.class)
   public void throwerExceptionHandler() throws Exception {
      dispatcher.setHandler(new ThrowerExceptionHandler());
      dispatcher.dispatch(exception);
   }

   /**
    * Vérifie que chaque handler est appelé.
    * 
    * @throws Exception
    */
   @Test
   public void chainTraversal() throws Exception {
      SpyHandler spy = new SpyHandler();
      SpyHandler spy2 = new SpyHandler();
      SpyHandler spy3 = new SpyHandler();
      
      assertFalse(spy.isCalled());
      assertFalse(spy2.isCalled());
      assertFalse(spy3.isCalled());
      
      spy.setSuccessor(spy2).setSuccessor(spy3);
      dispatcher.setHandler(spy);
      dispatcher.dispatch(exception);
      
      // On vérifie que les espions ont bien été appelés
      assertTrue(spy.isCalled());
      assertTrue(spy2.isCalled());
      assertTrue(spy3.isCalled());
   } 
   
   /**
    * Teste une chaine contenant un traitement (l'espion)
    * et qui se termine par une levée d'exception.
    * @throws Exception
    */
   @Test
   public void chainTraversalThenException() throws Exception {   
      SpyHandler spy = new SpyHandler();
      spy.setSuccessor(new ThrowerExceptionHandler());
      dispatcher.setHandler(spy);
      
      try {
         dispatcher.dispatch(exception);
         fail("Le dispatcher aurait dû lever une exception");
      } catch (Exception e) {
         assertTrue(exception == e);
         assertTrue(spy.isCalled());
      } 
   }

   
   /**
    * Dans une chaine avec 3 handlers, on simule une demande d'arrêt 
    * dans le deuxième handler. Le troisième handler ne doit pas être appelé.
    * 
    * @throws Exception
    */
   @Test
   public void chainBreak() throws Exception {
      SpyHandler spy = new SpyHandler();
      SpyHandler spy2 = new SpyHandler();
      SpyHandler spy3 = new SpyHandler();
      
      assertFalse(spy.isCalled());
      assertFalse(spy2.isCalled());
      assertFalse(spy3.isCalled());
      
      spy2.setCallNext(false);
      spy.setSuccessor(spy2).setSuccessor(spy3);
      dispatcher.setHandler(spy);
      dispatcher.dispatch(exception);
      
      // On vérifie que les deux premiers espions ont bien été appelés
      assertTrue(spy.isCalled());
      assertTrue(spy2.isCalled());
      // On vérifie que le dernier espion n'a PAS été appelé
      assertFalse(spy3.isCalled());
   }
}


/**
 * Exception utile pour ces tests uniquement
 */
@SuppressWarnings("serial")
class ExceptionDispatcherTestException extends Exception {};

