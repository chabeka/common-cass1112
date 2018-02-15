package fr.urssaf.image.commons.jms.spring.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.jms.spring.consumer.message.AccountMessage;

/**
 * Classe de consommation d'un message jms par abonnement
 * 
 * 
 */
@Component
public class AccountTopicConsumer implements MessageListener {

   private static final Logger LOG = Logger
         .getLogger(AccountTopicConsumer.class);

   @Override
   public final void onMessage(Message message) {

      try {
         this.receiveAccount(message);
      } catch (JMSException e) {
         throw new IllegalStateException(e);
      }

   }
   
   private void receiveAccount(Message message) throws JMSException {

      LOG.info("receiving account: " + AccountMessage.loadAccount(message));
   }

}
