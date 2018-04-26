package fr.urssaf.image.commons.spring.batch.support.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BibliothequeWriter implements ItemWriter<Livre>, ItemStream,
      InitializingBean {

   private static final String B_NAMESPACE = "http://www.cirtil.fr/sae/common/bibliotheque";

   private static final String REF_NAMESPACE = "http://www.cirtil.fr/sae/common/bibliotheque_element";

   private static final String REF_PREFIX = "ref";

   private static final String B_PREFIX = "b";

   private XMLStreamWriter writer;

   @Override
   public void write(List<? extends Livre> items) {

      for (Livre livre : items) {
         try {
            this.itemWriter(livre, writer);
         } catch (XMLStreamException e) {
            new ItemStreamException(e);
         }
      }

   }

   @Override
   public void close() throws ItemStreamException {

      try {
         footerWriter(writer);
      } catch (XMLStreamException e) {
         new ItemStreamException(e);
      }

   }

   @Override
   public void afterPropertiesSet() throws Exception {
      Assert.notNull(bibliothequeXMLPath, "'bibliothequeXMLPath' is required");

   }

   private File bibliothequeXMLPath;

   public void setBibliothequeXMLPath(File bibliothequeXMLPath) {
      this.bibliothequeXMLPath = bibliothequeXMLPath;
   }

   @Override
   public void open(ExecutionContext executionContext)
         throws ItemStreamException {

      OutputStream libraryXML;
      try {
         libraryXML = FileUtils.openOutputStream(bibliothequeXMLPath);
      } catch (IOException e) {
         throw new NestableRuntimeException(e);
      }

      XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

      try {
         writer = outputFactory.createXMLStreamWriter(libraryXML);
      } catch (XMLStreamException e) {
         new ItemStreamException(e);
      }

      try {
         headerWriter(writer);
      } catch (XMLStreamException e) {
         new ItemStreamException(e);
      }

   }

   @Override
   public void update(ExecutionContext executionContext)
         throws ItemStreamException {
      // TODO Auto-generated method stub

   }

   private void headerWriter(XMLStreamWriter writer) throws XMLStreamException {

      writer.writeStartDocument("UTF-8", "1.0");

      writer.writeStartElement(B_PREFIX, "bibliotheque", B_NAMESPACE);
      writer.writeNamespace(B_PREFIX, B_NAMESPACE);
      writer.writeNamespace(REF_PREFIX, REF_NAMESPACE);

      // balise adresse
      writer.writeStartElement(B_PREFIX, "adresse", B_NAMESPACE);
      writer.writeCharacters("adresse de la bibliotheque");
      writer.writeEndElement();

      writer.writeStartElement(B_PREFIX, "livres", B_NAMESPACE);

   }

   private void footerWriter(XMLStreamWriter writer) throws XMLStreamException {

      writer.writeEndElement();
      writer.writeEndDocument();

   }

   private void itemWriter(Livre livre, XMLStreamWriter writer)
         throws XMLStreamException {

      // ouverture de la balise livre
      writer.writeStartElement(REF_PREFIX, "livre", REF_NAMESPACE);
      writer.writeAttribute("id", ObjectUtils.toString(livre.getIdentifiant(),
            "nc"));

      // balise titre
      writer.writeStartElement(REF_PREFIX, "titre", REF_NAMESPACE);
      writer.writeCharacters(livre.getTitre());
      writer.writeEndElement();

      // balise auteur
      writer.writeStartElement(REF_PREFIX, "auteur", REF_NAMESPACE);
      writer.writeCharacters(livre.getAuteur());
      writer.writeEndElement();

      writer.writeEndElement();

   }

}
