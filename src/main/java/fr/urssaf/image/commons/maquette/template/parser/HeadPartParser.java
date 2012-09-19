package fr.urssaf.image.commons.maquette.template.parser;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;

/**
 * Parse la balise &lt;head&gt; d'une source HTML
 */
public final class HeadPartParser
{
	
   private Element headTag ;
	private Element titleTag ;
	private List<Element> metaTag ;
	private List<Element> linkTag ;
	private List<Element> styleTag ;
	private List<Element> scriptTag ;
	
	
	/**
	 * Constructeur par défaut
	 */
	public HeadPartParser() {
		// rien à faire
	}
	
	
	/**
	 * Constructeur qui effectue le parsing
	 * @param source la source HTML
	 * @throws MissingSourceParserException si la source HTML à parser est manquante
    * @throws MissingHtmlElementInTemplateParserException si un élément est manquant dans la source HTML 
	 */
	public HeadPartParser(Source source)
	throws 
	MissingSourceParserException, 
	MissingHtmlElementInTemplateParserException {
		doParse(source) ;
	}

	
	/**
	 * Renvoie la balise &lt;head&gt;
	 * @return la balise &lt;head&gt;
	 */
	public Element getHeadTag() {
		return headTag;
	}

	
	/**
	 * Renvoie la balise &lt;title&gt; de la balise &lt;head&gt;
	 * @return la balise &lt;title&gt; de la balise &lt;head&gt;
	 */
	public Element getTitleTag() {
		return titleTag;
	}

	
	/**
	 * Renvoie les balises &lt;meta&gt; de la balise &lt;head&gt;
	 * @return les balises &lt;meta&gt; de la balise &lt;head&gt;
	 */
	public List<Element> getMetaTag() {
		return metaTag;
	}

	
	/**
	 * Renvoie les balises &lt;link&gt; de la balise &lt;head&gt;
	 * @return the linkTag
	 */
	public List<Element> getLinkTag() {
		return linkTag;
	}

	
	/**
	 * Renvoie les balises &lt;style&gt; de la balise &lt;head&gt;
	 * @return les balises &lt;style&gt; de la balise &lt;head&gt;
	 */
	public List<Element> getStyleTag() {
		return styleTag;
	}
	

	/**
	 * Renvoie les balises &lt;script&gt; de la balise &lt;head&gt;
	 * @return les balises &lt;script&gt; de la balise &lt;head&gt;
	 */
	public List<Element> getScriptTag() {
		return scriptTag;
	}

	
	/**
	 * Parse les éléments de la balise &lt;head&gt; contenu dans la source HTML passé
	 * en paramètre
	 * 
	 * @param source la source HTML
	 * @throws MissingSourceParserException si la source HTML à parser est manquante
	 * @throws MissingHtmlElementInTemplateParserException si un élément est manquant dans la source HTML
	 */
	public void doParse(Source source)
	throws
	MissingSourceParserException, 
	MissingHtmlElementInTemplateParserException
	{	
		if( source == null ) {
		   throw new MissingSourceParserException("HeadPart") ;
		}
		else {
			
		   // -------------------------------------------------------------------------
         // Extrait la balise <head>
         // -------------------------------------------------------------------------
         
         headTag = source.getFirstElement(HTMLElementName.HEAD);
         if(headTag==null) {
            throw new MissingHtmlElementInTemplateParserException(HTMLElementName.HEAD);
         }
               
         
         // -------------------------------------------------------------------------
         // Extrait les parties de la balise <head>
         // -------------------------------------------------------------------------
         
         // La balise <title>
         titleTag = headTag.getFirstElement(HTMLElementName.TITLE);
         
         // Les balises <meta>
         metaTag = headTag.getAllElements(HTMLElementName.META);
         
         // Les balises <link>
         linkTag = headTag.getAllElements(HTMLElementName.LINK);
         
         // Les balises <style>
         styleTag = headTag.getAllElements(HTMLElementName.STYLE);
         
         // Les balises <script>
         scriptTag = headTag.getAllElements(HTMLElementName.SCRIPT);
		   
		}
	}
	
}
