package fr.urssaf.image.commons.jms.spring.consumer.message;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import fr.urssaf.image.commons.jms.spring.modele.Account;
import fr.urssaf.image.commons.jms.spring.modele.AccountFactory;

/**
 * Classe des {@link Message} contenant {@link Account}
 * 
 * 
 */
public final class AccountMessage {

   private AccountMessage() {

   }

   /**
    * retourne le compte contenu dans le message jms
    * 
    * @param message
    *           message jms
    * @return instance de compte
    * @throws JMSException
    *            exception levée par jms
    */
   public static Account loadAccount(Message message) throws JMSException {

      if (message instanceof MapMessage) {

         MapMessage mapMessage = (MapMessage) message;
         String firstname = mapMessage.getString("firstname");
         String lastname = mapMessage.getString("lastname");
         long idAccount = mapMessage.getLong("idAccount");

         return AccountFactory.createAccount(idAccount, firstname, lastname);

      } else {
         throw new IllegalArgumentException("Message must not be of type "
               + message.getClass());
      }
   }

}
