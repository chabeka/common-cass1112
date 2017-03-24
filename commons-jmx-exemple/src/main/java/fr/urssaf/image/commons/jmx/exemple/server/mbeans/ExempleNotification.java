package fr.urssaf.image.commons.jmx.exemple.server.mbeans;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class ExempleNotification
   extends NotificationBroadcasterSupport
   implements ExempleNotificationMBean {

   private int valeur = 1;
   
   private long numeroSequence = 1;
   
   @Override
   public String getNom() {
      return "Mon nom est en lecture seule";
   }

   @Override
   public int getValeur() {
      return valeur;
   }
   
   @Override
   public synchronized void setValeur(int valeur) {
      
      this.valeur = valeur;
      
      // Envoi d'une notification pour le changement de 'valeur'
      Notification notif = new AttributeChangeNotification(
            this,
            numeroSequence, 
            System.currentTimeMillis(),
            "Modification de l'attribut 'valeur'",
            "Valeur", 
            "int", 
            this.valeur, 
            valeur);
      numeroSequence++;
      sendNotification(notif); 

   }
   
   @Override
   public int add(int a, int b) {
      return a + b;
   }
   
   @Override
   public void rafraichir() {
      System.out.println("Rafraichir les donnees");
   }
   
   
   @Override 
   public MBeanNotificationInfo[] getNotificationInfo() { 
      
      // Description de la notification qui indique
      // que l'attribut Valeur a été modifié
      String[] types = new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE }; 
      String name = "valeur_modif";
      String description = "Modification de l'attribut Valeur"; 
      MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description); 
      return new MBeanNotificationInfo[] {info};
      
    }

}
