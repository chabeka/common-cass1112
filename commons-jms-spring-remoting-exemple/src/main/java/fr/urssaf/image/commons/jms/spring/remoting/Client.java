package fr.urssaf.image.commons.jms.spring.remoting;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.commons.jms.spring.remoting.modele.Account;
import fr.urssaf.image.commons.jms.spring.remoting.service.AccountService;


/**
 * Classe de consommation
 * 
 * 
 */
public final class Client {

   private static final Logger LOG = Logger.getLogger(Client.class);

   private Client() {

   }

   /**
    * paramètres:
    * <ul>
    * <li>read
    * <ul>
    * <li>n° de compte</li>
    * </ul>
    * </li>
    * <li>cancel
    * <ul>
    * <li>n° de compte</li>
    * </ul>
    * </li>
    * <li>save
    * <ul>
    * <li>prénom</li>
    * <li>nom</li>
    * </ul>
    * </li>
    * </ul>
    * 
    * @param args
    *           args du client
    */
   public static void main(String[] args) {

      ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
            "applicationContext-client.xml", "jms/applicationContext-jms.xml" });
      AccountService service = (AccountService) ctx.getBean("accountService");

      if ("read".equals(args[0])) {

         long accountId = Long.parseLong(args[1]);

         Account account = service.readAccount(accountId);
         if (account == null) {
            LOG.info("accountId [" + accountId + "] doesn't exist");
         } else {
            LOG.info(account.toString());
         }
      }

      if ("cancel".equals(args[0])) {

         long accountId = Long.parseLong(args[1]);

         Account account = service.cancelAccount(accountId);
         if (account == null) {
            LOG.info("accountId [" + accountId + "] doesn't exist");
         } else {
            LOG.info(account.toString());
         }

      }

      if ("save".equals(args[0])) {

         Account account = service.saveAccount(args[1], args[2]);

         LOG.info(account.toString());

      }

   }

}
