package fr.urssaf.image.commons.jms.spring;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.commons.jms.spring.consumer.AccountQueueConsumer;

/**
 * Classe executable de consommation
 * 
 * 
 */
public final class Consumer {

   private static final Logger LOG = Logger.getLogger(Consumer.class);

   private Consumer() {

   }

   /**
    * En mode synchrone le client s'arrête quand il reçoit le message En mode
    * asynchrone le client s'arrête au bout du temps imparti qu'il est reçu oui
    * au non le message (il peut dans ce mode en recevoir plusieurs)
    * 
    * arguments :
    * <ul>
    * <li>mode : synchrone/asynchrone/topic</li>
    * <li>time : temps d'attente en seconde (uniquement valable en mode
    * asynchrone et topic)</li>
    * <ul>
    * 
    * @param args
    *           arguments pour la consommation
    */
   public static void main(String[] args) {

      if (args[0].equals("synchrone")) {

         LOG.info("waiting synchronized account ...");

         ApplicationContext ctx = new ClassPathXmlApplicationContext(
               new String[] { "applicationContext.xml",
                     "applicationContext-jms.xml" });

         AccountQueueConsumer consumer = (AccountQueueConsumer) ctx
               .getBean("accountQueueConsumer");

         try {
            consumer.receiveAccount();
         } catch (JMSException e) {
            throw new IllegalStateException(e);
         }
      }

      else if (args[0].equals("asynchrone")) {

         LOG.info("waiting asynchronized account ...");

         new ClassPathXmlApplicationContext(new String[] {
               "applicationContext.xml", "applicationContext-jms.xml",
               "applicationContext-consumer-queue.xml" });

         sleep(args);

      }

      else if (args[0].equals("topic")) {

         LOG.info("waiting publisher account ...");

         new ClassPathXmlApplicationContext(new String[] {
               "applicationContext.xml", "applicationContext-jms.xml",
               "applicationContext-consumer-topic.xml" });

         sleep(args);

      }

   }

   private static final long COEF = 1000;

   private static void sleep(String[] args) {

      long time = Long.parseLong(args[1]) * COEF;

      try {
         Thread.sleep(time);
      } catch (InterruptedException e) {
         throw new IllegalStateException(e);
      }
   }

}
