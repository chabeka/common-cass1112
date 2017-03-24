package fr.urssaf.image.commons.webservice.axis.client.jms;

import java.util.Date;
import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQMessage;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.webservice.axis.client.util.TransformerUtils;

/**
 * Classe de consommation d'un message jms par abonnement
 * 
 * 
 */
@Component
public class UserGuideConsumer implements MessageListener {

   private static final Logger LOG = Logger.getLogger(UserGuideConsumer.class);

   @Override
   public final void onMessage(Message message) {

      try {
         this.receiveMessage(message);
      } catch (JMSException e) {
         throw new IllegalStateException(e);
      }

   }

   private void receiveMessage(Message message) throws JMSException {

      LOG.debug("\nnew message");
      LOG.debug("JMSDestination: " + message.getJMSDestination());
      LOG.debug("JMSReplyTo: " + message.getJMSReplyTo());
      LOG.debug("JMSMessageID: " + message.getJMSMessageID());
      LOG.debug("JMSType: " + message.getJMSType());
      LOG.debug("JMSCorrelationID: " + message.getJMSCorrelationID());
      LOG.debug("JMSRedelivered: " + message.getJMSRedelivered());
      LOG.debug("JMSExpiration: " + message.getJMSExpiration());
      LOG.debug("JMSPriority: " + message.getJMSPriority());
      LOG.debug("JMSTimestamp: "
            + DateFormatUtils.format(new Date(message.getJMSTimestamp()),
                  "dd/MM/yyyy HH:mm:ss:SSS"));
      LOG.debug("JMSDeliveryMode: " + message.getJMSDeliveryMode());
      @SuppressWarnings("unchecked")
      Enumeration<String> properties = message.getPropertyNames();

      while (properties.hasMoreElements()) {

         String property = properties.nextElement();
         LOG.debug(property + ": " + message.getStringProperty(property));
      }

      if (message instanceof TextMessage) {

         TextMessage textMessage = (TextMessage) message;

         LOG.debug("MESSAGE SOAP:\n"
               + TransformerUtils.print(textMessage.getText()));
      } else if (message instanceof ActiveMQMessage) {

         ActiveMQMessage activeMQMessage = (ActiveMQMessage) message;

         LOG.debug(activeMQMessage);
      }

   }

   protected static void topicConsumer() {

      LOG.debug("waiting publisher UserGuide ...");

      new ClassPathXmlApplicationContext(new String[] {
            "applicationContext.xml", "applicationContext-jms.xml",
            "applicationContext-consumer-userguide.xml" });

      try {
         Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException e) {
         throw new IllegalArgumentException(e);
      }

      LOG.debug("ending publisher UserGuide ...");

   }

   private static final long SLEEP_TIME = 10000000;

   /**
    * Ecouteur sur un topic
    * 
    * @param args
    *           aucun paramètre prise en compte
    */
   public static void main(String[] args) {

      topicConsumer();

   }

}
