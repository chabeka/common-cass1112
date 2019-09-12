package sae.integration.util;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
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
      // Ne marche pas. Bug JAXB ? Solution de contournement trouvée ici : https://code-examples.net/en/q/13a20f1
      /*
      final JAXBContext context = JAXBContext.newInstance(ResultatsType.class);
      final StringReader reader = new StringReader(xml);
      return (ResultatsType) context.createUnmarshaller().unmarshal(reader);
       */

      final StringReader reader = new StringReader(xml);
      final JAXBContext jaxbContext = JAXBContext.newInstance(sae.integration.xml.modele.ObjectFactory.class);
      final ResultatsType result = ((JAXBElement<ResultatsType>) jaxbContext.createUnmarshaller().unmarshal(reader)).getValue();
      return result;
   }

}
