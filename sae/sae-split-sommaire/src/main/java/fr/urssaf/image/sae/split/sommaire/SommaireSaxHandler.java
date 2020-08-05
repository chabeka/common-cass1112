/**
 *
 */
package fr.urssaf.image.sae.split.sommaire;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author AC75094939
 */
class SommaireSaxHandler extends DefaultHandler {

  private Transformer transformer;

  private DOMSource source;

  // counts number of sommaire created
  private int sommaireCount = 0;

  private int documentsCount = 0;

  // data line buffer (is reset when the file is split)
  private StringBuilder recordRowDataLines = new StringBuilder();

  private String headerSommaire = new String();

  private String batchMode = new String();

  private String restitUuid = new String();

  boolean checkModeBatch = false;

  boolean checkRestitUuid = false;

  // temporary variables used for the parser events
  private String currentElement = null;

  private String currentDocumentData = null;

  private String attributes;

  @Override
  public void startDocument() throws SAXException {
    final File dir = new File(SAXSplitSommaire.DIRECTORY);
    if (!dir.exists()) {
      dir.mkdir();
    }
  }

  @Override
  public void startElement(final String uri, final String localName, final String qName, final Attributes atts) throws SAXException {

    currentElement = qName;

    if (qName.equals("som:sommaire")) {

      // Get attributes
      headerSommaire = headerSommaire + "<" + qName;

      attributes = new String();
      if (atts != null && atts.getLength() > 0) {
        for (int i = 0; i < atts.getLength(); i++) {
          attributes += " " + atts.getQName(i) + "=" + "\"" + atts.getValue(i) + "\"";
        }
      }
      if (attributes != null && attributes.length() > 0) {
        headerSommaire = headerSommaire + attributes;
      }
      headerSommaire = headerSommaire + ">";

    } else if (qName.equals("som:batchMode")) {
      batchMode += "<" + qName + ">";
      checkModeBatch = true;

    } else if (qName.equals("som:restitutionUuids")) {
      restitUuid += "<" + qName + ">";
      checkRestitUuid = true;
    }

    else if (!qName.equals("som:documents")) {
      recordRowDataLines.append("<" + qName);
      attributes = new String();
      if (atts != null && atts.getLength() > 0) {
        for (int i = 0; i < atts.getLength(); i++) {
          attributes = attributes + " " + atts.getQName(i) + "=" + "\"" + atts.getValue(i) + "\"";
        }
      }
      if (attributes != null && attributes.length() > 0) {
        recordRowDataLines.append(attributes);
      }
      recordRowDataLines.append(">");
    }

  }

  @Override
  public void endElement(final String uri, final String localName, final String qName) throws SAXException {
    if (qName.equals("som:documents")) { // no more records - save last file here!
      try {
        saveFragment();
      }
      catch (TransformerFactoryConfigurationError | TransformerException | ParserConfigurationException e) {
        e.printStackTrace();
      }
      catch (final IOException ex) {
        throw new SAXException(ex);
      }

    }

    else if (qName.equals("som:batchMode")) {
      batchMode += "</" + qName + ">";

    } else if (qName.equals("som:restitutionUuids")) {
      restitUuid += "</" + qName + ">";
    } else if (qName.equals("somres:document")) { // one document finished - save in buffer
      documentsCount += 1;

      recordRowDataLines.append("</" + qName + ">");

      if (documentsCount == SAXSplitSommaire.ITEMS_PER_FILE) {
        try {
          saveFragment();
        }
        catch (TransformerFactoryConfigurationError | ParserConfigurationException | TransformerException e) {
          e.printStackTrace();
        }
        catch (final IOException ex) {
          throw new SAXException(ex);
        }

      }
    } else {
      // qname inconnu
      recordRowDataLines.append("</" + qName + ">");
    }
    currentElement = null;
  }

  @Override
  public void characters(final char[] ch, final int start, final int length) throws SAXException {

    if (checkModeBatch) {
      batchMode += new String(ch, start, length);
      checkModeBatch = false;
    } else if (checkRestitUuid) {
      restitUuid += new String(ch, start, length);
      checkRestitUuid = false;
    } else if (currentElement != null) {
      currentDocumentData = new String(ch, start, length);
      // TODO
      recordRowDataLines.append(currentDocumentData.replaceAll("&", "&amp;"));
    }

  }

  /**
   * Saves a new sommaire
   *
   * @throws ParserConfigurationException
   * @throws TransformerFactoryConfigurationError
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public void saveFragment() throws SAXException, IOException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {

    StringBuilder fileContent = new StringBuilder();
    fileContent.append(headerSommaire);

    fileContent.append(batchMode);
    fileContent.append(restitUuid);
    fileContent.append("<som:documents>");
    recordRowDataLines.append("</som:documents>");
    recordRowDataLines.append("</som:sommaire>");
    fileContent.append(recordRowDataLines);

    final Reader inputString = new StringReader(fileContent.toString());
    final BufferedReader inFromUser = new BufferedReader(inputString);

    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    final DocumentBuilder builder = dbf.newDocumentBuilder();

    final Document doc = builder.parse(new InputSource(inFromUser));
    source = new DOMSource(doc);
    transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    final StreamResult result = new StreamResult(new File(SAXSplitSommaire.DIRECTORY, "sommaire" + sommaireCount + ".xml"));
    transformer.transform(source, result);

    // final File fragment = new File(SAXSplitSommaire.DIRECTORY, "sommaire" + sommaireCount + ".xml");
    // final FileWriter out = new FileWriter(fragment);
    // out.write(fileContent.toString());
    // out.flush();
    // out.close();

    // Reset
    recordRowDataLines = new StringBuilder();
    fileContent = new StringBuilder();
    documentsCount = 0;
    ++sommaireCount;

  }

}
