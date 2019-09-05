package sae.integration.util;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import sae.integration.xml.modele.ResultatsType;

public class XMLHelper {

   private XMLHelper() {
      // Classe statique
   }

   /**
    * Parse un fichier "resultats.xml"
    * 
    * @param xml
    *           contenu du fichier resutlats.xml
    * @return
    * @throws JAXBException
    */
   public static ResultatsType parseResultatsXML(final String xml) throws JAXBException {
      final JAXBContext context = JAXBContext.newInstance(ResultatsType.class);
      final StringReader reader = new StringReader(xml);
      return (ResultatsType) context.createUnmarshaller().unmarshal(reader);
   }

}
