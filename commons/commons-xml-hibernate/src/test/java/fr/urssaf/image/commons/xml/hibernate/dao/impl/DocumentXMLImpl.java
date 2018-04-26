package fr.urssaf.image.commons.xml.hibernate.dao.impl;

import java.io.IOException;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.xml.hibernate.AbstractXMLWriter;
import fr.urssaf.image.commons.xml.hibernate.MyHibernateXMLSupport;
import fr.urssaf.image.commons.xml.hibernate.dao.DocumentXML;
import fr.urssaf.image.commons.xml.hibernate.modele.Document;

@Repository
public class DocumentXMLImpl extends MyHibernateXMLSupport<Document> implements
      DocumentXML {

   private final SessionFactory sessionFactory;

   @Autowired
   public DocumentXMLImpl(
         @Qualifier("sessionFactory") SessionFactory sessionFactory) {
      super(Document.class, sessionFactory);
      this.sessionFactory = sessionFactory;
   }

   @Override
   public void writeAllDocument(String file) throws IOException {

      this.writeAll(file, "documents");

   }

   @Override
   public void writeDocument(String file, final int maxResult)
         throws IOException {

      AbstractXMLWriter xmlWriter = new AbstractXMLWriter(this.sessionFactory) {

         @SuppressWarnings("unchecked")
         @Override
         protected void exWrite(XMLWriter writer, Session dom4jSession)
               throws IOException {

            Criteria criteria = dom4jSession.createCriteria(Document.class);
            criteria.setFetchMode("auteur", FetchMode.JOIN);
            
            criteria.setMaxResults(maxResult);
           
            List<Element> documents = criteria.list();

            for (Element document : documents) {
               //Hibernate.initialize(document.);
               writer.write(document);

            }

         }

      };

      xmlWriter.write(file, "documents");

   }

}
