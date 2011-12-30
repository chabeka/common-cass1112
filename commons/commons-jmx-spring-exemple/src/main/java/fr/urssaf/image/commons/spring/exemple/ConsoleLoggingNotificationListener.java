package fr.urssaf.image.commons.spring.exemple;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;




public class ConsoleLoggingNotificationListener implements NotificationListener, NotificationFilter {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   public void handleNotification(Notification notification, Object handback) {
      System.out.println(notification);
      System.out.println(handback);
   }
   
   public boolean isNotificationEnabled(Notification notification) {
      return AttributeChangeNotification.class.isAssignableFrom(notification.getClass());
   }
   
   
   
}

