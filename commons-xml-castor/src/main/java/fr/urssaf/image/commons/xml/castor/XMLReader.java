package fr.urssaf.image.commons.xml.castor;

import java.io.IOException;
import java.io.Reader;

import org.castor.xml.UnmarshalListener;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;

public class XMLReader<T> {

   private final XMLContext context;

   private final Class<T> type;

   public XMLReader(Class<T> type, String mappingFile) throws MappingException,
         IOException {

      this.type = type;

      Mapping mapping = new Mapping();

      mapping.loadMapping(mappingFile);
      // initialize and configure XMLContext
      context = new XMLContext();
      context.addMapping(mapping);
   }

   @SuppressWarnings("unchecked")
   public T read(Reader fileReader) throws MarshalException,
         ValidationException {

      Unmarshaller unmarshaller = context.createUnmarshaller();
      
      unmarshaller.setUnmarshalListener(new UnmarshalListener() {

         @Override
         public void attributesProcessed(Object target, Object parent) {
            // System.out.println("1 " + target + " " + parent);

         }

         @Override
         public void fieldAdded(String fieldName, Object parent, Object child) {
           // System.out.println("2 "+fieldName+" "+parent+" "+child);

         }

         @Override
         public void initialized(Object target, Object parent) {
           // System.out.println("3 " + target + " " + parent);

         }

         @Override
         public void unmarshalled(Object target, Object parent) {
           // System.out.println("4 " + target + " " + parent);

         }

      });
      unmarshaller.setClass(this.type);

      return (T) unmarshaller.unmarshal(fileReader);
   }

}
