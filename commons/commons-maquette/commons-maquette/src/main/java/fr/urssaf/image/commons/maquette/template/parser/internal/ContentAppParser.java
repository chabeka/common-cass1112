package fr.urssaf.image.commons.maquette.template.parser.internal;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;


/**
 * Parser de la div "content-application" du template de la maquette. Extrait :
 * <ul>
 *    <li>
 *    La balise identifiée par <code>content-application</code>, qui
 *    est la &lt;div&gt; du template de la maquette dans laquelle il 
 *    faut insérer le contenu de le vue à décorer de l'application 
 *    cliente.
 *    </li>
 *    <li>
 *    Les balises &lt;noscript&gt;
 *    </li>
 *    
 * </ul>
 */
public final class ContentAppParser extends AbstractParser
{
	
   /**
    * La balise identifiée par "content-application"
    */
   private Element contentAppTag ;
	
   
   /**
    * Les balises &lt;noscript&gt;
    */
   private List<Element> noScriptTag ;
   
		
	/**
	 * Constructeur par défaut
	 */
	public ContentAppParser() {
		super();
	}
	
	
	/**
	 * Constructeur qui fait le parsing
	 * 
	 * @param source la source HTML
	 * @throws MissingSourceParserException si la source HTML à parser est manquante
    * @throws MissingHtmlElementInTemplateParserException si un élément est manquant dans la source HTML 
	 */
	public ContentAppParser(Source source) 
	throws 
	MissingSourceParserException, 
	MissingHtmlElementInTemplateParserException{
	   super();
	   doParse(source) ;
	}
	
	
	/**
	 * Renvoie la balise identifiée par "content-application"
	 * @return la balise identifiée par "content-application"
	 */
	public Element getContentAppTag() {
		return contentAppTag;
	}

	
	/**
	 * Renvoie les balises &lt;noscript&gt;
	 * @return les balises &lt;noscript&gt;
	 */
	public List<Element> getNoScriptTag() {
		return noScriptTag;
	}
	

	/**
	 * {@inheritDoc} 
	 */
	protected void doParse(Source source)
	throws MissingSourceParserException, 
	MissingHtmlElementInTemplateParserException
	{	
		if (source == null) {
		   throw new MissingSourceParserException("ContentApp") ;
		}
		else {
		   contentAppTag = doGetContentAppTag(source);
         noScriptTag = doGetNoScriptTag(source) ;
		   
		}
	}
	

	private Element doGetContentAppTag(Source source)
	throws MissingHtmlElementInTemplateParserException {
	   return getElementById(source, "content-application") ;
	}
	
	
	private List<Element> doGetNoScriptTag(Source source)
	throws MissingHtmlElementInTemplateParserException {
		
	   List<Element> elList = source.getAllElements(HTMLElementName.NOSCRIPT) ;
		
	   if (elList.isEmpty()) {
	      throw new MissingHtmlElementInTemplateParserException(HTMLElementName.NOSCRIPT);
	   }
	   
	   return elList ;
		
	}
	
}
