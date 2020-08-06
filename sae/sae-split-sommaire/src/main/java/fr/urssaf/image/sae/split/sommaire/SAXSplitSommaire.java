/**
 *
 */
package fr.urssaf.image.sae.split.sommaire;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author AC75094939
 *         Split a huge sommaire file using SAXParser
 */
public class SAXSplitSommaire {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SAXSplitSommaire.class);

  // Change this to the directory where the files will be stored
  static String DIRECTORY = null;

  // Number of items by file
  static int ITEMS_PER_FILE = 10000;

  // Path of sommaire to split
  static String PATH_FILE_TO_SPLIT = "c:\\split_sommaire\\sommaire.xml";

  static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

  public static void main(final String[] args) throws ParserConfigurationException, SAXException, IOException {

    // Check arguments null values
    if (args == null || args[0] == null) {
      LOGGER.warn("Le repertoire de sortie de decoupage doit etre renseigne !");
      return;
    } else if (args[1] == null) {
      LOGGER.warn("Le chemin du fichier sommaire a decouper doit etre renseigne !");
      return;
    } else if (args[2] == null) {
      LOGGER.warn("Le nombre de documents par sommaire doit etre renseigne !");
      return;
    }
    // Assign values from parameters
    DIRECTORY = args[0];
    PATH_FILE_TO_SPLIT = args[1];
    final String nbrItems = args[2];
    ITEMS_PER_FILE = Integer.parseInt(nbrItems);

    // Parsing file
    final SAXParserFactory spf = SAXParserFactory.newInstance();
    final SAXParser sp = spf.newSAXParser();
    final XMLReader reader = sp.getXMLReader();
    reader.setContentHandler(new SommaireSaxHandler());
    reader.parse(new InputSource(new FileInputStream(new File(PATH_FILE_TO_SPLIT))));

  }

}