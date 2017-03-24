package fr.urssaf.image.commons.jms.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.commons.jms.spring.modele.AccountFactory;
import fr.urssaf.image.commons.jms.spring.producer.AccountProducer;

/**
 * Classe exécutable d'envoi des messages
 * 
 * 
 */
public final class Producer {

   private Producer() {

   }

   /**
    * arguments
    * <ul>
    * <li>mode : queue/topic</li>
    * <li>prénom</li>
    * <li>nom</li>
    * </ul>
    * 
    * @param args
    *           arguments de l'expéditeur du message
    */
   public static void main(String[] args) {
      ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
            "applicationContext.xml", "applicationContext-jms.xml" });

      AccountProducer producer = (AccountProducer) ctx
            .getBean("accountProducer");

      if ("queue".equals(args[0])) {

         producer.sendAccount(AccountFactory.createAccount(args[1], args[2]));
      } else if ("topic".equals(args[0])) {

         producer
               .publishAccount(AccountFactory.createAccount(args[1], args[2]));
      }

   }

}
