package fr.urssaf.image.commons.xml.castor;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.castor.xml.XMLProperties;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.MarshalListener;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;

public class XMLWriter<T> {

   private final XMLContext context;

   public XMLWriter(String mappingFile) throws MappingException, IOException {

      Mapping mapping = new Mapping();
      mapping.loadMapping(mappingFile);

      context = new XMLContext();
      context.addMapping(mapping);
      context.setProperty(XMLProperties.USE_INDENTATION, "true");

   }

   public void write(T arg, Writer fileWriter) throws IOException,
         MarshalException, ValidationException {

      Marshaller marshaller = context.createMarshaller();

      marshaller.setMarshalListener(new MarshalListener() {

         @Override
         public void postMarshal(Object object) {
            // TODO Auto-generated method stub

         }

         @Override
         public boolean preMarshal(Object object) {
            // TODO Auto-generated method stub
            return true;
         }

      });

      marshaller.setWriter(fileWriter);
      marshaller.marshal(arg);

   }

   public void write(List<T> args, String rootElement, Writer fileWriter)
         throws IOException, MarshalException, ValidationException,
         MappingException {

      Marshaller marshaller = context.createMarshaller();
      marshaller.setWriter(fileWriter);
      marshaller.setRootElement(rootElement);
      marshaller.marshal(args);

   }

}
