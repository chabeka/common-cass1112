package fr.urssaf.image.sae.split.sommaire;

import java.io.File;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author AC75094939
 *         Permet de découper un sommaire de quelques centaines de Mo,
 */
public class SplitSommaire {

  // Paramètres de découpage à définir
  final static String INPUT_FILE_PARAM = "c:\\split_sommaire\\sommaire.xml";

  final static String RESULT_SPLIT_PATH = "c:\\split_sommaire\\";

  final static int ITEMS_PER_FILE = 100;

  final static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

  /**
   * @param args
   * @throws Exception
   */
  public static void main(final String[] args) throws Exception {
    // Parsing document
    final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    final Document doc = dBuilder.parse(new File(INPUT_FILE_PARAM));
    // Split the sommaire of current document
    splitSommaire(doc, ITEMS_PER_FILE, RESULT_SPLIT_PATH);

  }

  /**
   * Fonction principale de découpage de fichier sommaire
   *
   * @param doc
   * @param itemsPerFile
   * @param resultSplitPath
   * @throws Exception
   */
  public static void splitSommaire(final Document doc, final int itemsPerFile, final String resultSplitPath) throws Exception {

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    doc.getDocumentElement().normalize();
    // Get Sommaire Node
    final Node mainSommaireNode = doc.getElementsByTagName("som:sommaire").item(0);
    // Get BatchMode node
    final Node batchModeNode = doc.getElementsByTagName("som:batchMode").item(0);
    // Get RestitutionUuuid Node
    final Node restitutionUuidsNode = doc.getElementsByTagName("som:restitutionUuids").item(0);

    Document currentDoc;
    try {
      currentDoc = dbFactory.newDocumentBuilder().newDocument();
      Node batchMode = currentDoc.createElement("som:batchMode");
      batchMode.setTextContent(batchModeNode.getTextContent());
      Node restitutionUuids = currentDoc.createElement("som:restitutionUuids");
      restitutionUuids.setTextContent(restitutionUuidsNode.getTextContent());
      Element principalNode = currentDoc.createElement("som:sommaire");
      final NamedNodeMap attributes = mainSommaireNode.getAttributes();
      copyNodeAttributes(principalNode, attributes);
      // Get root node (documents)
      Node rootNode = currentDoc.createElement("som:documents");
      final NodeList listOfDocuments = doc.getElementsByTagName("somres:document");
      int fileNumber = 0;

      File currentFile = new File(resultSplitPath + "sommaire" + fileNumber + ".xml");

      for (int i = 1; i <= listOfDocuments.getLength(); i++) {
        final Node imported = currentDoc.importNode(listOfDocuments.item(i - 1), true);
        rootNode.appendChild(imported);

        if (i % itemsPerFile == 0) {
          // principalNode.setNodeValue(mainSommaireNode.getAttributes().toString());
          principalNode.appendChild(batchMode);
          principalNode.appendChild(restitutionUuids);
          principalNode.appendChild(rootNode);
          // ecriture du sommaire
          writeToFile(principalNode, currentFile);

          // réinit des nodes
          batchMode = currentDoc.createElement("som:batchMode");
          batchMode.setTextContent(batchModeNode.getTextContent());
          restitutionUuids = currentDoc.createElement("som:restitutionUuids");
          restitutionUuids.setTextContent(restitutionUuidsNode.getTextContent());
          principalNode = currentDoc.createElement("som:sommaire");

          copyNodeAttributes(principalNode, attributes);
          rootNode = currentDoc.createElement("som:document");
          currentFile = new File(resultSplitPath + "sommaire" + (++fileNumber) + ".xml");
        }
      }
      principalNode.appendChild(batchMode);
      principalNode.appendChild(restitutionUuids);
      principalNode.appendChild(rootNode);
      writeToFile(principalNode, currentFile);
    }
    catch (final ParserConfigurationException e) {
      System.out.println("Erreur de parsing du fichier sommaire en entrée: " + e.getStackTrace());
      e.printStackTrace();
    }

  }

  /**
   * permet de recopier les attributs passée en paramètre dans le noeud principalNodeCopy passé en paramètre
   *
   * @param principalNode
   * @param mainSommaireNode
   */
  private static void copyNodeAttributes(final Element principalNodeCopy, final NamedNodeMap attributes) {
    Attr node;
    for (int i = 0; i < attributes.getLength(); i++) {
      node = (Attr) attributes.item(i).cloneNode(true);
      principalNodeCopy.setAttribute(node.getName(), node.getValue());
    }

  }

  /**
   * Ecriture node contenant le sommaire dans le file passé en paramètre
   * 
   * @param node
   * @param file
   * @throws Exception
   */
  private static void writeToFile(final Node node, final File file) throws Exception {
    final Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    transformer.transform(new DOMSource(node), new StreamResult(new FileWriter(file)));
  }

}