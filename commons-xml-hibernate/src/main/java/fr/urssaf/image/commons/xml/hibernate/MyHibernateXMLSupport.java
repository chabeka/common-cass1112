package fr.urssaf.image.commons.xml.hibernate;

import java.io.IOException;

import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class MyHibernateXMLSupport<T> {

   private final SessionFactory sessionFactory;

   private final Class<T> table;

   public MyHibernateXMLSupport(Class<T> table, SessionFactory sessionFactory) {
      this.table = table;
      this.sessionFactory = sessionFactory;
   }

   public void writeAll(String file, String balise) throws IOException {

      AbstractXMLWriter xmlWriter = new AbstractXMLWriter(this.sessionFactory) {

         @Override
         protected void exWrite(XMLWriter writer, Session dom4jSession)
               throws IOException {

            ScrollableResults docs = dom4jSession.createCriteria(table)
                  .scroll();

            while (docs.next()) {

               Element document = (Element) docs.get(0);
               writer.write(document);
               dom4jSession.evict(document);
            }

         }

      };

      xmlWriter.write(file, balise);

   }

}
