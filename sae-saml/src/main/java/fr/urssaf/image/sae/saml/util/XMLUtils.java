package fr.urssaf.image.sae.saml.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.XmlStreamWriter;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Classe de manipulation courante sur le DOM w3c
 * 
 * 
 */
public final class XMLUtils {

  private XMLUtils() {

  }

  /**
   * parse une chaine de caractère instancier une objet DOM w3c
   * 
   * @param xml
   *           chaine de caractère du contenu xml
   * @return objet DOM w3c
   * @throws SAXException
   *            exception levé par le parsing
   */
  public static Element parse(final String xml) throws SAXException {

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);


    try {
      factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final InputStream input = new ByteArrayInputStream(xml.getBytes());
      return builder.parse(input).getDocumentElement();

    } catch (final IOException e) {
      throw new IllegalStateException(e);
    } catch (final ParserConfigurationException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * affichage du contenu d'un objet DOM w3c<br>
   * <br>
   * <a href="http://stackoverflow.com/questions/2325388/java-shortest-way-to-pretty-print-to-stdout-a-org-w3c-dom-document"
   * >source du code</a>
   * 
   * @param element
   *           objet w3c à afficher
   * @param encoding
   *           encodage de l'affichage
   * @param transformer
   *           objet pour transformer le w3c en string
   * @return chaine de caractère du w3c
   */
  public static String print(final Element element, final String encoding,
                             final Transformer transformer) {

    try {

      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      transformer.transform(new DOMSource(element), new StreamResult(
                                                                     new XmlStreamWriter(out, encoding)));

      return out.toString();
    } catch (final TransformerException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * 
   * @return instance d'un objet {@link Transformer}
   */
  public static Transformer initTransformer() {

    final TransformerFactory factory = TransformerFactory.newInstance();

    try {

      return factory.newTransformer();

    } catch (final TransformerConfigurationException e) {
      throw new IllegalStateException(e);
    }
  }

}
