package fr.urssaf.image.sae.vi.util;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Classe utilitaires sur le XML
 * 
 * 
 */
public final class XMLUtils {

   private XMLUtils() {

   }

   /**
    * Parse un fichier xml en {@link Element}
    * 
    * @param identification
    *           chemin du fichier
    * @return fichier xml
    * @throws IOException
    *            exception levé par le fichier
    * @throws SAXException
    *            exception levé par le parsing
    */
   public static Element parse(String identification) throws IOException,
         SAXException {

      File file = new File(identification);
      return fr.urssaf.image.sae.saml.util.XMLUtils.parse(FileUtils
            .readFileToString(file, "UTF-8"));
   }

   /**
    * Renvoie la chaine de caractère
    * 
    * @param element
    *           xml
    * @return chaine de caractère du xml
    */
   public static String print(Element element) {

      Transformer transformer = fr.urssaf.image.sae.saml.util.XMLUtils
            .initTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(
            "{http://xml.apache.org/xslt}indent-amount", "4");

      return fr.urssaf.image.sae.saml.util.XMLUtils.print(element, "UTF-8",
            transformer);

   }
}
