package fr.urssaf.image.commons.spring.batch.support.callback;

import java.io.IOException;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.stereotype.Component;

@Component
public class XMLFooterCallback implements StaxWriterCallback {

   @Override
   public void write(XMLEventWriter writer) throws IOException {

      XMLEventFactory factory = XMLEventFactory.newInstance();

      try {
         writer.add(factory.createEndElement("", "", "livres"));

      } catch (XMLStreamException e) {
         throw new NestableRuntimeException(e);
      }

   }

}
