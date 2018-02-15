package fr.urssaf.image.commons.jms.spring.modele;

import java.io.Serializable;

/**
 * Modèle du compte
 * 
 * 
 */
public class Account implements Serializable {

   private static final long serialVersionUID = 1L;

   private long idAccount;

   private String firstname;

   private String lastname;

   /**
    * 
    * @return n° du compte
    */
   public final long getIdAccount() {
      return idAccount;
   }

   /**
    * 
    * @param idAccount
    *           n° du compte
    */
   public final void setIdAccount(long idAccount) {
      this.idAccount = idAccount;
   }

   /**
    * 
    * @return prénom
    */
   public final String getFirstname() {
      return firstname;
   }

   /**
    * 
    * @param firstname
    *           prénom
    */
   public final void setFirstname(String firstname) {
      this.firstname = firstname;
   }

   /**
    * 
    * @return nom
    */
   public final String getLastname() {
      return lastname;
   }

   /**
    * 
    * @param lastname
    *           nom
    */
   public final void setLastname(String lastname) {
      this.lastname = lastname;
   }

   /**
    * 
    * @return contenu du compte
    */
   @Override
   public final String toString() {
      return "Account [idAccount=" + idAccount + ", firstname=" + firstname
            + ", lastname=" + lastname + "]";
   }

}
