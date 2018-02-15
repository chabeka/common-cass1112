package fr.urssaf.image.commons.jms.spring.consumer;

import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.jms.spring.consumer.message.AccountMessage;
import fr.urssaf.image.commons.jms.spring.modele.Account;
import fr.urssaf.image.commons.jms.spring.modele.AccountFactory;

/**
 * Classe de consommation d'un message jms point à point
 * 
 * 
 */
@Component
public class AccountQueueConsumer {

   private static final Logger LOG = Logger.getLogger(AccountQueueConsumer.class);

   @Autowired
   private JmsTemplate jmsTemplate;

   @Autowired
   @Qualifier("queue")
   private Destination destinataire;

   /**
    * Reçoit les message du destinataire en mode synchronisé
    * 
    * <pre>
    * &lt;bean id="queue" class="org.apache.activemq.command.ActiveMQQueue">
    *       &lt;constructor-arg value="account-test" />
    * &lt;/bean>
    * </pre>
    * 
    * @throws JMSException
    *            exception levée par jms
    */
   @Transactional
   public final void receiveAccount() throws JMSException {
      
      Message message = this.jmsTemplate.receive(this.destinataire);
      LOG.info("receiving account: "+AccountMessage.loadAccount(message));
   }

   /**
    * Reçoit les message du destinataire en mode asynchronisé
    * 
    * <pre>
    * &lt;jms:listener-container connection-factory="connectionFactory"
    *       acknowledge="auto" transaction-manager="transactionManager" >
    *       &lt;jms:listener destination="account-test"
    *          ref="accountConsumer" method="receiveAccount" />
    * &lt;/jms:listener-container>
    * </pre>
    * 
    * 
    * @param message
    *           message JMS
    * @throws JMSException
    *            exception levée par jms
    */
   public final void receiveAccount(Map<String, Object> message)
         throws JMSException {

      String firstname = (String) message.get("firstname");
      String lastname = (String) message.get("lastname");
      long idAccount = (Long) message.get("idAccount");

      Account account = AccountFactory.createAccount(idAccount, firstname, lastname);

      LOG.info("receiving account: " + account.toString());
   }

  

}
