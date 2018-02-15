package fr.urssaf.image.commons.xml.castor.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.commons.xml.castor.XMLWriter;
import fr.urssaf.image.commons.xml.castor.hibernate.HibernateSessionFactory;
import fr.urssaf.image.commons.xml.castor.modele.Auteur;
import fr.urssaf.image.commons.xml.castor.modele.Document;
import fr.urssaf.image.commons.xml.castor.modele.Documents;
import fr.urssaf.image.commons.xml.castor.modele.Etat;

public class XMLWriterTest {

   private static final String DIRECTORY;

   private static final String MAPPING;

   private static final Logger LOG = Logger.getLogger(XMLWriterTest.class);

   static {
      DIRECTORY = FilenameUtils.concat(SystemUtils.getJavaIoTmpDir()
            .getAbsolutePath(), "mappingXMLCastor");

      MAPPING = FilenameUtils.concat("src/test/resources/mapping/castor",
            "Document.xml");
   }

   private static XMLWriter<Document> xmlWriter;

   private static final String FORMAT = "dd/MM/yyyy";

   @BeforeClass
   public static void init() throws IOException, MappingException {
      File directory = new File(DIRECTORY);
      FileUtils.forceMkdir(directory);
      FileUtils.cleanDirectory(directory);

      xmlWriter = new XMLWriter<Document>(MAPPING);
   }

   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void xmlWriterTest() throws IOException, MarshalException,
         ValidationException, ParseException, MappingException {

      Document document = new Document("titre 0", this.parseDate("10/12/2010"));
      document.setId(1);
      Auteur auteur = new Auteur("auteur 2");
      auteur.setId(2);
      document.setAuteur(auteur);

      Etat etat0 = new Etat();
      etat0.setId(0);
      etat0.setDate(this.parseDate("10/12/1999"));
      etat0.setEtat("open");

      Etat etat1 = new Etat();
      etat1.setId(1);
      etat1.setDate(this.parseDate("10/12/1998"));
      etat1.setEtat("close");

      Etat etat2 = new Etat();
      etat2.setId(2);
      etat2.setDate(this.parseDate("10/12/1996"));
      etat2.setEtat("init");

      document.getEtats().add(etat0);
      document.getEtats().add(etat1);
      document.getEtats().add(etat2);

      String FILE = FilenameUtils.concat(DIRECTORY, "documents_castor.xml");

      FileWriter writer = new FileWriter(FILE);

      Documents documents = new Documents();

      documents.getDocuments().add(document);
      documents.getDocuments().add(
            new Document("titre 2", this.parseDate("12/07/1998")));

      xmlWriter.write(documents.getDocuments(), "documents", writer);

      writer.close();

   }

   @SuppressWarnings( { "unchecked", "PMD.JUnitTestsShouldIncludeAssert" })
   @Test
   @Ignore
   public void xmlWriterHibernateTest() throws IOException, MarshalException,
         ValidationException, MappingException {

      String FILE = FilenameUtils.concat(DIRECTORY,
            "documents_castor_hibernate.xml");
      FileWriter writer = new FileWriter(FILE);

      Session session = HibernateSessionFactory.currentSession();

      Criteria criteria = session.createCriteria(Document.class);
      criteria.setFetchMode("auteur", FetchMode.JOIN);
      criteria.setMaxResults(15000);

      List<Document> documents = criteria.list();

      xmlWriter.write(documents, "documents", writer);
      LOG.debug("nombre de document dans le xml:" + documents.size());
      writer.close();

      HibernateSessionFactory.closeSession();

   }

   private Date parseDate(String date) throws ParseException {

      return DateUtils.parseDate(date, new String[] { FORMAT });
   }

}
