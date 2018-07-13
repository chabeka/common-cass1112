package fr.urssaf.image.commons.maquette.template.parser.internal;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;

/**
 * Parser de la div "footer" du template de la maquette. En extrait :
 * <ul>
 *    <li>la balise identifiée par "providedby"</li>
 *    <li>la balise identifiée par "copyright"</li>
 * </ul>
 */
public final class FooterParser extends AbstractParser
{
	
   /**
    * La balise identifiée par "providedby"
    */
   private Element providedByTag ;
   
   
   /**
    * La balise identifiée par "copyright"
    */
	private Element copyrightTag ;
	
	
	/**
	 * Constructeur par défaut
	 */
	public FooterParser() {
		super();
	}
	
	
	/**
	 * Constructeur qui fait le parsing
	 * 
	 * @param source la source HTML
    * @throws MissingSourceParserException si la source HTML à parser est manquante
    * @throws MissingHtmlElementInTemplateParserException si un élément est manquant dans la source HTML 
	 */
	public FooterParser(Source source) 
	throws 
	MissingSourceParserException, 
	MissingHtmlElementInTemplateParserException {
	   super();
	   doParse(source) ;
	}

	/**
	 * Renvoie la balise identifiée par "providedby"
	 * @return La balise identifiée par "providedby"
	 */
	public Element getProvidedByTag() {
		return providedByTag;
	}

	
	/**
	 * Renvoie la balise identifiée par "copyright"
	 * @return La balise identifiée par "copyright"
	 */
	public Element getCopyrightTag() {
		return copyrightTag;
	}
	

	/**
	 * {@inheritDoc} 
	 */
	protected void doParse(Source source)
	throws MissingSourceParserException, 
	MissingHtmlElementInTemplateParserException
	{	
		if( source == null ) {
		   throw new MissingSourceParserException("Footer") ;
		}
		else {
		   providedByTag = getElementById(source, "providedby");
         copyrightTag = getElementById(source, "copyright") ;
		}
	}
	
	
}
