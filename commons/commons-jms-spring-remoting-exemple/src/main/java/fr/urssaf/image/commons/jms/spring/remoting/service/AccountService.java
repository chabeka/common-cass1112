package fr.urssaf.image.commons.jms.spring.remoting.service;

import fr.urssaf.image.commons.jms.spring.remoting.modele.Account;


/**
 * Services disponible
 * 
 * 
 */
public interface AccountService {

   /**
    * supprimer un compte
    * 
    * @param accountId
    *           n° du compte à supprimer
    *           
    * @return compte supprimé
    */
   Account cancelAccount(long accountId);

   /**
    * enregsitre un compte
    * 
    * @param firstname
    *           prénom
    * @param lastname
    *           nom
    * @return compte enregistré
    */
   Account saveAccount(String firstname, String lastname);

   /**
    * lecture du compte
    * 
    * @param accountId
    *           n° de compte
    * @return compte correspondant au n° de compte
    */
   Account readAccount(long accountId);
}
