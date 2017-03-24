package fr.urssaf.image.commons.xml.castor.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateSessionFactory {

   private HibernateSessionFactory() {

   }

   public static ThreadLocal<Session> session = new ThreadLocal<Session>();

   private static SessionFactory sessionFactory;

   private static Configuration cfg = new Configuration();

   private final static String CONFIG = "hibernate.cfg.xml";

   @SuppressWarnings("PMD.NonThreadSafeSingleton")
   public static Session currentSession() {
      Session sess = session.get();
      if (sess == null || !sess.isConnected()) {
         if (sessionFactory == null) {

            cfg.configure(CONFIG);
            sessionFactory = cfg.buildSessionFactory();

         }
         sess = sessionFactory.openSession();
         session.set(sess);
      }
      return sess;
   }

   public static void closeSession() {
      Session sess = (Session) session.get();
      session.set(null);
      if (sess != null) {
         sess.close();
      }
   };

}
