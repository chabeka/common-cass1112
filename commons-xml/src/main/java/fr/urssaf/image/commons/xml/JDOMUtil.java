package fr.urssaf.image.commons.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 
 * Fonctions utilitaires pour la manipulation de XML avec JDOM 
 *
 */
public final class JDOMUtil {

   
   private JDOMUtil()
   {
      
   }
   
   
	/**
	 * Persiste un objet JDOM Document dans un fichier.<br>
	 * NB : le formatage du XML en sortie consiste en :<br>
	 * <ul>
	 *   <li>l'indentation est de 2 espaces</li>
	 *   <li>les retours charriots sont des CRLF (\r\n)</li>
	 *   <li>une ligne vide supplémentaire est ajoutée à la fin</li>
	 * </ul>
	 * 
	 * @param document l'objet représentant le document XML à persister
	 * @param file l'objet fichier dans lequel effectuer la persistance
	 * @param encoding l'encoding de sortie ("UTF-8", "ISO-8859-1", ...)
	 * 
	 * @throws IOException en cas d'erreur d'E/S
	 */
	public static void writeToFile(Document document, File file, String encoding)
	throws IOException {
	   
	   // Définition du format de sortie
	   Format format = Format.getPrettyFormat();
	   format.setEncoding(encoding);
	   
	   // Pour info, le séparateur de ligne utilisé est : format.getLineSeparator()
	   // Par défaut : "\r\n" 
	   
	   XMLOutputter out = new XMLOutputter(format);
	   
	   FileOutputStream fos = new FileOutputStream(file);
	   try {
	      out.output(document, fos);
	   }
	   finally {
	      if (fos!=null) {
	         fos.close();
	      }
	   }
	   
	}
	
	
	/**
	 * Persiste un objet JDOM Document dans une chaîne de caractères.<br>
    * NB : le formatage du XML en sortie consiste en :<br>
    * <ul>
    *   <li>l'indentation est de 2 espaces</li>
    *   <li>les retours charriots sont des CRLF (\r\n)</li>
    *   <li>une ligne vide supplémentaire est ajoutée à la fin</li>
    * </ul>
	 * 
	 * @param document l'objet représentant le document XML
	 * @param encoding l'encoding de sortie ("UTF-8", "ISO-8859-1", ...)
	 * 
	 * @return le XML sous forme de chaîne de caractères
	 * 
	 * @throws IOException en cas d'erreur d'E/S 
	 */
	public static String writeToString(Document document, String encoding)
	throws IOException
   {
      XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
      StringWriter stringWriter = new StringWriter(0);
      out.output(document, stringWriter);
      return stringWriter.toString();
   }

	
	/**
	 * Lit du XML à partir d'un fichier et le restitue dans un objet JDOM Document
	 * 
	 * @param fichierXML le chemin complet du fichier XML à lire
	 * @param encoding l'encoding à utiliser pour la lecture du fichier ("UTF-8", "ISO-8859-1", ...)
	 * 
	 * @return l'objet JDOM Document représentant de le document XML
	 * 
	 * @throws JDOMException en cas d'erreur de parsing du XML
	 * @throws IOException en cas d'erreur d'E/S
	 */
	public static Document readFromFile(File fichierXML,String encoding)
	throws
	   JDOMException, 
	   IOException {
	   
	   SAXBuilder sxb = new SAXBuilder();
	   
	   FileInputStream fis = new FileInputStream(fichierXML);
	   try {
	      
	      InputStreamReader isr = new InputStreamReader(fis,encoding);
	      try {
	         
	         return sxb.build(isr);
	         
	      }
	      finally {
	         if (isr!=null) {
	            isr.close();
	         }
	      }
	      
	   }
	   finally {
	      if (fis!=null) {
	         fis.close();
	      }
	   }

	}
	
	
	/**
	 * Lit du XML à partir d'une chaîne de caractères UTF8 et le restitue dans un objet JDOM Document
	 * 
	 * @param xml le XML en chaîne de caractères
	 * 
	 * @return l'objet JDOM Document représentant de le document XML
	 *  
	 * @throws JDOMException en cas d'erreur de parsing du XML
    * @throws IOException en cas d'erreur d'E/S
	 */
	public static Document readFromStringUTF8(String xml) throws JDOMException, IOException
	{
	   SAXBuilder sxb = new SAXBuilder();
      InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
      return sxb.build(inputStream);
	}
	

}
