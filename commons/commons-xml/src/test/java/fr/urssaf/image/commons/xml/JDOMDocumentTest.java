package fr.urssaf.image.commons.xml;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.junit.Test;

import fr.urssaf.image.commons.util.tempfile.TempFileUtils;

/**
 * Teste les utilitaires sur le document JDOM
 * 
 */
@SuppressWarnings("PMD.TooManyMethods")
public class JDOMDocumentTest {
   private static final String FILE_NAME = "src/test/resources/bibliotheque_jdom.xml";

   private static final String TEST = "test";
   /*
    * Teste les constructeurs du document
    */
   @Test
   public void constructeurSimpleTest() {
      JDOMDocument dom = new JDOMDocument();

      dom.setRacine(new Element(TEST));

      assertNotNull("Le DOM est null", dom.getRacine());
      assertEquals("Noeud racine incorrect", dom.getRacine().getName(), TEST);
   }

   /**
    * Teste le constructeur qui crée un noeud racine
    */
   public void constructeurAvecRacineTest() {
      JDOMDocument dom = new JDOMDocument(TEST);
      assertNotNull("Le DOM est null", dom.getRacine());
      assertEquals("Noeud racine incorrect", dom.getRacine().getName(), TEST);
   }

   /**
    * Renvoie un fichier XML temporaire
    * 
    * @return File du fichier XML temporaire
    */
   private String getTemporyFileName() {
      // Obtention d'un nom de fichier temporaire
      String fichierTest = TempFileUtils.getFullTemporaryFileName("xml");

      return fichierTest;
   }

   /**
    * Test de lecture d'un fichier UTF8 pour initialiser le DOM
    * 
    * @throws IOException
    * @throws JDOMException
    */
   @Test
   public void readWriteTest() throws JDOMException, IOException {
      JDOMDocument dom = new JDOMDocument();

      // On lit le fichier dans les tests
      dom.read(FILE_NAME);
      assertNotNull("lecture du fichier de test" + FILE_NAME + " incorrecte !",
            dom.getDom());

      // On l'écrit
      String cheminFichier = getTemporyFileName();
      dom.save(cheminFichier);

      try {
         // On commence à charger un XML valide depuis les ressources
         dom = new JDOMDocument();

         dom.read(cheminFichier);
      } finally {
         // On flingue le fichier temporaire dans tous les cas
         File file = new File(cheminFichier);
         file.delete();
      }

      assertNotNull("lecture du fichier " + cheminFichier + " incorrecte !",
            dom.getDom());
  
      //Création du XPath
      XPath query = XPath.newInstance("/bibliotheque/livre");
      
      // Récupère la liste des livres
      Element racine = dom.getRacine();
      List<?> liste=query.selectNodes(racine);
      
      assertEquals("Le nombre de noeuds attendu est 2 (on en trouve " + ((Integer)liste.size()).toString()+")", liste.size(), 2);
   }
   
   /**
    * Crée une arborescence minima à parcourir avec des XPath
    * 
    * @return Le DOM rempli
    */
   private JDOMDocument creationArboDOMmini() {
      JDOMDocument dom = new JDOMDocument(TEST);
      // Création d'un noeud avec un contenu textuel uniquement
      Element monNoeud=dom.createElement(dom.getRacine(), "node", "Contenu Textuel", null);
      
      HashMap<String,String> attr = new HashMap<String,String>();
      attr.put("attribut1", "valeur 1");
      attr.put("attribut2", "valeur 2");
      attr.put("attribut3", "valeur 3");

      // Création d'un sous noeud avec des attributs et sans contenu textuel
      Element monSousNoeud=dom.createElement(monNoeud, "sous_node", null, attr);
      attr.clear();
      attr.put("attribut_1", "valeur 3.1");
      attr.put("attribut_2", "valeur 3.2");
      attr.put("attribut_3", "valeur 3.3");
  
      dom.createElement(monSousNoeud, "niveau_3", "Texte de niveau 3", attr);
      return dom;
   }
   
   
   @Test
   public void createElementTest() throws JDOMException{
      JDOMDocument dom = creationArboDOMmini();
      
      //Création du XPath
      XPath query = XPath.newInstance("//niveau_3/@attribut_2");
      String valeur = query.valueOf(dom.getRacine());
      
      assertEquals("La valeur de l'attribut 2 du noeud de niv 3 est fausse", valeur, "valeur 3.2");
      
      query=XPath.newInstance("//niveau_3");
      valeur = query.valueOf(dom.getRacine());
      
      assertEquals("La valeur textuelle du noeud de niv 3 est fausse", valeur, "Texte de niveau 3");

   }
   
   @Test
   public final void selectSingleNodeTest() throws IllegalArgumentException, JDOMException{
      JDOMDocument dom = creationArboDOMmini();
      
      Element elt = dom.selectSingleNode("//niveau_3");
      
      String valeur = elt.getAttributeValue("attribut_2");
      
      assertEquals("La valeur de l'attribut 2 du noeud niveau_3 est fausse !", valeur, "valeur 3.2");
   }
   
   @Test
   public final void selectNodesTest() throws IllegalArgumentException, JDOMException{
      JDOMDocument dom = creationArboDOMmini();
      
      HashMap<String,String> attr = new HashMap<String,String>();
      attr.put("attribut1", "valeur 2_1");
      attr.put("attribut2", "valeur 2_2");
      attr.put("attribut3", "valeur 2_3");

      dom.createElement(dom.getRacine(),"node2", "Contenu textuel 2", attr);
      
      List<Element> liste = dom.selectNodes("/" + TEST + "/*");
      
      assertEquals("La liste des éléments sous la racine n'est pas bonne !", liste.size(), 2);
      
      assertEquals("Le premier noeud n'est pas 'node' !",liste.get(0).getName(), "node");
      assertEquals("Le deuxième noeud n'est pas 'node2'", liste.get(1).getName(), "node2");
   }
   
   @Test( expected=IllegalArgumentException.class)
   public final void selectNodesArgIllegalTest() throws JDOMException {
      JDOMDocument dom = creationArboDOMmini();
      dom.selectNodes(null);
   }
   
   @Test( expected=IllegalArgumentException.class)
   public final void selectNodesArgIllegal2Test() throws JDOMException {
      JDOMDocument dom = creationArboDOMmini();
      dom.selectNodes(null,"//test/*");
   }
   
   @Test( expected=IllegalArgumentException.class)
   public final void selectSingleNodeArgIllegalTest() throws JDOMException {
      JDOMDocument dom = creationArboDOMmini();
      dom.selectSingleNode("");
   }
   
   @Test( expected=IllegalArgumentException.class)
   public final void selectSingleNodeArgIllegal2Test() throws JDOMException {
      JDOMDocument dom = creationArboDOMmini();
      dom.selectSingleNode(null,"//test/*");
   }

   
}
