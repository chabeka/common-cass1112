package fr.urssaf.image.commons.jms.spring.remoting.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.jms.spring.remoting.modele.Account;
import fr.urssaf.image.commons.jms.spring.remoting.service.AccountService;


/**
 * Classe d'implémentation de {@link AccountService}
 * 
 * 
 */
@Service
public class AccountServiceImpl implements AccountService {

   private static final Logger LOG = Logger.getLogger(AccountServiceImpl.class);

   private final Map<Long, Account> accounts;

   /**
    * instanciation d'un tableau de compte
    */
   public AccountServiceImpl() {

      accounts = new HashMap<Long, Account>();
      this.init();

   }

   private void init() {

      // chargement de comptes pour les tests
      this.saveAccount("john", "Doe");
      this.saveAccount("ward", "Wayne");
   }

   @Override
   public final Account cancelAccount(long accountId) {

      Account account = accounts.remove(accountId);

      if (account == null) {
         LOG.info("accountId [" + accountId + "] doesn't exist");

      } else {
         LOG.info("Cancelling account: " + account.toString());
      }

      return account;

   }

   @Override
   public final Account readAccount(long accountId) {

      Account account = accounts.get(accountId);

      if (account == null) {
         LOG.info("accountId [" + accountId + "] doesn't exist");
      } else {
         LOG.info("Reading account: " + account.toString());

      }

      return account;
   }

   @Override
   public final Account saveAccount(String firstname, String lastname) {

      Account account = new Account();
      account.setFirstname(firstname);
      account.setLastname(lastname);
      account.setIdAccount(getSequence());

      accounts.put(account.getIdAccount(), account);

      LOG.info("Saving account: " + account.toString());

      return account;
   }

   private long sequence = 0;

   protected final long getSequence() {

      synchronized (this) {

         try {
            return sequence;
         } finally {
            sequence++;
         }
      }
   }

}
