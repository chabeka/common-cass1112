package fr.urssaf.image.commons.maquette.template.parser.internal;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;


/**
 * Parser de la balise &lt;div id="header"&gt; du template de la maquette. En extrait :
 * <ul>
 *    <li>la balise identifiée par "logoimage" (&lt;img&gt;)</li>
 *    <li>la balise identifiée par "title-app" (&lt;h1&gt;)</li>
 *    <li>la balise identifiée par "logoappli" (&lt;img&gt;)</li>
 *    <li>la balise identifiée par "menu" (&lt;div&gt;)</li>
 *    <li>la balise identifiée par "minheight" (&lt;img&gt;)</li>
 * </ul>
 * 
 */
public final class DivHeaderParser extends AbstractParser
{
	
   
   /**
    * La balise identifiée par "logoimage"
    */
   private Element mainLogoTag ;
	
   
   /**
    * La balise identifiée par "title-app"
    */
   private Element titleTag ;
   
   
   /**
    * La balise identifiée par "logoappli"
    */
	private Element logoTag ;
	
	
	/**
	 * La balise identifiée par "menu"
	 */
	private Element menuTag ;
	
	
	/**
	 * La balise identifiée par "minheight"
	 */
	private Element minHeightImg ;
	
	
	/**
	 * Constructeur par défaut
	 */
	public DivHeaderParser() {
	   super();
	}
	
	
	/**
    * Constructeur qui fait le parsing
    * 
    * @param source la source HTML
    * @throws MissingSourceParserException si la source HTML à parser est manquante
    * @throws MissingHtmlElementInTemplateParserException si un élément est manquant dans la source HTML 
    */
	public DivHeaderParser(Source source)
	throws 
	MissingSourceParserException, 
	MissingHtmlElementInTemplateParserException {
		super();
	   doParse(source) ;
	}

	
	/**
	 * Renvoie la balise identifiée par "logoimage"
	 * @return La balise identifiée par "logoimage"
	 */
	public Element getMainLogoTag() {
		return mainLogoTag;
	}
	

	/**
	 * Renvoie la balise identifiée par "title-app"
	 * @return La balise identifiée par "title-app"
	 */
	public Element getTitleTag() {
		return titleTag;
	}

	
	/**
	 * Renvoie la balise identifiée par "logoappli"
	 * @return La balise identifiée par "logoappli"
	 */
	public Element getLogoTag() {
		return logoTag;
	}

	
	/**
	 * Renvoie la balise identifiée par "menu"
	 * @return La balise identifiée par "menu"
	 */
	public Element getMenuTag() {
		return menuTag;
	}
	
	
	/**
	 * Renvoie la balise identifiée par "minheight"
	 * @return La balise identifiée par "minheight"
	 */
	public Element getMinHeightImg() {
		return minHeightImg;
	}
	

	/**
	 * {@inheritDoc} 
	 */
	public void doParse(Source source)
	throws MissingSourceParserException, 
	MissingHtmlElementInTemplateParserException
	{	
		if( source == null ) {
		   throw new MissingSourceParserException("Header") ;
		}
		else {
		   mainLogoTag = getElementById(source, "logoimage");
         titleTag = getElementById(source, "title-app");
         logoTag = getElementById(source, "logoappli") ;
         menuTag = getElementById(source, "menu") ;
         minHeightImg = getElementById(source, "minheight") ;
		}
	}
	
	
}
