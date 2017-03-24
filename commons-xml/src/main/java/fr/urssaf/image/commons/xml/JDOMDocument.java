package fr.urssaf.image.commons.xml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * Permet de géréer un document XML avec JDom
 */
public class JDOMDocument {

   //dom
   private Document dom;
   
   // Le noeud racine
   private Element racine;
   
   /**
    * Le get du DOM
    * 
    * @return Le DOM
    */
   public final Document getDom() {
      return dom;
   }

   /**
    * Le get de l'Element racine du DOM
    * 
    * @return Element racine
    */
   public final Element getRacine() {
      return racine;
   }

   /**
    * Le seteur de la racine
    * 
    * @param racine L'élément racine à insérer
    */
   public final void setRacine(Element racine) {
      this.racine = racine;
      dom.setRootElement(racine);
   }

   /**
    * Constructeur simple
    */
   public JDOMDocument(){    
      dom=new Document();
   }
   
   /**
    * Constructeur avec ajout de la racine du noeud
    * 
    * @param nomRacine Nom du node racine
    */
   public JDOMDocument(String nomRacine) {
      dom=new Document();
      racine= new Element(nomRacine);
      dom.setRootElement(racine);
   }
   
   /**
    * Sauve le dom dans le fichier donné en UTF8
    * 
    * @param cheminFichier Le chemin du fichier
    * 
    * @throws IOException Si problème de gestion de fichier
    */
   public final void save(String cheminFichier) throws IOException {
      File file = new File(cheminFichier);
      JDOMUtil.writeToFile(dom, file, CharEncoding.UTF_8);
   }
   
   /**
    * Lecture du DOM à partir d'un fichier au format UTF8
    * 
    * @param cheminFichier    Le chemin du fichier
    * 
    * @throws JDOMException   Le fichier xml est mal structuré
    * @throws IOException     Le fichier n'est pas lisible
    * 
    */
   public final void read(String cheminFichier) throws JDOMException, IOException {
      File file = new File(cheminFichier);
      dom = JDOMUtil.readFromFile(file,CharEncoding.UTF_8);
      racine = dom.getRootElement();
   }
   
   
   /**
    * Creation d'un Element avec ses attributs
    *
    * @param pere    Le noeud père
    * @param nom     Le nom de l'élément à créer
    * @param contenu Le contenu textuel de l'élément (peut être null)
    * @param attrib  La liste des attributs (peut être null)
    * 
    * @return L'élement créé
    */
   public final Element createElement( Element pere, String nom, String contenu, Map<String, String> attrib){
      // Crée l'élément
      Element elt = new Element(nom);
      
      // Si le contenu textuel existe l'ajouter
      if ( StringUtils.isNotEmpty(contenu)){
         elt.addContent(contenu);
      }
      
      // Ajoute l'élément au père
      pere.addContent(elt);
      
      // Création des attributs
      if ( attrib != null ) {         
         for ( Map.Entry<String, String> entry : attrib.entrySet() ){
            elt.setAttribute(entry.getKey(),entry.getValue());
         }
      }
      return elt;
   }
   
   /**
    * Selectionne Le premier noeud correspondant au xPath relativement au noeud reference
    * 
    * @param reference Le noeud à partir duquel appliquer le XPath
    * @param xPath La requète xPath
    * 
    * @return L'élément sélectionné ou null
    * 
    * @throws JDOMException en cas d'xPath mall formé
    */
   public final Element selectSingleNode(Element reference, String xPath) throws JDOMException {
      if (reference == null ){
         throw new IllegalArgumentException("reference");
      }
      
      if ( StringUtils.isEmpty(xPath)) {
         throw new IllegalArgumentException("xPath");
      }
      
      XPath query = XPath.newInstance(xPath);
      return (Element)query.selectSingleNode(reference);
   }
   
   /**
    * Selectionne le premier élément correspondant au xPath à partir du noeud racine
    * 
    * @param xPath Le chemin XPath
    * 
    * @return Element Le premier élément qui correspond au XPath
    * 
    * @throws JDOMException Si la requête est mal formée
    */
   public final Element selectSingleNode(String xPath) throws  JDOMException {
      return selectSingleNode(this.racine, xPath);
   }
   
   
   /**
    *  Renvoie une liste d'objets coorespondant au XPath appliqué au noeud de référence.
    *  Cest objets peuvent être de type :
    *  <ul>
    *    <li>Element,</li> 
    *    <li>Attribute,</li>
    *    <li>Text,</li>
    *    <li>CDATA,</li>
    *    <li>Comment ou</li>
    *    <li>des valeurs (String, Double)</li>
    *  </ul>
    *  
    * @param reference Le noeud à partir duquel on applique le XPath
    * @param xPath Le XPath
    * 
    * @return Une liste d'objets Element, Attribute, Text, CDATA, Comment ou de valeurs (String, Double) selon la requête du XPath.
    * 
    * @throws JDOMException Requête XPath mal formée
    */
   @SuppressWarnings("unchecked")
   public final <T> List<T> selectNodes(Element reference, String xPath) throws JDOMException{
      if (reference == null ){
         throw new IllegalArgumentException("reference");
      }
      
      if ( StringUtils.isEmpty(xPath)) {
         throw new IllegalArgumentException("xPath");
      }

      XPath query = XPath.newInstance(xPath);
      return query.selectNodes(reference);
   }
   
   /**
    *  Renvoie une liste d'objets coorespondant au XPath appliqué au noeud racine du DOM.
    *  Cest objets peuvent être de type :
    *  <ul>
    *    <li>Element,</li> 
    *    <li>Attribute,</li>
    *    <li>Text,</li>
    *    <li>CDATA,</li>
    *    <li>Comment ou</li>
    *    <li>des valeurs (String, Double)</li>
    *  </ul>
    *  
    * @param xPath Le XPath à appliquer à la racine
    * 
    * @return Une liste d'objets Element, Attribute, Text, CDATA, Comment ou de valeurs (String, Double) selon la requête du XPath.
    * 
    * @throws JDOMException Requête XPath mal formée
    */
   public final <T> List<T> selectNodes(String xPath) throws JDOMException {
      return selectNodes(this.racine, xPath);
   }
}
