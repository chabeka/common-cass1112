package fr.urssaf.image.commons.xml.hibernate;

import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractXMLWriter {

   private final SessionFactory sessionFactory;

   public AbstractXMLWriter(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }

   public void write(String file, String balise) throws IOException {

      org.dom4j.Document doc = DocumentHelper.createDocument();
      OutputFormat format = OutputFormat.createPrettyPrint();
      XMLWriter writer = new XMLWriter(new FileWriter(file), format);
      Element root = doc.addElement(balise);
      writer.writeOpen(root);

      Session session = sessionFactory.openSession();
      Session dom4jSession = session.getSession(EntityMode.DOM4J);

      exWrite(writer, dom4jSession);

      dom4jSession.close();
      session.close();

      writer.writeClose(root);
      writer.close();

   }

   protected abstract void exWrite(XMLWriter writer, Session dom4jSession) throws IOException;

}
