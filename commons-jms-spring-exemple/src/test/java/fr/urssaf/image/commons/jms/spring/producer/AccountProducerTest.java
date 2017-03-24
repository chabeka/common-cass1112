package fr.urssaf.image.commons.jms.spring.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Queue;

import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.jms.spring.modele.Account;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml",
      "/applicationContext-jms-test.xml" })
public class AccountProducerTest  {

   @Autowired
   private AccountProducer producer;

   @Autowired
   private JmsTemplate jmsTemplate;

   @Autowired
   private Queue queue;

   private Account account;
   
   private BrokerService broker;

   @Before
   public void before() throws Exception {

      account = new Account();
      account.setIdAccount(0);
      account.setFirstname("prénom");
      account.setLastname("nom");

      broker = new BrokerService();
      broker.setPersistent(false);
      broker.start();

   }

   @After
   public void after() throws Exception {

      broker.stop();
   }

   @Test(timeout = 5000)
   public void sendAccount() throws JMSException {

      producer.sendAccount(account);
      
      Message msg = jmsTemplate.receive(queue);
      assertMessage(msg);

   }

   private void assertMessage(Message message) throws JMSException {

      if (message instanceof MapMessage) {

         MapMessage mapMessage = (MapMessage) message;

         assertEquals("prénom", mapMessage.getString("firstname"));
         assertEquals("nom", mapMessage.getString("lastname"));
         assertEquals(0, mapMessage.getLong("idAccount"));

      } else {
         fail("Message must not be of type " + message.getClass());
      }
   }

}
